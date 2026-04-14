// App.jsx — Root component.
//
// Manages the WebSocket connection, all top-level game state, and the
// high-level screen transitions (lobby → waiting → playing → game over).
//
// Key design note — stale closure problem:
//   ws.onmessage is assigned once inside connect() and never reassigned.
//   If it closed over React state variables directly (e.g. playerNumber),
//   it would always see their initial values, not the current ones.
//
//   The fix: store playerNumber in a ref (playerNumberRef) and update the
//   ref immediately whenever it changes.  ws.onmessage reads the ref, which
//   always holds the current value regardless of when onmessage was created.

import { useState, useEffect, useRef } from "react";
import Board from "./components/Board";
import Chat from "./components/Chat";
import FleetStatus from "./components/FleetStatus";

const SERVER_URL = "ws://localhost:8080";
const BOARD_SIZE = 10;

const emptyBoard = () =>
  Array.from({ length: BOARD_SIZE }, () => Array(BOARD_SIZE).fill(0));

const initialFleet = () =>
  ["Carrier", "Battleship", "Cruiser", "Submarine", "Destroyer"].map(
    (name, i) => ({ name, size: [5, 4, 3, 3, 2][i], sunk: false })
  );

export default function App() {

  const [nameInput, setNameInput]         = useState("");
  const [status, setStatus]               = useState("idle");
  // status values: "idle" | "connecting" | "waiting" | "playing" | "over"
  const [playerNumber, setPlayerNumber]   = useState(null);
  const [errorMsg, setErrorMsg]           = useState("");
  const [myBoard, setMyBoard]             = useState(emptyBoard());
  const [opponentBoard, setOpponentBoard] = useState(emptyBoard());
  const [myFleet, setMyFleet]             = useState(initialFleet());
  const [opponentFleet, setOpponentFleet] = useState(initialFleet());
  const [myTurn, setMyTurn]               = useState(false);
  const [winner, setWinner]               = useState(null);
  const [chatLog, setChatLog]             = useState([]);

  const wsRef           = useRef(null);
  const playerNumberRef = useRef(null);  // always-current copy — read this in onmessage

  const statusRef = useRef(status);

  // Keep the ref in sync with the state value.
  useEffect(() => { playerNumberRef.current = playerNumber; }, [playerNumber]);

  useEffect(() => {statusRef.current = status; }, [status]);

  // -------------------------------------------------------------------------
  // TODO: implement connect()
  //
  // - Create new WebSocket(SERVER_URL), store in wsRef.current
  // - Set status to "connecting"
  //
  // ws.onmessage:
  //   Parse event.data as JSON.  Dispatch on msg.type:
  //
  //   "ASSIGN"    → update playerNumberRef.current AND setPlayerNumber immediately
  //                 (updating the ref first ensures comparisons below are correct
  //                  even before React re-renders)
  //                 setStatus("waiting")
  //                 send READY with the player's name
  //
  //   "WAITING"   → setStatus("waiting")
  //
  //   "GAME_START"→ populate myBoard from msg.myBoard (2D array of 0s and 1s)
  //                 reset opponentBoard, fleets, winner
  //                 setMyTurn(msg.turn === playerNumberRef.current)  ← use the ref
  //                 setStatus("playing")
  //
  //   "SHOT_RESULT"→ update the correct board cell and fleet
  //                  use playerNumberRef.current to decide if you are the shooter
  //
  //   "TURN_CHANGE"→ setMyTurn(msg.turn === playerNumberRef.current)
  //
  //   "CHAT"      → append { from, text, time } to chatLog
  //
  //   "GAME_OVER" → setWinner, setStatus("over"), update final board
  //
  //   "OPPONENT_DISCONNECTED" → setErrorMsg, setStatus("over")
  //
  //   "ERROR"     → console.warn
  //
  // ws.onclose: clear wsRef, set an error if not already "over"
  // ws.onerror:  set error, reset status to "idle"
  // -------------------------------------------------------------------------
  const connect = () => {
    // TODO

    // prevent double-clicking from doing anything
    if (status === "connecting") return;

    // instantly set the status to connecting
    setStatus("connecting");

    // create the connection to the java server
    const ws = new WebSocket(SERVER_URL);

    // save the connection into the ref so fire and sendchat can see it
    wsRef.current = ws;

    // attach the message handler
    ws.onmessage = (event) => {
        const msg = JSON.parse(event.data);
        console.log("Received from server:", msg);

        switch (msg.type) {
            case "ASSIGN":
                // save player number
                setPlayerNumber(msg.playerNumber);
                playerNumberRef.current = msg.playerNumber; // update the ref so future closures see the correct value

                // immediately change UI
                setStatus("waiting");

                // tell the java server this player is ready
                const readyMsg = {
                    type: "READY",
                    name: nameInput
                };
                wsRef.current.send(JSON.stringify(readyMsg));
                break;

            case "WAITING":
                setStatus("waiting");
                break;

            case "GAME_START":
                setStatus("playing");
                // it's my turn if the server's starting turn number matches my number
                setMyTurn(msg.turn === playerNumberRef.current);
                setMyBoard(msg.myBoard);

                // reset the opponent's board and both fleets for a fresh game
                setOpponentBoard(emptyBoard());
                setMyFleet(initialFleet());
                setOpponentFleet(initialFleet());
                break;

            case "TURN_CHANGE":
                setMyTurn(msg.turn === playerNumberRef.current);
                break;

            case "CHAT":
                // add the message to the end of the chat log array
                setChatLog(prev => [...prev, {
                    from: msg.from,
                    text: msg.text,
                    time: new Date().toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'})
                }]);
                break;

            case "GAME_OVER":
                statusRef.current = "over";
                setStatus("over");
                setWinner(msg.winner);

                // update the boards so the unhit ships are revealed
                if (msg.winner === playerNumberRef.current) {
                    // I won. The final board belongs to the opponent, so reveal their ships
                    setOpponentBoard(msg.finalBoard);
                } else {
                    // I lost. the final board belongs to me, so update my own board
                    setMyBoard(msg.finalBoard);
                }

                break;

            case "OPPONENT_DISCONNECTED":
                statusRef.current = "over";
                setStatus("over");
                setWinner(playerNumberRef.current); // you win by default
                setErrorMsg("Opponent disconnected.");
                break;

            case "ERROR":
                setErrorMsg(msg.message);
                break;

            case "SHOT_RESULT":
                handleShotResult(msg);  // helper function


                break;

            default:
                console.warn("Unknown message type:", msg.type);
        }
    }

    ws.onclose = () => {
        wsRef.current = null;
        // 3. WRAP YOUR EXISTING ONCLOSE LOGIC IN THIS IF STATEMENT:
                  if (statusRef.current !== "over") { // Make sure it matches your exact game over status string
                      setErrorMsg("Opponent disconnected or server closed.");
                      setStatus("idle"); // Or however you handle sudden disconnects
                      }
    };

    ws.onerror = () => {
        setErrorMsg("Could not connect to server.");
        setStatus("idle");
    };



  };

  const handleShotResult = (msg) => {
    const isMe = msg.shooter === playerNumberRef.current;
    const cellValue = msg.hit ? 2 : 3;

    if (isMe) {
      // I fired — update my view of the opponent's board
      setOpponentBoard(prev => {
        const newBoard = prev.map(row => [...row]);
        newBoard[msg.row][msg.col] = cellValue;
        return newBoard;
      });
      if (msg.sunkShip) {
        setOpponentFleet(prev => prev.map(ship =>
          ship.name === msg.sunkShip ? { ...ship, sunk: true } : ship
        ));
      }
    } else {
      // Opponent fired — update my own board
      setMyBoard(prev => {
        const newBoard = prev.map(row => [...row]);
        newBoard[msg.row][msg.col] = cellValue;
        return newBoard;
      });
      if (msg.sunkShip) {
        setMyFleet(prev => prev.map(ship =>
          ship.name === msg.sunkShip ? { ...ship, sunk: true } : ship
        ));
      }
    }

    // System chat message for sunk ships — show for both players
    if (msg.sunkShip) {
      setChatLog(prev => [...prev, {
        from: "🤖 System",
        time: new Date().toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" }),
        text: isMe
          ? `You sunk the opponent's ${msg.sunkShip}!`
          : `Your ${msg.sunkShip} was sunk!`
      }]);
    }
  };




  useEffect(() => {
    return () => { if (wsRef.current) wsRef.current.close(); };
  }, []);

  // -------------------------------------------------------------------------
  // TODO: implement fire(row, col)
  //
  // Send {"type":"FIRE","row":row,"col":col}.
  // Check wsRef.current.readyState === WebSocket.OPEN before sending.
  // The Board component already gates clicks on interactive={myTurn}, so you
  // do not need to recheck myTurn here — but it doesn't hurt.
  // -------------------------------------------------------------------------
  const fire = (row, col) => {
    // TODO
    if (wsRef.current && wsRef.current.readyState === WebSocket.OPEN) {
        const msg = {
            type: "FIRE",
            row: row,
            col: col
        };
        wsRef.current.send(JSON.stringify(msg));
    }
  };

  // -------------------------------------------------------------------------
  // TODO: implement sendChat(text)
  // -------------------------------------------------------------------------
  const sendChat = (text) => {
    // TODO
    if (wsRef.current && wsRef.current.readyState === WebSocket.OPEN) {
        const msg = {
            type: "CHAT",
            text: text
        };
        wsRef.current.send(JSON.stringify(msg));
    }
  };

  // -------------------------------------------------------------------------
  // Render — skeleton provided; fill in the playing screen.
  // -------------------------------------------------------------------------

  if (status === "idle" || status === "connecting") {
    return (
      <div className="lobby">
        <h1>⚓ Battleship</h1>
        <p>Enter your name and connect to the server.</p>
        <input
          type="text"
          placeholder="Your name"
          value={nameInput}
          onChange={e => setNameInput(e.target.value)}
          onKeyDown={e => {
              if (e.key === "Enter") {
                  e.preventDefault(); // stop enter key from also clicking the button
                  connect();
                  }
              }}
          maxLength={20}
        />
        <button onClick={connect} disabled={status === "connecting"}>
          {status === "connecting" ? "Connecting…" : "Connect"}
        </button>
        {errorMsg && <p className="error">{errorMsg}</p>}
      </div>
    );
  }

  if (status === "waiting") {
    return (
      <div className="lobby">
        <h1>⚓ Battleship</h1>
        <p>You are <strong>Player {playerNumber}</strong>.</p>
        <p>Waiting for your opponent to connect…</p>
      </div>
    );
  }

  if (status === "over") {
    // TODO: render game-over screen (winner/loser, both boards, reload button)
    //return <div className="game-over"><h1>Game Over</h1></div>;
    const iWon = winner === playerNumber;
      return (
        <div className="game-over">
          <h1>{iWon ? "🏆 You Win!" : "💀 You Lose!"}</h1>
          <p style={{ color: "#90a4ae" }}>
            {iWon ? "You sunk all enemy ships!" : "All your ships were sunk."}
          </p>
          {errorMsg && <p className="error">{errorMsg}</p>}

          <div className="boards-row">
            <div>
              <h3>My Board</h3>
              <Board cells={myBoard} interactive={false} />
            </div>
            <div>
              <h3>Opponent's Board</h3>
              <Board cells={opponentBoard} interactive={false} />
            </div>
          </div>

          <button onClick={() => window.location.reload()}>Play Again</button>
        </div>
      );
  }

  // status === "playing"
  // TODO: render the main game screen:
  //   - header with turn indicator (use myTurn state)
  //   - My Board (interactive={false}) + FleetStatus
  //   - Opponent's Board (interactive={myTurn}, onFire={fire}) + FleetStatus
  //   - Chat panel
  //return <div className="game"><p>TODO: game screen</p></div>;

  return (

      <div className="game">
          <header>
              <h1>⚓ Battleship</h1>

              {/* Turn indicator */}
              <div className={`turn-indicator ${myTurn ? 'my-turn' : 'their-turn'}`}>
                  {myTurn ? "🟢 YOUR TURN" : "🔴 OPPONENT'S TURN"}
              </div>
          </header>

          <div className="main-layout">
              <div className="boards-section">

              {/* My side of the screen */}
              <div className="player-area">
                  {/* my board is never interactive */}
                  <Board
                    cells={myBoard}
                    interactive={false}
                  />
                  <FleetStatus
                    fleet={myFleet}
                    label="My Ships"
                  />
              </div>

              {/* Opponent's side of the screen */}
              <div className="player-area opponent-area">
                  <Board
                    cells={opponentBoard}
                    // only let me click if it is currently my turn
                    interactive={myTurn}
                    // when the board component registers a click, it will call fire(row, call)
                    onFire={fire}
                  />
                  <FleetStatus
                    fleet={opponentFleet}
                    label="Opponent's Ships"
                  />
              </div>
          </div>

          {/* Chat Panel on the right side */}
          <div className="chat-section">
              <Chat log={chatLog} onSend={sendChat} />
          </div>
      </div>
  </div>
  );
}

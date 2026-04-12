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

  // Keep the ref in sync with the state value.
  useEffect(() => { playerNumberRef.current = playerNumber; }, [playerNumber]);

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
  };

  // -------------------------------------------------------------------------
  // TODO: implement sendChat(text)
  // -------------------------------------------------------------------------
  const sendChat = (text) => {
    // TODO
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
          onKeyDown={e => e.key === "Enter" && connect()}
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
    return <div className="game-over"><h1>Game Over</h1></div>;
  }

  // status === "playing"
  // TODO: render the main game screen:
  //   - header with turn indicator (use myTurn state)
  //   - My Board (interactive={false}) + FleetStatus
  //   - Opponent's Board (interactive={myTurn}, onFire={fire}) + FleetStatus
  //   - Chat panel
  return <div className="game"><p>TODO: game screen</p></div>;
}

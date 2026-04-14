# Lab 5 Design Writeup — Networked Battleship

**Name(s): Aidan Gonzales**
**EID(s): AGG3498**
**Date submitted: 4/13/26**

---

## What This Document Is

2–3 pages of honest reflection on the decisions you made and the problems you hit.  Not a summary of the lab spec — we wrote the spec, we know what it says.

Full credit requires:

- Two specific design choices with a stated reason and a named alternative
- A complete message sequence diagram covering one full game
- Three concurrency questions answered with specific references to your code
- Two concrete bugs (symptom → cause → fix → lesson learned)
- A testing section that describes what you actually ran, not what you planned to run

Vague entries ("I synchronized the shared state") earn no credit. Entries like "I made `handleMessage` synchronized because both reader threads call it concurrently and I need turn/readyCount/boards to be updated atomically — a `BlockingQueue` approach would have also worked but requires a fifth thread and complicates the CHAT bypass" earn full credit.

---

## Section 1 — Design Choices (two required, ~100 words each)

For each: what did you choose, what was the alternative, and why?

Candidate topics (pick two; you may substitute your own if more interesting):

- `synchronized` method on `handleMessage` vs. a dedicated `BlockingQueue` consumer thread for game logic — why did you choose one over the other?
- How you handle CHAT without the game-logic lock — what exactly does your code do differently for CHAT vs. FIRE?
- `WriterThread` + `LinkedBlockingQueue` per client vs. making `writeFrame` calls directly from `GameServer` with a per-client lock
- Where you send ASSIGN — why the timing relative to the WebSocket handshake matters and what goes wrong if you get it wrong
- How you signal threads to exit after game over — sentinel in queue, socket close, interrupt, or something else

### 1.A — synchronized on handleMessage vs. a dedicated BlockingQueue consumer thread

*What I chose:* I made handleMessage in GameServer use a synchronized(this) block for all game logic (READY and FIRE). CHAT is extracted before that block and forwarded immediately.

*What I considered instead:* A fifth dedicated thread that consumes game-logic events from a LinkedBlockingQueue<Pair<Integer, Message>>.

*Why:* The synchronized approach keeps everything in one place and is easier to reason about. With a BlockingQueue consumer, I'd need a wrapper object to carry both the player number and the message, a fifth thread to manage, and I'd still need to bypass that queue for CHAT anyway. Since CHAT is already handled before the lock, the synchronized path naturally gives me the isolation I need. The only real risk with synchronized is doing slow I/O while holding the lock, but I only call writers[x].send() inside the lock, which is just outbox.offer(), a non-blocking operation. So the lock is never held during any real I/O.

---

### 1.B — WriterThread + LinkedBlockingQueue per client vs. calling writeFrame directly

*What I chose:* Each client has a dedicated WriterThread that drains a LinkedBlockingQueue<String>. Any thread that wants to send a message calls writers[x].send(), which just does outbox.offer() and returns immediately.

*What I considered instead:* A fifth dedicated thread that consumes game-logic events from a LinkedBlockingQueue<Pair<Integer, Message>>.

*Why:* The synchronized approach keeps everything in one place and is easier to reason about. With a BlockingQueue consumer, I'd need a wrapper object to carry both the player number and the message, a fifth thread to manage, and I'd still need to bypass that queue for CHAT anyway. Since CHAT is already handled before the lock, the synchronized path naturally gives me the isolation I need. The only real risk with synchronized is doing slow I/O while holding the lock — but I only call writers[x].send() inside the lock, which is just outbox.offer(), a non-blocking operation. So the lock is never held during any real I/O.

---

## Section 2 — Message Protocol

### 2.1 — Full message sequence for one complete game

Write out the complete sequence of messages exchanged between the server and both clients, from the moment each browser tab connects through to GAME_OVER. Include who sends each message and who receives it.  Use the format below.

Do not skip the handshake phase or abbreviate the mid-game turns — show at least three full fire/result/turn-change cycles.

```
[Tab 1]   →  [Server]   TCP connect + WebSocket handshake
[Server]  →  [Tab 1]    ASSIGN { playerNumber: 1 }
[Server]  →  [Tab 1]    WAITING
[Tab 2]   →  [Server]   TCP connect + WebSocket handshake
[Server]  →  [Tab 2]    ASSIGN { playerNumber: 2 }
[Tab 1]   →  [Server]   READY { name: "Alice" }
[Tab 2]   →  [Server]   READY { name: "Bob" }
[Server]  →  [Tab 1]    GAME_START { myBoard: [[1,0,...],[...]], turn: 1 }
[Server]  →  [Tab 2]    GAME_START { myBoard: [[0,1,...],[...]], turn: 1 }

-- Turn 1: Alice fires --
[Tab 1]   →  [Server]   FIRE { row: 3, col: 5 }
[Server]  →  [Tab 1]    SHOT_RESULT { shooter: 1, row: 3, col: 5, hit: false, sunkShip: null }
[Server]  →  [Tab 2]    SHOT_RESULT { shooter: 1, row: 3, col: 5, hit: false, sunkShip: null }
[Server]  →  [Tab 1]    TURN_CHANGE { turn: 2 }
[Server]  →  [Tab 2]    TURN_CHANGE { turn: 2 }

-- Turn 2: Bob fires --
[Tab 2]   →  [Server]   FIRE { row: 0, col: 0 }
[Server]  →  [Tab 1]    SHOT_RESULT { shooter: 2, row: 0, col: 0, hit: true, sunkShip: null }
[Server]  →  [Tab 2]    SHOT_RESULT { shooter: 2, row: 0, col: 0, hit: true, sunkShip: null }
[Server]  →  [Tab 1]    TURN_CHANGE { turn: 1 }
[Server]  →  [Tab 2]    TURN_CHANGE { turn: 1 }

-- Mid-game: Alice sends a chat while it's her turn --
[Tab 1]   →  [Server]   CHAT { text: "Nice try" }
[Server]  →  [Tab 1]    CHAT { from: "Alice", text: "Nice try" }
[Server]  →  [Tab 2]    CHAT { from: "Alice", text: "Nice try" }

-- Turn 3: Alice fires and sinks a ship --
[Tab 1]   →  [Server]   FIRE { row: 2, col: 1 }
[Server]  →  [Tab 1]    SHOT_RESULT { shooter: 1, row: 2, col: 1, hit: true, sunkShip: "Destroyer" }
[Server]  →  [Tab 2]    SHOT_RESULT { shooter: 1, row: 2, col: 1, hit: true, sunkShip: "Destroyer" }
[Server]  →  [Tab 1]    TURN_CHANGE { turn: 2 }
[Server]  →  [Tab 2]    TURN_CHANGE { turn: 2 }

-- ... more turns until Alice sinks all ships --

-- Final turn: Alice sinks last ship --
[Tab 1]   →  [Server]   FIRE { row: 7, col: 4 }
[Server]  →  [Tab 1]    SHOT_RESULT { shooter: 1, row: 7, col: 4, hit: true, sunkShip: "Carrier" }
[Server]  →  [Tab 2]    SHOT_RESULT { shooter: 1, row: 7, col: 4, hit: true, sunkShip: "Carrier" }
[Server]  →  [Tab 1]    GAME_OVER { winner: 1, finalBoard: [[0,2,...],[...]] }
[Server]  →  [Tab 2]    GAME_OVER { winner: 1, finalBoard: [[0,2,...],[...]] }
```

### 2.2 — Any message types you added or changed

I added a name field to the READY message sent from client to server. The README spec lists READY as having no additional fields, but the server needs a display name for the CHAT from field. The server reads this in handleReady and stores it in the names array.

---

## Section 3 — Concurrency

Answer each question in 3–6 sentences.  Reference specific class and method names from your own code.

**3.1 — How does your implementation ensure that a CHAT message sent by player 2 is never delayed by a FIRE message being processed for player 1?**

Name the exact location in your code where CHAT diverges from the game-logic path.  Explain what would go wrong if you handled CHAT the same way as FIRE.

In GameServer.handleMessage, the CHAT check happens at the top of the method before the synchronized(this) block. When a CHAT message comes in, the code calls broadcast(Message.chatJson(...)) and returns immediately — it never acquires the lock at all. FIRE goes through synchronized(this), which means if player 1's reader thread is inside handleFire (holding the lock while updating boards, fleets, and broadcasting SHOT_RESULT), player 2's CHAT still forwards immediately on player 2's reader thread without waiting. If CHAT were handled inside the synchronized block the same as FIRE, it would queue behind whatever game logic was currently holding the lock, which could be a noticeable delay.

---

**3.2 — Two `ClientHandler` threads call `handleMessage` concurrently. What shared state could be corrupted without synchronization, and what is the specific failure mode?**

Give a concrete interleaving — e.g., "Thread 1 reads `readyCount` as 1, Thread 2 reads `readyCount` as 1, both increment to 2, both enter `handleReady` and each generates a separate set of boards, with the second overwriting the first."  Don't just say "a race condition could occur."

The clearest example is readyCount. Both reader threads could call handleReady nearly simultaneously. Thread 1 reads readyCount as 1. Thread 2 reads readyCount as 1. Both increment to 2. Both pass the readyCount < 2 check. Both enter the game-start block, each calling ShipPlacementGenerator and constructing new Board and Fleet objects. The second one overwrites boards[0], boards[1], fleets[0], fleets[1] with a completely different placement, and both threads send GAME_START — so each client gets the message twice, with the second one overwriting the first board layout on the client. The same interleaving on turn during handleFire could let two shots fire on the same turn, updating the board twice without a turn change in between.

---

**3.3 — How do all five threads exit cleanly when the game ends?**

Walk through exactly what happens after `handleFire` detects that all ships are sunk: what method is called, what does it do to each thread's blocking call (`outbox.take()` or `readFrame()`), and in what order do the threads terminate.

When handleFire detects fleets[defenderIdx].allSunk(), it sets phase = PHASE_DONE, broadcasts GAME_OVER, then calls shutdown(). shutdown() calls writers[0].shutdown() and writers[1].shutdown(), which each call outbox.offer(POISON_PILL). Each WriterThread is blocked on outbox.take(), receiving POISON_PILL breaks its loop. In its finally block, each WriterThread calls socket.close(). Closing the socket causes the corresponding ClientHandler's WebSocketUtil.readFrame() call (which is blocked on InputStream.read()) to either return null or throw an IOException. Either way, ClientHandler.run() exits its read loop and reaches the finally block, which calls server.handleDisconnect(). Since phase is already PHASE_DONE, handleDisconnect returns immediately without double-sending OPPONENT_DISCONNECTED. Both ClientHandler threads exit, BattleshipServer.main() returns from its join() calls, and the process exits cleanly.

---

## Section 4 — Bugs (two required)

If you genuinely only hit one bug, describe a second plausible one — but only one you could actually imagine hitting, not one you invented from thin air. Graders can tell the difference.

### 4.A — Player 2 sees "Opponent Disconnected" instead of the Game Over screen

*Symptom:* When player 1 wins, player 1's browser shows the game over screen correctly. Player 2's browser shows the "opponent disconnected" error message instead of the game over screen.

*Cause:* In App.jsx, the useEffect that was supposed to sync statusRef had a typo — it used === (comparison) instead of = (assignment): useEffect(() => {statusRef.current === status; }, [status]). This meant statusRef.current was permanently stuck at "idle". When the server closed the socket after sending GAME_OVER, ws.onclose fired and checked statusRef.current !== "over", which was always true, and set the error message and reset state to idle, wiping the game over screen.

*Fix:* Updated the useEffect to use =. Also added statusRef.current = "over" synchronously inside the GAME_OVER case handler, so the ref updates before ws.onclose can fire.

*Lesson:* A typo that produces valid JavaScript (=== compiles fine) with completely silent wrong behavior is harder to catch than a syntax error. Reading useEffect dependencies more carefully would have caught it sooner.

---

### 4.B — ReferenceError crash when the opponent hits a ship

*Symptom:* After the opponent fired a shot that hit one of my ships, the WebSocket message handler silently stopped working, no board update, no fleet update, no further UI changes even when subsequent messages arrived.

*Cause:* In the else branch of handleShotResult (opponent fired at me), the sunk-ship chat notification referenced data.sunkShipName, but the function parameter is named msg, and data was never declared anywhere in that scope. This threw a ReferenceError at runtime, crashing the ws.onmessage handler. Also, the field name was sunkShipName instead of sunkShip, which is the actual field name the server sends per the protocol.

*Fix:* Replaced all data references with msg, and corrected sunkShipName to sunkShip everywhere in handleShotResult. Moved the sunk-ship chat notification outside the if/else so both players see it regardless of who fired.

*Lesson:* Using a variable name that doesn't exist in scope won't be caught until runtime in JavaScript. A linter (ESLint with no-undef) would have flagged data immediately.

---

## Section 5 — Testing

Describe what you actually ran.  "I tested it and it worked" earns no credit.

**5.1 — How did you verify the server before building the frontend?**

Name the tool and the specific JSON messages you sent.  What did you check in the server's output to confirm each step was working?

I send commands through the terminal to see how the server responded.
{"type": "READY", "name": "Alice"}
{"type": "FIRE", "row": 4, "col": 5}
{"type": "CHAT", "text": "Good luck, have fun!"}
{"type": "FIRE"}
{"type": "UNKNOWN_GARBAGE"}
 {"type":"READY"}
 {"type":"GAME_START","myBoard":[[1,1,1,1,1,0,...],...],"turn":1}
 {"type":"FIRE","row":3,"col":5}
 {"type":"SHOT_RESULT","shooter":1,"row":3,"col":5,"hit":false,"sunkShip":null}

---

**5.2 — How did you verify turn enforcement?**

Describe the exact test: which player sent FIRE out of turn, what JSON did you send, and what did the server return?

In T1 send: {"type":"READY","name":"Player1"}

In T2 send: {"type":"READY","name":"Player2"}

Intentional out of order: In T1/2 send: {"type":"FIRE","row":0,"col":0}

Error Message: {"type":"ERROR","message":"It is not your turn"}

---

**5.3 — How did you verify that CHAT is not blocked by concurrent FIRE processing?**

This is hard to test with two browser tabs.  Describe the specific setup you used — did you add an artificial delay to FIRE processing, use two `wscat` terminals, or something else?

This was very hard to test, but I did my best. I opened two wscat terminals and sent CHAT and FIRE in rapid succession from different terminals and observed that CHAT responses arrived without waiting for FIRE to complete.

---

**5.4 — How did you test disconnect handling?**

What did you close (the tab, the terminal, the socket), when did you close it (before READY, during play, after game over), and what did the surviving client receive?

I did Ctrl+C on the server, and I also closed a browser tab. When I did Ctrl+C, the browsers go back to the main screen and say "Server closed." When I closed a browser tab, the other browser goes to the game over screen and says that the opponent disconnected and you win by default.

---

**5.5 — Browser and OS**

List the browser(s) and OS(es) you tested on.

I used Windows and Google Chrome.

---

## Section 6 — Reflection (3–5 sentences, answer at least two)

- What was the hardest ordering constraint to get right, and how did you figure it out?
- What does this lab teach about the difference between correctness and performance in concurrent systems?
- What would you do differently if starting from scratch?
- Was there a moment something clicked about threads or WebSockets? What was it?

The hardest ordering constraint was figuring out when it was safe to send ASSIGN. Early on I had ASSIGN being enqueued before the WebSocket handshake completed, which caused the browser to drop the connection immediately — the frame was being written into the middle of the HTTP 101 response. The fix was making sendAssign get called from ClientHandler after WebSocketUtil.handshake() returns, not from BattleshipServer before the threads start. That was the moment WebSockets clicked for me — the handshake is not transparent, it's an actual HTTP exchange that has to finish before you can treat the connection as a frame stream.
This lab also made the difference between correctness and performance very concrete. A synchronized method is slow if the lock is held during I/O — but it's correct. Using WriterThread makes it fast AND correct. You don't get to just pick correctness and ignore performance when threads are involved, because a lock held too long is effectively the same as a bug from the user's perspective.
If I were starting over, I'd write the full message protocol doc before writing any code. I had to rewrite a lot of my code because I didn't have a solid understanding of the message protocol.

---

## Section 7 — Time Log

| Date | Hours | What you worked on      |
|------|-------|-------------------------|
| 4/11 | 6     | Backend                 |
| 4/12 | 3     | Frontend                |
| 4/13 | 4     | Integration and Testing |


**Total hours:** 12

**Approximate split between backend and frontend:** 60% / 40%

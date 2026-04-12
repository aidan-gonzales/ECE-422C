/*
 * GameServer.java
 *
 * Holds all authoritative game state and processes messages from both
 * ClientHandler threads.
 *
 * Thread-safety requirement:
 *   handleMessage() is called from two different threads simultaneously.
 *   You must ensure reads and writes to shared state (boards, fleets, turn,
 *   phase, readyCount) are properly synchronized.
 *
 * I/O rule:
 *   Never hold a lock while calling WebSocketUtil.writeFrame or any blocking
 *   I/O.  Enqueue outbound messages via WriterThread.send() instead — it
 *   returns immediately.
 *
 * Chat rule:
 *   CHAT messages must reach clients without being delayed by ongoing
 *   game-logic processing.  Handle CHAT before acquiring any lock.
 *
 * ASSIGN timing:
 *   sendAssign() is called from ClientHandler immediately after the WebSocket
 *   handshake completes.  It must NOT be called before the handshake, because
 *   WriterThread and ClientHandler share the same OutputStream — writing a
 *   WebSocket frame before the HTTP 101 response is sent corrupts the upgrade
 *   and causes the browser to close the connection.
 */

import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class GameServer {

    private static final int PHASE_WAITING = 0;
    private static final int PHASE_PLAYING = 1;
    private static final int PHASE_DONE    = 2;

    private final Board[]        boards  = new Board[2];
    private final Fleet[]        fleets  = new Fleet[2];
    private final WriterThread[] writers = new WriterThread[2];
    private final String[]       names   = {"Player 1", "Player 2"};
    private final Socket[]       sockets;

    private int phase      = PHASE_WAITING;
    private int turn       = 1;
    private int readyCount = 0;

    public GameServer(Socket s1, Socket s2, WriterThread w1, WriterThread w2) {
        sockets    = new Socket[]{s1, s2};
        writers[0] = w1;
        writers[1] = w2;
    }

    // -----------------------------------------------------------------------
    // Called by ClientHandler immediately after handshake() returns
    // -----------------------------------------------------------------------

    /**
     * Sends ASSIGN (and WAITING for player 1) to the given player.
     * Safe to call without the game-logic lock — only touches per-player
     * outboxes, which are already thread-safe.
     *
     * Must only be called after WebSocketUtil.handshake() has returned for
     * this player's socket.
     */
    public void sendAssign(int playerNum) {
        //throw new UnsupportedOperationException("Not implemented");
        // TODO: send ASSIGN to this player via their WriterThread
        int index = playerNum - 1;

        String assignMsg = "{\"type\":\"ASSIGN\",\"playerNumber\":" + playerNum + "}";
        writers[index].send(assignMsg);

        // TODO: if playerNum == 1, also send WAITING
        if (playerNum == 1) {
            writers[index].send("{\"type\":\"WAITING\"}");
        }
    }

    // -----------------------------------------------------------------------
    // Called by ClientHandler threads
    // -----------------------------------------------------------------------

    /**
     * Processes one message from the given player.
     *
     * Handle CHAT outside any lock — forward it to both clients immediately.
     * For all other types, synchronize on 'this' and dispatch on msg.type:
     *   "READY" → handleReady
     *   "FIRE"  → handleFire
     *   anything else → send ERROR back to the sender
     */
    public void handleMessage(int playerNum, Message msg) {
        //throw new UnsupportedOperationException("Not implemented");

        // catch the name setup message and update the name
        if ("JOIN".equals(msg.type)) {
            names[playerNum - 1] = msg.name;
            return;
        }

        // TODO: handle CHAT outside any lock
        if ("CHAT".equals(msg.type)) {
            // Broadcast the chat to everyone, so both screens update
            String chatJson = "{\"type\":\"CHAT\",\"from\":\"" + names[playerNum - 1] + "\",\"text:\":\"" + msg.text + "\"}";
            broadcast(chatJson);
            return;
        }

        // TODO: for all other types, synchronize and dispatch
        synchronized (this) {
            if ("READY".equals(msg.type)) {
                handleReady(playerNum, msg);
            } else if ("FIRE".equals(msg.type)) {
                handleFire(playerNum, msg);
            } else {
                // unknown command, so send an error back to the sender
                writers[playerNum - 1].send("{\"type\":\"ERROR\",\"message\":\"Unknown command\"}");
            }
        }
    }

    /**
     * Called when a client disconnects or throws an IOException.
     * Notifies the other player and initiates shutdown.
     * Must be idempotent — safe to call twice if both sockets close at once.
     */
    public void handleDisconnect(int playerNum) {
        //throw new UnsupportedOperationException("Not implemented");
        synchronized (this) {
            // TODO: guard against double-call (check/set phase under lock)
            if (phase == PHASE_DONE) {
                return;
            }
            phase = PHASE_DONE;

            // TODO: send OPPONENT_DISCONNECTED to the other player
            int opponentIdx = (playerNum == 1) ? 1 : 0;
            writers[opponentIdx].send("{\"type\":\"OPPONENT_DISCONNECTED\"}");

            // TODO: call shutdown()
            shutdown();
        }

    }

    // -----------------------------------------------------------------------
    // Private message handlers — call only while holding synchronized(this)
    // -----------------------------------------------------------------------

    private void handleReady(int playerNum, Message msg) {
        int idx = playerNum - 1;

        // ignore if we are already playing, or if this player is already ready
        if (phase != PHASE_WAITING || boards[idx] != null) {
            return;
        }

        // TODO: store display name if msg.name is non-blank
        if (msg.name != null && !msg.name.isEmpty()) {
            names[idx] = msg.name;
        }

        // TODO: increment readyCount; return early if readyCount < 2
        readyCount++;

        // TODO: when both ready: set phase/turn, generate placements for both
        //       players, construct Board and Fleet for each, send GAME_START
        //       to each player with their own board layout and the starting turn
        throw new UnsupportedOperationException("Not implemented");
    }

    private void handleFire(int playerNum, Message msg) {
        // TODO: reject if phase != PHASE_PLAYING (send ERROR)
        // TODO: reject if playerNum != turn (send ERROR — do not advance turn)
        // TODO: validate row and col are in [0, BOARD_SIZE)
        // TODO: reject if the target cell is already targeted (send ERROR)
        // TODO: fire the shot on the defender's board
        // TODO: if hit, register the hit on the defender's fleet
        // TODO: broadcast SHOT_RESULT to both players
        // TODO: if all defender ships sunk: set phase = PHASE_DONE,
        //       broadcast GAME_OVER with the defender's final board, call shutdown()
        // TODO: advance turn and broadcast TURN_CHANGE
        throw new UnsupportedOperationException("Not implemented");
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    /** Enqueues json in both clients' outboxes. */
    private void broadcast(String json) {
        //throw new UnsupportedOperationException("Not implemented");
        // TODO
        writers[0].send(json);
        writers[1].send(json);
    }

    /** Signals both WriterThreads to flush and exit. */
    private void shutdown() {
        //throw new UnsupportedOperationException("Not implemented");
        // TODO
        writers[0].shutdown();
        writers[1].shutdown();

        try {
            sockets[0].close();
            sockets[1].close();
        } catch (Exception e) {
            System.out.println("Error closing sockets during shutdown: " + e.getMessage());
        }
    }
}

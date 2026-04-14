/*
 * BattleshipServer.java
 *
 * Entry point.  Accepts exactly two client connections, wires up all
 * objects, starts all threads, and waits for the game to finish.
 *
 * Usage:
 *   java BattleshipServer          (default port 8080)
 *   java BattleshipServer 9090     (custom port)
 *
 * Important — do NOT pre-queue ASSIGN messages here.
 * ASSIGN is sent from inside ClientHandler, immediately after the WebSocket
 * handshake completes.  Sending it earlier risks writing a WebSocket frame
 * before the HTTP 101 upgrade response is finished, which corrupts the
 * connection and causes the browser to disconnect immediately.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class BattleshipServer {

    private static final int DEFAULT_PORT = 8080;

    /**
     * main function that starts the entire program.
     * @param args passed in through the terminal
     * @throws IOException must throw this checked exception
     */
    public static void main(String[] args) throws IOException {
        // TODO: parse port from args[0] if present, fall back to DEFAULT_PORT
        int port = DEFAULT_PORT;

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        // TODO: create a ServerSocket on the chosen port (try-with-resources)
        try (ServerSocket serversocket = new ServerSocket(port)) {


            // TODO: print a startup message
            System.out.println("Battleship server starting on port " + port);

            // TODO: call serverSocket.accept() to get player 1's Socket
            System.out.println("Waiting for Player 1...");
            Socket p1Socket = serversocket.accept();
            // TODO: call serverSocket.accept() again to get player 2's Socket
            System.out.println("Player 1 connected! Waiting for Player 2...");
            Socket p2Socket = serversocket.accept();

            System.out.println("Player 2 connected! Starting game setup...");

            // TODO: create a LinkedBlockingQueue<String> for each player's outbox
            LinkedBlockingQueue<String> p1Outbox = new LinkedBlockingQueue<>();
            LinkedBlockingQueue<String> p2Outbox = new LinkedBlockingQueue<>();

            // TODO: create a WriterThread for each player
            WriterThread p1Writer = new WriterThread(p1Outbox, p1Socket.getOutputStream(), p1Socket);
            WriterThread p2Writer = new WriterThread(p2Outbox, p2Socket.getOutputStream(), p2Socket);

            // TODO: create the shared GameServer
            GameServer gameserver = new GameServer(p1Socket, p2Socket, p1Writer, p2Writer);

            // TODO: create a ClientHandler for each player
            ClientHandler p1Handler = new ClientHandler(p1Socket, 1, gameserver);
            ClientHandler p2Handler = new ClientHandler(p2Socket, 2, gameserver);

            // TODO: start both WriterThreads first (so they are ready to drain
            //       outboxes as soon as ClientHandlers start enqueuing post-handshake)
            p1Writer.start();
            p2Writer.start();

            // TODO: then start both ClientHandlers
            p1Handler.start();
            p2Handler.start();

            // reject any other join requests
            Thread rejectThread = new Thread(() -> {
                while (true) {
                    try {
                        Socket extra = serversocket.accept();
                        WebSocketUtil.handshake(extra.getInputStream(), extra.getOutputStream());
                        WebSocketUtil.writeFrame(extra.getOutputStream(),
                                Message.errorJson("Server is full. Only 2 players allowed."));
                        extra.close();
                    } catch (IOException e) {
                        break; // ServerSocket was closed when try-with-resources ended — exit cleanly
                    }
                }
            });
            rejectThread.setDaemon(true);
            rejectThread.start();

            // TODO: join both ClientHandler threads so main() waits for the game to end
            try {
                p1Handler.join();
                p2Handler.join();
            } catch (InterruptedException e) {
                System.out.println("Server interrupted: " + e.getMessage());
            }

        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }

        // TODO: print a shutdown message
        System.out.println("Game over. Server shutting down cleanly.");
    }
}

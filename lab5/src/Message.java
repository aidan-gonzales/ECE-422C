/*
 * Message.java
 *
 * Represents a single protocol message.  All messages share a "type" field;
 * additional fields are present or absent depending on the type.
 *
 * You must implement:
 *   - parse(String json)          — client-to-server message parsing
 *   - JSON builder methods        — server-to-client message construction
 *
 * No external JSON library is permitted.  Use String methods only.
 */
public class Message {

    // -----------------------------------------------------------------------
    // Fields — not every field is present in every message type.
    // Absent numeric fields should default to -1; absent strings to null.
    // -----------------------------------------------------------------------

    public String type;
    public int    row  = -1;
    public int    col  = -1;
    public String text = null;
    public String name = null;

    // -----------------------------------------------------------------------
    // Parsing
    // -----------------------------------------------------------------------

    /**
     * Parses a JSON string received from a client into a Message.
     *
     * At minimum, handle the types a client can send:
     *   READY  — may include a "name" field
     *   FIRE   — includes "row" and "col" (integers)
     *   CHAT   — includes "text"
     *
     * If the string cannot be parsed, return a Message whose type is "UNKNOWN".
     *
     * @param json the raw JSON string from a WebSocket frame
     * @return a populated Message object
     */
    public static Message parse(String json) {
        // TODO: extract "type" from json; set m.type
        // TODO: extract "row", "col", "text", "name" as appropriate for each type
        // Hint: write a private helper that extracts the value for a named key
        //       from a flat JSON object string.
        throw new UnsupportedOperationException("Not implemented");
    }

    // -----------------------------------------------------------------------
    // JSON builders  (server → client)
    //
    // Each method returns a JSON string ready to hand to WriterThread.send().
    // Field names must exactly match the protocol table in the README.
    // -----------------------------------------------------------------------

    /** {"type":"ASSIGN","playerNumber":<n>} */
    public static String assignJson(int playerNumber) {
        // TODO
        throw new UnsupportedOperationException("Not implemented");
    }

    /** {"type":"WAITING"} */
    public static String waitingJson() {
        // TODO
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * {"type":"GAME_START","myBoard":<2d array>,"turn":<n>}
     *
     * @param myBoardJson the JSON array string returned by Board.shipLayoutToJson()
     * @param turn        1 or 2 — which player moves first
     */
    public static String gameStartJson(String myBoardJson, int turn) {
        // TODO
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * {"type":"SHOT_RESULT","shooter":<n>,"row":<r>,"col":<c>,
     *  "hit":<bool>,"sunkShip":<name or null>}
     */
    public static String shotResultJson(int shooter, int row, int col,
                                        boolean hit, String sunkShip) {
        // TODO: encode sunkShip as a JSON string if non-null, or the literal null
        throw new UnsupportedOperationException("Not implemented");
    }

    /** {"type":"TURN_CHANGE","turn":<n>} */
    public static String turnChangeJson(int turn) {
        // TODO
        throw new UnsupportedOperationException("Not implemented");
    }

    /** {"type":"CHAT","from":"<from>","text":"<text>"} */
    public static String chatJson(String from, String text) {
        // TODO: remember to escape any special characters in the strings
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * {"type":"GAME_OVER","winner":<n>,"finalBoard":<2d array>}
     *
     * @param finalBoardJson the JSON array string returned by Board.fullStateToJson()
     */
    public static String gameOverJson(int winner, String finalBoardJson) {
        // TODO
        throw new UnsupportedOperationException("Not implemented");
    }

    /** {"type":"OPPONENT_DISCONNECTED"} */
    public static String opponentDisconnectedJson() {
        // TODO
        throw new UnsupportedOperationException("Not implemented");
    }

    /** {"type":"ERROR","message":"<msg>"} */
    public static String errorJson(String msg) {
        // TODO
        throw new UnsupportedOperationException("Not implemented");
    }
}

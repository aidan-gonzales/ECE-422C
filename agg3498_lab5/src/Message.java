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
        //throw new UnsupportedOperationException("Not implemented");
        // TODO: extract "type" from json; set m.type
        Message msg = new Message();

        // always extra type first
        msg.type = extractString(json, "type");
        if (msg.type == null) {
            msg.type = "UNKNOWN";
            return msg;
        }

        // TODO: extract "row", "col", "text", "name" as appropriate for each type
        // Hint: write a private helper that extracts the value for a named key
        //       from a flat JSON object string.
        if ("READY".equals(msg.type) || "JOIN".equals(msg.type)) {
            msg.name = extractString(json, "name");
        } else if ("FIRE".equals(msg.type)) {
            msg.row = extractInt(json, "row");
            msg.col = extractInt(json, "col");
        } else if ("CHAT".equals(msg.type)) {
            msg.text = extractString(json, "text");
        }

        return msg;
    }


    /**
     * private helper for parse, finds Strings
     * @param json json string that this function is parsing
     * @param key the value that we're looking for
     * @return the string associated with the key value
     */
    private static String extractString(String json, String key) {
        String searchPattern = "\"" + key + "\":\"";
        int startIndex = json.indexOf(searchPattern);
        if (startIndex == -1) return null;

        startIndex += searchPattern.length();
        int endIndex = json.indexOf("\"", startIndex);
        if (endIndex == -1) return null;

        return json.substring(startIndex, endIndex);
    }

    /**
     * private helper for parse, finds ints
     * @param json json string that this function is parsing
     *      * @param key the value that we're looking for
     *      * @return the int  associated with the key value
     */
    private static int extractInt(String json, String key) {
        String searchPattern = "\"" + key + "\":";
        int startIndex = json.indexOf(searchPattern);
        if (startIndex == -1) return -1;

        startIndex += searchPattern.length();

        int endComma = json.indexOf(",", startIndex);
        int endBrace = json.indexOf("}", startIndex);

        int endIndex;
        if (endComma == -1) endIndex = endBrace;
        else if (endBrace == -1) endIndex = endComma;
        else endIndex = Math.min(endComma, endBrace);

        if (endIndex == -1) return -1;

        try {
            return Integer.parseInt(json.substring(startIndex, endIndex).trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // -----------------------------------------------------------------------
    // JSON builders  (server → client)
    //
    // Each method returns a JSON string ready to hand to WriterThread.send().
    // Field names must exactly match the protocol table in the README.
    // -----------------------------------------------------------------------

    /**
     * {"type":"ASSIGN","playerNumber":<n>}
     * @param playerNumber current player number
     * @return json string for assign command
     */
    public static String assignJson(int playerNumber) {
        // TODO
        //throw new UnsupportedOperationException("Not implemented");
        return "{\"type\":\"ASSIGN\",\"playerNumber\":" + playerNumber + "}";
    }

    /**
     * {"type":"WAITING"}
     * @return json string for waiting command
     */
    public static String waitingJson() {
        // TODO
        //throw new UnsupportedOperationException("Not implemented");
        return "{\"type\":\"WAITING\"}";
    }

    /**
     * {"type":"GAME_START","myBoard":<2d array>,"turn":<n>}
     *
     * @param myBoardJson the JSON array string returned by Board.shipLayoutToJson()
     * @param turn        1 or 2 — which player moves first
     * @return json string for the game start command
     */
    public static String gameStartJson(String myBoardJson, int turn) {
        // TODO
        //throw new UnsupportedOperationException("Not implemented");
        // do not put quotes around myBoardJson because it is already a valid JSON array
        return "{\"type\":\"GAME_START\",\"turn\":" + turn + ",\"myBoard\":" + myBoardJson + "}";
    }

    /**
     * {"type":"SHOT_RESULT","shooter":<n>,"row":<r>,"col":<c>,
     *  "hit":<bool>,"sunkShip":<name or null>}
     * @param shooter player that is firing
     * @param row row being targeted
     * @param col column being targeted
     * @param hit true if a hit, false if not
     * @param sunkShip null if no ship sunk, ship name if ship sunk
     * @return json string for shot result command
     */
    public static String shotResultJson(int shooter, int row, int col,
                                        boolean hit, String sunkShip) {
        // TODO: encode sunkShip as a JSON string if non-null, or the literal null
        //throw new UnsupportedOperationException("Not implemented");
        // If sunkShip is null, output the JSON literal null. Otherwise, wrap the name in quotes.
        String sunkStr = (sunkShip == null) ? "null" : "\"" + sunkShip + "\"";

        return "{\"type\":\"SHOT_RESULT\",\"shooter\":" + shooter + ",\"row\":" + row +
                ",\"col\":" + col + ",\"hit\":" + hit + ",\"sunkShip\":" + sunkStr + "}";
    }

    /**
     * {"type":"TURN_CHANGE","turn":<n>}
     * @param turn tells us which player's turn it is
     * @return json string for turn change command
     */
    public static String turnChangeJson(int turn) {
        // TODO
        //throw new UnsupportedOperationException("Not implemented");
        return "{\"type\":\"TURN_CHANGE\",\"turn\":" + turn + "}";
    }

    /**
     * {"type":"CHAT","from":"<from>","text":"<text>"}
     * @param from message sender
     * @param text contents of the message
     * @return json string for the chat command
     */
    public static String chatJson(String from, String text) {
        // TODO: remember to escape any special characters in the strings
        //throw new UnsupportedOperationException("Not implemented");
        // Escape quotes, backslashes, and newlines so the user can't break the JSON structure
        String escapedText = text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
        String escapedFrom = from.replace("\\", "\\\\").replace("\"", "\\\"");

        return "{\"type\":\"CHAT\",\"from\":\"" + escapedFrom + "\",\"text\":\"" + escapedText + "\"}";
    }

    /**
     * {"type":"GAME_OVER","winner":<n>,"finalBoard":<2d array>}
     *
     * @param finalBoardJson the JSON array string returned by Board.fullStateToJson()
     * @param winner winner of the game
     * @return json string for the game over command
     */
    public static String gameOverJson(int winner, String finalBoardJson) {
        // TODO
        //throw new UnsupportedOperationException("Not implemented");
        // do not put quotes around finalBoardJson because it is already a valid JSON array "[...]"
        return "{\"type\":\"GAME_OVER\",\"winner\":" + winner + ",\"finalBoard\":" + finalBoardJson + "}";
    }

    /**
     * {"type":"OPPONENT_DISCONNECTED"}
     * @return json string for the opponent disconnected command
     */
    public static String opponentDisconnectedJson() {
        // TODO
        //throw new UnsupportedOperationException("Not implemented");
        return "{\"type\":\"OPPONENT_DISCONNECTED\"}";
    }

    /**
     * {"type":"ERROR","message":"<msg>"}
     * @param msg error message
     * @return json string for the error command
     */
    public static String errorJson(String msg) {
        // TODO
        //throw new UnsupportedOperationException("Not implemented");
        String escapedMsg = msg.replace("\\", "\\\\").replace("\"", "\\\"");
        return "{\"type\":\"ERROR\",\"message\":\"" + escapedMsg + "\"}";
    }
}

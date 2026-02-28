

public class Board {

    private char[][] board = new char[GameConfiguration.BOARD_SIZE][GameConfiguration.BOARD_SIZE];


    /**
     * Board constructor. Sets all cells to the default value '~'
     */
    public Board() {
        for (int i = 0; i < GameConfiguration.BOARD_SIZE; i++) {
            for (int j = 0; j < GameConfiguration.BOARD_SIZE; j++) {
                board[i][j] = '~';
            }
        }
    }
}

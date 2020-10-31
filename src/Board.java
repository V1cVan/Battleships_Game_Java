

public class Board {

    private final int[] BOARD_SIZE;  // e.g. BOARD_SIZE = {8,8}
    private Tile[][] tiles;  // All the tiles on the board in [x]by[x] matrix

    // Board class constructor
    public Board(int[] brdSze){
        BOARD_SIZE = brdSze;
        // Initialise all the tiles on the board
        tiles = new Tile[BOARD_SIZE[0]][BOARD_SIZE[1]];
        for (int i=0; i<BOARD_SIZE[0]; i++) {
            for (int j = 0; j < BOARD_SIZE[1]; j++) {
                tiles[i][j] = new Tile();
            }
        }
    }

    public int[] getBOARD_SIZE() {
        return BOARD_SIZE;
    }

    public String showBoard(){
        String board = "";
        for (int i=0; i<BOARD_SIZE[0]; i++){
            for (int j=0; j<BOARD_SIZE[1]; j++){
                board = board +"["+ tiles[i][j].getTileColour() +"]";
            }
            board = board + "\n";
        }
        return board;
    }

}

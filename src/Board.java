import java.lang.reflect.Array;
import java.util.Random;
import java.util.HashMap; // import the HashMap class
import java.util.Arrays;

public class Board {

    private final int[] BOARD_SIZE;  // e.g. BOARD_SIZE = {8,8}
    private Tile[][] board;



    public Board(int[] brdSze, Boolean strtPlayer) {
        BOARD_SIZE = brdSze;

        // Initialise board's individual tiles
        board = new Tile[BOARD_SIZE[0]][BOARD_SIZE[1]];
        for (int i=0; i<BOARD_SIZE[0]; i++) {
            for (int j = 0; j < BOARD_SIZE[1]; j++) {
                board[i][j] = new Tile('f',new int[]{i,j});
            }
        }

        if (strtPlayer == true){
            // Ship types for player 1
            Ship destroyer = new Ship(1, 'd');
            Ship submarine = new Ship(2, 's');
            Ship battleship = new Ship(3, 'b');
            Ship carrier = new Ship(4, 'c');
        }else{
            // Ship types for player 2
            Ship destroyer = new Ship(1, 'd');
            Ship submarine = new Ship(2, 's');
            Ship battleship = new Ship(3, 'b');
            Ship carrier = new Ship(4, 'c');
        }
    }

    private int[][] generateShipCoordinates(int shipLength){
        Random randValue = new Random();
        int[] shipLocation;
        int shipOrientation;
        // Generate random ship location and orientation
        // TODO improve random algo
        shipLocation = new int[] {randValue.nextInt(BOARD_SIZE[0]), randValue.nextInt(BOARD_SIZE[1])};
        shipOrientation = randValue.nextInt(4);  // 0 = horizontal_right, 1 = vertical_downwards, 2 = horizontal_left, 3 = vertical_upwards
        System.out.println(String.format("Ship Location = [%d,%d]", shipLocation[0], shipLocation[1]));
        System.out.println(String.format("Ship Orientation = %d", shipOrientation));

        // Get matching coordinates
        int[][] shipCoords = new int[shipLength][2];
        switch(shipOrientation) {
            case 0:  // 0 = horizontal_right
                for (int i=0; i<shipLength; i++){
                    shipCoords[i][0] = shipLocation[0]+i;  // x-coordinate
                    shipCoords[i][1] = shipLocation[1];  // y-coordinate
                }
                break;
            case 1:  // 1 = vertical_downwards
                for (int i=0; i<shipLength; i++){
                    shipCoords[i][0] = shipLocation[0];  // x-coordinate
                    shipCoords[i][1] = shipLocation[1]+i;  // y-coordinate
                }
                break;
            case 2:  // 2 = horizontal_left
                for (int i=0; i<shipLength; i++){
                    shipCoords[i][0] = shipLocation[0]-i;  // x-coordinate
                    shipCoords[i][1] = shipLocation[1];  // y-coordinate
                }
                break;
            case 3:  // 3 = vertical_upwards
                for (int i=0; i<shipLength; i++){
                    shipCoords[i][0] = shipLocation[0];  // x-coordinate
                    shipCoords[i][1] = shipLocation[1]-i;  // y-coordinate
                }
                break;
        }
        return shipCoords;
    }

    private boolean isValidShipPlacement(int[][] coords){
        // Check if ship is out of bounds
        boolean outOfBounds = false;
        for (int[] i : coords){
            for (int j = 0; j<i.length; j++) {
                if (i[j] < 0 | i[j] > BOARD_SIZE[0] | i[j] > BOARD_SIZE[1]) {
                    outOfBounds=true;
                }
            }
        }

        // Check if ships overlap on board
        boolean shipOverlap = false;

        boolean isValid;
        if (shipOverlap | outOfBounds){
            isValid = false;
        }else{
            isValid = true;
        }

        return isValid;
    }

    public void placeShipsRandomly(){
        int[] shipLengths = {2, 3, 4, 5};
        // Place each of the different ships:

        for (int shipIndex : shipLengths) {  // Loop through each type of ship
            Boolean locationFound = false;
            int[][] shipCoords = new int[shipIndex][2];
            while (locationFound != true) {
                shipCoords = generateShipCoordinates(shipIndex);
                Boolean isValid = isValidShipPlacement(shipCoords);
                System.out.println(isValid);
                System.out.println(Arrays.deepToString(shipCoords));
                locationFound = true;
            }
        }
    }

    public int[] getBOARD_SIZE() {
        return BOARD_SIZE;
    }

    public String showBoard(){
        String boardText = "";
        for (int i=0; i<BOARD_SIZE[0]; i++) {
            for (int j = 0; j < BOARD_SIZE[1]; j++) {
                boardText = boardText + "[" + board[i][j].getTileType() + "]";
            }
            boardText = boardText + "\n";
        }
        return boardText;
    }
}

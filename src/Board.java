import java.util.Random;
import java.util.ArrayList;

public class Board {

    private final int[] BOARD_SIZE;  // e.g. BOARD_SIZE = {8,8}
    private Tile[][] board;
    private Ship[] ships = new Ship[4];


    public Board(int[] brdSze, Boolean strtPlayer) {
        BOARD_SIZE = brdSze;

        // Initialise board's individual tiles
        board = new Tile[BOARD_SIZE[0]][BOARD_SIZE[1]];
        for (int i=0; i<BOARD_SIZE[0]; i++) {
            for (int j = 0; j < BOARD_SIZE[1]; j++) {
                board[i][j] = new Tile('w',new int[]{i,j});
            }
        }
        // Initialise ships
        ships[0] = new Ship(1, 2, 'd', null);
        ships[1] = new Ship(2, 3, 's', null);
        ships[2] = new Ship(3, 4, 'b', null);
        ships[3] = new Ship(4, 5, 'c', null);
    }

    private int[][] generateShipCoordinates(Ship currentShip){
    // Generates coordinates for a ship's random placement

        Random randValue = new Random();
        int[] shipLocation;
        int shipOrientation;

        // Generate random ship location and orientation:
        shipLocation = new int[] {randValue.nextInt(BOARD_SIZE[0]), randValue.nextInt(BOARD_SIZE[1])};  // (x,y)
        shipOrientation = randValue.nextInt(2);  // 0 = horizontal, 1 = vertical

        // Get coordinates for each ship given its location and orientation:
        int[][] shipCoords = new int[currentShip.getShipLength()][2];  // Format: (x0,y0), (x1,y1), ... , (xN,yN)
        if (shipOrientation == 0) { // If horizontal
            for (int i = 0; i < currentShip.getShipLength(); i++) {
                shipCoords[i][0] = shipLocation[0] + i;     // x-coordinate
                shipCoords[i][1] = shipLocation[1];         // y-coordinate
            }
        }else { // if vertical
            for (int i = 0; i < currentShip.getShipLength(); i++) {
                shipCoords[i][0] = shipLocation[0];         // x-coordinate
                shipCoords[i][1] = shipLocation[1] + i;     // y-coordinate
            }
        }
        return shipCoords;
    }

    private boolean isValidShipPlacement(int[][] coords, ArrayList placedShips){
    // Checks if the ship placement is out of bounds.

        // Check if ship is out board's bounds:
        boolean outOfBounds = false;
        for (int[] i : coords){
            for (int j = 0; j<i.length; j++) {
                if (i[j] < 0 | i[j] > BOARD_SIZE[0] | i[j] > BOARD_SIZE[1]) {
                    outOfBounds=true;
                }
            }
        }

        // Check if ships overlap on board:
        boolean shipOverlap = false;
        ArrayList<Ship> shipsToCheck = null;
        if (placedShips.size() != 0) {
            // Remove ship that was just placed
            shipsToCheck = new ArrayList<Ship>(placedShips.subList(0, placedShips.size() - 1));
        }
        if (shipsToCheck != null) {
            for (Ship ship : shipsToCheck) {
                for (int[] i : ship.getShipCoordinates()){
                    for (int[] j : coords){
                        if (i==j) shipOverlap = true;
                    }
                }
            }
        }

        // Check if placement is valid:
        boolean isValid;
        if (shipOverlap == false & outOfBounds==false){
            isValid = true;
        }else{
            isValid = false;
        }

        return isValid;
    }

    public void placeShipsRandomly(){
        ArrayList<Ship> placedShips = new ArrayList<Ship>();
        // Place each of the different ships:
        for (int i=0; i<4; i++) {  // Loop through each type of ship
            Boolean validCoordsFound = false;
            System.out.println(String.format("Placing ship number %d ",i));
            while (validCoordsFound == false) {
                int[][] shipCoords = generateShipCoordinates(this.ships[i]);
                Boolean isValid = isValidShipPlacement(shipCoords, placedShips);
                if (isValid == true) {
                    this.ships[i].setShipCoordinates(shipCoords);
                    placedShips.add(this.ships[i]);
                    validCoordsFound = true;
                }
            }
        }
    }

    public int[] getBOARD_SIZE() {
        return BOARD_SIZE;
    }

    public Ship[] getShips() {
        return ships;
    }

    public void showShipPlacement() {
        for (Ship ship : this.ships){
            for (int[] coordPair : ship.getShipCoordinates()){
                int x = coordPair[0];
                int y = coordPair[1];
                board[x][y].setTile(ship.getShipType());
            }
        }
        System.out.println(this.showBoard());
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

import java.util.Random;
import java.util.ArrayList;
import java.io.File;  // File reading class
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;  // File error handling class
import java.util.Scanner;  // Text scanner class

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

    private boolean isValidShipPlacement(int[][] coords, ArrayList<Ship> placedShips){
    // Checks if the ship placement is out of bounds.
        boolean isValid = true;
        // Check if ship is out board's bounds:
        for (int[] i : coords){
            for (int j = 0; j<i.length; j++) {
                if (i[j] < 0 | i[j] > BOARD_SIZE[0]-1 | i[j] > BOARD_SIZE[1]-1) {
                    return isValid = false;
                }
            }
        }

        // Check if ships overlap on board:
        if (placedShips != null) {
            for (Ship ship : placedShips) {
                for (int[] i : ship.getShipCoordinates()){
                    for (int[] j : coords){
                        if (i[0]==j[0] & i[1]==j[1]) {
                            return isValid = false;
                        }
                    }
                }
            }
        }
        return isValid;
    }

    public void placeShipsRandomly(){
        ArrayList<Ship> placedShips = new ArrayList<Ship>();
        // Place each of the different ships:
        for (int i=0; i<4; i++) {  // Loop through each type of ship
            Boolean validCoordsFound = false;
            while (validCoordsFound == false) {
                int[][] shipCoords = generateShipCoordinates(this.ships[i]);
                Boolean isValid = isValidShipPlacement(shipCoords, placedShips);
                System.out.println(isValid);
                System.out.println(shipCoords);
                if (isValid == true) {
                    this.ships[i].setShipCoordinates(shipCoords);
                    placedShips.add(this.ships[i]);
                    validCoordsFound = true;
                }
            }
        }
    }

    public void placeShipsFromFile(){
        /*
        File format:
        8
        Carrier;3*2;3*3;3*4;3*5;3*6
        Battleship;5*6;6*6;7*6;8*6
        Submarine;5*2;6*2;7*2;
        Destroyer;1*7;1*8

        Error Catching:
        -overlapping ships
        -tile coordinates and board dimension specifications that are inconsistent
        -incorrect ships names
        -too few or too many ships
         */


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
                if (x == 8 | y ==8){
                    System.out.println("hello");
                }
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

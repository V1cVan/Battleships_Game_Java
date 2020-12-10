import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;


public class Board {

    private int[] BOARD_SIZE;  // e.g. BOARD_SIZE = {8,8}
    private Tile[][] board;
    private Ship[] ships = new Ship[4];


    public Board(int[] brdSize) {
        BOARD_SIZE = brdSize;
        // Initialise board's individual tiles
        board = new Tile[BOARD_SIZE[0]][BOARD_SIZE[1]];
        for (int i=0; i<BOARD_SIZE[0]; i++) {
            for (int j = 0; j < BOARD_SIZE[1]; j++) {
                board[i][j] = new Tile('w',new int[]{i,j});
            }
        }
        // Initialise ships
        ships[0] = new Ship(1, 2, "Destroyer", null);
        ships[1] = new Ship(2, 3, "Submarine", null);
        ships[2] = new Ship(3, 4, "Battleship", null);
        ships[3] = new Ship(4, 5, "Carrier", null);
    }

    private int[][] generateShipCoordinates(Ship currentShip){
        /**
         Generates random ship coordinates.
         @param currentShip: Ship who's coordinates are being generated
         @return shipCoords: Ship's generated coordiantes.
         @throws None
         */

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
        /**
         Checks if a ship placement is valid:
            1)Checks if ship is placed on the board
            2)Checks if ship placements overlap
         @param coords: Current ship coordinates to be placed
                placedShips: Ships already placed on board
         @return isValid: Whether or not placement was valid.
         @throws None
         */
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
        /**
         Places ships randomly on the board.
         @param None
         @return None
         @throws None
         */
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
        this.setShipPlacement();
    }

    public void readShipPlacementFile(String filename){
        /**
         Reads in ship coordinates from the provided game text files.
         @param filename = file read from
         @return None
         @throws ErrorsInShipPlacement: Out of board bounds, Overlapping ships, too few ships, too many ships
                 ErrorsInShipNaming
         */

        ArrayList<Ship> placedShips = new ArrayList<Ship>();
        int[][] coordinates = null;
        String shipType = null;
        try(
                FileReader file = new FileReader(filename);
                Scanner scan = new Scanner(file);
        ){
            scan.useDelimiter(";");
            int lineNum = 0;
            // Loop through each line of file:
            while (scan.hasNext()) {
                String line = scan.nextLine();
                if (lineNum > 0) {
                    String[] lineElements = line.split("\\;");
                    System.out.println("Line = " + line);
                    // Loop through each segment in the current line.
                    for (int lineIndex = 0; lineIndex < lineElements.length; lineIndex++) {
                        System.out.println("Line element = " + lineElements[lineIndex]);
                        lineElements[lineIndex] = lineElements[lineIndex].strip();
                        // Init ship coordinates. 
                        if (lineIndex == 0){
                            shipType = lineElements[lineIndex].toLowerCase();
                            switch(shipType) {
                                case "carrier":
                                    coordinates = new int[ships[3].getShipLength()][2];
                                    break;
                                case "battleship":
                                    coordinates = new int[ships[2].getShipLength()][2];
                                    break;
                                case "submarine":
                                    coordinates = new int[ships[1].getShipLength()][2];
                                    break;
                                case "destroyer":
                                    coordinates = new int[ships[0].getShipLength()][2];
                                    break;
                                default:
                                    System.out.println("Ship possibly spelled incorrectly.");
                                    System.out.println("Error in setting a ship from the gameSettings text file!");
                                    System.exit(0);
                            }
                        }else{ // read in ship coordinates
                            String[] coordElements = lineElements[lineIndex].split("\\*");
                            int x = Integer.parseInt(coordElements[0])-1;
                            int y = Integer.parseInt(coordElements[1])-1;
                            coordinates[lineIndex-1] = new int[] {x,y};
                        }
                    }
                    // Check if valid ship placement
                    boolean isValid = isValidShipPlacement(coordinates, placedShips);
                    if (isValid == true) {
                        switch (shipType) {
                            case "carrier":
                                this.ships[3].setShipCoordinates(coordinates);
                                placedShips.add(this.ships[3]);
                                break;
                            case "battleship":
                                this.ships[2].setShipCoordinates(coordinates);
                                placedShips.add(this.ships[2]);
                                break;
                            case "submarine":
                                this.ships[1].setShipCoordinates(coordinates);
                                placedShips.add(this.ships[1]);
                                break;
                            case "destroyer":
                                this.ships[0].setShipCoordinates(coordinates);
                                placedShips.add(this.ships[0]);
                                break;
                            default:
                        }
                    }else{
                        System.out.println("Invalid placement of ships. Please edit the ship placement definition txt.");
                        System.exit(0);
                    }

                }
                lineNum = lineNum + 1;
            }
            // Final checks on the numbers of ships defined:
            if (lineNum < 5){
                System.out.println("Invalid placement of ships. Too few ships defined!");
                System.exit(0);
            }else if(lineNum > 5){
                System.out.println("Invalid placement of ships. Too many ships defined!");
                System.exit(0);
            }else {  // if all check passed
                this.setShipPlacement();
            }

        } catch(FileNotFoundException fileNotFoundException){
            fileNotFoundException.printStackTrace();
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void setShipPlacement() {
        /**
         Sets all the ship locations on the board's tiles.
         @param None
         @return None
         @throws None
         */
        for (Ship ship : this.ships){
            for (int[] coordPair : ship.getShipCoordinates()){
                int x = coordPair[0];
                int y = coordPair[1];
                if (x == 8 | y ==8){
                    System.out.println("hello");
                }
                board[x][y].setTile(ship.getShipSymbol());
            }
        }
    }

    public int[] getBOARD_SIZE() {
        return BOARD_SIZE;
    }

    public Ship[] getShips() {
        return ships;
    }


    public String showBoard(){
        /**
         Shows a text-based representation of the board with locations of the ships.
         @param None
         @return boardText: String containing the ship locations. 
         @throws None
         */
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

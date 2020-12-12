package backend;// Import packages:
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;


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
            3)Checks if ship placements are disjoint (a ship is not continuous)
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

        // Check if ship forms a line (for file inputted ships):
        for (int i=0; i<coords.length-1; i++){
            int[] currLoc = coords[i];
            int[] nextLoc = coords[i+1];
            if ((Math.abs(currLoc[0] - nextLoc[0]) != 1 | Math.abs(currLoc[1] - nextLoc[1]) != 1) == false){
                return isValid = false;
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
                if (isValid == true) {
                    this.ships[i].setShipCoordinates(shipCoords);
                    placedShips.add(this.ships[i]);
                    validCoordsFound = true;
                }
            }
        }
        this.setShipPlacement();
    }

    public int readShipPlacementFile(String filename){
        /**
         // TODO Update comment according to new return value
         Reads in ship coordinates from the provided game text files.
         @param filename = file read from
         @return None
         @throws ErrorsInShipPlacement: (1) Out of board bounds, (2) Overlapping ships, (3) too few ships,
                                        (4) too many ships, (5) too few coordinates specified for ship,
                                        (6) too many coordinates specified for ship, (7) ships are not continuous
                 (8) ErrorsInShipNaming
                 (9) NumberFormatException : inputting letters instead of numbers for ship coordinates or using
                                             the incorrect separators (; and *) in the text file.
                 (10) FileNotFoundException
                 (11) IOException
         */
        // Error number corresponds to the errors in ship placement as defined above
        int loadShipsErrorNumber = 0; // 0 = no error
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
                    // Loop through each segment in the current line.
                    for (int lineIndex = 0; lineIndex < lineElements.length; lineIndex++) {
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
                                    System.out.println("Error in setting a ship from the gameSettings text file!");
                                    System.out.println("Ship possibly spelled incorrectly.");
                                    return loadShipsErrorNumber = 8;
                            }
                        }else{ // read in ship coordinates
                            String[] coordElements = lineElements[lineIndex].split("\\*");
                            try {
                                int x = Integer.parseInt(coordElements[0]) - 1;
                                int y = Integer.parseInt(coordElements[1]) - 1;
                                coordinates[lineIndex-1] = new int[] {x,y};
                            }catch(NumberFormatException numberFormatException){
                                System.out.println("Error placing the " + shipType + " ship!");
                                System.out.println("Coordinates have to be numbers seperated with a star.");
                                numberFormatException.printStackTrace();
                                return loadShipsErrorNumber = 9;
                            }
                        }
                    } // end of: loop through line elements

                    // Check if valid ship placement
                    boolean isValid = isValidShipPlacement(coordinates, placedShips);
                    if (isValid == true) {
                        switch (shipType) {
                            case "carrier":
                                if (coordinates.length > ships[3].getShipLength()){
                                    System.out.println("Too many coordinates specified for the" + shipType);
                                    System.out.println("Please edit the ship placement definition txt.");
                                    loadShipsErrorNumber = 6;
                                }else if(coordinates.length < ships[3].getShipLength()){
                                    System.out.println("Too few coordinates specified for the" + shipType);
                                    System.out.println("Please edit the ship placement definition txt.");
                                    loadShipsErrorNumber = 5;
                                }
                                this.ships[3].setShipCoordinates(coordinates);
                                placedShips.add(this.ships[3]);
                                break;
                            case "battleship":
                                if (coordinates.length > ships[2].getShipLength()){
                                    System.out.println("Too many coordinates specified for the" + shipType);
                                    System.out.println("Please edit the ship placement definition txt.");
                                    loadShipsErrorNumber = 6;
                                }else if(coordinates.length < ships[2].getShipLength()){
                                    System.out.println("Too few coordinates specified for the" + shipType);
                                    System.out.println("Please edit the ship placement definition txt.");
                                    loadShipsErrorNumber = 5;
                                }
                                this.ships[2].setShipCoordinates(coordinates);
                                placedShips.add(this.ships[2]);
                                break;
                            case "submarine":
                                if (coordinates.length > ships[1].getShipLength()){
                                    System.out.println("Too many coordinates specified for the" + shipType);
                                    System.out.println("Please edit the ship placement definition txt.");
                                    loadShipsErrorNumber = 6;
                                }else if(coordinates.length < ships[1].getShipLength()){
                                    System.out.println("Too few coordinates specified for the" + shipType);
                                    System.out.println("Please edit the ship placement definition txt.");
                                    loadShipsErrorNumber = 5;
                                }
                                this.ships[1].setShipCoordinates(coordinates);
                                placedShips.add(this.ships[1]);
                                break;
                            case "destroyer":
                                if (coordinates.length > ships[0].getShipLength()){
                                    System.out.println("Too many coordinates specified for the" + shipType);
                                    System.out.println("Please edit the ship placement definition txt.");
                                    loadShipsErrorNumber = 6;
                                }else if(coordinates.length < ships[0].getShipLength()){
                                    System.out.println("Too few coordinates specified for the" + shipType);
                                    System.out.println("Please edit the ship placement definition txt.");
                                    loadShipsErrorNumber = 5;
                                }
                                this.ships[0].setShipCoordinates(coordinates);
                                placedShips.add(this.ships[0]);
                                break;
                            default:
                        }
                    }else{
                        System.out.println("Invalid placement of ships. Please edit the ship placement definition txt.");
                        System.out.println("Possible causes:\n" +"- Ship off of board\n"
                                                                +"- Ship placed on another ship\n"
                                                                +"- Ship is not continuous.");
                        loadShipsErrorNumber = 1; // Corresponding to errors 1, 2, or 7
                    }  // end of: Check if placements were valid
                }
                lineNum = lineNum + 1;
            } // end of: loop through lines in file

            // Final checks on the numbers of ships defined:
            if (lineNum < 5){
                System.out.println("Invalid placement of ships. Too few ships defined!");
                loadShipsErrorNumber = 3;
            }else if(lineNum > 5){
                System.out.println("Invalid placement of ships. Too many ships defined!");
                loadShipsErrorNumber = 4;
            }else {  // if all check passed
                if (loadShipsErrorNumber == 0) {
                    this.setShipPlacement();
                }else{
                    this.placeShipsRandomly();
                }
            }

        } catch(FileNotFoundException fileNotFoundException){
            fileNotFoundException.printStackTrace();
            return loadShipsErrorNumber = 10;
        } catch(IOException ioException) {
            ioException.printStackTrace();
            return loadShipsErrorNumber = 11;
        }
        return loadShipsErrorNumber;
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

    public String getTextBasedBoard(){
        /**
         Shows a text-based representation of the board with the locations of the ships if they are visible.
         @param None
         @return boardText: String containing the board with all of it's tiles.
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

    public char[][] getCharBasedBoard(){
        int xSize = BOARD_SIZE[0];
        int ySize = BOARD_SIZE[1];
        char[][] boardColours = new char[xSize][ySize];
        for (int xIndex=0; xIndex<xSize; xIndex++) {
            for (int yIndex = 0; yIndex < ySize; yIndex++) {
                boardColours[xIndex][yIndex] = board[xIndex][yIndex].getTileType();
            }
        }
        return boardColours;
    }

    // TODO look at merging all attack sequence methods to one class
    public int pointsForHit(int[] coordinates){
        int pointsForHit = 0;

        // Check if spot has already been attacked
        if ( board[coordinates[0]][coordinates[1]].getHiddenStatus() == false ){
            System.out.println("Tile has already been attacked.");
            // -1 = flag that player attacked the same tile
            return pointsForHit = -1;
        }else{
            // Change board display
            board[coordinates[0]][coordinates[1]].setHiddenStatus(false);
            // Check if a ship is present on the attacked coordinates:
            for (Ship ship : ships){
                boolean isHit = ship.isShipHit(coordinates);
                if (isHit){
                    // Get corresponding points for ship hit
                    pointsForHit = ship.getShipPoints();
                    return pointsForHit;
                }
            }
        }
        return pointsForHit;
    }

    public boolean areAllShipsSunk(){
        boolean allSunk = true;
        for (Ship ship : this.ships){
            if (ship.isSunk()==false){
                return allSunk = false;
            }
        }
        return allSunk;
    }
}

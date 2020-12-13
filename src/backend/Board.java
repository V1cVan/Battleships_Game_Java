package backend;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;

/**
 * The Board class manages each player's board during the game.
 * It is responsible for placing ships either randomly or from the user provided text files,
 *      checking whether or not all the ships on the board have been sunk (game end),
 *      the number of points received for attacking a specific tile,
 *      and providing the current status of the board at the start of each turn.
 * It hosts a number of tile objects relative to the board size, and all the ship objects placed on the board.
 * The Board class is instantiated twice in the game, once for each player.
 * Board has a single property BOARD_SIZE.
 */

public class Board {

    // Private variables of a board:
    private final int[] BOARD_SIZE;     // BOARD_SIZE = number of rows x number of columns. E.g. {8,8}
    private Tile[][] board;             // All tiles of the board
    private Ship[] ships = new Ship[4]; // All ships placed on the board

    // Board class constructor, set when game is started
    public Board(int[] boardSize) {
        BOARD_SIZE = boardSize;

        // Initialise board's individual tiles
        board = new Tile[BOARD_SIZE[0]][BOARD_SIZE[1]]; // board initialised as number of tile objects
        for (int x = 0; x < BOARD_SIZE[0]; x++) {
            for (int y = 0; y < BOARD_SIZE[1]; y++) {
                // for each x and y position on the board instantiate a "water" tile
                board[x][y] = new Tile('w');
            }
        }

        // Initialise ships on the board. Coordinates null because ships have not been placed yet.
        ships[0] = new Ship(1, 2, "Destroyer", null);
        ships[1] = new Ship(2, 3, "Submarine", null);
        ships[2] = new Ship(3, 4, "Battleship", null);
        ships[3] = new Ship(4, 5, "Carrier", null);
    }

    private int[][] generateShipCoordinates(Ship currentShip){
        /*
         * Generates random ship coordinates by randomly setting a ships location and orientation.
         * @param Ship currentShip: Ship who's coordinates are being generated
         * @return shipCoords: Ship's generated coordinates in the form of an array of (x,y) positions
         */

        Random randValue = new Random();
        int[] shipLocation;
        int shipOrientation;

        // Generate random ship start location (within the board dimensions) as a (x,y) position
        shipLocation = new int[] {randValue.nextInt(BOARD_SIZE[0]), randValue.nextInt(BOARD_SIZE[1])};

        // Generate random ship orientation
        shipOrientation = randValue.nextInt(2);  // 0 = horizontal, 1 = vertical

        // Get the coordinates for each ship given its location and orientation as function of it's length
        int[][] shipCoords = new int[currentShip.getShipLength()][2];  // Format: (x0,y0), (x1,y1), ... , (xN,yN)

        if (shipOrientation == 0) { // If horizontal
            for (int i = 0; i < currentShip.getShipLength(); i++) {
                // if a ship is horizontally oriented the number of x-coordinates increases
                shipCoords[i][0] = shipLocation[0] + i;     // x-coordinate
                shipCoords[i][1] = shipLocation[1];         // y-coordinate
            }
        }else {                     // if vertical
            for (int i = 0; i < currentShip.getShipLength(); i++) {
                // if a ship is vertically oriented the number of y-coordinates increases
                shipCoords[i][0] = shipLocation[0];         // x-coordinate
                shipCoords[i][1] = shipLocation[1] + i;     // y-coordinate
            }
        }
        return shipCoords;
    }

    private boolean isValidShipPlacement(int[][] coordinates, ArrayList<Ship> placedShips){
        /*
         * Checks if a ship's placement on the board is valid based on the following criteria:
         *    1) Checks if ship is placed on the board (a ship is placed within the board's bounds)
         *    2) Checks if ship placements overlap (a ship is placed on another ship)
         *    3) Checks if ship is disjoint (a ship is not continuously placed on the board)
         * Method is used when ships are randomly placed and when a user specifies a ships placement.
         * @param coordinates: The coordinates of the ship to be placed on the board
         *        placedShips: Ships objects that have already been placed on the board
         * @return isValid: Whether or not placement was valid.
         */

        // By default a ship's placement is assumed to be valid and is made false if it fails one of the tests
        boolean isValid = true;

        // Check if ship is out of board's bounds (only relevant for user defined ship placements)
        for (int[] i : coordinates){
            for (int k : i) {
                // if x or y coordinates are negative or out of the board size bounds the placement is not valid
                if (k < 0 | k > BOARD_SIZE[0] - 1 | k > BOARD_SIZE[1] - 1) {
                    return isValid = false;
                }
            }
        }

        // Check if a ship placement overlaps with another ship's placement
        if (placedShips != null) {                          // Only check if another ship has already been paced
            for (Ship ship : placedShips) {                 // Loop through each placed ship
                for (int[] i : ship.getShipCoordinates()){  // Loop through each ship's coordinates
                    for (int[] j : coordinates){
                        if (i[0]==j[0] & i[1]==j[1]) {      // Check if ships have the same defined coordinates
                            return isValid = false;
                        }
                    }
                }
            }
        }

        // Check if a ship is continuously placed on the board (only relevant for user defined ship placements)
        for (int i=0; i<coordinates.length-1; i++){
            int[] currLoc = coordinates[i];
            int[] nextLoc = coordinates[i+1];
            // If the x- or y-coordinates of a ship are more than 1 space apart the ship is discontinuous
            if ((Math.abs(currLoc[0] - nextLoc[0]) != 1 | Math.abs(currLoc[1] - nextLoc[1]) != 1) == false){
                return isValid = false;
            }
        }

        return isValid;
    }

    public void placeShipsRandomly(){
        /*
         * Assigns coordinates to each ship object by regenerating a ship's random location and orientation
         * until valid coordinates are found (in accordance to checks done by isValidShipPlacement()).
         */
        ArrayList<Ship> placedShips = new ArrayList<>();
        // Place each of the different ships:
        for (int shipIndex=0; shipIndex<4; shipIndex++) {  // Loop through each type of ship to be placed randomly
            Boolean validCoordsFound = false;
            while (validCoordsFound == false) {            // Repeatedly place ships until valid location found
                int[][] shipCoords = generateShipCoordinates(this.ships[shipIndex]); // Generate ship location
                Boolean isValid = isValidShipPlacement(shipCoords, placedShips);     // Check if placement valid
                if (isValid == true) {
                    // If ship placement is valid, set the ship's coordinates (not placing on
                    this.ships[shipIndex].setShipCoordinates(shipCoords);
                    placedShips.add(this.ships[shipIndex]);
                    validCoordsFound = true;
                }
            }
        }
        /* If all the random ship coordinates have assigned, and they are all valid,
        they are placed on the board's tiles in the setShipPlacementFile */
        this.setShipPlacement();
    }

    public int readShipPlacementFile(String filename){
        /*
         * Reads in user defined ship placement coordinates from the provided text files.
         * The validity of the ship placements are checked before they are assigned to the board's tiles.
         * If placements fail the validity check the ships are placed randomly (increase game fluidity)
         *      and the user is provided with the possible reason why the placement of the ships failed.
         * @param filename = the name of the file that the ship's placements are defined
         * @return An integer corresponding to success (0) or errors incurred during the placement of the ships
         * @throws ErrorsInShipPlacement: (1) Out of board bounds
         *                                (2) Overlapping ships
         *                                (3) Too few ships specified
         *                                (4) Too many ships specified
         *                                (5) Too few coordinates specified for a ship
         *                                (6) Too many coordinates specified for a ship
         *                                (7) Ships placements are not continuous
         *         ErrorsInShipNaming:    (8) Incorrect types of ships defined
         *         NumberFormatException: (9) Inputting letters instead of numbers for ship coordinates or using
         *                                     the incorrect separators (; and *) in the text file.
         *         FileNotFoundException  (10) Providing the wrong file name for the placement of the ships
         *         IOException            (11)
         */

        /* Error number corresponds to the errors in ship placement as defined above.
           It is assumed that the ship placements are valid until a error is found. */
        int loadShipsErrorNumber = 0;                   // 0 = no error
        ArrayList<Ship> placedShips = new ArrayList<>();// Already placed ships. Used in placement validity check.
        int[][] coordinates = null;                     // Coordinates of current ship being considered
        String shipType = null;                         // Type of ship being considered
        try(
                FileReader file = new FileReader(filename);
                Scanner scan = new Scanner(file);
        ){
            scan.useDelimiter(";");
            int lineNum = 0;
            // Loop through each line of file
            while (scan.hasNext()) {
                String line = scan.nextLine();
                if (lineNum > 0) {
                    String[] lineElements = line.split("\\;");
                    // Loop through each segment in the current line
                    for (int lineIndex = 0; lineIndex < lineElements.length; lineIndex++) {
                        lineElements[lineIndex] = lineElements[lineIndex].strip();
                        // Initialise the ship coordinates from the specifications in the text file
                        if (lineIndex == 0){ // First index corresponds to the type of the ship
                            shipType = lineElements[lineIndex].toLowerCase();
                            // Initialise the length of the coordinates variable based on the type of ship
                            switch (shipType) {
                                case "carrier" -> coordinates = new int[ships[3].getShipLength()][2];
                                case "battleship" -> coordinates = new int[ships[2].getShipLength()][2];
                                case "submarine" -> coordinates = new int[ships[1].getShipLength()][2];
                                case "destroyer" -> coordinates = new int[ships[0].getShipLength()][2];
                                default -> {
                                    System.out.println("Error in setting a ship from the gameSettings text file!");
                                    System.out.println("Ship possibly spelled incorrectly.");
                                    return loadShipsErrorNumber = 8;
                                }
                            }
                        }else{ // Read in ship coordinates corresponding to the other indexes of the current line
                            String[] coordElements = lineElements[lineIndex].split("\\*");
                            try {
                                // Define x- and y-coordinates. Text file scaled 1..N, program coordinate scale 0..N-1
                                int x = Integer.parseInt(coordElements[0]) - 1;
                                int y = Integer.parseInt(coordElements[1]) - 1;
                                coordinates[lineIndex-1] = new int[] {x,y};
                            }catch(NumberFormatException numberFormatException){
                                System.out.println("Error placing the " + shipType + " ship!");
                                System.out.println("Coordinates have to be numbers separated with a star.");
                                numberFormatException.printStackTrace();
                                return loadShipsErrorNumber = 9;
                            }
                        }
                    } // end of: loop through line elements

                    // Check if valid ship placement
                    boolean isValid = isValidShipPlacement(coordinates, placedShips);
                    // If placement valid, switch block checks if too many/too few coordinates were specified per ship type
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
                                /* If placement is valid, and the ship has the proper amount of coordinates, the ships
                                   object's coordinates are set for the carrier */
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
                                /* If placement is valid, and the ship has the proper amount of coordinates, the ships
                                   object's coordinates are set for the battleship */
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
                                /* If placement is valid, and the ship has the proper amount of coordinates, the ships
                                   object's coordinates are set for the submarine */
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
                                /* If placement is valid, and the ship has the proper amount of coordinates, the ships
                                   object's coordinates are set for the destroyer */
                                this.ships[0].setShipCoordinates(coordinates);
                                placedShips.add(this.ships[0]);
                                break;
                            default:
                        }
                    }else{  // If ship placement was not valid
                        System.out.println("Invalid placement of ships. Please edit the ship placement definition txt.");
                        System.out.println("Possible causes:\n" +"- Ship off of board\n"
                                                                +"- Ship placed on another ship\n"
                                                                +"- Ship is not continuous.");
                        loadShipsErrorNumber = 1; // Corresponding to errors 1, 2, or 7
                    }  // end of: Check if placements were valid
                }
                lineNum = lineNum + 1;
            } // end of: loop through lines in file

            // Final checks on the numbers of ships defined in the user text file
            if (lineNum < 5){
                System.out.println("Invalid placement of ships. Too few ships defined!");
                loadShipsErrorNumber = 3;
            }else if(lineNum > 5){
                System.out.println("Invalid placement of ships. Too many ships defined!");
                loadShipsErrorNumber = 4;
            }else {
                if (loadShipsErrorNumber == 0) {
                    // If all checks are passed the ship coordinates are placed on the board
                    this.setShipPlacement();
                }else{
                    // If a check is failed the boats are placed randomly so that the game can still be played
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
        // Return the error number so that the user knows what to fix in the provided text files
        return loadShipsErrorNumber;
    }

    private void setShipPlacement() {
        /*
         * Sets all the ship locations on the board's tiles.
         * Called after ships coordinates are randomly defined and if they are read from the user provided text files.
         * Only called after validity checks on both methods have been performed.
         */
        for (Ship ship : this.ships){                           // Loop through each of the boards ships
            for (int[] coordPair : ship.getShipCoordinates()){  // Loop through each coordinate pair
                int x = coordPair[0];
                int y = coordPair[1];
                board[x][y].setTile(ship.getShipSymbol());      // Set tile type to the ship type
            }
        }
    }

    public String getTextBasedBoard(){
        /*
         * Shows a text-based representation of the board with the locations of the ships if they are visible.
         * Only used in the nonGui version of the game.
         * @return boardText: String containing the board with all of it's tiles that can be printed to the console.
         */
        String boardText = "";
        for (int x=0; x<BOARD_SIZE[0]; x++) {
            for (int y = 0; y < BOARD_SIZE[1]; y++) {
                boardText = boardText + "[" + board[x][y].getTileType() + "]";
            }
            boardText = boardText + "\n";
        }
        return boardText;
    }

    public char[][] getCharBasedBoard(){
        /*
         * Returns an tensor of characters corresponding to what the user sees while playing the game.
         * @return Tensor of char-based tile symbols
         */
        int xSize = BOARD_SIZE[0];
        int ySize = BOARD_SIZE[1];
        char[][] boardCharacters = new char[xSize][ySize];
        for (int x=0; x<xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                boardCharacters[x][y] = board[x][y].getTileType();
            }
        }
        return boardCharacters;
    }

    public int pointsForAttack(int[] coordinates){
        /*
         * Returns the points a player can receive for attacking a specific tile at the specified coordinates.
         * @return Integer value corresponding to the points received for attacking a tile.
         *              (-1) if the tile has already been attacked. Flag that user can try again.
         *              (0) if player receives no points
         *              () otherwise, points correspond the the type of ship hit and whether it was sunk with that shot
         */

        int pointsForAttack = 0;

        // Check if spot has already been attacked
        if ( board[coordinates[0]][coordinates[1]].getHiddenStatus() == false ){
            System.out.println("Tile has already been attacked.");
            // -1 = flag that player attacked the same tile and can go again
            return pointsForAttack = -1;
        }else{
            // Change board display if a tile has not already been attacked
            board[coordinates[0]][coordinates[1]].setHiddenStatus(false);

            // Check if a ship is present on the attacked coordinates:
            for (Ship ship : ships){
                boolean isHit = ship.isShipHit(coordinates);
                if (isHit){
                    // Get corresponding points for ship hit
                    pointsForAttack = ship.getShipPoints();
                    return pointsForAttack;
                }
            }
        }
        return pointsForAttack;
    }

    public boolean areAllShipsSunk(){
        /*
         * Checks if all the ships on the board have been sunk. Used for end-of-game trigger.
         * @return boolean value: true if all ships are sunk and game is over
         *                        false if not and game can continue
         */
        boolean allSunk = true;
        for (Ship ship : this.ships){
            if (ship.isSunk()==false){
                return allSunk = false;
            }
        }
        return allSunk;
    }
}

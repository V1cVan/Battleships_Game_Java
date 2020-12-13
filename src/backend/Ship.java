package backend;

/**
 * The ship class is used to define each ship object placed on the battleships board.
 * Ship types:
 *      d - destroyer
 *      s - submarine
 *      b - battleship
 *      c - carrier
 * Each type of ship is instantiated once per battleships board.
 * Ships have the following properties:
 *      Coordinates - Location of each part of the ship on the board
 *      Points      - number of points received for hitting a ship
 *      Type        - The type of the ship
 *      Length      - The length of the ship
 *      hitCount    - The number of times a ship has been hit
 */

public class Ship {

    // Variables of a ship (as described above)
    private int[][] SHIP_COORDINATES;   // x,y coordinates for each location of the ship on the board.
    private final int SHIP_POINTS;      // Points between 1 and 4
    private final String SHIP_TYPE;     // Ship name
    private final int SHIP_LENGTH;
    private int hitCount;

    // Ship class constructor set when ships are initialised on the board
    public Ship (int points, int shipLength, String shipType, int[][]shipCoords) {
        this.SHIP_POINTS = points;
        this.SHIP_TYPE = shipType;
        this.SHIP_COORDINATES = shipCoords;
        this.SHIP_LENGTH = shipLength;
        this.hitCount = 0;
    }

    public boolean isSunk(){
        /*
         * Returns whether or not a ship has been sunk by comparing the number of times it has been hit
         *      with the length of the ship.
         * @param None.
         * @return boolean value corresponding to true if a ship has been sunk
         */
        if (this.hitCount == this.SHIP_LENGTH){
            return true;
        }else{
            return false;
        }
    }

    public boolean isShipHit(int[] attackCoordinates){
        /*
         * Determines whether or not the ship has been hit. Attack coordinates are compared with the coordinates of the
         *      ship on the ship to determine if it is hit.
         * @param int[] attackCoordinates corresponds to the attacked location on the board.
         * @return boolean isHit == true when one of the ship's coordinates equals the coordinates of the ship.
         */
        boolean isHit = false;
        for (int i=0; i<this.SHIP_LENGTH; i++){
            boolean xHit = (attackCoordinates[0] == this.SHIP_COORDINATES[i][0]);
            boolean yHit = (attackCoordinates[1] == this.SHIP_COORDINATES[i][1]);
            if (xHit & yHit){
                this.hitCount = this.hitCount + 1;
                return isHit = true;
            }
        }
        return isHit;
    }

    public int[][] getShipCoordinates() {
        return this.SHIP_COORDINATES;
    }

    public int getShipPoints() {
        /*
         * Returns the points received for attacking the ship successfully.
         * Points for hit are doubled if the ship is successfully sunk.
         * @param None
         * @return Points for attacking the ship, dependent to the type of ship.
         */
        if (this.isSunk()){
            // If ship is successfully sunk, return double the points for the attack
            return this.SHIP_POINTS*2;
        }else {
            // Return the points for hitting the ship
            return this.SHIP_POINTS;
        }
    }

    public char getShipSymbol() {
        /*
         * Returns the type of of the ship as the first character of the ship's name.
         * @param None
         * @return Ship's symbol as a lowercase character
         */
        return this.SHIP_TYPE.toLowerCase().charAt(0);
    }

    public int getShipLength(){
        /*
         * Returns the ship's length so that it doesn't have to be calculated in other
         *      classes from the ship's coordinates.
         * @param None
         * @return The ship's length.
         */
        return this.SHIP_LENGTH;
    }

    public void setShipCoordinates(int[][] coordinates){
        /*
         * Sets the coordinates of the ship when it is placed on the board.
         * @param X,Y array of the coordinates of the ship
         * @return None
         */
        this.SHIP_COORDINATES = coordinates;
    }

}

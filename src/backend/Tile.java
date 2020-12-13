package backend;

/**
 * The Tile class is used to define each of the tiles on a battleships game board.
 * Tile types:
 *      w - water (no ship)
 *      d - destroyer type of ship
 *      s - submarine type of ship
 *      b - battleship type of ship
 *      c - carrier type of ship
 *      f - fog of war (if true tile type is hidden)
 * A board with 6 rows and 6 columns with have 36 tile objects instantiated.
 * Each tile has an associated type and hidden status.
 * The hidden status corresponds to the fog of war covering a tile at the start of the game before it has been attacked.
 */

public class Tile {
    private boolean isHidden;   // Describes whether a tile is visible or not
    private char type;          // Describes the type of tile (possible values - f, w, d, s, b, c)

    // Constructor for the Tile class
    public Tile(char typ){
        this.type = typ;        // Type is initialised to either water or a type of ship
        this.isHidden = true;   // Initially all tiles on the board are hidden
    }

    public char getTileType() {
        /*
          Returns the type of a tile if it is visible otherwise it returns the grey fog of war
          @param None
         * @return Char corresponding to true tile type if tile is not hidden. f char (fog of war) if tile is hidden.
         */
        if (this.isHidden){
            return 'f';
        }else {
            return this.type;
        }
    }

    public void setHiddenStatus(boolean isHidden){
        /*
         * Sets the hidden status of the tile.
         * @param boolean isHidden. True when tile is hidden.
         * @return None
         */
        this.isHidden = isHidden;
    }

    public boolean getHiddenStatus(){
        /*
         * Returns the hidden status of a tile.
         * @param None
         * @return boolean isHidden status of a tile. True when tile is hidden.
         */
        return this.isHidden;
    }

    public void setTile(char tileType) {
        /*
         * Sets the tile type when ships are placed on the board.
         * @param char tileType corresponding to the character representing the tile type
         * @return None
         */
        this.type = tileType;
    }

}

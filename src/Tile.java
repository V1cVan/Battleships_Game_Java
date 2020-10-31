public class Tile {
    /*
    Colour of tiles:
        f = grey fog-of-war
        b = water
        g = green ship - Destroyer
        p = purple ship - Submarine
        r = red ship - Battleship
        y = yellow ship - Carrier
    */
    private char tileColour = 'f';

    public char getTileColour() {
        return tileColour;
    }

    public void setTileColour(char tileColour) {
        this.tileColour = tileColour;
    }
}

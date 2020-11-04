public class Tile {
    /*
    Colour of tiles:
        f = grey fog-of-war
        w = water
        d = green ship - Destroyer; length 2
        s = purple ship - Submarine; length 3
        b = red ship - Battleship; length 4
        c = yellow ship - Carrier; length 5
    */
    private char type;
    private int[] location;

    public Tile(char typ, int[] loc){
        type = typ;
        location = loc;
    }

    public char getTileType() {
        return type;
    }

    public void setTile(char tle) {
        this.type = tle;
    }

    public int[] getLocation() {
        return location;
    }
}

package backend;

public class Tile {
    /*
    Colour of tiles:
        f = grey fog-of-war
        w = water
        d = green ship - Destroyer; length 2
        s = magenta ship - Submarine; length 3
        b = red ship - Battleship; length 4
        c = yellow ship - Carrier; length 5
    */
    private boolean isHidden;
    private char type;
    private int[] location;

    public Tile(char typ, int[] loc){
        this.type = typ;
        this.location = loc;
        this.isHidden = true;
    }

    public char getTileType() {
        if (this.isHidden == true){
            return 'f';
        }else {
            return this.type;
        }
    }

    public void setHiddenStatus(boolean hidden){
        this.isHidden = hidden;
    }

    public boolean getHiddenStatus(){
        return this.isHidden;
    }

    public void setTile(char tle) {
        this.type = tle;
    }

}

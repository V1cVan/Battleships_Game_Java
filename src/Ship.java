

public class Ship {

    private int[][] SHIP_COORDINATES;
    private final int SHIP_POINTS;
    private final char SHIP_TYPE;
    private final int SHIP_LENGTH;
    private int hitCount;

    // Ship class constructor
    public Ship (int pnts, int length, char type, int[][]shipCoords) {
        this.SHIP_POINTS = pnts;
        this.SHIP_TYPE = type;
        this.SHIP_COORDINATES = shipCoords;
        this.SHIP_LENGTH = length;
        this.hitCount = 0;
    }

    public boolean isSunk(){
        if (this.hitCount == this.SHIP_LENGTH){
            return true;
        }else{
            return false;
        }
    }

    public int[][] getShipCoordinates() {
        return this.SHIP_COORDINATES;
    }

    public int getShipPoints() {
        return this.SHIP_POINTS;
    }

    public char getShipType() {
        return this.SHIP_TYPE;
    }

    public int getShipLength(){
        return this.SHIP_LENGTH;
    }

    public void setShipCoordinates(int[][] coords){
        this.SHIP_COORDINATES = coords;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }
}

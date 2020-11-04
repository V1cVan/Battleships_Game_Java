

public class Ship {

    private int[][] SHIP_COORDINATES;
    private final int SHIP_POINTS;
    private final char SHIP_TYPE;
    // Ship class constructor
    public Ship (int pnts, char typ) {
        SHIP_POINTS = pnts;
        SHIP_TYPE = typ;
    }

    public int[][] getShipCoordinates() {
        return SHIP_COORDINATES;
    }

    public int getShipPoints() {
        return SHIP_POINTS;
    }

    public char getShipType() {
        return SHIP_TYPE;
    }

    public void setShipCoordinates(int[][] coords){
        SHIP_COORDINATES = coords;
    }
}

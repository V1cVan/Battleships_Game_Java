package backend;

public class Ship {

    private int[][] SHIP_COORDINATES;
    private final int SHIP_POINTS;
    private final String SHIP_TYPE;
    private final int SHIP_LENGTH;
    private int hitCount;

    // Ship class constructor
    public Ship (int pnts, int length, String type, int[][]shipCoords) {
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

    public boolean isShipHit(int[] attackCoordinates){
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
        return this.SHIP_POINTS;
    }

    public String getShipType() {
        return this.SHIP_TYPE;
    }

    public char getShipSymbol() {
        char shipSymbol = this.SHIP_TYPE.toLowerCase().charAt(0);
        return shipSymbol;
    }

    public int getShipLength(){
        return this.SHIP_LENGTH;
    }

    public void setShipCoordinates(int[][] coords){
        this.SHIP_COORDINATES = coords;
    }

}

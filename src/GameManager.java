// Import packages:


public class GameManager {
    // DONE Different board sizes
    // DONE Implement random ship placement
    // TODO Implement ship placement via input file
    // TODO Implement scoring system x2 (different scales)
    // TODO Saving previous game scores
    // TODO Implement GUI

    public static void main(String[] args) {
        int[] boardSize = {8,8};  // Board size in the form: [numberRows x numberColumns]
        Board boardPlayer1 = new Board(boardSize,true);
        boardPlayer1.placeShipsRandomly();
        System.out.println("Player 1 Board:");
        boardPlayer1.showShipPlacement();

        Board boardPlayer2 = new Board(boardSize,true);
        boardPlayer2.placeShipsRandomly();
        System.out.println("Player 2 Board:");
        boardPlayer2.showShipPlacement();
    }

    /*
        CHILD CLASSES SHOULD NEVER REFERENCE THEIR PARENTS!
            board knows about ship and ship doesnt know about the board

        Admin class (settings package) - controlling menu of the game
        main class
            keep track of whos turn it is

        Scorecounter class
            adjust score (player hashmap)
        Board manager class (board hashmap)

        Ship class - List of ships with coordinates
            get and set for dimensions and coordinates

        Start here and then if it gets too large you can make board and player classes if needed.
     */
}

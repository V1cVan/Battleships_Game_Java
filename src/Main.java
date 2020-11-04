// Import packages:
import java.io.File;  // File reading class
import java.io.FileNotFoundException;  // File error handling class
import java.util.Scanner;  // Text scanner class
import java.util.Random;  // Random number generation class

public class Main {

    // TODO Implement random ship placement
    // TODO Implement different board sizes
    // TODO Implement scoring system x2 (different scales)
    // TODO Implement ship placement via input file
    // TODO Implement GUI

    public static void main(String[] args) {
        int[] boardSize = {8,8};  // Board size in the form: [boardSize x boardSize]
        Board boardPlayer1 = new Board(boardSize,true);
        System.out.println("Player 1 Board:");
        System.out.println(boardPlayer1.showBoard());
        boardPlayer1.placeShipsRandomly();

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

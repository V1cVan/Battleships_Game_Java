// Import packages:
import java.io.File;  // File reading class
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;  // File error handling class
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;  // Text scanner class

public class GameManager {
    // DONE Different board sizes
    // DONE Implement random ship placement
    // TODO Implement ship placement via input file
    // TODO Implement scoring system x2 (different scales)
    // TODO Saving previous game scores
    // TODO Implement GUI

    public static void main(String[] args) {
        String gameFilePlayer1 = new String("src/gameSettingsPlayer1.txt");
        String gameFilePlayer2 = new String("src/gameSettingsPlayer2.txt");
        String playerScoreboardFile = new String("src/playerScoreboard.txt");

        boolean boardSizeFromFile = true;
        int[] boardSizePlayer1;
        int[] boardSizePlayer2;
        if (boardSizeFromFile == true){
            boardSizePlayer1 = readBoardSize(gameFilePlayer1);
            boardSizePlayer2 = readBoardSize(gameFilePlayer2);
        }else{
            // TODO get boardsize from GUI
            // Board size in the form: [numberRows x numberColumns]
            boardSizePlayer1 = new int[] {8,8};
            boardSizePlayer2 = new int[] {8,8};
        }

        Board boardPlayer1 = new Board(boardSizePlayer1);
//        boardPlayer1.placeShipsRandomly();
        boardPlayer1.readShipPlacementFile(gameFilePlayer1);
        System.out.println("Player 1 Board:");
        System.out.println(boardPlayer1.showBoard());

        Board boardPlayer2 = new Board(boardSizePlayer2);
        //boardPlayer2.placeShipsRandomly();
        boardPlayer2.readShipPlacementFile(gameFilePlayer2);
        System.out.println("Player 2 Board:");
        System.out.println(boardPlayer2.showBoard());
    }

    private static int[] readBoardSize(String filename) {
        /**
         Reads the board size from the provided files.
         @param filename = file read from
         @return boardSize
         @throws FileNotFoundException, IOException, Invalid boardSize
         */
        int boardDimension = 8;
        try(
                FileReader file = new FileReader(filename);
                Scanner scan = new Scanner(file);)
        {
            String line = scan.nextLine();
            line = line.strip();
            if (line != null){
                try {
                    boardDimension = Integer.parseInt(line);
                }catch(NumberFormatException numberFormatException){
                    System.out.println("Problem with board size definition in gameSettingsPlayer2.txt");
                    System.exit(0);
                }
            }else { // invalid board size
                System.out.println("Problem with board size definition in gameSettingsPlayer1.txt");
                System.out.println("Setting default board size of (Xsize,Ysize) = (8,8)");
            }
        } catch(FileNotFoundException fileNotFoundException){
            fileNotFoundException.printStackTrace();
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }
        int[] boardSize = new int[] {boardDimension, boardDimension};
        return boardSize;
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

// Import packages:
import backend.Board;
import backend.Player;
import javax.swing.SwingUtilities;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.lang.Thread;

public class BattleshipsGameMain {
    // DONE Different board sizes
    // DONE Implement random ship placement
    // DONE Implement ship placement via input file
    // DONE Implement scoring system x2 (different scales)
    // DONE Playing loop
    // TODO Saving previous game scores
    // TODO Implement GUI and check integration!

    private static Board boardPlayerOne;
    private static Board boardPlayerTwo;
    private static Player playerOne;
    private static Player playerTwo;

    // You can adjust this boolean to disable the Gui and play from the console.
    // (game functionality is fully present without GUI interface)
    private static boolean playWithoutGUI = true;

    public BattleshipsGameMain(){}

    public static void initBoards(int[] boardSizePlayerOne, int[] boardSizePlayerTwo){
        boardPlayerOne = new Board(boardSizePlayerOne);
        boardPlayerTwo = new Board(boardSizePlayerTwo);
    }

    public static void placeShips(boolean isPlaceShipsFromFile, String playerOneFileName, String playerTwoFileName){
        if (isPlaceShipsFromFile == true){
            boardPlayerOne.readShipPlacementFile(playerOneFileName);
            boardPlayerTwo.readShipPlacementFile(playerTwoFileName);
        }else{ // Place ships randomly
            boardPlayerOne.placeShipsRandomly();
            boardPlayerTwo.placeShipsRandomly();
        }
    }

    public static void initPlayers(String playerOneName, String playerTwoName,
                           boolean isPlayerOneDisadvantaged, boolean isPlayerTwoDisadvantaged){
        playerOne = new Player(playerOneName, isPlayerOneDisadvantaged);
        playerTwo = new Player(playerTwoName, isPlayerTwoDisadvantaged);
    }

    public int getPlayerOneScore(){
        return playerOne.getScore();
    }

    public int getPlayerTwoScore(){
        return playerTwo.getScore();
    }

    public static int[] readBoardSize(String filename) {
        /**
         Reads the board size from the provided files.
         @param filename = file read from
         @return boardSize
         @throws FileNotFoundException, IOException, Invalid boardSize
         */

        // boardDimension defaults to 8 if not successfully read from file
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
                    System.out.println("Problem with board size definition in gameSettings*.txt");
                    System.out.println("Setting default board size of (Xsize,Ysize) = (8,8)");
                }
            }else { // invalid board size
                System.out.println("Problem with board size definition in gameSettings*.txt");
                System.out.println("Setting default board size of (Xsize,Ysize) = (8,8)");
            }
        } catch(FileNotFoundException fileNotFoundException){
            System.out.println("Problem with board size definition. We cannot find the file, check spelling please.");
            System.out.println("Setting default board size of (Xsize,Ysize) = (8,8)");
            fileNotFoundException.printStackTrace();
        } catch(IOException ioException) {
            System.out.println("Problem with board size definition.");
            System.out.println("Setting default board size of (Xsize,Ysize) = (8,8)");
            ioException.printStackTrace();
        }
        int[] boardSize = new int[] {boardDimension, boardDimension};
        return boardSize;
    }

    public static void main(String[] args) throws InterruptedException {
        // play game with GUI
        if (playWithoutGUI == false) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    SplashGuiManager splashGui = new SplashGuiManager();
                }
            });
        // play without GUI
        }else{
            // Initialise player boards:
            String gameFilePlayer1 = new String("src/gameSettingsPlayer1.txt");
            String gameFilePlayer2 = new String("src/gameSettingsPlayer2.txt");
            String playerScoreboardFile = new String("src/playerScoreboard.txt");

            boolean boardSizeFromFile = false;
            int[] boardSizePlayer1;
            int[] boardSizePlayer2;
            if (boardSizeFromFile == true){
                boardSizePlayer1 = readBoardSize(gameFilePlayer1);
                boardSizePlayer2 = readBoardSize(gameFilePlayer2);
            }else{
                // Board size in the form: [numberRows x numberColumns]
                int boardNumRows = 8;
                int boardNumColumns = 8;
                boardSizePlayer1 = new int[] {boardNumRows,boardNumColumns};
                boardSizePlayer2 = new int[] {boardNumRows,boardNumColumns};
            }

            initBoards(boardSizePlayer1, boardSizePlayer2);

            boolean placeShipsRandomly = !boardSizeFromFile;
            if (placeShipsRandomly){
                boardPlayerOne.placeShipsRandomly();
                boardPlayerTwo.placeShipsRandomly();
            }else{
                boardPlayerOne.readShipPlacementFile(gameFilePlayer1);
                boardPlayerTwo.readShipPlacementFile(gameFilePlayer2);
            }

            // Initialise players and scoreboard:
            String playerOneName = "Lecturer";
            String playerTwoName = "Student";
            initPlayers(playerOneName, playerTwoName, false, false);

            // Play the game:
            boolean gameRunning = true;
            boolean isPlayer1Turn = true;
            Scanner userInput = new Scanner(System.in);
            System.out.println("\nGame has begun ... \n");
            while (gameRunning) {
                System.out.println("Current Score:");
                System.out.println(playerOne.getName()+" = "+ String.valueOf(playerOne.getScore()));
                System.out.println(playerTwo.getName()+" = "+ String.valueOf(playerTwo.getScore())+"\n");
                if (isPlayer1Turn == false) {  // Player 2 turn
                    System.out.println(playerTwo.getName()+"'s turn! Take your shot.\n");
                    System.out.println(boardPlayerOne.showBoard());
                }else {  // Player 1 turn
                    System.out.println(playerOne.getName()+"'s Turn! Take your shot...\n");
                    System.out.println(boardPlayerTwo.showBoard());
                }

                // Take shot at board:
                // Get user input for attack coordinates
                System.out.println("Enter attack coordinates (x=1..8, y=1..8) {without brackets and separated with comma}");
                String input = userInput.nextLine();
                int x = Integer.parseInt(input.split(",")[0]);
                int y = Integer.parseInt(input.split(",")[1]);
                int[] attackCoordinates = new int[] {x-1,y-1};
                if (isPlayer1Turn == false) {  // Player 2 turn
                    // See if player gets points for shot
                    int pointsForHit = boardPlayerOne.pointsForHit(attackCoordinates);
                    if ( pointsForHit > 0){
                        System.out.println("Hit!");
                        Thread.sleep(700);
                        // Assign points for hit
                        playerTwo.increaseScore(pointsForHit);
                        // Check if game is over
                        if (boardPlayerOne.areAllShipsSunk()){
                            gameRunning = false;
                        }
                    }else{
                        System.out.println("Miss!");
                        Thread.sleep(700);
                    }
                }else{  // Player 1 turn
                    int pointsForHit = boardPlayerTwo.pointsForHit(attackCoordinates);
                    if ( pointsForHit > 0){
                        System.out.println("Hit!");
                        Thread.sleep(700);
                        // Assign points for hit
                        playerOne.increaseScore(pointsForHit);
                        // Check if game is over
                        if (boardPlayerTwo.areAllShipsSunk()){
                            gameRunning = false;
                        }
                    }else{
                        System.out.println("Miss!");
                        Thread.sleep(700);
                    }
                }
            // Next players turn
                isPlayer1Turn = !isPlayer1Turn;

            } // end of game loop

            if (playerOne.getScore()>playerTwo.getScore()){
                System.out.println(playerOne.getName()+" has won the game!");
            }else if (playerOne.getScore()<playerTwo.getScore()){
                System.out.println(playerTwo.getName()+" has won the game!");
            }else{
                System.out.println("The game is a tie!");
            }
        }

    }


}

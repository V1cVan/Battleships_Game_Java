// Import packages:
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class GameManager {
    // DONE Different board sizes
    // DONE Implement random ship placement
    // DONE Implement ship placement via input file
    // DONE Implement scoring system x2 (different scales)
    // DONE Playing loop
    // TODO Saving previous game scores
    // TODO Implement GUI and check integration!

    public static void main(String[] args) {
        // Initialise player boards:
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
        Board boardPlayer2 = new Board(boardSizePlayer2);

        boolean placeShipsRandomly = false;
        if (placeShipsRandomly){
            boardPlayer1.placeShipsRandomly();
            boardPlayer2.placeShipsRandomly();
        }else{
            boardPlayer1.readShipPlacementFile(gameFilePlayer1);
            boardPlayer2.readShipPlacementFile(gameFilePlayer2);
        }

        // Initialise players and scoreboard:
        Player player1 = new Player("victor", false);
        Player player2 = new Player("ines", false);

//        // Play the game:
//        boolean gameRunning = true;
//        // TODO set player turn from GUI
//        boolean player1Turn = true;
//        Scanner userInput = new Scanner(System.in);
//        System.out.println("\nGame has begun ... \n");
//        while (gameRunning) {
//            System.out.println("Current Score:");
//            System.out.println(player1.getName()+" = "+ String.valueOf(player1.getScore()));
//            System.out.println(player2.getName()+" = "+ String.valueOf(player2.getScore())+"\n");
//            if (player1Turn == false) {  // Player 2 turn
//                System.out.println(player2.getName()+"'s turn! Take your shot.\n");
//                System.out.println(boardPlayer1.showBoard());
//            }else {  // Player 1 turn
//                System.out.println(player1.getName()+"'s Turn! Take your shot...\n");
//                System.out.println(boardPlayer2.showBoard());
//            }
//
//            // Take shot at board:
//            // Get user input for attack coordinates
//            System.out.println("Enter attack coordinates (x=1..8, y=1..8) {without brackets and separated with comma}");
//            String input = userInput.nextLine();
//            int x = Integer.parseInt(input.split(",")[0]);
//            int y = Integer.parseInt(input.split(",")[1]);
//            int[] attackCoordinates = new int[] {x-1,y-1};
//            if (player1Turn == false) {  // Player 2 turn
//                // See if player gets points for shot
//                int pointsForHit = boardPlayer1.pointsForHit(attackCoordinates);
//                if ( pointsForHit > 0){
//                    System.out.println("Hit!");
//                    // Assign points for hit
//                    player2.increaseScore(pointsForHit);
//                    // Check if game is over
//                    if (boardPlayer1.areAllShipsSunk()){
//                        gameRunning = false;
//                    }
//                }else{
//                    System.out.println("Miss!");
//                }
//            }else{  // Player 1 turn
//                int pointsForHit = boardPlayer2.pointsForHit(attackCoordinates);
//                if ( pointsForHit > 0){
//                    System.out.println("Hit!");
//                    // Assign points for hit
//                    player1.increaseScore(pointsForHit);
//                    // Check if game is over
//                    if (boardPlayer2.areAllShipsSunk()){
//                        gameRunning = false;
//                    }
//                }else{
//                    System.out.println("Miss!");
//                }
//            }
//        // Next players turn
//        player1Turn = !player1Turn;
//
//        } // end of game loop
//
//        if (player1.getScore()>player2.getScore()){
//            System.out.println(player1.getName()+" has won the game!");
//        }else if (player1.getScore()<player2.getScore()){
//            System.out.println(player2.getName()+" has won the game!");
//        }else{
//            System.out.println("The game is a tie!");
//        }

        // GUI's
        SplashGuiManager splashGui = new SplashGuiManager();
        MainGuiManager mainGui = new MainGuiManager();
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

    private static void createGUI(){

    }

}

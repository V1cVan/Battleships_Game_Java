package frontend;

import main.BattleshipsMain;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * The GameBoardGui is responsible for the main visual interface when playing the game.
 * Here the players take turns at trying to attack the other fleet.
 * The game board gui has the following major elements:
 *      - Each players score
 *      - Which players turn it is
 *      - Buttons for seeing the leaderboard, and exiting the game
 *      - A number of buttons corresponding to the tiles on the board in accordance each players board size.
 */

public class GameBoardGui {
    // Setting elements of of the game board gui
    private final JFrame mainGuiFrame;
    private final JLabel player1ScoreLabel = new JLabel();
    private final JLabel player2ScoreLabel = new JLabel();
    private final JLabel currentTurnLabel = new JLabel();
    private final JPanel boardPanel = new JPanel();

    // Game functionality variables (variables are final as they are received from settings splash screen)
    private final boolean IS_BOARD_SIZE_FROM_FILE;
    private final String PLAYER_ONE_NAME;
    private final String PLAYER_TWO_NAME;
    private final int[] BOARD_SIZE_PLAYER_ONE;
    private final int[] BOARD_SIZE_PLAYER_TWO;

    // Constructor for the game board gui, called when the game is started through the splash screen button
    public GameBoardGui(boolean isBoardSizeFrmFile,
                        String playerOneNme,
                        String playerTwoNme,
                        boolean playerOneFirst,
                        boolean playerOneDisadvantaged,
                        boolean playerTwoDisadvantaged,
                        int[] boardSzePlayerOne,
                        int[] boardSzePlayerTwo,
                        String playerOneLayoutFileNme,
                        String playerTwoLayoutFileNme) {

        // Set constants of the game from the splash screen
        IS_BOARD_SIZE_FROM_FILE = isBoardSizeFrmFile;
        PLAYER_ONE_NAME = playerOneNme;
        PLAYER_TWO_NAME = playerTwoNme;

        // Initialise the game board
        if (IS_BOARD_SIZE_FROM_FILE){
            // Read board size from files if custom ship placement was selected
            BOARD_SIZE_PLAYER_ONE = BattleshipsMain.readBoardSize(playerOneLayoutFileNme);
            BOARD_SIZE_PLAYER_TWO = BattleshipsMain.readBoardSize(playerTwoLayoutFileNme);
        }else{
            // Get board size from spinners on splash screen
            BOARD_SIZE_PLAYER_ONE = boardSzePlayerOne;
            BOARD_SIZE_PLAYER_TWO = boardSzePlayerTwo;
        }

        // Current turn is set depending on whether player one was selected to go first
        BattleshipsMain.setIsPlayerOnesTurn(playerOneFirst);

        // Initialise the two boards in the backend
        BattleshipsMain.initBoards(BOARD_SIZE_PLAYER_ONE, BOARD_SIZE_PLAYER_TWO);
        // Initialise the two players in the backend
        BattleshipsMain.initPlayers(PLAYER_ONE_NAME, PLAYER_TWO_NAME,
                playerOneDisadvantaged, playerTwoDisadvantaged);

        // Place ships on the boards, either randomly or from the text file definitions
        int[] shipPlacementFileErrors = BattleshipsMain.placeShips(
                IS_BOARD_SIZE_FROM_FILE,
                playerOneLayoutFileNme,
                playerTwoLayoutFileNme);

        // Display errors that may have occurred when defining ship placements manually
        this.displayBoatPlacementErrors(shipPlacementFileErrors);

        /*
        Initialise main gui frame (mainGuiFrame). The frame consists of two main elements:
            (1) The information of on who's turn it is and player scores during the game
            (2) The board of buttons
         */
        mainGuiFrame = new JFrame("Battleship");

        // Initialise tiles on board for the player going first
        this.createBoardGui();

        // Set game gui fonts:
        JLabel player1Label = new JLabel();
        player1Label.setFont(new Font("", Font.BOLD, 16));
        player1ScoreLabel.setFont(new Font("", Font.BOLD, 16));
        JLabel turnLabel = new JLabel("Turn:");
        turnLabel.setFont(new Font("", Font.BOLD, 16));
        currentTurnLabel.setFont(new Font("", Font.BOLD, 24));
        JLabel player2Label = new JLabel();
        player2Label.setFont(new Font("", Font.BOLD, 16));
        player2ScoreLabel.setFont(new Font("", Font.BOLD, 16));
        JButton exitButton = new JButton("Quit Game");
        exitButton.setFont(new Font("", Font.PLAIN, 16));
        JButton scoreBoardButton = new JButton("Leaderboard");
        scoreBoardButton.setFont(new Font("", Font.PLAIN, 16));

        // set size of board gui
        boardPanel.setMinimumSize(new Dimension(800,800));

        // (1) Set panel containing game information above the board
        JPanel informationPanel = new JPanel();
        informationPanel.setLayout(new GridLayout(1, 5));
        informationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        informationPanel.add(scoreBoardButton);
        scoreBoardButton.addActionListener(new ScoreboardActionListener());

        // (1) Set Player 1 information on gui
        JPanel player1Panel = new JPanel();
        player1Panel.setLayout(new GridLayout(2,1));
        player1Panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        player1Label.setText(PLAYER_ONE_NAME+"'s score:");
        player1Panel.add(player1Label);
        player1ScoreLabel.setHorizontalAlignment(JLabel.CENTER);
        player1Panel.add(player1ScoreLabel);
        informationPanel.add(player1Panel);

        // (1) Set gui elements for who's turn it is
        JPanel currentTurnPanel = new JPanel();
        currentTurnPanel.setLayout(new GridLayout(2,1));
        currentTurnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        currentTurnPanel.add(turnLabel);
        turnLabel.setHorizontalAlignment(JLabel.CENTER);
        currentTurnLabel.setHorizontalAlignment(JLabel.CENTER);
        currentTurnPanel.add(currentTurnLabel);
        informationPanel.add(currentTurnPanel);

        // (1) Set Player 2 information on gui
        JPanel player2Panel = new JPanel();
        player2Panel.setLayout(new GridLayout(2,1));
        player2Panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        player2Label.setText(PLAYER_TWO_NAME+"'s score:");
        player2Panel.add(player2Label);
        player2ScoreLabel.setHorizontalAlignment(JLabel.CENTER);
        player2Panel.add(player2ScoreLabel);
        informationPanel.add(player2Panel);

        // Place exit button
        exitButton.addActionListener(new ExitActionListener());
        informationPanel.add(exitButton);

        // Add items to main gui for battleships game:
        mainGuiFrame.setLayout(new BorderLayout());
        mainGuiFrame.setLocation(1000, 100);
        mainGuiFrame.setMinimumSize(new Dimension(800,800));
        mainGuiFrame.add(informationPanel, BorderLayout.NORTH);
        mainGuiFrame.add(boardPanel, BorderLayout.CENTER);
        mainGuiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainGuiFrame.pack();
        mainGuiFrame.setVisible(true);
    }

    private void createBoardGui(){
        /*
         * This method creates the board panel containing all of the buttons on each of the tiles on the board.
         * The board panel is re-created (called) after each player takes their turn so that they previous
         *      actions are visible on the board.
         * (2) corresponds to the second element in this gui. See comment on the mainGuiFrame variable.
         * @return None. The board panel class variable is updated.
         */

        // Set current score is refreshed on the screen
        player1ScoreLabel.setText(String.format("%.2f", BattleshipsMain.getPlayerOneScore()));
        player2ScoreLabel.setText(String.format("%.2f", BattleshipsMain.getPlayerTwoScore()));

        // Get current attacked players board in characters
        char[][] boardCharacters = BattleshipsMain.getCurrentPlayerBoardChars();

        // Create board tiles
        JButton[][] boardButton; // buttons of the board tiles

        if (BattleshipsMain.getIsPlayerOnesTurn()){     // if player one's turn
            // Set turn label
            currentTurnLabel.setText(PLAYER_ONE_NAME);
            // Set board of player 2
            boardButton = new JButton[BOARD_SIZE_PLAYER_TWO[0]][BOARD_SIZE_PLAYER_TWO[1]]; // button array size equals board size
            boardPanel.setLayout(new GridLayout(BOARD_SIZE_PLAYER_TWO[0],BOARD_SIZE_PLAYER_TWO[1])); // set layout
            // Loop through each x and y board location and set buttons
            for (int x=0; x<BOARD_SIZE_PLAYER_TWO[0]; x++){
                for (int y=0; y<BOARD_SIZE_PLAYER_TWO[1]; y++){
                    boardButton[x][y] = new JButton();
                    boardButton[x][y].setSize(50,50);
                    boardButton[x][y].addActionListener(
                            new AttackActionListener(x,y)); // Provides button listener with coordinates of the tile
                    boardButton[x][y].setBackground(
                            this.getMatchingColor(boardCharacters[x][y])); // set appropriate colour for the button
                    boardPanel.add(boardButton[x][y]);
                }
            }
        }else{                                          // if player two's turn
            // Set turn label
            currentTurnLabel.setText(PLAYER_TWO_NAME);
            // Set board of player 1
            boardButton = new JButton[BOARD_SIZE_PLAYER_ONE[0]][BOARD_SIZE_PLAYER_ONE[1]]; // button array size equals board size
            boardPanel.setLayout(new GridLayout(BOARD_SIZE_PLAYER_ONE[0],BOARD_SIZE_PLAYER_ONE[1])); // set layout
            // Loop through each x and y board location and set buttons
            for (int x=0; x<BOARD_SIZE_PLAYER_ONE[0]; x++){
                for (int y=0; y<BOARD_SIZE_PLAYER_ONE[1]; y++){
                    boardButton[x][y] = new JButton();
                    boardButton[x][y].setSize(50,50);
                    boardButton[x][y].addActionListener(
                            new AttackActionListener(x,y)); // Provides button listener with coordinates of the tile
                    boardButton[x][y].setBackground(
                            this.getMatchingColor(boardCharacters[x][y])); // set appropriate colour for the button
                    boardPanel.add(boardButton[x][y]);
                }
            }
        }
    }

    private Color getMatchingColor(char tileCharacter){
        /*
         * Converts the character based representation of the board received from the backend to appropriate colours
         *      for the GUI buttons on the board.
         * @param tileCharacter: Tile character corresponding what the user should see on a tile
         * @return The colour of the tile, which is used to set the button colour.
         */

        switch (tileCharacter) {
            case 'f' -> { return Color.GRAY; }     // fog of war ; grey tile
            case 'w' -> { return Color.BLUE; }     // water ; blue tile
            case 'd' -> { return Color.GREEN; }    // destroyer ; green tile
            case 's' -> { return Color.MAGENTA; }  // submarine ; pink tile
            case 'b' -> { return Color.RED;}       // battleship ; red tile
            case 'c' -> { return Color.YELLOW;}    // carrier ; yellow tile
            default -> throw new IllegalStateException("Unexpected value: " + tileCharacter);
        }
    }

    private class ExitActionListener implements ActionListener {
        /*
         * Action listener triggered when the exit button is pressed. Closes the program.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            mainGuiFrame.dispose();
            System.exit(0);
        }
    }

    private class ScoreboardActionListener implements ActionListener {
        /*
         * Action listener triggered when the scoreboard/leaderboard button is pressed.
         * Displays players, ordered by their number of wins in a pop-up window.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String leaderboard = BattleshipsMain.getLeaderboard();
            JOptionPane.showMessageDialog(mainGuiFrame,
                    "Local Battleships Scoreboard:\n\n" +
                            leaderboard,
                    "Local Leaderboard",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class AttackActionListener implements ActionListener  {
        /*
         * Action listener triggered when the one of the board buttons is pressed.
         * Triggers communication with the game backend assigning scores to players, changing who's turn it is,
         *      and checking if the game is over.
         */

        // Attack coordinates
        private final int xAttackCoordinate;
        private final int yAttackCoordinate;
        public AttackActionListener(int xTileIndex, int yTileIndex){
            // Set attack coordinates corresponding to the button pressed on the GUI
            xAttackCoordinate = xTileIndex;
            yAttackCoordinate = yTileIndex;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int[] attackCoordinates = new int[] {xAttackCoordinate, yAttackCoordinate};
            boolean isValidAttack = false;
            try {   // try catch implemented because nonGui game play includes thread sleep
                // Check if attack is valid.
                isValidAttack = BattleshipsMain.attackCoordinates(attackCoordinates);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            if (isValidAttack) {    // if it is a valid attack it is the next players turn
                // Next players turn if attack was not on a tile already attacked
                BattleshipsMain.setIsPlayerOnesTurn(!BattleshipsMain.getIsPlayerOnesTurn());
                boardPanel.removeAll();
                createBoardGui();   // Creates new board GUI of the next player
            }else{                  // if it is NOT a valid attack the player can go again
                BattleshipsMain.setIsPlayerOnesTurn(BattleshipsMain.getIsPlayerOnesTurn());
            }

            // Here we check if the game is over after an attack and if so, who won the game.
            if (BattleshipsMain.getIsGameOver()){
                if(BattleshipsMain.checkWhoWon() == 1) {                     // Player 1 won the game
                    BattleshipsMain.addWinnerToLeaderboard(PLAYER_ONE_NAME); // Add player 1 to leaderboard
                    // Display the winner of the game
                    JOptionPane.showMessageDialog(mainGuiFrame,
                            PLAYER_ONE_NAME.toUpperCase()+" HAS WON THE GAME!\n\n"+
                                    "Score Overview:\n" +
                                    PLAYER_ONE_NAME+" = "+String.format("%.2f", BattleshipsMain.getPlayerOneScore())+" points\n"+
                                    PLAYER_TWO_NAME+" = "+String.format("%.2f", BattleshipsMain.getPlayerTwoScore())+" points",
                            "The game is over!",
                            JOptionPane.INFORMATION_MESSAGE);
                }else if(BattleshipsMain.checkWhoWon() == 0){                // Player 2 won the game
                    BattleshipsMain.addWinnerToLeaderboard(PLAYER_TWO_NAME); // Add player 2 to leaderboard
                    // Display the winner of the game
                    JOptionPane.showMessageDialog(mainGuiFrame,
                            PLAYER_TWO_NAME.toUpperCase()+" HAS WON THE GAME!\n\n"+
                                    "Score Overview:\n" +
                                    PLAYER_ONE_NAME+" = "+String.format("%.2f", BattleshipsMain.getPlayerOneScore())+" points\n"+
                                    PLAYER_TWO_NAME+" = "+String.format("%.2f", BattleshipsMain.getPlayerTwoScore())+" points",
                            "The game is over!",
                            JOptionPane.INFORMATION_MESSAGE);
                }else{                                                      // Game is a tie
                    // Display tie message
                    JOptionPane.showMessageDialog(mainGuiFrame,
                            "THE GAME IS A TIE!\n\n"+
                                    "Score Overview:\n" +
                                    PLAYER_ONE_NAME+" = "+String.format("%.2f", BattleshipsMain.getPlayerOneScore())+" points\n"+
                                    PLAYER_TWO_NAME+" = "+String.format("%.2f", BattleshipsMain.getPlayerTwoScore())+" points",
                            "The game is over!",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                mainGuiFrame.dispose();

                // Display the new leaderboard based on who won the game.
                String leaderboard = BattleshipsMain.getLeaderboard();
                JOptionPane.showMessageDialog(mainGuiFrame,
                        "Local Battleships Scoreboard:\n\n" +
                                leaderboard,
                        "Local Leaderboard",
                        JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
    }

    private void displayBoatPlacementErrors(int[] shipPlacementFileErrors) {
        /*
         * This method shows the result of trying to load custom user boat placements.
         * It provides error messages in accordance to the type of flag received from the backend when loading the files.
         * It helps users debug the textfiles and find mistakes they have made, or confirms that it was loaded correctly.
         * @param shipPlacementFileErrors: error flag for each players textfile definition.
         */

        // If the user selected to place the ships using the textfiles check if they were properly loaded
        if (IS_BOARD_SIZE_FROM_FILE) {
            String errorMessageFileOne;
            String errorMessageFileTwo;
            int fileOneErrors = shipPlacementFileErrors[0];
            // Switch block converts error number to an appropriate message to be displayed for player 1
            errorMessageFileOne = switch (fileOneErrors) {
                case 0 -> "Ship definition text file was loaded successfully.";
                case 3 -> "Invalid placement of ships: Too few ships defined!";
                case 4 -> "Invalid placement of ships. Too many ships defined!";
                case 5 -> "Invalid placement of ships. Too few coordinates specified one of the ships.";
                case 6 -> "Invalid placement of ships. Too many coordinates specified one of the ships.";
                case 8 -> "Invalid placement of ships. A ship was possibly spelled incorrectly.";
                case 9 -> "Invalid placement of ships. Coordinates have to be numbers.";
                case 10 -> "Error during placement of ships. " +
                        "Ship definition text file was not found.";
                case 11 -> "Error during placement of ships. " +
                        "Ship definition text file had an InputOutputException.";
                default -> "Error during placement of ships. " +
                        "Possible causes:\n" +
                        "- A ship has been placed off of the board.\n" +
                        "- A ship has been placed on another ship.\n" +
                        "- A ship is not continuous.";
            };

            int fileTwoErrors = shipPlacementFileErrors[1];
            // Switch block converts error number to an appropriate message to be displayed for player 2
            errorMessageFileTwo = switch (fileTwoErrors) {
                case 0 -> "Ship definition text file was loaded successfully.";
                case 3 -> "Invalid placement of ships: Too few ships defined!";
                case 4 -> "Invalid placement of ships. Too many ships defined!";
                case 5 -> "Invalid placement of ships. Too few coordinates specified one of the ships.";
                case 6 -> "Invalid placement of ships. Too many coordinates specified one of the ships.";
                case 8 -> "Invalid placement of ships. A ship was possibly spelled incorrectly.";
                case 9 -> "Invalid placement of ships. Coordinates have to be numbers.";
                case 10 -> "Error during placement of ships. " +
                        "Ship definition text file was not found.";
                case 11 -> "Error during placement of ships. " +
                        "Ship definition text file had an InputOutputException.";
                default -> "Error during placement of ships. " +
                        "Possible causes:\n" +
                        "- A ship has been placed off of the board.\n" +
                        "- A ship has been placed on another ship.\n" +
                        "- A ship is not continuous.";
            };

            // Check what errors were found for each player's text file and display the appropriate messages.
            if ((fileOneErrors == 0) & (fileTwoErrors) == 0) {          // Both player text files were without errors
                JOptionPane.showMessageDialog(mainGuiFrame,
                        PLAYER_ONE_NAME + "'s ship placement file was loaded successfully.\n\n" +
                                PLAYER_TWO_NAME + "'s ship placement files was loaded successfully.\n\n" +
                                "Enjoy the game!",
                        "Loading ship placement text files.",
                        JOptionPane.INFORMATION_MESSAGE);
            } else if ((fileOneErrors != 0) & (fileTwoErrors == 0)) {   // Only player ones text file had errors
                JOptionPane.showMessageDialog(mainGuiFrame,
                        PLAYER_ONE_NAME + "'s ship placement file was NOT loaded successfully.\n" +
                                errorMessageFileOne + "\n" + "The ships have been placed randomly instead.\n\n" +
                                PLAYER_TWO_NAME + "'s ship placement files was loaded successfully.\n\n" +
                                "Enjoy the game!",
                        "Loading ship placement text files.",
                        JOptionPane.WARNING_MESSAGE);
            } else if ((fileOneErrors == 0) & (fileTwoErrors != 0)) {   // Only player twos text file had errors
                JOptionPane.showMessageDialog(mainGuiFrame,
                        PLAYER_ONE_NAME + "'s ship placement files was loaded successfully.\n\n" +
                                PLAYER_TWO_NAME + "'s ship placement files was NOT loaded successfully.\n" +
                                errorMessageFileTwo + "\n" + "The ships have been placed randomly instead.\n\n" +
                                "Enjoy the game!",
                        "Loading ship placement text files.",
                        JOptionPane.WARNING_MESSAGE);
            } else {                                                    // Both player text files had errors
                JOptionPane.showMessageDialog(mainGuiFrame,
                        PLAYER_ONE_NAME + "'s ship placement files was NOT loaded successfully.\n" +
                                errorMessageFileOne + "\n" + "The ships have been placed randomly instead.\n\n" +
                                PLAYER_TWO_NAME + "'s ship placement files was NOT loaded successfully.\n" +
                                errorMessageFileTwo + "\n" + "The ships have been placed randomly instead.\n\n" +
                                "Enjoy the game!",
                        "Loading ship placement text files.",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}

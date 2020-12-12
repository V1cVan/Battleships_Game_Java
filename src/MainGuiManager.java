import com.sun.tools.javac.Main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.*;

public class MainGuiManager {
    // GUI related visual variables:
    private JFrame mainGuiFrame;
    private JPanel informationPanel = new JPanel();
    private JPanel player1Panel = new JPanel();
    private JPanel player2Panel = new JPanel();
    private JPanel currentTurnPanel = new JPanel();
    private JLabel player1Label = new JLabel();
    private JLabel player1ScoreLabel = new JLabel();
    private JLabel player2Label = new JLabel();
    private JLabel player2ScoreLabel = new JLabel();
    private JLabel turnLabel = new JLabel("Turn:");
    private JLabel currentTurnLabel = new JLabel();
    private JButton exitButton = new JButton("Quit Game");
    private JButton scoreBoardButton = new JButton("Leaderboard");
    private JPanel boardPanel = new JPanel();
    private JButton[][] boardButton;

    // Game functionality variables:
    private BattleshipsGameMain gameMain = new BattleshipsGameMain();
    private final boolean IS_BOARD_SIZE_FROM_FILE;
    private final String PLAYER_ONE_NAME;
    private final String PLAYER_TWO_NAME;
    private final boolean IS_PLAYER_ONE_FIRST;
    private final boolean IS_PLAYER_ONE_DISADVANTAGED;
    private final boolean IS_PLAYER_TWO_DISADVANTAGED;
    private final int[] BOARD_SIZE_PLAYER_ONE;
    private final int[] BOARD_SIZE_PLAYER_TWO;
    private boolean isPlayerOnesTurn;

    public MainGuiManager(boolean isBoardSizeFrmFile,
                          String playerOneNme,
                          String playerTwoNme,
                          boolean playerOneFirst,
                          boolean playerOneDisadvantaged,
                          boolean playerTwoDisadvantaged,
                          int[] boardSzePlayerOne,
                          int[] boardSzePlayerTwo,
                          String playerOneLayoutFileNme,
                          String playerTwoLayoutFileNme) {

        IS_BOARD_SIZE_FROM_FILE = isBoardSizeFrmFile;
        PLAYER_ONE_NAME = playerOneNme;
        PLAYER_TWO_NAME = playerTwoNme;
        IS_PLAYER_ONE_FIRST = playerOneFirst;
        IS_PLAYER_ONE_DISADVANTAGED = playerOneDisadvantaged;
        IS_PLAYER_TWO_DISADVANTAGED = playerTwoDisadvantaged;

        if (IS_BOARD_SIZE_FROM_FILE == true){
            // Read board size from files if custom ship placement was selected
            BOARD_SIZE_PLAYER_ONE = gameMain.readBoardSize(playerOneLayoutFileNme);
            BOARD_SIZE_PLAYER_TWO = gameMain.readBoardSize(playerTwoLayoutFileNme);
        }else{
            // Get board size from spinners on splash screen
            BOARD_SIZE_PLAYER_ONE = boardSzePlayerOne;
            BOARD_SIZE_PLAYER_TWO = boardSzePlayerTwo;
        }

        // Current turn is set depending on whether player one was selected to go first
        isPlayerOnesTurn = IS_PLAYER_ONE_FIRST;

        // Initialise the two boards in the backend
        gameMain.initBoards(BOARD_SIZE_PLAYER_ONE, BOARD_SIZE_PLAYER_TWO);
        // Initialise the two players in the backend
        gameMain.initPlayers(PLAYER_ONE_NAME, PLAYER_TWO_NAME,
                IS_PLAYER_ONE_DISADVANTAGED, IS_PLAYER_TWO_DISADVANTAGED);

        // Place ships on the boards, either randomly or from the text file definitions
        gameMain.placeShips(IS_BOARD_SIZE_FROM_FILE , playerOneLayoutFileNme, playerTwoLayoutFileNme);

        // Initialise main gui frame
        mainGuiFrame = new JFrame("Battleship");

        // Initialise tiles on board for the player going first
        this.createBoardTiles();
        boardPanel.setMinimumSize(new Dimension(800,800));

        // Set game gui fonts:
        player1Label.setFont(new Font("", Font.BOLD, 16));
        player1ScoreLabel.setFont(new Font("", Font.BOLD, 16));
        turnLabel.setFont(new Font("", Font.BOLD, 16));
        currentTurnLabel.setFont(new Font("", Font.BOLD, 24));
        player2Label.setFont(new Font("", Font.BOLD, 16));
        player2ScoreLabel.setFont(new Font("", Font.BOLD, 16));
        exitButton.setFont(new Font("", Font.PLAIN, 16));
        scoreBoardButton.setFont(new Font("", Font.PLAIN, 16));

        // Set panel containing game information above the board
        informationPanel.setLayout(new GridLayout(1, 5));
        informationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        informationPanel.add(scoreBoardButton);
        scoreBoardButton.addActionListener(new ScoreboardActionListener());

        // Player 1 information:
        player1Panel.setLayout(new GridLayout(2,1));
        player1Panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        player1Label.setText(PLAYER_ONE_NAME+"'s score:");
        player1Panel.add(player1Label);
        player1ScoreLabel.setText(Integer.toString(gameMain.getPlayerOneScore()));
        player1ScoreLabel.setHorizontalAlignment(JLabel.CENTER);
        player1Panel.add(player1ScoreLabel);
        informationPanel.add(player1Panel);

        // Current turn panel:
        currentTurnPanel.setLayout(new GridLayout(2,1));
        currentTurnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        currentTurnPanel.add(turnLabel);
        turnLabel.setHorizontalAlignment(JLabel.CENTER);
        if (isPlayerOnesTurn == true){
            currentTurnLabel.setText(PLAYER_ONE_NAME);
        }else{
            currentTurnLabel.setText(PLAYER_TWO_NAME);
        }
        currentTurnLabel.setHorizontalAlignment(JLabel.CENTER);
        currentTurnPanel.add(currentTurnLabel);
        informationPanel.add(currentTurnPanel);

        // Player 2 information:
        player2Panel.setLayout(new GridLayout(2,1));
        player2Panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        player2Label.setText(PLAYER_TWO_NAME+"'s score:");
        player2Panel.add(player2Label);
        player2ScoreLabel.setText(Integer.toString(gameMain.getPlayerTwoScore()));
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

    private void createBoardTiles(){
        if (isPlayerOnesTurn){
            boardButton = new JButton[BOARD_SIZE_PLAYER_ONE[0]][BOARD_SIZE_PLAYER_ONE[1]];
            boardPanel.setLayout(new GridLayout(BOARD_SIZE_PLAYER_ONE[0],BOARD_SIZE_PLAYER_ONE[1]));
            for (int xTileIndex=0; xTileIndex<BOARD_SIZE_PLAYER_ONE[0]; xTileIndex++){
                for (int yTileIndex=0; yTileIndex<BOARD_SIZE_PLAYER_ONE[1]; yTileIndex++){
                    boardButton[xTileIndex][yTileIndex] = new JButton();
                    boardButton[xTileIndex][yTileIndex].setSize(50,50);
                    boardButton[xTileIndex][yTileIndex].addActionListener(new AttackActionListener());
                    boardPanel.add(boardButton[xTileIndex][yTileIndex]);
                }
            }
        }else{ // if player two's turn
            boardButton = new JButton[BOARD_SIZE_PLAYER_ONE[0]][BOARD_SIZE_PLAYER_ONE[1]];
            boardPanel.setLayout(new GridLayout(BOARD_SIZE_PLAYER_TWO[0],BOARD_SIZE_PLAYER_TWO[1]));
            for (int xTileIndex=0; xTileIndex<BOARD_SIZE_PLAYER_TWO[0]; xTileIndex++){
                for (int yTileIndex=0; yTileIndex<BOARD_SIZE_PLAYER_TWO[1]; yTileIndex++){
                    boardButton[xTileIndex][yTileIndex] = new JButton();
                    boardButton[xTileIndex][yTileIndex].setSize(50,50);
                    boardButton[xTileIndex][yTileIndex].addActionListener(new AttackActionListener());
                    boardPanel.add(boardButton[xTileIndex][yTileIndex]);
                }
            }
        }
    }

    private class ExitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainGuiFrame.dispose();
            System.exit(0);
        }
    }

    private class ScoreboardActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(mainGuiFrame,
                    "Local Battleships Scoreboard:\n" +
                            "TODO!!!!",
                    "Scoreboard",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class AttackActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO attack
        }
    }


}

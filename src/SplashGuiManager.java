import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton; // Buttons
import javax.swing.JLabel; // Labels
import javax.swing.JTextField; // Entering data for ship placement
import javax.swing.JSpinner; // Spinner for board size
import javax.swing.SpinnerNumberModel;
import javax.swing.JCheckBox; // Disadvantage for fist player
import javax.swing.JRadioButton; // Start player selection
import javax.swing.ButtonGroup; // For radio button selection
import javax.swing.SpinnerModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane; // Message dialogs
import javax.swing.JPanel;

import javax.swing.SwingUtilities;




public class SplashGuiManager {
    // Splash screen:
    private JFrame splashScreenFrame;
    private JFrame boatLayoutFrame;
    private JFrame mainGameFrame = new JFrame();
    private JFrame scoreBoardFrame = new JFrame();

    private JPanel informationPanel = new JPanel();
    private JPanel optionsPanel = new JPanel();
    private JPanel customScoringPanel = new JPanel();
    private JPanel boardSizePanel = new JPanel();
    private JPanel buttonsPanel = new JPanel();
    private JPanel placeShipsPanel = new JPanel();
    private JPanel startPanel = new JPanel();
    private JPanel extrasPanel = new JPanel();


    // Labels on splash screen:
    private JLabel battleShipTitle = new JLabel("Welcome to Battleship");
    private JLabel splashInformation = new JLabel("Please select your options and get started");
    private JLabel playerOneName = new JLabel("Player 1 name:");
    private JLabel playerTwoName = new JLabel("Player 2 name:");
    private JLabel firstPlayer = new JLabel("Player to go first:");
    private JLabel boardInformation = new JLabel("Choose the board size:");
    private JLabel boardRowNumber = new JLabel("  Rows",JLabel.LEFT);
    private JLabel boardColumnNumber = new JLabel("  Columns",JLabel.LEFT);

    // Elements on splash screen:
    private JButton shipPlacementButton = new JButton("Choose Ship Placement");
    private JCheckBox scoreCompensationButton = new JCheckBox("Score compensation for player going second?");
    private JRadioButton firstPlayerRButton = new JRadioButton("Player 1");
    private JRadioButton secondPlayerRButton = new JRadioButton("Player 2");
    private JTextField playerOneNameBox = new JTextField();
    private JTextField playerTwoNameBox = new JTextField();
    private JButton startGameButton = new JButton("Start Game");
    private JButton rulesButton = new JButton("Rules");
    private JButton scoreboardButton = new JButton("High Scores");
    private JButton exitButton = new JButton("Exit");
    private SpinnerModel spinRowModel = new SpinnerNumberModel(8, 5, 15, 1);
    private JSpinner numberRowsSpin = new JSpinner(spinRowModel);
    private SpinnerModel spinColumnModel = new SpinnerNumberModel(8, 5, 15, 1);
    private JSpinner numberColumnsSpin = new JSpinner(spinColumnModel);

    public SplashGuiManager(){
        splashScreenFrame = new JFrame("Battship Game: selection screen");

        // Set splash screen fonts
        battleShipTitle.setFont(new Font("", Font.PLAIN, 30));
        splashInformation.setFont(new Font("", Font.PLAIN, 18));
        boardInformation.setFont(new Font("", Font.PLAIN, 16));
        boardRowNumber.setFont(new Font("", Font.PLAIN, 15));
        boardColumnNumber.setFont(new Font("", Font.PLAIN, 15));
        playerOneName.setFont(new Font("", Font.PLAIN, 15));
        playerTwoName.setFont(new Font("", Font.PLAIN, 15));
        firstPlayer.setFont(new Font("", Font.PLAIN, 15));
        firstPlayerRButton.setFont(new Font("", Font.PLAIN, 13));
        secondPlayerRButton.setFont(new Font("", Font.PLAIN, 13));
        scoreCompensationButton.setFont(new Font("", Font.PLAIN, 13));

        // Splash screen information panel
        informationPanel.add(battleShipTitle);
        battleShipTitle.setHorizontalAlignment(0);
        informationPanel.add(splashInformation);
        splashInformation.setHorizontalAlignment(0);
        informationPanel.setBorder(BorderFactory.createEmptyBorder(10,30,10,30));
        informationPanel.setLayout(new GridLayout(2,1));

        // Splash screen game options panel
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10,30,10,30));
        optionsPanel.setLayout(new GridLayout(1,2));
        // Scoring setting panel:
        customScoringPanel.setBorder(BorderFactory.createEtchedBorder());
        customScoringPanel.setLayout(new GridLayout(8,1));
        customScoringPanel.add(playerOneName);
        customScoringPanel.add(playerOneNameBox);
        customScoringPanel.add(playerTwoName);
        customScoringPanel.add(playerTwoNameBox);
        customScoringPanel.add(firstPlayer);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(firstPlayerRButton);
        firstPlayerRButton.setSelected(true);
        buttonGroup.add(secondPlayerRButton);
        customScoringPanel.add(firstPlayerRButton);
        customScoringPanel.add(secondPlayerRButton);
        customScoringPanel.add(scoreCompensationButton);
        optionsPanel.add(customScoringPanel);

        // Splash screen board options panel
        boardSizePanel.setBorder(BorderFactory.createEtchedBorder());
        boardSizePanel.setLayout(new GridLayout(5,1));
        boardSizePanel.add(boardInformation);
        boardSizePanel.add(boardRowNumber);
        boardSizePanel.add(numberRowsSpin);
        boardSizePanel.add(boardColumnNumber);
        boardSizePanel.add(numberColumnsSpin);
        optionsPanel.add(boardSizePanel);

        // Splash screen buttons panel
        buttonsPanel.setLayout(new GridLayout(3,1));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0,0,30,0));
        shipPlacementButton.setPreferredSize(new Dimension(300,50));
        placeShipsPanel.add(shipPlacementButton);
        buttonsPanel.add(placeShipsPanel);
        shipPlacementButton.addActionListener(new ShipPlacementActionListener());

        startPanel.add(startGameButton);
        startGameButton.setPreferredSize(new Dimension(300,50));
        buttonsPanel.add(startPanel);
        startGameButton.addActionListener(new StartButtonActionListener());

        extrasPanel.setLayout(new GridLayout(1,3));
        extrasPanel.setBorder(BorderFactory.createEmptyBorder(15,60,30,60));
        extrasPanel.add(rulesButton);
        extrasPanel.add(scoreboardButton);
        extrasPanel.add(exitButton);
        rulesButton.setPreferredSize(new Dimension(50,20));
        rulesButton.addActionListener(new GameRulesActionListener());
        scoreboardButton.setPreferredSize(new Dimension(50,20));
        scoreboardButton.addActionListener(new ScoreboardActionListener());
        exitButton.setPreferredSize(new Dimension(50,20));
        exitButton.addActionListener(new ExitActionListener());
        buttonsPanel.add(extrasPanel);

        // Final splash screen layout
        splashScreenFrame.setLayout(new GridLayout(3,1));
        splashScreenFrame.setLocation(1000,100);
        splashScreenFrame.add(informationPanel);
        splashScreenFrame.add(optionsPanel);
        splashScreenFrame.add(buttonsPanel);
        splashScreenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        splashScreenFrame.pack();
        splashScreenFrame.setVisible(true);
    }

    private class GameRulesActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(splashScreenFrame,
                    "Basics of the game Battleships:\n" +
                            "- At the start of the game each player places 5 ships on his/her board.\n" +
                            "- Each player then takes a turn at attacking the other players fleet.\n" +
                            "- At the beginning of the game the locations of all the ships are hidden by the 'fog of war'.\n" +
                            "- When attacking the player clicks on the tile where they think an opponents ship may be located.\n" +
                            "- After a tile is selected, the attack may either be a hit or a miss.\n" +
                            "- If you hit a ship you will be assigned points in accordance with the length of the ship hit.\n" +
                            "- When an attack misses a ship you will be assigned no points but the 'fog of war' will be cleared.\n" +
                            "- If a ship is sunk additional points are awarded according to the length of the ship.\n" +
                            "- The game ends when a players fleet of ships is completely destroyed.\n" +
                            "- At this time the player with the highest score wins the game.\n" +
                            "\nNote: Various options can be explored on the games splash screen including:\n" +
                            "Different board sizes, scoring penalties on the player who goes first, and the local leaderboards.",
                    "Rules of Battleships",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class ScoreboardActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(splashScreenFrame,
                    "Local Battleships Scoreboard:\n" +
                            "TODO!!!!",
                    "Scoreboard",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class ExitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            splashScreenFrame.dispose();
            System.exit(0);
        }
    }

    private class StartButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO implement game start
        }
    }

    private class ShipPlacementActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            splashScreenFrame.setEnabled(false);
            // TODO ship placement GUI
        }
    }

}
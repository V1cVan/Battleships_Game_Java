import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton; // Buttons
import javax.swing.JLabel; // Labels
import javax.swing.JTextArea; // Text area for board size
import javax.swing.JSpinner; // Spinner for board size
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;



public class MenuManager {


    // Splash screen:
    private JFrame splashScreen;
    private JPanel splashInformationPanel = new JPanel();
    private JPanel splashOptionsPanel = new JPanel();
    private JPanel splashBoardSizePanel = new JPanel();
    private JPanel splashButtonsPanel = new JPanel();
    private JPanel startGamePanel = new JPanel();
    private JPanel emptyPanel = new JPanel();
    // Labels on splash screen:
    private JLabel battleShipTitle = new JLabel("Welcome to Battleship");
    private JLabel splashInformation = new JLabel("Please select your options and get started");
    private JLabel boardInformation = new JLabel("Choose the board size:");
    private JLabel boardRowNumber = new JLabel("  Rows",JLabel.LEFT);
    private JLabel boardColumnNumber = new JLabel("  Columns",JLabel.LEFT);
    // Elements on splash screen:
    private JButton shipPlacementButton = new JButton("Choose Ship Placement");
    private JButton scoringSystemButton = new JButton("Choose Scoring System");
    private JButton startGameButton = new JButton("Start Game");
    private JButton rulesButton = new JButton("Rules");
    private JButton scoreboardButton = new JButton("High Scores");
    private JButton exitButton = new JButton("Exit");
    private JSpinner numberRowsSpin = new JSpinner();
    private JSpinner numberColumnsSpin = new JSpinner();


    private JFrame mainGame = new JFrame();
    private JFrame scoreBoard = new JFrame();
    private JFrame rules  = new JFrame();
    private JFrame scoringSystem = new JFrame();


    public MenuManager(){
        splashScreen = new JFrame("Battship Game: selection screen");
        // Set splash screen fonts
        battleShipTitle.setFont(new Font("", Font.PLAIN, 30));
        splashInformation.setFont(new Font("", Font.PLAIN, 18));
        boardInformation.setFont(new Font("", Font.PLAIN, 16));
        boardRowNumber.setFont(new Font("", Font.PLAIN, 15));
        boardColumnNumber.setFont(new Font("", Font.PLAIN, 15));
        // Splash screen information panel
        splashInformationPanel.add(battleShipTitle);
        battleShipTitle.setHorizontalAlignment(0);
        splashInformationPanel.add(splashInformation);
        splashInformation.setHorizontalAlignment(0);
        splashInformationPanel.setBorder(BorderFactory.createEmptyBorder(10,30,10,30));
        splashInformationPanel.setLayout(new GridLayout(2,1));
        // Splash screen game options panel
        splashOptionsPanel.add(shipPlacementButton);
        splashOptionsPanel.add(scoringSystemButton);
        splashOptionsPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        splashOptionsPanel.setLayout(new GridLayout(1,2));
        // Splash screen board options panel
        splashBoardSizePanel.setBorder(BorderFactory.createEmptyBorder(30,70,30,70));
        splashBoardSizePanel.setLayout(new GridLayout(3,2));
        splashBoardSizePanel.add(boardInformation);
        splashBoardSizePanel.add(emptyPanel);
        splashBoardSizePanel.add(numberRowsSpin);
        splashBoardSizePanel.add(boardRowNumber);
        splashBoardSizePanel.add(numberColumnsSpin);
        splashBoardSizePanel.add(boardColumnNumber);


        // Start game panel
        startGamePanel.setBorder(BorderFactory.createEmptyBorder(20,30,0,30));
        startGameButton.setPreferredSize(new Dimension(400,50));
        startGamePanel.add(startGameButton);
        // Splash screen buttons panel
        splashButtonsPanel.setBorder(BorderFactory.createEmptyBorder(0,0,30,0));
        rulesButton.setPreferredSize(new Dimension(120,30));
        scoreboardButton.setPreferredSize(new Dimension(120,30));
        exitButton.setPreferredSize(new Dimension(120,30));
        splashButtonsPanel.add(rulesButton);
        splashButtonsPanel.add(scoreboardButton);
        splashButtonsPanel.add(exitButton);

        splashScreen.setLayout(new GridLayout(5,1));
        splashScreen.add(splashInformationPanel);
        splashScreen.add(splashOptionsPanel);
        splashScreen.add(splashBoardSizePanel);
        splashScreen.add(startGamePanel);
        splashScreen.add(splashButtonsPanel);
        splashScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        splashScreen.pack();
        splashScreen.setVisible(true);
    }
    // TODO Labels on Score screen

//    // Splash screen properties
//    splashScreensetSize(900, 300);
//    setTitle("Battleship");
//    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    setVisible(true);
}

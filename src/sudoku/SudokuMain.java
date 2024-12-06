package sudoku;

import javax.swing.*;
import java.awt.*;

public class SudokuMain extends JFrame {
    private static final long serialVersionUID = 1L;
    private GameBoardPanel board = new GameBoardPanel();
    private JTextField statusBar = new JTextField("Welcome to Sudoku!"); // Status bar
    private JLabel timerLabel = new JLabel("Time: 00:00"); // Timer display
    private Timer timer; // Swing Timer for countdown or elapsed time
    private int seconds = 0; // Tracks elapsed seconds

    public SudokuMain() {
        // Setup JFrame layout
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        // Add game board
        cp.add(board, BorderLayout.CENTER);

        // Add timer label at the top
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(timerLabel, BorderLayout.WEST);
        cp.add(topPanel, BorderLayout.NORTH);

        // Add status bar at the bottom
        statusBar.setEditable(false);
        statusBar.setHorizontalAlignment(JTextField.CENTER);
        cp.add(statusBar, BorderLayout.SOUTH);

        // Timer setup
        timer = new Timer(1000, e -> updateTimer());
        timer.start();

        // Add a menu bar
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> {
            board.newGame();
            updateStatusBar();
        }); // Start new game
        JMenuItem resetGameItem = new JMenuItem("Reset Game");
        resetGameItem.addActionListener(e -> {
            board.resetGame();
            updateStatusBar();
        }); // Reset the game
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(newGameItem);
        fileMenu.add(resetGameItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Options menu
        JMenu optionsMenu = new JMenu("Options");

        // Difficulty options
        JMenuItem easyItem = new JMenuItem("Easy");
        easyItem.addActionListener(e -> board.setDifficulty("Easy"));
        JMenuItem mediumItem = new JMenuItem("Intermediate");
        mediumItem.addActionListener(e -> board.setDifficulty("Intermediate"));
        JMenuItem hardItem = new JMenuItem("Hard");
        hardItem.addActionListener(e -> board.setDifficulty("Hard"));
        optionsMenu.add(easyItem);
        optionsMenu.add(mediumItem);
        optionsMenu.add(hardItem);
        optionsMenu.addSeparator();

        // Functional options
        JMenuItem checkItem = new JMenuItem("Check Progress");
        checkItem.addActionListener(e -> {
            board.checkProgress();
            updateStatusBar();
        });
        JMenuItem hintItem = new JMenuItem("Hint");
        hintItem.addActionListener(e -> {
            board.giveHint();
            updateStatusBar();
        });
        JMenuItem solveItem = new JMenuItem("Solve");
        solveItem.addActionListener(e -> {
            board.solvePuzzle();
            updateStatusBar();
        });
        optionsMenu.add(checkItem);
        optionsMenu.add(hintItem);
        optionsMenu.add(solveItem);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Sudoku Game v1.0"));
        helpMenu.add(aboutItem);

        // Add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(optionsMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        // Initialize the game board
        board.newGame();
        updateStatusBar();

        // Setup frame
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sudoku");
        setVisible(true);

    }

    private void addDifficultyMenuItem(JMenu menu, String difficulty) {
        JMenuItem item = new JMenuItem(difficulty);
        item.addActionListener(e -> {
            board.setDifficulty(difficulty);
            board.newGame();
            resetTimer();
            updateStatusBar();
        });
        menu.add(item);
    }

    // Updates the timer label every second
    private void updateTimer() {
        seconds++;
        timerLabel.setText(String.format("Time: %02d:%02d", seconds / 60, seconds % 60));
    }

    // Updates the status bar with the remaining cells
    private void updateStatusBar() {
        int remainingCells = board.getRemainingCells();
        statusBar.setText(remainingCells + " cells remaining");
    }

    private void resetTimer() {
        seconds = 0;
        timerLabel.setText("Time: 00:00");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuMain::new);
    }
}

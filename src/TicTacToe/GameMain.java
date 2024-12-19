package TicTacToe;

import TicTacToe.Board;
import TicTacToe.Seed;
import TicTacToe.State;
import TicTacToe.Cell;
import TicTacToe.SoundManager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameMain extends JPanel {

    public static final Color COLOR_CROSS = Color.RED;
    public static final Color COLOR_NOUGHT = Color.BLUE;

    private static final int CANVAS_WIDTH = 400;
    private static final int CANVAS_HEIGHT = 400;
    private static final int GRID_WIDTH = 8;
    private static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;

    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private SoundManager soundManager;

    public GameMain() {
        board = new Board();
        soundManager = new SoundManager();
        initGame();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int rowSelected = mouseY / (CANVAS_HEIGHT / Board.ROWS);
                int colSelected = mouseX / (CANVAS_WIDTH / Board.COLS);

                if (currentState == State.PLAYING) {
                    if (rowSelected >= 0 && rowSelected < Board.ROWS && colSelected >= 0 && colSelected < Board.COLS
                            && board.cells[rowSelected][colSelected].content == Seed.NO_SEED) {
                        board.cells[rowSelected][colSelected].content = currentPlayer;
                        soundManager.playSoundEffect("boop-741-mhz-39314.wav");
                        updateGame(currentPlayer, rowSelected, colSelected);
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                    }
                } else {
                    initGame();
                }

                repaint();
            }
        });

        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> soundManager.stopBackgroundMusic()));
    }

    private void initGame() {
        board.init();
        currentState = State.PLAYING;
        currentPlayer = Seed.CROSS;
        soundManager.playBackgroundMusic("mixkit-alter-ego-481.wav");
    }

    private void updateGame(Seed theSeed, int row, int col) {
        if (board.hasWon(theSeed, row, col)) {
            currentState = (theSeed == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
            soundManager.playSoundEffect("winning-218995.wav");
        } else if (board.isDraw()) {
            currentState = State.DRAW;
            soundManager.playSoundEffect("winning-218995.wav");
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        board.paint(g);

        if (currentState == State.PLAYING) {
            g.setColor(Color.BLUE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString((currentPlayer == Seed.CROSS ? "X" : "O") + "'s Turn", 10, 20);
        } else {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            if (currentState == State.CROSS_WON) {
                g.drawString("X Won! Click to restart.", 10, 20);
            } else if (currentState == State.NOUGHT_WON) {
                g.drawString("O Won! Click to restart.", 10, 20);
            } else if (currentState == State.DRAW) {
                g.drawString("It's a Draw! Click to restart.", 10, 20);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tic Tac Toe");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new GameMain());
            frame.pack();
            frame.setVisible(true);
        });
    }
}

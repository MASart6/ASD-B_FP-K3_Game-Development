package TicTacToe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ConnectFour extends JPanel {

    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final int CELL_SIZE = 100;
    private static final Color GRID_COLOR = Color.BLACK;

    private Cell[][] board;
    private Seed currentPlayer;
    private GameState currentState;

    public ConnectFour() {
        setPreferredSize(new Dimension(COLS * CELL_SIZE, ROWS * CELL_SIZE));
        initializeGame();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentState == GameState.PLAYING) {
                    int clickedCol = e.getX() / CELL_SIZE;
                    handlePlayerMove(clickedCol);
                } else {
                    initializeGame();
                }
                repaint();
            }
        });
    }

    private void initializeGame() {
        board = new Cell[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                board[row][col] = new Cell(row, col);
            }
        }
        currentPlayer = Seed.CROSS;
        currentState = GameState.PLAYING;
    }

    private void handlePlayerMove(int col) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col].content == Seed.EMPTY) {
                board[row][col].content = currentPlayer;
                checkGameState(currentPlayer, row, col);
                currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                return;
            }
        }
    }

    private void checkGameState(Seed player, int row, int col) {
        if (isWinningMove(player, row, col)) {
            currentState = (player == Seed.CROSS) ? GameState.CROSS_WON : GameState.NOUGHT_WON;
        } else if (isBoardFull()) {
            currentState = GameState.DRAW;
        }
    }

    private boolean isWinningMove(Seed player, int row, int col) {
        return checkDirection(player, row, col, 1, 0) || // Horizontal
                checkDirection(player, row, col, 0, 1) || // Vertical
                checkDirection(player, row, col, 1, 1) || // Diagonal \
                checkDirection(player, row, col, 1, -1);  // Diagonal /
    }

    private boolean checkDirection(Seed player, int row, int col, int deltaRow, int deltaCol) {
        int count = 0;
        for (int i = -3; i <= 3; i++) {
            int newRow = row + i * deltaRow;
            int newCol = col + i * deltaCol;
            if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLS && board[newRow][newCol].content == player) {
                count++;
                if (count == 4) return true;
            } else {
                count = 0;
            }
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int col = 0; col < COLS; col++) {
            if (board[0][col].content == Seed.EMPTY) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawCells(g);
        if (currentState != GameState.PLAYING) {
            drawGameOverMessage(g);
        }
    }

    private void drawGrid(Graphics g) {
        g.setColor(GRID_COLOR);
        for (int row = 0; row <= ROWS; row++) {
            g.drawLine(0, row * CELL_SIZE, COLS * CELL_SIZE, row * CELL_SIZE);
        }
        for (int col = 0; col <= COLS; col++) {
            g.drawLine(col * CELL_SIZE, 0, col * CELL_SIZE, ROWS * CELL_SIZE);
        }
    }

    private void drawCells(Graphics g) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                board[row][col].draw(g, col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void drawGameOverMessage(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String message = switch (currentState) {
            case CROSS_WON -> "X Won! Click to restart.";
            case NOUGHT_WON -> "O Won! Click to restart.";
            case DRAW -> "It's a Draw! Click to restart.";
            default -> "";
        };
        g.drawString(message, 10, 30);
    }

    private enum Seed {
        EMPTY, CROSS, NOUGHT
    }

    private enum GameState {
        PLAYING, CROSS_WON, NOUGHT_WON, DRAW
    }

    private static class Cell {
        int row, col;
        Seed content;

        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
            this.content = Seed.EMPTY;
        }

        public void draw(Graphics g, int x, int y, int size) {
            if (content == Seed.CROSS) {
                g.setColor(Color.RED);
                g.drawLine(x + 10, y + 10, x + size - 10, y + size - 10);
                g.drawLine(x + size - 10, y + 10, x + 10, y + size - 10);
            } else if (content == Seed.NOUGHT) {
                g.setColor(Color.BLUE);
                g.drawOval(x + 10, y + 10, size - 20, size - 20);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Connect Four");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ConnectFour());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

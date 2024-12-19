/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #3
 * 1 - 5026221134 - Mohammad Affan Shofi
 * 2 - 5026231121 - Rian Chairul Ichsan
 * 3 - 5026231211 - Hafidz Putra Dermawan
 */

package TicTacToe;
import java.awt.*;

/**
 * The Cell class models each individual cell of the game board.
 */
public class Cell {
    // Define named constants for drawing
    public static final int SIZE = 120; // cell width/height (square)
    // Symbols (cross/nought) are displayed inside a cell, with padding from border
    public static final int PADDING = SIZE / 5;
    public static final int SEED_SIZE = SIZE - PADDING * 2;
    public static final int SEED_STROKE_WIDTH = 8; // pen's stroke width

    // Define properties (package-visible)
    /**
     * Content of this cell (Seed.EMPTY, Seed.CROSS, or Seed.NOUGHT)
     */
    public Seed content;
    /**
     * Row and column of this cell
     */
    int row, col;

    /**
     * Constructor to initialize this cell with the specified row and col
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        content = Seed.NO_SEED;
    }

    /**
     * Reset this cell's content to EMPTY, ready for new game
     */
    public void newGame() {
        content = Seed.NO_SEED;
    }

    /** Paint itself on the graphics canvas, given the Graphics context */
    /**
     * Paint itself on the graphics canvas, given the Graphics context
     */
    public void paint(Graphics g) {
        // Gunakan Graphics2D untuk setelan lebih lanjut
        Graphics2D g2d = (Graphics2D) g;

        // Hitung posisi gambar atau simbol dalam cell
        int x1 = col * SIZE + PADDING;
        int y1 = row * SIZE + PADDING;

        if (content == Seed.CROSS || content == Seed.NOUGHT) {
            Image img = content.getImage(); // Ambil gambar dari enum Seed
            if (img != null) {
                // Gambar image di dalam cell
                g2d.drawImage(img, x1, y1, SEED_SIZE, SEED_SIZE, null);
            } else {
                // Fallback: jika gambar tidak ada, gunakan simbol
                g2d.setStroke(new BasicStroke(SEED_STROKE_WIDTH,
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                if (content == Seed.CROSS) { // Gambar "X"
                    g2d.setColor(GameMain.COLOR_CROSS);
                    int x2 = (col + 1) * SIZE - PADDING;
                    int y2 = (row + 1) * SIZE - PADDING;
                    g2d.drawLine(x1, y1, x2, y2);
                    g2d.drawLine(x2, y1, x1, y2);
                } else if (content == Seed.NOUGHT) { // Gambar "O"
                    g2d.setColor(GameMain.COLOR_NOUGHT);
                    g2d.drawOval(x1, y1, SEED_SIZE, SEED_SIZE);
                }
            }
        }
    }
}

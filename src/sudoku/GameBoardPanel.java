/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #3
 * 1 - 5026221134 - Mohammad Affan Shofi
 * 2 - 5026231121 - Rian Chairul Ichsan
 * 3 - 5026231211 - Hafidz Putra Dermawan
 */

package sudoku;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.EventListenerList;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final int CELL_SIZE = 60;
    public static final int BOARD_WIDTH = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;

    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    private Puzzle puzzle = new Puzzle();
    private EventListenerList listenerList = new EventListenerList();
    private String currentDifficulty = "Easy";

    public GameBoardPanel() {
        super.setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);
                super.add(cells[row][col]);
            }
        }

        CellInputListener listener = new CellInputListener();
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].isEditable()) {
                    cells[row][col].addActionListener(listener);
                }
            }
        }
        super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    }

    public void newGame() {
        int cellsToGuess = getCellsToGuess(currentDifficulty);
        puzzle.newPuzzle(cellsToGuess);  // Pass the number of cells to guess

        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                try {
                    // Assuming puzzle.numbers[row][col] is an int[][] (no need for parsing)
                    int number = puzzle.numbers[row][col];
                    cells[row][col].newGame(number, puzzle.isGiven[row][col]);
                } catch (Exception e) {
                    // Handle any unexpected exceptions
                    System.out.println("Error setting number at (" + row + "," + col + "): " + e.getMessage());
                    cells[row][col].newGame(0, puzzle.isGiven[row][col]);  // Use default or error value
                }
            }
        }
    }

    public void resetGame() {
        newGame();
    }

    private int getCellsToGuess(String difficulty) {
        switch (difficulty) {
            case "Easy":
                return 40;  // Example: 40 cells to guess for Easy
            case "Medium":
                return 50;  // Example: 50 cells to guess for Medium
            case "Hard":
                return 60;  // Example: 60 cells to guess for Hard
            default:
                return 40;  // Default to Easy if something unexpected happens
        }
    }

    public void giveHint() {
        int[][] solution = puzzle.getSolution();
        for (int row = 0; row < SudokuConstants.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; col++) {
                if (cells[row][col].status == CellStatus.TO_GUESS) {
                    cells[row][col].setText(String.valueOf(solution[row][col]));
                    cells[row][col].setEditable(false);
                    cells[row][col].status = CellStatus.CORRECT_GUESS;
                    notifyStatusListeners();
                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(this, "No hints available!");
    }

    public void checkProgress() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; col++) {
                Cell cell = cells[row][col];
                if (cell.status == CellStatus.TO_GUESS || cell.status == CellStatus.WRONG_GUESS) {
                    JOptionPane.showMessageDialog(this, "Puzzle not solved yet!");
                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(this, "Congratulations! Puzzle is solved!");
    }

    public void solvePuzzle() {
        int[][] solution = puzzle.getSolution();
        for (int row = 0; row < SudokuConstants.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; col++) {
                cells[row][col].setText(String.valueOf(solution[row][col]));
                cells[row][col].setEditable(false);
                cells[row][col].status = CellStatus.CORRECT_GUESS;
            }
        }
        notifyStatusListeners();
    }

    public int getRemainingCells() {
        int count = 0;
        for (int row = 0; row < SudokuConstants.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; col++) {
                if (cells[row][col].status == CellStatus.TO_GUESS) {
                    count++;
                }
            }
        }
        return count;
    }

    public void addStatusListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    private void notifyStatusListeners() {
        for (ActionListener listener : listenerList.getListeners(ActionListener.class)) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "statusUpdate"));
        }
    }

    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Cell sourceCell = (Cell) e.getSource();
            try {
                int numberIn = Integer.parseInt(sourceCell.getText().trim());
                if (numberIn == sourceCell.number) {
                    sourceCell.status = CellStatus.CORRECT_GUESS;
                } else {
                    sourceCell.status = CellStatus.WRONG_GUESS;
                }
                sourceCell.paint();
                notifyStatusListeners();
            } catch (NumberFormatException ex) {
                sourceCell.setText("");
                JOptionPane.showMessageDialog(null, "Invalid input! Enter numbers between 1-9.");
            }
        }
    }
}

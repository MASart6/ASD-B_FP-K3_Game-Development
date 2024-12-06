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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public static final int CELL_SIZE = 60;
    public static final int BOARD_WIDTH = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;

    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    private Puzzle puzzle = new Puzzle();
    private String currentDifficulty = "Easy";

    public GameBoardPanel() {
        super.setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE));

        // Initialize cells
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);
                super.add(cells[row][col]);
            }
        }

        // Add listener to all editable cells
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

    public void setDifficulty(String difficulty) {
        currentDifficulty = difficulty;
        newGame();  // Restart the game with the new difficulty
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
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                Cell cell = cells[row][col];
                if (cell.status == CellStatus.TO_GUESS) {
                    cell.setText(String.valueOf(solution[row][col]));
                    cell.setEditable(false);
                    cell.status = CellStatus.CORRECT_GUESS;
                    notifyStatusListeners();
                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(this, "No hints available!");
    }

    public void checkProgress() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
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
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].setText(String.valueOf(solution[row][col]));
                cells[row][col].setEditable(false);
                cells[row][col].status = CellStatus.CORRECT_GUESS;
            }
        }
        notifyStatusListeners();
    }

    public int getRemainingCells() {
        int count = 0;
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (cell.status == CellStatus.TO_GUESS) {
                    count++;
                }
            }
        }
        return count;
    }

    private void highlightConflicts() {
        // Clear all previous highlights
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].setHighlight(false); // Ensure no lingering highlights
            }
        }


        // Detect conflicts in rows and columns
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (!cells[row][col].isEditable()) continue; // Skip non-editable cells

                int num = cells[row][col].getEnteredValue();
                if (num == 0) continue; // Skip empty cells

                // Check for conflicts in the same row and column
                boolean conflict = false;
                for (int i = 0; i < SudokuConstants.GRID_SIZE; ++i) {
                    // Check row
                    if (i != col && cells[row][i].getEnteredValue() == num) {
                        cells[row][col].setHighlight(true);
                        cells[row][i].setHighlight(true);
                        conflict = true;
                    }
                    // Check column
                    if (i != row && cells[i][col].getEnteredValue() == num) {
                        cells[row][col].setHighlight(true);
                        cells[i][col].setHighlight(true);
                        conflict = true;
                    }
                }
            }
        }
    }

    private void notifyStatusListeners() {
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "statusUpdate");
        for (ActionListener listener : listenerList.getListeners(ActionListener.class)) {
            listener.actionPerformed(event);
        }
    }


    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Cell sourceCell = (Cell) e.getSource();
            int numberIn = Integer.parseInt(sourceCell.getText());

            // Check for conflicts after user input
            highlightConflicts();

            // After checking for conflicts, update the cell status (if needed)
            // e.g., highlight the cells with the same value as the guess
            highlightSameValueCells(numberIn);
        }
    }

    private void highlightSameValueCells(int value) {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].getEnteredValue() == value) {
                    cells[row][col].setHighlight(true);
                }
            }
        }
    }
}

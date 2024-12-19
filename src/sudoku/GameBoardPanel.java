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

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public static final int CELL_SIZE = 60;
    public static final int BOARD_WIDTH = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;

    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    private Puzzle puzzle = new Puzzle();
    private int filledCellsCount = 0;  // Jumlah sel yang diisi dengan benar
    private int totalCells = SudokuConstants.GRID_SIZE * SudokuConstants.GRID_SIZE;

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
        puzzle.newPuzzle(cellsToGuess); // Pass the number of cells to guess

        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                try {
                    // Assuming puzzle.numbers[row][col] is an int[][] (no need for parsing)
                    int number = puzzle.numbers[row][col];
                    cells[row][col].newGame(number, puzzle.isGiven[row][col]);
                } catch (Exception e) {
                    // Handle any unexpected exceptions
                    System.out.println("Error setting number at (" + row + "," + col + "): " + e.getMessage());
                    cells[row][col].newGame(0, puzzle.isGiven[row][col]); // Use default or error value
                }
            }
        }
    }

    public boolean isSolved() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].status == CellStatus.TO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS) {
                    return false;
                }
            }
        }
        return true;
    }

    public void resetGame() {
        newGame();
    }

    public void setDifficulty(String difficulty) {
        currentDifficulty = difficulty;
        newGame(); // Restart the game with the new difficulty
    }

    private int getCellsToGuess(String difficulty) {
        switch (difficulty) {
            case "Easy":
                return 40; // Example: 40 cells to guess for Easy
            case "Medium":
                return 50; // Example: 50 cells to guess for Medium
            case "Hard":
                return 60; // Example: 60 cells to guess for Hard
            default:
                return 40; // Default to Easy if something unexpected happens
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
            try {
                int numberIn = Integer.parseInt(sourceCell.getText());
                if (numberIn == sourceCell.number) {
                    if (sourceCell.status != CellStatus.CORRECT_GUESS) {
                        filledCellsCount++;  // Tambah jumlah sel yang terisi dengan benar
                    }
                    sourceCell.status = CellStatus.CORRECT_GUESS;
                } else {
                    if (sourceCell.status == CellStatus.CORRECT_GUESS) {
                        filledCellsCount--;  // Kurangi jumlah sel yang terisi jika sebelumnya benar
                    }
                    sourceCell.status = CellStatus.WRONG_GUESS;
                }
                sourceCell.paint();  // Repaint cell
            } catch (NumberFormatException ex) {
                sourceCell.setText("");  // Reset jika input tidak valid
            }

            if (isSolved()) {
                JOptionPane.showMessageDialog(null, "Congratulations! You solved the puzzle!");
                newGame();
            }
        }
    }
}

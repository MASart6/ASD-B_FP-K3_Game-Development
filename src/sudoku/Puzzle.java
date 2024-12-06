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

/**
 * The Sudoku number puzzle to be solved
 */
public class Puzzle {
    // All variables have package access
    // The numbers on the puzzle
    int[][] numbers = new int[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    // The clues - isGiven (no need to guess) or need to guess
    boolean[][] isGiven = new boolean[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];

    private int[][] solution = new int[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];

    // Constructor
    public Puzzle() {
        super();
    }

    // Generate a new puzzle given the number of cells to be guessed, which can be
    // used
    // to control the difficulty level.
    // This method shall set (or update) the arrays numbers and isGiven
    public void newPuzzle(int cellsToGuess) {
        // Generate a solved puzzle (you could implement your own algorithm or hardcode
        // it)
        generateSolvedPuzzle();

        // Now, create a puzzle with `cellsToGuess` empty cells (difficulty control)
        setPuzzleDifficulty(cellsToGuess);
    }

    // Method to generate a fully solved Sudoku puzzle (for simplicity, we'll
    // hardcode it)
    private void generateSolvedPuzzle() {
        // Hardcoded solved puzzle
        int[][] hardcodedNumbers = {
                { 5, 3, 4, 6, 7, 8, 9, 1, 2 },
                { 6, 7, 2, 1, 9, 5, 3, 4, 8 },
                { 1, 9, 8, 3, 4, 2, 5, 6, 7 },
                { 8, 5, 9, 7, 6, 1, 4, 2, 3 },
                { 4, 2, 6, 8, 5, 3, 7, 9, 1 },
                { 7, 1, 3, 9, 2, 4, 8, 5, 6 },
                { 9, 6, 1, 5, 3, 7, 2, 8, 4 },
                { 2, 8, 7, 4, 1, 9, 6, 3, 5 },
                { 3, 4, 5, 2, 8, 6, 1, 7, 9 }
        };

        // Copy from hardcodedNumbers into numbers array
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                numbers[row][col] = hardcodedNumbers[row][col];
            }
        }

        // Initially, all cells are considered given
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                isGiven[row][col] = true; // Set all cells to be initially filled
            }
        }

        // Copy solved puzzle into solution array
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                solution[row][col] = numbers[row][col];
            }
        }
    }

    // Method to remove numbers from the puzzle based on difficulty level
    private void setPuzzleDifficulty(int cellsToGuess) {
        // Create a list of random positions to clear based on cellsToGuess
        int cellsRemoved = 0;
        while (cellsRemoved < cellsToGuess) {
            int row = (int) (Math.random() * SudokuConstants.GRID_SIZE);
            int col = (int) (Math.random() * SudokuConstants.GRID_SIZE);

            if (isGiven[row][col]) {
                // Remove the number by setting it to 0 and updating isGiven to false
                numbers[row][col] = 0;
                isGiven[row][col] = false; // The cell is now to be guessed
                cellsRemoved++;
            }
        }
    }

    // Accessor methods for numbers and isGiven, in case you need them in
    // GameBoardPanel
    public int[][] getNumbers() {
        return numbers;
    }

    public boolean[][] getIsGiven() {
        return isGiven;
    }

    public int[][] getSolution() {
        return solution;
    }
}

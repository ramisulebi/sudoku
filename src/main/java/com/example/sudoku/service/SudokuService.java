package com.example.sudoku.service;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

@Service
public class SudokuService {
    private static final int SIZE = 4;
    private static final int BOX_SIZE = 2;
    private static final int INITIAL_FILLED_CELLS = 5; //I put 5 but can be any number (can be harder or easier) 

    public int[][] generatePuzzle() {
        int[][] board = new int[SIZE][SIZE];
        
        fillRandomCells(board);

        // Copy the current board and solve it to generate the solution
        int[][] solution = copyBoard(board);
        
        // Try to solve the puzzle, if it can't be solved, generate a new puzzle
        if (!solve(solution)) {
            return generatePuzzle();
        }

        return board;
    }

    // Returns the solution to the puzzle
    public int[][] getSolution(int[][] puzzle) {
        int[][] solution = copyBoard(puzzle);
        if (!solve(solution)) {
            // If the solution can't be found, the puzzle is unsolvable, return an empty board
            return new int[SIZE][SIZE];
        }
        return solution;
    }
    
    //To fill puzzle with random number 
    private void fillRandomCells(int[][] board) {
        int cellsFilled = 0;
        while (cellsFilled < INITIAL_FILLED_CELLS) {
            int row = getRandomNumber(SIZE);
            int col = getRandomNumber(SIZE);
            int num = getRandomNumber(SIZE) + 1;

            if (board[row][col] == 0 && isPlacementValid(board, row, col, num)) {
                board[row][col] = num;
                cellsFilled++;
            }
        }
    }

    //to get random numbers from 1 - 4
    private int getRandomNumber(int max) {
        return ThreadLocalRandom.current().nextInt(max);
    }

    //To check If the number is not exist in row, column and small square (2x2)
    private boolean isPlacementValid(int[][] board, int row, int col, int num) {
        return isRowValid(board, row, num) &&
               isColumnValid(board, col, num) &&
               isBoxValid(board, row, col, num);
    }

    private boolean isRowValid(int[][] board, int row, int num) {
        for (int col = 0; col < SIZE; col++) {
            if (board[row][col] == num) {
                return false;
            }
        }
        return true;
    }

    private boolean isColumnValid(int[][] board, int col, int num) {
        for (int row = 0; row < SIZE; row++) {
            if (board[row][col] == num) {
                return false;
            }
        }
        return true;
    }

    private boolean isBoxValid(int[][] board, int row, int col, int num) {
        int boxRowStart = row - row % BOX_SIZE;
        int boxColStart = col - col % BOX_SIZE;

        for (int i = 0; i < BOX_SIZE; i++) {
            for (int j = 0; j < BOX_SIZE; j++) {
                if (board[boxRowStart + i][boxColStart + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    //check if there is a solution to the puzzle using recursive (Backtracking) 
    private boolean solve(int[][] board) {
        int[] emptyCell = findEmptyCell(board);
        if (emptyCell == null) {
            return true;
        }

        int row = emptyCell[0];
        int col = emptyCell[1];

        for (int num = 1; num <= SIZE; num++) {
            if (isPlacementValid(board, row, col, num)) {
                board[row][col] = num;
                if (solve(board)) {
                    return true;
                }
                board[row][col] = 0;
            }
        }

        return false;
    }

    private int[] findEmptyCell(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    return new int[] {row, col};
                }
            }
        }
        return null;
    }

    private int[][] copyBoard(int[][] original) {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }
}

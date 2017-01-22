package com.johnteckemeyer.sudokusolver;

import android.util.Log;

/**
 * Created by jptec on 1/19/2017.
 */

public class Sudoku {

    final int BOARD_SIZE = 9;
    final int MIN_NUMBER = 1;
    final int MAX_NUMBER = 9;
    final int BOX_SIZE   = 3;

    private int[][] sudokuTable;

    public Sudoku (int[][] sudokuTable) {
        this.sudokuTable = sudokuTable;
    }

    public void solvePuzzle () {

        if (!solve(0,0)) {
            Log.i("Info", "No feasible solution found...");
        } else {
            showResults();
        }


    }

    private boolean solve (int rowIndex, int colIndex) {

        if (rowIndex == BOARD_SIZE && ++colIndex == BOARD_SIZE) {
            return true;
        }

        if (rowIndex == BOARD_SIZE) {
            rowIndex = 0;
        }

        if (sudokuTable[rowIndex][colIndex] != 0) {
            return solve(rowIndex + 1, colIndex);
        }

        for (int numbers = MIN_NUMBER; numbers <= MAX_NUMBER; numbers++) {

            if (valid(rowIndex, colIndex, numbers)) {
                sudokuTable[rowIndex][colIndex] = numbers;

                if (solve(rowIndex+1, colIndex))
                    return true;
            }
        }

        sudokuTable[rowIndex][colIndex] = 0;

        return false;

    }

    public int[][] getSolution () {
        if (sudokuTable.length > 0) {
            return sudokuTable;
        } else {
            return null;
        }
    }

    private boolean valid (int rowIndex, int colIndex, int actualNumber) {

        // if the number is already in the row or column, it is not part of the solution
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (sudokuTable[i][colIndex] == actualNumber || sudokuTable[rowIndex][i] == actualNumber)
                return false;
        }

        // if the number is already part of the 3X3 square, it is not part of the solution
        int boxRowOffset = (rowIndex / 3) *  BOX_SIZE;
        int boxColOffset = (colIndex / 3) *  BOX_SIZE;

        for (int i = 0; i < BOX_SIZE; i++) {
            for (int j = 0; j < BOX_SIZE; j++) {
                if (actualNumber == sudokuTable[boxRowOffset + i][boxColOffset + j])
                    return false;
            }
        }

        return true;

    }

    private void showResults () {

        String resultRow = "";

        for (int i = 0; i < BOARD_SIZE; i++) {

            for (int j = 0; j < BOARD_SIZE; j++) {

                resultRow += String.valueOf(sudokuTable[i][j] + " ");

                if ((j+1) % 3 == 0)
                    resultRow += "   ";
            }

            Log.i("Info", resultRow);

            if ((i+1) % 3 == 0)
                Log.i("Info", " ");

            resultRow = "";
        }
    }

}

package com.johnteckemeyer.sudokusolver;

/**
 * Created by jptec on 1/20/2017.
 */
public class SudokuGrid {

    private String [][] theGrid;

    private static SudokuGrid ourInstance = new SudokuGrid();

    public static SudokuGrid getInstance() {
        return ourInstance;
    }

    private SudokuGrid() {

        theGrid = new String[9][9];
    }

    public String[][] getGrid () {
        return theGrid;
    }
}

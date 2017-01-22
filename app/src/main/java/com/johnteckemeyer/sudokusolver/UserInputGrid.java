package com.johnteckemeyer.sudokusolver;

/**
 * Created by jptec on 1/21/2017.
 */
public class UserInputGrid {

    private String[][] theGrid;

    private static UserInputGrid ourInstance = new UserInputGrid();

    public static UserInputGrid getInstance() {
        return ourInstance;
    }

    private UserInputGrid() {

        theGrid = new String[9][9];
    }

    public String[][] getGrid () {
        return theGrid;
    }
}

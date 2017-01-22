package com.johnteckemeyer.sudokusolver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GridView gridView;
    private GridAdapter adapter;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button buttonClear;
    private Button buttonSolve;
    public int selectedNumber;

    private String[][] sudokuGrid;
    private boolean isGoodInput;
    private boolean boardIsGood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedNumber = 0;
        isGoodInput = true;
        boardIsGood = true;

        gridView = (GridView) findViewById(R.id.gridView);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        buttonClear = (Button) findViewById(R.id.buttonClear);
        buttonSolve = (Button) findViewById(R.id.buttonSolve);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonClear.setOnClickListener(this);
        buttonSolve.setOnClickListener(this);

        adapter = new GridAdapter(getApplicationContext());

        gridView.setAdapter(adapter);

        // Enters values into any cell that is touched
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setNumber(i, selectedNumber);

                adapter.notifyDataSetChanged();

                checkCellValidity(i);

                if (!boardIsGood) {
                    buttonSolve.setEnabled(false);
                } else {
                    buttonSolve.setEnabled(true);
                }

            }
        });

        setUpGrid();
    }
    
    public void checkCellValidity (int position) {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudokuGrid[i][j] = adapter.getItem(i * 9 + j);
            }
        }

        isGoodInput = true;
        int iPos = position / 9;
        int jPos = position % 9;
        int iBoxOffset = (iPos / 3) * 3;
        int jBoxOffset = (jPos / 3) * 3;

        // Check all the cells in the same row and column for any with the same number
        for (int i = 0; i < 9; i++) {

            if (sudokuGrid[i][jPos] == String.valueOf(selectedNumber) && (i * 9 + jPos) != position) {
                isGoodInput = false;
                adapter.setBad(i * 9 + jPos);
            } else {

            }

            if (sudokuGrid[iPos][i] == String.valueOf(selectedNumber) && (iPos * 9 + i) != position) {
                isGoodInput = false;
                adapter.setBad(iPos * 9 + i);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (sudokuGrid[iBoxOffset + i][jBoxOffset + j] == String.valueOf(selectedNumber)
                        && (((iBoxOffset + i) * 9) + (jBoxOffset + j)) != position) {
                    isGoodInput = false;
                    adapter.setBad(((iBoxOffset + i) * 9) + (jBoxOffset + j));
                }
            }
        }

        // Disable the solve button
        if (!isGoodInput) {
            adapter.setBad(position);
            boardIsGood = false;
        } else {
            adapter.setGood(position);

            boardIsGood = adapter.isBoardOK();
        }

    }

    // Setup an empty grid
    public void setUpGrid () {

        sudokuGrid = SudokuGrid.getInstance().getGrid();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudokuGrid[i][j] = "";
                adapter.addCell("");
            }
        }
    }

    // Get user clicks on buttons
    @Override
    public void onClick(View view) {

        switch (view.getTag().toString()) {
            case "1":
                selectedNumber = 1;
                break;
            case "2":
                selectedNumber = 2;
                break;
            case "3":
                selectedNumber = 3;
                break;
            case "4":
                selectedNumber = 4;
                break;
            case "5":
                selectedNumber = 5;
                break;
            case "6":
                selectedNumber = 6;
                break;
            case "7":
                selectedNumber = 7;
                break;
            case "8":
                selectedNumber = 8;
                break;
            case "9":
                selectedNumber = 9;
                break;
            case "clear":
                selectedNumber = 0;
                break;
            case "solve":
                solve ();
                break;
            default:
                break;
        }
    }

    public void solve () {

        ArrayList<String> temp = new ArrayList<>();

        for (int i = 0; i < 81; i++) {
            temp.add(adapter.getItem(i));
        }

        if (temp == null) {
            Log.i("Info", "ArrayList is empty");
        } else {

            String[][] userInputGrid = UserInputGrid.getInstance().getGrid();

            int [][] array = new int [9][9];

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (temp.get(i * 9 + j) == "") {
                        array[i][j] = 0;
                    } else {
                        userInputGrid[i][j] = temp.get(i * 9 + j);
                        array[i][j] = Integer.parseInt(temp.get(i * 9 + j));
                    }
                }
            }

            Sudoku sudokuPuzzle = new Sudoku(array);
            sudokuPuzzle.solvePuzzle();

            array = sudokuPuzzle.getSolution();

            if (array != null) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        sudokuGrid[i][j] = String.valueOf(array[i][j]);
                    }
                }
            }

            Toast.makeText(this, "Puzzle Solved", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), ExposeActivity.class);

            startActivity(intent);
        }
    }
}

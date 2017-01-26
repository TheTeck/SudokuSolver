package com.johnteckemeyer.sudokusolver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
    private String[][] userInputGrid;

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

        userInputGrid = UserInputGrid.getInstance().getGrid();

        adapter = new GridAdapter(getApplicationContext());

        gridView.setAdapter(adapter);

        // Enters values into any cell that is touched
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String oldInput = adapter.getItem(i); // In case there is already a bad value in this cell

                adapter.setNumber(i, selectedNumber);

                adapter.notifyDataSetChanged();

                if (adapter.isGoodCell(i)) {    // Cell is clear or has already has a valid value
                    checkCellValidity(i, selectedNumber);
                } else {    // Cell already has an invalid value

                    checkCellValidity(i, selectedNumber);

                    for (int j = 0; j < 9; j++) { // Check for any cells that were invalid in the row and column
                        if (sudokuGrid[i / 9][j].equals(oldInput)) {
                            checkCellValidity((i / 9) * 9 + j, Integer.parseInt(oldInput));
                        }
                        if (sudokuGrid[j][i % 9].equals(oldInput)) {
                            checkCellValidity(j * 9 + (i % 9), Integer.parseInt(oldInput));
                        }
                    }
                    for (int j = 0; j < 3; j++) { // Check for any cells that were invalid in the same 9-cell
                        for (int k = 0; k < 3; k++) {

                            int x = ((i % 9) / 3) * 3 + k;
                            int y = ((i / 9) / 3) * 3 + j;

                            if (sudokuGrid[y][x].equals(oldInput)) {
                                checkCellValidity(y * 9 + x, Integer.parseInt(oldInput));
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                }

                if (!boardIsGood) {
                    buttonSolve.setEnabled(false);
                } else {
                    buttonSolve.setEnabled(true);
                }

            }
        });

        setUpGrid();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.newGridMenuItem) {
            adapter.deleteGrid();
            setUpGrid();
            adapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }
    
    public void checkCellValidity (int position, int cellValue) {

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

            if (sudokuGrid[i][jPos].equals(String.valueOf(cellValue)) && (i * 9 + jPos) != position) {
                isGoodInput = false;
                adapter.setBad(i * 9 + jPos);
            }

            if (sudokuGrid[iPos][i].equals(String.valueOf(cellValue)) && (iPos * 9 + i) != position) {
                isGoodInput = false;
                adapter.setBad(iPos * 9 + i);
            }
        }

        // Check all the cells in the same 9-cell square for any with the same number
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (sudokuGrid[iBoxOffset + i][jBoxOffset + j].equals(String.valueOf(cellValue))
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
                userInputGrid[i][j] = "";
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

        int[][] array = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (temp.get(i * 9 + j).equals("")) {
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

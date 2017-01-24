package com.johnteckemeyer.sudokusolver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

public class ExposeActivity extends AppCompatActivity implements View.OnClickListener {

    private String [][] solutionGrid;
    private String [][] inputGrid;
    private int exposeState;

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
    private ImageButton buttonHoriz;
    private ImageButton buttonVert;
    private ImageButton buttonNineCell;
    private ImageButton buttonAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expose);

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
        buttonHoriz = (ImageButton) findViewById(R.id.rowButton);
        buttonVert = (ImageButton) findViewById(R.id.columnButton);
        buttonNineCell = (ImageButton) findViewById(R.id.nineCellButton);
        buttonAll = (ImageButton) findViewById(R.id.everythingButton);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonHoriz.setOnClickListener(this);
        buttonVert.setOnClickListener(this);
        buttonNineCell.setOnClickListener(this);
        buttonAll.setOnClickListener(this);

        solutionGrid = SudokuGrid.getInstance().getGrid();
        inputGrid = UserInputGrid.getInstance().getGrid();

        exposeState = -1;

        adapter = new GridAdapter(getApplicationContext());

        gridView.setAdapter(adapter);

        // Enters values into any cell that is touched
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (exposeState == 0) {
                    int row = i / 9;
                    int rowPos = row * 9;

                    for (int j = rowPos; j < rowPos + 9; j++) {
                        if (row == 0) {
                            adapter.setNumber (j, Integer.parseInt(solutionGrid[row][j]));
                        } else {
                            adapter.setNumber(j, Integer.parseInt(solutionGrid[row][j % rowPos]));
                        }
                        adapter.notifyDataSetChanged();
                    }

                } else if (exposeState == 1) {
                    int col = i % 9;

                    for (int j = 0; j < 9; j++) {
                        adapter.setNumber(j * 9 + col, Integer.parseInt(solutionGrid[j][col]));
                    }
                    adapter.notifyDataSetChanged();

                } else if (exposeState == 2) {
                    int colOffset = (i / 9) / 3;
                    int rowOffset = (i % 9) / 3;

                    for (int j = 0; j < 3; j++) {
                        for (int k = 0; k < 3; k++) {
                            adapter.setNumber (((colOffset * 3 + j) * 9) + (rowOffset * 3 + k), Integer.parseInt(solutionGrid[colOffset * 3 + j][rowOffset * 3 + k]));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        setUpGrid();
    }

    // Setup an empty grid
    public void setUpGrid () {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                adapter.addCell(inputGrid[i][j]);
            }
        }

        adapter.notifyDataSetChanged();
    }

    // Get user clicks on buttons
    @Override
    public void onClick(View view) {

        switch (view.getTag().toString()) {
            case "1":
                exposeNumber("1");
                exposeState = -1;
                break;
            case "2":
                exposeNumber("2");
                exposeState = -1;
                break;
            case "3":
                exposeNumber("3");
                exposeState = -1;
                break;
            case "4":
                exposeNumber("4");
                exposeState = -1;
                break;
            case "5":
                exposeNumber("5");
                exposeState = -1;
                break;
            case "6":
                exposeNumber("6");
                exposeState = -1;
                break;
            case "7":
                exposeNumber("7");
                exposeState = -1;
                break;
            case "8":
                exposeNumber("8");
                exposeState = -1;
                break;
            case "9":
                exposeNumber("9");
                exposeState = -1;
                break;
            case "row":
                exposeState = 0;
                break;
            case "column":
                exposeState = 1;
                break;
            case "nineCell":
                exposeState = 2;
                break;
            case "all":
                exposeAll ();
                break;
            default:
                break;
        }
    }

    private void exposeNumber (String number) {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (solutionGrid[i][j].equals(number)) {
                    adapter.setNumber(i * 9 + j, Integer.parseInt(solutionGrid[i][j]));
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void exposeAll () {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                adapter.setNumber(i * 9 + j, Integer.parseInt(solutionGrid[i][j]));
            }
        }

        adapter.notifyDataSetChanged();
    }
}

package com.johnteckemeyer.sudokusolver;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jptec on 1/18/2017.
 */

public class GridAdapter extends BaseAdapter {

    ArrayList<String> mItems;
    ArrayList<Boolean> mIsGoodInput;
    Context mContext;
    int [] gridAccentCells; // Holds the location of alternating 9-cell grid clusters

    public GridAdapter(Context context) {
        mItems = new ArrayList<>();
        mIsGoodInput = new ArrayList<>();
        this.mContext = context;
        gridAccentCells = new int [] {0,2,3,5,6,8,10,13,16,18,20,21,23,24,26};
    }

    public void addCell(String number) {
        mItems.add(number);
        mIsGoodInput.add(true);
    }

    public void setNumber(int position, int value) {
        if (value == 0) {
            mItems.set(position, "");
        } else {
            mItems.set(position, String.valueOf(value));
        }
    }

    public boolean isBoardOK () {
        for (int i = 0; i < 81; i++) {
            if (!mIsGoodInput.get(i)) {
                return false;
            }
        }

        return true;
    }

    public void setBad (int position) {
        mIsGoodInput.set(position, false);
    }

    public void setGood (int position) {
        mIsGoodInput.set(position, true);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public String getItem(final int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.cell_card, viewGroup, false);
            view.setTag(String.valueOf(position));
        }


        TextView numText = (TextView) view.findViewById(R.id.textView);

        for (int i : gridAccentCells) {
            if (position / 3 == i)
                numText.setBackgroundResource(R.color.colorCell1);
        }

        numText.setText(mItems.get(position));

        if (!mIsGoodInput.get(position)) {
            numText.setTextColor(Color.RED);
        } else {
            numText.setTextColor(Color.BLACK);
        }

        return view;
    }
}

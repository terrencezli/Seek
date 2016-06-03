package com.aacfslo.tzli.seek;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by terrence on 5/23/16.
 */
public class ChartCard extends Card {

    protected LineChart lineChart;
    protected ArrayList<Entry> data;

    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public ChartCard(Context context) {
        this(context, R.layout.chart_view);
    }

    /**
     *
     * @param context
     * @param innerLayout
     */
    public ChartCard(Context context, int innerLayout) {
        super(context, innerLayout);
        data = new ArrayList<>();
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        lineChart = (LineChart) parent.findViewById(R.id.chart);

        // creating list of entry
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(2f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(9f, 5));

        LineDataSet dataset = new LineDataSet(entries, "# of MOI's");
        dataset.setDrawFilled(true);

        // creating labels
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        labels.add("July");
        labels.add("August");
        labels.add("September");
        labels.add("October");
        labels.add("November");
        labels.add("December");

        LineData data = new LineData(labels, dataset);
        lineChart.setData(data); // set the data and list of lables into chart
        lineChart.setDescription("Meet up");
        lineChart.setDragDecelerationEnabled(true);
        lineChart.animateXY(3000, 3000);
    }
}

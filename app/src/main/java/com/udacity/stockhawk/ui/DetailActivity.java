package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.charts.MyAxisValueFormatter;
import com.udacity.stockhawk.charts.MyYAxisValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by prakash_pun on 1/24/2017.
 */

public class DetailActivity extends AppCompatActivity {

    private String symbol;
    private String history;
    private String name;

    private BarChart chart;

    private List<BarEntry> historicalQuoteEntries;

    Calendar[] datesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();
        symbol = extras.getString("SYMBOL");
        history = extras.getString("HISTORY");
        name = extras.getString("NAME");

        setTitle(name);
        chart = (BarChart) findViewById(R.id.chart);

        extractHistoricalQuotes();
        displayChart();
    }

    protected void extractHistoricalQuotes(){
        Float close;
        historicalQuoteEntries = new ArrayList<BarEntry>();

        List<String> historyQuotesList = Arrays.asList(history.split("\n"));

        int totalQuotes = historyQuotesList.size();
        datesArray = new Calendar[totalQuotes];

        int i = totalQuotes-1;
        for(String quote : historyQuotesList){
            String[] quoteItems = quote.split(",");

            datesArray[i] = Calendar.getInstance();
            datesArray[i].setTimeInMillis(Long.valueOf(quoteItems[0]));

            close = Float.valueOf(quoteItems[1]);

            historicalQuoteEntries.add(new BarEntry(i, close));
            i--;

        }
    }


    protected  void displayChart(){
        BarDataSet set = new BarDataSet(historicalQuoteEntries, "Stock Closing Value");
        set.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new MyAxisValueFormatter(datesArray));
        xAxis.setGranularity(1f);

        YAxis left = chart.getAxisLeft();
        left.setValueFormatter(new MyYAxisValueFormatter());

        YAxis right = chart.getAxisRight();
        right.setValueFormatter(new MyYAxisValueFormatter());

        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.invalidate(); // refresh
        //chart.animateY(50);
        // chart.animateX(500);
    }

}

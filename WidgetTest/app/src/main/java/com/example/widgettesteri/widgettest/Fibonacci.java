package com.example.widgettesteri.widgettest;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Fibonacci extends AppCompatActivity {

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
            TextView resultView = (TextView) findViewById(R.id.textView);


            Intent widgetUpdateIntent = new Intent(context, ProgressWidget.class);
            widgetUpdateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            int[] ids = appWidgetManager
                    .getAppWidgetIds(new ComponentName(getApplication(), ProgressWidget.class));

            widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            sendBroadcast(widgetUpdateIntent);



            if (intent.getAction().equals(NaiveFibonacciIntentService.BROADCAST_ACTION_START)) {
                pb.setIndeterminate(true);
                pb.setVisibility(View.VISIBLE);
                resultView.setText("");

            }
            if (intent.getAction().equals(NaiveFibonacciIntentService.BROADCAST_ACTION_FINISH)) {
                int val = intent.getIntExtra(NaiveFibonacciIntentService.EXTRA_NUMBER, 0);
                pb.setIndeterminate(false);
                pb.setVisibility(View.GONE);
                resultView.setText(Integer.toString(val));
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fibonacci);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
        if (NaiveFibonacciIntentService.isCalculating())
        {
            pb.setIndeterminate(true);
            pb.setVisibility(View.VISIBLE);
        }
        else {
            pb.setIndeterminate(false);
            pb.setVisibility(View.GONE);
            TextView resultView = (TextView) findViewById(R.id.textView);
            resultView.setText(Integer.toString(NaiveFibonacciIntentService.getLastResult(this)));

        }

        final TextView textFibOf = (TextView) findViewById(R.id.textFibOf);
        final SeekBar sb = (SeekBar) findViewById(R.id.seekBar);
        sb.setMax(140);
        textFibOf.setText(Integer.toString(25));

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int val = i / 10 + 25;
                textFibOf.setText(Integer.toString(val));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Don't allow the user to queue multiple calculations
                if (NaiveFibonacciIntentService.isCalculating())
                    return;
                int i = sb.getProgress();
                int val = i / 10 + 25;
                NaiveFibonacciIntentService.startAction(view.getContext(), val);
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(NaiveFibonacciIntentService.BROADCAST_ACTION_START);
        filter.addAction(NaiveFibonacciIntentService.BROADCAST_ACTION_FINISH);
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this);
        bm.registerReceiver(broadcastReceiver, filter);
    }

}

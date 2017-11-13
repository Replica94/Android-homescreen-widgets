package com.example.widgettesteri.widgettest;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NaiveFibonacciIntentService extends IntentService {

    public static final String BROADCAST_ACTION_START  = "com.example.widgettesteri.widgettest.broadcast_action.CALCULATION_STARTED";
    public static final String BROADCAST_ACTION_FINISH  = "com.example.widgettesteri.widgettest.broadcast_action.CALCULATION_FINISHED";
    private static final String ACTION_CALCULATE = "com.example.widgettesteri.widgettest.action.CALCULATE";

    public static final String EXTRA_NUMBER = "com.example.widgettesteri.widgettest.extra.NUMBER";
    private static boolean ongoingCalculation = false;

    static boolean isCalculating(){
        return ongoingCalculation;
    }

    private static final String PREFERENCES_NAME = "FIBONACCI_PREF";
    private static final String LASTRESULT_KEY = "LASTRESULT_KEY";
    static int getLastResult(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_NAME, 0);
        return prefs.getInt(LASTRESULT_KEY, -1);
    }

    public NaiveFibonacciIntentService() {
        super("NaiveFibonacciIntentService");
    }

    /**
     * Starts the Fibonacci number calculation service.
     *
     * @see IntentService
     */
    public static void startAction(Context context, int number) {
        Intent intent = new Intent(context, NaiveFibonacciIntentService.class);
        intent.setAction(ACTION_CALCULATE);
        intent.putExtra(EXTRA_NUMBER, number);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            final String action = intent.getAction();
            if (ACTION_CALCULATE.equals(action)) {

                {
                    Intent retIntent = new Intent(BROADCAST_ACTION_START);
                    LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
                    broadcastManager.sendBroadcast(retIntent);
                }
                final int number = intent.getIntExtra(EXTRA_NUMBER, 1);

                ongoingCalculation = true;
                int ret = calculateFib(number);
                ongoingCalculation = false;
                {

                    Intent retIntent = new Intent(BROADCAST_ACTION_FINISH);
                    LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
                    retIntent.putExtra(EXTRA_NUMBER, ret);
                    broadcastManager.sendBroadcast(retIntent);
                }

                SharedPreferences prefs =  getSharedPreferences(PREFERENCES_NAME, 0);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(LASTRESULT_KEY, ret);
                editor.commit();

            }
        }
    }

    static private int calculateFib(int num) {
        if (num <= 0)
            return 0;
        if (num <= 2)
            return 1;
        return calculateFib(num - 1) + calculateFib(num - 2);
    }

}

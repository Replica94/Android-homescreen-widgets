package com.example.widgettesteri.widgettest;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class ProgressWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.progress_widget);


        if (NaiveFibonacciIntentService.isCalculating()) {
            views.setProgressBar(R.id.widget_progress, 1, 1, true);
            views.setViewVisibility(R.id.widget_progress, View.VISIBLE);
            views.setViewVisibility(R.id.widget_image, View.GONE);
        }
        else {

            views.setViewVisibility(R.id.widget_progress, View.GONE);
            views.setViewVisibility(R.id.widget_image, View.VISIBLE);
            Intent intent = new Intent(context, Fibonacci.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

            views.setOnClickPendingIntent(R.id.widget_image, pendingIntent);

        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


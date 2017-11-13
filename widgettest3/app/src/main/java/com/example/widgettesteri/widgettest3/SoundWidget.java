package com.example.widgettesteri.widgettest3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link SoundWidgetConfigureActivity SoundWidgetConfigureActivity}
 */
public class SoundWidget extends AppWidgetProvider {

    public static final String CLICK_ACTION = "com.example.widgettesteri.widgettest3.soundwidget.CLICK_ACTION";
    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(CLICK_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            Uri uri = SoundWidgetConfigureActivity.loadSoundUriPref(context, appWidgetId);
            if (uri == null) {
                Toast.makeText(context, "Thy sound be lost! " + appWidgetId, Toast.LENGTH_SHORT).show();
            }
            else {
                MediaPlayer mp = MediaPlayer.create(context, uri);
                mp.start();
            }
        }
        super.onReceive(context, intent);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Uri imageUri = SoundWidgetConfigureActivity.loadImageUriPref(context, appWidgetId);



        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        if (imageUri != null)
            views.setImageViewUri(R.id.app_widget_button, imageUri);
        //views.setTextViewText(R.id.appwidget_text, widgetText);
        Intent clickIntent = new Intent(context, SoundWidget.class);

        clickIntent.setAction(CLICK_ACTION);
        clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.app_widget_button, pendingIntent);

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
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            SoundWidgetConfigureActivity.deletePref(context, appWidgetId);
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


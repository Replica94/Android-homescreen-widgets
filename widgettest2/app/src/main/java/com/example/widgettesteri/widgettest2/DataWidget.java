package com.example.widgettesteri.widgettest2;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link DataWidgetConfigureActivity DataWidgetConfigureActivity}
 */
public class DataWidget extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(LIST_CLICK_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int id = intent.getIntExtra(EXTRA_LIST_ITEM, 0);
            Toast.makeText(context, "List item " + id, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }

    static class Data
    {
        public String text;
        public int number;
        Data(String text, int number)
        {
            this.text = text;
            this.number = number;
        }
    }
    public static final String LIST_CLICK_ACTION = "com.example.widgettesteri.widgettest2.datawidget.LIST_CLICK_ACTION";
    public static final String EXTRA_LIST_ITEM = "com.example.widgettesteri.widgettest2.datawidget.EXTRA_LIST_ITEM";
    public static final String EXTRA_ITEM_COUNT = "com.example.widgettesteri.widgettest2.datawidget.EXTRA_ITEM_COUNT";
    static class DataRemoteViewsFactory implements
            RemoteViewsService.RemoteViewsFactory {
        private int count;
        private List<Data> data = new ArrayList<Data>();
        private Context mContext;
        private int mAppWidgetId;

        public DataRemoteViewsFactory(Context context, Intent intent) {
            count = intent.getIntExtra(EXTRA_ITEM_COUNT,0);
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            for (int i = 1; i <= count; i++) {
                data.add(new Data("I'm number " + i + "!", i));
            }

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {

            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.data_item);
            rv.setTextViewText(R.id.textView, data.get(i).text);

            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(DataWidget.EXTRA_LIST_ITEM, (int)getItemId(i));

            rv.setOnClickFillInIntent(R.id.textView, fillInIntent);

            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return data.get(i).number;
    }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                            int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.data_widget);

        Intent intent = new Intent(context, DataRemoteViewsService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        int count =DataWidgetConfigureActivity.loadItemCount(context, appWidgetId);
        intent.putExtra(EXTRA_ITEM_COUNT, count);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        views.setRemoteAdapter(R.id.list_view, intent);

        Intent clickIntent = new Intent(context, DataWidget.class);

        clickIntent.setAction(LIST_CLICK_ACTION);
        clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.list_view, clickPendingIntent);

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
            DataWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
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


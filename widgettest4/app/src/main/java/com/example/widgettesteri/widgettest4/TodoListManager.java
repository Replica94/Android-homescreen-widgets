package com.example.widgettesteri.widgettest4;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MacodiusMaximus on 13.11.2017.
 */

public class TodoListManager {
    static private String PREFS_NAME = "TodoList_Data";
    static int getCount(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        int count = settings.getInt("count", 0);
        return count;
    }

    static List<String> getList(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        int count = settings.getInt("count", 0);
        List<String> list = new ArrayList<>();
        Log.d("TodoListManager", "Retrieving list of "+count);
        for (int i = 0; i < count; i++)
        {

            list.add(settings.getString("item_"+i,"error"));
        }
        return list;
    }

    static void setList(Context context, List<String> list)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.putInt("count", list.size());
        for (int i = 0; i < list.size(); i++)
            editor.putString("item_"+i, list.get(i));
        editor.commit();
    }
}

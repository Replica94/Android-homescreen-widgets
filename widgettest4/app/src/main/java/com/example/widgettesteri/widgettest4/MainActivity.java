package com.example.widgettesteri.widgettest4;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    class TodoItemAdapter extends ArrayAdapter<String> {
        TodoItemAdapter(Context context, List<String> list)
        {
            super(context, 0, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String string = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent,false);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.list_item_text_view);
            if (string.length() > 20)
            {
                string = string.substring(0, 17) + "...";
            }
            textView.setText(string);
            return convertView;
        }
    }

    List<String> dataSet;
    TodoItemAdapter adapter;
    private final static int VIEW_ITEM_REQUEST = 1;

    private void dataSetUpdated()
    {
        adapter.notifyDataSetChanged();

        TodoListManager.setList(this, dataSet);

        Context ctx = this;

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ctx);

        int[] ids = appWidgetManager
                .getAppWidgetIds(new ComponentName(getApplication(), TodoListWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.todo_widget_list_view);
        Log.d("MainActivity", "Idcount: "+ids.length);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VIEW_ITEM_REQUEST) {
            if (resultCode == RESULT_OK) {
                int i = data.getIntExtra(ViewItemActivity.EXTRA_INDEX, -1);
                int rest = data.getIntExtra(ViewItemActivity.EXTRA_RESULT, -1);
                if (rest == ViewItemActivity.RESULT_DELETE) {
                    if (i >= 0) {
                        dataSet.remove(i);
                        dataSetUpdated();
                    }
                }
                else if (rest == ViewItemActivity.RESULT_SAVE)
                {
                    String str = data.getStringExtra(ViewItemActivity.EXTRA_TEXT);
                    if (i >= 0 && str != null) {

                        dataSet.set(i, str);
                        dataSetUpdated();
                    }
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dataSet = TodoListManager.getList(this);
        adapter = new TodoItemAdapter(this, dataSet);
        ListView lv = (ListView) findViewById(R.id.items_list_view);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                MainActivity context = MainActivity.this;
                Intent intent = new Intent(context, ViewItemActivity.class);
                intent.putExtra(ViewItemActivity.EXTRA_INDEX, i);
                intent.putExtra(ViewItemActivity.EXTRA_TEXT, dataSet.get(i));
                context.startActivityForResult(intent, VIEW_ITEM_REQUEST);
            }
        });

        findViewById(R.id.new_item_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSet.add("Testing testing testing testing testing");
                dataSetUpdated();
            }
        });


        {
            Intent myIntent = getIntent();
            int idx = myIntent.getIntExtra(ViewItemActivity.EXTRA_INDEX, -1);
            if (idx >= 0 && idx < dataSet.size())
            {
                MainActivity context = MainActivity.this;
                Intent intent = new Intent(context, ViewItemActivity.class);
                intent.putExtra(ViewItemActivity.EXTRA_INDEX, idx);
                intent.putExtra(ViewItemActivity.EXTRA_TEXT, dataSet.get(idx));
                context.startActivityForResult(intent, VIEW_ITEM_REQUEST);
            }
        }

    }
}

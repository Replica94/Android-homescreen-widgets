package com.example.widgettesteri.widgettest4;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ViewItemActivity extends AppCompatActivity {

    public final static int RESULT_SAVE = 1;
    public final static int RESULT_DELETE = 2;
    public final static String EXTRA_RESULT = "EXTRA_RESULT";
    public final static String EXTRA_INDEX = "EXTRA_INDEX";
    public final static String EXTRA_TEXT = "EXTRA_TEXT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        String text = intent.getStringExtra(EXTRA_TEXT);
        final Integer index = intent.getIntExtra(EXTRA_INDEX, -1);

        if (text == null || index < 0)
        {
            Log.d("ViewItemActivity", "No EXTRA_TEXT found");
            finish();
            return;
        }

        setContentView(R.layout.activity_view_item);

        final TextView tv = (TextView) findViewById(R.id.view_item_text);
        tv.setText(text);

        findViewById(R.id.view_item_delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent result = new Intent();
                result.putExtra(EXTRA_RESULT, RESULT_DELETE);
                result.putExtra(EXTRA_INDEX, index);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });

        findViewById(R.id.view_item_save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent result = new Intent();
                result.putExtra(EXTRA_RESULT, RESULT_SAVE);
                result.putExtra(EXTRA_INDEX, index);
                result.putExtra(EXTRA_TEXT, tv.getText().toString());
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });

    }
}

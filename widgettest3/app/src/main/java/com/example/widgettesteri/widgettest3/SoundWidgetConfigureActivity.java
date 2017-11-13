package com.example.widgettesteri.widgettest3;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The configuration screen for the {@link SoundWidget SoundWidget} AppWidget.
 */
public class SoundWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.example.widgettesteri.widgettest3.SoundWidget";
    private static final String PREF_SOUND_KEY = "appwidget_s_";
    private static final String PREF_IMAGE_KEY = "appwidget_i_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    Uri soundUri = null;
    Uri imageUri = null;
    View.OnClickListener finishOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = SoundWidgetConfigureActivity.this;

            if (soundUri == null)
            {
                Toast.makeText(context, "Choose thy sound first!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (imageUri == null)
            {
                Toast.makeText(context, "Choose thy image first!", Toast.LENGTH_SHORT).show();
                return;
            }

            saveSoundUriPref(context, mAppWidgetId, soundUri);
            saveImageUriPref(context, mAppWidgetId, imageUri);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            SoundWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    View.OnClickListener chooseSoundOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("audio/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            SoundWidgetConfigureActivity.this.startActivityForResult(intent,REQUEST_SOUND);
        }
    };

    View.OnClickListener chooseImageOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            SoundWidgetConfigureActivity.this.startActivityForResult(intent,REQUEST_IMAGE);
        }
    };
    private final static int REQUEST_SOUND = 1;
    private final static int REQUEST_IMAGE = 2;
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        if(requestCode == REQUEST_SOUND){
            if(resultCode == RESULT_OK){
                soundUri = data.getData();
                ((TextView)findViewById(R.id.selected_sound)).setText(soundUri.toString());
            }
        }
        else
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                imageUri = data.getData();
                ((TextView)findViewById(R.id.selected_image)).setText(imageUri.toString());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public SoundWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveSoundUriPref(Context context, int appWidgetId, Uri uri) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_SOUND_KEY + appWidgetId, uri.toString());
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static Uri loadSoundUriPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String str = prefs.getString(PREF_SOUND_KEY + appWidgetId, null);
        if (str != null) {
            return Uri.parse(str);
        } else {
            return null;
        }
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveImageUriPref(Context context, int appWidgetId, Uri uri) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_IMAGE_KEY + appWidgetId, uri.toString());
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static Uri loadImageUriPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String str = prefs.getString(PREF_IMAGE_KEY + appWidgetId, null);
        if (str != null) {
            return Uri.parse(str);
        } else {
            return null;
        }
    }

    static void deletePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_SOUND_KEY + appWidgetId);
        prefs.remove(PREF_IMAGE_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.new_app_widget_configure);
        findViewById(R.id.choose_sound_button).setOnClickListener(chooseSoundOnClickListener);
        findViewById(R.id.choose_image_button).setOnClickListener(chooseImageOnClickListener);
        findViewById(R.id.add_button).setOnClickListener(finishOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }


    }
}


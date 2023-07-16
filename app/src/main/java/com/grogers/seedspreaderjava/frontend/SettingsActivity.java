package com.grogers.seedspreaderjava.frontend;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.grogers.seedspreaderjava.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Log.d(this.getClass().getSimpleName(), "*&* This is a settings activity and log for it");
        onCreateValues(savedInstanceState);
    }

    protected void onCreateValues(Bundle savedInstanceState) {
        EditText dateSettings = findViewById(R.id.asDateSettingsEditTextView);
        dateSettings.setText(Integer.toString(LanguageProcessor.getYear()));
    }

}

package com.grogers.seedspreaderjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View v = findViewById(R.id.textViewMotd);
        TextView vv = (TextView) v;
        vv.setText("This is message of the day");
        Log.d("ssmain", "This is a log file");
    }

    public void clickSettings(View view) {
        Log.d("ssmain", "clickSettings()");
        this.startActivity(new Intent(this, SettingsActivity.class));
    }
    public void clickNewtray(View view) {
        Log.d("ssmain", "clickNewtray()");
    }
}
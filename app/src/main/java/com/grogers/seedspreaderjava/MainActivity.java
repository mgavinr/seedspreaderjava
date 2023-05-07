package com.grogers.seedspreaderjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View v = this.findViewById(R.id.motd);
        android.widget.TextView vt = (TextView) v;
        vt.setText("Today is the day you die gavin");

    }
}
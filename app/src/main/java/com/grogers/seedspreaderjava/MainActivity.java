package com.grogers.seedspreaderjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
** TODO list
*   we need to come up with a naming style that makes sense
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View v = findViewById(R.id.textViewMotd);
        TextView vv = (TextView) v;
        vv.setText("This is message of the day");
        Log.d("ssmain", "This is a log file");
        LinearLayout mainll = (LinearLayout) findViewById(R.id.linearLayoutMain);

        // Create a tray
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.linearLayoutFrag, new TrayFragment(), "frag1");
        ft.commit();
        fm.executePendingTransactions();

        /*  https://stackoverflow.com/questions/17261633/multiple-fragments-in-a-vertical-linearlayout */
        ft = fm.beginTransaction();
        ft.add(R.id.linearLayoutFrag, new TrayFragment(), "frag2");
        ft.commit();
        fm.executePendingTransactions();
    }

    public void clickSettings(View view) {
        Log.d("ssmain", "clickSettings()");
        this.startActivity(new Intent(this, SettingsActivity.class));
    }
    public void clickNewtray(View view) {
        Log.d("ssmain", "clickNewtray()");
    }
}
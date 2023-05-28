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
        Log.d(this.getClass().getSimpleName(), "*&* ///////////////////////////////////////");
        Log.d(this.getClass().getSimpleName(), "*&* This is a log file");
        LinearLayout mainll = (LinearLayout) findViewById(R.id.linearLayoutMain);

        // Create a tray
        TrayFragment tray1 = TrayFragment.newInstance("Tray1", "tray1");
        TrayFragment tray2 = TrayFragment.newInstance("Tray2", "tray2");

        // add it
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.linearLayoutFrag, tray1, tray1.getClass().toString());
        ft.commit();
        fm.executePendingTransactions();

        /*  https://stackoverflow.com/questions/17261633/multiple-fragments-in-a-vertical-linearlayout */
        ft = fm.beginTransaction();
        ft.add(R.id.linearLayoutFrag, tray2, tray2.getClass().toString());
        ft.commit();
        fm.executePendingTransactions();
    }

    public void clickSettings(View view) {
        Log.d(this.getClass().getSimpleName(), "*&* clickSettings()");
        this.startActivity(new Intent(this, SettingsActivity.class));
    }
    public void clickNewTray(View view) {
        Log.d(this.getClass().getSimpleName(), "*&* clickNewTray()");
        this.startActivity(new Intent(this, EditTrayActivity.class));
    }
}
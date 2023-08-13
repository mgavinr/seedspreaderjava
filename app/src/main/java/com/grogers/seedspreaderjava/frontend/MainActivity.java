package com.grogers.seedspreaderjava.frontend;

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

import com.grogers.seedspreaderjava.R;
import com.grogers.seedspreaderjava.backend.SeedSpreader;

import java.io.LineNumberReader;
import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;


/**
 * This class displays: A heading, settings button, new tray button, and TrayFragment
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Public member variables
     */
    public IBackend backend = null;
    /**
     * Private member variables
     */
    /**
     * onCreate is called first
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(this.getClass().getSimpleName(), "*&* /////////////////////////////////////// MainActivity onCreate()");
        Log.d(this.getClass().getSimpleName(), "*&* This is a log file");
        backend = IBackend.getInstance();
        onCreateSetupValues(savedInstanceState);
    }

    protected void onCreateSetupValues(Bundle savedInstanceState) {
        LinearLayout main = (LinearLayout) findViewById(R.id.linearLayoutMain);
        LinearLayout frag = (LinearLayout) findViewById(R.id.linearLayoutFrag);
        LocalDateTime currentDateTime = LocalDateTime.now();

        /* Fill in the message of the day */
        View v = findViewById(R.id.textViewMotd);
        TextView vv = (TextView) v;
        vv.setText("" + backend.seedSpreader.trays.size() + " trays, " + backend.seedSpreader.seeds.size() + " seeds, " + backend.seedSpreader.images.size() + " images. " + currentDateTime);

        /* Fill in the fragments */
        // notes: https://stackoverflow.com/questions/17261633/multiple-fragments-in-a-vertical-linearlayout
        frag.removeAllViews();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Log.d(this.getClass().getSimpleName(), "*&* Adding " + backend.Tray.getTrays().size());
        for (String trayName : backend.Tray.getTraysSort()) {
            Log.d(this.getClass().getSimpleName(), "*&* Adding a fragment " + trayName);
            // TODO should check that getTraysSort() is the same as the .get("name") parameter
            TrayFragment trayFragment = TrayFragment.newInstance(trayName);
            ft.add(R.id.linearLayoutFrag, trayFragment, trayName);
        }
        ft.commit();
        fm.executePendingTransactions();
    }

    public void clickSettings(View view) {
        Log.d(this.getClass().getSimpleName(), "*&* clickSettings()");
        this.startActivity(new Intent(this, SettingsActivity.class));
    }
    public void clickNewTray(View view) {
        Log.d(this.getClass().getSimpleName(), "*&* clickNewTray()");
        String trayName = "New Tray " + LanguageProcessor.getDate();
        String description = "New Tray created on " + LanguageProcessor.getDate();
        Random random = new Random();
        while (backend.Tray.hasTray(trayName) == true) {
            trayName = trayName + random.nextInt(10);
        }
        backend.Tray.addTray(trayName, description);
        Intent intent = new Intent(this, EditTrayActivity.class);
        Bundle args = new Bundle();
        args.putString("name", trayName);
        intent.putExtras(args);
        this.startActivity(intent);
    }
    /*
    @Override
    public void onBackPressed() {
        Log.d(this.getClass().getSimpleName(), "*&* onBackPressed for main()");
    }
     */

    @Override
    public void onResume() {
        Log.d(this.getClass().getSimpleName(), "*&* onResume for main()");
        onCreateSetupValues(null);
        super.onResume();
    }
}
package com.grogers.seedspreaderjava.frontend;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grogers.seedspreaderjava.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * TextListener
 * - listens for changes to focus for alot of fields (we new this for each)
 * - also listen to each text change in a field, but we don't use that, i forget it
 */
class TextListener implements TextWatcher, View.OnFocusChangeListener {
    EditText view = null;
    String key = null;
    public IBackend backend = IBackend.getInstance();
    public TextListener(String k, View v) {
        this.key = k;
        this.view = (EditText) v;
    }
    public TextListener(String k) {
        this.key = k;
    }
    public TextListener(View v) {
        this.view = (EditText) v;
    }

    public void save(String value) {
        if (key == null) return;
        if (view == null) return;
        if (view.getInputType() == InputType.TYPE_CLASS_NUMBER) {
            try {
                Integer ivalue = Integer.parseInt(value);
                backend.tray.put(key, ivalue);
            } catch (Exception e) {
                Log.d(this.getClass().getSimpleName(), "*&* listener save():" + e.toString());
            }
        } else {
            backend.tray.put(key, value);   // row colls descritpion etc.
            if (key == "name") backend.Tray.updateTrayName(value);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void onFocusChange(View fview, boolean hasFocus) {
        // hmm we don't need view as it is arg too but...
        Log.d(this.getClass().getSimpleName(), "*&* " + key + " focus changed to:" + view.getText());
        if(hasFocus == false) {
            this.save(view.getText().toString());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        // This method is called after the text has been changed.
        String value = s.toString();
        Log.d(this.getClass().getSimpleName(), "*&* " + key + " watcher changed to:" + s.toString());
        this.save(value);
    }
}

/**
 * PlantRegister
 * - this is a class that popups up a dialog, and then updates the backend and frontent
 */
class PlantRegister {
    EditTrayActivity context;
    LinearLayout linearLayout = null;
    public IBackend backend = IBackend.getInstance();
    public PlantRegister(EditTrayActivity context) {
        this.context = context;
    }
    public void onClickR(View view) {
        Log.d(this.getClass().getSimpleName(), "*&* aetRegisterEvent()");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Register Event");
        LinearLayout linearLayout = newRegisterLayout();
        builder.setView(linearLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the selected items
                Log.d(this.getClass().getSimpleName(), "*&* OK event");
                String rowcols = ((EditText) linearLayout.getChildAt(1)).getText().toString();
                String date = ((EditText) linearLayout.getChildAt(3)).getText().toString();
                String eventName = ((EditText) linearLayout.getChildAt(5)).getText().toString();
                Log.d(this.getClass().getSimpleName(), "*&* OK said " + rowcols + ", " + date + ", " + eventName);
                backend.Tray.updateEvent(rowcols, date, null, eventName);
                context.onCreateSetupValues(null, false);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    /**
     * Quick way to create a dialog to allow you to register an event, it maynot be best code
     * @return
     */
    public LinearLayout newRegisterLayout() {
        // notes
        // * https://developer.android.com/develop/ui/views/components/dialogs
        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(8,16,8,8);
        linearLayout.addView(new TextView(context));
        linearLayout.addView(new EditText(context));
        linearLayout.addView(new TextView(context));
        linearLayout.addView(new EditText(context));
        linearLayout.addView(new TextView(context));
        linearLayout.addView(new AutoCompleteTextView(context));
        linearLayout.addView(new TextView(context));
        ((TextView) linearLayout.getChildAt(0)).setText("Row/Column(s)");
        ((TextView) linearLayout.getChildAt(2)).setText("Date");
        ((EditText) linearLayout.getChildAt(3)).setText(LanguageProcessor.getDate());
        ((TextView) linearLayout.getChildAt(4)).setText("Event");
        String[] suggestions = {"spare", "planted", "seedling", "death", "transplant"};
        ((TextView) linearLayout.getChildAt(6)).setText(String.join(",", suggestions));
        ((TextView) linearLayout.getChildAt(6)).setTypeface(null, Typeface.ITALIC);
        // not what you want really it filters on each letter entered
        // ((EditText) linearLayout.getChildAt(5)).setFilters(new InputFilter[]{ new WordFilter(suggestions)});
        ArrayAdapter adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, suggestions);
        ((AutoCompleteTextView) linearLayout.getChildAt(5)).setThreshold(0); // always show suggestions
        ((AutoCompleteTextView) linearLayout.getChildAt(5)).setAdapter(adapter);
        return linearLayout;
    }
}
/**
 * Seed
 * - this is a class that popups up a dialog, and then updates the backend and frontend
 */
class PlantSeed {
    EditTrayActivity context;
    boolean last = false;   // display empty dialog or the last one for when you made a spellng mistake
    public ProcessValues processValues = null;
    class ProcessValues {
        public String rowCols;
        public String date;
        public String seedName;
        public String event = "newplanted";
        boolean processed = false;
        public ProcessValues(String rc, String d, String n) {
            rowCols = rc;
            date = d;
            seedName = n;
        }

        public void process() {
            if(!processed) {
                backend.Tray.updateEvent(rowCols, date, seedName, "newplanted");
                context.onCreateSetupValues(null, false);
                processed = true;
            }
        }
    };

    public IBackend backend = IBackend.getInstance();
    public PlantSeed(EditTrayActivity context) {
        this.context = context;
    }
    void onClickS(View view, boolean last) {
        Log.d(this.getClass().getSimpleName(), "*&* aetAddSeedsEvent()");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Planting Seeds");
        LinearLayout linearLayout = newSeedLayout();
        builder.setView(linearLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(this.getClass().getSimpleName(), "*&* OK seeds");
                String rowCols = ((EditText) linearLayout.getChildAt(1)).getText().toString();
                String date = ((EditText) linearLayout.getChildAt(3)).getText().toString();
                String seedName = ((EditText) linearLayout.getChildAt(5)).getText().toString();
                Log.d(this.getClass().getSimpleName(), "*&* OK said " + rowCols + ", " + date + ", " + seedName);
                processValues = new ProcessValues(rowCols, date, seedName);
                if (backend.Seed.getSeed(seedName) == null) {
                    Log.d(this.getClass().getSimpleName(), "*&* Seed() no seed for " + seedName);
                    dialog.dismiss();
                    // TODO this is destroyed when exactly?
                    // TODO there is alot of functions chasing functions here
                    context.plantNewSeed = new PlantNewSeed(context, seedName);
                } else {
                    Log.d(this.getClass().getSimpleName(), "*&* Seed() yes we have already seed for " + seedName);
                    processValues.process();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    public LinearLayout newSeedLayout() {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(8, 16, 8, 8);
        linearLayout.addView(new TextView(context));
        linearLayout.addView(new EditText(context));
        linearLayout.addView(new TextView(context));
        linearLayout.addView(new EditText(context));
        linearLayout.addView(new TextView(context));
        linearLayout.addView(new AutoCompleteTextView(context));
        linearLayout.addView(new TextView(context));
        ((TextView) linearLayout.getChildAt(0)).setText("Row/Column(s)");
        if(last) ((EditText) linearLayout.getChildAt(1)).setText(processValues.rowCols);
        ((TextView) linearLayout.getChildAt(2)).setText("Date");
        ((EditText) linearLayout.getChildAt(3)).setText(LanguageProcessor.getDate());
        ((TextView) linearLayout.getChildAt(4)).setText("Seed Name");
        if(last) ((EditText) linearLayout.getChildAt(5)).setText(processValues.seedName);

        String[] suggestions = backend.Seed.getSeeds();
        ArrayAdapter adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, suggestions);
        ((AutoCompleteTextView) linearLayout.getChildAt(5)).setThreshold(0); // always show suggestions
        ((AutoCompleteTextView) linearLayout.getChildAt(5)).setAdapter(adapter);

        ((TextView) linearLayout.getChildAt(6)).setText(String.join(",", suggestions));
        ((TextView) linearLayout.getChildAt(6)).setTypeface(null, Typeface.ITALIC);
        return linearLayout;
    }

}

/**
 * ViewSeed
 * - this is a class that popups up a dialog, and then updates the backend and frontend
 */
class ViewSeed implements View.OnClickListener {
    EditTrayActivity context;
    public IBackend backend = IBackend.getInstance();
    public ViewSeed(EditTrayActivity context) {
        this.context = context;
    }
    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Seed");
        LinearLayout linearLayout = newSeedLayout();
        builder.setView(linearLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();
    }

    public LinearLayout newSeedLayout() {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(8, 16, 8, 8);
        linearLayout.addView(new TextView(context));
        linearLayout.addView(new EditText(context));
        linearLayout.addView(new TextView(context));
        linearLayout.addView(new EditText(context));
        linearLayout.addView(new TextView(context));
        linearLayout.addView(new AutoCompleteTextView(context));
        linearLayout.addView(new TextView(context));
        ((TextView) linearLayout.getChildAt(0)).setText("Row/Column(s)");
        ((TextView) linearLayout.getChildAt(2)).setText("Date");
        ((EditText) linearLayout.getChildAt(3)).setText(LanguageProcessor.getDate());
        ((TextView) linearLayout.getChildAt(4)).setText("Seed Name");

        String[] suggestions = backend.Seed.getSeeds();
        ArrayAdapter adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, suggestions);
        ((AutoCompleteTextView) linearLayout.getChildAt(5)).setThreshold(0); // always show suggestions
        ((AutoCompleteTextView) linearLayout.getChildAt(5)).setAdapter(adapter);

        ((TextView) linearLayout.getChildAt(6)).setText(String.join(",", suggestions));
        ((TextView) linearLayout.getChildAt(6)).setTypeface(null, Typeface.ITALIC);
        return linearLayout;
    }
}


/**
 * NewSeed
 * - this is a class that popups up a dialog, and then updates the backend and frontend
 */
class PlantNewSeed
{
    EditTrayActivity context;
    public IBackend backend = IBackend.getInstance();
    public String title = "New Seed ";
    public String seedName;
    public PlantNewSeed.ProcessValues processValues = null;

    class ProcessValues {
        public String seedName;
        public String description;
        public String year;
        boolean processed = false;

        public ProcessValues(String seedName, String description, String year) {
            this.seedName = seedName;
            this.description = description;
            this.year = year;
        }

        public void process() {
            if (!processed) {
                context.plantSeed.processValues.process();
                processed = true;
                // TODO could garbage collect
            }
        }
    };

    public PlantNewSeed(EditTrayActivity context, String seedName) {
        this.context = context;
        this.seedName = seedName;
        this.onClickNS(null); // this is not started by a click
    }

    void onClickNS(View view) {
        Log.d(this.getClass().getSimpleName(), "*&* Dialog for: " + title);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title + ": " + seedName);
        LinearLayout linearLayout = dialogLayout();
        builder.setView(linearLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (processValues == null) {
                    String description = ((EditText) linearLayout.getChildAt(3)).getText().toString();
                    String year = ((EditText) linearLayout.getChildAt(5)).getText().toString();
                    processValues = new PlantNewSeed.ProcessValues(seedName, description, year);
                }
                context.intentImage("seed");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.plantSeed.onClickS(null, true);
            }
        });
        builder.create();
        builder.show();
    }

    public LinearLayout dialogLayout() {
        String message;
        if(processValues == null)
            message = "Please take a photo of the seed packet "
                + seedName
                + " (front and back).  There are "
                + backend.Seed.getSeedImageCount(seedName)
                + " images saved already.";
        else
            message = "Please take another photo of the seed packet "
                + seedName
                + " (back).  There are "
                + backend.Seed.getSeedImageCount(seedName)
                + " images saved already.";

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(8, 16, 8, 8);
        //  0,1
        linearLayout.addView(new TextView(context));
        ((TextView) linearLayout.getChildAt(0)).setText(message);
        linearLayout.addView(new TextView(context));
        // 2,3
        linearLayout.addView(new TextView(context));
        ((TextView) linearLayout.getChildAt(2)).setText("description:");
        linearLayout.addView(new EditText(context));
        if(processValues != null) ((EditText) linearLayout.getChildAt(3)).setText(processValues.description);
        // 4,5
        linearLayout.addView(new TextView(context));
        ((TextView) linearLayout.getChildAt(4)).setText("purchase year:");
        linearLayout.addView(new EditText(context));
        if(processValues != null) ((EditText) linearLayout.getChildAt(5)).setText(processValues.year);
        return linearLayout;
    }
}


/**
 * EditTray
 * - shows the picture, the tray name, the rows and cols
 * - register button
 * - seed button
 */
public class EditTrayActivity extends AppCompatActivity
        implements View.OnLongClickListener, View.OnClickListener, ActivityResultCallback<ActivityResult>
{
    /**
     * Members : for passed in bundle
     */
    public static String ARG_TRAY_NAME = TrayFragment.ARG_TRAY_NAME;
    public static String ARG_TRAY_IMAGE_NAME = TrayFragment.ARG_TRAY_IMAGE_NAME;

    /**
     * Members : members
     */
    public IBackend backend = IBackend.getInstance();
    public PlantRegister plantRegister = null;
    public PlantSeed plantSeed = null;
    public PlantNewSeed plantNewSeed = null;
    public ViewSeed viewSeed = new ViewSeed(this);
    public String intentName = "tray";
    //
    private ActivityResultLauncher<Intent> launcher; // onLongClick
    private ImageView trayImageView; // used in local onLongClick result()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tray);
        onCreateArgs(savedInstanceState);
        onCreateSetupHandlers(savedInstanceState);
        onCreateSetupValues(savedInstanceState, true);
    }
    // mine
    protected void onCreateArgs(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String trayName = bundle.getString(ARG_TRAY_NAME);
            String trayImageName = bundle.getString(ARG_TRAY_IMAGE_NAME);
            backend.Tray.getTray(trayName);
            backend.Tray.getImage(trayImageName);
            Log.d(this.getClass().getSimpleName(), "*&* EditTrayActivity for " + trayName + " and " + trayImageName);
        } else {
            Log.d(this.getClass().getSimpleName(), "*&* we got no args EditTrayActivity");
        }
    }
    // mine
    protected void onCreateSetupHandlers(Bundle savedInstanceState) {
        // onLongClick - aetTrayImage
        // allow the user to change the tray image
        plantRegister = new PlantRegister(this);
        plantSeed = new PlantSeed(this);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this);
        trayImageView = findViewById(R.id.aetTrayImage);
        trayImageView.setOnLongClickListener(this); // onLongClick .. we only have one
        trayImageView.setImageBitmap(backend.trayImage);
        trayImageView.setOnClickListener(this); // onClick()

        // onEditTextFocusChange - multiple
        // allow the user to change the contents of all fields
        EditText editText = findViewById(R.id.aetTrayName);
        //trayName.addTextChangedListener(new TextListener("name", trayName));
        editText.setOnFocusChangeListener(new TextListener("name", editText));
        editText = findViewById(R.id.aetColsEdit); //width
        editText.setOnFocusChangeListener(new TextListener("cols", editText));
        editText = findViewById(R.id.aetRowsEdit); //width
        editText.setOnFocusChangeListener(new TextListener("rows", editText));
        // TODO add the other user custom fields with now Ids
    }
    protected void onCreateSetupValues(Bundle savedInstanceState, Boolean create) {
        List<String> doneList = Arrays.asList("name", "image", "cols", "rows");
        LinearLayout main = findViewById(R.id.aetLinearLayoutEditTray);
        LinearLayout mainc = findViewById(R.id.aetContentsViewLL);

        // Change tray name
        EditText trayName = findViewById(R.id.aetTrayName);
        trayName.setText(backend.trayName);

        // Change tray contents
        EditText contents = findViewById(R.id.aetContentsEditTextML);
        String contentsText = LanguageProcessor.getContents(backend);
        contents.setText(contentsText);

        // Change tray cols
        EditText trayCols = findViewById(R.id.aetColsEdit); //width
        EditText trayRows = findViewById(R.id.aetRowsEdit); //height
        Integer cols = (Integer)backend.tray.get("cols");
        Integer rows = (Integer)backend.tray.get("rows");
        trayCols.setText(cols.toString());
        trayRows.setText(rows.toString());

        // Create: Seed list
        HashMap<String, String> seedList = LanguageProcessor.getContentsPerSeed(backend);
        mainc.removeAllViews();
        for(String key: seedList.keySet()) {
            TextView text = new TextView(this);
            Button button = new Button(this);
            button.setText(key);
            button.setOnClickListener(this.viewSeed);
            text.setText(seedList.get(key));
            mainc.addView(button);
            mainc.addView(text);
        }

        // Create: Seed list
        if(create) {
            for (String key : backend.tray.keySet()) {
                if (doneList.contains(key)) {
                    Log.d(this.getClass().getSimpleName(), "*&* no need to add ui for " + key);
                } else {
                    if (!key.contains("content_")) {
                        Log.d(this.getClass().getSimpleName(), "*&* programmatically adding views for " + key);
                        TextView text = new TextView(this);
                        text.setText(key);
                        EditText editText = new EditText(this);
                        main.addView(text);
                        main.addView(editText);
                    }
                }
            }
        }

        // Create: User defined key values
        if(create) {
            for (String key : backend.tray.keySet()) {
                if (doneList.contains(key)) {
                    Log.d(this.getClass().getSimpleName(), "*&* no need to add ui for " + key);
                } else {
                    if (!key.contains("content_")) {
                        Log.d(this.getClass().getSimpleName(), "*&* programmatically adding views for " + key);
                        TextView text = new TextView(this);
                        text.setText(key);
                        EditText editText = new EditText(this);
                        main.addView(text);
                        main.addView(editText);
                    }
                }
            }
        }
        // TODO Create: Button to add User defined key values
    }

    @Override
    public boolean onLongClick(View v) {
        Log.d(this.getClass().getSimpleName(), "*&* Yay! on long click");
        intentImage("tray");
        return true;
    }

    public void intentImage(String name) {
        this.intentName = name;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d(this.getClass().getSimpleName(), "*&* fui:"+ intent.hashCode());
        launcher.launch(intent);
    }

    @Override
    public void onActivityResult(ActivityResult result) {
        Log.d(this.getClass().getSimpleName(), "*&* Yay! on long click result");
        if (result.getResultCode() == Activity.RESULT_OK) {
            // The image capture was successful. You can access the captured image here.
            Intent data = result.getData();
            Log.d(this.getClass().getSimpleName(), "*&* fuo:"+ data.hashCode());
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (intentName == "tray") {
                Log.d(this.getClass().getSimpleName(), "*&* Yay! image for tray " + intentName);
                trayImageView.setImageBitmap(backend.Tray.saveTrayImage(imageBitmap));
            } else {
                Log.d(this.getClass().getSimpleName(), "*&* Yay! image for " + intentName);
                backend.Seed.addSeed(this.plantSeed.processValues.seedName, plantNewSeed.processValues.description, plantNewSeed.processValues.year);
                backend.Seed.saveSeedImage(this.plantSeed.processValues.seedName, imageBitmap);
                if(backend.Seed.getSeedImageCount(this.plantSeed.processValues.seedName) < 2) {
                    plantNewSeed.onClickNS(null);
                } else {
                    // garbage collection
                    plantNewSeed = null;
                }
                plantSeed.processValues.process();
            }
        }
    }

    /**
     * Called when user clicks add seeds
     * @param view
     */
    public void aetAddSeedsEvent(View view) {
        plantSeed.onClickS(view, false);
    }

    /**
     * Called when user clicks register event button
     * @param view
     */
    public void aetRegisterEvent(View view) {
        plantRegister.onClickR(view);
    }

    @Override
    public void onBackPressed() {
        backend.seedSpreader.update(null);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        // TODO add a red box, to mean modified not saved, green to mean saved
        // TODO add a back arrow to the left, that is the background property of image view
        backend.seedSpreader.update(null);
        finish();
    }
}
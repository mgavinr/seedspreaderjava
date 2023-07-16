package com.grogers.seedspreaderjava.frontend;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.grogers.seedspreaderjava.R;
import com.grogers.seedspreaderjava.frontend.LanguageProcessor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


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
            if (key == "name") backend.updateTrayName(value);
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
 * Register
 * - this is a class that popups up a dialog, and then updates the backend and frontent
 */
class Register {
    EditTrayActivity context;
    LinearLayout linearLayout = null;
    public IBackend backend = IBackend.getInstance();
    public Register(EditTrayActivity context) {
        this.context = context;
    }
    public void onClick(View view) {
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
                backend.updateEvent(rowcols, date, null, eventName);
                context.onCreateSetupValues(null, false);
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
 * - this is a class that popups up a dialog, and then updates the backend and frontent
 */
class Seed {
    EditTrayActivity context;
    public IBackend backend = IBackend.getInstance();
    public Seed(EditTrayActivity context) {
        this.context = context;
    }
    void onClick(View view) {
        Log.d(this.getClass().getSimpleName(), "*&* aetAddSeedsEvent()");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Planting Seeds");
        LinearLayout linearLayout = newSeedLayout();
        builder.setView(linearLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(this.getClass().getSimpleName(), "*&* OK seeds");
                String rowcols = ((EditText) linearLayout.getChildAt(1)).getText().toString();
                String date = ((EditText) linearLayout.getChildAt(3)).getText().toString();
                String seedName = ((EditText) linearLayout.getChildAt(5)).getText().toString();
                Log.d(this.getClass().getSimpleName(), "*&* OK said " + rowcols + ", " + date + ", " + seedName);
                if (backend.getSeed(seedName) == null) {
                    Log.d(this.getClass().getSimpleName(), "*&* Seed() no seed for " + seedName);
                } else {
                    Log.d(this.getClass().getSimpleName(), "*&* Seed() yes we have already seed for " + seedName);
                    backend.updateEvent(rowcols, date, seedName, "newplanted");
                    context.onCreateSetupValues(null, false);
                }
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

        String[] suggestions = backend.getSeeds();
        ArrayAdapter adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, suggestions);
        ((AutoCompleteTextView) linearLayout.getChildAt(5)).setThreshold(0); // always show suggestions
        ((AutoCompleteTextView) linearLayout.getChildAt(5)).setAdapter(adapter);

        ((TextView) linearLayout.getChildAt(6)).setText(String.join(",", suggestions));
        ((TextView) linearLayout.getChildAt(6)).setTypeface(null, Typeface.ITALIC);
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
    public Register register = null;
    public Seed seed = null;
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
            backend.getTray(trayName);
            backend.getImage(trayImageName);
            Log.d(this.getClass().getSimpleName(), "*&* EditTrayActivity for " + trayName + " and " + trayImageName);
        } else {
            Log.d(this.getClass().getSimpleName(), "*&* we got no args EditTrayActivity");
        }
    }
    // mine
    protected void onCreateSetupHandlers(Bundle savedInstanceState) {
        // onLongClick - aetTrayImage
        // allow the user to change the tray image
        register = new Register(this);
        seed = new Seed(this);
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
        HashMap<String, String> seedList = new HashMap<>();
        List<String> keys = new ArrayList<String>(backend.tray.keySet());
        Collections.sort(keys);
        int row = 0;
        for(String key : keys) {
            if (key.contains("content_")) {
                ++row;
                ArrayList<?> rowContent = (ArrayList<?>) backend.tray.get(key);
                if (rowContent != null) {
                    int col = 0;
                    for (Object colContent : rowContent) {
                        ++col;
                        Map<String, Object> colmap = (Map<String, Object>) colContent;
                        String seedName = (String)colmap.get("name");
                        String event = (String)colmap.get("event");
                        String date = (String)colmap.get("date");
                        String seedInfo = "["+ row +","+col+"] "+ event + " on " + date + ".\n";
                        if (seedList.containsKey(seedName)) {
                            String existingSeedInfo = seedList.get(seedName);
                            seedList.put(seedName, existingSeedInfo+seedInfo);
                        } else {
                            seedList.put(seedName, seedInfo);
                        }
                    }
                }
            }
        }
        for(String key: seedList.keySet()) {
            TextView text = new TextView(this);
            Button button = new Button(this);
            button.setText(key);
            text.setText(seedList.get(key));
            main.addView(button);
            main.addView(text);
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        launcher.launch(intent);
        return true;
    }

    @Override
    public void onActivityResult(ActivityResult result) {
        Log.d(this.getClass().getSimpleName(), "*&* Yay! on long click result");
        if (result.getResultCode() == Activity.RESULT_OK) {
            // The image capture was successful. You can access the captured image here.
            Intent data = result.getData();
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // TODO Do something with the imageBitmap, such as displaying it in an ImageView
            trayImageView.setImageBitmap(imageBitmap);
        }
    }

    /**
     * Called when user clicks add seeds
     * @param view
     */
    public void aetAddSeedsEvent(View view) {
        seed.onClick(view);
    }

    /**
     * Called when user clicks register event button
     * @param view
     */
    public void aetRegisterEvent(View view) {
        register.onClick(view);
    }

    @Override
    public void onBackPressed() {
        backend.seedSpreader.update();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        // TODO add a red box, to mean modified not saved, green to mean saved
        // TODO add a back arrow to the left, that is the background property of image view
        backend.seedSpreader.update();
        finish();
    }
}
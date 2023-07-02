package com.grogers.seedspreaderjava.frontend;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.grogers.seedspreaderjava.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

class Register {
    Context context;
    public Register(Context context) {
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
                Log.d(this.getClass().getSimpleName(), "*&* onclick for the dialog register");
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
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(8,16,8,8);
        linearLayout.addView(new TextView(context));
        linearLayout.addView(new EditText(context));
        linearLayout.addView(new TextView(context));
        linearLayout.addView(new EditText(context));
        linearLayout.addView(new TextView(context));
        linearLayout.addView(new EditText(context));
        ((TextView) linearLayout.getChildAt(0)).setText("Row/Column(s)");
        ((TextView) linearLayout.getChildAt(2)).setText("Date");
        ((TextView) linearLayout.getChildAt(4)).setText("Event");
        return linearLayout;
    }
}
class Seed {
    Context context;
    public Seed(Context context) {
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
                // Get the selected items
                Log.d(this.getClass().getSimpleName(), "*&* onclick for the dialog seeds");
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
        linearLayout.addView(new EditText(context));
        ((TextView) linearLayout.getChildAt(0)).setText("Row/Column(s)");
        ((TextView) linearLayout.getChildAt(2)).setText("Date");
        ((TextView) linearLayout.getChildAt(4)).setText("Seed Name");
        return linearLayout;
    }

    public LinearLayout newLinearLayout() {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(8, 16, 8, 8);
        linearLayout.addView(new TextView(context));
        linearLayout.addView(new EditText(context));
        linearLayout.addView(new TextView(context));
        linearLayout.addView(new EditText(context));
        linearLayout.addView(new TextView(context));
        linearLayout.addView(new EditText(context));
        ((TextView) linearLayout.getChildAt(0)).setText("Row/Column(s)");
        ((TextView) linearLayout.getChildAt(2)).setText("Date");
        ((TextView) linearLayout.getChildAt(4)).setText("Event");
        return linearLayout;
    }

    // not used old code
    public AlertDialog.Builder newLinearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Checkbox Dialog");
        LinearLayout linearLayout = newLinearLayout();
        builder.setView(linearLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the selected items
                Log.d(this.getClass().getSimpleName(), "*&* onclick for the dialog");
            }
        });
        return builder;
    }

    // not used old code
    public ListView newListView() {
        ListView listView = new ListView(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_multiple_choice);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // Add items to the adapter
        adapter.add("Item 1");
        adapter.add("Item 2");
        adapter.add("Item 3");
        return listView;
    }

    // not used old code
    public AlertDialog.Builder newListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Register Event");
        ListView listView = newListView();
        builder.setView(listView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the selected items
                SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                for (int i = 0; i < checkedItems.size(); i++) {
                    int position = checkedItems.keyAt(i);
                    if (checkedItems.valueAt(i)) {
                        String selected = (String) listView.getAdapter().getItem(position);
                        Log.d(this.getClass().getSimpleName(), "*&* dialog selected " + selected);
                        // Perform actions with the selected items
                        // ...
                    }
                }
            }
        });
        return builder;
    }
}

/**
 * EditTray shows the picture, the tray name, the width, height, seeds button,
 * register button, and a summary which the idea maybe to use emoji seeds
 * (We need a query too, or a text summary)
 */
public class EditTrayActivity extends AppCompatActivity
        implements View.OnLongClickListener, ActivityResultCallback<ActivityResult>
{
    /**
     * EditTray Members
     */
    public static String ARG_TRAY_NAME = TrayFragment.ARG_TRAY_NAME;
    public static String ARG_TRAY_IMAGE_NAME = TrayFragment.ARG_TRAY_IMAGE_NAME;
    public Register register = null;
    public Seed seed = null;
    private ActivityResultLauncher<Intent> launcher; // onLongClick
    private ImageView tray; // onLongClick result
    public IBackend backend = IBackend.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_edit_tray);
        onCreateArgs(savedInstanceState);
        onCreateSetupHandlers(savedInstanceState);
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
        List<String> doneList = Arrays.asList("name", "image", "cols", "rows", "contents");
        LinearLayout main = findViewById(R.id.aetLinearLayoutEditTray);

        // Change image pic
        // Long click for the image
        register = new Register(this);
        seed = new Seed(this);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this);
        tray = findViewById(R.id.aetTrayImage);
        tray.setOnLongClickListener(this);
        tray.setImageBitmap(backend.trayImage);

        // Change tray name
        EditText trayName = findViewById(R.id.aetTrayName);
        trayName.setText(backend.trayName);
        trayName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                // This method is called after the text has been changed.
                Log.d(this.getClass().getSimpleName(), "*&* Tray Name Changed to:"+ s.toString());
            }
        });


        // Change tray contents
        EditText contents = findViewById(R.id.aetContentsEditTextML);
        String contentsText = convertContents(contents);
        contents.setText(contentsText);

        // Change tray cols
        EditText trayCols = findViewById(R.id.aetColsEdit); //width
        EditText trayRows = findViewById(R.id.aetRowsEdit); //height
        Integer cols = (Integer)backend.tray.get("cols");
        Integer rows = (Integer)backend.tray.get("rows");
        trayCols.setText(cols.toString());
        trayRows.setText(rows.toString());

        for(String key : backend.tray.keySet()) {
            if (doneList.contains(key)) {
                Log.d(this.getClass().getSimpleName(), "*&* no need to add ui for " + key);
            } else {
                Log.d(this.getClass().getSimpleName(), "*&* programmatically adding views for " + key);
                TextView text = new TextView(this);
                text.setText(key);
                EditText editText = new EditText(this);
                main.addView(text);
                main.addView(editText);
            }
        }
    }

    String convertContents(View v) {
        Map<String, Object> eventE = Map.ofEntries(
            Map.entry("ddeath", "\u2620"),
            Map.entry("sseedling", "\uD83C\uDF31"),
            Map.entry("death", "💀"),
            Map.entry("corn", "🌽"),
            Map.entry("chili", "🌶"),
            Map.entry("pineapple", "🍍"),
            Map.entry("strawberry", "🍓"),
            Map.entry("carrot", "🥕"),
            Map.entry("planted2", "🌰"),
            Map.entry("seedling", "🌱"),
            Map.entry("top", "🔝"),
            Map.entry("transplant", "🏘"),
            Map.entry("bone", "🦴"),
            Map.entry("seedling2", "🌾"),
            Map.entry("cherry", "🍒"),
            Map.entry("planted", "🥔"),
            Map.entry("nut", "🥜"),
            Map.entry("broccoli", "🥦"),
            Map.entry("cucumber", "🥬"),
            Map.entry("eggplant", "🍆"),
            Map.entry("avocado", "🥑"),
            Map.entry("coconut", "🥥"),
            Map.entry("tomato", "🍅"),
            Map.entry("kiwi", "🥝"),
            Map.entry("pear2", "🥭"),
            Map.entry("redApple", "🍎"),
            Map.entry("greenApple", "🍏"),
            Map.entry("pear", "🍐"),
            Map.entry("mandarin", "🍑"),
            Map.entry("lemon", "🍋"),
            Map.entry("orange", "🍊"),
            Map.entry("melon", "🍉"),
            Map.entry("tennis", "🍈"),
            Map.entry("grape", "🍇"),
            Map.entry("banana", "🍌"),
            Map.entry("blank", "🌑"),
            Map.entry("spare", "🌑"),
            Map.entry("spare2", "x")
        );
        String multiLineE= "";
        Integer cols = (Integer)backend.tray.get("cols");
        Integer rows = (Integer)backend.tray.get("rows");

        int max = cols * rows;
        int row = 0;
        int col = 0;
        Object contents = backend.tray.get("contents");
        ArrayList<?> trayContents = (ArrayList<?>) contents;
        for (Object potContent : trayContents) {
            col++;
            ArrayList<?> potContentList = (ArrayList<?>) potContent;
            Map<String, Object> potEvent = null;
            for (Object content: potContentList) {
                potEvent = (Map<String, Object>) content;
                Log.d(this.getClass().getSimpleName(), "*&* row="+row+" col="+ col + " pot="+ potEvent.toString());
            }
            multiLineE = multiLineE + eventE.get(potEvent.get("event"));
        }
        while(col < max) {
            multiLineE = multiLineE + eventE.get("spare");
            col++;
        }
        Log.d(this.getClass().getSimpleName(), "*&* " + multiLineE);
        return multiLineE;
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
            // Do something with the imageBitmap, such as displaying it in an ImageView
            tray.setImageBitmap(imageBitmap);
            // TODO save to backend
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
}
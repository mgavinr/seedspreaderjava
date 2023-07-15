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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.grogers.seedspreaderjava.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class LanguageProcessor {
    static Map<String, String> iconList = Map.ofEntries(
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

    static public String getDate() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.toString();
    }

    static ArrayList<Integer> getRowCol(String rowcol, int maxRow, int maxCol) {
        // This tries to be a natural language interpreter for row col coords
        // it accepts rX as the row, and then cols follow, rX on it's own
        // means nothing, it's all about the cols, but you can say all, or * for all colls
        // All english starts counting at 1 .. r1 is row 0 ..  col 1to3 is col 0,1,2
        ArrayList<Integer> result = new ArrayList<Integer>();
        rowcol = rowcol.replace(" to", "to");
        rowcol = rowcol.replace("to ", "to");
        int currentRow = 0;
        for (String part : rowcol.split(" ")) {
            Log.d(LanguageProcessor.class.getSimpleName(), "*&* Language=[" + part + "] for row " + currentRow);
            try {
                if (part.contains("r")) {
                    // parse a row number
                    Log.d(LanguageProcessor.class.getSimpleName(), "*&* parserow=>" + part);
                    String row = part.replace("r", "");
                    currentRow = Integer.parseInt(row);
                    currentRow -= 1;
                    if (currentRow < 0) {
                        currentRow = 0;
                    }
                    Log.d(LanguageProcessor.class.getSimpleName(), "*&* parserow<=" + part);
                } else {
                    Log.d(LanguageProcessor.class.getSimpleName(), "*&* parsecol=>" + part);
                    if ((part.equals("*")) || (part.equals("all"))) {
                        for (int i = 0; i < maxCol; ++i) {
                            result.add((currentRow*maxCol) + i);
                        }
                    } else if (part.contains("to")) {
                        String[] twoParts = part.split("to");
                        Integer start = Integer.parseInt(twoParts[0].trim()); --start;
                        Integer end = Integer.parseInt(twoParts[1].trim()); --end;
                        for (int i = start; i <= end; ++i) {
                            result.add((currentRow*maxCol) + i);
                        }
                    } else {
                        for (String col : part.split(" ")) {
                            result.add((currentRow*maxCol) + Integer.parseInt(col) - 1);
                        }
                    }
                    Log.d(LanguageProcessor.class.getSimpleName(), "*&* parsecol<=" + part);
                }
            } catch (Exception e) {
                Log.d(LanguageProcessor.class.getSimpleName(), "*&* parse error " + e.toString());
            }
        }
        Collections.sort(result);
        Log.d(LanguageProcessor.class.getSimpleName(), "*&* The rowCol interpreter has taken input [" + rowcol + "] to mean [" + result.toString() + "]");
        return result;
    }

    static String getContents(IBackend backend) {
        String multiLineE= "";
        boolean start = true;

        String icon = null;
        String iconSpare = iconList.get("spare");
        List<String> keys = new ArrayList<String>(backend.tray.keySet());
        Collections.sort(keys);
        for(String key : keys) {
            if (key.contains("contents")) {
                if(!start) multiLineE = multiLineE + "\n";
                start = false;
                ArrayList<?> rowContent = (ArrayList<?>) backend.tray.get(key);
                if (rowContent != null) {
                    for (Object colContent : rowContent) {
                        Map<String, Object> col = (Map<String, Object>) colContent;
                        icon = iconList.get(col.get("event"));
                        if (icon == null) {
                            icon = iconSpare;
                        }
                        multiLineE = multiLineE + icon;
                    }
                }
            }
        }
        Log.d(LanguageProcessor.class.getSimpleName(), "*&* " + multiLineE);
        return multiLineE;
    }

}

class WordFilter implements InputFilter {
    private ArrayList<String> allowedWords;

    public WordFilter(String[] allowedWords) {
        this.allowedWords = new ArrayList<>(Arrays.asList(allowedWords));
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        StringBuilder filtered = new StringBuilder();
        for (int i = start; i < end; i++) {
            char character = source.charAt(i);
            filtered.append(character);
        }

        String newWord = dest.subSequence(0, dstart) + filtered.toString() + dest.subSequence(dend, dest.length());
        if (allowedWords.contains(newWord)) {
            Log.d(this.getClass().getSimpleName(), "*&* The word entered is okay: "+ newWord);
            return null;
        }
        Log.d(this.getClass().getSimpleName(), "*&* The word entered is not okay: " + newWord);
        return "";
    }
}

// TODO if you change the coll size, you need to redo the events?
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
    }

    @Override
    public void afterTextChanged(Editable s) {
        // This method is called after the text has been changed.
        String value = s.toString();
        Log.d(this.getClass().getSimpleName(), "*&* " + key + " watcher changed to:" + s.toString());
        if (key == null) return;
        if (view == null) return;
        if (view.getInputType() == InputType.TYPE_CLASS_NUMBER) {
            try {
                Integer ivalue = Integer.parseInt(value);
                backend.tray.put(key, ivalue);
            } catch (Exception e) {
                Log.d(this.getClass().getSimpleName(), "*&* a:" + e.toString());
            }
        } else {
            backend.tray.put(key, value);
        }
    }
}

class Register {
    Context context;
    LinearLayout linearLayout = null;
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
                Log.d(this.getClass().getSimpleName(), "*&* OK event");
                String rowcols = ((EditText) linearLayout.getChildAt(1)).getText().toString();

                String date = ((EditText) linearLayout.getChildAt(3)).getText().toString();
                String event = ((EditText) linearLayout.getChildAt(5)).getText().toString();
                Log.d(this.getClass().getSimpleName(), "*&* OK said " + rowcols + ", " + date + ", " + event);
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
                    LanguageProcessor.getRowCol(rowcols, context.rows, context.cols);

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

    /**
     * Computed EditTray Members
     */
    public Integer cols = 0;
    public Integer rows = 0;

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
        List<String> doneList = Arrays.asList("name", "image", "cols", "rows");
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
        //trayName.addTextChangedListener(new TextListener("name", trayName));
        trayName.setOnFocusChangeListener(new TextListener("name", trayName));

        // Change tray contents
        EditText contents = findViewById(R.id.aetContentsEditTextML);
        String contentsText = LanguageProcessor.getContents(backend);
        contents.setText(contentsText);

        // Change tray cols
        EditText trayCols = findViewById(R.id.aetColsEdit); //width
        EditText trayRows = findViewById(R.id.aetRowsEdit); //height
        cols = (Integer)backend.tray.get("cols");
        rows = (Integer)backend.tray.get("rows");
        trayCols.setText(cols.toString());
        trayRows.setText(rows.toString());

        for(String key : backend.tray.keySet()) {
            if (doneList.contains(key)) {
                Log.d(this.getClass().getSimpleName(), "*&* no need to add ui for " + key);
            } else {
                if (!key.contains("contents")) {
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
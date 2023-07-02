package com.grogers.seedspreaderjava.frontend;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.grogers.seedspreaderjava.R;

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
        // Long click for the image
        register = new Register(this);
        seed = new Seed(this);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this);
        tray = findViewById(R.id.aetTrayImage);
        tray.setOnLongClickListener(this);
        tray.setImageBitmap(backend.trayImage);

        // Tray name
        EditText trayName = findViewById(R.id.aetTrayName);
        trayName.setText(backend.trayName);

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
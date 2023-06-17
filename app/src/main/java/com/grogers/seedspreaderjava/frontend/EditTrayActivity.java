package com.grogers.seedspreaderjava.frontend;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
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

public class EditTrayActivity extends AppCompatActivity
        implements View.OnLongClickListener, ActivityResultCallback<ActivityResult>
{

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ActivityResultLauncher<Intent> launcher;
    private ImageView tray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tray);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this);

        tray = findViewById(R.id.aetTrayImage);
        tray.setOnLongClickListener(this);
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
        }
    }

    public void aetAddSeedsEvent(View view) {
        Log.d(this.getClass().getSimpleName(), "*&* aetAddSeedsEvent()");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    public void aetRegisterEvent(View view) {
        Log.d(this.getClass().getSimpleName(), "*&* aetRegisterEvent()");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    /*
     ** extra functions, ideally you would just follow this instead:
     * https://developer.android.com/develop/ui/views/components/dialogs
     ** but these could be useful sometime.
     */
    public LinearLayout newRegisterLayout() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(8,16,8,8);
        linearLayout.addView(new TextView(this));
        linearLayout.addView(new EditText(this));
        linearLayout.addView(new TextView(this));
        linearLayout.addView(new EditText(this));
        linearLayout.addView(new TextView(this));
        linearLayout.addView(new EditText(this));
        ((TextView) linearLayout.getChildAt(0)).setText("Row/Column(s)");
        ((TextView) linearLayout.getChildAt(2)).setText("Date");
        ((TextView) linearLayout.getChildAt(4)).setText("Event");
        return linearLayout;
    }

    public LinearLayout newSeedLayout() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(8,16,8,8);
        linearLayout.addView(new TextView(this));
        linearLayout.addView(new EditText(this));
        linearLayout.addView(new TextView(this));
        linearLayout.addView(new EditText(this));
        linearLayout.addView(new TextView(this));
        linearLayout.addView(new EditText(this));
        ((TextView) linearLayout.getChildAt(0)).setText("Row/Column(s)");
        ((TextView) linearLayout.getChildAt(2)).setText("Date");
        ((TextView) linearLayout.getChildAt(4)).setText("Seed Name");
        return linearLayout;
    }
    public LinearLayout newLinearLayout() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(8,16,8,8);
        linearLayout.addView(new TextView(this));
        linearLayout.addView(new EditText(this));
        linearLayout.addView(new TextView(this));
        linearLayout.addView(new EditText(this));
        linearLayout.addView(new TextView(this));
        linearLayout.addView(new EditText(this));
        ((TextView) linearLayout.getChildAt(0)).setText("Row/Column(s)");
        ((TextView) linearLayout.getChildAt(2)).setText("Date");
        ((TextView) linearLayout.getChildAt(4)).setText("Event");
        return linearLayout;
    }

    public ListView newListView() {
        ListView listView = new ListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // Add items to the adapter
        adapter.add("Item 1");
        adapter.add("Item 2");
        adapter.add("Item 3");
        return listView;
    }

    public AlertDialog.Builder newListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    public AlertDialog.Builder newLinearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
}
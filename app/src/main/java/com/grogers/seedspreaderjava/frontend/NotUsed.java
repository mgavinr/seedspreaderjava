package com.grogers.seedspreaderjava.frontend;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Arrays;

class NUWordFilter implements InputFilter {
    private ArrayList<String> allowedWords;

    public NUWordFilter(String[] allowedWords) {
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

public class NotUsed {
    Context context = null;
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

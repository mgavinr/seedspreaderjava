package com.grogers.seedspreaderjava;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NAME = "name";
    private static final String ARG_PICTURE = "picture";

    // TODO: Rename and change types of parameters
    private String name;
    private String picture;

    public TrayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name  - the name of the tray
     * @param picture - the picture name of the tray
     * @return A new instance of fragment TrayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrayFragment newInstance(String name, String picture) {
        TrayFragment fragment = new TrayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_PICTURE, picture);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            picture = getArguments().getString(ARG_PICTURE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tray, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText name = (EditText) view.findViewById(R.id.id_frag_tray_name);
        name.setText(this.name);
        int resourceId = getContext().getResources().getIdentifier(picture, "drawable", SeedApplication.getContext().getPackageName());
        ImageView image = (ImageView) view.findViewById(R.id.id_frag_tray_image);
        image.setImageResource(R.drawable.tray1);
        Log.d(this.getClass().getSimpleName(), "*&* The id for drawing " + name + " is " + resourceId);
        image.setImageResource(resourceId);
    }
}
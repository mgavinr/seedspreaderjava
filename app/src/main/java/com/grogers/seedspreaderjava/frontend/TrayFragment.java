package com.grogers.seedspreaderjava.frontend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.grogers.seedspreaderjava.R;
import com.grogers.seedspreaderjava.backend.IFrontend;

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

    public IBackend backend = IBackend.getInstance();

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
        View view = inflater.inflate(R.layout.fragment_tray, container, false);
        Button btTrayName = (Button)view.findViewById(R.id.ftTrayNameButton);
        btTrayName.setOnClickListener(this::ftTrayNameEventClick);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        Button name = (Button) view.findViewById(R.id.ftTrayNameButton);
        name.setText(this.name);
        //
        Bitmap bitmap = backend.getImage(this.picture);
        if (bitmap != null) {
            ImageView image = (ImageView) view.findViewById(R.id.ftPicture);
            image.setImageBitmap(bitmap);
        } else {
            Log.d(this.getClass().getSimpleName(), "*&* fragment has no image: " + this.picture);
        }
    }

    public void ftTrayNameEventClick(View view) {
        Log.d(this.getClass().getSimpleName(), "*&* clickSettings()");
        this.startActivity(new Intent(getActivity(), EditTrayActivity.class));
    }

}
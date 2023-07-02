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
    public static final String ARG_TRAY_NAME = "name";
    public static final String ARG_TRAY_IMAGE_NAME = "image";

    // TODO: Rename and change types of parameters
    private String trayName;
    private String trayImageName;

    public IBackend backend = IBackend.getInstance();

    public TrayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param trayName  - the name of the tray
     * @param trayImageName - the image name of the tray
     * @return A new instance of fragment TrayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrayFragment newInstance(String trayName, String trayImageName) {
        TrayFragment fragment = new TrayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRAY_NAME, trayName);
        args.putString(ARG_TRAY_IMAGE_NAME, trayImageName);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called to initialise variables, non ui setup
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trayName = getArguments().getString(ARG_TRAY_NAME);
            trayImageName = getArguments().getString(ARG_TRAY_IMAGE_NAME);
        }
    }

    /**
     * Called to inflate the ui, the root iew is initialised here
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tray, container, false);
        return view;
    }

    /**
     * After the above, you can do user customisation here, add listeners and so on
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        Button btTrayName = (Button)view.findViewById(R.id.ftTrayNameButton);
        btTrayName.setOnClickListener(this::ftTrayNameEventClick);
        //
        Button name = (Button) view.findViewById(R.id.ftTrayNameButton);
        name.setText(this.trayName);
        //
        Bitmap bitmap = backend.getImage(this.trayImageName);
        if (bitmap != null) {
            ImageView image = (ImageView) view.findViewById(R.id.ftPicture);
            image.setImageBitmap(bitmap);
        } else {
            Log.d(this.getClass().getSimpleName(), "*&* fragment has no image: " + this.trayImageName);
        }
    }

    public void ftTrayNameEventClick(View view) {
        Log.d(this.getClass().getSimpleName(), "*&* clickSettings()");
        Intent intent = new Intent(getActivity(), EditTrayActivity.class);
        if (getArguments() != null) intent.putExtras(getArguments());
        this.startActivity(intent);
    }

}
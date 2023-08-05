package com.grogers.seedspreaderjava.backend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.grogers.seedspreaderjava.R;
import com.grogers.seedspreaderjava.frontend.SeedApplication;

import java.io.File;

/**
 * Put all android specific stuff here, that is usually frontent related but not necessary
 */
public class IFrontend {
    public static IFrontend instance = null;

    public static IFrontend getInstance() {
        if (instance == null) {
            instance = new IFrontend();
        }
        return instance;
    }

    private IFrontend() {
    }

    public Context context = SeedApplication.getContext();

    /* Files */
    ///* /data/user/0/com.grogers.seedspreaderjava/seed.yaml */
    public File filesPrivateParent = context.getDataDir();
    ///* /data/user/0/com.grogers.seedspreaderjava/files/seed.yaml */
    public File filesPrivate = context.getFilesDir();
    // public
    ///*  /storage/emulated/0/Android/data/com.grogers.seedspreaderjava/files/Documents/seed.yaml */
    public File filesPublic = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
    public File imageFilesPublic = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

    /* Cache */
    ///* /data/user/0/com.grogers.seedspreaderjava/cache/seed.yaml */
    public File cachePrivate = context.getCacheDir();
    // public
    // /* /storage/emulated/0/Android/data/com.grogers.seedspreaderjava/cache/seed.yaml */
    public File cachePublic = context.getExternalCacheDir();

    public Bitmap getBitmap(String resource) {
        if (resource == "sample_chili")
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.sample_chili);
        else if (resource == "new_image")       // TODO, I know you are a programmer, but
            // make sure you video this app for the records,
            // the look and feel needs to be kept, for the record.
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.new_image);
        else if (resource == "sample_chili_back")
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.sample_chili_back);
        else if (resource == "sample_tomato")
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.sample_tomato);
        else if (resource == "sample_tomato_back")
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.sample_tomato_back);
        else if (resource == "tray1")
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.tray1);
        else if (resource == "tray2")
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.tray2);
        return null;
    }
}
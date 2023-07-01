package com.grogers.seedspreaderjava.frontend;

import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.util.Log;

import com.grogers.seedspreaderjava.backend.SeedSpreader;

import java.util.Hashtable;
import java.util.Map;

public class IBackend {
    public static IBackend instance = null;

    public static IBackend getInstance() {
        if (instance == null) {
            instance = new IBackend();
        }
        return instance;
    }

    private IBackend() {
    }

    SeedSpreader seedSpreader = SeedSpreader.getInstance();

    public java.util.Set<String> getTrays() {
        return seedSpreader.trays.keySet();
    }

    public Bitmap getImage(String imageName) {
        Bitmap rv = seedSpreader.images.get(imageName);
        if (rv == null) {
            Log.d(this.getClass().getSimpleName(), "*&* Not--found: " + imageName);
            Log.d(this.getClass().getSimpleName(), "*&* Images are: " + seedSpreader.images.keySet().toString());
        }
        return rv;
    }


    Map<String, Object> getTray(String trayName) {
        return seedSpreader.trays.get(trayName);
    }
}
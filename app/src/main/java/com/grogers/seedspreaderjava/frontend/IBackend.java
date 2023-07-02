package com.grogers.seedspreaderjava.frontend;

import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.util.Log;

import com.grogers.seedspreaderjava.backend.SeedSpreader;

import java.util.Hashtable;
import java.util.Map;

public class IBackend {
    public static IBackend instance = null;
    public String trayName = null;
    public String trayImageName = null;
    Bitmap trayImage = null;
    public Map<String, Object> tray = null;

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
        this.trayImageName = imageName;
        trayImage = seedSpreader.images.get(imageName);
        if (trayImage == null) {
            Log.d(this.getClass().getSimpleName(), "*&* Not--found: " + imageName);
            Log.d(this.getClass().getSimpleName(), "*&* Images are: " + seedSpreader.images.keySet().toString());
        }
        return trayImage;
    }

    Map<String, Object> getTray(String name) {
        this.trayName = name;
        tray = seedSpreader.trays.get(name);
        if (tray == null) {
            Log.d(this.getClass().getSimpleName(), "*&* Not--found: " + name);
            Log.d(this.getClass().getSimpleName(), "*&* Images are: " + seedSpreader.trays.keySet().toString());
        }
        return tray;
    }
}
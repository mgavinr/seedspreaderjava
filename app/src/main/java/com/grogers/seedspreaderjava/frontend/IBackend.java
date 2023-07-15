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
    public String seedName = null;
    public String seedImageName = null;
    public String seedImageNameBack = null;
    Bitmap trayImage = null;
    Bitmap seedImage = null;
    Bitmap seedImageBack = null;
    public Map<String, Object> tray = null;
    public Map<String, Object> seed = null;

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

    public Bitmap getSImage(String imageName) {
        this.seedImageName = imageName;
        seedImage = seedSpreader.images.get(imageName);
        if (seedImage == null) {
            Log.d(this.getClass().getSimpleName(), "*&* Not--found: " + imageName);
            Log.d(this.getClass().getSimpleName(), "*&* Images are: " + seedSpreader.images.keySet().toString());
        }
        return seedImage;
    }
    public Bitmap getSBImage(String imageName) {
        this.seedImageNameBack = imageName.replace(".", "_back.");
        seedImageBack = seedSpreader.images.get(imageName);
        if (seedImageBack == null) {
            Log.d(this.getClass().getSimpleName(), "*&* Not--found: " + imageName);
            Log.d(this.getClass().getSimpleName(), "*&* Images are: " + seedSpreader.images.keySet().toString());
        }
        return seedImageBack;
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

    Map<String, Object> getSeed(String name) {
        this.seedName = name;
        seed = seedSpreader.seeds.get(name);
        if (seed == null) {
            Log.d(this.getClass().getSimpleName(), "*&* Not--found: " + name);
            Log.d(this.getClass().getSimpleName(), "*&* Seeds are: " + seedSpreader.seeds.keySet().toString());
        }
        return seed;
    }

    String[] getSeeds() {
        java.util.Set<String> set = seedSpreader.seeds.keySet();
        String[] setArray = set.toArray(new String[set.size()]);
        return setArray;
    }
}
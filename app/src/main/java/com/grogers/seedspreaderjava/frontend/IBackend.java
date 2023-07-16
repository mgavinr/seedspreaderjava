package com.grogers.seedspreaderjava.frontend;

import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.EditText;

import com.grogers.seedspreaderjava.backend.SeedSpreader;

import java.util.ArrayList;
import java.util.HashMap;
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

    public Bitmap getSeedImage(String imageName) {
        this.seedImageName = imageName;
        seedImage = seedSpreader.images.get(imageName);
        if (seedImage == null) {
            Log.d(this.getClass().getSimpleName(), "*&* Not--found: " + imageName);
            Log.d(this.getClass().getSimpleName(), "*&* Images are: " + seedSpreader.images.keySet().toString());
        }
        return seedImage;
    }

    public Bitmap getSeedBackImage(String imageName) {
        this.seedImageNameBack = imageName.replace(".", "_back.");
        seedImageBack = seedSpreader.images.get(imageName);
        if (seedImageBack == null) {
            Log.d(this.getClass().getSimpleName(), "*&* Not--found: " + imageName);
            Log.d(this.getClass().getSimpleName(), "*&* Images are: " + seedSpreader.images.keySet().toString());
        }
        return seedImageBack;
    }

    void updateTrayName(String name) {
        seedSpreader.trays.remove(trayName);
        trayName = name;
        tray = seedSpreader.trays.put(trayName, tray);
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

    void save() {
        seedSpreader.update();
    }

    String[] getSeeds() {
        java.util.Set<String> set = seedSpreader.seeds.keySet();
        String[] setArray = set.toArray(new String[set.size()]);
        return setArray;
    }

    void updateEvent(String rowcols, String date, String seedName, String eventName) {
        ArrayList<Integer> userRowCol = LanguageProcessor.getRowCol(rowcols, (Integer) tray.get("rows"), (Integer) tray.get("cols"));
        ArrayList<HashMap<String, Object>> rowContent = null;
        Map<String, Object> colContent = null;
        for(Integer rowOrColValue : userRowCol) {
            // negative values are rows
            if (rowOrColValue < 0) {
                int origRowOrColValue = -rowOrColValue;
                rowOrColValue = -rowOrColValue;
                //Object what = backend.tray.get("content_"+rowOrColValue.toString());
                //if (what != null) {
                //    Log.d(this.getClass().getSimpleName(), "*&* Seed() OK: got this: " + what.toString());
                //}
                rowContent = (ArrayList< HashMap<String, Object> >) tray.get("content_"+rowOrColValue.toString());
                while((rowContent == null) && (rowOrColValue > 0)) {
                    Log.d(this.getClass().getSimpleName(), "*&* Seed() OK: adding new row to yaml: content_"+ rowOrColValue);
                    rowContent = new ArrayList< HashMap<String, Object> >();
                    tray.put("content_"+rowOrColValue, rowContent);
                    rowOrColValue--;
                    rowContent = (ArrayList< HashMap<String, Object> >) tray.get("content_"+rowOrColValue.toString());
                }
                rowOrColValue = origRowOrColValue;
                rowContent = (ArrayList< HashMap<String, Object> >) tray.get("content_"+rowOrColValue.toString());
            }
            // positive values are cols
            else {
                // size 2, means index 0, 1  .. so we use <=
                while(rowContent.size() <= rowOrColValue) {
                    Log.d(this.getClass().getSimpleName(), "*&* Seed() OK: adding new col " + rowOrColValue);
                    rowContent.add(new HashMap<String, Object>());
                }
                Log.d(this.getClass().getSimpleName(), "*&* Seed() OK: setting col" + rowOrColValue);
                colContent = rowContent.get(rowOrColValue);
                if(colContent == null) {
                    colContent = new HashMap<String, Object>();
                } else {
                    int index = 1;
                    while(true) {
                        if (!colContent.containsKey("name"+index)) break;
                        ++index;
                    }
                    colContent.put("name"+index, colContent.get("name"));
                    colContent.put("date"+index, colContent.get("date"));
                    colContent.put("event"+index, colContent.get("event"));
                }
                if (seedName != null) colContent.put("name", seedName);
                colContent.put("date", date);
                colContent.put("event", eventName);
            }
        }
        seedSpreader.update();  // probably best to save just in case exit called.
    }
}
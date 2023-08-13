package com.grogers.seedspreaderjava.frontend;

import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.EditText;

import com.grogers.seedspreaderjava.backend.SeedSpreader;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;

// Major TODO you have to remove all these imageName trayName copies, they are not allowed,

public class IBackend {
    public static IBackend instance = null;
    public Map<String, Object> tray = null;
    public Map<String, Object> seed = null;
    Tray Tray = new Tray();
    Seed Seed = new Seed();

    public static IBackend getInstance() {
        if (instance == null) {
            instance = new IBackend();
        }
        return instance;
    }

    private IBackend() {
    }

    SeedSpreader seedSpreader = SeedSpreader.getInstance();


    class Seed {
        public void saveSeedImage(String imageName, Bitmap bitmap) {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 1000, 1500, false);
            if (!seed.containsKey("image_front")) {
                String imageFileName = LanguageProcessor.getImageName(imageName, "_front");
                Log.d(this.getClass().getSimpleName(), "*&* add image_front");
                seed.put("image_front", imageFileName);
                seedSpreader.images.put(imageFileName, scaledBitmap);
            } else if (!seed.containsKey("image_back")) {
                String imageFileName = LanguageProcessor.getImageName(imageName, "_back");
                Log.d(this.getClass().getSimpleName(), "*&* add image_back");
                seed.put("image_back", imageFileName);
                seedSpreader.images.put(imageFileName, scaledBitmap);
            } else {
                String imageFileName = LanguageProcessor.getImageName(imageName, "");
                Log.d(this.getClass().getSimpleName(), "*&* add image");
                seed.put("image", imageFileName);
                seedSpreader.images.put(imageFileName, scaledBitmap);
            }
        }

        public Bitmap getSeedFrontImage(String imageName) {
            Bitmap seedImage = seedSpreader.images.get(imageName);
            if (seedImage == null) {
                Log.d(this.getClass().getSimpleName(), "*&* Not--found: " + imageName);
                Log.d(this.getClass().getSimpleName(), "*&* Images are: " + seedSpreader.images.keySet().toString());
            }
            return seedImage;
        }

        public Bitmap getSeedBackImage(String imageName) {
            String seedImageNameBack = imageName.replace(".", "_back.");
            Bitmap seedImageBack = seedSpreader.images.get(imageName);
            if (seedImageBack == null) {
                Log.d(this.getClass().getSimpleName(), "*&* Not--found: " + imageName);
                Log.d(this.getClass().getSimpleName(), "*&* Images are: " + seedSpreader.images.keySet().toString());
            }
            return seedImageBack;
        }

        Map<String, Object> addSeed(String seedName, String description, String year) {
            Integer seedPacketYear = Integer.parseInt(year);
            if (seedSpreader.seeds.containsKey(seedName)) {
                seed = getSeed(seedName);
            } else {
                seed = new TreeMap<String, Object>();
                seed.put("name", seedName);
                seed.put("description", description);
                seedSpreader.seeds.put(seedName, seed);
            }
            if (seed.containsKey("year") == false) {
                ArrayList<String> years = new ArrayList<>();
                seed.put("year", years);
            }
            ArrayList<Integer> years = (ArrayList<Integer>) seed.get("year");
            if (!years.contains(seedPacketYear)) years.add(seedPacketYear);
            return seed;
        }

        Map<String, Object> getSeed(String seedName) {
            seed = seedSpreader.seeds.get(seedName);
            if (seed == null) {
                Log.d(this.getClass().getSimpleName(), "*&* Not--found: " + seedName);
                Log.d(this.getClass().getSimpleName(), "*&* Seeds are: " + seedSpreader.seeds.keySet().toString());
            }
            return seed;
        }

        int getSeedImageCount(String seedName) {
            int rv = 0;
            seed = seedSpreader.seeds.get(seedName);
            if (seed == null) return 0;
            if (seed.containsKey("image_front")) ++rv;
            if (seed.containsKey("image_back")) ++rv;
            Log.d(this.getClass().getSimpleName(), "*&* seed is " + seed.toString());
            return rv;
        }

        // TODO, as it gets longer, just return the first letters after say 5 seeds?
        // have a scrolling list of seeds?
        // have a changing list of seeds?
        // have a random list of seeds?
        String[] getSeeds() {
            java.util.Set<String> set = seedSpreader.seeds.keySet();
            String[] setArray = set.toArray(new String[set.size()]);
            return setArray;
        }
    }

    class Tray {

        public java.util.Set<String> getTrays() {
            return seedSpreader.trays.keySet();
        }

        public java.util.Set<String> getTraysSort() {
            return seedSpreader.trays.keySet();
        }

        void remTray(String trayName) {
            seedSpreader.trays.remove(trayName);
        }

        Map<String, Object> addTray(String trayName, String description) {
            if (seedSpreader.trays.containsKey(trayName)) {
                tray = getTray(trayName);
            } else {
                tray = new TreeMap<String, Object>();
                tray.put("name", trayName);
                tray.put("description", description);
                tray.put("cols", 11);
                tray.put("rows", 11);
                tray.put("image", LanguageProcessor.getImageName(trayName, ""));
                // TODO check this is unique otherwise they'll duplicate
                // TODO check while on the filesystem, all the images look terrible, taken fromthe camera
                Log.d(this.getClass().getSimpleName(), "*&* New tray will use image: " + tray.get("image"));
                // Duplicate hard coded new_image.jpg to the new trays image
                seedSpreader.images.put(tray.get("image").toString(), seedSpreader.images.get("new_image.jpg"));
                seedSpreader.trays.put(trayName, tray);
            }
            if (tray.containsKey("year") == false) {
                ArrayList<String> years = new ArrayList<>();
                tray.put("year", years);
            }
            ArrayList<Integer> years = (ArrayList<Integer>) tray.get("year");
            if (!years.contains(LanguageProcessor.getYear())) years.add(LanguageProcessor.getYear());
            // get it?
            tray = getTray(trayName);
            return tray;
        }

        public Bitmap saveTrayImage(Bitmap bitmap) {
            String trayImageName = tray.get("image").toString();
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 1000, 1500, false);
            seedSpreader.images.put(trayImageName, scaledBitmap);
            return scaledBitmap;
        }

        public String getTrayName() {
            return tray.get("name").toString();
        }

        public Bitmap getImage() {
            String trayImageName = tray.get("image").toString();
            if (trayImageName != null) {
                Bitmap trayImage = seedSpreader.images.get(trayImageName);
                if (trayImage == null) {
                    Log.d(this.getClass().getSimpleName(), "*&* Not--found: " + trayImageName);
                    Log.d(this.getClass().getSimpleName(), "*&* Images are: " + seedSpreader.images.keySet().toString());
                }
                return trayImage;
            } else {
                Log.d(this.getClass().getSimpleName(), "*&* Looking for 'null' image so we are returning null");
                return null;
            }
        }

        void updateTrayName(String trayNameNew) {
            Object removed = seedSpreader.trays.remove(tray.get("name"));
            if (removed == null) Log.e(this.getClass().getSimpleName(), "*&* TrayRemove failed");
            if (trayNameNew.equals("delete")) {
                Log.i(this.getClass().getSimpleName(), "*&* TrayRemove and not re-added (delete)");
            } else {
                Log.d(this.getClass().getSimpleName(), "*&* TrayRemove and re-added |" + trayNameNew + "|");
                seedSpreader.trays.put(trayNameNew, tray);
            }
            tray.put("name", trayNameNew);
        }

        boolean hasTray(String name) {
            return seedSpreader.trays.containsKey(name);
        }

        Map<String, Object> getTray(String trayName) {
            tray = seedSpreader.trays.get(trayName);
            if (tray == null) {
                Log.d(this.getClass().getSimpleName(), "*&* Not--found: " + trayName);
                Log.d(this.getClass().getSimpleName(), "*&* Trays are: " + seedSpreader.trays.keySet().toString());
            } else {
                Log.d(this.getClass().getSimpleName(), "*&* Tray is " + tray.toString());
            }
            return tray;
        }

        void updateEvent(String rowcols, String date, String seedName, String eventName) {
            if (eventName == null) Log.e(this.getClass().getSimpleName(), "*&* updateEvent for null error");
            ArrayList<Integer> userRowCol = LanguageProcessor.getRowCol(rowcols, (Integer) tray.get("rows"), (Integer) tray.get("cols"));
            ArrayList<TreeMap<String, Object>> rowContent = null;
            Map<String, Object> colContent = null;
            String rowName = null;
            for(Integer rowOrColValue : userRowCol) {
                // negative values are rows
                if (rowOrColValue < 0) {
                    int origRowOrColValue = -rowOrColValue;
                    rowOrColValue = -rowOrColValue;
                    rowName = String.format("content_%05d", rowOrColValue);
                    rowContent = (ArrayList< TreeMap<String, Object> >) tray.get(rowName);
                    while((rowContent == null) && (rowOrColValue > 0)) {
                        Log.d(this.getClass().getSimpleName(), "*&* Seed() OK: adding new row to yaml: " + rowName);
                        rowContent = new ArrayList< TreeMap<String, Object> >();
                        tray.put(rowName, rowContent);
                        rowOrColValue--;
                        rowName = String.format("content_%05d", rowOrColValue);
                        rowContent = (ArrayList< TreeMap<String, Object> >) tray.get(rowName);
                    }
                    rowOrColValue = origRowOrColValue;
                    rowName = String.format("content_%05d", rowOrColValue);
                    rowContent = (ArrayList< TreeMap<String, Object> >) tray.get(rowName);
                }
                // positive values are cols
                else {
                    // rowContent is an array of TreeMap/HashMaps each TreeMap/HashMap is a column
                    while(rowContent.size() <= rowOrColValue) {
                        // size 2, means index 0, 1  .. so we use <=
                        Log.d(this.getClass().getSimpleName(), "*&* adding column to row for  " + rowOrColValue);
                        rowContent.add(new TreeMap<String, Object>());
                    }
                    // colContent is a TreeMap/HashMap single, with many entries bumped on per event
                    colContent = rowContent.get(rowOrColValue);
                    if(colContent == null) colContent = new TreeMap<String, Object>();

                    // bump the last event, TODO it assumes comma is not in any other field
                    if (colContent.get("name") != null) {
                        int index = 1;
                        while (true) {
                            if (!colContent.containsKey("history" + index)) break;
                            ++index;
                        }
                        String history = "";
                        if (colContent.get("history" + index) != null) history += colContent.get("history"+ index);
                        history += colContent.get("date") + " ," + colContent.get("event") + " ," + colContent.get("name") + " ,";
                        colContent.put("history" + index, history);
                    }

                    // Add the event, seedName is only available in add seeds
                    if (seedName != null) colContent.put("name", seedName);
                    colContent.put("date", date);
                    colContent.put("event", eventName);
                }
            }
        }
    }

}

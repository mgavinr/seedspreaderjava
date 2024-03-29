package com.grogers.seedspreaderjava.backend;

import android.graphics.Bitmap;
import android.util.Log;

import com.grogers.seedspreaderjava.frontend.LanguageProcessor;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SampleData {
    static public IFrontend frontend = IFrontend.getInstance();
    static public boolean seeds = false;
    static public boolean trays = false;
    static public boolean images = false;
    static public boolean settings = false;
    static public int settings_api = 1;
    static public int images_api = 1;
    static public int trays_api = 1;
    static public int seeds_api = 1;

    // maybe call flush before close
    static void createImages() {
        images = true;
        for (String image : List.of("new_image", "sample_chili", "sample_chili_back", "sample_tomato", "sample_tomato_back", "tray1", "tray2")) {
            try {
                String filePath = frontend.imageFilesPublic + "/" + image + ".jpg";
                FileOutputStream fos = new FileOutputStream(filePath);
                Bitmap bitmap = frontend.getInstance().getBitmap(image);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            } catch (IOException e) {
                Log.e(SampleData.class.getSimpleName(), "*&*&* Could not write sample data: ioe " + e + " " + image);
            } catch (Exception e) {
                Log.e(SampleData.class.getSimpleName(), "*&*&* Could not write sample data: " + e + " "+ image);
            }
        }
    }

    static void createSeeds() {
        seeds = true;
        String sample = "---\n" +
                "api: " + seeds_api + "\n" +
                "name: Sample Chili Pepper\n" +
                "image_front: sample_chili.jpg\n" +
                "image_back: sample_chili_back.jpg\n" +
                "description: A sample chili pepper\n" +
                "year:\n" +
                "- " + LanguageProcessor.getYear() + "\n" +
                "---\n" +
                "api: " + seeds_api + "\n" +
                "name: Sample tomato\n" +
                "image_front: sample_tomato.jpg\n" +
                "image_back: sample_tomato_back.jpg\n" +
                "description: A sample tomato\n" +
                "year:\n" +
                "- " + LanguageProcessor.getYear() + "\n" +
                "";
        try {
            String filePath = frontend.filesPublic + "/" + "seeds.yaml";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(sample);
            writer.close();
        } catch (IOException e) {
            Log.e(SampleData.class.getSimpleName(), "*&*&* Could not write sample data: " + e);
        }
    }

    static void createSettings() {
        settings = true;
        String sample = "---\n" +
                "api: " + settings_api + "\n" +
                "name: default\n" +
                "year: " + LanguageProcessor.getYear() + "\n";
        try {
            String filePath = frontend.filesPublic + "/" + "settings.yaml";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(sample);
            writer.close();
        } catch (IOException e) {
            Log.e(SampleData.class.getSimpleName(), "*&*&* Could not write sample data: " + e);
        }
    }

    /**
     * This auto removes some errors from old format yaml files
     * @param ss
     */
    static boolean fixTraysApi_0(SeedSpreader ss) {
        boolean rv = false;
        for (String trayName : ss.trays.keySet()) {
            Map<String, Object> tray = ss.trays.get(trayName);
            for (String key : tray.keySet()) {
                if (key.contains("content_")) {
                    ArrayList<?> rowContent = (ArrayList<?>) tray.get(key);
                    if (rowContent != null) {
                        for (Object colObject : rowContent) {
                            Map<String, Object> col = (Map<String, Object>) colObject;
                            if (col != null) {
                                List<String> keysToRemove = new ArrayList<>();
                                for (String colkey : col.keySet()) {
                                    if (col.get(colkey) == null) {
                                        Log.d("SampleData.java", "*&*&* COL Fix remove key with value: " + colkey + "=" + col.get(colkey));
                                        keysToRemove.add(colkey);
                                    }
                                }
                                for(String keyrm: keysToRemove) {
                                    col.remove(keyrm);
                                    rv = true;
                                }
                            }
                        }
                    }
                }
                if (tray.get(key) == null) {
                    Log.d("SampleData.java", "*&*&* Fix remove key with value: " + key + "="+ tray.get(key));
                    //tray.remove(key);
                }
                if (tray.get(key) != null && tray.get(key).equals("")) {
                    Log.d("SampleData.java", "*&*&* Fix remove key with value: " + key + "="+ tray.get(key));
                    //tray.remove(key);
                }
            }
        }
        return rv;
    }

    static void createTrays() {
        trays = true;
        String sampleTrays = "---\n" +
                "api: " + trays_api + "\n" +
                "name: Veg Tray\n" +
                "description: A tray holding veg\n" +
                "image: tray1.jpg\n" +
                "rows: 10\n" +
                "cols: 10\n" +
                "year:\n" +
                "- " + LanguageProcessor.getYear() + "\n" +
                "content_1:\n" +
                "- name: Sample Tomato\n" +
                "  date: '" + LanguageProcessor.getDate(5) + "'\n" +
                "  event: planted\n" +
                "- name: Sample Tomato\n" +
                "  date: '" + LanguageProcessor.getDate(4) + "'\n" +
                "  event: seedling\n" +
                "- name: Sample Tomato\n" +
                "  date: '" + LanguageProcessor.getDate(3) + "'\n" +
                "  event: planted\n" +
                "content_2: null\n" +
                "content_3:\n" +
                "- name: Sample Tomato\n" +
                "  date: '" + LanguageProcessor.getDate(2) + "'\n" +
                "  event: planted\n" +
                "---\n" +
                "api: " + trays_api + "\n" +
                "name: Fruit Tray\n" +
                "description: A tray holding fruits\n" +
                "image: tray2.jpg\n" +
                "year:\n" +
                "- " + LanguageProcessor.getYear() + "\n" +
                "rows: 10\n" +
                "cols: 10\n" +
                "content_1:\n" +
                "- name: Sample Chili Pepper\n" +
                "  date: '" + LanguageProcessor.getDate(4) + "'\n" +
                "  event: planted\n" +
                "- name: Sample Chili Pepper\n" +
                "  date: '" + LanguageProcessor.getDate(3) + "'\n" +
                "  event: seedling\n" +
                "- name: Sample Chili Pepper\n" +
                "  date: '" + LanguageProcessor.getDate(2) + "'\n" +
                "  event: planted\n";
        try {
            String filePath = frontend.filesPublic + "/" + LanguageProcessor.getYear() + "trays.yaml";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(sampleTrays);
            writer.close();
        } catch (IOException e) {
            Log.e(SampleData.class.getSimpleName(), "*&*&* Could not write sample data: " + e);
        }
    }
}

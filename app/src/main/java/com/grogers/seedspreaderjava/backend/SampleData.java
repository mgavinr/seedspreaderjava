package com.grogers.seedspreaderjava.backend;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SampleData {
    static public IFrontend frontend = IFrontend.getInstance();
    static public boolean seeds = false;
    static public boolean trays = false;
    static public boolean images = false;

    // maybe call flush before close
    static void createImages() {
        images = true;
        for (String image : List.of("sample_chili", "sample_chili_back", "sample_tomato", "sample_tomato_back", "tray1", "tray2")) {
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
                "image: sample_chili.jpg\n" +
                "year:\n" +
                "- 2023\n" +
                "name: Sample Chili Pepper\n" +
                "description: A sample chili pepper\n" +
                "---\n" +
                "image: sample_tomato.jpg\n" +
                "year:\n" +
                "- 2023\n" +
                "name: Sample tomato\n" +
                "description: A sample tomato\n";
        try {
            String filePath = frontend.filesPublic + "/" + "seeds.yaml";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(sample);
            writer.close();
        } catch (IOException e) {
            Log.e(SampleData.class.getSimpleName(), "*&*&* Could not write sample data: " + e);
        }
    }

    static void createTrays() {
        trays = true;
        String sampleTrays = "---\n" +
                "name: Veg Tray\n" +
                "description: A tray holding veg\n" +
                "rows: 10\n" +
                "cols: 10\n" +
                "image: tray1.jpg\n" +
                "year:\n" +
                "- 2023\n" +
                "contents:\n" +
                "- - name: Sample Tomato\n" +
                "    date: 2023\n" +
                "    event: planted\n" +
                "  - name: Sample Tomato\n" +
                "    date: 2023\n" +
                "    event: seedling\n" +
                "- - name: Sample Tomato\n" +
                "    date: 2023\n" +
                "    event: planted\n" +
                "---\n" +
                "name: Fruit Tray\n" +
                "description: A tray holding fruits\n" +
                "rows: 10\n" +
                "cols: 10\n" +
                "image: tray2.jpg\n" +
                "year:\n" +
                "- 2023\n" +
                "contents:\n" +
                "- - name: Sample Chili Pepper\n" +
                "    date: 2023\n" +
                "    event: planted\n" +
                "  - name: Sample Chili Pepper\n" +
                "    date: 2023\n" +
                "    event: seedling\n" +
                "- - name: Sample Chili Pepper\n" +
                "    date: 2023\n" +
                "    event: planted\n";
        try {
            String filePath = frontend.filesPublic + "/" + "trays.yaml";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(sampleTrays);
            writer.close();
        } catch (IOException e) {
            Log.e(SampleData.class.getSimpleName(), "*&*&* Could not write sample data: " + e);
        }
    }
}

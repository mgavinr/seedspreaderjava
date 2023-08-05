package com.grogers.seedspreaderjava.backend;

import android.graphics.Bitmap;
import android.util.Log;

import com.grogers.seedspreaderjava.frontend.LanguageProcessor;

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
    static public boolean settings = false;

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
                "name: Sample Chili Pepper\n" +
                "image_front: sample_chili.jpg\n" +
                "image_back: sample_chili_back.jpg\n" +
                "description: A sample chili pepper\n" +
                "year:\n" +
                "- " + LanguageProcessor.getYear() + "\n" +
                "---\n" +
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

    static void createTrays() {
        trays = true;
        String sampleTrays = "---\n" +
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

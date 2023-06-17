package com.grogers.seedspreaderjava.backend;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.grogers.seedspreaderjava.frontend.SeedApplication;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Map;

class YamlReader {
    public Map<String, Object> readYamlFile(String filePath) throws FileNotFoundException {
        Yaml yaml = new Yaml();

        FileInputStream fileInputStream = new FileInputStream(filePath);
        Map<String, Object> yamlData = yaml.load(fileInputStream);

        return yamlData;
    }

}
class ifAndroid {
    public Context context = SeedApplication.getContext();

    /* Files */
    ///* /data/user/0/com.grogers.seedspreaderjava/seed.yaml */
    public File filesPrivate = context.getDataDir();
    ///* /data/user/0/com.grogers.seedspreaderjava/files/seed.yaml */
    public File filesPrivateParent = context.getFilesDir();
    // public
    ///*  /storage/emulated/0/Android/data/com.grogers.seedspreaderjava/files/Documents/seed.yaml */
    public File filesPublic = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

    /* Cache */
    ///* /data/user/0/com.grogers.seedspreaderjava/cache/seed.yaml */
    public File cachePrivate = context.getCacheDir();
    // public
    // /* /storage/emulated/0/Android/data/com.grogers.seedspreaderjava/cache/seed.yaml */
    public File cachePublic = context.getExternalCacheDir();
}
public class SeedSpreader {
    /**
     * Constructors
     */
    public static SeedSpreader instance = null;
    public static SeedSpreader getInstance() {
        if (instance == null) {
            instance = new SeedSpreader();
        }
        return instance;
    }
    public SeedSpreader() {
        Log.d(this.getClass().getSimpleName(), "*&**&* Starting backend SeedSpreader app");
        this.readDataForSeed();
    }

    /**
     * Public Fields
     */
    public ifAndroid ifand = new ifAndroid();
    public Hashtable<String, String> trays = new Hashtable<String, String>();

    /**
     * Read all data from filesystem
     *
     */
    void readDataForSeed() {
        YamlReader reader = new YamlReader();
        try {
            // this is internal String filePath = ifand.context.getFilesDir() + "seed.yaml";
            String filePath = ifand.cachePrivate + "/" + "seed.yaml";
            Map<String, Object> yamlData = reader.readYamlFile(filePath);
        } catch (FileNotFoundException e) {
            Log.d(this.getClass().getSimpleName(), "*&* There is no seed.yaml file: " + e.toString());
        }
    }


}

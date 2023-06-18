package com.grogers.seedspreaderjava.backend;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.grogers.seedspreaderjava.frontend.SeedApplication;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

class YamlReader {
    public Map<String, Object> readYamlFile(String filePath) throws FileNotFoundException {
        Yaml yaml = new Yaml();

        FileInputStream fileInputStream = new FileInputStream(filePath);
        Map<String, Object> yamlData = yaml.load(fileInputStream);

        return yamlData;
    }

}

class YamlWriter {
    public boolean append = true; // truncate is false, otherwise
    public void writeYamlFile(String filePath, Map<String, Object> data) {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // Optional: Set the desired flow style

        // Create a YAML instance
        Yaml yaml = new Yaml(options);

        // Write the YAML content to a file
        try (FileWriter writer = new FileWriter(filePath, append)) {
            yaml.dump(data, writer);
            Log.d(this.getClass().getSimpleName(), "*&*&* YAML file written successfully.");
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "*&*&* Error writing YAML file: " + e.getMessage());
        }
    }
}

class ifAndroid {
    public Context context = SeedApplication.getContext();

    /* Files */
    ///* /data/user/0/com.grogers.seedspreaderjava/seed.yaml */
    public File filesPrivateParent = context.getDataDir();
    ///* /data/user/0/com.grogers.seedspreaderjava/files/seed.yaml */
    public File filesPrivate = context.getFilesDir();
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
class SampleData {
    public SampleData() {
        createSeeds();
    }
    void createSeeds() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "Sweet Pepper");
        data.put("description", "A sweet pepper plant");
        data.put("image", "sweetpepper.jpg");
        data.put("year", List.of(2023));
        SeedSpreader.getInstance().seeds.put(data.get("name").toString(), data);
    }
}
public class SeedSpreader {
    /**
     * Constructors
     */
    public static SeedSpreader instance = null;
    public static SeedSpreader getInstance() {
        if (instance == null) {
            instance = new SeedSpreader();
            instance.start();   // like this so to avoid loops creating it
        }
        return instance;
    }
    public SeedSpreader() {
        Log.d(this.getClass().getSimpleName(), "*&*&* Starting backend SeedSpreader app");
    }

    public void start() {
        readDataForSeeds();
    }

    /**
     * Public Fields
     */
    public ifAndroid ifand = new ifAndroid();
    /* table is thread safe HashMap is modern not thread safe */
    public Hashtable<String, Map<String, Object> > trays = new Hashtable<String, Map<String, Object> >();
    public Hashtable<String, Map<String, Object> > seeds = new Hashtable<String, Map<String, Object> >();

    /**
     * Read all data from filesystem
     *
     */
    void readDataForSeeds() {
        YamlReader reader = new YamlReader();
        String filePath = ifand.filesPublic + "/" + "seeds.yaml";
        try {
            // this is internal String filePath = ifand.context.getFilesDir() + "seeds.yaml";
            Map<String, Object> yamlData = reader.readYamlFile(filePath);
            Log.d(this.getClass().getSimpleName(), "*&*&* readDataForSeeds(" + yamlData.get("name") + ")");
        } catch (FileNotFoundException e) {
            Log.d(this.getClass().getSimpleName(), "*&*&* There is no seeds.yaml file: " + e.toString());
            new SampleData();
        }
    }

    void writeDataForSeeds() {
        YamlWriter writer = new YamlWriter();
        String filePath = ifand.filesPublic + "/" + "seeds.yaml";
        for( String name : seeds.keySet()) {
            Log.d(this.getClass().getSimpleName(), "*&*&* writeDataForSeeds(" + name + ")");
            Map<String, Object> o = seeds.get(name);
            writer.writeYamlFile(filePath, o);
        }
    }


}

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class YamlReader {
    boolean multiple = true;
    interface Callback {
        void onCallback(Map<String, Object> yamlData);
    }
    void readYamlFile(String filePath, Callback cb) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        FileInputStream fileInputStream = new FileInputStream(filePath);
        if (multiple) {
            Log.d(this.getClass().getSimpleName(), "*&*&* Reading multiple yaml documents");
            Iterable<Object> documents = yaml.loadAll(fileInputStream);
            Iterator<Object> iterator = documents.iterator();
            while(iterator.hasNext()) {
                Object document = iterator.next();
                if (document instanceof Map) {
                    Map<String, Object> yamlData = (Map<String, Object>) document;
                    cb.onCallback(yamlData);
                }
            }
        } else {
            Log.d(this.getClass().getSimpleName(), "*&*&* Reading single yaml documents");
            Map<String, Object> yamlData = yaml.load(fileInputStream);
            Log.d(this.getClass().getSimpleName(), "*&*&* YAML file read successfully.");
            Log.d(this.getClass().getSimpleName(), "*&*&*" + yaml.dump(yamlData));
            cb.onCallback(yamlData);
        }
    }

}

class YamlWriter {
    public boolean append = true; // truncate is false, otherwise
    public void writeYamlFile(String filePath, Map<String, Object> data) {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // Optional: Set the desired flow style
        options.setExplicitStart(true);

        // Create a YAML instance
        Yaml yaml = new Yaml(options);

        // Write the YAML content to a file
        try (FileWriter writer = new FileWriter(filePath, append)) {
            Log.d(this.getClass().getSimpleName(), "*&*&* YAML file written successfully.");
            Log.d(this.getClass().getSimpleName(), "*&*&*" + yaml.dump(data));
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
        Map<String, Object> yamlData = new HashMap<>();
        yamlData.put("name", "Sweet Pepper");
        yamlData.put("description", "A sweet pepper plant");
        yamlData.put("image", "sweetpepper.jpg");
        yamlData.put("year", List.of(2023));
        SeedSpreader.getInstance().seeds.put(yamlData.get("name").toString(), yamlData);
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
        Log.d(this.getClass().getSimpleName(), "*&*&* readDataForSeeds(" + filePath + ")");
        try {
            // this is internal String filePath = ifand.context.getFilesDir() + "seeds.yaml";
            reader.readYamlFile(filePath, new YamlReader.Callback() {
                @Override
                public void onCallback(Map<String, Object> yamlData) {
                    seeds.put(yamlData.get("name").toString(), yamlData);
                }
            });
            Log.d(this.getClass().getSimpleName(), "*&*&* readDataForSeeds done, read " + seeds.size() + " seeds");
        } catch (FileNotFoundException e) {
            Log.d(this.getClass().getSimpleName(), "*&*&* There is no seeds.yaml file: " + e.toString());
            new SampleData();
        } catch (Exception e) {
            Log.d(this.getClass().getSimpleName(), "*&*&* There is an error with seeds.yaml file: " + e.toString());
        }
        writeDataForSeeds();
    }

    void writeDataForSeeds() {
        YamlWriter writer = new YamlWriter();
        String filePath = ifand.filesPublic + "/" + "seeds.yaml";
        for( String name : seeds.keySet()) {
            Log.d(this.getClass().getSimpleName(), "*&*&* writeDataForSeeds(" + name + ", " + filePath + ")");
            Map<String, Object> o = seeds.get(name);
            writer.writeYamlFile(filePath, o);
        }
    }


}

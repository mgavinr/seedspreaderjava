package com.grogers.seedspreaderjava.backend;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.grogers.seedspreaderjava.frontend.LanguageProcessor;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
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
            Log.d(this.getClass().getSimpleName(), "*&*&* readYamlFile: Reading multiple yaml documents");
            Iterable<Object> documents = yaml.loadAll(fileInputStream);
            Iterator<Object> iterator = documents.iterator();
            while(iterator.hasNext()) {
                Object document = iterator.next();
                if (document instanceof Map) {
                    Map<String, Object> yamlData = (Map<String, Object>) document;
                    cb.onCallback(yamlData);
                } else {
                    Log.e(this.getClass().getSimpleName(), "*&*&* we don't know what this yaml is");
                }
            }
        } else {
            Log.d(this.getClass().getSimpleName(), "*&*&* readYamlFile: Reading single yaml documents");
            Map<String, Object> yamlData = yaml.load(fileInputStream);
            Log.d(this.getClass().getSimpleName(), "*&*&* YAML file read successfully.");
            Log.d(this.getClass().getSimpleName(), "*&*&*" + yaml.dump(yamlData));
            cb.onCallback(yamlData);
        }
    }

}

class YamlWriter {
    public boolean append = true; // truncate is false, otherwise
    public void writeYamlFile(String filePath, Object data) {
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
        readDataForSettings();
        settingsYear = (Integer) settings.get("year");
        Log.d(this.getClass().getSimpleName(), "*&*&* Settings for year:" + settingsYear.toString());
        readDataForSeeds();
        readDataForTrays();
        readImages();
    }

    public void update() {
        writeDataForSettings();
        writeDataForSeeds();
        writeDataForTrays();
        writeImages();
    }

    /**
     * Public Fields
     */
    public boolean sampleData = true;
    public IFrontend frontend = IFrontend.getInstance();
    /* table is thread safe HashMap is modern not thread safe */
    public Hashtable<String, Map<String, Object> > trays = new Hashtable<String, Map<String, Object> >();
    public Hashtable<String, Map<String, Object> > seeds = new Hashtable<String, Map<String, Object> >();
    public Map<String, Object> settings = new Hashtable<String, Object>();
    public Hashtable<String, Bitmap> images = new Hashtable<String, Bitmap>();
    public boolean scale = true;
    public Integer settingsYear = (Integer) LanguageProcessor.getYear();

    /**
     * Read seeds.yaml document and store in a Map name -> anything
     */
    void readDataForSeeds() {
        try {
            YamlReader reader = new YamlReader();
            String filePath = frontend.filesPublic + "/" + "seeds.yaml";
            Log.d(this.getClass().getSimpleName(), "*&*&* readDataForSeeds(" + filePath + ")");
            reader.readYamlFile(filePath, new YamlReader.Callback() {
                @Override
                public void onCallback(Map<String, Object> yamlData) {
                    seeds.put(yamlData.get("name").toString(), yamlData);
                }
            });
            Log.d(this.getClass().getSimpleName(), "*&*&* readDataForSeeds done, read " + seeds.size() + " seeds from AndroidFS");
        } catch (FileNotFoundException e) {
            Log.d(this.getClass().getSimpleName(), "*&*&* readDataForSeeds no seeds.yaml file: " + e.toString());
        } catch (Exception e) {
            Log.d(this.getClass().getSimpleName(), "*&*&* readDataForSeeds has an error with seeds.yaml file: " + e.toString());
        }
        if(sampleData || seeds.size() == 0) {
            if (SampleData.seeds == false) {
                Log.d(this.getClass().getSimpleName(), "*&*&* readDataForSeeds() use sample data");
                SampleData.createSeeds();
                Log.d(this.getClass().getSimpleName(), "*&*&* readDataForSeeds() re-reading from sample data");
                readDataForSeeds();
            }
        } else {
            writeDataForSeeds();
        }
    }

    /**
     * Read settings.yaml document and store in a Map name -> anything
     */
    void readDataForSettings() {
        try {
            YamlReader reader = new YamlReader();
            String filePath = frontend.filesPublic + "/" + "settings.yaml";
            Log.d(this.getClass().getSimpleName(), "*&*&* readDataForSettings(" + filePath + ")");
            reader.readYamlFile(filePath, new YamlReader.Callback() {
                @Override
                public void onCallback(Map<String, Object> yamlData) {
                    settings = yamlData;
                }
            });
            Log.d(this.getClass().getSimpleName(), "*&*&* readDataForSettings done, read " + settings.size() + " settings from AndroidFS");
        } catch (FileNotFoundException e) {
            Log.d(this.getClass().getSimpleName(), "*&*&* readDataForSettings no settings.yaml file: " + e.toString());
        } catch (Exception e) {
            Log.d(this.getClass().getSimpleName(), "*&*&* readDataForSettings has an error with settings.yaml file: " + e.toString());
        }
        if(sampleData || settings.size() == 0) {
            if (SampleData.settings == false) {
                Log.d(this.getClass().getSimpleName(), "*&*&* readDataForSettings() use sample data");
                SampleData.createSettings();
                Log.d(this.getClass().getSimpleName(), "*&*&* readDataForSettings() re-reading from sample data");
                readDataForSettings();
            }
        } else {
            writeDataForSettings();
        }
    }

    /**
     * Write seeds.yaml document (reopens file and appends, auto adds ---)
     */
    void writeDataForSeeds() {
        YamlWriter writer = new YamlWriter();
        String filePath = frontend.filesPublic + "/" + "seeds.yaml";
        for( String name : seeds.keySet()) {
            Log.d(this.getClass().getSimpleName(), "*&*&* writeDataForSeeds(" + name + ", " + filePath + ")");
            Map<String, Object> o = seeds.get(name);
            writer.writeYamlFile(filePath, o);
        }
    }

    /**
     * Write settings.yaml document (reopens file and appends, auto adds ---)
     */
    void writeDataForSettings() {
        YamlWriter writer = new YamlWriter();
        String filePath = frontend.filesPublic + "/" + "settings.yaml";
        for( String name : settings.keySet()) {
            Log.d(this.getClass().getSimpleName(), "*&*&* writeDataForSettings(" + name + ", " + filePath + ")");
            Object o = settings.get(name);
            writer.writeYamlFile(filePath, o);
        }
    }


    /**
     * Read all images from fileystem (/storage/emulated/0/Android/data/com.grogers.seedspreaderjava/files/Pictures/*)
     */
    void readImages() {
        try {
            File filePath = frontend.imageFilesPublic;
            Log.d(this.getClass().getSimpleName(), "*&*&* readImages(" + filePath + ")");
            File[] files = filePath.listFiles();
            for (File file : files) {
                FileInputStream fileInputStream = new FileInputStream(file);
                Log.d(this.getClass().getSimpleName(), "*&*&* readImages read " + file.getName());
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if(bitmap.getWidth() == 1000) {
                    images.put(file.getName(), bitmap);
                } else {
                    Log.d(this.getClass().getSimpleName(), "*&*&* readImages Scaling image from " + bitmap.getWidth());
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 1000,1500, false);
                    images.put(file.getName(), scaledBitmap);
                }
            }
            Log.d(this.getClass().getSimpleName(), "*&*&* readImages done, read " + images.size() + " images from AndroidFS");
        } catch (FileNotFoundException e) {
            Log.d(this.getClass().getSimpleName(), "*&*&* readImages: There is no image: " + e.toString());
        } catch (Exception e) {
            Log.d(this.getClass().getSimpleName(), "*&*&* readImages: There is an error with images file: " + e.toString());
        }

        if(sampleData || images.size() == 0) {
            if( SampleData.images == false) {
                Log.d(this.getClass().getSimpleName(), "*&*&* readImages use sample data");
                SampleData.createImages();
                Log.d(this.getClass().getSimpleName(), "*&*&* readImages re-reading from sample data");
                readImages();
            }
        } else {
            Log.d(this.getClass().getSimpleName(), "*&*&* readImages There are " + images.size() +" images");
            writeImages();
        }
    }

    /**
     * Write each image (/storage/emulated/0/Android/data/com.grogers.seedspreaderjava/files/Pictures/*)
     */
    void writeImages() {
        File filePath = frontend.imageFilesPublic;
        Log.d(this.getClass().getSimpleName(), "*&*&* writeImages(size=" + images.size() + ", to=" + filePath + ")");
        for (String name : images.keySet()) {
            try {
                File file = new File(filePath, name);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                Log.d(this.getClass().getSimpleName(), "*&*&* writeImages " + file.getName());
                Bitmap bitmap = images.get(name);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.close();
            } catch (IOException e) {
                Log.d(this.getClass().getSimpleName(), "*&*&* writeImages failed for ioe " + name);
            } catch (Exception e) {
                Log.d(this.getClass().getSimpleName(), "*&*&* writeImages failed for " + name);
            }
        }
    }

    /**
     * Read trays.yaml document and store in a Map name -> anything
     */
    void readDataForTrays() {
        try {
            YamlReader reader = new YamlReader();
            String filePath = frontend.filesPublic + "/" + settingsYear + "trays.yaml";
            Log.d(this.getClass().getSimpleName(), "*&*&* readDataForTrays(" + filePath + ")");
            reader.readYamlFile(filePath, new YamlReader.Callback() {
                @Override
                public void onCallback(Map<String, Object> yamlData) {
                    trays.put(yamlData.get("name").toString(), yamlData);
                }
            });
            Log.d(this.getClass().getSimpleName(), "*&*&* readDataForTrays done, read " + trays.size() + " trays from AndroidFS");
        } catch (FileNotFoundException e) {
            Log.d(this.getClass().getSimpleName(), "*&*&* readDataForTrays There is no trays.yaml file: " + e.toString());
        } catch (Exception e) {
            Log.d(this.getClass().getSimpleName(), "*&*&* readDataForTrays There is an error with trays.yaml file: " + e.toString());
        }
        if (sampleData || trays.size() == 0) {
            if (SampleData.trays == false) {
                Log.d(this.getClass().getSimpleName(), "*&*&* readDataForTrays use sample data");
                SampleData.createTrays();
                Log.d(this.getClass().getSimpleName(), "*&*&* readDataForTrays re-reading from sample data");
                readDataForTrays();
            }
        } else {
            writeDataForTrays();
        }
    }

    /**
     * Write trays.yaml document (reopens file and appends, auto adds ---)
     */
    void writeDataForTrays() {
        YamlWriter writer = new YamlWriter();
        String filePath = frontend.filesPublic + "/" + settingsYear + "trays.yaml";
        for( String name : trays.keySet()) {
            Log.d(this.getClass().getSimpleName(), "*&*&* writeDataForTrays(" + name + ", " + filePath + ")");
            Map<String, Object> o = trays.get(name);
            writer.writeYamlFile(filePath, o);
        }
    }
}
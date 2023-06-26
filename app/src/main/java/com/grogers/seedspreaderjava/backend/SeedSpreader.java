package com.grogers.seedspreaderjava.backend;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.grogers.seedspreaderjava.R;
import com.grogers.seedspreaderjava.frontend.SeedApplication;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
                } else {
                    Log.e(this.getClass().getSimpleName(), "*&*&* we don't know what this yaml is");
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
    public static ifAndroid instance = null;
    public static ifAndroid getInstance() {
        if (instance == null) {
            instance = new ifAndroid();
        }
        return instance;
    }
    public Context context = SeedApplication.getContext();

    /* Files */
    ///* /data/user/0/com.grogers.seedspreaderjava/seed.yaml */
    public File filesPrivateParent = context.getDataDir();
    ///* /data/user/0/com.grogers.seedspreaderjava/files/seed.yaml */
    public File filesPrivate = context.getFilesDir();
    // public
    ///*  /storage/emulated/0/Android/data/com.grogers.seedspreaderjava/files/Documents/seed.yaml */
    public File filesPublic = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
    public File imageFilesPublic = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

    /* Cache */
    ///* /data/user/0/com.grogers.seedspreaderjava/cache/seed.yaml */
    public File cachePrivate = context.getCacheDir();
    // public
    // /* /storage/emulated/0/Android/data/com.grogers.seedspreaderjava/cache/seed.yaml */
    public File cachePublic = context.getExternalCacheDir();

    public void getWriteBitmap(String resource, FileOutputStream fos) {
        if (resource == "sample-chili") BitmapFactory.decodeResource(context.getResources(), R.drawable.sample_chili).compress(Bitmap.CompressFormat.JPEG, 100, fos);
        else if (resource == "sample_chili_back") BitmapFactory.decodeResource(context.getResources(), R.drawable.sample_chili_back).compress(Bitmap.CompressFormat.JPEG, 100, fos);
        else if (resource == "sample_tomoato") BitmapFactory.decodeResource(context.getResources(), R.drawable.sample_tomato).compress(Bitmap.CompressFormat.JPEG, 100, fos);
        else if (resource == "sample_tomoato_back") BitmapFactory.decodeResource(context.getResources(), R.drawable.sample_tomato_back).compress(Bitmap.CompressFormat.JPEG, 100, fos);
    }
}
class SampleData {
    static public ifAndroid ifand = ifAndroid.getInstance();
    static public boolean seeds = false;
    static public boolean trays = false;
    static public boolean images = false;

    // maybe call flush before close
    static void createImages() {
        images = true;
        for (String image : List.of("sample_chili", "sample_chili_back")) {
            try {
                String filePath = ifand.imageFilesPublic + "/" + image + ".jpg";
                FileOutputStream fos = new FileOutputStream(filePath);
                ifAndroid.getInstance().getWriteBitmap("sample", fos);
                fos.close();
            } catch (IOException e) {
                Log.e(SampleData.class.getSimpleName(), "*&*&* Could not write sample data: " + e);
            }
        }
    }


    static void createSeeds() {
        seeds = true;
        String sample = "---\n" +
                "image: sweetnotpepper.jpg\n" +
                "year:\n" +
                "- 2023\n" +
                "name: Sweet Pepper\n" +
                "description: A sweet pepper plant for me\n" +
                "---\n" +
                "image: chilitpepper.jpg\n" +
                "year:\n" +
                "- 2023\n" +
                "name: Chili Pepper\n" +
                "description: A chili pepper plant for me woohoo!!\n";
        try {
            String filePath = ifand.filesPublic + "/" + "seeds.yaml";
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
                "name: Fruit Tray\n" +
                "description: A tray holding fruits\n" +
                "rows: 10\n" +
                "cols: 10\n" +
                "image: fruittray.jpg\n" +
                "year:\n" +
                "- 2023\n" +
                "contents\n" +
                "- -   name: Chili\n" +
                "      date: 2023\n" +
                "      event: planted\n" +
                "  -   name: Chili\n" +
                "      date: 2023\n" +
                "      event: seedling\n" +
                "- -   name: Sweet Pepper\n" +
                "      date: 2023\n" +
                "      event: planted\n";
        try {
            String filePath = ifand.filesPublic + "/" + "trays.yaml";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(sampleTrays);
            writer.close();
        } catch (IOException e) {
            Log.e(SampleData.class.getSimpleName(), "*&*&* Could not write sample data: " + e);
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
        readDataForSeeds();
        readDataForTrays();
        readImages();
    }

    /**
     * Public Fields
     */
    public ifAndroid ifand = ifAndroid.getInstance();
    /* table is thread safe HashMap is modern not thread safe */
    public Hashtable<String, Map<String, Object> > trays = new Hashtable<String, Map<String, Object> >();
    public Hashtable<String, Map<String, Object> > seeds = new Hashtable<String, Map<String, Object> >();
    public Hashtable<String, Bitmap> images = new Hashtable<String, Bitmap>();
    public boolean scale = true;

    /**
     * Read all data from filesystem
     *
     */
    void readDataForSeeds() {
        try {
            YamlReader reader = new YamlReader();
            String filePath = ifand.filesPublic + "/" + "seeds.yaml";
            Log.d(this.getClass().getSimpleName(), "*&*&* readDataForSeeds(" + filePath + ")");
            reader.readYamlFile(filePath, new YamlReader.Callback() {
                @Override
                public void onCallback(Map<String, Object> yamlData) {
                    seeds.put(yamlData.get("name").toString(), yamlData);
                }
            });
            Log.d(this.getClass().getSimpleName(), "*&*&* readDataForSeeds done, read " + seeds.size() + " seeds");
        } catch (FileNotFoundException e) {
            Log.d(this.getClass().getSimpleName(), "*&*&* There is no seeds.yaml file: " + e.toString());
        } catch (Exception e) {
            Log.d(this.getClass().getSimpleName(), "*&*&* There is an error with seeds.yaml file: " + e.toString());
        }
        if(seeds.size() == 0) {
            if (SampleData.seeds == false) {
                SampleData.createSeeds();
                readDataForSeeds();
            }
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

    /**
     * Read all data from filesystem
     *
     */
    void readImages() {
        try {
            File filePath = ifand.imageFilesPublic;
            Log.d(this.getClass().getSimpleName(), "*&*&* readImages(" + filePath + ")");
            File[] files = filePath.listFiles();
            for (File file : files) {
                FileInputStream fileInputStream = new FileInputStream(file);
                Log.d(this.getClass().getSimpleName(), "*&*&* readImages read " + file.getName());
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if(bitmap.getWidth() == 1000) {
                    images.put(file.getName(), bitmap);
                } else {
                    Log.d(this.getClass().getSimpleName(), "*&*&* Scaling image from " + bitmap.getWidth());
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 1000,1500, false);
                    images.put(file.getName(), scaledBitmap);
                }
            }
        } catch (FileNotFoundException e) {
            Log.d(this.getClass().getSimpleName(), "*&*&* There is no images: " + e.toString());
        } catch (Exception e) {
            Log.d(this.getClass().getSimpleName(), "*&*&* There is an error with images file: " + e.toString());
        }

        if(images.size() == 0) {
            if( SampleData.images == false) {
                SampleData.createImages();
                readImages();
            }
        }
        writeImages();
    }

    void writeImages() {
    }

    /**
     * Read all data from filesystem
     *
     */
    void readDataForTrays() {
        try {
            YamlReader reader = new YamlReader();
            String filePath = ifand.filesPublic + "/" + "trays.yaml";
            Log.d(this.getClass().getSimpleName(), "*&*&* readDataForTrays(" + filePath + ")");
            reader.readYamlFile(filePath, new YamlReader.Callback() {
                @Override
                public void onCallback(Map<String, Object> yamlData) {
                    trays.put(yamlData.get("name").toString(), yamlData);
                }
            });
            Log.d(this.getClass().getSimpleName(), "*&*&* readDataForTrays done, read " + trays.size() + " trays");
        } catch (FileNotFoundException e) {
            Log.d(this.getClass().getSimpleName(), "*&*&* There is no trays.yaml file: " + e.toString());
        } catch (Exception e) {
            Log.d(this.getClass().getSimpleName(), "*&*&* There is an error with trays.yaml file: " + e.toString());
        }
        if (trays.size() == 0) {
            if (SampleData.trays == false) {
                SampleData.createTrays();
                readDataForTrays();
            }
        }
        writeDataForTrays();
    }

    void writeDataForTrays() {
        YamlWriter writer = new YamlWriter();
        String filePath = ifand.filesPublic + "/" + "trays.yaml";
        for( String name : trays.keySet()) {
            Log.d(this.getClass().getSimpleName(), "*&*&* writeDataForTrays(" + name + ", " + filePath + ")");
            Map<String, Object> o = trays.get(name);
            writer.writeYamlFile(filePath, o);
        }
    }

}

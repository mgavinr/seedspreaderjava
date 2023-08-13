package com.grogers.seedspreaderjava.frontend;

import android.util.Log;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LanguageProcessor {
    static Map<String, String> iconList = Map.ofEntries(
            Map.entry("ddeath", "\u2620"),
            Map.entry("sseedling", "\uD83C\uDF31"),
            Map.entry("death", "💀"),
            Map.entry("corn", "🌽"),
            Map.entry("chili", "🌶"),
            Map.entry("pineapple", "🍍"),
            Map.entry("strawberry", "🍓"),
            Map.entry("carrot", "🥕"),
            Map.entry("planted2", "🌰"),
            Map.entry("seedling", "🌱"),
            Map.entry("top", "🔝"),
            Map.entry("transplant", "🏘"),
            Map.entry("bone", "🦴"),
            Map.entry("seedling2", "🌾"),
            Map.entry("cherry", "🍒"),
            Map.entry("planted", "🥔"),
            Map.entry("newplanted", "🥔"),
            Map.entry("nut", "🥜"),
            Map.entry("broccoli", "🥦"),
            Map.entry("cucumber", "🥬"),
            Map.entry("eggplant", "🍆"),
            Map.entry("avocado", "🥑"),
            Map.entry("coconut", "🥥"),
            Map.entry("tomato", "🍅"),
            Map.entry("kiwi", "🥝"),
            Map.entry("pear2", "🥭"),
            Map.entry("redApple", "🍎"),
            Map.entry("greenApple", "🍏"),
            Map.entry("pear", "🍐"),
            Map.entry("mandarin", "🍑"),
            Map.entry("lemon", "🍋"),
            Map.entry("orange", "🍊"),
            Map.entry("melon", "🍉"),
            Map.entry("tennis", "🍈"),
            Map.entry("grape", "🍇"),
            Map.entry("banana", "🍌"),
            Map.entry("blank", "🌑"),
            Map.entry("spare", "🌑"),
            Map.entry("spare2", "x")
    );

    static public String getDate() {
        LocalDate currentDate = LocalDate.now();
        Log.d(LanguageProcessor.class.getSimpleName(), "*&* Date=" + currentDate.toString());
        return currentDate.toString();
    }
    static public String getDate(int minus) {
        LocalDate currentDate = LocalDate.now();
        currentDate = currentDate.minusDays(minus);
        Log.d(LanguageProcessor.class.getSimpleName(), "*&* Date-=" + currentDate.toString());
        return currentDate.toString();
    }

    static public int getYear() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.getYear();
    }

    static public String getImageName(String seedName, String suffix) {
        String imageName = seedName.replace(" ", "_");
        imageName = imageName.toLowerCase();
        imageName = imageName.replace(".jpg", "");
        return imageName + suffix + ".jpg";
    }

    static public ArrayList<Integer> getRowCol(String rowcol, int maxRow, int maxCol) {
        // This tries to be a natural language interpreter for row col coords
        // it accepts rX as the row, and then cols follow, rX on it's own means nothing,
        // it's all about the cols, but you can say all, or * for all colls
        // row 1 is the first row
        // col 1 is the first col
        ArrayList<Integer> result = new ArrayList<Integer>();
        rowcol = rowcol.replace(" to", "to");
        rowcol = rowcol.replace("to ", "to");
        int currentRow = 1;
        boolean lastWasRow = false;
        for (String part : rowcol.split(" ")) {
            Log.d(LanguageProcessor.class.getSimpleName(), "*&* Language=[" + part + "] for row " + currentRow);
            try {
                if (part.contains("r")) {
                    // check if we need to add stuff for last element, lastWasRow but no colls
                    if (lastWasRow) {
                        // we have to enter all colls for the previous last
                        for (int i = 0; i < maxCol; ++i) {
                            result.add(i);
                        }
                    }
                    lastWasRow = true;

                    // parse this row number
                    Log.d(LanguageProcessor.class.getSimpleName(), "*&* parserow=>" + part);
                    String row = part.replace("r", "");
                    currentRow = Integer.parseInt(row);
                    if (currentRow < 1) currentRow = 1;
                    result.add(-currentRow);
                    Log.d(LanguageProcessor.class.getSimpleName(), "*&* parserow<=" + part);
                } else {
                    lastWasRow = false;
                    Log.d(LanguageProcessor.class.getSimpleName(), "*&* parsecol=>" + part);
                    if ((part.equals("*")) || (part.equals("all"))) {
                        for (int i = 0; i < maxCol; ++i) {
                            result.add(i);
                        }
                    } else if (part.contains("to")) {
                        String[] twoParts = part.split("to");
                        Integer start = Integer.parseInt(twoParts[0].trim()) - 1;
                        if (start < 0) start = 0;
                        Integer end = Integer.parseInt(twoParts[1].trim()) - 1;
                        if (end < 0) end = 0;
                        for (int i = start; i <= end; ++i) {
                            result.add(i);
                        }
                    } else {
                        for (String col : part.split(" ")) {
                            Integer start = Integer.parseInt(col.trim()) - 1;
                            if (start < 0) start = 0;
                            result.add(start);
                        }
                    }
                    Log.d(LanguageProcessor.class.getSimpleName(), "*&* parsecol<=" + part);
                }
            } catch (Exception e) {
                Log.d(LanguageProcessor.class.getSimpleName(), "*&* parse error " + e.toString());
            }
        }
        if (lastWasRow == true) {
            for (int i = 0; i < maxCol; ++i) {
                result.add(i);
            }
        }
        Log.d(LanguageProcessor.class.getSimpleName(), "*&* The rowCol interpreter has taken input [" + rowcol + "] to mean [" + result.toString() + "]");
        return result;
    }

    static public String getContents(IBackend backend) {
        String multiLineE= "";
        boolean start = true;

        String icon = null;
        String iconSpare = iconList.get("spare");
        List<String> keys = new ArrayList<String>(backend.tray.keySet());
        Collections.sort(keys);
        for(String key : keys) {
            if (key.contains("content_")) {
                if(!start) multiLineE = multiLineE + "\n";
                start = false;
                ArrayList<?> rowContent = (ArrayList<?>) backend.tray.get(key);
                if (rowContent != null) {
                    for (Object colContent : rowContent) {
                        Map<String, Object> col = (Map<String, Object>) colContent;
                        icon = iconList.get(col.get("event"));
                        if (icon == null) {
                            icon = iconSpare;
                        }
                        multiLineE = multiLineE + icon;
                    }
                }
            }
        }
        Log.d(LanguageProcessor.class.getSimpleName(), "*&* " + multiLineE);
        return multiLineE;
    }

    // TODO we want to compress this, and put more human language in it
    // suggestions, statistics, temperatures
    static public TreeMap<String, String> getContentsPerSeed(IBackend backend) {
        // Create: Seed list
        TreeMap<String, String> seedList = new TreeMap<>();
        List<String> keys = new ArrayList<String>(backend.tray.keySet());
        Collections.sort(keys);
        int row = 0;
        for (String key : keys) {
            if (key.contains("content_")) {
                ++row;
                ArrayList<?> rowContent = (ArrayList<?>) backend.tray.get(key);
                if (rowContent != null) {
                    int col = 0;
                    for (Object colContent : rowContent) {
                        ++col;
                        Map<String, Object> colmap = (Map<String, Object>) colContent;
                        String seedName = (String) colmap.get("name");
                        String event = (String) colmap.get("event");
                        String date = (String) colmap.get("date");
                        String seedInfo = "[" + row + "," + col + "] " + event + " on " + date + ".\n";
                        if (seedList.containsKey(seedName)) {
                            String existingSeedInfo = seedList.get(seedName);
                            seedList.put(seedName, existingSeedInfo + seedInfo);
                        } else {
                            seedList.put(seedName, seedInfo);
                        }
                    }
                }
            }
        }
        return seedList;
    }
}


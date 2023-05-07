package com.grogers.seedspreaderjava;

import java.util.Hashtable;

public class SeedSpreader {
    static SeedSpreader instance = null;
    static SeedSpreader getInstance() {
        if (instance == null) {
            instance = new SeedSpreader();
        }
        return instance;
    }
    // Note I use _ to denote priv^H^H sorry members used the most and outside
    Hashtable<String, String> _trays = new Hashtable<String, String>();



}

package com.grogers.seedspreaderjava;

import java.util.ArrayList;

class Seed {
    public String name;
    public int start_row;
    public int start_col;
    public int end_row;
    public int end_col;
    public int size;
}
public class SeedTray {
    String name;
    int width;
    int height;
    int year;
    ArrayList<Seed> seedList;

}

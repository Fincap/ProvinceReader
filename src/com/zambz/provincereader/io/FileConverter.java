package com.zambz.provincereader.io;

import java.io.File;

/**
 * Singleton that parses through an image file and generates a province graph based off pixel colour.
 */
public class FileConverter {
    private static FileConverter ourInstance = new FileConverter();

    public static FileConverter getInstance() {
        return ourInstance;
    }

    private File provinceMap;

    private FileConverter() {

    }

    public void setProvinceMap(String path) {
        this.provinceMap = new File(path);
    }

    public File getProvinceMap() {
        return provinceMap;
    }
}

package com.zambz.provincereader;

import com.zambz.provincereader.io.Debugger;
import com.zambz.provincereader.io.FileConverter;

/**
 * Main class
 */
public class ProvinceReader {

    private FileConverter fileConverter;

    public ProvinceReader() {
        fileConverter = FileConverter.getInstance();
    }

    public static void main(String[] args) {
        Debugger.log("Province reader");
    }
}

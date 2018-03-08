package com.zambz.provincereader;

import com.zambz.provincereader.io.Debugger;
import com.zambz.provincereader.io.FileConverter;

import java.io.File;
import java.io.IOException;

/**
 * Main class
 */
public class ProvinceReader {

	private FileConverter fileConverter;

	private String provinceFile = "map/provinces.png";	//Temporary placeholder reference. Will take input/auto-detect.

	public ProvinceReader() {
		fileConverter = FileConverter.getInstance();
		fileConverter.setProvinceMap(provinceFile);

		//Try generate pixel array/read BufferedImage from file
		try {
			fileConverter.generatePixelArray();
		} catch (IOException e) {
			Debugger.log("I/O exception generating pixel array");
			Debugger.log(e.getLocalizedMessage());
		}
	}

	public static void main(String[] args) {
		ProvinceReader reader = new ProvinceReader();
	}
}

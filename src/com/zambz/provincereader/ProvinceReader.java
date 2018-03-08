package com.zambz.provincereader;

import com.zambz.provincereader.io.Debugger;
import com.zambz.provincereader.io.FileConverter;

import java.io.File;

/**
 * Main class
 */
public class ProvinceReader {

	private FileConverter fileConverter;

	private File provinceFile;	//Temporary placeholder reference. Will take input/auto-detect.

	public ProvinceReader() {
		fileConverter = FileConverter.getInstance();
		
		//Try loading file
		try {
			provinceFile = new File("map/provinces.png");
			Debugger.log(provinceFile.toString() + (provinceFile.exists() ? " exists" : " doesn't exist"));
		} catch (Exception e) {
			Debugger.log("Unable to load province file: " + provinceFile.toString());
			Debugger.log(e.getLocalizedMessage());
		}
	}

	public static void main(String[] args) {
		ProvinceReader reader = new ProvinceReader();
	}
}

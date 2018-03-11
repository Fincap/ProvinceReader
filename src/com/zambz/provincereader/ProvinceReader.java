package com.zambz.provincereader;

import com.zambz.provincereader.io.Debugger;
import com.zambz.provincereader.province.ProvinceMap;

import java.io.IOException;

/**
 * Main class
 */
public class ProvinceReader {

	public static String provinceFile = "map/provinces.png";	//Temporary placeholder reference. Will take input/auto-detect in future.

	private ProvinceMap map;

	public ProvinceReader(String provincePath) {
		this.map = new ProvinceMap(provincePath);
		try {
			this.map.generatePixelArray();
		} catch (IOException e) {
			Debugger.log("I/O Exception when trying to read file");
			Debugger.log(e.getLocalizedMessage());
		}
		this.map.parsePixels();
	}

	public static void main(String[] args) {
		new ProvinceReader(ProvinceReader.provinceFile);
	}
}

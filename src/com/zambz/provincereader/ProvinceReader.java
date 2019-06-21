package com.zambz.provincereader;

import com.zambz.debugger.Debugger;
import com.zambz.provincereader.province.ProvinceMap;

import java.io.IOException;

/**
 * Object that parses an image file and generates a map from it, containing provinces and the adjacencies between provinces.
 */
public class ProvinceReader {

	private ProvinceMap map;

	/**
	 * Constructor reads the province file path, generates the map instance, and strips the data from the image file.
	 * @param provincePath A string describing the location of the province image file.
	 */
	public ProvinceReader(String provincePath) {
		this.map = new ProvinceMap(provincePath);
		try {
			this.map.generatePixelArray();
		} catch (IOException e) {
			Debugger.log("I/O Exception when trying to read file");
			Debugger.log(e.getLocalizedMessage());
		}
	}

	/**
	 * @return Instance of map.
	 */
	public ProvinceMap getMap() {
		return map;
	}

	/**
	 * Generates provinces and adjacencies from the pixel data of the map.
	 */
	public void parseMap() {
		this.map.parsePixels();
	}

	/**
	 * Generates an image file graph representation of the province map.
	 * @param path Destination of the file (not including file extension).
	 * @param scale Integer upwards scale of the output image file relative to the original province image file.
	 */
	public void drawMap(String path, int scale) {
		this.map.drawGraph(path, scale);
	}

	public static void main(String[] args) {
		ProvinceReader reader = new ProvinceReader("map/provinces1.png");
		reader.parseMap();
		reader.drawMap("map/graph1", 26);
	}
}

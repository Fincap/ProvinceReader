package com.zambz.provincereader.io;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

/**
 * Singleton that parses through an image file and generates a province graph based off pixel colour.
 */
public class FileConverter {

	private static FileConverter ourInstance = new FileConverter();
	public static FileConverter getInstance() {
		return ourInstance;
	}

	private File provinceMap;
	private int[] pixels;

	public boolean setProvinceMap(String path) {
		provinceMap = new File(path);
		Debugger.log(provinceMap.toString() + (provinceMap.exists() ? " exists" : " doesn't exist"));
		if(provinceMap.exists()) return true;
		return false;
	}

	//TODO Fix this method. Byte array only gives -1, 0, or 1, and it can't be casted to an int array.
	public void generatePixelArray() throws IOException {
		BufferedImage bi = ImageIO.read(this.provinceMap);
		this.pixels = new int[bi.getWidth() * bi.getHeight()];
		Raster raster = bi.getRaster();
		raster.getPixels(0, 0, bi.getWidth(), bi.getHeight(), this.pixels);
		Debugger.log("Successfully generated pixel array");
		for (int pixel : this.pixels) {
			System.out.println(pixel);
		}
	}


}

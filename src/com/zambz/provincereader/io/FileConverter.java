package com.zambz.provincereader.io;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
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
	private byte[] pixels;

	public boolean setProvinceMap(String path) {
		provinceMap = new File(path);
		Debugger.log(provinceMap.toString() + (provinceMap.exists() ? " exists" : " doesn't exist"));
		if(provinceMap.exists()) return true;
		return false;
	}

	//TODO Fix this method. Byte array only gives -1, 0, or 1, and it can't be casted to an int array.
	public void generatePixelArray() throws IOException {
		BufferedImage bi = ImageIO.read(this.provinceMap);
		this.pixels = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
		Debugger.log("Successfully generated pixel array");
		for (byte pixel : this.pixels) {
			System.out.println(pixel);
		}
	}


}

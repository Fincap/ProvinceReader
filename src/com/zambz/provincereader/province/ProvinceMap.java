package com.zambz.provincereader.province;

import com.zambz.provincereader.io.Debugger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

/**
 * Graph
 * The entire map of data containing the province list and adjacency list, as well as other information
 * about the whole map.
 * Also includes the code to convert an image file into a pixel array, and the parsing of colour data into graph information.
 */
public class ProvinceMap {

	private File provinceFile;
	private int[] pixels;

	public ProvinceMap(String provincePath) {
		//Try to load image file
		Debugger.log("Loading map file: " + provincePath);
		this.provinceFile = new File(provincePath);
		Debugger.log(this.provinceFile.toString() + (this.provinceFile.exists() ? " exists" : " doesn't exist! This map will not be able to read pixel data!"));
	}

	public void generatePixelArray() throws IOException {
		BufferedImage fileImg = ImageIO.read(this.provinceFile);
		BufferedImage newImg = new BufferedImage(fileImg.getWidth(), fileImg.getHeight(), BufferedImage.TYPE_INT_RGB);

		Graphics2D g = newImg.createGraphics();
		g.drawImage(fileImg, 0, 0, null);
		g.dispose();
		this.pixels = ((DataBufferInt) newImg.getRaster().getDataBuffer()).getData();
	}

}

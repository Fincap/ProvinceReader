package com.zambz.provincereader.province;

import com.zambz.provincereader.io.Debugger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Graph
 * The entire map of data containing the province list and adjacency list, as well as other information
 * about the whole map.
 * Also includes the code to convert an image file into a pixel array, and the parsing of colour data into graph information.
 */
public class ProvinceMap {

	private File provinceFile;
	private int[] pixels;
	private int mapWidth, mapHeight;
	private HashMap<Integer, Province> provinces;
	private HashMap<Integer, Adjacency> adjacencies;

	public ProvinceMap(String provincePath) {
		this.provinces = new HashMap<>();
		//Try to load image file
		Debugger.log(String.format("Loading map file: %s", provincePath));
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
		this.mapWidth = newImg.getWidth();
		this.mapHeight = newImg.getHeight();
		Debugger.log(String.format("Province map (%s|%dx%d) successfully loaded", this.provinceFile.toString(), newImg.getWidth(), newImg.getHeight()));
	}

	/**
	 * Procedure:
	 * 1. Check if colour already belongs to a province.
	 * -> If not, create a new province.
	 * 2. Add the pixel to the province point list.
	 * 3. Once all provinces are generated, calculate each province vertex, then run again to calculate adjacencies.
	 * 5. Check the surrounding pixels.
	 * -> If it is the same colour, ignore the pixel.
	 * -> If it's a different colour, check if the pixel belongs to the province's neighbour.
	 *     -> If it doesn't belong to a neighbour, create a new neighbour relationship between the two provinces, using the absolute distance between two province vertices as weight.
	 */
	public void parsePixels() {
		//Initial parse - Generates provinces
		Debugger.log("Calculating provinces...");
		for (int y = 0; y < this.mapHeight; y++) {
			for (int x = 0; x < this.mapWidth; x++) {
				int pixel = this.pixels[x + y * this.mapWidth];
				if (!provinces.containsKey(pixel)) {
					provinces.put(pixel, new Province(pixel, x, y));
				} else {
					provinces.get(pixel).addPoint(x, y);
				}
			}
		}

		//Calculate province vertices
		Debugger.log("Calculating vertices...");
		for (Province province : provinces.values()) province.calculateVertex();

		//Second parse - Calculates adjacencies
		//TODO Issues: OOB Exception, last pixel of column will search first pixel of column on next row
		Debugger.log("Calculating adjacencies...");
		for (int y = 0; y < this.mapHeight; y++) {
			for (int x = 0; x < this.mapWidth; x++) {
				int pixel = this.pixels[x + y * this.mapWidth];
				int otherPixel = this.pixels[(x + 1) + y * this.mapWidth]; // out of bounds
				Province prov = this.provinces.get(pixel);
				Province otherProv = this.provinces.get(otherPixel);
				if (pixel != otherPixel) {
					if (!prov.containsAdjacency(otherPixel)) {
						//Calculate absolute distance between points - Sqrt ( (p1.x - p2.x)^2 + (p1.y - p2.y)^2 )
						int distance = (int) Math.sqrt(Math.pow(prov.getVertex().getX() - otherProv.getVertex().getX(), 2) + Math.pow(prov.getVertex().getY() - otherProv.getVertex().getY(), 2));
						prov.addAdjacency(new Adjacency(prov, otherProv, distance));
					}
				}
			}
		}
	}

}

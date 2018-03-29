package com.zambz.provincereader.province;

import com.zambz.provincereader.io.Debugger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
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

	/**
	 * @param provincePath File path to generate map from.
	 */
	public ProvinceMap(String provincePath) {
		this.provinces = new HashMap<>();
		this.adjacencies = new HashMap<>();
		//Try to load image file
		Debugger.log(String.format("Loading map file: %s", provincePath));
		this.provinceFile = new File(provincePath);
		Debugger.log(this.provinceFile.toString() + (this.provinceFile.exists() ? " exists" : " doesn't exist! This map will not be able to read pixel data!"));
	}

	/**
	 * Strips the image file of pixel data and generates an integer array of pixel colour data.
	 * @throws IOException
	 */
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

	/*
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
	/**
	 * Parses the pixel data array and generates appropriate provinces, adjacencies, and province types.
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

		//Calculate province vertices and sea provinces
		Debugger.log("Calculating vertices...");
		for (Province province : provinces.values()) province.calculateVertex();

		Debugger.log("Calculating Sea provinces...");
		for (Province province : provinces.values()) province.calculateSeaProvince();

		//Second parse - Calculates adjacencies
		Debugger.log("Calculating adjacencies...");
		for (int y = 0; y < this.mapHeight - 1; y++) {
			for (int x = 0; x < this.mapWidth - 1; x++) {
				int xMod = 1;		// These modifiers are basically alternating between the two loops to check first the province to the right, then the province below.
				int yMod = 0;
				int pixel = this.pixels[x + y * this.mapWidth];
				for (int i = 0; i < 2; i++) {
					int otherPixel = this.pixels[(x + xMod) + (y + yMod) * this.mapWidth];
					Province prov = this.provinces.get(pixel);
					Province otherProv = this.provinces.get(otherPixel);
					if (pixel != otherPixel) {
						if (!this.adjacencies.containsKey(Adjacency.createHash(pixel, otherPixel))) {
							//Calculate absolute distance between points - Sqrt ( (p1.x - p2.x)^2 + (p1.y - p2.y)^2 )
							int distance = (int) Math.sqrt(Math.pow(prov.getVertex().getX() - otherProv.getVertex().getX(), 2) + Math.pow(prov.getVertex().getY() - otherProv.getVertex().getY(), 2));
							Adjacency adj = new Adjacency(prov, otherProv, distance);
							prov.addAdjacency(adj);
							this.adjacencies.put(adj.hashCode(), adj);
						}
					}
					xMod = 0;
					yMod = 1;
				}
			}
		}
	}

	/**
	 * Generates an image file graph representation of the province map.
	 * @param path Destination of the file (not including file extension).
	 * @param scale Integer upwards scale of the output image file relative to the original province image file.
	 */
	public void drawGraph(String path, int scale) {
		int offset = scale / 2;		// Offset for correctly positioning the circle render

		Debugger.log("Generating image");
		BufferedImage bi = new BufferedImage(this.mapWidth * scale, this.mapHeight * scale, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();

		g.setPaint(Color.WHITE);
		g.fillRect(0, 0, this.mapWidth * scale, this.mapHeight* scale);
		g.setPaint(Color.BLACK);

		for (Adjacency adjacency : this.adjacencies.values()) {
			if (adjacency.isSeaConnection()) g.setPaint(Color.BLUE); else g.setPaint(Color.BLACK);
			g.draw(new Line2D.Double(adjacency.getOne().getVertex().getX() * scale + offset, adjacency.getOne().getVertex().getY() * scale + offset,
					adjacency.getTwo().getVertex().getX() * scale + offset, adjacency.getTwo().getVertex().getY() * scale + offset));
		}

		for (Province province : this.provinces.values()) {
			if (province.isSeaProvince()) g.setPaint(Color.BLUE); else g.setPaint(Color.BLACK);
			g.fill(new Ellipse2D.Double(province.getVertex().getX() * scale, province.getVertex().getY() * scale, scale, scale));
		}

		try {
			ImageIO.write(bi, "PNG", new File(path + ".png"));
			Debugger.log("Graph written to: map/graph.png");
		} catch (IOException e) {
			Debugger.log("Failed to write to file");
			Debugger.log(e.getLocalizedMessage());
		}
	}

	/**
	 * @return This object's provinces HashMap.
	 */
	public HashMap<Integer, Province> getProvinces() {
		return provinces;
	}

	/**
	 * @param x x value of pixel.
	 * @param y y value of pixel.
	 * @return Province located at given position
	 */
	public Province getProvince(int x, int y) {
		return this.provinces.get(this.pixels[x + y * this.mapWidth]);
	}

	/**
	 * @param colour Colour of desired province.
	 * @return Province of specified colour.
	 */
	public Province getProvince(int colour) {
		return this.provinces.get(colour);
	}
}

package com.zambz.provincereader.province;

import com.zambz.provincereader.io.Debugger;

import java.awt.*;
import java.util.ArrayList;

/**
 * Vertex
 * Individual province class, defining the province's data, as well as containing the vertex and
 * the adjacent provinces.
 */
public class Province {

	private int colour;
	private Point vertex;
	private ProvinceType type;
	private ArrayList<Integer> xPositions;
	private ArrayList<Integer> yPositions;
	private ArrayList<Adjacency> adjacencies;

	/**
	 * Initialises the province and adds the starting point to positions lists.
	 * @param colour Province's assigned colour.
	 * @param startX Province's starting x.
	 * @param startY Province's starting y.
	 */
	public Province(int colour, int startX, int startY) {
		this.colour = colour;
		this.xPositions = new ArrayList<>();
		this.yPositions = new ArrayList<>();
		this.adjacencies = new ArrayList<>();
		this.addPoint(startX, startY);

		Debugger.log(String.format("New province added: 0x%08X", this.colour));
	}

	/**
	 * Adds a point to the province's positions list.
	 * @param x Point's x position.
	 * @param y Point's y position.
	 */
	public void addPoint(int x, int y) {
		this.xPositions.add(x);
		this.yPositions.add(y);

		//Debugger.log(String.format("Point (%d, %d) added to province 0x%08X", x, y, this.colour));
	}

	/**
	 * Adds adjacency object to this province's adjacencies.
	 * @param adj Adjacency to add.
	 */
	public void addAdjacency(Adjacency adj) {
		if (this.adjacencies.contains(adj)) return;

		this.adjacencies.add(adj);
	}

	/**
	 * @param adj Adjacency to check.
	 * @return Returns true if adjacency is in the province's adjacencies.
	 */
	public boolean containsAdjacency(Adjacency adj) {
		return this.adjacencies.contains(adj);
	}

	/**
	 * @param colour Province colour to check if adjacent.
	 * @return Returns true if province is adjacent.
	 */
	public boolean containsAdjacency(int colour) {
		for (Adjacency adj : this.adjacencies) {
			if (adj.hasProvince(this) && adj.getOther(this).colour == colour) return true;
		}

		return false;
	}

	/**
	 * @param prov Province to check if adjacent.
	 * @return Returns true if province is adjacent.
	 */
	public boolean containsAdjacency(Province prov) {
		return this.containsAdjacency(prov.getColour());
	}

	/**
	 * @param index Position in province's adjacency list to check.
	 * @return Adjacency at specified index.
	 */
	public Adjacency getAdjacency(int index) {
		return this.adjacencies.get(index);
	}

	/**
	 * @return Province's colour.
	 */
	public int getColour() {
		return colour;
	}

	/**
	 * @return Position of province's vertex.
	 */
	public Point getVertex() {
		return vertex;
	}

	/**
	 * @return Type of province.
	 */
	public ProvinceType getType() {
		return type;
	}

	/**
	 * @return Returns true if is sea province.
	 */
	public boolean isSeaProvince() {
		return this.type == ProvinceType.SEA;
	}

	/**
	 * @return Returns true if adjacent province is sea province.
	 */
	public boolean isCoastal() {
		for (Adjacency adj : this.adjacencies) if (adj.isSeaConnection()) return true;
		return false;
	}

	/**
	 * @return List of all adjacencies to province.
	 */
	public ArrayList<Adjacency> getAdjaecencies() {
		return new ArrayList<>(this.adjacencies);
	}

	/**
	 * Manually moves vertex of province to given position.
	 * @param x x position of new vertex.
	 * @param y y position of new vertex.
	 */
	public void nudgeVertex(int x, int y) {
		this.vertex.setLocation(x, y);
	}
	
	//Currently calculates centroid. Fine for time being, but eventually need to calculate point of isolation (or faster alternative)

	/**
	 * Calculates the centroid of all points in province's positions list and sets the province's vertex to the centroid.
	 */
	public void calculateVertex() {
		int xAvg = 0;
		int yAvg = 0;
		for (int x : this.xPositions) xAvg += x;
		for (int y : this.yPositions) yAvg += y;
		xAvg = xAvg / this.xPositions.size();
		yAvg = yAvg / this.yPositions.size();
		this.vertex = new Point(xAvg, yAvg);
		Debugger.log(String.format("Province %d vertex calculated at: %d, %d", this.colour, xAvg, yAvg));
	}

	/**
	 * Detects if the province is a sea province based off RGB data. If B > (R | G) then it is a sea province.
	 */
	public void calculateSeaProvince() {
		int r = (this.colour >> 16) & 0xFF;
		int g = (this.colour >> 8) & 0xFF;
		int b = this.colour & 0xFF;

		this.type = (b > r && b > g) ? ProvinceType.SEA : ProvinceType.LAND;
	}

	@Override
	public int hashCode() {
		return this.colour;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Province)) return false;

		Province p = (Province) obj;
		return this.colour == p.colour;
	}

	@Override
	public String toString() {
		return String.format("0x%08X", this.colour);
	}
}

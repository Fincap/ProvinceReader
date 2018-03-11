package com.zambz.provincereader.province;

import com.zambz.provincereader.io.Debugger;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Vertex
 * Individual province class, defining the province's data, as well as containing the vertex and
 * the adjacent provinces.
 */
public class Province {

	private int colour;
	private Point vertex;
	private ArrayList<Integer> xPositions;
	private ArrayList<Integer> yPositions;
	private ArrayList<Adjacency> adjaecencies;

	public Province(int colour, int startX, int startY) {
		this.colour = colour;
		this.xPositions = new ArrayList<>();
		this.yPositions = new ArrayList<>();
		this.adjaecencies = new ArrayList<>();
		this.addPoint(startX, startY);

		Debugger.log(String.format("New province added: 0x%08X", this.colour));
	}

	public void addPoint(int x, int y) {
		this.xPositions.add(x);
		this.yPositions.add(y);

		//Debugger.log(String.format("Point (%d, %d) added to province 0x%08X", x, y, this.colour));
	}
	
	public void addAdjacency(Adjacency adj) {
		if (this.adjaecencies.contains(adj)) return;

		this.adjaecencies.add(adj);
	}

	public boolean containsAdjacency(Adjacency adj) {
		return this.adjaecencies.contains(adj);
	}

	public boolean containsAdjacency(int colour) {
		for (Adjacency adj : this.adjaecencies) {
			if (adj.hasProvince(this) && adj.getOther(this).colour == colour) return true;
		}

		return false;
	}

	public boolean containsAdjacency(Province prov) {
		return this.containsAdjacency(prov.getColour());
	}

	public Adjacency getAdjacency(int index) {
		return this.adjaecencies.get(index);
	}
	
	public int getColour() {
		return colour;
	}

	public Point getVertex() {
		return vertex;
	}

	public ArrayList<Adjacency> getAdjaecencies() {
		return new ArrayList<>(this.adjaecencies);
	}
	
	//Currently calculates centroid. Fine for time being, but eventually want to calculate point of isolation (or faster alternative)
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

package com.zambz.provincereader.province;

/**
 * Edge
 * Defines the physical relationship between two provinces
 */
public class Adjacency {
	
	private Province one, two;
	private int weight;
	
	public Adjacency(Province one, Province two, int weight) {
		// One/two positions organised by "size" of colour
		this.one = (one.getColour() < two.getColour()) ? one : two;
		this.two = (this.one == one) ? two : one;
		this.weight = weight;
	}
	
	//TODO: Hashcode based on one and two colours. Compare method.
}

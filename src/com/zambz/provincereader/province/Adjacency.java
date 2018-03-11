package com.zambz.provincereader.province;

import com.zambz.provincereader.io.Debugger;

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

		Debugger.log(String.format("New adjacency added between %s and %s (weight %d)", this.one.toString(), this.two.toString(), this.weight));
	}

	public Province getOne() {
		return one;
	}

	public Province getTwo() {
		return two;
	}

	public Province getOther(Province province) {
		return (this.one == province) ? this.two : this.one;
	}

	public boolean hasProvince(Province province) {
		return this.one == province || this.two == province;
	}

	public int getWeight() {
		return weight;
	}

	@Override
	public int hashCode() {
		return (this.one.getColour() + this.two.getColour());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Adjacency)) return false;

		Adjacency a = (Adjacency) obj;
		return a.one.equals(this.one) && a.two.equals(this.two);
	}
}

package com.zambz.provincereader.province;

import com.zambz.provincereader.io.Debugger;

/**
 * Edge
 * Defines the physical relationship between two provinces
 */
public class Adjacency {
	
	private Province one, two;
	private int weight;
	private ProvinceType type;
	
	public Adjacency(Province one, Province two, int weight) {
		// One/two positions organised by "size" of colour
		this.one = (one.getColour() < two.getColour()) ? one : two;
		this.two = (this.one == one) ? two : one;
		this.weight = weight;
		this.type = (this.one.isSeaProvince() || this.two.isSeaProvince()) ? ProvinceType.SEA : ProvinceType.LAND;

		Debugger.log(String.format("New adjacency added between %s and %s (weight %d)\t\tSea: %b\t\tHASH: %d", this.one.toString(), this.two.toString(), this.weight, this.isSeaConnection(), this.hashCode()));
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

	public ProvinceType getType() {
		return type;
	}

	public boolean isSeaConnection() {
		return this.type == ProvinceType.SEA;
	}

	@Override
	public int hashCode() {
		return (this.one.getColour() * 13 + this.two.getColour() - 1) >> 2;
	}

	public static int createHash(int one, int two) {
		int newOne = (one < two) ? one : two;
		int newTwo = (newOne == one) ? two : one;
		return (newOne * 13 + newTwo - 1) >> 2;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Adjacency)) return false;

		Adjacency a = (Adjacency) obj;
		return a.one.equals(this.one) && a.two.equals(this.two);
	}
}

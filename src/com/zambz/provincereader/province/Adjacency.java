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

	/**
	 * Constructor for adjacency, orders the two ends of the adjacency smallest->largest based on colour integer size.
	 * @param one Province at one end of adjacency.
	 * @param two Province at other end of adjacency.
	 * @param weight Weight of the adjacency.
	 */
	public Adjacency(Province one, Province two, int weight) {
		// One/two positions organised by "size" of colour
		this.one = (one.getColour() < two.getColour()) ? one : two;
		this.two = (this.one == one) ? two : one;
		this.weight = weight;
		this.type = (this.one.isSeaProvince() || this.two.isSeaProvince()) ? ProvinceType.SEA : ProvinceType.LAND;

		Debugger.log(String.format("New adjacency added between %s and %s (weight %d)\t\tSea: %b\t\tHASH: %d", this.one.toString(), this.two.toString(), this.weight, this.isSeaConnection(), this.hashCode()));
	}

	/**
	 * @return First province at end of adjacency.
	 */
	public Province getOne() {
		return one;
	}

	/**
	 * @return Second province at end of adjacency.
	 */
	public Province getTwo() {
		return two;
	}

	/**
	 * @param province Province at one end of the adjacency
	 * @return Province at other end of the adjacency.
	 */
	public Province getOther(Province province) {
		return (this.one == province) ? this.two : this.one;
	}

	/**
	 * @param province Province to check
	 * @return Returns true if province is at either end of adjacency.
	 */
	public boolean hasProvince(Province province) {
		return this.one == province || this.two == province;
	}

	/**
	 * @return Weight of adjacency.
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @return Province's type.
	 */
	public ProvinceType getType() {
		return type;
	}

	/**
	 * @return Returns true if province connects two sea provinces, or a land province to a sea province.
	 */
	public boolean isSeaConnection() {
		return this.type == ProvinceType.SEA;
	}

	@Override
	public int hashCode() {
		return (this.one.getColour() * 13 + this.two.getColour() - 1) >> 2;
	}

	/**
	 * @param one Colour of province one.
	 * @param two Colour of province two.
	 * @return Hash value of two provinces.
	 */
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

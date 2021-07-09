package com.uriegas;
/**
 * A class representing a centroid <p>
 * A centroid is a point in the eucleadian space,
 * it is a n-dimensional point.
 * TODO: methods for comparition
 */
public class Centroid {
	private double[] position;
	public Centroid(double... x ){
		position = x;
	}
	public double[] get_centroid(){
		return position;
	}
}

package com.uriegas;

import java.io.*;
import java.util.*;

/**
 * Implementation of the Kmeans++ algorithm
 * @author Eduardo Uriegas
 */
public class Kmeans {
	/**
	 * Kmeans implementation without paralellism <p>
	 * Based on: {@link https://medium.com/geekculture/implementing-k-means-clustering-with-k-means-initialization-in-python-7ca5a859d63a}
	 * TODO: Add more dimensions handling
	 * @param data
	 * @param centroids the centroids ej. {@code (2, 3); (4, 5, 1)}
	 * @param k the number of clusters
	 * @return a list of centroids with the new positions
	 */
	public List<Centroid> k_means_plus_plus(ArrayList<List<Double>> data, int k){
		// Pseudocode:
		List<Centroid> centroids = get_random_centroids(data, k); //Random centroids
		// List<Centroid> centroids = get_centroids(data, k); //Kmeans++ centroids

		// while()
		return centroids;
	}
	/**
	 * The kmeans++ innovation is the algorithm for getting the centroids
	 * {@link http://ilpubs.stanford.edu:8090/778/1/2006-13.pdf}
	 * @param data each List<Double> is a row
	 * @param k the number of clusters
	 */
	public List<Centroid> get_centroids(ArrayList<List<Double>> data, int k){
		List<Centroid> centroids = new ArrayList<Centroid>();
		//TODO algorithm 
		return centroids;
	}
	/**
	 * Get random number of centroids. Old Kmeans implementation
	 * TODO: test
	 * @param data the data limits
	 * @param k number of desired groups
	 * @return list of centroids
	 */
	public List<Centroid> get_random_centroids(ArrayList<List<Double>> data, int k){
		Random rand = new Random();
		List<Double> dimensions = new ArrayList<Double>();
		List<Centroid> centroids = new ArrayList<Centroid>();

		for(int i = 0; i < k; i++){//Number of centroids
			for(int j = 0; j < data.get(0).size(); j++)//Number of dimensions of the centroid
				dimensions.add(rand.nextDouble());
			centroids.add(new Centroid(rand.nextDouble()));
			dimensions.clear();
		}
		return centroids;
	}
	//-->Kmeans for 2D datasets
	/**
	 * Kmeans implementation using a 2D dataset and without threading
	 * @param data the 2D dataset
	 * @param k the number of clusters
	 * @return a list of centroids with the new positions
	 */
	public static List<Point2D> classicKmeans(List<Point2D> data, int k){
		List<Point2D> centers = new ArrayList<Point2D>();
		boolean converge;
		do{
			List<Point2D> newCenters = getNewCenters(data, centers);
			double distance = getDistance(centers, newCenters);
			centers = newCenters;
			converge = distance == 0;
		}while(!converge);
		return centers;
	}
	public static List<Point2D> initializeRandomCenters(int n, int low, int high){
		List<Point2D> points = new ArrayList<Point2D>(n);
		for(int i = 0; i < n; i++){
			Double x = Math.random() * (high - low) + low;
			Double y = Math.random() * (high - low) + low;
			points.add(new Point2D(x, y));
		}
		return points;
	}
	public static List<Point2D> getNewCenters(List<Point2D> dataset, List<Point2D> centers){
		List<List<Point2D>> clusters = new ArrayList<>(centers.size());
		for (int i = 0; i < centers.size(); i++) {
			clusters.add(new ArrayList<Point2D>());
		}
		for (Point2D data : dataset) {
			int index = data.getNearestPointIndex(centers);
			clusters.get(index).add(data);
		}
		List<Point2D> newCenters = new ArrayList<>(centers.size());
		for (List<Point2D> cluster : clusters) {
			newCenters.add(Point2D.getMean(cluster));
		}
		return newCenters;
	}
	public static double getDistance(List<Point2D> centers, List<Point2D> newCenters){
		double distance = 0;
		for(int i = 0; i < centers.size(); i++){
			distance += centers.get(i).getDistance(newCenters.get(i));
		}
		return distance;
	}
	public static class Point2D{
		private double x;
		private double y;
		public Point2D(double x, double y){
			this.x = x;
			this.y = y;
		}
		public double getX(){
			return x;
		}
		public double getY(){
			return y;
		}
		/**
		 * Euclidean distance between two points
		 * TODO: euclidean distance between n points
		 */
		private double getDistance(Point2D point){
			return Math.sqrt(Math.pow(x - point.getX(), 2) + Math.pow(y - point.getY(), 2));
		}
		public int getNearestPointIndex(List<Point2D> points){
			int index = -1;
			double min = Double.MAX_VALUE;
			for(int i = 0; i < points.size(); i++){
				double distance = this.getDistance(points.get(i));
				if(distance < min){
					min = distance;
					index = i;
				}
			}
			return index;
		}
		public static Point2D getMean(List<Point2D> points){
			double x = 0;
			double y = 0;
			for(Point2D point : points){
				x += point.getX();
				y += point.getY();
			}
			return new Point2D(x / points.size(), y / points.size());
		}
		@Override
		public String toString(){
			return "("+x+", "+y+")";
		}
		@Override
		public boolean equals(Object o){
			if(o instanceof Point2D){
				Point2D p = (Point2D)o;
				return p.x == x && p.y == y;
			}
			return false;
		}
	}
	//<--Kmeans for 2D datasets

	//-->2D Points data loader
	public static List<Point2D> getDataset(String inputFile) throws Exception {
		List<Point2D> dataset = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line;
		while ((line = br.readLine()) != null) {
			String[] tokens = line.split(",");
			float x = Float.valueOf(tokens[0]);
			float y = Float.valueOf(tokens[1]);
			Point2D point = new Point2D(x, y);
			for (int i = 0; i < 200; i++)//TODO: Erase this for loop
				dataset.add(point);
		}
		br.close();
		return dataset;
	}
}

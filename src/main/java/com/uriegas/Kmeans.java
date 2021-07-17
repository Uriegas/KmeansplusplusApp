package com.uriegas;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
/**
 * Kmeans implementations <p>
 * Concurrent and non-concurrent versions.
 * N-dimensional and 2-dimensional implementations <p>
 * Implementation based on: https://abnerrjo.github.io/blog/2016/03/12/multithreaded-k-means-in-java/
 * Extended to work with n-dimensional data and the {@code ++} in kmeans concurrently (intialize centroids).
 * @author Eduardo Uriegas & Abner Araujo
 * TODO add the ++ in kmeans
 */
public class Kmeans{
  private static final int REPLICATION_FACTOR = 200;//For testing porpuses
  private static final int NUM_THREADS = 30;//Number of threads to use
  private static final int MAX_RANDOM_VALUE = 1000000;//Max random value to use
  /**
   * Generalization of a point.
   * @author Eduardo Uriegas
   */
    public static class Point {
        private double[] point;
        /**
         * @param point the point to be represented
         */
        public Point(double[] point) {
            this.point = point;
        }
        /**
         * Get eucledian distance between this point and another.
         * @param other
         * @return
         */
        public double getDistance(Point other) {
            double distance = 0;
            for (int i = 0; i < this.size(); i++)
                distance += Math.pow(this.get(i) - other.get(i), 2);
            return Math.sqrt(distance);
        }
        /**
         * Get nerest point in the list of points.
         * @param points
         * @return index of the nearest point
         */
        public int getNearestPointIndex(List<Point> points) {
            int index = -1;
            double min = Double.MAX_VALUE;
            for (int i = 0; i < points.size(); i++) {//For each point
                double distance = getDistance(points.get(i));
                if (distance < min) {
                    min = distance;
                    index = i;
                }
            }
            return index;
        }
            
        /**
         * Get the mean of a list of points. 
         * @param points
         * @param dimension necessary because of the empty list case
         */
        public static Point getMean(List<Point> points, int dimension){
            if(points.size() == 0)
                return new Point(new double[dimension]);
            double[] mean = new double[points.get(0).size()];
            for (Point point : points) {
                for (int i = 0; i < point.size(); i++)
                    mean[i] += point.get(i);
            }
            for (int j = 0; j < points.get(0).size(); j++)
                mean[j] /= points.size();
            return new Point(mean);
        }
        /**
         * To string.
         */
        @Override
        public String toString() {
            String s = "[ ";
            for (int i = 0; i < point.length; i++)
                s += point[i] + " ";
            s += "]";
            return s;
        }
        /**
         * Equals
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Point))
                return false;
            Point other = (Point) obj;
            for (int i = 0; i < point.length; i++)
                if (point[i] != other.point[i])
                    return false;
            return true;
        }
        /**
         * Get size.
         */
        public int size() {
            return point.length;
        }
        /**
         * Get a value.
         */
        public double get(int index) {
            return point[index];
        }
    }
    /**
     * Get dataset for n-dimensional data into a list of points.
     */
    public static List<Point> getDataSets(String file) throws Exception {
        List<Point> dataset = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split(",");
            double[] point = new double[tokens.length];
            for (int i = 0; i < point.length; i++)
                point[i] = Double.parseDouble(tokens[i]);
            dataset.add(new Point(point));
            for (int i = 0; i < REPLICATION_FACTOR; i++)
                dataset.add(new Point(point));
        }
        br.close();
        return dataset;
    }
    public static List<Point> getDataSets(ArrayList<List<String>> table) throws Exception {
        List<Point> dataset = new ArrayList<>();
        for (List<String> row : table) {
            double[] point = new double[row.size()];
            for (int i = 0; i < point.length; i++)
                point[i] = Double.parseDouble(row.get(i));
            dataset.add(new Point(point));
            // for (int i = 0; i < REPLICATION_FACTOR; i++)
            //     dataset.add(new Point(point));
        }
        return dataset;
    }

  /**
   * A 2D point
   * @deprecated
   */
  public static class Point2D {
      private double x;
      private double y;
      
      public Point2D(double x, double y) {
          this.x = x;
          this.y = y;
      }
      
      private double getDistance(Point2D other) {
          return Math.sqrt(Math.pow(this.x - other.x, 2)
                  + Math.pow(this.y - other.y, 2));
      }
      
      public int getNearestPointIndex(List<Point2D> points) {
          int index = -1;
          double minDist = Double.MAX_VALUE;
          for (int i = 0; i < points.size(); i++) {
              double dist = this.getDistance(points.get(i));
              if (dist < minDist) {
                  minDist = dist;
                  index = i;
              }
          }
          return index;
      }

      public static Point2D getMean(List<Point2D> points) {
          double accumX = 0;
          double accumY = 0;
          if (points.size() == 0) return new Point2D(accumX, accumY);
          for (Point2D point : points) {
              accumX += point.x;
              accumY += point.y;
          }
          return new Point2D(accumX / points.size(), accumY / points.size());
      }
      
      @Override
      public String toString() {
          return "(" + this.x + "," + this.y + ")";
      }
      
      @Override
      public boolean equals(Object obj) {
          if (obj == null || !(obj.getClass() != Point2D.class)) {
              return false;
          }
          Point2D other = (Point2D) obj;
          return this.x == other.x && this.y == other.y;
      }
      
  }
  /**
   * Get the data from a file, for testing purposes
   * @deprecated
   */
  public static List<Point2D> getDataset(String inputFile) throws Exception {
      List<Point2D> dataset = new ArrayList<>();
      BufferedReader br = new BufferedReader(new FileReader(inputFile));
      String line;
      while ((line = br.readLine()) != null) {
          String[] tokens = line.split(",");
          double x = Double.valueOf(tokens[0]);
          double y = Double.valueOf(tokens[1]);
          Point2D point = new Point2D(x, y);
          for (int i = 0; i < REPLICATION_FACTOR; i++)
              dataset.add(point);
      }
      br.close();
      return dataset;
  }
    /**
     * Randomly generate a set of points (centroids)
     * @param k number of centroids
     * @param dim number of dimensions
     */
    public static List<Point> initializeRandomCenters(int k, int dim) {
        List<Point> centers = new ArrayList<>(k);
        for (int i = 0; i < k; i++) {
            double[] point = new double[dim];
            for(int j = 0; j < dim; j++)
                point[j] = Math.random() * MAX_RANDOM_VALUE;//Hardcoded max value: 10k
            centers.add(new Point(point));
        }
        return centers;
    }
    /**
     * Generate a set of points (centroids) using a k-means++ algorithm
     * @param k number of centroids
     * @param dataset the dataset for the k-means++ algorithm
     * @return a list of centroids quasi randomly generated and organized
     */
    public static List<Point> initializeCentersplusplus(int k, List<Point> dataset){
        List<Point> centroids = new ArrayList<>(k);
        double[] currPoint = new double[dataset.get(0).point.length];
        double[] distToClosestCentroid = new double[dataset.size()];
        double[] weightedDistribution  = new double[dataset.size()];  // cumulative sum of squared distances

        for (int j = 0; j < k; j++) {//For each centroid
            //-->Get the first centroid randomly
            if(j == 0){
                for(int i = 0; i < currPoint.length; i++)
                    currPoint[i] = Math.random() * MAX_RANDOM_VALUE;//Hardcoded max value: 10k
            }
            //<--Get the first centroid randomly
            else{
                //-->Get the rest of the centroids using a probabilitic approach
                for(int l = 0; l < dataset.size(); l++){
                    double distance = dataset.get(l).getDistance(new Point(currPoint));
                    if(j == 1)
                        distToClosestCentroid[l] = distance;
                    else  
                        if(distance < distToClosestCentroid[l])
                            distToClosestCentroid[l] = distance;
                    if(l == 0)
                        weightedDistribution[0] = distToClosestCentroid[0];
                    else
                        weightedDistribution[l] = weightedDistribution[l-1] + distToClosestCentroid[l];
                }
                double rand = Math.random() * weightedDistribution[dataset.size()-1];
                for(int m = dataset.size()-1; m >= 0; m--){
                    if(rand < weightedDistribution[m]){
                        for(int n = 0; n < currPoint.length; n++)
                            currPoint[n] = dataset.get(m).point[n];
                        break;
                    }
                }
                //<--Get the rest of the centroids using a probabilitic approach
            }
            centroids.add(new Point(currPoint));
        }
        return centroids;
    }
    /**
     * Create a thread to run the k-means algorithm
     * @param partition
     * @param centers
     * @param clusters
     * @return
     */
    private static Callable<Void> createWorker(final List<Point> partition, final List<Point> centers, final List<List<Point>> clusters) {
        return new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                int indexes[] = new int[partition.size()];
                for (int i = 0; i < partition.size(); i++) {
                    Point data = partition.get(i);
                    int index = data.getNearestPointIndex(centers);
                    indexes[i] = index;
                }
                synchronized (clusters) {
                    for (int i = 0; i < indexes.length; i++) {
                        clusters.get(indexes[i]).add(partition.get(i));
                    }    
                }
                return null;
            }
            
        };
    }
  /**
   * Partition 
   */
  private static <V> List<List<V>> partition(List<V> list, int parts) {
      List<List<V>> lists = new ArrayList<List<V>>(parts);
      for (int i = 0; i < parts; i++) {
          lists.add(new ArrayList<V>());
      }
      for (int i = 0; i < list.size(); i++) {
          lists.get(i % parts).add(list.get(i));
      }
      return lists;
  }
  
  public static List<Point> concurrentGetNewCenters(final List<Point> dataset, final List<Point> centers) {
      final List<List<Point>> clusters = new ArrayList<List<Point>>(centers.size());
      for (int i = 0; i < centers.size(); i++) {
          clusters.add(new ArrayList<Point>());
      }
      List<List<Point>> partitionedDataset = partition(dataset, NUM_THREADS);
      ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
      List<Callable<Void>> workers = new ArrayList<>();
      for (int i = 0; i < NUM_THREADS; i++) {
          workers.add(createWorker(partitionedDataset.get(i), centers, clusters));
      }
      try {
          executor.invokeAll(workers);
      } catch (InterruptedException e) {
          e.printStackTrace();
          System.exit(-1);
      }
      List<Point> newCenters = new ArrayList<>(centers.size());
      for (List<Point> cluster : clusters) {
          newCenters.add(Point.getMean(cluster, dataset.get(0).size()));
      }
      return newCenters;
  }

  public static List<Point> getNewCenters(List<Point> dataset, List<Point> centers) {
      List<List<Point>> clusters = new ArrayList<>(centers.size());
      for (int i = 0; i < centers.size(); i++) {
          clusters.add(new ArrayList<Point>());
      }
      for (Point data : dataset) {
          int index = data.getNearestPointIndex(centers);
          clusters.get(index).add(data);
      }
      List<Point> newCenters = new ArrayList<>(centers.size());
      for (List<Point> cluster : clusters) {
          newCenters.add(Point.getMean(cluster, dataset.get(0).size()));
      }
      return newCenters;
  }
  
  public static double getDistance(List<Point> oldCenters, List<Point> newCenters) {
      double accumDist = 0;
      for (int i = 0; i < oldCenters.size(); i++)
          accumDist += oldCenters.get(i).getDistance(newCenters.get(i));
      return accumDist;
  }
  /**
   * The kmeans algorithm<p>
   * Not using multi-threading
   */
  public static List<Point> kmeans(List<Point> dataset, int k) {
      List<Point> centers = initializeRandomCenters(k, dataset.get(0).size());
      boolean converged;
      do {
          List<Point> newCenters = getNewCenters(dataset, centers);
          double dist = getDistance(centers, newCenters);//TODO: Paralelize this, useful when k is large
          centers = newCenters;
          converged = dist == 0;
      } while (!converged);
      return centers;
  }
  /**
   * Concurrent version of the kmeans algorithm
   * @param dataset
   * @param k
   * @return
   */
  public static List<Point> concurrentKmeans(List<Point> dataset, int k) {
      List<Point> centers = initializeRandomCenters(k, dataset.get(0).size());
      boolean converged;
      do {
          List<Point> newCenters = concurrentGetNewCenters(dataset, centers);
          double dist = getDistance(centers, newCenters);
          centers = newCenters;
          converged = dist == 0;
      } while (!converged);
      return centers;
  }
    /**
     * Create a map of the points to their nearest centroid
     */
    public static Map<Integer, List<Point>> getClusters(List<Point> centers, List<Point> data){
        Map<Integer, List<Point>> clusters = new HashMap<Integer, List<Point>>();
        for(int i = 0; i < centers.size(); i++){
            clusters.put(i, new ArrayList<Point>());
        }
        for(Point p : data){
            int index = p.getNearestPointIndex(centers);
            clusters.get(index).add(p);
        }
        return clusters;
    }
  
  public static void main(String[] args) {
      String inputFile = "/home/uriegas/Downloads/dataset.csv";
      int k = Integer.valueOf(15);
      List<Point> dataset = null;
      try {
          dataset = getDataSets(inputFile);
      } catch (Exception e) {
          System.err.println("ERROR: Could not read file " + inputFile);
          System.exit(-1);
      }
      System.out.println("Non-parallelized version");
      long start = System.currentTimeMillis();
      kmeans(dataset, k);
      System.out.println("Time elapsed: " + (System.currentTimeMillis() - start) + "ms");
      System.out.println("Parallelized version");
      start = System.currentTimeMillis();
      List<Point> points = concurrentKmeans(dataset, k);
      System.out.println("Time elapsed: " + (System.currentTimeMillis() - start) + "ms");
      printData(points);
      System.exit(0);
  }
    public static void printData(List<Point> dataset){
        for(int i=0;i<dataset.size();i++){
            for(int j=0;j<dataset.get(i).size();j++)
                System.out.print(dataset.get(i).get(j)+" ");
            System.out.println();
        }
    }
}


package com.uriegas;

import static org.junit.Assert.assertEquals;

import java.io.*;
import java.util.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
/**
 * Test kmeans
 */
@RunWith(Parameterized.class)
public class KmeansTest {
    String path;//The path to read
    int k;
    List<Kmeans.Point> data;//The data to test
    /**
     * Before instantiating this class run this setUp
     */
    @Before
    public void setUp(){
    }
    /**
     * Constructor with parameter injection
     * @param e
     * @param k
     */
    public KmeansTest(String e, int k) throws Exception{
        this.path = e;
        this.k = k;
        data = Kmeans.getDataSets(path);
    }
    /**
     * Parameters to add to the constructor @see{@link com.uriegas.Kmeans#get_centroids(ArrayList, int)}
     * @return Collection of tables
     */
    @Parameterized.Parameters(name = "Loading data from {0} with k = {1}")
    public static Collection<Object[]> getTestData(){
		return Arrays.asList(
            new Object[][]{//path and k
                // {"/datasets/mcdonalds.csv", 9},
                // {"/datasets/walmart.csv", 8},
                // {"/datasets/data3.csv", 2},
                // {"/datasets/data4.csv", 7},
                // {"/datasets/data5.csv", 2},
                // {System.getProperty("user.dir") + "/src/main/resources/datasets/dataset3D.csv", 15},
                // {System.getProperty("user.dir") + "/src/main/resources/datasets/dataset2D.csv", 5},
                {System.getProperty("user.dir") + "/src/main/resources/datasets/smalldataset2D.csv", 5}
            }
        );
	}
    /**
     * Load n-dimensional data from the file
     */
    @Test
    public void testLoadDatas() throws Exception{
        List<Kmeans.Point> data = Kmeans.getDataSets(path);
        Kmeans.printData(data);
        //Test that it does not throw an exception
        // assertNoException(Kmeans2.getDataset(path));
    }
    /**
     * Test classical centroids
     */
    @Test
    public void testCentroids(){
        System.out.println("Centroids test");
        long time = System.currentTimeMillis();

        //-->Run and mesuare code performance
		List<Kmeans.Point> centroids = Kmeans.initializeRandomCenters(k, data.get(0).size());
        System.out.println("Time elapsed: " + (System.currentTimeMillis() - time) + "ms");
        //Expected result: 5 centroids of 3 dimensions
        assertEquals(k,centroids.size());//Check 5  centroids returned
        for(int i = 0; i < centroids.size(); i++){//for each centroid
            Kmeans.Point point = centroids.get(i);
            assertEquals(data.get(0).size(), point.size());//Check the dimensions
        }
        //<--Run and mesuare code performance
    }
    /**
     * Test centroids with kmeans++<p>
     * Implementating the algorithm from:<br>
     * http://ilpubs.stanford.edu:8090/778/1/2006-13.pdf<br>
     * https://github.com/JasonAltschuler/KMeansPlusPlus/blob/master/src/KMeans.java
     */
    @Test
    public void testCentroids_plus_plus(){
        System.out.println("Centroids++ test");
        long time = System.currentTimeMillis();

        //-->Run and mesuare code performance
		Kmeans.initializeCentersplusplus(k, data);
        //<--Run and mesuare code performance

        System.out.println("Time elapsed: " + (System.currentTimeMillis() - time) + "ms");
    }
    /**
     * Test classical kmeans algorithm
     */
    @Test
    public void testKmeans() throws Exception{
        System.out.println("Kmeans test");
        long time = System.currentTimeMillis();

        //-->Run and mesuare code performance
        // data = Kmeans.getDataset(path);
		List<Kmeans.Point> result = Kmeans.kmeans(data, k);
        System.out.println("Time elapsed: " + (System.currentTimeMillis() - time) + "ms");
        System.out.println(result);
        //<--Run and mesuare code performance

        // System.out.println("Result:");
        // for(Kmeans2.Point2D p : result)
        //     System.out.println(p.toString() + " ");
    }
    /**
     * Test classical kmeans distributed algorithm
     */
    @Test
    public void testKmeans_distributed() throws Exception{
        System.out.println("Kmeans test");
        long time = System.currentTimeMillis();

        //-->Run and mesuare code performance
		Kmeans.concurrentKmeans(data, k);
        System.out.println("Time elapsed: " + (System.currentTimeMillis() - time) + "ms");
        //<--Run and mesuare code performance
    }
    /**
     * Test convert table to points
     */
    @Test
    public void testConvertTableToPoints() throws Exception{
        ArrayList<List<String>> table = Utilities.loadFile(new File(path));
        List<Kmeans.Point> points = Kmeans.getDataSets(table);
        assertEquals(table.size(), points.size());
        assertEquals(table.get(0).size(), points.get(0).size());
    }
    /**
     * Test get clusters
     */
    @Test
    public void testGetClusters() throws Exception{
        System.out.println("Get clusters test");
        List<Kmeans.Point> points = Kmeans.getDataSets(path);
        Map<Integer, List<Kmeans.Point>> clusters = Kmeans.getClusters(points, data);
        for(int i = 0; i < clusters.size(); i++)
            System.out.println(clusters.get(i).size() + " ");
    }
}

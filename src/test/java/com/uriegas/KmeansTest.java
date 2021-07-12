package com.uriegas;

import static org.junit.Assert.assertEquals;

import java.io.*;
import java.util.*;

import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
/**
 * Test kmeans
 */
@RunWith(Parameterized.class)
public class KmeansTest {
    String path;//The path to read
    Kmeans kmeans;
    int k;
    /**
     * Before instantiating this class run this setUp
     */
    @Before
    public void setUp(){
        kmeans = new Kmeans();
    }
    /**
     * Constructor with parameter injection
     * @param e
     * @param k
     */
    public KmeansTest(String e, int k){
        this.path = e;
        this.k = k;
    }
    /**
     * Parameters to add to the constructor @see{@link com.uriegas.Kmeans#get_centroids(ArrayList, int)}
     * @return Collection of tables
     */
    @Parameterized.Parameters(name = "Loading data from {0} with k = {1}")
    public static Collection<Object[]> getTestData(){
		return Arrays.asList(
            new Object[][]{//path and k
                {"/datasets/mcdonalds.csv", 9},
                {"/datasets/walmart.csv", 8},
                {"/datasets/data3.csv", 2},
                {"/datasets/data4.csv", 7},
                {"/datasets/data5.csv", 2},
            }
        );
	}
    /**
     * Test classical centroids
     */
    @Test
    public void testCentroids(){
        System.out.println("Centroids test");
        long time = System.currentTimeMillis();

        //-->Run and mesuare code performance
		kmeans.get_random_centroids(data, k);
        //<--Run and mesuare code performance

        System.out.println("Time elapsed: " + (System.currentTimeMillis() - time) + "ms");
    }
    /**
     * Test centroids with kmeans++
     */
    @Test
    public void testCentroids_plus_plus(){
        System.out.println("Centroids++ test");
        long time = System.currentTimeMillis();

        //-->Run and mesuare code performance
		kmeans.get_centroids(data, k);
        //<--Run and mesuare code performance

        System.out.println("Time elapsed: " + (System.currentTimeMillis() - time) + "ms");
    }
    /**
     * Test classical kmeans algorithm
     */
    @Test
    public void testKmeans(){
        System.out.println("Kmeans test");
        long time = System.currentTimeMillis();

        //-->Run and mesuare code performance
		kmeans.classic_kmeans(data, k);
        //<--Run and mesuare code performance

        System.out.println("Time elapsed: " + (System.currentTimeMillis() - time) + "ms");
    }
    /**
     * Test classical kmeans distributed algorithm
     */
    @Test
    public void testKmeans_distributed(){
        System.out.println("Kmeans test");
        long time = System.currentTimeMillis();

        //-->Run and mesuare code performance
		kmeans.classic_kmeans(data, k);
        //<--Run and mesuare code performance

        System.out.println("Time elapsed: " + (System.currentTimeMillis() - time) + "ms");
    }
    /**
     * Test kmeans++ algorithm
     */
    @Test
    public void testKmeans_plus_plus(){
        System.out.println("Kmeans++ test");
        long time = System.currentTimeMillis();

        //-->Run and mesuare code performance
		kmeans.k_means_plus_plus(data, k);
        //<--Run and mesuare code performance

        System.out.println("Time elapsed: " + (System.currentTimeMillis() - time) + "ms");
    }
    /**
     * Test kmeans++ distributed algorithm
     */
    @Test
    public void testKmeans_plus_plus_distributed(){
        System.out.println("Kmeans++ test");
        long time = System.currentTimeMillis();

        //-->Run and mesuare code performance
		kmeans.k_means_plus_plus_distributed(data, k);
        //<--Run and mesuare code performance

        System.out.println("Time elapsed: " + (System.currentTimeMillis() - time) + "ms");
    }
}

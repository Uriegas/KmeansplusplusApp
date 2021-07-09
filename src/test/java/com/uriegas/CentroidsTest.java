package com.uriegas;

import static org.junit.Assert.assertEquals;

import java.io.*;
import java.util.*;

import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
/**
 * Test correctness of excel file reading
 */
@RunWith(Parameterized.class)
public class CentroidsTest {
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
    public CentroidsTest(String e, int k){
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
                {"/examples/data1.csv", 9},
                {"/examples/data2.csv", 8},
                {"/examples/data3.csv", 2},
                {"/examples/data4.csv", 7},
                {"/examples/data5.csv", 2},
            }
        );
	}
    /**
     * Test the correctness of the first and last expected rows, it would be hard to check all the rows.
     */
    @Test
    public void testCentroids(){//assert centroids are correctly assigned
        // ArrayList<List<String>> data = Excel.load(path);
        // kmeans.get_centroids(data, k)
        System.out.println(path + " with k = " + k);
    }
}
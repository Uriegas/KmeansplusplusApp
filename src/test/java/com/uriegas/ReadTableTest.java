package com.uriegas;

import java.io.*;
import java.util.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
/**
 * Test the 2 supported table formats: XLSX and CSV
 */
@RunWith(Parameterized.class)
public class ReadTableTest {
	String path;
    /**
     * Constructor with expected value injection
     * @param path path to load file
     */
    public ReadTableTest(String path){
        this.path = path;
    }
    /**
	 * Collection of paths to files to read
     * @return Collection of tables
     */
    @Parameterized.Parameters(name = "Source file is {0}")
    public static Collection<String> getTestData(){
		return Arrays.asList( "/datasets/mcdonalds.csv", "/datasets/walmart.csv");
		// "/datasets/data3.xlsx", "/datasets/data4.xlsx", "/datasets/data5.xlsx");
	}
	/**
	 * Test if the specified csv/xlsx path is successfully loaded
	 * Assert that the code doesn't throws an error
	 */
	@Test
	public void loadDataTest() throws Exception{
		// Excel.load(path); 
		Utilities.loadFile(new File( new File(".").getCanonicalPath() + "/src/main/resources" + path));
		// System.out.println(t.toString());
	}
}

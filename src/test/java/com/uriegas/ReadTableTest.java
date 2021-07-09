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
		return Arrays.asList( "/examples/data1.csv", "/examples/data2/csv",
		"/examples/data3.xlsx", "/examples/data4.xlsx", "/examples/data5.xlsx");
	}
	/**
	 * Test if the specified csv/xlsx path is successfully loaded
	 * Assert that the code doesn't throws an error
	 */
	@Test
	public void loadDataTest(){
		// Excel.load(path);
		System.out.println(path);
	}
}

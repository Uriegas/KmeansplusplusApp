package com.uriegas;

import java.io.*;
import java.util.*;

/**
 * Utilities for the app, just a globla loadFile function
 * TODO: Implement error handling for xlsx
 */
public class Utilities {
	/**
	 * Load a csv or xlsx file
	 * @param path
	 * @throws IOException
	 */
	public static ArrayList<List<String>> loadFile(File path) throws IOException, IllegalArgumentException {
		//-->Analize csv
		if(path.getName().endsWith(".csv")){
			Scanner sc = new Scanner(path);
			ArrayList<List<String>> table = new ArrayList<List<String>>();

			while(sc.hasNextLine()){//Iterate over each row
				List<String> row = Arrays.asList(sc.nextLine().split(","));//Split and create row
				table.add(row);//Add row to table
			}
			sc.close();
			return table;
		}
		//<--Analize csv
		//-->Analize xlsx
		else if(path.getName().endsWith(".xlsx")){
			Scanner sc = new Scanner(path);
			ArrayList<List<String>> table = new ArrayList<List<String>>();

			while(sc.hasNextLine()){//Iterate over each row
				List<String> row = Arrays.asList(sc.nextLine().split(","));//Split and create row
				table.add(row);//Add row to table
			}
			sc.close();
			return table;
		}
		//<--Analize xlsx
		else{
			throw new IllegalArgumentException("The file extension: " +
				path.getName().substring(path.getName().lastIndexOf("."), path.getName().length()) 
				+ " is allowed");
		}
	}
}

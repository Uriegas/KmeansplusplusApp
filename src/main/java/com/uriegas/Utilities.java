package com.uriegas;

import java.io.*;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

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
			//-->Setup
            FileInputStream file = new FileInputStream(path);
            XSSFWorkbook worksheet = new XSSFWorkbook(file);
            XSSFSheet sheet = worksheet.getSheetAt(0);
            Iterator<Row> rowIt = sheet.rowIterator();//Iterator over rows
            ArrayList<List<String>> values = new ArrayList<List<String>>();//DB
            //<--Setup
            //-->Get values of Excel file
			while(rowIt.hasNext()){
                Row row = rowIt.next();
                if(isRowEmpty(row))
                    continue;
				Iterator<Cell> cellIt = row.cellIterator();
				values.add(new ArrayList<String>());
				while(cellIt.hasNext()){
					Cell cell = cellIt.next();
                    if( cell.getCellType() != CellType.BLANK || cell != null ){
					try{
                        if(cell.getStringCellValue() != "")
                            values.get(values.size()-1).add(cell.getStringCellValue());
					}catch(IllegalStateException e){//TODO: Parse Doubles Ex. 10 -> "10"; NOT 10 -> "10.0"
						values.get(values.size()-1).add(String.valueOf(cell.getNumericCellValue()));
					}
                    }
				}
			}
            //<--Get values of Excel file
            worksheet.close();
            file.close();
			return values;
		}
		//<--Analize xlsx
		else{
			throw new IllegalArgumentException("The file extension: " +
				path.getName().substring(path.getName().lastIndexOf("."), path.getName().length()) 
				+ " is allowed");
		}
	}
	/**
     * Evaluate if a row is empty.
     * Source from: https://roytuts.com/how-to-detect-and-delete-empty-or-blank-rows-from-excel-file-using-apache-poi-in-java/
     * @param row
     * @return boolean
     */
    private static boolean isRowEmpty(Row row) {
		boolean isEmpty = true;
		DataFormatter dataFormatter = new DataFormatter();
		if (row != null) {
			for (Cell cell : row) {
				if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
					isEmpty = false;
					break;
				}
			}
		}
		return isEmpty;
	}
}

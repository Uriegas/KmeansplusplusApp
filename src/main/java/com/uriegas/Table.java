package com.uriegas;

import java.util.*;
import javafx.collections.*;

/**
 * Class Table, represent a table with headers and rows.
 */
public class Table{
	private ObservableList<String> headers;
	private ObservableList<ObservableList<String>> data;
	public Table(){
		data = FXCollections.observableArrayList();
		headers = FXCollections.observableArrayList();
	}
	public Table(ArrayList<List<String>> data, ArrayList<String> headers){
		this();
		for(List<String> row : data)
			this.data.add(FXCollections.observableArrayList(row));
		this.headers.addAll(headers);
	}
	public void setHeaders(ArrayList<String> h){
		this.headers.addAll(h);
	}
	public ObservableList<String> headersProperty(){
		return this.headers;
	}
	public ArrayList<String> getHeaders(){
		ArrayList<String> h = new ArrayList<String>();
		for(String header : headers)
			h.add(header);
		return h;
	}
	public ObservableList<ObservableList<String>> dataProperty(){
		return this.data;
	}
	public void setData(ArrayList<List<String>> data){
		this.data = FXCollections.observableArrayList();
		for(List<String> row : data)
			this.data.add(FXCollections.observableArrayList(row));
	}
	public ArrayList<List<String>> getData(){
		ArrayList<List<String>> data = new ArrayList<List<String>>();
		for(ObservableList<String> row : this.data)
			data.add(FXCollections.observableArrayList(row));
		return data;
	}
	//Add a row to the table
	public void addRow(ArrayList<String> row){
		this.data.add(FXCollections.observableArrayList(row));
	}
	public void clearData(){
		this.data.clear();
	}
	public void clearHeaders(){
		this.headers.clear();
	}
	public Double[] getRowData( String header ) throws NumberFormatException{
		//Get the index of the header
		int index = this.headers.indexOf(header);
		//Get the data of the row
		ArrayList<String> rowData = new ArrayList<String>();
		for(ObservableList<String> row : this.data)
			rowData.add(row.get(index));
		//Convert the data to a Double array
		Double[] data = new Double[rowData.size()];
		for(int i = 0; i < data.length; i++)
			data[i] = Double.parseDouble(rowData.get(i));
		return data;
	}
}

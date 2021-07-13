package com.uriegas;

import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;

/**
 * Wrapper of the a TableView. <p>
 * Purpose: implement methods over this tableview
 */
public class Table extends TableView<ObservableList<String>> {
	private ArrayList<String> headers;
	private ArrayList<List<String>> table;//Save the table in normal format too
	public Table(){
		super();
	}
	public Table(ArrayList<List<String>> table){
		super();
		this.table = table;
		// ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();
		// for(List<String> row : table){
		// 	rows.add(FXCollections.observableArrayList(row));
		// }
		// this.setItems(rows);
	}
	public void setHeaders(ArrayList<String> h){
		this.headers = h;
	}
	public ArrayList<String> getHeaders(){
		return this.headers;
	}
	public ArrayList<List<String>> getData(){
		return table;
	}
	// @Override public String toString(){
	// 	String s = "";
	// 	for( List<String> row : table ){
	// 		for( String cell : row )
	// 			s += cell + ", ";
	// 		s += '\n';
	// 	}
	// 	return s;
	// }
}

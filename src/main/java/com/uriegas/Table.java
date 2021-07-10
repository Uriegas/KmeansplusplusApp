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
	public Table(){
		super();
	}
	public void setHeaders(ArrayList<String> h){
		this.headers = h;
	}
	public ArrayList<String> getHeaders(){
		return this.headers;
	}
}

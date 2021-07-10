package com.uriegas;

import java.io.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.*;

public class Model implements Serializable {
	private transient ObservableList<File> files = FXCollections.observableArrayList();
	private transient Table table = new Table();

	//-->Table methods
	public ObservableList<ObservableList<String>> tableProperty(){
		return table.getItems();
	}
	public void setTable(ArrayList<List<String>> table){
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();//Excel table in javafx arraylist's
        List<String> headers = table.get(0);//Headers of the table

        for(int i = 1; i < table.size(); i++)
            data.add(FXCollections.observableArrayList(table.get(i)));

        this.table.setItems(data);

        //Create the table columns, set the cell value factory and add the column to the tableview.
        for (int i = 0; i < table.get(0).size(); i++) {
            final int curCol = i;
            final TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                    headers.get(curCol)
            );
            column.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().get(curCol))
            );
            this.table.getColumns().add(column);
        }
		this.table.setHeaders((ArrayList<String>)headers);
	}
	//<--Table methods

	//-->Files methods
	public ObservableList<File> filesProperty(){
		return files;
	}
	public void setFile(File file){
		this.files.add(file);
	}
	public void setFile(ArrayList<File> files){
		this.files.addAll(files);
	}
	public ArrayList<File> getFile(){
		ArrayList<File> tmp = new ArrayList<File>();
		for(File f : files)
			tmp.add(f);
		return tmp;
	}
	//<--Files methods
}
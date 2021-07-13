package com.uriegas;

import java.io.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.*;
/**
 * Data Model for this project: contains stored files, loaded data and current file.
 */
public class Model implements Serializable {
	/**
	 * List of files to show the user recently used files
	 */
	private transient ObservableList<File> files = FXCollections.observableArrayList();
	/**
	 * The current loaded data (table)
	 */
	private transient Table table = new Table();
	/**
	 * The current loaded data path (file)
	 */
	private transient StringProperty currentFile = new SimpleStringProperty(){
		@Override public String get(){//Overload get method to show name of file instead of absolute path
			try{
				if(super.get().contains("/"))
					return super.get().substring(super.get().lastIndexOf("/")+1, super.get().length());
				else
					return super.get();
			}catch(NullPointerException e){
				return null;
			}
		}
	};

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
		for( File f : this.files )
			if(f.equals(file)){//Exist so don't add to list, just change current file
				this.setCurrentFile(file);
				return;
			}
		this.files.add(file);
		this.setCurrentFile(file);
	}
	public ArrayList<File> getFiles(){
		ArrayList<File> tmp = new ArrayList<File>();
		for(File f : files)
			tmp.add(f);
		return tmp;
	}
	//<--Files methods

	//-->CurrentFile methods
	public StringProperty currentFileProperty(){
		return this.currentFile;
	}
	public void setCurrentFile(String file){
		this.currentFile.set(file);
	}
	public void setCurrentFile(File file){
		this.currentFile.set(file.getAbsolutePath());
	}
	public String getCurrentFile(){
		return currentFileProperty().get();
	}
	//<--CurrentFile methods

	/**
	 * Serialize this object
	 * @param s
	 * @throws Exception
	 */
    private void writeObject(ObjectOutputStream s) throws Exception {
        s.defaultWriteObject();
		s.writeObject(getFiles());
		s.writeUTF(getCurrentFile());
    }
	/**
	 * Serialize this object, expect for the mails
	 * @param s
	 * @throws Exception
	 */
    private void readObject(ObjectInputStream s) throws Exception {
        s.defaultReadObject();
		files = FXCollections.observableList((List<File>)s.readObject());
		currentFile = new SimpleStringProperty(s.readUTF());
		//-->Initialize not serialized objects(if not initialized they are null)
		table = new Table();
		//<--Initialize not serialized objects(if not initialized they are null)
    }
}

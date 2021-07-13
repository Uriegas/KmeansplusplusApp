package com.uriegas;

import java.io.*;
import java.util.*;
import javafx.concurrent.*;
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
	private transient Table theTable = new Table();
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
		return theTable.getItems();
	}
	public void setTable(ArrayList<List<String>> table){
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();//Excel table in javafx arraylist's
        List<String> headers = new ArrayList<String>();//Headers of the table

		//-->Load data into the table
        for(int i = 0; i < table.size(); i++)
            data.add(FXCollections.observableArrayList(table.get(i)));

        theTable.setItems(data);
		//<--Load data into the table

        //Create the table columns, set the cell value factory and add the column to the tableview.
        for (int i = 0; i < table.get(0).size(); i++) {
            final int curCol = i;
            final TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                    "Column " + (i + 1)
            );
            column.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().get(curCol))
            );
            theTable.getColumns().add(column);
        }
		theTable.setHeaders((ArrayList<String>)headers);
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
	@SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream s) throws Exception {
        s.defaultReadObject();
		files = FXCollections.observableList((List<File>)s.readObject());
		currentFile = new SimpleStringProperty(s.readUTF());
		//-->Initialize not serialized objects(if not initialized they are null)
		theTable = new Table();
		//<--Initialize not serialized objects(if not initialized they are null)
    }
	public Task<String> fileLoaderTask(File fileToLoad){
		//Create a task to load the file asynchronously
		Task<String> loadFileTask = new Task<>() {
			@Override
			protected String call() throws Exception {//Load the file
				ArrayList<List<String>> data = new ArrayList<>();
				if( fileToLoad.getName().endsWith(".xlsx") || fileToLoad.getName().endsWith(".csv") ){
					data = Utilities.loadFile(fileToLoad);
					setTable(data);
					setFile(fileToLoad);
				}else{throw new Exception("File format not supported");}
				System.out.println("Loaded file: " + fileToLoad.getAbsolutePath());
				return "File loaded";
			}
		};

		//If successful, update the text area, display a success message and store the loaded file reference
		loadFileTask.setOnSucceeded(workerStateEvent -> {
			try {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("File loaded");
				alert.setHeaderText("File loaded");
				alert.setContentText("File loaded successfully");
				alert.show();
			} catch (Exception e) {
				System.out.println("Error loading file: " + fileToLoad.getAbsolutePath());
			}
		});

		//If unsuccessful, set text area with error message and status message to failed
		loadFileTask.setOnFailed(workerStateEvent -> {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error loading file");
			alert.setContentText("Could not load file from:\n " + fileToLoad.getAbsolutePath());
			alert.show();
		});

		return loadFileTask;
	}
}

package com.uriegas;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import java.io.*;
/**
 * General class that represents a window
 */
public class Window {
	protected Model model;
	/**
     * Switch the current scene (window) to the specified FXML file
	 * @param FXML file ej. {@code "/main.fxml"}
     * @param e the event that made the window switching
	 * @param m pass the data model instance
     */
    public void switchScene(Event e, Model m, String FXML){
        Stage switchscene = (Stage) ((Node)e.getSource()).getScene().getWindow();
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/fxml"+FXML));
            Scene scene = loader.load();
            // scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());//Add css
            Window w = loader.getController();
            w.initModel(m);
            switchscene.setScene(scene);
        }catch(IOException ex){ex.printStackTrace();}
    }
    /**
     * Abstract method for initializing the model
     * @param m
     */
    public void initModel(Model m){
        if(this.model != null)
            throw new IllegalStateException("Model only can be instantiated once");
        else{
            this.model = m;
        }
    }
    /**
     * Creates a new pop up from the current window
     * @param e event to handle this pop up
     * @param FXML the fxml file to load this pop up
     */
    public void createPopUp(Event e, String FXML){
        Stage dialog = new Stage(); // new stage
        dialog.initModality(Modality.WINDOW_MODAL);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/"+FXML));
        try{
			Parent root = loader.load();
            Scene scene = new Scene(root);
            dialog.setScene(scene);
        }catch(IOException ex){ex.printStackTrace();}
        dialog.initOwner(((Node)e.getTarget()).getScene().getWindow());
        dialog.show();
    }
    /**
     * Create a pop up from the current window with the model instance
     */
    public void createPopUp(Event e, Model m, String FXML){
        Stage dialog = new Stage(); // new stage
        dialog.initModality(Modality.WINDOW_MODAL);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/fxml/"+FXML));
        try{
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Window w = loader.getController();
            w.initModel(m);
            dialog.setScene(scene);
        }catch(IOException ex){ex.printStackTrace();}
        dialog.initOwner(((Node)e.getTarget()).getScene().getWindow());
        dialog.show();
    }
}

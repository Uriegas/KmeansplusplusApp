package com.uriegas;

import java.net.URL;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
/**
 * Controller of the Scene view
 */
public class FXMLController implements Initializable {
    @FXML private TableView<ArrayList<List<String>>> table;
    @FXML private ListView<String> lastViewed;
    @FXML private Button loadFile;
    @FXML private Button applyKmeans;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadFile.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override public void handle(MouseEvent e){
                System.out.println("Load file clicked");
            }
        });
        loadFile.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override public void handle(MouseEvent e){
                System.out.println("apply kmeans clicked");
            }
        });

        // lastViewed.setItems(model.getLastViewedProperty());
        lastViewed.setCellFactory(lv -> new ListCell<String>(){
            @Override public void updateItem(String item, boolean empty){
                super.updateItem(item, empty);
                if(empty)
                    setText(null);
                else
                    setText(item);
            }
        });
    }    
}

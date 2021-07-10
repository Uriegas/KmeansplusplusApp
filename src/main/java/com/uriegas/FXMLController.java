package com.uriegas;

import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.*;
import javafx.stage.*;
/**
 * Controller of the Scene view
 */
public class FXMLController implements Initializable {
    public Model model;
    @FXML private TableView<ObservableList<String>> table;
    @FXML private ListView<File> lastViewed;
    @FXML private Button loadFile;
    @FXML private Button applyKmeans;
    @FXML private Button about;
    @FXML private TextField currentFile;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.model = new Model();
        //-->Data binding
        lastViewed.setItems(this.model.filesProperty());
        table.setItems(this.model.tableProperty());
        currentFile.textProperty().bindBidirectional(this.model.currentFileProperty());
        //<--Data binding
        lastViewed.setOnMouseClicked(event -> {
            if( event.getButton().equals(MouseButton.PRIMARY) )
                if( event.getClickCount() == 2 )
                    this.model.setFile(lastViewed.getSelectionModel().getSelectedItem());
        });
        // loadFile.setOnMouseClicked(new EventHandler<MouseEvent>(){//When loadFile is clicked
        //     final Table table = new Table();
        //     @Override public void handle(MouseEvent e){
        //         //-->Select file
        //         FileChooser fiChooser = new FileChooser();
        //         fiChooser.setTitle("Select a data file(csv/xlsx)");
        //         File file = fiChooser.showOpenDialog( null );
        //         //<--Select file

        //         //-->Load file
        //         this.model.setFile(file);
        //         System.out.println("Load file " + file.getName());
        //         //<--Load file
        //     }
        // });
        applyKmeans.setOnMouseClicked(new EventHandler<MouseEvent>(){//When kmeans is clicked
            @Override public void handle(MouseEvent e){
                //-->Show: select k dialog
                //<--Show: select k dialog
                //-->Show kmeas++ aggrupation
                //<--Show kmeas++ aggrupation
                System.out.println("apply kmeans clicked");
            }
        });
        about.setOnMouseClicked(new EventHandler<MouseEvent>(){//When about is clicked
            @Override public void handle(MouseEvent e){//Show explanation of the program
                //-->Show about dialog
                //This a new independent window
                //<--Show about dialog
                System.out.println("about button clicked");
            }
        });

        // lastViewed.setItems(model.getLastViewedProperty());
        lastViewed.setCellFactory(lv -> new ListCell<File>(){
            @Override public void updateItem(File item, boolean empty){
                super.updateItem(item, empty);
                if(empty)
                    setText(null);
                else
                    setText(item.getName());
            }
        });
    }
    @FXML
    public void loadClicked(ActionEvent e){
        //-->Select file
        FileChooser fiChooser = new FileChooser();
        fiChooser.setTitle("Select a data file(csv/xlsx)");
        File file = fiChooser.showOpenDialog( null );
        if( !(file.getName().endsWith(".xlsx") || file.getName().endsWith(".csv")) ){
            Alert error = new Alert(AlertType.ERROR);
            error.setTitle("Wrong file type");
            error.setHeaderText("The file " + file.getName() + " is not valid");
            error.show();
            e.consume();
        }
        //<--Select file

        //-->Load file
        else{
            this.model.setFile(file);
            System.out.println("Load file " + file.getName());
        }
        //<--Load file
    }
}

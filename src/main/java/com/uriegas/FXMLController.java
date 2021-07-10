package com.uriegas;

import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Scene;
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

        //-->Event Handling
        lastViewed.setOnMouseClicked(event -> {
            if( event.getButton().equals(MouseButton.PRIMARY) )
                if( event.getClickCount() == 2 )
                    this.model.setFile(lastViewed.getSelectionModel().getSelectedItem());
        });
        loadFile.setOnMouseClicked(e ->{ //When loadFile is clicked
            //-->Select file
            FileChooser fiChooser = new FileChooser();
            fiChooser.setTitle("Select a data file(csv/xlsx)");
            File file = fiChooser.showOpenDialog( null );
            if( !(file.getName().endsWith(".xlsx") || file.getName().endsWith(".csv")) ){
                Alert error = new Alert(AlertType.ERROR);
                error.setTitle("Wrong file type");
                error.setHeaderText("The file " + file.getName() + " has not a valid format");
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
        });
        applyKmeans.setOnMouseClicked(e ->{//When kmeans is clicked
                //-->Show: select k dialog
                //<--Show: select k dialog
                //-->Show kmeas++ aggrupation
                //<--Show kmeas++ aggrupation
                System.out.println("apply kmeans clicked");
        });
        //TODO The about pop up doesn't close
        about.setOnMouseClicked(e ->{//When about is clicked show explanation of the program
                //-->Show about dialog
                // Stage dialog = new Stage();
                // dialog.initModality(Modality.WINDOW_MODAL);
                // FXMLLoader loader = new FXMLLoader();
                // loader.setLocation(this.getClass().getResource("/fxml/About.fxml"));
                // try{
                //     Scene scene = loader.load();
                //     scene.getStylesheets().add(getClass().getResource("/styles/Styles.css").toExternalForm());//Add css
                //     dialog.setScene(scene);
                // }catch(IOException ex){ex.printStackTrace();}
                // dialog.initOwner(((Node)e.getTarget()).getScene().getWindow());
                // dialog.show();
                DialogPane dialog = new DialogPane();
                dialog.getStylesheets().add(getClass().getResource("/styles/Styles.css").toExternalForm());//Add css
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("/fxml/About.fxml"));
                try {
                    dialog = loader.load();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                Alert alert = new Alert(AlertType.INFORMATION );
                alert.setDialogPane(dialog);
                // alert.setOnCloseRequest(e3->{((Event)e3.getSource()).consume();});
                alert.show();
                e.consume();
                //<--Show about dialog
                System.out.println("about button clicked");
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
        //<--Event Handling
    }
}

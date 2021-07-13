package com.uriegas;

import java.io.*;
import java.util.Optional;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.*;
import javafx.stage.*;
/**
 * Controller of the Scene view
 */
public class FXMLController extends Window {
    // private Model model;
    @FXML private TableView<ObservableList<String>> table;
    @FXML private ListView<File> lastViewed;
    @FXML private Button loadFile;
    @FXML private Button applyKmeans;
    @FXML private Button about;
    @FXML private TextField currentFile;

    /**
     * Initialize the model<p>
     * Here we implement data binding
     * @param m
     */
    public void initModel(Model m){
        super.initModel(m);
        //-->Data binding
        lastViewed.setItems(this.model.filesProperty());
        table.setItems(this.model.tableProperty());
        currentFile.textProperty().bindBidirectional(this.model.currentFileProperty());
        //<--Data binding
    }
    /**
     * Initialize the controller<p>
     * Here we implement event handling
     */
    public void initialize() {
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
                TextInputDialog popup = new TextInputDialog("2");
                popup.setTitle("Select K value");
                popup.setHeaderText("Enter number of k-classes");
                popup.getDialogPane().setContentText("K: ");
                popup.getEditor().setTextFormatter(new TextFormatter<>(c ->{//Allow only positive integers
                    if (c.isContentChange()) {
                        if (c.getControlNewText().length() == 0)
                            return c;
                        try {
                            Integer.parseInt(c.getControlNewText());
                            return c;
                        } catch (NumberFormatException ex){}
                        return null;
                    }
                    return c;
                }));
                Optional<String> input = popup.showAndWait();
                //<--Show: select k dialog

                //-->Show kmeas++ aggrupation
                //<--Show kmeas++ aggrupation
                System.out.println("apply kmeans clicked, k is " + ( input.isPresent() ? input.get() : "nothig") );
        });
        about.setOnMouseClicked(e ->{//When about is clicked show explanation of the program
                //-->Show about dialog
                createPopUp(e, "About.fxml");
                System.out.println("about button clicked");
                // //<--Show about dialog
        });
        //<--Event Handling

        //-->Formatting data
        lastViewed.setCellFactory(lv -> new ListCell<File>(){
            @Override public void updateItem(File item, boolean empty){
                super.updateItem(item, empty);
                if(empty)
                    setText(null);
                else
                    setText(item.getName());
            }
        });
        //<--Formatting data
    }
}

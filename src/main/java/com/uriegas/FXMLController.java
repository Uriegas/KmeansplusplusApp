package com.uriegas;

import java.io.*;
import java.util.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.concurrent.Task;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
/**
 * Controller of the Scene view
 */
public class FXMLController extends Window {
    @FXML private TableView<ObservableList<String>> table;
    @FXML private ListView<File> lastViewed;
    @FXML private Button loadFile;
    @FXML private Button applyKmeans;
    @FXML private Button about;
    @FXML private TextField currentFile;
    @FXML private Button dragAndDrop;

    /**
     * Initialize the model<p>
     * Here we implement data binding
     * @param m
     */
    public void initModel(Model m){
        super.initModel(m);
        //-->Data binding
        lastViewed.setItems(this.model.filesProperty());
        // table.setItems(this.model.tableProperty());
        currentFile.textProperty().bindBidirectional(this.model.currentFileProperty());
        //-->Bind the table to the headers of the model's table
        this.model.headersProperty().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> c) {
                try{
                table.getColumns().clear();
                table.getItems().clear();
                for(String header : c.getList()){
                    TableColumn<ObservableList<String>, String> column = new TableColumn<>(header);
                    table.getColumns().add(column);
                }
                for(int i = 0; i < table.getColumns().size(); i++){//Add columns cell value factory (styling)
                    final int j = i;
                    ((TableColumn<ObservableList<String>,String>)table.getColumns().get(i)).setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j)));
                }
                table.getItems().addAll(model.tableProperty());
                }catch(Exception e){System.out.println("An erroroccured while binding the table to the model");}
            }
        });
        table.setItems(this.model.tableProperty());
        //-->Bind the table to the headersof the model's table

        //-->Bind the data to the table
        this.model.tableProperty().addListener(new ListChangeListener<ObservableList<String>>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends ObservableList<String>> c) {
                table.setItems(FXMLController.this.model.tableProperty());
            }
        });
        //<--Bind the data to the table
        //<--Data binding
    }
    /**
     * Initialize the controller<p>
     * Here we implement event handling
     */
    public void initialize() {
        //-->Event Handling
        lastViewed.setOnMouseClicked(e -> {
            if( e.getButton().equals(MouseButton.PRIMARY) )
                if( e.getClickCount() == 2 ){
                    //-->Load file
                    Task<String> task = model.fileLoaderTask(lastViewed.getSelectionModel().getSelectedItem());
                    task.run();
                    //<--Load file
                }
        });
        loadFile.setOnMouseClicked(e ->{ //When loadFile is clicked
            //-->Select file
            FileChooser fiChooser = new FileChooser();
            fiChooser.setTitle("Select a data file(csv/xlsx)");
            File file = fiChooser.showOpenDialog( null );
            //<--Select file

            //-->Load file
            Task<String> task = model.fileLoaderTask(file);
            task.run();
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
                //TODO: call utility function to analize data
                List<Double> x = Arrays.asList(Double.valueOf(14.2), Double.valueOf(48.2), Double.valueOf(14.7));
                List<Double> y = Arrays.asList(Double.valueOf(14.2), Double.valueOf(48.2), Double.valueOf(14.7));
                plotScatter(x, y);
                //<--Show kmeas++ aggrupation
                System.out.println("apply kmeans clicked, k is " + ( input.isPresent() ? input.get() : "nothig") );
        });
        about.setOnMouseClicked(e ->{//When about is clicked show explanation of the program
                //-->Show about dialog
                createPopUp(e, "About.fxml");
                System.out.println("about button clicked");
                // //<--Show about dialog
        });
        dragAndDrop.setOnDragOver(event ->{//When about is dragged show explanation of the program
            event.acceptTransferModes(TransferMode.MOVE);
        });
        dragAndDrop.setOnDragDropped(e ->{
            Dragboard db = e.getDragboard();

            if( db.hasFiles() ){
                File file = db.getFiles().get(0);//Get only the first file
                System.out.println("File dropped: " + file.getName());
                Task<String> loadFile = model.fileLoaderTask(file);//Load the file
                loadFile.run();
            }

        });
        // dragAndDrop.setOnMouseClicked(e -> {//Add rows to the table
        //     if( e.getButton().equals(MouseButton.PRIMARY) )
        //         if( e.getClickCount() == 1 ){
                    //In-line arraylist
                    // model.setHeader(new ArrayList<String>(){{
                    //     add("Row 1");
                    // }});
                    // model.getTableData().clear();
                    // ArrayList<String> data = new ArrayList<String>(){{
                    //     for( int i = 0; i < model.getHeaders().size(); i++ )
                    //         add("Row " + (i+1));
                    // }};
                    // model.addRowToTable( data );
        //         }
        // });
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

    static void plotScatter(List<Double> x, List<Double> y){
        Text txt = new Text("Scatter(x,y)");
        NumberAxis xAxis = new NumberAxis(0, 10, 1);
        NumberAxis yAxis = new NumberAxis(-100, 500, 100);
        ScatterChart<Number, Number> scatter = new ScatterChart<Number, Number>(xAxis, yAxis);
        xAxis.setLabel("x");
        yAxis.setLabel("y");
        scatter.setTitle("Scatter(x,y)");

        XYChart.Series<Number,Number> series1 = new XYChart.Series<Number,Number>();
        series1.setName("Data");
        for(int i = 0; i < x.size(); i++ )
            series1.getData().add(new XYChart.Data<Number, Number>(x.get(i), y.get(i)));
      
        scatter.getData().addAll(series1);

        VBox vb = new VBox(txt, scatter);
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(10, 10, 10, 10));

        Stage s = new Stage();
        s.setTitle("Scatter graph");
        s.setScene(new Scene(vb, 400, 400));
        s.show();
    }
}

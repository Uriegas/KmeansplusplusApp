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
import javax.imageio.*;
import javafx.embed.swing.*;
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
    @FXML private Button plotCorrelation;

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
        this.model.headersProperty().addListener(new ListChangeListener<String>() {// TODO: maybe we can change this to a cellFactory instead of using a ListChangeListener and an embbeded CellValueFactory
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
        loadFile.setOnDragOver(e ->{//When this button is accept the drag and drop
            e.acceptTransferModes(TransferMode.MOVE);
        });
        loadFile.setOnDragDropped(e ->{//Execute this when accepting the drag and drop
            Dragboard db = e.getDragboard();

            if( db.hasFiles() ){
                File file = db.getFiles().get(0);//Get only the first file
                System.out.println("File dropped: " + file.getName());
                Task<String> loadFile = model.fileLoaderTask(file);//Load the file
                loadFile.run();
            }
        });
        applyKmeans.setOnMouseClicked(e ->{//When kmeans is clicked
                //-->Show: select k dialog
                TextInputDialog popup = new TextInputDialog("2");
                popup.setTitle("Select K value");
                popup.setHeaderText("Enter number of k-classes");
                popup.getDialogPane().setContentText("K: ");
                popup.getEditor().setTextFormatter(new TextFormatter<>(c ->{//Allow only positive integers
                    if (c.isContentChange()) {
                        //Allow text of maximum length of 2
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
                Optional<String> k = popup.showAndWait();
                //<--Show: select k dialog
                if(k.isPresent()){
                //-->Show kmeas++ aggrupation
                try{
                    List<Kmeans.Point> dat = Kmeans.getDataSets(model.getTableData());
                    List<Kmeans.Point> points = Kmeans.concurrentKmeans(dat, Integer.parseInt(k.get()));
                    Map<Integer, List<Kmeans.Point>> clusters = Kmeans.getClusters(points, dat);
                    plotClusters(clusters);
                }catch(Exception exception){
                    System.out.println("An error occured while applying kmeans++");
                    //Show error pop up
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("An error occured while applying kmeans++");
                    alert.setContentText("Please try again");
                    alert.showAndWait();
                }
                //<--Show kmeas++ aggrupation
                System.out.println("apply kmeans clicked, k is " + ( k.isPresent() ? k.get() : "nothig") );
                }
        });
        about.setOnMouseClicked(e ->{//When about is clicked show explanation of the program
                //-->Show about dialog
                createPopUp(e, "About.fxml");
                System.out.println("about button clicked");
                //<--Show about dialog
        });
        //TODO: add error handling
        plotCorrelation.setOnMouseClicked(e ->{//When plot correlation is clicked show correlation plot
            System.out.println("plot correlation clicked");
            //-->Set the data
            //Quick fix for the set data: Create a choice box dialog
            ChoiceDialog<String> choiceBox = new ChoiceDialog<String>();
            choiceBox.getItems().addAll(this.model.getHeaders());
            choiceBox.setTitle("Select variable");
            choiceBox.setHeaderText("Select variable to graph in x-axis");
            choiceBox.setContentText("Choose a variable");

            choiceBox.showAndWait().ifPresent(var -> {
                System.out.println("Variable: " + var);
                this.model.setVariable1(var);
            });

            ChoiceDialog<String> choiceBox2 = new ChoiceDialog<String>();
            choiceBox2.getItems().addAll(this.model.getHeaders());
            choiceBox2.setTitle("Select variable");
            choiceBox2.setHeaderText("Select variable to graph in y-axis");
            choiceBox2.setContentText("Choose a variable");

            choiceBox2.showAndWait().ifPresent(var -> {
                System.out.println("Variable: " + var);
                this.model.setVariable2(var);
            });
            //<--Set the data

            //-->Show correlation plot
            plotGraph();
            //<--Show correlation plot
        });
        //<--Event Handling

        //-->Formatting data
        lastViewed.setCellFactory(lv -> new ListCell<File>(){// TODO: maybe this setCellFactory is useful in the tableview data binding
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
    /**
     * Create a pop up window with a correlation plot
     * TODO: plot regression line
     */
    public void plotGraph(){
        //Create the scatter plot with variable axises
        ScatterChart<Number, Number> chart = new ScatterChart<Number, Number>(
            new NumberAxis(
                model.getVariable1Min()*0.8, 
                model.getVariable1Max()*1.1,
                ((model.getVariable1Max() + model.getVariable1Min()) / 10)),
            new NumberAxis(
                model.getVariable2Min()*0.8,
                model.getVariable2Max()*1.1, 
                ((model.getVariable2Max() + model.getVariable2Min()) / 10)));
        //Load data from the model
		Number[] xData = (Number[]) model.getVariable1Data();
		Number[] yData = (Number[]) model.getVariable2Data();
        //Create the scatter plot with variable axises
		NumberAxis xAxis = new NumberAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel(model.getVariable1());
		yAxis.setLabel(model.getVariable2());
        chart.setTitle( "R = " + Utilities.getCorrelation(model.getVariable1Data(), model.getVariable2Data()).toString() );
        XYChart.Series<Number,Number> serie = new XYChart.Series<Number,Number>();
        //Populate the scatter plot with data
		for(int j=0;j<model.getTableData().size();j++)//Add the data to the serie
			serie.getData().add(new XYChart.Data<Number,Number>(xData[j],yData[j]));
		chart.getData().add(serie);//Add the serie to the chart
        //Mange stage

        //-->Save the chart button
        Button save = new Button("Save");
        save.setOnMouseClicked(e ->{//Save the chart
			FileChooser fc = new FileChooser();
			fc.setTitle("Save chart as");
			fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
			File file = fc.showSaveDialog(null);
			if (file != null) {
				try {//Save ScatterChart as PNG in the file
				ImageIO.write(SwingFXUtils.fromFXImage(((Node)e.getSource()).getScene().snapshot(null), null), "png", file);
				System.out.println("Saved chart to " + file.getAbsolutePath());
				}catch (IOException ex) {System.err.println("Error saving chart to file: " + ex.getMessage());} 
			}else{
				System.out.println("No file selected");
			}
		});
        //<--Save the chart button

        HBox hb = new HBox(save);
        hb.setAlignment(Pos.CENTER_RIGHT);
        VBox vb = new VBox(hb, chart);
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(10, 10, 10, 10));
        Stage s = new Stage();
		s.setTitle("Correlation: " + model.getVariable1() + " and " + model.getVariable2());
        s.setScene(new Scene(vb, 400, 400));
        s.show();
    }
    public void plotClusters(Map<Integer, List<Kmeans.Point>> clusters){
        model.setVariable1(model.getHeaders().get(0));
        model.setVariable2(model.getHeaders().get(1));
        //Create the scatter plot with variable axises
        ScatterChart<Number, Number> chart = new ScatterChart<Number, Number>(
            new NumberAxis(
                model.getVariable1Min()*0.8, 
                model.getVariable1Max()*1.1,
                ((model.getVariable1Max() + model.getVariable1Min()) / 10)),
            new NumberAxis(
                model.getVariable2Min()*0.8,
                model.getVariable2Max()*1.1, 
                ((model.getVariable2Max() + model.getVariable2Min()) / 10)));
        //Create the scatter plot with variable axises
		NumberAxis xAxis = new NumberAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel(model.getVariable1());
		yAxis.setLabel(model.getVariable2());
        chart.setTitle("Clusters (k):" + clusters.size());
        //Populate the scatter plot with the cluster
        for(int i=0;i<clusters.size();i++){
            XYChart.Series<Number,Number> serie = new XYChart.Series<Number,Number>();
            for(Kmeans.Point p : clusters.get(i))
                serie.getData().add(new XYChart.Data<Number,Number>(p.get(0),p.get(1)));//Can only graph 2D data
            chart.getData().add(serie);
        }
        //Mange stage

        //-->Save the chart button
        Button save = new Button("Save");
        save.setOnMouseClicked(e ->{//Save the chart
			FileChooser fc = new FileChooser();
			fc.setTitle("Save chart as");
			fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
			File file = fc.showSaveDialog(null);
			if (file != null) {
				try {//Save ScatterChart as PNG in the file
				ImageIO.write(SwingFXUtils.fromFXImage(((Node)e.getSource()).getScene().snapshot(null), null), "png", file);
				System.out.println("Saved chart to " + file.getAbsolutePath());
				}catch (IOException ex) {System.err.println("Error saving chart to file: " + ex.getMessage());} 
			}else{
				System.out.println("No file selected");
			}
		});
        //<--Save the chart button

        HBox hb = new HBox(save);
        hb.setAlignment(Pos.CENTER_RIGHT);
        VBox vb = new VBox(hb, chart);
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(10, 10, 10, 10));
        Stage s = new Stage();
		s.setTitle("Correlation: " + model.getVariable1() + " and " + model.getVariable2());
        s.setScene(new Scene(vb, 400, 400));
        s.show();
    }
}

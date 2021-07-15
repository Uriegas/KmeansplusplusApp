package com.uriegas;

import java.io.*;
import javax.imageio.*;
import javafx.embed.swing.*;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.stage.*;
/**
 * Controller for the plot
 * @author Eduardo Uriegas
 * TODO autoscale axes
 */
public class PlotController extends Window {
	@FXML private Label label;
	@FXML private Button save;
	@FXML private ScatterChart<Number,Number> chart;
	/**
	 * Initialize the model
	 * @param m
	 */
	public void initModel(Model m){
		super.initModel(m);
		//-->Set the chart with the data of the model
		//Create arrays with the data
		Number[] xData = (Number[]) model.getVariable1Data();
		Number[] yData = (Number[]) model.getVariable2Data();

		NumberAxis xAxis = new NumberAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel(model.getVariable1());
		yAxis.setLabel(model.getVariable2());
		chart.setTitle("This char");
        XYChart.Series<Number,Number> serie = new XYChart.Series<Number,Number>();
		serie.setName("Correlation: " + model.getVariable1() + " and " + model.getVariable2());
		for(int j=0;j<model.getTableData().size();j++)//Add the data to the serie
			serie.getData().add(new XYChart.Data<Number,Number>(xData[j],yData[j]));
		chart.getData().add(serie);//Add the serie to the chart
	}
	/**
	 * Initialize the controller class.
	 */
	public void initialize() {
		/**
		 * Save the chart as an image
		 */
		save.setOnAction(e -> {//save the chart
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
		autoScale();
	}
	/**
	 * Method to autoscale axes
	 */
	public void autoScale() {
		chart.getXAxis().setAutoRanging(true);
		chart.getYAxis().setAutoRanging(true);
	}
	/**
	 * Setup the chart
	 * @param chart
	 */
	public void setChart(ScatterChart<Number,Number> chart) {
		this.chart = chart;
	}
	/**
	 * Setup the chart
	 * @param title
	 */
	public void setTitle(String title) {
		label.setText(title);
	}
}

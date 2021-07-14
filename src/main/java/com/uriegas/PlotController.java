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
public class PlotController {
	@FXML private Label label;
	@FXML private Button save;
	@FXML private ScatterChart<Number,Number> chart;
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
}

package com.uriegas;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
/**
 * Controller for the SelectVariables dialog.
 */
public class SelectVariablesController extends Window {
	@FXML private MenuButton variable1;
	@FXML private MenuButton variable2;
	@FXML private ListView<String> listView = new ListView<String>();
	private boolean isListSelected = false;//true if list is selected one time
	/**
	 * Initializes the model.
	 */
	public void initModel(Model m){
		super.initModel(m);
		//Initialize the menu buttons
		variable1 = new MenuButton("Variable 1");
		variable2 = new MenuButton("Variable 2");
		listView.setItems(this.model.headersProperty());
		//Convert the list to a list of MenuItems
		for(String s: model.getHeaders()){
			MenuItem mItem1 = new MenuItem(s);
			mItem1.setOnAction(e -> {
				model.setVariable1(s);
			});
			MenuItem mItem2 = new MenuItem(s);
			mItem1.setOnAction(e -> {
				model.setVariable2(s);
			});
			variable1.getItems().add(mItem1);
			variable2.getItems().add(mItem2);
		}
	}
	public void initialize() {
		listView.setOnMouseClicked(e -> {
			if(e.getButton()== MouseButton.PRIMARY && e.getClickCount() == 2){
				if(!isListSelected){
					model.setVariable1(listView.getSelectionModel().getSelectedItem());
					variable1.setText(model.getVariable1());
					isListSelected = true;
				}
				else{
					model.setVariable2(listView.getSelectionModel().getSelectedItem());
					variable2.setText(model.getVariable2());
					isListSelected = false;
				}
			}
		});
	}
}

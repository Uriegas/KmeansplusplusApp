package com.uriegas;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

/**
 * Main Apllication of the project
 */
public class MainApp extends Application {
    /**
     * Data model for the application
     */
    private Model model = new Model();
    /**
     * Start method
     */
    @Override
    public void start(Stage stage) throws Exception {
        //-->Load data model from file
        try{
            Files.createDirectory(new File(System.getProperty("user.home") + "/.KmeansApp").toPath());
        }catch(Exception ex){System.out.println("Hidden directory exists");}
        File file = new File(System.getProperty("user.home") + "/.KmeansApp/Model.ser");
        if(!file.exists())
            file.createNewFile();
        try(ObjectInputStream out = new ObjectInputStream(new FileInputStream(file))){
            model = (Model)out.readObject();
            System.out.printf("Deserialized data from /Model.ser");
        } catch (Exception i) {
            System.out.println("Error loading data model at: " + file.getAbsolutePath());
        }
        //-->Load data model from file

        //-->Create main window
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/Scene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Window main = loader.getController();
        main.initModel(model);
        scene.getStylesheets().add(getClass().getResource("/styles/Styles.css").toExternalForm());
        stage.setTitle("Kmeans++ distribuido");
        stage.setScene(scene);
        //<--Create main window

        //-->When trying to close the app
        stage.setOnCloseRequest(event -> {
            Alert closeConfirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Está seguro que desea salir?"
            );
            Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
            Button closeButton = (Button)closeConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setText("Cancelar");
            exitButton.setText("Salir");
            closeConfirmation.setHeaderText("Confirmación de Salida");
            closeConfirmation.initModality(Modality.APPLICATION_MODAL);
            closeConfirmation.initOwner(stage);

            Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();

            if (!ButtonType.OK.equals(closeResponse.get()))
                event.consume();
            else
                Platform.exit();
        });
        //<--When trying to close window
        stage.show();
    }
    /**
     * Stop method: when application is closed save data model.<p>
     * In reality we only save some trivial data.
     */
    @Override
    public void stop(){
        File file = new File(System.getProperty("user.home") + "/.KmeansApp/Model.ser");
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))){
            out.writeObject(model);
            System.out.println("Serialized data at: " + file.getAbsolutePath());
        } catch (Exception i) {
            System.out.println("Couldn't serialize data to: " + file.getAbsolutePath());
        }
    }
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

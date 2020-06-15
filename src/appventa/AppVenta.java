/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appventa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author loizp
 */
public class AppVenta extends Application{
    
    public static Scene scene;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            BorderPane ventanaPrincipal = (BorderPane)FXMLLoader.load(getClass().getResource("VentanaPrincipal.fxml"));
            scene = new Scene(ventanaPrincipal);
//            scene.getStylesheets().add(getClass().getResource("ventanaprincipal.css").toExternalForm());
            primaryStage.setTitle("Punto de Venta");
            primaryStage.setScene(scene);
            primaryStage.show();
	} catch(Exception e) {
            e.printStackTrace();
	}
    }
    
}

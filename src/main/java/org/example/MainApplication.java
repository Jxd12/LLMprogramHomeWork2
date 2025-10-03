package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.MainController;


public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();
            MainController controller = loader.getController();
            
            Scene scene = new Scene(root, 1200, 800);
            primaryStage.setTitle("水印工具");
            primaryStage.setScene(scene);
            primaryStage.show();
            
            // 加载上次配置
            controller.loadLastConfiguration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

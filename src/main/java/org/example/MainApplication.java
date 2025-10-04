package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.MainController;

import java.util.Arrays;


public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("Loading FXML...");
            System.out.flush(); // 强制刷新输出流
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            if (loader.getLocation() == null) {
                System.err.println("FXML file not found!");
                System.err.flush(); // 强制刷新输出流
                return;
            }
            Parent root = loader.load();
            System.out.println("FXML loaded successfully");
            System.out.flush(); // 强制刷新输出流
            MainController controller = loader.getController();
            
            Scene scene = new Scene(root, 1200, 800);
            primaryStage.setTitle("水印工具");
            primaryStage.setScene(scene);
            primaryStage.show();

            // 添加窗口关闭事件处理
            primaryStage.setOnCloseRequest(event -> {
                System.out.println("Application closing...");
                System.out.flush(); // 强制刷新输出流
                // 可以在这里添加资源清理代码
                System.exit(0);
            });
            
            // 加载上次配置
            controller.loadLastConfiguration();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error starting application: " + e.getMessage());
            System.err.flush(); // 强制刷新输出流
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Main method called with args: " + Arrays.toString(args));
        System.out.flush(); // 强制刷新输出流
        launch(args);
    }
}

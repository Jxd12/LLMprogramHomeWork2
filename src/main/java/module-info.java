module org.example.watermark {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;

    opens org.example to javafx.fxml;
    opens org.example.controller to javafx.fxml;
    opens org.example.model to com.fasterxml.jackson.databind;

    exports org.example;
    exports org.example.util; // 添加这一行
}

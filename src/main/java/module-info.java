module com.example.shooter {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.example.shooter to javafx.fxml;
    exports com.example.shooter.server;
    opens com.example.shooter.server to javafx.fxml;
    exports com.example.shooter;
}
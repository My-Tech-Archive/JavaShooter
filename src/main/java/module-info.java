module com.example.shooter {
    requires java.naming;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.persistence;
    requires jdk.unsupported;


    opens com.example.shooter to javafx.fxml;
    exports com.example.shooter.server;
    opens com.example.shooter.server to javafx.fxml;
    exports com.example.shooter;
    exports com.example.shooter.database;
    opens com.example.shooter.database to javafx.fxml;
}
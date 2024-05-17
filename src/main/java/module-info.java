module com.example.egringotts {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;


    opens com.example.egringotts to javafx.fxml;
    exports com.example.egringotts;
    exports controllers;
    opens controllers to javafx.fxml;
}
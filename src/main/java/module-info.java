module com.example.egringotts {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.egringotts to javafx.fxml;
    exports com.example.egringotts;
    exports controllers;
    opens controllers to javafx.fxml;
}
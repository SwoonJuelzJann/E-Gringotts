package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import static com.example.egringotts.main.activeUsername;
import static com.example.egringotts.main.mongo;

public class dashboardController {
    @FXML
    private Label balance_K,balance_S,balance_G;

    public void initialize() {
        balance_K.setText(String.format("%.2f", mongo.findBalance_K(activeUsername)));
        balance_S.setText(String.format("%.2f", mongo.findBalance_S(activeUsername)));
        balance_G.setText(String.format("%.2f", mongo.findBalance_G(activeUsername)));
    }
}

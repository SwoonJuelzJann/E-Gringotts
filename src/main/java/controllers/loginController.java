package controllers;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

import static com.example.egringotts.main.mongo;

public class loginController {
    @FXML
    private Button loginButton,goblinLoginButton;
    @FXML
    private AnchorPane loginPane,loginPic, mainPane;
    @FXML
    private StackPane parentContainer;
    @FXML
    private TextField usernameTextfield;
    @FXML
    private PasswordField passwordTextfield;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private AnchorPane child;

    public void login(ActionEvent event) throws IOException {
        BorderPane mainPage = FXMLLoader.load(getClass().getResource("/com/example/egringotts/main_page.fxml"));
        AnchorPane dashboard = FXMLLoader.load(getClass().getResource("/com/example/egringotts/dashboard.fxml"));
        StackPane stackPane = (StackPane) mainPage.getRight();

        Scene scene = new Scene(mainPage);
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

        stackPane.getChildren().add(dashboard);
    }

    public void goblinLogin(ActionEvent event) throws IOException {
        AnchorPane goblinPage = FXMLLoader.load(getClass().getResource("/com/example/egringotts/goblinPage.fxml"));
        Scene scene = new Scene(goblinPage);
        Stage stage = (Stage) goblinLoginButton.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}

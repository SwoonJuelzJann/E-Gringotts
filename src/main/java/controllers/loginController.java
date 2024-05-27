package controllers;

import com.example.egringotts.MongoDBConnection;
import com.example.egringotts.security;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Objects;

import static com.example.egringotts.main.activeUsername;
import static com.example.egringotts.main.mongo;

public class loginController{
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
    @FXML
    private Label warningLabel;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private AnchorPane child;

    public void initialize(){
        warningLabel.setVisible(false);
    }

    public void login(ActionEvent event) throws IOException {
        //if(mongo.findByUsername(MongoDBConnection.connection(database))){
        String username = usernameTextfield.getText();
        String password = passwordTextfield.getText();

        if (!username.isEmpty() && !password.isEmpty() ) {
            if (validateLogin(username, password)) {
                activeUsername=username;
                BorderPane mainPage = FXMLLoader.load(getClass().getResource("/com/example/egringotts/main_page.fxml"));
                AnchorPane dashboard = FXMLLoader.load(getClass().getResource("/com/example/egringotts/dashboard.fxml"));
                StackPane stackPane = (StackPane) mainPage.getRight();

                Scene scene = new Scene(mainPage);
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();

                stackPane.getChildren().add(dashboard);


            } else {
                warningLabel.setVisible(true);
                warningLabel.setText("Invalid username or password");
            }
        } else {
            warningLabel.setVisible(true);
        }




    }

    public void goblinLogin(ActionEvent event) throws IOException {
        AnchorPane goblinPage = FXMLLoader.load(getClass().getResource("/com/example/egringotts/goblinPage.fxml"));
        Scene scene = new Scene(goblinPage);
        Stage stage = (Stage) goblinLoginButton.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public boolean validateLogin(String username, String password){
        //connection();
        //super.findByUsername(accountsCollection,text);
        if(mongo.findByUsername(mongo.accountsCollection,username)==null){
            return false;
        }

        String storedPassword = mongo.findPassword(username);
        return security.verifyPassword(password, storedPassword);
    }
}

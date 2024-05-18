package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static com.example.egringotts.main.activeUsername;
import static com.example.egringotts.main.mongo;

public class goblinPageController {
    @FXML
    private Label nameLabel;
    @FXML
    private ImageView avatarImage;
    @FXML
    private Button logoutButton;

    public void initialize() throws IOException {
        nameLabel.setText(mongo.findFirstName(activeUsername));
        avatarImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(mongo.findAvatar(activeUsername))))); //reminder image path kena ada '/' kat depan
    }

    public void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/egringotts/login.fxml")));
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}

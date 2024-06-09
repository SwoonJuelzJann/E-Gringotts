package com.example.egringotts;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.bson.Document;

import java.util.Objects;

import static com.example.egringotts.MongoDBConnection.accountsCollection;

public class main extends Application {

    public static String activeUsername = "DsPalat123";  //testing
    public static MongoDBConnection mongo;
    public static GMailer emailSender;

    @Override
    public void start(Stage stage) throws Exception {
        try{
            mongo = new MongoDBConnection();
            MongoDBConnection.connection();
            emailSender = new GMailer();



            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/WizardHat.png"))));
            stage.setTitle("E-Gringotts");
            stage.setResizable(false);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

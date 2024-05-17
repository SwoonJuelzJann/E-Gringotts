package com.example.egringotts;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.bson.Document;

import static com.example.egringotts.MongoDBConnection.accountsCollection;

public class main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        try{
            MongoDBConnection mongo = new MongoDBConnection();
            MongoDBConnection.connection();
            Document found = mongo.findByUsername(accountsCollection,"Tester321");
            System.out.println(found.get("username"));

            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/WizardHat.png")));
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

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

    public static String activeUsername = "DsPalat123";  //testing
    public static MongoDBConnection mongo;

    @Override
    public void start(Stage stage) throws Exception {
        try{
            mongo = new MongoDBConnection();
            mongo.connection();
//            Document found = mongo.findByUsername(accountsCollection,"Tester321");
//            System.out.println(found.get("username"));
//            System.out.println(mongo.findFirstName("Tester321"));
//            System.out.println(mongo.findLastName("Tester321"));
//            System.out.println(mongo.findPhoneNo("Tester321"));
//            System.out.println(mongo.findUserAddress("Tester321"));
//            System.out.println(mongo.findPostcode("Tester321"));
//            System.out.println(mongo.findUserType("Tester321"));
//            System.out.printf("%.2f",mongo.findBalance_K("DsPalat123"));
//
//            System.out.println(mongo.findAmount("Tester321"));


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

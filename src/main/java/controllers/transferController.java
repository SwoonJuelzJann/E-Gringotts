package controllers;

import com.example.egringotts.security;
import com.example.egringotts.transaction;
import com.mongodb.client.FindIterable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.egringotts.MongoDBConnection.accountsCollection;
import static com.example.egringotts.main.activeUsername;
import static com.example.egringotts.main.mongo;

public class transferController {
    @FXML
    private ChoiceBox<String> usernameChoice,currencyChoice,categoryChoice,searchChoice;
    @FXML
    private TextField searchField,amountField, pinField;
    @FXML
    private Label searchErrorLabel,transferErrorLabel;

    public void initialize() {
        categoryChoice.setItems(FXCollections.observableArrayList("Food","Transportation","Property","Utilities","Healthcare",
                                                                    "Entertainment","Clothing/Vanity","Personal Care/Hygene","Others"));
        currencyChoice.setItems(FXCollections.observableArrayList("Knut(K)","Sickle(S)","Galleon(G)"));
        searchChoice.setItems(FXCollections.observableArrayList("username","firstName","lastName","phoneNo","email"));
        searchErrorLabel.setVisible(false);
        transferErrorLabel.setVisible(false);
        amountField.setTextFormatter(createNumericTextFormatter());
    }

    public void searchUser(ActionEvent event) {
        Document filter = new Document(searchChoice.getValue(), searchField.getText());
        FindIterable<Document> iterable = accountsCollection.find(filter);    //list out all matching values
        ObservableList<String> usernameList = FXCollections.observableArrayList();

        for (Document doc : iterable) {                             //iterating through docs, getting the username based on the values gotten
            String username = (String) doc.get("username");         //adding them into the list
            usernameList.add(username);
        }

        if (usernameList.isEmpty()){
            searchErrorLabel.setText("NO USERNAMES FOUND, CHOICES NOT UPDATED");
            searchErrorLabel.setTextFill(Color.RED);
            searchErrorLabel.setVisible(true);
        }
        else {
            usernameChoice.setItems(usernameList);       //add all username options into the choice box
            searchErrorLabel.setText("FOUND USERNAMES, CHOICES UPDATED");
            searchErrorLabel.setTextFill(Color.GREEN);
            searchErrorLabel.setVisible(true);
        }
    }

    public void transfer(ActionEvent event) {

        String pin = pinField.getText();

        if (pin.isEmpty()) {
            transferErrorLabel.setVisible(true);
            transferErrorLabel.setText("PLEASE FILL IN YOUR PIN");
            return;
        }
        if (!validatePin(pin)) {
            transferErrorLabel.setVisible(true);
            transferErrorLabel.setText("INVALID PIN");
            return;
        }

        if (usernameChoice.getValue()==null || currencyChoice.getValue()==null || categoryChoice.getValue()==null) {    //check if fields are empty
            transferErrorLabel.setText("PLEASE FILL IN ALL REQUIRED FIELDS");
            transferErrorLabel.setTextFill(Color.RED);
            transferErrorLabel.setVisible(true);
            return;
        }
        if (Double.parseDouble(amountField.getText())==0) {                     //check if amount transfer >0
            transferErrorLabel.setText("PLEASE FILL IN AMOUNT TO TRANSFER");
            transferErrorLabel.setTextFill(Color.RED);
            transferErrorLabel.setVisible(true);
            return;
        }
        if (currencyChoice.getValue().equals("Knut(K)")){                       //check if balance is sufficient & not restricted by usertype
            if (Double.parseDouble(amountField.getText()) > mongo.findBalance_K(activeUsername) || Double.parseDouble(amountField.getText()) > mongo.findMaxTransferK(mongo.findUserType(activeUsername))) {
                transferErrorLabel.setText("INSUFFICIENT FUNDS");
                transferErrorLabel.setTextFill(Color.RED);
                transferErrorLabel.setVisible(true);
                return;
            }
            else {
                addAndRemoveFromBalance("balance_K",Double.parseDouble(amountField.getText()) );
            }
        }
        else if (currencyChoice.getValue().equals("Sickle(S)")){
            if (Double.parseDouble(amountField.getText()) > mongo.findBalance_S(activeUsername) || Double.parseDouble(amountField.getText()) > mongo.findMaxTransferS(mongo.findUserType(activeUsername))) {
                transferErrorLabel.setText("INSUFFICIENT FUNDS");
                transferErrorLabel.setTextFill(Color.RED);
                transferErrorLabel.setVisible(true);
                return;
            }
            else {
                addAndRemoveFromBalance("balance_S",Double.parseDouble(amountField.getText()));
            }
        }
        else if (currencyChoice.getValue().equals("Galleon(G)")){
            if (Double.parseDouble(amountField.getText()) > mongo.findBalance_G(activeUsername) || Double.parseDouble(amountField.getText()) > mongo.findMaxTransferG(mongo.findUserType(activeUsername))) {
                transferErrorLabel.setText("INSUFFICIENT FUNDS");
                transferErrorLabel.setTextFill(Color.RED);
                transferErrorLabel.setVisible(true);
                return;
            }
            else {
                addAndRemoveFromBalance("balance_G",Double.parseDouble(amountField.getText()));
            }
        }

        transaction newtransaction = new transaction(activeUsername,usernameChoice.getValue(),Double.parseDouble(amountField.getText()),currencyChoice.getValue(),categoryChoice.getValue());
        mongo.addTransactionDocument(newtransaction);
        transferErrorLabel.setText("TRANSFER SUCCESSFUL");
        transferErrorLabel.setTextFill(Color.GREEN);
        transferErrorLabel.setVisible(true);
    }

    private void addAndRemoveFromBalance(String balance_K, double total) {
        Document sender = (Document) accountsCollection.find(new Document("username", activeUsername)).first();     //find document under username
        Bson updateSender = new Document(balance_K,mongo.findBalance(balance_K,activeUsername)-total);                      //creates updated entry
        accountsCollection.updateOne(sender,new Document("$set", updateSender));                                    //updates document with the entry

        Document receiver = (Document) accountsCollection.find(new Document("username",usernameChoice.getValue())).first();     //process repeated for receiver
        Bson updateReceiver = new Document(balance_K,mongo.findBalance(balance_K,usernameChoice.getValue())+total);
        accountsCollection.updateOne(receiver,new Document("$set", updateReceiver));
    }

    public boolean validatePin(String pin){
        String storedPin = mongo.findPin(pin);
        if(pin.length() < 6){
            return false;
        }
        return security.verifyPassword(pin, storedPin);
    }

    //method to allow only digits and decimal values in textfield
    public static TextFormatter<Double> createNumericTextFormatter() {
        String pattern = "^[0-9]*\\.?[0-9]+$";
        Pattern decimalPattern = Pattern.compile(pattern);
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (!change.isContentChange()) {
                return change;
            }

            Matcher matcher = decimalPattern.matcher(change.getControlNewText());
            if (matcher.matches()) {
                return change;
            }

            return null;
        };

        TextFormatter<Double> textFormatter = new TextFormatter<>(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                return String.format("%.2f", value);
            }

            @Override
            public Double fromString(String string) {
                try {
                    return Double.parseDouble(string);
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            }
        }, 0.0, filter);

        return textFormatter;
    }
}

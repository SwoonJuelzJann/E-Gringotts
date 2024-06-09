package controllers;

import com.example.egringotts.transaction;
import com.mongodb.client.FindIterable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Date;

import static com.example.egringotts.MongoDBConnection.transactionsCollection;
import static com.example.egringotts.main.activeUsername;
import static com.example.egringotts.main.mongo;
import static com.example.egringotts.MongoDBConnection.accountsCollection;

public class exchangeController  {

    @FXML
    private TextField myText;
    @FXML
    private ChoiceBox<String> oldCurrency,newCurrency;
    @FXML
    private Label message;
    @FXML
    private Button exchange;

    double amount,fee,totalDeduct,originalAmount;
    String changeFrom, changeTo;
    public ObservableList<transaction> transactionList;

    dashboardController dashboard = new dashboardController();

    public void initialize() {
        oldCurrency.setItems(FXCollections.observableArrayList("Knut(K)","Sickle(S)","Galleon(G)"));
        newCurrency.setItems(FXCollections.observableArrayList("Knut(K)","Sickle(S)","Galleon(G)"));

        Document filter = new Document("username", activeUsername);
        FindIterable<Document> iterable = transactionsCollection.find(filter);    //list out all matching values from database
        transactionList = FXCollections.observableArrayList();

        for (Document doc : iterable) {                                          //iterating through docs, getting the transaction info
            transaction pastTransaction = new transaction(activeUsername,
                    (String) doc.get("receiverUsername"),
                    (Double) doc.get("amount"),
                    (String) doc.get("currency"),
                    (String) doc.get("category"),
                    (Date) doc.get("date"));
            transactionList.add(pastTransaction);                                   //adding them into the list
        }
        getUserTypeeee(transactionList.size());
        fee = mongo.findExchangeFee(getUserTypeeee(transactionList.size()));
    }

    public void exchangeCurrency(ActionEvent event) {
        try {
            amount = Double.parseDouble(myText.getText());
            changeFrom = oldCurrency.getValue();
            changeTo = newCurrency.getValue();
            fee = mongo.findExchangeFee(getUserTypeeee(transactionList.size()));

            if (changeFrom == null || changeTo == null) {
                message.setText("Please select both currencies.");
                return;
            }
            if(changeFrom.equalsIgnoreCase(changeTo)){
                message.setText("YOU CANNOT EXCHANGE TO THE SAME CURRENCY");
            }
            else if(changeFrom.equalsIgnoreCase("Knut(K)") && changeTo.equalsIgnoreCase("Sickle(S)")){
                if(amount+(fee*amount) <= mongo.findBalance_K(activeUsername) ) {     //checking if amount <= balance+fee

                    System.out.println(fee);

                    if (fee == 0) {
                        message.setText("Error retrieving exchange fee. Please try again.");
                        return;
                    }
                    totalDeduct = ( (fee*amount) + amount);                  //totalDeduct
                    originalAmount = amount;
                    amount = amount*29;

                    updateCurencyValue("balance_K",totalDeduct,"balance_S",amount);
                    //message.setText("EXCHANGE "+ amount +" FROM "+changeFrom+" TO "+changeTo+" IS SUCCESSFULL !");
                    setMessage();
                }else {
                    message.setText("INSUFFICIENT AMOUNT TO EXCHANGE.");
                }

            }else if(changeFrom.equalsIgnoreCase("Knut(K)") && changeTo.equalsIgnoreCase("Galleon(G)")){
                if(amount+(fee*amount) <= mongo.findBalance_K(activeUsername)) {

                    System.out.println(fee);

                    if (fee == 0) {
                        message.setText("Error retrieving exchange fee. Please try again.");
                        return;
                    }
                    totalDeduct = ( (fee*amount) + amount);              //totalDeduct
                    originalAmount = amount;
                    amount = amount*493;

                    updateCurencyValue("balance_K",totalDeduct,"balance_G",amount);
                    //message.setText("EXCHANGE "+ amount +" FROM "+changeFrom+" TO "+changeTo+" IS SUCCESSFULL !");
                    setMessage();
                }else {
                    message.setText("INSUFFICIENT AMOUNT TO EXCHANGE.");
                }

            }else if(changeFrom.equalsIgnoreCase("Sickle(S)") && changeTo.equalsIgnoreCase("Knut(K)")){
                if(amount+(fee*amount) <= mongo.findBalance_S(activeUsername)) {

                    System.out.println(fee);

                    if (fee == 0) {
                        message.setText("Error retrieving exchange fee. Please try again.");
                        return;
                    }
                    totalDeduct = ( (fee*amount) + amount);               //totalDeduct
                    originalAmount = amount;
                    amount = amount/29;

                    updateCurencyValue("balance_S",totalDeduct,"balance_K",amount);
                    //message.setText("EXCHANGE "+ amount +" FROM "+changeFrom+" TO "+changeTo+" IS SUCCESSFULL !");
                    setMessage();
                }else {
                    message.setText("INSUFFICIENT AMOUNT TO EXCHANGE.");
                }

            }else if(changeFrom.equalsIgnoreCase("Sickle(S)") && changeTo.equalsIgnoreCase("Galleon(G)")){
                if(amount+(fee*amount) <= mongo.findBalance_S(activeUsername)) {

                    System.out.println(fee);

                    if (fee == 0) {
                        message.setText("Error retrieving exchange fee. Please try again.");
                        return;
                    }
                    totalDeduct = ( (fee*amount) + amount);              //totalDeduct
                    originalAmount = amount;
                    amount = amount*17;

                    updateCurencyValue("balance_S",totalDeduct,"balance_G",amount);
                    //message.setText("EXCHANGE "+ amount +" FROM "+changeFrom+" TO "+changeTo+" IS SUCCESSFULL !");
                    setMessage();
                }else {
                    message.setText("INSUFFICIENT AMOUNT TO EXCHANGE.");
                }

            }else if(changeFrom.equalsIgnoreCase("Galleon(G)") && changeTo.equalsIgnoreCase("Knut(K)")){
                if(amount+(fee*amount) <= mongo.findBalance_G(activeUsername)) {

                    System.out.println(fee);

                    if (fee == 0) {
                        message.setText("Error retrieving exchange fee. Please try again.");
                        return;
                    }
                    totalDeduct = ( (fee*amount) + amount);              //totalDeduct
                    originalAmount = amount;
                    amount = amount/493;

                    updateCurencyValue("balance_G",totalDeduct,"balance_K",amount);
                    //message.setText("EXCHANGE "+ amount +" FROM "+changeFrom+" TO "+changeTo+" IS SUCCESSFULL !");
                    setMessage();
                }else {
                    message.setText("INSUFFICIENT AMOUNT TO EXCHANGE.");
                }

            }else {
                if(amount+(fee*amount) <= mongo.findBalance_G(activeUsername)) {

                    System.out.println(fee);

                    if (fee == 0) {
                        message.setText("Error retrieving exchange fee. Please try again.");
                        return;
                    }
                    totalDeduct = ( (fee*amount) + amount);              //totalDeduct
                    originalAmount = amount;
                    amount = amount/17;

                    updateCurencyValue("balance_G",totalDeduct,"balance_S",amount);
                    //message.setText("EXCHANGE "+ amount +" FROM "+changeFrom+" TO "+changeTo+" IS SUCCESSFULL !");
                    setMessage();
                }else {
                    message.setText("INSUFFICIENT AMOUNT TO EXCHANGE.");
                }

            }
        }
        catch (NumberFormatException e) {
            message.setText("PLEASE ENTER A VALID AMOUNT ONLY !");
        }
    }

    private void updateCurencyValue(String balanceFrom, double includedFee, String balanceTo, double amount) {
        Document user = (Document) accountsCollection.find(new Document("username", activeUsername)).first();     //find document under username
        Bson updateFrom = new Document(balanceFrom, mongo.findBalance(balanceFrom, activeUsername) - includedFee); //creates updated entry
        accountsCollection.updateOne(user, new Document("$set", updateFrom));                                    //updates document with the entry

        Document userr = (Document) accountsCollection.find(new Document("username", activeUsername)).first();
        Bson updateTo = new Document(balanceTo, mongo.findBalance(balanceTo, activeUsername) + amount);         //creates updated entry
        accountsCollection.updateOne(userr, new Document("$set", updateTo));                                    //updates document with the entry

    }

    private String getUserTypeeee(int size){
        if (size > 20) {
            return "Platinum Patronus";
        } else if (size > 10) {
            return "Golden Galleon";
        } else if (size > 5) {
            return "Silver Snitch";
        } else {
            return "Bronze Bargainer";
        }
        //return dashboard.getUserType();
    }

    private void setMessage(){
        String formattedOriAmount = String.format("%.2f", originalAmount);
        String formattedTotalDeduct = String.format("%.2f", totalDeduct);
        String formattedFee = String.format("%.2f", (fee*originalAmount));
        String formattedAmount = String.format("%.2f", amount);

        message.setText("EXCHANGE "+ formattedOriAmount +" FROM "+changeFrom+" TO "+changeTo+" IS SUCCESSFULL !"
        +"\n"+formattedOriAmount+" FROM "+changeFrom+" + "+ formattedFee +"(fee)"
        +"\n"+formattedAmount+" TO "+changeTo);
    }

    }

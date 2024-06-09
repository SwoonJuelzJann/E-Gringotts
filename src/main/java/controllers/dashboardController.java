package controllers;

import com.example.egringotts.transaction;
import com.mongodb.client.FindIterable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Date;

import static com.example.egringotts.MongoDBConnection.accountsCollection;
import static com.example.egringotts.MongoDBConnection.transactionsCollection;
import static com.example.egringotts.main.activeUsername;
import static com.example.egringotts.main.mongo;

public class dashboardController {
    @FXML
    private Label balance_K,balance_S,balance_G,userTypeLabel,maxTransferK,maxTransferS,maxTransferG,exchangeFee,remainingTransactions;
    @FXML
    private TableView<transaction> transferHistoryTable;
    @FXML
    private TableColumn<transaction,String> receiverColumn,currencyColumn,categoryColumn;
    @FXML
    private TableColumn<transaction,Double> amountColumn;
    @FXML
    private TableColumn<transaction,Date> dateColumn;
    @FXML
    private Rectangle userTypeBlock;
    @FXML
    private ChoiceBox<String> filterButton;

    public ObservableList<transaction> transactionList;
    public String userType;

    public void initialize() {
        // ~~BALANCE DISPLAY

        balance_K.setText(String.format("%.2f", mongo.findBalance_K(activeUsername)));
        balance_S.setText(String.format("%.2f", mongo.findBalance_S(activeUsername)));
        balance_G.setText(String.format("%.2f", mongo.findBalance_G(activeUsername)));

        // ~~TRANSACTION HISTORY DISPLAY
        loadTransaction();

        filterButton.setItems(FXCollections.observableArrayList("All","Food","Transportation","Property","Utilities","Healthcare",
                "Entertainment","Clothing/Vanity","Personal Care/Hygene","Others"));
        filterButton.setValue("All");
        filterButton.setOnAction(e -> filter());

        // ~~USERTYPE DISPLAY

        checkUserType(transactionList.size());
    }
    public void loadTransaction(){
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

        receiverColumn.setCellValueFactory(new PropertyValueFactory<>("receiverUsername"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        currencyColumn.setCellValueFactory(new PropertyValueFactory<>("currency"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        transferHistoryTable.setItems(transactionList);

        dateColumn.setSortType(TableColumn.SortType.DESCENDING);
        transferHistoryTable.getSortOrder().add(dateColumn);
        transferHistoryTable.sort();
    }

    public void checkUserType(int totalTransactions) {
        //String userType;
        if (totalTransactions > 20) {
            userType = "Platinum Patronus";
            userTypeBlock.setFill(Color.LIGHTGRAY);
        } else if (totalTransactions > 10) {
            userType = "Golden Galleon";
            userTypeBlock.setFill(Color.GOLD);
        } else if (totalTransactions > 5) {
            userType = "Silver Snitch";
            userTypeBlock.setFill(Color.SILVER);
        } else {
            userType = "Bronze Bargainer";
            userTypeBlock.setFill(Color.valueOf("#CD7F32"));
        }
        userTypeLabel.setText(userType);
        maxTransferK.setText(String.format("%.2f", mongo.findMaxTransferK(userType)));
        maxTransferS.setText(String.format("%.2f", mongo.findMaxTransferS(userType)));
        maxTransferG.setText(String.format("%.2f", mongo.findMaxTransferG(userType)));
        exchangeFee.setText(mongo.findExchangeFee(userType) * 100+"%");
        System.out.println("dashboard: "+mongo.findExchangeFee(userType));
        remainingTransactions.setText("Perform "+(mongo.findTierUpQuota(userType)-totalTransactions+1)+" more transactions to tier up.");

        if (!mongo.findUserType(activeUsername).equals(userType)){          //updates usertype into database
            Document user = (Document) accountsCollection.find(new Document("username", activeUsername)).first();     //find document under username
            Bson updateUser = new Document("userType",userType);                                                      //creates updated entry
            accountsCollection.updateOne(user,new Document("$set", updateUser));
        }
    }

    public void filter(){
        String selectedCategory = filterButton.getValue();

        // If "All" is selected, display all transactions
        if ("All".equals(selectedCategory)) {
            transferHistoryTable.setItems(transactionList);
            return;
        }

        // Create a filtered list based on the selected category
        ObservableList<transaction> filteredList = FXCollections.observableArrayList();
        for (transaction t : transactionList) {
            if (t.getCategory().equals(selectedCategory)) {
                filteredList.add(t);
            }
        }

        transferHistoryTable.setItems(filteredList);
        transferHistoryTable.sort();
    }

}

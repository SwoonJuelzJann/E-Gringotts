package controllers;

import com.example.egringotts.transaction;
import com.mongodb.client.FindIterable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;

import java.util.Date;

import static com.example.egringotts.MongoDBConnection.transactionsCollection;
import static com.example.egringotts.main.activeUsername;
import static com.example.egringotts.main.mongo;

public class dashboardController {
    @FXML
    private Label balance_K,balance_S,balance_G;
    @FXML
    private TableView<transaction> transferHistoryTable;
    @FXML
    private TableColumn<transaction,String> receiverColumn,currencyColumn,categoryColumn;
    @FXML
    private TableColumn<transaction,Double> amountColumn;
    @FXML
    private TableColumn<transaction,Date> dateColumn;

    private ObservableList<transaction> transactionList;

    public void initialize() {
        balance_K.setText(String.format("%.2f", mongo.findBalance_K(activeUsername)));
        balance_S.setText(String.format("%.2f", mongo.findBalance_S(activeUsername)));
        balance_G.setText(String.format("%.2f", mongo.findBalance_G(activeUsername)));

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
}

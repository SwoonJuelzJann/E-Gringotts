package controllers;

import com.example.egringotts.transaction;
import com.mongodb.client.FindIterable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.egringotts.MongoDBConnection.transactionsCollection;
import static com.example.egringotts.main.activeUsername;

public class profileController {
    @FXML
    private PieChart expenditureChart;
    @FXML
    private ChoiceBox<String> currencyChoice,daysBeforeChoice;
    @FXML
    private Button loadButton;

    private ObservableList<transaction> transactionList;

    public void initialize(){
        currencyChoice.setItems(FXCollections.observableArrayList("Knut(K)","Sickle(S)","Galleon(G)"));
        currencyChoice.setValue("Knut(K)");
        daysBeforeChoice.setItems(FXCollections.observableArrayList("All time","Last 3 days","Last week","Last 14 days","Last month"));
        daysBeforeChoice.setValue("All time");

        Document filter = new Document("username", activeUsername);
        FindIterable<Document> iterable = transactionsCollection.find(filter);    //list out all matching values
        transactionList = FXCollections.observableArrayList();

        for (Document doc : iterable) {                                          //iterating through docs, getting the transaction info
            transaction pastTransaction = new transaction(activeUsername,
                    (String) doc.get("receiverUsername"),
                    (Double) doc.get("amount"),
                    (String) doc.get("currency"),
                    (String) doc.get("category"),
                    (Date) doc.get("date"));
            transactionList.add(pastTransaction);                                   //adding them into the list
        }                                                                           // by now dah ada list all transaction active user dah buat

    }

    public void loadPieChart(ActionEvent event) {
        loadButton.setText("Reload");
        expenditureChart.getData().clear();
        ObservableList<transaction> updatedTransactionList = FXCollections.observableArrayList();

        switch(daysBeforeChoice.getValue()){
            case "All time":
                updatedTransactionList.addAll(transactionList);
                break;
            case "Last 3 days":
                updatedTransactionList.setAll(getTransactionsDaysBefore(transactionList,3));
                break;
            case "Last week":
                updatedTransactionList.setAll(getTransactionsDaysBefore(transactionList,7));
                break;
            case "Last 14 days":
                updatedTransactionList.setAll(getTransactionsDaysBefore(transactionList,14));
                break;
            case "Last month":
                updatedTransactionList.setAll(getTransactionsDaysBefore(transactionList,30));
                break;
        }

        Map<String, Double> categoryToAmount = new HashMap<>();
        for (transaction tran : updatedTransactionList) {
            if (tran.getCurrency().equals(currencyChoice.getValue())){
                String category = tran.getCategory();
                double amount = tran.getAmount();
                if (categoryToAmount.containsKey(category)) {
                    amount += categoryToAmount.get(category);
                }
                categoryToAmount.put(category, amount);
            }

        }

        for (Map.Entry<String, Double> entry : categoryToAmount.entrySet()) {
            String category = entry.getKey();
            double amount = entry.getValue();
            PieChart.Data data = new PieChart.Data(category, amount);
            expenditureChart.getData().add(data);
        }

        double totalValue = expenditureChart.getData().stream().mapToDouble(PieChart.Data::getPieValue).sum();
        expenditureChart.setTitle(String.format("TOTAL SPENT: %.2f %s", totalValue,currencyChoice.getValue()));
        expenditureChart.setStartAngle(90);
        expenditureChart.getData().forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " (", String.format("%.2f", data.getPieValue() / totalValue * 100), "%)"
                        )
                )
        );
    }

    public ObservableList<transaction> getTransactionsDaysBefore(ObservableList<transaction> transactions,int days) {
        ObservableList<transaction> result = FXCollections.observableArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.add(Calendar.DAY_OF_MONTH, -days);            //get the date based on how many days before
        Date previousWeekStart = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, days);             //get the current date
        Date previousWeekEnd = calendar.getTime();

        for (transaction item : transactions) {
            Date itemDate = item.getDate();
            if (itemDate.after(previousWeekStart) && itemDate.before(previousWeekEnd)) {
                result.add(item);
            }
        }

        return result;
    }


}

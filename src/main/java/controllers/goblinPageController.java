package controllers;

import com.example.egringotts.account;
import com.example.egringotts.security;
import com.example.egringotts.transaction;
import com.mongodb.client.FindIterable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.egringotts.MongoDBConnection.accountsCollection;
import static com.example.egringotts.MongoDBConnection.transactionsCollection;
import static com.example.egringotts.main.*;

public class goblinPageController {
    @FXML
    private Label nameLabel,errorText,blockLabel,searchErrorLabel,addErrorLabel;
    @FXML
    private ImageView avatarImage,chosenAvatar;
    @FXML
    private Button logoutButton,loadButton,createAccountButton;
    @FXML
    private TextField usernameTextfield,FNameField,LNameField,emailField,numberField,addressField,postcodeField,KDepoField,SDepoField,GDepoField,depositAmount,searchField;
    @FXML
    private PasswordField passwordTextfield,verifyPasswordTextfield;
    @FXML
    private CheckBox goblinCheckbox;
    @FXML
    private AnchorPane newAccountTab,blockpane;
    @FXML
    private PieChart accountsChart;
    @FXML
    private ChoiceBox<String> searchChoice, usernameChoice,currencyChoice;

    private boolean isGoblin;
    private String choosenAvatarUrl;
    private ObservableList<transaction> transactionList;
    private ObservableList<account> accountsList;
    private String newAccEmailMessage = String.format("""
                Greetings Widard,

                New account registered.

                Best regards,
                goblin
                """);

    public void initialize() {
        nameLabel.setText(mongo.findFirstName(activeUsername));
        avatarImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(mongo.findAvatar(activeUsername))))); //reminder image path kena ada '/' kat depan
        KDepoField.setTextFormatter(createNumericTextFormatter());
        SDepoField.setTextFormatter(createNumericTextFormatter());
        GDepoField.setTextFormatter(createNumericTextFormatter());
        errorText.setVisible(false);
        choosenAvatarUrl = "/images/avatars/Air.png";

        FindIterable<Document> iterable_T = transactionsCollection.find();    //list out all matching values from database
        FindIterable<Document> iterable_A = accountsCollection.find();
        transactionList = FXCollections.observableArrayList();
        accountsList = FXCollections.observableArrayList();

        for (Document doc : iterable_T) {                                          //iterating through docs, getting the transaction info
            transaction transaction = new transaction((String) doc.get("username"),
                    (String) doc.get("receiverUsername"),
                    (Double) doc.get("amount"),
                    (String) doc.get("currency"),
                    (String) doc.get("category"),
                    (Date) doc.get("date"));
            transactionList.add(transaction);                                   //adding them into the list
        }

        //ADD DEPOSIT PAGE

        currencyChoice.setItems(FXCollections.observableArrayList("Knut(K)","Sickle(S)","Galleon(G)"));
        searchChoice.setItems(FXCollections.observableArrayList("username","firstName","lastName","phoneNo","email"));
        searchErrorLabel.setVisible(false);
        addErrorLabel.setVisible(false);
        depositAmount.setTextFormatter(createNumericTextFormatter());
    }

    //~~~~~~~GRINGOTTS INFO TAB~~~~~~~

    public void loadPieChart(ActionEvent event) {
        loadButton.setText("Reload");
        accountsChart.getData().clear();
        ObservableList<transaction> updatedTransactionList = FXCollections.observableArrayList();

        FindIterable<Document> documents = accountsCollection.find();
        Map<String, Integer> acctypeToAmount = new HashMap<>();
        for (Document doc : documents) {
            String accType = (String) doc.get("userType");
            int amount = 1;
            if (acctypeToAmount.containsKey(accType)) {
                amount += acctypeToAmount.get(accType);
            }
            acctypeToAmount.put(accType,amount);
        }
        for (Map.Entry<String, Integer> entry : acctypeToAmount.entrySet()) {
            String category = entry.getKey();
            double amount = entry.getValue();
            PieChart.Data data = new PieChart.Data(category, amount);
            accountsChart.getData().add(data);
        }

        int totalAccs = (int) accountsChart.getData().stream().mapToDouble(PieChart.Data::getPieValue).sum();
        accountsChart.setTitle("TOTAL ACCOUNTS : "+totalAccs);
        accountsChart.setStartAngle(90);
        accountsChart.getData().forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " (", (int) data.getPieValue(), ")"
                        )
                )
        );
    }


    public void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/egringotts/login.fxml")));
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }


        //~~~~~~~CREATE A NEW ACCOUNT TAB~~~~~~~

    public void chooseAvatarAir(MouseEvent event) {
        ImageView clickedAvatar = (ImageView) event.getSource();
        chosenAvatar.setImage(clickedAvatar.getImage());
        choosenAvatarUrl = "/images/avatars/Air.png";
    }

    public void chooseAvatarDeath(MouseEvent event) {
        ImageView clickedAvatar = (ImageView) event.getSource();
        chosenAvatar.setImage(clickedAvatar.getImage());
        choosenAvatarUrl = "/images/avatars/Death.png";
    }

    public void chooseAvatarFire(MouseEvent event) {
        ImageView clickedAvatar = (ImageView) event.getSource();
        chosenAvatar.setImage(clickedAvatar.getImage());
        choosenAvatarUrl = "/images/avatars/Fire.png";
    }

    public void chooseAvatarHydro(MouseEvent event) {
        ImageView clickedAvatar = (ImageView) event.getSource();
        chosenAvatar.setImage(clickedAvatar.getImage());
        choosenAvatarUrl = "/images/avatars/Hydro.png";
    }

    public void chooseAvatarLife(MouseEvent event) {
        ImageView clickedAvatar = (ImageView) event.getSource();
        chosenAvatar.setImage(clickedAvatar.getImage());
        choosenAvatarUrl = "/images/avatars/Life.png";
    }

    public void chooseAvatarPlant(MouseEvent event) {
        ImageView clickedAvatar = (ImageView) event.getSource();
        chosenAvatar.setImage(clickedAvatar.getImage());
        choosenAvatarUrl = "/images/avatars/Plant.png";
    }

    public void chooseAvatarSmoke(MouseEvent event) {
        ImageView clickedAvatar = (ImageView) event.getSource();
        chosenAvatar.setImage(clickedAvatar.getImage());
        choosenAvatarUrl = "/images/avatars/Smoke.png";
    }

    public void setGoblin(ActionEvent event) {
        isGoblin = goblinCheckbox.isSelected();
    }

    public void createAccount(ActionEvent event) throws Exception {
        if (usernameTextfield.getText().isEmpty() || FNameField.getText().isEmpty() || LNameField.getText().isEmpty() || emailField.getText().isEmpty()
            || numberField.getText().isEmpty() || addressField.getText().isEmpty() || postcodeField.getText().isEmpty() || KDepoField.getText().isEmpty()
            || SDepoField.getText().isEmpty() || GDepoField.getText().isEmpty()) {
            errorText.setVisible(true);
            errorText.setTextFill(Color.RED);
            errorText.setText("FILL IN ALL ACCOUNT CREDENTIALS");
            return;
        }
        if (passwordTextfield.getText().isEmpty() || !verifyPasswordTextfield.getText().equals(passwordTextfield.getText()) || passwordTextfield.getText().length() < 6) {
            errorText.setVisible(true);
            errorText.setTextFill(Color.RED);
            errorText.setText("PASSWORD SHOULD BE ATLEAST 6 CHARACTERS");
            return;
        }

        String pin = getPin();

        account newAcc = new account(usernameTextfield.getText(),
                passwordTextfield.getText(),
                FNameField.getText(),
                LNameField.getText(),
                numberField.getText(),
                addressField.getText(),
                postcodeField.getText(),
                "Bronze Bargainer",
                Double.parseDouble(KDepoField.getText()),
                Double.parseDouble(SDepoField.getText()),
                Double.parseDouble(GDepoField.getText()),
                isGoblin,
                choosenAvatarUrl,
                emailField.getText(),
                pin);

        emailSender.sendMail("WELCOME TO E-GRINGOTTS", String.format("""
                Greetings Wizard,

                You have successfully registered a new account with username %s.
                
                Your generated PIN number is %s. Do remember this PIN for your future endavours.

                Best regards,
                goblin
                """,newAcc.getUsername(),newAcc.getPin()));

        String password = newAcc.getPassword();
        String storedSaltedHash = security.getSaltedHash(password);
        newAcc.setPassword(storedSaltedHash);


        mongo.addAccountDocument(newAcc);
        errorText.setVisible(false);
        clearTextFields(newAccountTab);
        errorText.setVisible(true);
        errorText.setTextFill(Color.GREEN);
        errorText.setText("ACCOUNT CREATED");
    }

    public static String getPin() {
        Random random = new Random();

        int number = random.nextInt(900000) + 100000; // generate a 6-digit number between 100000 and 999999
        return String.valueOf(number);
    }

    public static void clearTextFields(AnchorPane pane) {
        for (Node node : pane.getChildren()) {
            if (node instanceof TextField) {
                TextField textField = (TextField) node;
                textField.clear();
            } else if (node instanceof AnchorPane) {
                clearTextFields((AnchorPane) node);
            }
        }
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

    //ADD DEPOSIT METHODS
    public void addDeposit(ActionEvent event){
        if (usernameChoice.getValue()==null || currencyChoice.getValue()==null) {    //check if fields are empty
            addErrorLabel.setText("PLEASE FILL IN ALL REQUIRED FIELDS");
            addErrorLabel.setTextFill(Color.RED);
            addErrorLabel.setVisible(true);
            return;
        }
        if (Double.parseDouble(depositAmount.getText())==0) {                     //check if amount transfer >0
            addErrorLabel.setText("PLEASE FILL IN AMOUNT TO TRANSFER");
            addErrorLabel.setTextFill(Color.RED);
            addErrorLabel.setVisible(true);
            return;
        }

        if (currencyChoice.getValue().equals("Knut(K)")){                       //check if balance is sufficient & not restricted by usertype
             double amount = Double.parseDouble(depositAmount.getText());
             addBalance("balance_K",amount);
        }
        else if (currencyChoice.getValue().equals("Sickle(S)")){                       //check if balance is sufficient & not restricted by usertype
            double amount = Double.parseDouble(depositAmount.getText());
            addBalance("balance_S",amount);
        }
        else if (currencyChoice.getValue().equals("Galleon(G)")){                       //check if balance is sufficient & not restricted by usertype
            double amount = Double.parseDouble(depositAmount.getText());
            addBalance("balance_G",amount);
        }else return;

        String currency = currencyChoice.getValue();
        double amount = Double.parseDouble(depositAmount.getText());
        String username = usernameChoice.getValue();
        String formattedAmount = String.format("%.2f", amount);

        addErrorLabel.setText(formattedAmount+" "+currency+" DEPOSITED TO \""+username+"\"");
        addErrorLabel.setTextFill(Color.GREEN);
        addErrorLabel.setVisible(true);
    }

    public void addBalance(String balance, double total){
        Document user = (Document) accountsCollection.find(new Document("username",usernameChoice.getValue())).first();     //process repeated for receiver
        Bson updateReceiver = new Document(balance,mongo.findBalance(balance,usernameChoice.getValue())+total);
        accountsCollection.updateOne(user,new Document("$set", updateReceiver));
    }
}

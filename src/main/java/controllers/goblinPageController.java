package controllers;

import com.example.egringotts.account;
import com.example.egringotts.security;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.egringotts.main.activeUsername;
import static com.example.egringotts.main.mongo;

public class goblinPageController {
    @FXML
    private Label nameLabel,errorText,blockLabel;
    @FXML
    private ImageView avatarImage,chosenAvatar;
    @FXML
    private Button logoutButton,reloadButton,createAccountButton;
    @FXML
    private TextField usernameTextfield,FNameField,LNameField,emailField,numberField,addressField,postcodeField,KDepoField,SDepoField,GDepoField;
    @FXML
    private PasswordField passwordTextfield,verifyPasswordTextfield;
    @FXML
    private CheckBox goblinCheckbox;
    @FXML
    private AnchorPane newAccountTab,blockpane;
    private boolean isGoblin;
    private String choosenAvatarUrl;

    public void initialize() {
        nameLabel.setText(mongo.findFirstName(activeUsername));
        avatarImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(mongo.findAvatar(activeUsername))))); //reminder image path kena ada '/' kat depan
        KDepoField.setTextFormatter(createNumericTextFormatter());
        SDepoField.setTextFormatter(createNumericTextFormatter());
        GDepoField.setTextFormatter(createNumericTextFormatter());
        errorText.setVisible(false);
    }

    public void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/egringotts/login.fxml")));
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

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

    public void createAccount(ActionEvent event) throws IOException {
        if (usernameTextfield.getText().isEmpty() || FNameField.getText().isEmpty() || LNameField.getText().isEmpty() || emailField.getText().isEmpty()
            || numberField.getText().isEmpty() || addressField.getText().isEmpty() || postcodeField.getText().isEmpty() || KDepoField.getText().isEmpty()
            || SDepoField.getText().isEmpty() || GDepoField.getText().isEmpty()) {
            errorText.setVisible(true);
            errorText.setText("FILL IN ALL ACCOUNT CREDENTIALS");
            return;
        }
        if (passwordTextfield.getText().isEmpty() || !verifyPasswordTextfield.getText().equals(passwordTextfield.getText()) || passwordTextfield.getText().length() < 6) {
            errorText.setVisible(true);
            errorText.setText("PASSWORD SHOULD BE ATLEAST 6 CHARACTERS");
            return;
        }

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
                emailField.getText());

        String password = newAcc.getPassword();
        String storedSaltedHash = security.getSaltedHash(password);
        newAcc.setPassword(storedSaltedHash);


        mongo.addAccountDocument(newAcc);
        errorText.setVisible(false);
        clearTextFields(newAccountTab);
        System.out.println("Account created");
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
}

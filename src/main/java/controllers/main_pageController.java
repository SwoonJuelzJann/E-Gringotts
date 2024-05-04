package controllers;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.util.Duration;

import java.io.IOException;

public class main_pageController {
    @FXML
    private Button dashboardButton,transferButton,exchangeButton,profileButton,logoutButton;
    @FXML
    private BorderPane borderPane_main;
    @FXML
    private StackPane stackPane_main;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private AnchorPane anchorPane;

    public void initialize() throws IOException {
    }

    public void logout(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/com/example/egringotts/login.fxml"));
        stage = (Stage) logoutButton.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void openDashboardPage(ActionEvent event) throws IOException {
        openPage("/com/example/egringotts/dashboard.fxml");

    }

    public void openTransferPage(ActionEvent event) throws IOException {
        openPage("/com/example/egringotts/transfer.fxml");

    }

    public void openExchangePage(ActionEvent event) throws IOException {
        openPage("/com/example/egringotts/exchange.fxml");

    }

    public void openProfilePage(ActionEvent event) throws IOException {
        openPage("/com/example/egringotts/profile.fxml");

    }

    public boolean openPage(String pageFile) throws IOException {
        anchorPane = FXMLLoader.load(getClass().getResource(pageFile));
        AnchorPane paneToRemove = (AnchorPane) stackPane_main.getChildren().get(0);
        if (isSamePage(anchorPane, paneToRemove)){
            return false;
        }

        anchorPane.translateYProperty().set(stackPane_main.getHeight());
        stackPane_main.getChildren().add(anchorPane);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(anchorPane.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event1 -> {
            stackPane_main.getChildren().remove(paneToRemove);
        });
        timeline.play();
        return true;
    }

    public static boolean isSamePage(AnchorPane pane1, AnchorPane page2) {
        if (pane1.getChildren().size() != page2.getChildren().size()) {
            return false;
        }

        for (int i = 0; i < pane1.getChildren().size(); i++) {
            Node node1 = pane1.getChildren().get(i);
            Node node2 = page2.getChildren().get(i);
            if (node1 instanceof AnchorPane) {
                if (!isSamePage((AnchorPane) node1, (AnchorPane) node2)) {
                    return false;
                }
            }
        }
        return true;
    }
}

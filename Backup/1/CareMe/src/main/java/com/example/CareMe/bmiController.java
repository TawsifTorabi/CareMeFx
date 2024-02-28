package com.example.CareMe;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class bmiController implements Initializable {

    Timeline alertTimer = new Timeline(new KeyFrame(Duration.seconds(600), event -> {
        showAlert();
    }));
    private void showAlert() {
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert!");
            alert.setContentText("You should give your eyes some rest!");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isEmpty()){
                System.out.println("Alert Closed");
                alertTimer.play();
            } else if (result.get()==ButtonType.OK) {
                System.out.println("OK!");
                alertTimer.play();
            } else if (result.get()==ButtonType.CANCEL) {
                System.out.println("Never!");
                alertTimer.play();

            }
        });

    }

    public void initialize(URL url, ResourceBundle resourceBundle){
        alertTimer.play();
    }



    @FXML private Label welcomeText;

    @FXML
    private TextField height;

    @FXML
    private TextField weight;

    @FXML
    private Label Result;

    @FXML private void SwitchToHome(ActionEvent event) throws IOException {
        Parent p2 = FXMLLoader.load(getClass().getResource("home.fxml"));
        Scene scene = new Scene(p2);
        Stage wind = (Stage)((Node)event.getSource()).getScene().getWindow();
        wind.setScene(scene);
    }


    @FXML
    void bmical(){
        String s1=height.getText();
        String s2=weight.getText();
        if(s1!=null && s2!=null) {
            double res = Double.parseDouble(s2) / ((Double.parseDouble(s1)) * (Double.parseDouble(s1)));
            Result.setText(res + "");
        }
    }

    @FXML void RecordData(){

    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    void BMI_Checker(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("bmi-counter.fxml"));
        Scene scene2 = new Scene(parent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(scene2);


    }
}
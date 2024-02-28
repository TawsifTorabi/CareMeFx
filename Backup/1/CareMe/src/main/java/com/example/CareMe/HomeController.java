package com.example.CareMe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeController implements Initializable {

    String username;
    @FXML private Text usernameTop;
    @FXML private Text bloodGroup;
    @FXML private Button DonateBtn;
    @FXML private Button BloodDonationHistoryBtn;
    @FXML private Text bloodNotificationText;
    @FXML private Text LastDonationDate;
    @FXML public BorderPane MainStageScreen;


    @FXML
    private void receiveData(ActionEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        User u = (User) stage.getUserData();
        username = u.getUsername();
    }


    @FXML private void SwitchToBMI(ActionEvent event) throws IOException {
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("bmi-counter.fxml");
        MainStageScreen.setCenter(view);
    }

    @FXML private void BackToLogin(ActionEvent event) throws IOException {
        BackToLogin((Stage)((Node)event.getSource()).getScene().getWindow());
    }
    private void BackToLogin(Stage stage) throws IOException {
        Parent p2 = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(p2);
        stage.setScene(scene);
    }

    @FXML
    private void SetBloodRecord(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Blood Donation Confirmation");
        alert.setContentText("You sure you want to Record you donation? \n You won't be able to record anything before 4 Months!");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            SetBloodRecordGO();
        }else{
            //Do Nothing
        }
    }

    private void SetBloodRecordGO(){

        //Set Variables
        long time = Instant.now().getEpochSecond();
        long last_donated_time = 0;

        //Create Connection
        connectivity ob = new connectivity();
        Connection conn = ob.connection();

        String query="SELECT * FROM `blood_donation_record` WHERE `user_id`=? ORDER BY `last_donated` DESC LIMIT 1";
        String RecordSql = "INSERT INTO `blood_donation_record` VALUES(DEFAULT, "+ User.userid +", "+ time +", '')";
        try {
            PreparedStatement Statement1 = conn.prepareStatement(query);
            Statement1.setString(1, User.userid);
            ResultSet rs = Statement1.executeQuery();

            //Count Blood Donation Rows
            int i = 0;
            if(rs.next()){
                last_donated_time = rs.getLong("last_donated");
                i++;
            }

            if(i >= 1){

                //long last_donation_date = gmdate("Y-m-d", last_donated_time);
                //String last_donation_date = gmdate("Y-m-d", $last_donated_time);
                //$current_date = gmdate("Y-m-d", $time);

                long dateDiff = time - last_donated_time;
                int dateCount = Integer.parseInt(String.valueOf((dateDiff / (60 * 60 * 24))));

                if(dateCount > 119){
                    //Send the Data to the SQL Server
                    try {
                        Statement s = conn.createStatement();
                        s.executeUpdate(RecordSql);
                        s.close();
                        System.out.println("Successfully Recorded Blood Donation Time!");
                        BloodDonationHistoryBtn.setVisible(true);
                        bloodNotificationText.setText("Recorded!");
                        GetBloodRecord();
                        DonateBtn.setVisible(false);
                        //Last Donated Time Calculation
                        long millisTime = User.lastDonated * 1000;
                        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d,yyyy", Locale.ENGLISH);
                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                        LastDonationDate.setText(sdf.format(new Date(millisTime)));

                    } catch (SQLException err) {
                        err.printStackTrace();
                        System.out.println("Error Recording Blood Donation Time!");
                    }
                }else{
                    System.out.println("4 Months Not Passed!");
                    bloodNotificationText.setText("4 Months Not Passed!");
                }


            }else if(i == 0){

                //Send the Data to the SQL Server
                try {
                    Statement s = conn.createStatement();
                    s.executeUpdate(RecordSql);
                    s.close();
                    System.out.println("Successfully Recorded Blood Donation Time!");
                    bloodNotificationText.setText("Recorded");
                    DonateBtn.setVisible(false);
                } catch (SQLException err) {
                    err.printStackTrace();
                    System.out.println("Error Recording Blood Donation Time!");
                }
            }
            rs.close();
            Statement1.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //Blood Donation Record Function
    @FXML
    public void GetBloodRecord(){

        //Set Variables
        long time = Instant.now().getEpochSecond();
        long last_donated_time = 0;

        //Create Connection
        connectivity ob = new connectivity();
        Connection conn = ob.connection();

        String query="SELECT * FROM `blood_donation_record` WHERE `user_id`=? ORDER BY `last_donated` DESC LIMIT 1";
        try {
            PreparedStatement Statement1 = conn.prepareStatement(query);
            Statement1.setString(1, User.userid);
            ResultSet rs = Statement1.executeQuery();

            //Count Blood Donation Rows
            int i = 0;
            if(rs.next()){
                last_donated_time = rs.getLong("last_donated");
                i++;
            }

            User.lastDonated = last_donated_time;

            if(i >= 1){

                long dateDiff = time - last_donated_time;
                int dateCount = Integer.parseInt(String.valueOf((dateDiff / (60 * 60 * 24))));

                if(dateCount > 119){
                    System.out.println("You are Eligible to Donate!");
                    bloodNotificationText.setText("You are Eligible!");
                    User.BloodBoolean = "true";
                }else{
                    System.out.println("4 Months Not Passed!");
                    bloodNotificationText.setText("4 Months Not Passed!");
                    User.BloodBoolean = "false";
                    DonateBtn.setVisible(false);
                }

                //Last Donated Time Calculation
                long millisTime = User.lastDonated * 1000;
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                //SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d,yyyy h:mm,a", Locale.ENGLISH); // Tuesday,November 1,2011 12:00,AM
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                LastDonationDate.setText(sdf.format(new Date(millisTime)));

            }else if(i == 0){
                System.out.println("You are Eligible to Donate! No Donation Records!");
                User.BloodBoolean = "true";
                bloodNotificationText.setText("Eligible to Donate!");

            }
            rs.close();
            Statement1.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }




    @FXML
    public void BloodDonationHistory(ActionEvent e){
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("BloodDonationRecords.fxml");
        MainStageScreen.setCenter(view);
    }
    

    @FXML
    public void LoadFriendsPane(){
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("friends-home.fxml");
        MainStageScreen.setLeft(view);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameTop.setText(User.firstname + " " + User.lastname);
        bloodGroup.setText(User.bloodGroup);
        LoadFriendsPane();
        GetBloodRecord();
        BloodDonationHistoryBtn.setVisible(true);
    }
}

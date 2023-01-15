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
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;



import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class bmiController implements Initializable{

    @FXML
    private TextField height;

    @FXML
    private TextField weight;

    @FXML
    private Label Result;

    @FXML
    private Text RecordNotification;

    @FXML
    private Label typem;

    @FXML void RecordData(){
        //Create Connection
        connectivity ob = new connectivity();
        Connection conn = ob.connection();

        String query="SELECT * FROM userdata WHERE `user_id`=?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, User.userid);
            ResultSet rs = pstmt.executeQuery();
            int i = 0;
            if(rs.next()){
                i++;
            }
            if(i > 0){

                try {
                    //Try Update Note
                    String userHeight = CustomFunction.getMysqlRealScapeString(height.getText());
                    String userWeight = CustomFunction.getMysqlRealScapeString(weight.getText());
                    String query2="UPDATE `userdata` SET `height`='"+userHeight+"', `weight`='"+userWeight+"' WHERE `userdata`.`user_id`='"+User.userid+"'";
                    PreparedStatement Statement1 = conn.prepareStatement(query2);
                    if(Statement1.execute()){
                        System.out.println("** ERROR Height Weight Record Update Not Executed");
                    }else{
//                      RecordNotification.setVisible(true);
                        RecordNotification.setText("Recorded!");
                    }

                    //Close Connection
                    Statement1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }else{
                try {
                    //Try Update Note
                    String userHeight = CustomFunction.getMysqlRealScapeString(height.getText());
                    String userWeight = CustomFunction.getMysqlRealScapeString(weight.getText());
                    String query3="INSERT INTO `userdata` VALUES ('"+User.userid+"','"+userHeight+"', '"+userWeight+"')";

                    Statement s = conn.createStatement();
                    s.executeUpdate(query3);
                    //Close Connection
                    s.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }



    public void loadData(){
        //Create Connection
        connectivity ob = new connectivity();
        Connection conn = ob.connection();

        String query="SELECT * FROM userdata WHERE `user_id`=?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, User.userid);
            ResultSet rs = pstmt.executeQuery();
            int i = 0;
            if(rs.next()){
                i++;
            }
            if(i > 0){
                try {
                    //Try Get
                    String query2="SELECT * FROM userdata WHERE `user_id`=?";
                    PreparedStatement Statement12 = conn.prepareStatement(query2);
                    Statement12.setString(1, User.userid);
                    ResultSet rs2 = Statement12.executeQuery();
                    if(rs2.next()){
                        height.setText(rs2.getString("height"));
                        weight.setText(rs2.getString("weight"));
                    }else{
                        System.out.println("** ERROR Height Weight Record Get Not Executed");
                    }

                    //Close Connection
                    Statement12.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }



    @FXML
    void bmical(){
        String s1=weight.getText();
        String s2=height.getText();

        double res = Double.parseDouble(s2)/((Double.parseDouble(s1))*(Double.parseDouble(s1)));
        Result.setText(res+"");
        String s3;
        if(res<18.5){
            typem.setText("Underweight");
        } else if (res<=24.9 && res>=18.5) {
            typem.setText("Normal");
        } else if (res>24.9 && res<=39.9) {
            typem.setText("Overweight");
        }else if(res>=40){
            typem.setText("Obese");
        }
    }

    @FXML
    void BMI_Checker(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("BMIcounter.fxml"));
        Scene scene2 = new Scene(parent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(scene2);


    }

    @FXML
    void ER(ActionEvent event) throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("ExerciseRoutine.fxml"));
        Scene scene3 = new Scene(parent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(scene3);


    }

    @FXML
    void prevpg(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene scene2 = new Scene(parent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(scene2);


    }

    @FXML
    void diet(ActionEvent event) throws IOException {
        Parent parent3 = FXMLLoader.load(getClass().getResource("DietPlan.fxml"));
        Scene scene3 = new Scene(parent3);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene3);


    }


    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){
        loadData();
    }
}
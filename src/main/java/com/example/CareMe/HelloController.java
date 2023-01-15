package com.example.CareMe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;

import java.io.*;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Float.NaN;

public class HelloController {

    //Login Window
    @FXML private Button btn;
    @FXML public PasswordField passwordInput;
    @FXML private Text notificationText;
    @FXML private TextField usernameInput;


///
    //Sign Up Window
    @FXML private Button SignUpbtn;
    @FXML private Text PassMsg;
    @FXML private Label loginTitle1;
    @FXML private Text emailMsg;
    @FXML private TextField usernameInputNew;
    @FXML private Text UsernameMsg;
    @FXML private TextField emailAddress;
    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private PasswordField passwordInput2;
    @FXML private ComboBox<String> bloogGroupSelector;
    @FXML private ComboBox<String> genderSelector;
    @FXML private Text processingText;

    //Scene Switching
    @FXML private void SwitchToSignUp(ActionEvent event) throws IOException {
        Parent p2 = FXMLLoader.load(getClass().getResource("sign-up.fxml"));
        Scene scene = new Scene(p2);
        Stage wind = (Stage)((Node)event.getSource()).getScene().getWindow();
        wind.setScene(scene);
    }

    @FXML private void BackToLogin(ActionEvent event) throws IOException {
        BackToLogin((Stage)((Node)event.getSource()).getScene().getWindow() );
    }
    private void BackToLogin(Stage stage) throws IOException {
        Parent p2 = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(p2);
        stage.setScene(scene);
    }

    private int validateValue = 0;

    @FXML
    private void validate(ActionEvent e) throws IOException, InterruptedException {
        if(checkPassword()==true && CheckUsername()==true && CheckEmail()==true && CheckName()==true && bloogGroupSelector.getValue() != null && genderSelector.getValue() != null){
            processingText.setText("Processing... Please Wait");

            System.out.println("___________________");
            System.out.println("Validation Done!");
            System.out.println("Name: " + CheckName());
            System.out.println("Username: " + CheckUsername());
            System.out.println("Email: " + CheckEmail());
            System.out.println("Password: " + checkPassword());
            System.out.println("Blood Group: " + bloogGroupSelector.getValue());
            System.out.println("Gender: " + genderSelector.getValue());

            String firstname = firstName.getText();
            String lastname = lastName.getText();
            String username = usernameInputNew.getText();
            String email = emailAddress.getText();
            String password = passwordInput2.getText();
            String bloodGroup = bloogGroupSelector.getValue();
            String Gender = genderSelector.getValue();

            //Send the Data to the SQL Server
            connectivity ob = new connectivity();
            Connection conn = ob.connection();
            String query =  "INSERT INTO users (first_name, last_name, username, password, email, blood_group, gender) " +
                            "VALUES('"+firstname+"', '"+lastname+"', '"+username+"', '"+password+"', '"+email+"', '"+bloodGroup+"', '"+Gender+"')";
            try {
                Statement s = conn.createStatement();
                s.executeUpdate(query);
                s.close();

                //Check If the Data Submitted
                String CheckQuery =  "SELECT * FROM users WHERE username=?";
                try {
                    PreparedStatement pstmt = conn.prepareStatement(CheckQuery);
                    pstmt.setString(1, username);

                    ResultSet rs = pstmt.executeQuery();
                    int i = 0;
                    if (rs.next()) { i++; }
                    if (i == 1) {
                        System.out.println("Successful!");
                        Parent p2 = FXMLLoader.load(getClass().getResource("sign-up-confirmation.fxml"));
                        Scene scene = new Scene(p2);
                        Stage wind = (Stage)((Node)e.getSource()).getScene().getWindow();
                        wind.setScene(scene);
                    } else if (i == 0) {
                        System.out.println("Unsuccessful!");
                    }
                    rs.close();
                    pstmt.close();
                    conn.close();

                } catch (SQLException err) {
                    err.printStackTrace();
                }

            } catch (SQLException err) {
                err.printStackTrace();
            }

        }else{
            System.out.println("___________________");
            System.out.println("Validation Error!");
            System.out.println("Name:" + CheckName());
            System.out.println("Username:" + CheckUsername());
            System.out.println("Email:" + CheckEmail());
            System.out.println("Password:" + checkPassword());
            System.out.println("Blood Group: " + bloogGroupSelector.getValue());
        }
    }
    @FXML
    private boolean checkPassword(){
        boolean validated = false;
        if(passwordInput.getText().length() > 0){
            if(passwordInput.getText().length() > 8){
                //If Password Value is longer than 8
                PassMsg.setText("");
                if(passwordInput2.getText().length() > 0){
                    //If Retype Password value is more than 0, the user started typing.
                    if(!passwordInput2.getText().equals(passwordInput.getText())){
                        //If password does not matches
                        PassMsg.setText("Password Mismatch");
                        validated = false;
                    }else{
                        PassMsg.setText("");
                        validated = true;
                    }
                }
            }else if(passwordInput.getText().length() <= 8){
                PassMsg.setText("Too Short!");
                validated = false;
            }
        }

        return validated;
    }

    @FXML
    private boolean CheckEmail(){
        if(!emailAddress.getText().isBlank()){
            String Email = emailAddress.getText();

            //Regular Expression
            String regex = "^(.+)@(.+)$";
            //Compile regular expression to get the pattern
            Pattern pattern = Pattern.compile(regex);
            //Create instance of matcher
            Matcher matcher = pattern.matcher(Email);
            if(matcher.matches()){
                emailMsg.setText("");
                return true;
            }else{
                emailMsg.setText("Invalid Email!");
                return false;
            }
        }else{
            emailMsg.setText("");
            return false;
        }
    }

    @FXML
    private boolean CheckName(){
        if(firstName.getText().isBlank() == true && lastName.getText().isBlank() == true){
           return false;
        }else{
            return true;
        }
    }

    @FXML
    private boolean CheckUsername(){
        int validate = 0;
        if(usernameInputNew.getText().isEmpty() == false){
            String username = usernameInputNew.getText();
            connectivity ob = new connectivity();
            Connection conn = ob.connection();
            String query = "SELECT * FROM users WHERE `username`=?";

            try {
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                int i = 0;
                if (rs.next()) {
                    i++;
                }
                if (i == 1) {
                    System.out.println("Not Available!");
                    UsernameMsg.setText("Not Available!");
                    validate = 0;

                } else if (i == 0) {
                    System.out.println("Available!");
                    UsernameMsg.setText("Available!");
                    validate = 1;
                }
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(validate == 1){
            return true;
        }else{
            return false;
        }
    }

    void recordUserData(String username) throws IOException {

    }

    @FXML
    void Login(ActionEvent event) {

        notificationText.setText("Processing... Please Wait");
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        connectivity ob = new connectivity();
        Connection conn = ob.connection();
        String query = "SELECT * FROM users WHERE `username`=? AND `password`=?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            int i = 0;
            if (rs.next()) {
                i++;
            }
            if (i == 1) {
                System.out.println("User Found");
                notificationText.setStyle("-fx-text-fill: GREEN;");
                notificationText.setText("Logged In!");

                //Get User Info and Save as Variable
                String userQuery = "SELECT * FROM `users` WHERE `username`=?";
                try {
                    PreparedStatement psTmt2 = conn.prepareStatement(userQuery);
                    pstmt.setString(1, username);
                    ResultSet rs2 = pstmt.executeQuery();
                    int j = 0;
                    if (rs2.next()) {
                        User.userid = rs2.getString("user_id");
                        User.username = rs2.getString("username");
                        User.firstname = rs2.getString("first_name");
                        User.lastname = rs2.getString("last_name");
                        User.bloodGroup = rs2.getString("blood_group");
                        User.email = rs2.getString("email");
                        User.gender = rs2.getString("gender");
                    }
                    rs.close();
                    psTmt2.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }


                try {
                    //Try Get
                    String query4 = "SELECT * FROM userdata WHERE `user_id`=?";
                    PreparedStatement Statement12 = conn.prepareStatement(query4);
                    Statement12.setString(1, User.userid);
                    ResultSet rs2 = Statement12.executeQuery();
                    if (rs2.next()) {
                        User.height = (rs2.getString("height"));
                        User.weight = (rs2.getString("weight"));
                        User.TimerDuration = rs2.getInt("EyeSaverTimer");
                        if (User.TimerDuration == 0) {
                            User.showTimer = false;
                        } else if (User.TimerDuration == NaN) {
                            User.showTimer = false;
                        } else {
                            User.showTimer = true;
                        }
                    } else {
                        System.out.println("** ERROR Height Weight Record Get Not Executed");
                    }

                    //Close Connection
                    Statement12.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            } else if (i == 0) {
                System.out.println("User Not Found");
                notificationText.setStyle("-fx-text-fill: RED;");
                notificationText.setText("Username or Password Mismatch.");
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        //Done Getting Info
        try {
            GoToMain(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML private void GoToMain(ActionEvent event) throws IOException {
        GoToMain((Stage)((Node)event.getSource()).getScene().getWindow());
    }
    private void GoToMain(Stage stage) throws IOException {
        Parent p2 = FXMLLoader.load(getClass().getResource("home.fxml"));
        Scene scene = new Scene(p2);
        stage.setScene(scene);
        stage.setMaximized(true);
    }
}

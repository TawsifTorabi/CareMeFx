package com.example.CareMe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class HomeFriendsController implements Initializable {

    @FXML private TableView<Friends> FriendListTable;
    @FXML private TableView<Friends> FriendListTable2;

    @FXML private TableColumn<Friends, String> FriendListView;
    @FXML private TableColumn<Friends, String> FriendListView2;

    @FXML private TableView<FriendRequests> FriendRequestTable;
    @FXML private TableColumn<FriendRequests, String> FriendRequestNameCol;
    @FXML private TableColumn<HomeFriendsController.FriendRequests, String> FriendRequestActionCol;

    @FXML private Button AddFriendsButton;
    @FXML private TextField AddFriendsInput;
    @FXML private Button FriendRequestButton;
    @FXML private Button SentRequestButton;
    @FXML private Text FriendRequestNotification;
    @FXML private Text FriendsRequestSentText;
    @FXML private BorderPane FriendsMainStageScreen;


    @FXML private TabPane FriendsTabPane;
    @FXML private TabPane RequestTabPane;
    @FXML private Tab FriendsTab;
    @FXML private Tab ManageFriendsTab;
    @FXML private Tab ReceivedRequestsTab;
    @FXML private Tab SentRequestsTab;
    @FXML private Tab FriendRequestsTab;

    @FXML private Button SeeFriendsWindowButton;




    @FXML
    public void SendFriendRequest(ActionEvent event) {
        //Bug: Sends friend request to self
        if(AddFriendsInput.getText().isBlank() || AddFriendsInput.getText().isEmpty()){
            System.out.println("Add Friend Username is Empty Field");
            FriendsRequestSentText.setText("Type in Username First!");
            return;
        }

        //Create Connection
        connectivity ob = new connectivity();
        Connection conn = ob.connection();

        //Get Time Current
        long time = Instant.now().getEpochSecond();

        //Get userid from username
        String ToUserID = "";

        //Get User Input
        String ToUserName = CustomFunction.getMysqlRealScapeString(AddFriendsInput.getText());

        //Check User ID
        String SearchQGetUserID = "SELECT `user_id` FROM `users` WHERE `username`=?";
        try {
            //Get User ID from User Name
            PreparedStatement StatementGetUserID = conn.prepareStatement(SearchQGetUserID);
            StatementGetUserID.setString(1, ToUserName);
            ResultSet rsGetUserID = StatementGetUserID.executeQuery();

            int userCount = 0;
            if (rsGetUserID.next()) {
                //Ger User ID if Username found
                ToUserID = rsGetUserID.getString("user_id");
                userCount++;
            }

            if (userCount == 0) {
                //If Username not found
                System.out.println("User Not Found");
                FriendsRequestSentText.setText("User Not Found");
            } else if (userCount == 1) {
                //User Found, Go next

                String SearchQFromFriendRequest = "SELECT COUNT(*) AS `CountRow` FROM `friend_requests` WHERE `from_user`=? AND `to_user`=?";
                String SearchQFromFriendList = "SELECT COUNT(*) AS `CountRow` FROM `friends` WHERE `user_1`=? AND `user_2`=?";
                String InsertQ = "INSERT INTO `friend_requests` VALUES(DEFAULT, '" + User.userid + "', '" + ToUserID + "', '" + time + "')";

                try {
                    //Check From Friends List
                    PreparedStatement FromFriendListState = conn.prepareStatement(SearchQFromFriendList);
                    FromFriendListState.setString(1, User.userid);
                    FromFriendListState.setString(2, ToUserID);
                    ResultSet FromFriendListResult = FromFriendListState.executeQuery();
                    if (FromFriendListResult.next()) {
                        System.out.println("Friends Count: " + FromFriendListResult.getString("CountRow"));

                        if (Integer.parseInt(FromFriendListResult.getString("CountRow")) > 0) {
                            System.out.println("Found in Friendlist");
                            FriendsRequestSentText.setText("Already Friend with " + ToUserName);
                            AddFriendsInput.setText("");
                        } else if (Integer.parseInt(FromFriendListResult.getString("CountRow")) == 0) {
                            System.out.println("Not Found in Friendlist");
                            try {

                                //Check From Friend Requests
                                PreparedStatement FromFriendRequestState = conn.prepareStatement(SearchQFromFriendRequest);
                                FromFriendRequestState.setString(1, User.userid);
                                FromFriendRequestState.setString(2, ToUserID);
                                ResultSet FromFriendRequestResult = FromFriendRequestState.executeQuery();

                                if (FromFriendRequestResult.next()) {
                                    if (Integer.parseInt(FromFriendRequestResult.getString("CountRow")) > 0) {
                                        System.out.println("Found in Friend Requests");
                                        FriendsRequestSentText.setText("Already Sent to " + ToUserName);
                                        AddFriendsInput.setText("");
                                    } else if (Integer.parseInt(FromFriendRequestResult.getString("CountRow")) == 0) {
                                        System.out.println("Not Found in Friend Requests");
                                        //Send Friend Request
                                        try {
                                            Statement SentRequest = conn.createStatement();
                                            SentRequest.executeUpdate(InsertQ);
                                            SentRequest.close();
                                            System.out.println("Friend Request Sent to " + ToUserName);
                                            FriendsRequestSentText.setText("Friend Request Sent to " + ToUserName);
                                            AddFriendsInput.setText("");
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                                FromFriendRequestState.close();
                                FromFriendRequestResult.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    FromFriendListState.close();
                    FromFriendListResult.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            rsGetUserID.close();
            StatementGetUserID.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void UnfriendSelected(ActionEvent actionEvent) {
    }
    @FXML
    public void ViewProfileSelected(ActionEvent actionEvent) {
    }
    @FXML
    public void SeeFriendRequests(ActionEvent actionEvent) {
        FriendsTabPane.getSelectionModel().select(FriendRequestsTab);
        loadFriendRequests();
    }

    @FXML
    void SeeFriendsWindow(ActionEvent event) {
        FriendsTabPane.getSelectionModel().select(ManageFriendsTab);
        loadFriendRequests();
    }

    @FXML
    void SeeSentRequests(ActionEvent event) {
        FriendsTabPane.getSelectionModel().select(FriendRequestsTab);
        RequestTabPane.getSelectionModel().select(SentRequestsTab);
        loadFriendRequests();
    }

    @FXML
    private void SeeFriendsWindow(){
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("bmi-counter.fxml");
        FriendsMainStageScreen.setLeft(view);
    }

    private ArrayList<HomeFriendsController.Friends> FriendList = new ArrayList<HomeFriendsController.Friends>();
    private ArrayList<HomeFriendsController.FriendRequests> FriendRequests = new ArrayList<HomeFriendsController.FriendRequests>();

    private void initiateFriendsCols(){
        FriendListView.setCellValueFactory(new PropertyValueFactory<Friends, String>("user_name"));
        FriendListView2.setCellValueFactory(new PropertyValueFactory<Friends, String>("user_name"));
    }

    private void initiateFriendRequestsCols(){
        FriendRequestNameCol.setCellValueFactory(new PropertyValueFactory<FriendRequests, String>("user_name"));
        FriendRequestActionCol.setCellValueFactory(new PropertyValueFactory<FriendRequests, String>("accept"));
    }


    private void loadFriendRequests(){

        if(FriendRequests != null){
            FriendRequests.removeAll(FriendList);
        }
        //Create Connection
        connectivity ob = new connectivity();
        Connection conn = ob.connection();
        String query="SELECT * FROM `friend_requests` INNER JOIN `users` ON friend_requests.from_user = users.user_id WHERE `to_user`=? ORDER BY `timestamp` DESC";
        try {
            PreparedStatement Statement1 = conn.prepareStatement(query);
            Statement1.setString(1, User.userid);
            ResultSet rs = Statement1.executeQuery();
            FriendRequests.removeAll(FriendRequests);
            //Count Friends Rows
            int i = 0;
            while(rs.next()){
                i++;
                String fetchedId = rs.getString("from_user");
                String fetchedName = rs.getString("first_name") + " " + rs.getString("last_name") + " (" + rs.getString("blood_group") +")";
                String fetchedTime = rs.getString("timestamp");

                long millisTime = Long.parseLong(fetchedTime) * 1000;
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH); //November 1,2011
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                FriendRequests.add(new HomeFriendsController.FriendRequests(fetchedId, fetchedName, sdf.format(new Date(millisTime))));
            }
            rs.close();
            Statement1.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FriendRequestTable.getItems().addAll(FriendRequests);
    }

    private void loadFriendsData(){

        if(FriendList != null){
            FriendList.removeAll(FriendList);
        }
        //Create Connection
        connectivity ob = new connectivity();
        Connection conn = ob.connection();
        String query="SELECT * FROM `friends` INNER JOIN `users` ON friends.user_2 = users.user_id WHERE `user_1`=? ORDER BY `timestamp` DESC";
        try {
            PreparedStatement Statement1 = conn.prepareStatement(query);
            Statement1.setString(1, User.userid);
            ResultSet rs = Statement1.executeQuery();
            FriendList.removeAll(FriendList);
            //Count Friends Rows
            int i = 0;
            while(rs.next()){
                i++;
                String fetchedId = rs.getString("user_2");
                String fetchedName = rs.getString("first_name") + " " + rs.getString("last_name") + " (" + rs.getString("blood_group") +")";
                String fetchedTime = rs.getString("timestamp");

                long millisTime = Long.parseLong(fetchedTime) * 1000;
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH); // Tuesday,November 1,2011
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                FriendList.add(new HomeFriendsController.Friends(fetchedId, fetchedName, sdf.format(new Date(millisTime))));
            }
            rs.close();
            Statement1.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FriendListTable.getItems().addAll(FriendList);
        FriendListTable2.getItems().addAll(FriendList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initiateFriendsCols();
        initiateFriendRequestsCols();
        loadFriendsData();
        loadFriendRequests();
    }

    public static class Friends{
        private String user_id;
        private String user_name;
        private String timestamp;

        public Friends(String user_id, String user_name, String timestamp){
            this.timestamp = timestamp;
            this.user_id = user_id;
            this.user_name = user_name;
        }

        public String getUserID(){
            return user_id;
        }
        public String getUser_name(){
            return user_name;
        }
        public String getTimestamp(){
            return timestamp;
        }
    }

    public static class FriendRequests{
        private String user_id;
        private String user_name;
        private String timestamp;
        private Button accept;
        private Button Decline;

        public FriendRequests(String user_id, String user_name, String timestamp){
            this.timestamp = timestamp;
            this.user_id = user_id;
            this.user_name = user_name;
            this.accept = new Button("Accept");
            this.Decline = new Button("Decline");
        }

        public String getUserID(){
            return user_id;
        }

        public String getUser_name(){
            return user_name;
        }

        public void setAcceptButton(Button button){
            this.accept = button;
        }

        public void setDeclineButton(Button button){
            this.Decline = button;
        }

        public Button getAcceptButton(){
            return accept;
        }

        public Button getDeclineButton(){
            return Decline;
        }

        public String getTimestamp(){
            return timestamp;
        }
    }
}

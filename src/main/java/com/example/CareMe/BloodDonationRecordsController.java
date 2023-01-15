package com.example.CareMe;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class BloodDonationRecordsController implements Initializable {

    @FXML
    private TableView<DonationRecord> BloodDonationRecordTable;

    @FXML
    private TableColumn<DonationRecord, String> BloodRecordIndexCol;

    @FXML
    private TableColumn<DonationRecord, String> BloodRecordNoteCol;

    @FXML
    private TableColumn<DonationRecord, String> BloodRecordTimeCol;

    private ArrayList<DonationRecord> DonationList = new ArrayList<DonationRecord>();

    @FXML
    private Text DateTimeText;

    @FXML
    private Text IndexText;

    @FXML
    private HBox RecordDetailsArea;

    @FXML
    private TextArea RecordNote;


    @FXML
    private Text BloodDonationRecordNotification;

    @FXML
    void DeleteRecordAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation!");
        alert.setContentText("You sure you want to Delete this Record? \n You won't be able to recover!");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            DeleteRecordActionGo();
        }else{
            //Do Nothing
        }
    }

    void DeleteRecordActionGo(){

            //Create Connection
            connectivity ob = new connectivity();
            Connection conn = ob.connection();

            try {
                //Try Delete Row
                String serverIndex = BloodDonationRecordTable.getSelectionModel().getSelectedItem().getServerIndex();
                System.out.println("Server Index: " + serverIndex);
                String query="DELETE FROM `blood_donation_record` WHERE `blood_donation_record`.`id`="+serverIndex+"";
                PreparedStatement Statement1 = conn.prepareStatement(query);

                if(Statement1.execute()){
                    System.out.println("___________Not Executed_____________");
                }else{
                    //Remove Row Code
                    DonationRecord selectedItem = BloodDonationRecordTable.getSelectionModel().getSelectedItem();
                    BloodDonationRecordTable.getItems().remove(selectedItem);
                    RecordDetailsArea.setVisible(false);
                    HomeController aa = new HomeController();
                    aa.GetBloodRecord();
                    BloodDonationRecordNotification.setText("Record Deleted!");
                    System.out.println("___________Executed_____________");
                }

                //Close Connection
                Statement1.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    void SaveNoteAction(ActionEvent event) {
        //Create Connection
        connectivity ob = new connectivity();
        Connection conn = ob.connection();

        try {
            //Try Update Note
            String serverIndex = BloodDonationRecordTable.getSelectionModel().getSelectedItem().getServerIndex();
            String NoteData = CustomFunction.getMysqlRealScapeString(RecordNote.getText());
            System.out.println("Server Index: " + serverIndex);
            String query="UPDATE `blood_donation_record` SET `note`='"+NoteData+"' WHERE `blood_donation_record`.`id`="+serverIndex+"";
            PreparedStatement Statement1 = conn.prepareStatement(query);
            if(Statement1.execute()){
                System.out.println("** ERROR Blood Donation Record Note Update Not Executed");
            }else{
                DonationRecord selectedItem = BloodDonationRecordTable.getSelectionModel().getSelectedItem();
                RecordDetailsArea.setVisible(false);
                System.out.println("** Blood Donation Record Note Update Executed");
                BloodDonationRecordTable.getItems().clear();
                loadData();
                BloodDonationRecordNotification.setText("Record Updated!");
            }

            //Close Connection
            Statement1.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void DisplaySelected(javafx.scene.input.MouseEvent mouseEvent) {
        DonationRecord record = BloodDonationRecordTable.getSelectionModel().getSelectedItem();
        if(record == null){
            RecordDetailsArea.setVisible(false);
        }else{
            RecordDetailsArea.setVisible(true);
            String index = record.getIndex();
            String time = record.getTime();
            String note = record.getNote();
            IndexText.setText("Record Index #"+index);
            DateTimeText.setText(time);
            RecordNote.setText(note);
        }
    }


    private void initiateCols(){
        BloodRecordIndexCol.setCellValueFactory(new PropertyValueFactory<DonationRecord, String>("index"));
        BloodRecordTimeCol.setCellValueFactory(new PropertyValueFactory<DonationRecord, String>("time"));
        BloodRecordNoteCol.setCellValueFactory(new PropertyValueFactory<DonationRecord, String>("note"));
    }
    private void loadData(){
        if(DonationList != null){
            DonationList.removeAll(DonationList);
        }

        //Create Connection
        connectivity ob = new connectivity();
        Connection conn = ob.connection();

        String query="SELECT * FROM `blood_donation_record` WHERE `user_id`=? ORDER BY `last_donated` DESC";
        try {
            PreparedStatement Statement1 = conn.prepareStatement(query);
            Statement1.setString(1, User.userid);
            ResultSet rs = Statement1.executeQuery();

            DonationList.removeAll(DonationList);
            //Count Blood Donation Rows
            int i = 0;
            while(rs.next()){
                i++;
                String fetchedId = rs.getString("id");
                String fetchedTime = rs.getString("last_donated");
                String fetchedNote = rs.getString("note");

                //Last Donated Time Calculation
                long millisTime = Long.parseLong(fetchedTime) * 1000;
                //SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH); // Tuesday,November 1,2011
                //SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d,yyyy h:mm,a", Locale.ENGLISH); // Tuesday,November 1,2011 12:00,AM
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                DonationList.add(new DonationRecord(fetchedId, Integer.toString(i), sdf.format(new Date(millisTime)), fetchedNote));
            }

            rs.close();
            Statement1.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        BloodDonationRecordTable.getItems().addAll(DonationList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initiateCols();
        loadData();
        BloodDonationRecordTable.getSelectionModel().select(0);
        RecordDetailsArea.setVisible(false);
    }

    public static class DonationRecord{
        private final SimpleStringProperty index;
        private SimpleStringProperty time;
        private SimpleStringProperty note;
        private String serverIndex;

        public DonationRecord(String serverIndex, String index, String time, String note){
            this.serverIndex = serverIndex;
            this.index = new SimpleStringProperty(index);
            this.time = new SimpleStringProperty(time);
            this.note = new SimpleStringProperty(note);
        }

        public String getIndex(){
            return index.get();
        }
        public String getTime(){
            return time.get();
        }
        public String getNote(){
            return note.get();
        }
        public void setNote(String s){
            note.set(s);
        }
        public String getServerIndex(){
            return serverIndex;
        }
    }

}

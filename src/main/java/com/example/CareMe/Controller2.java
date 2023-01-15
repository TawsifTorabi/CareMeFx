package com.example.CareMe;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller2 implements Initializable {
    public TextArea tf1;

    @FXML
        private ChoiceBox<String> gender;
        @FXML
            private MenuBar menubar;
            @FXML
            private ChoiceBox<String> btype;


        void demo1() {
            String txt = "";
            String txt2 ="";
            String txt3 ="";
            String txt4 ="";
            String txt5 ="";
            String txt6 ="";
            String txt7 ="";
            String txt8 ="";


            try {
                FileReader fr;
                fr = new FileReader("src/main/resources/Texts/Normal,Male,Ex.txt");
                BufferedReader br = new BufferedReader(fr);
                String Line;
                while ((Line = br.readLine()) != null) {
                    txt += Line;
                    txt += "\n";
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                FileReader fr2;
                fr2 = new FileReader("src/main/resources/Texts/txt2");
                BufferedReader br2 = new BufferedReader(fr2);
                String Line;
                while ((Line = br2.readLine()) != null) {
                    txt2 += Line;
                    txt2 += "\n";
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } try {
                FileReader fr3;
                fr3 = new FileReader("src/main/resources/Texts/txt3");
                BufferedReader br3 = new BufferedReader(fr3);
                String Line;
                while ((Line = br3.readLine()) != null) {
                    txt3 += Line;
                    txt3 += "\n";
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } try {
                FileReader fr4;
                fr4 = new FileReader("src/main/resources/Texts/txt4");
                BufferedReader br4 = new BufferedReader(fr4);
                String Line;
                while ((Line = br4.readLine()) != null) {
                    txt4 += Line;
                    txt4 += "\n";
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } try {
                FileReader fr5;
                fr5 = new FileReader("src/main/resources/Texts/txt5");
                BufferedReader br5 = new BufferedReader(fr5);
                String Line;
                while ((Line = br5.readLine()) != null) {
                    txt5 += Line;
                    txt5 += "\n";
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } try {
                FileReader fr6;
                fr6 = new FileReader("src/main/resources/Texts/txt6");
                BufferedReader br6 = new BufferedReader(fr6);
                String Line;
                while ((Line = br6.readLine()) != null) {
                    txt6 += Line;
                    txt6 += "\n";
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } try {
                FileReader fr7;
                fr7 = new FileReader("src/main/resources/Texts/txt7");
                BufferedReader br7 = new BufferedReader(fr7);
                String Line;
                while ((Line = br7.readLine()) != null) {
                    txt7 += Line;
                    txt7 += "\n";
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } try {
                FileReader fr8;
                fr8 = new FileReader("src/main/resources/Texts/txt8");
                BufferedReader br8 = new BufferedReader(fr8);
                String Line;
                while ((Line = br8.readLine()) != null) {
                    txt8 += Line;
                    txt8 += "\n";
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (btype.getValue() != null && gender.getValue() != null) {
                if (btype.getValue().equals("Normal") && gender.getValue().equals("Male")) {
                    tf1.setText(txt);
                } else if (btype.getValue().equals("Normal") && gender.getValue().equals("Female")) {
                    tf1.setText(txt2);
                }else if (btype.getValue().equals("Underweight") && gender.getValue().equals("Male")) {
                    tf1.setText(txt3);
                }else if (btype.getValue().equals("Underweight") && gender.getValue().equals("Female")) {
                    tf1.setText(txt4);
                }else if (btype.getValue().equals("Overweight") && gender.getValue().equals("Male")) {
                    tf1.setText(txt5);
                }else if (btype.getValue().equals("Overweight") && gender.getValue().equals("Female")) {
                    tf1.setText(txt6);
                }else if (btype.getValue().equals("Obese") && gender.getValue().equals("Male")) {
                    tf1.setText(txt7);
                }else if (btype.getValue().equals("Obese") && gender.getValue().equals("Female")) {
                    tf1.setText(txt8);
                }
             }
            }


        @FXML
         public void initialize(URL url, ResourceBundle resourceBundle){
            
            gender.setItems(FXCollections.observableArrayList(
                    "Male","Female")
            );
            btype.setItems(FXCollections.observableArrayList(
                    "Obese","Overweight","Normal","Underweight")
            );


            gender.setOnAction(event -> {
                int selectedIndex = gender.getSelectionModel().getSelectedIndex();
                Object selectedItem = gender.getSelectionModel().getSelectedItem();

                System.out.println("Selection made: [" + selectedIndex + "] " + selectedItem);
                System.out.println("   ChoiceBox.getValue(): " + gender.getValue());
                demo1();
            });

            btype.setOnAction(event -> {
                int selectedIndex = btype.getSelectionModel().getSelectedIndex();
                Object selectedItem = btype.getSelectionModel().getSelectedItem();

                System.out.println("Selection made: [" + selectedIndex + "] " + selectedItem);
                System.out.println("   ChoiceBox.getValue(): " + btype.getValue());
                demo1();
            });

            gender.getSelectionModel().select(User.gender);
            btype.getSelectionModel().select(CustomFunction.bmiCalculation());
        }
    @FXML
    void exit(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        Scene scene2 = new Scene(parent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(scene2);


    }


    }


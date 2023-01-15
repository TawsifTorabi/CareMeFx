package com.example.CareMe;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.net.URL;

public class FxmlLoader {
    private Pane view;

    public Pane getPage(String filename) {
        try{
            URL fileUrl = HelloController.class.getResource(filename);
            if (fileUrl == null){
                throw new java.io.FileNotFoundException("FXML File not Found");
            }
            view = new FXMLLoader().load(fileUrl);

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("NO page "+ filename + "please check FxmlLoader");
        }

        return view;
    }
}

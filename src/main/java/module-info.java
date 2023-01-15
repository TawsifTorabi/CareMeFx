module com.example.dbms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.CareMe to javafx.fxml;
    exports com.example.CareMe;
}
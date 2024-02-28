package com.example.CareMe;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectivity {
   Connection con;
   String dbname="careme";
   String user="root";
   String pass="";
   public Connection connection(){
      try {
         Class.forName("com.mysql.cj.jdbc.Driver");
         con= DriverManager.getConnection("jdbc:mysql://localhost/"+dbname,user,pass);
      } catch (ClassNotFoundException | SQLException e) {
         e.printStackTrace();
      }
      return con;
   }
}

package com.example.CareMe;


public class User {

    public static String userid = "";
    public static String username = "";
    public static String email = "";
    public static String firstname = "";
    public static String lastname = "";
    public static String bloodGroup = "";
    public static String gender = "";
    public static String BloodBoolean = "";
    public static String weight = "";
    public static String height = "";
    public static long lastDonated = 0;


    public static int TimerDuration = 60;
    public static boolean showTimer = true;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}


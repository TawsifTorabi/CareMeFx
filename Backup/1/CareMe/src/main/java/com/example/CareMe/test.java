package com.example.CareMe;

import javax.swing.JOptionPane;
import static java.lang.Integer.parseInt;

public class test {
    public static void main(String[] args) {

        String inputtime = JOptionPane.showInputDialog("Input 24hour Time:");
        String[] timearray = inputtime.split(":");

        int hh = parseInt(timearray[0]);
        int mm = parseInt(timearray[1]);
        int ss = parseInt(timearray[2]);

        int showHr = 0;
        String daytime = null;

        if(hh>12){
            showHr = hh-12;
        } else if(hh == 0){
            showHr = 12;
        } else{
            showHr = hh;
        }

        if(hh >= 12){
            daytime = "PM";
        } else {
            daytime = "AM";
        }

        String newtimevalue = showHr+":"+mm+":"+ss+" "+daytime;
        JOptionPane.showMessageDialog(null, newtimevalue);
        //Using JSwing
    }
}
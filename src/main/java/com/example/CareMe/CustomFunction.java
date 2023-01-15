package com.example.CareMe;

public class CustomFunction {
    public static synchronized String getMysqlRealScapeString(String str) {
        String data = null;
        if (str != null && str.length() > 0) {
            str = str.replace("\\", "\\\\");
            str = str.replace("'", "\\'");
            str = str.replace("\0", "\\0");
            str = str.replace("\n", "\\n");
            str = str.replace("\r", "\\r");
            str = str.replace("\"", "\\\"");
            str = str.replace("\\x1a", "\\Z");
            str = str.replace("\\x1a", "\\Z");
            data = str;
        }
        return data;
    }

    public static String bmiCalculation(){
        String s1=User.weight;
        String s2=User.height;
        System.out.println("weight:" + s1);
        System.out.println("height:" + s2);
        double res = Double.parseDouble(s2)/((Double.parseDouble(s1))*(Double.parseDouble(s1)));
        String s3 = "";
        if(res<18.5){
            s3 = "Underweight";
        } else if (res<=24.9 && res>=18.5) {
            s3 =  "Normal";
        } else if (res>24.9 && res<=39.9) {
            s3 =  "Overweight";
        }else if(res>=40){
            s3 =  "Obese";
        }
        return s3;
    }
}

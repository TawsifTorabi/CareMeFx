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
}

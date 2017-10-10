package com.example.home.gym;

/**
 * Created by Home on 18-08-2015.
 */
public class Dbdetails {



    final private String Driver="com.mysql.jdbc.Driver";
    final private String UserName="root";
    final private String Pass="1234";
    private String Url="jdbc:mysql://192.168.0.100:3306/gym";

    Dbdetails() {}

    public String getDriver() {
        return Driver;
    }

    public String getUserName() {
        return UserName;
    }

    public String getPass() {
        return Pass;
    }

    public String getUrl() {
        return Url;
    }
}

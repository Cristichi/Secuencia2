package org.iesmurgi.cristichi.data;

public class InfoCustomGamemode{
    private int id;
    private String userEmail;
    private String name;

    InfoCustomGamemode(int id, String userEmail, String name){
        this.id = id;
        this.userEmail = userEmail;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUserEmail() {
        return userEmail;
    }
}

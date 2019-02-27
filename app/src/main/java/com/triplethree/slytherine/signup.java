package com.triplethree.slytherine;

public class signup {
    private String username;
    private float phoneno;
    public signup(){}
    public signup(String username, float phoneno) {
        this.username = username;
        this.phoneno = phoneno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(float phoneno) {
        this.phoneno = phoneno;
    }
}

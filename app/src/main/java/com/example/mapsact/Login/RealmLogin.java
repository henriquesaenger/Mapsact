package com.example.mapsact.Login;

import io.realm.RealmObject;

public class RealmLogin extends RealmObject {

    private String user;
    private String pass;

    public RealmLogin(String u, String p){
        user=u;
        pass=p;
    }

    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setUser(String user) {
        this.user = user;
    }
    public String getUser() {
        return user;
    }




}

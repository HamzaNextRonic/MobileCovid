package com.learntodroid.androidqrcodescanner.model;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String accessToken ;
    private int account_type;
    private int permissions;
    private  String first_name;
    private String email;
    private String last_name;

    public User(String email, String accessToken) {
        this.email=email;
        this.accessToken=accessToken;

    }



    public User(String accessToken, int account_type, int permissions, String first_name, String email, String last_name) {
        this.accessToken = accessToken;
        this.account_type = account_type;
        this.permissions = permissions;
        this.first_name = first_name;
        this.email = email;
        this.last_name = last_name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getAccount_type() {
        return account_type;
    }

    public void setAccount_type(int account_type) {
        this.account_type = account_type;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    //private String token;

    public static User getUser(JSONObject jsonObject) throws JSONException {

        String accessToken = jsonObject.getString("accessToken");
        String first_name = jsonObject.getString("first_name");
        String last_name = jsonObject.getString("last_name");
        String email = jsonObject.getString("email");
        int permissions = jsonObject.getInt("permissions");
        int account_type = jsonObject.getInt("account_type");

        User user = new User( accessToken, account_type,  permissions,  first_name,  email,  last_name);
        return  user;
    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object obj){
        boolean result = false;

        if(obj != null && obj instanceof User){
            User that = (User) obj;
            if(this.email.equalsIgnoreCase(that.email)){
                result = true;
            }
        }
        return result;
    }

    public String toString(){
        return this.first_name +" ("+this.email +")";
    }



}

package com.learntodroid.androidqrcodescanner.model;

import android.app.Application;

import com.learntodroid.androidqrcodescanner.interfaces.API;

public class Model {


    private static Model sInstance = null;
    private final API mApi;
    private User mUser;


    public static Model getInstance(Application application){
        if(sInstance == null){
            sInstance = new Model(application);
        }
        return sInstance;
    }

    private final Application mApplication;

    private Model(Application application){
        mApplication = application;
        mApi = new WebAPI(mApplication);
    }
    public Application getmApplication(){
        return  mApplication;
    }

    public void login(String email, String password){
        mApi.login(email,password);

    }

    public User getmUser() {
        return mUser;
    }

    public void setmUser(User user) {
        this.mUser = user;
    }
}

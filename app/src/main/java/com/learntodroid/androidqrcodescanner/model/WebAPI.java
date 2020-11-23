package com.learntodroid.androidqrcodescanner.model;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.learntodroid.androidqrcodescanner.interfaces.API;

import org.json.JSONException;
import org.json.JSONObject;

public class  WebAPI  implements API {

    public static final String BASE_URL = "http://10.0.2.2:3000/";

    private final Application mApplication;

    private RequestQueue mRequestQueue;




    public WebAPI (Application application){
        mApplication = application;
        mRequestQueue = Volley.newRequestQueue(application);

    }

    public void login(String email, String password){
        String url = BASE_URL + "auth/";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);


            Response.Listener<JSONObject> seccessListner = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.d("user1 ", response.toString());

                    try {
                        User user = User.getUser(response);
                        Log.d("user",user.toString());
                        Toast.makeText(mApplication,"User : "+ user.getFirst_name() +" Logged In ",Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {

                        Toast.makeText(mApplication,"JSON Exception 1 ",Toast.LENGTH_LONG).show();
                    }

                }
            };



            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mApplication,"Error response",Toast.LENGTH_LONG).show();

                }
            };

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,jsonObject,seccessListner,errorListener);
            mRequestQueue.add(request);


        }catch (JSONException e){
            //e.printStackTrace();
            Toast.makeText(mApplication,"Json Exception 2 ",Toast.LENGTH_LONG).show();
        }
    }
}

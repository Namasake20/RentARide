package com.rentride.rentaride;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.SplittableRandom;

public class SessionService {
    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "LoggedIn";

    public static final String USERNAME = "username";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";

    //constructor
    public SessionService(Context _context){
        context = _context;
        userSession = context.getSharedPreferences("userLoginSession",Context.MODE_PRIVATE);
        editor = userSession.edit();

    }

    public void createLoginSession(String username,String phone,String password,String email){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(USERNAME,username);
        editor.putString(PHONE_NUMBER,phone);
        editor.putString(PASSWORD,password);
        editor.putString(EMAIL,email);
        editor.commit();
    }

    public HashMap<String,String> getUserDetailsFromSession(){
        HashMap<String,String> userData = new HashMap<String , String>();

        userData.put(USERNAME,userSession.getString(USERNAME,null));
        userData.put(PHONE_NUMBER,userSession.getString(PHONE_NUMBER,null));
        userData.put(PASSWORD,userSession.getString(PASSWORD,null));
        userData.put(EMAIL,userSession.getString(EMAIL,null));

        return userData;
    }
    public boolean checkLogin(){
        if (userSession.getBoolean(IS_LOGIN,false)){
            return true;
        }
        else
            return false;
    }
    public void LogoutUserFromSession(){
        editor.clear();
        editor.commit();
    }
}

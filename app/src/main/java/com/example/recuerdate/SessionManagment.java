package com.example.recuerdate;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagment {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY="session_user";

    public SessionManagment(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(UsuariLocalitzat user){
        //save session of user whenever user is logged in
     int id = user.getId_usuari();
     editor.putInt(SESSION_KEY, id).commit();
    }
    public int getSession(){
        //Torna usuare el qual la sessio esta guardada
        return sharedPreferences.getInt(SESSION_KEY,-1);
    }

    public void removeSession(){
        editor.putInt(SESSION_KEY, -1).commit();
    }
}

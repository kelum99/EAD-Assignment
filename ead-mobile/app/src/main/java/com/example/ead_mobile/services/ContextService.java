package com.example.ead_mobile.services;

import android.content.Context;

public class ContextService {
    private static ContextService singleton;
    private Context applicationContext;

    //Returns ContextService singleton object
    public static ContextService getInstance() {
        if (singleton == null)
            singleton = new ContextService();
        return singleton;
    }

    public void setApplicationContext(Context applicationContext){
        this.applicationContext = applicationContext;
    }

    public Context getApplicationContext(){
        return applicationContext;
    }
}

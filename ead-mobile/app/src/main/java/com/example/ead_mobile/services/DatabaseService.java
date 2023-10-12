package com.example.ead_mobile.services;

import androidx.room.Room;

import com.example.ead_mobile.models.DatabaseModel;

public class DatabaseService {
    private static DatabaseService singleton;
    private final DatabaseModel databaseModel;

    //Returns Database Service singleton object
    public static DatabaseService getInstance(){
        if (singleton == null)
            singleton = new DatabaseService();
        return singleton;
    }

    private DatabaseService(){
        ContextService contextService = ContextService.getInstance();
        String databaseName = "eadDb";
        databaseModel = Room.databaseBuilder(
                contextService.getApplicationContext(),
                DatabaseModel.class,
                databaseName).build();
    }

    public DatabaseModel db(){
        return databaseModel;
    }
}

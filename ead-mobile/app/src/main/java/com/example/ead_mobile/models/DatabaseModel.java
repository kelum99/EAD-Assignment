package com.example.ead_mobile.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.ead_mobile.interfaces.ILocalService;

@Database(entities = {UserEntity.class}, version = 1)
public abstract class DatabaseModel extends RoomDatabase {
    public abstract ILocalService iLocalService();
}

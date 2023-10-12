package com.example.ead_mobile.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ead_mobile.models.UserEntity;

import java.util.List;

@Dao
public interface ILocalService {
    @Query("SELECT * FROM Users")
    List<UserEntity> getAll();

    @Insert
    void insert(UserEntity user);

    @Update
    void update(UserEntity user);

    @Query("DELETE FROM Users")
    void removeAll();
}

package com.example.ead_mobile.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "Users")
public class UserEntity {
    @PrimaryKey()
    @NotNull
    public String nic;
    public String id;
    public String name;
    public String status;
    public String email;
    public String mobile;

    public static UserEntity fromDto(User user) {
        UserEntity entity = new UserEntity();
        entity.id = user.id;
        entity.nic = user.nic;
        entity.name = user.name;
        entity.email = user.email;
        entity.status = user.status;
        entity.mobile = user.mobile;
        return entity;
    }
}

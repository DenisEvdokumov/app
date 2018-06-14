package com.example.teachergradebook.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.teachergradebook.data.Config;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = Config.USER_TABLE_NAME)
public class User {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @PrimaryKey()
    @SerializedName("id")
    @ColumnInfo(name = "id")
    private long id;

    @SerializedName("login")
    @ColumnInfo(name = "login")
    String login;


    @SerializedName("password")
    @ColumnInfo(name = "password")
    String password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @SerializedName("token")
    @ColumnInfo(name = "token")
    String token;







    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

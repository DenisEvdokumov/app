package com.example.teachergradebook.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.teachergradebook.data.Config;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Денис on 12.03.2018.
 */

@Entity(tableName = Config.GROUP_TABLE_NAME)
public class StudentGroup {

    @SerializedName("question_id")
    @PrimaryKey
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("predmetId")

    private long predmetId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @SerializedName("userId")

    private long userId;

    public long getPredmetId() {
        return predmetId;
    }

    public void setPredmetId(long predmetId) {
        this.predmetId = predmetId;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}

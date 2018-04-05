package com.example.teachergradebook.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.teachergradebook.data.Config;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Денис on 17.03.2018.
 */

@Entity(tableName = Config.COURSE_TABLE_NAME)
public class Course {
    @PrimaryKey
    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;


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

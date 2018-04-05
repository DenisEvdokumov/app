package com.example.teachergradebook.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.teachergradebook.data.Config;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Денис on 17.03.2018.
 */

@Entity(tableName = Config.PRACTICE_TABLE_NAME)
public class Practice  {


    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

//    @SerializedName("course_id")
//    private long course_id;

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

//    public long getCourse_id() {
//        return course_id;
//    }
//
//    public void setCourse_id(long course_id) {
//        this.course_id = course_id;
//    }
}

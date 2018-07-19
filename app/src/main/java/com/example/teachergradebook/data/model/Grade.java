package com.example.teachergradebook.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.example.teachergradebook.data.Config;
import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Денис on 17.03.2018.
 */

@Entity(tableName = Config.GRADE_TABLE_NAME
//        foreignKeys = {
//                @ForeignKey(
//                        entity = Practice.class,
//                        parentColumns = "id",
//                        childColumns = "practiceId",
//                        onDelete = CASCADE
//                ),
//                @ForeignKey(
//                        entity = Student.class,
//                        parentColumns = "id",
//                        childColumns = "studentId",
//                        onDelete = CASCADE)
//        }
 )
public class Grade {


    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("practiceId")
    private long practiceId;

    @SerializedName("studentId")
    private long studentId;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPracticeId() {
        return practiceId;
    }

    public void setPracticeId(long practiceId) {
        this.practiceId = practiceId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

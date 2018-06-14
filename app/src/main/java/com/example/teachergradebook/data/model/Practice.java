package com.example.teachergradebook.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.example.teachergradebook.data.Config;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Денис on 17.03.2018.
 */

@Entity(tableName = Config.PRACTICE_TABLE_NAME
//        foreignKeys = {
//        @ForeignKey(
//                entity = StudentGroup.class,
//                parentColumns = "id",
//                childColumns = "studentGroupId",
//                onDelete = CASCADE
//        )

//}
)
public class Practice  {

    public Practice (long studentGroupId,long predmetId){
        this.studentGroupId = studentGroupId;
        this.predmetId = predmetId;
    }


    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("studentGroupId")
    @Expose
    private long studentGroupId;

    @SerializedName("predmetId")
    @Expose
    private long predmetId;

    public String getTaskfile() {
        return taskfile;
    }

    public void setTaskfile(String taskfile) {
        this.taskfile = taskfile;
    }

    @SerializedName("taskfile")
    @Expose
    private String taskfile;

    public long getStudentGroupId() {
        return studentGroupId;
    }

    public void setStudentGroupId(long studentGroupId) {
        this.studentGroupId = studentGroupId;
    }

    public long getPredmetId() {
        return predmetId;
    }

    public void setPredmetId(long predmetId) {
        this.predmetId = predmetId;
    }
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

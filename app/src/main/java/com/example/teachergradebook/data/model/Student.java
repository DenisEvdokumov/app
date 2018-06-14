package com.example.teachergradebook.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.evrencoskun.tableview.filter.IFilterableModel;
import com.evrencoskun.tableview.sort.ISortableModel;
import com.example.teachergradebook.data.Config;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Денис on 12.03.2018.
 */

@Entity

public class Student implements  IFilterableModel {
    private String mFilterKeyword;


    public Student (String name,long group_id){
        this.name = name;
        this.group_id = group_id;
    }

    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    @Expose
    private int id;



    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("group_id")
    @ColumnInfo(name = "group_id")
    private long group_id;

//    @Embedded(prefix = "gurades")
//    private Gurade gurades;
//
//    public Gurade getGurade() {
//        return gurades;
//    }
//
//    public void setGurade(Gurade grades) {
//        this.gurades = grades;
//    }


    public int getId() {
        return id;
    }




    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public String getFilterKeyword() {
        return mFilterKeyword;
    }

    public void setFilterKeyword(String filterKeyword) {
        this.mFilterKeyword = filterKeyword;
    }

    @Override
    public String getFilterableKeyword() {
        return mFilterKeyword;
    }

}

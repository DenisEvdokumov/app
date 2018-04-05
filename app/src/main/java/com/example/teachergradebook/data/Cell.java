package com.example.teachergradebook.data;

import android.arch.persistence.room.Entity;

import com.evrencoskun.tableview.filter.IFilterableModel;
import com.evrencoskun.tableview.sort.ISortableModel;

/**
 * Created by Денис on 19.02.2018.
 */
@Entity
public class Cell implements ISortableModel, IFilterableModel {
    private String mId;
    private Object mData;
    private String mFilterKeyword;

    public Cell(String id) {
        this.mId = id;
    }

    public Cell(String id, Object data) {
        this.mId = id;
        this.mData = data;
        this.mFilterKeyword = String.valueOf(data);
    }

    public Cell(String id, Object data, String filterKeyword) {
        this.mId = id;
        this.mData = data;
        this.mFilterKeyword = filterKeyword;
    }

    /**
     * This is necessary for sorting process.
     * See ISortableModel
     */
    @Override
    public String getId() {
        return mId;
    }

    /**
     * This is necessary for sorting process.
     * See ISortableModel
     */
    @Override
    public Object getContent() {
        return mData;
    }


    public Object getData() {
        return mData;
    }

    public void setData(String data) { mData = data; }

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

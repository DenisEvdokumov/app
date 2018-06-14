package com.example.teachergradebook.data.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class ResList1 {
    @SerializedName("rez")
    @Expose
    private String rez;
    @SerializedName("data")
    @Expose
    private List<Student> data = null;

    public String getRez() {
        return rez;
    }

    public void setRez(String rez) {
        this.rez = rez;
    }

    public List<Student> getData() {
        return data;
    }

    public void setData(List<Student> data) {
        this.data = data;
    }
}

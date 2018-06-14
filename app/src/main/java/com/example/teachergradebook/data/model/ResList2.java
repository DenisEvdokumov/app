package com.example.teachergradebook.data.model;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class ResList2 {
    @SerializedName("rez")
    @Expose
    private String rez;
    @SerializedName("data")
    @Expose
    private List<Practice> data = null;

    public String getRez() {
        return rez;
    }

    public void setRez(String rez) {
        this.rez = rez;
    }

    public List<Practice> getData() {
        return data;
    }

    public void setData(List<Practice> data) {
        this.data = data;
    }
}

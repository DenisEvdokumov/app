package com.example.teachergradebook.data.model;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class Res {

    @SerializedName("rez")
    @Expose
    private String rez;
    @SerializedName("data")
    @Expose
    private Data data;






    public String getRez() {
        return rez;
    }

    public void setRez(String rez) {
        this.rez = rez;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
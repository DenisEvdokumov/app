package com.example.teachergradebook.data.api;

import com.example.teachergradebook.data.model.Practice;
import com.example.teachergradebook.data.model.Res;
import com.example.teachergradebook.data.model.ResList;
import com.example.teachergradebook.data.model.ResList1;
import com.example.teachergradebook.data.model.ResList2;
import com.example.teachergradebook.data.model.ResList3;
import com.example.teachergradebook.data.model.Student;
import com.example.teachergradebook.data.model.StudentGroup;
import com.example.teachergradebook.data.model.User;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServerApi {

    @GET("login.php")
    Flowable<Res> login(@Query("login") String login, @Query("password") String password);
//    @Path("login") String login, @Path("password") String password
//    @GET("listgroupscourses.php")
//    Call<List<StudentGroup>> getListStudentGroup(@Header("login") String login, @Header("password") String password);

    @GET("listcourses.php")
    Flowable<ResList> loadPredmet(@Query("token") String login);



    @GET("logout.php")
    Flowable<Res> logout(@Query("token") String login);

    @GET("liststudentsingroup.php")
    Flowable<ResList1> loadStudent(@Query("token") String token, @Query("groupid") String groupId);

    @GET("listtasks.php")
    Flowable<ResList2> loadPractice(@Query("token") String token, @Query("courseid") String courseid, @Query("groupid") String groupId);

    @GET("liststudentsingroup.php")
    Flowable<ResList3> loadGrade(@Query("token") String token, @Query("courseid") String courseid, @Query("groupid") String groupId);
}

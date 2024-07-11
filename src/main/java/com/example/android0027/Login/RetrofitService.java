package com.example.android0027.Login;


import com.example.android0027.Login.DataResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService {
    @GET("api/users")
    Call<List<DataResponse>> getUserData();

//    @POST()
//    Call<DataResponse> sendAttendance();
}

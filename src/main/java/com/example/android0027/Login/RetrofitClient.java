package com.example.android0027.Login;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.50.159:8081/";
      //http://192.168.50.159:8081/ 직장
     // http://172.30.1.3:8081/ 집
    //http://192.168.244.7:8081/ 핫스팟

    public void getAddress(){
        BASE_URL.toString().trim();
    }
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

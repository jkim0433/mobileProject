package com.example.android0027.Article;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.50.159:8082";
    //http://192.168.50.159:8082/ 직장
    // http://172.30.1.3:8082/ 집
    //http://192.168.244.7:8082/ 핫스팟

    public static Retrofit getArticleInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

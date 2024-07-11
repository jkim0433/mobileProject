package com.example.android0027.Article;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ArticleSevice {

    @GET("api/articles")
    Call<List<ArticleResponse>> getArticleData();

    @GET("api/articles/{articleId}/comments")
    Call<List<CommentResponse>> getCommentData(@Path("articleId") int articleId );
}


package com.example.android0027.Article;

import com.google.gson.annotations.SerializedName;

public class CommentResponse {
    @SerializedName("id")
    private int ComeId;

    @SerializedName("articleId")
    private int articleId;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("body")
    private String body;

    // 생성자, Getter
    public int getId(){return ComeId;}
    public String getNickname(){return nickname;}

    public String getBody(){return body;}
}
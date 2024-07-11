package com.example.android0027.Article;

import com.google.gson.annotations.SerializedName;

public class ArticleResponse {

    @SerializedName("id")
    private int ArtId;

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    public int getId(){return ArtId;}
    public String getTitle(){return title;}

    public String getContent(){return content;}
}

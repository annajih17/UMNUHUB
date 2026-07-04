package com.example.umnu_hub;

public class Postingan {

    private String id;
    private String text;

    private int like;
    private int komentar;
    private String media;

    public Postingan(
            String id,
            String text,
            String media,
            int like,
            int komentar
    ){

        this.id=id;
        this.text=text;
        this.media=media;
        this.like=like;
    }

    public String getId(){
        return id;
    }

    public String getText(){
        return text;
    }

    public String getMedia(){
        return media;
    }

    public int getLike(){
        return like;
    }

    public int getKomentar(){
        return komentar;
    }

}
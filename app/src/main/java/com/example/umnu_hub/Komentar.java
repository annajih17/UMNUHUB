package com.example.umnu_hub;

public class Komentar {

    private String nama;
    private String komentar;

    public Komentar(String nama, String komentar) {
        this.nama = nama;
        this.komentar = komentar;
    }

    public String getNama() {
        return nama;
    }

    public String getKomentar() {
        return komentar;
    }
}
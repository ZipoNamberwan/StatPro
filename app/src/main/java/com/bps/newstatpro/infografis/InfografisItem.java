package com.bps.newstatpro.infografis;

import java.io.Serializable;

public class InfografisItem implements Serializable {
    private String id;
    private String judul;
    private String gambar;
    private String deskripsi;
    private String urlDownload;

    public InfografisItem(String id, String judul, String gambar, String deskripsi, String urlDownload){
        this.id = id;
        this.judul = judul;
        this.gambar = gambar;
        this.deskripsi = deskripsi;
        this.urlDownload = urlDownload;
    }

    public String getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getGambar() {
        return gambar;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getUrlDownload() {
        return urlDownload;
    }
}

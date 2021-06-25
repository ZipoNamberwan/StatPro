package com.bps.statistikprobolinggo.indikator;

public class RecentIndikatorItem {
    private String id;
    private String judul;
    private String nilai;
    private String deskripsi;
    private String satuan;
    private String sumber;
    private VertikalVariabel verVar;
    private TurunanVertikalVariabel turVar;
    private boolean isLoaded;

    public RecentIndikatorItem(String id, String judul, String deskripsi, String sumber, String nilai, String satuan, VertikalVariabel verVar, TurunanVertikalVariabel turVar, boolean isLoaded){
        this.id = id;
        this.judul = judul;
        this.sumber = sumber;
        this.setDeskripsi(deskripsi);
        this.nilai= nilai;
        this.satuan = satuan;
        this.verVar = verVar;
        this.turVar = turVar;
        this.setLoaded(isLoaded);
    }

    public String getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getNilai() {
        return nilai;
    }

    public String getSatuan() {
        return satuan;
    }

    public String getSumber() {
        return sumber;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public VertikalVariabel getVerVar() {
        return verVar;
    }

    public TurunanVertikalVariabel getTurVar() {
        return turVar;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}

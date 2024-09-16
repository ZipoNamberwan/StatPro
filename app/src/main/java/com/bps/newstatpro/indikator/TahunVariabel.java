package com.bps.newstatpro.indikator;

public class TahunVariabel {

    private String id;
    private String label;

    public TahunVariabel(String id, String label){
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }
}

package com.eaglez.hilib.components.material;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class MaterialFile {
    private String name, url;
    private @ServerTimestamp Date date_uploaded;

    public MaterialFile() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate_uploaded() {
        return date_uploaded;
    }

    public void setDate_uploaded(Date date_uploaded) {
        this.date_uploaded = date_uploaded;
    }
}

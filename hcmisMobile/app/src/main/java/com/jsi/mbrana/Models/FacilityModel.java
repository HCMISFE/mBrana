package com.jsi.mbrana.Models;

/**
 * Created by Sololia on 8/30/2016.
 */

public class FacilityModel {
    String name;
    String version;
    int id_;
    int image;

    public FacilityModel(String name, String version, int id_, int image) {
        this.name = name;
        this.version = version;
        this.id_ = id_;
        this.image=image;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public int getImage() {
        return image;
    }

    public int getId() {
        return id_;
    }
}

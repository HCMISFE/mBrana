package com.jsi.mbrana.Models;

/**
 * Created by Sololia on 6/13/2016.
 */
public class MenuObject {
    private String title, Description;

    public MenuObject() {
    }

    public MenuObject(String title, String Description) {
        this.title = title;
        this.Description = Description;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }


}

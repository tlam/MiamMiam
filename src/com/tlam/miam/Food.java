package com.tlam.miam;

public class Food {

    private String name;
    private String description;
    private long category;

    public Food(String name, String description, long category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
       return this.description;
    }

    public long getCategory() {
       return this.category;
    }

}

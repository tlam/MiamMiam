package com.tlam.miam;

public class Food {

    private String name;
    private String description;
    private String slug;
    private long category;

    public Food(String name, String description, String slug, long category) {
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.category = category;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
       return this.description;
    }

    public String getSlug() {
       return this.slug;
    }

    public long getCategory() {
       return this.category;
    }

}

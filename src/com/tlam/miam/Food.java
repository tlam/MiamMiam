package com.tlam.miam;

public class Food {

    private long id;
    private String name;
    private String description;
    private String slug;
    private long category;

    public Food(long id, String name, String description, String slug, long category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.category = category;
    }

    public long getId() {
        return this.id;
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

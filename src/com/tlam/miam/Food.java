package com.tlam.miam;

public class Food {

    private long mId;
    private String mName;
    private String mDescription;
    private String mSlug;
    private long mCategory;

    public Food(long id, String name, String description, String slug, long category) {
        this.mId = id;
        this.mName = name;
        this.mDescription = description;
        this.mSlug = slug;
        this.mCategory = category;
    }

    public long getId() {
        return this.mId;
    }

    public String getName() {
        return this.mName;
    }

    public String getDescription() {
       return this.mDescription;
    }

    public String getSlug() {
       return this.mSlug;
    }

    public long getCategory() {
       return this.mCategory;
    }

}

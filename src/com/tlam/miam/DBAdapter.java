package com.tlam.miam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;


public class DBAdapter extends SQLiteOpenHelper {

    public static final String CATEGORY_ID = "_id";
    public static final String CATEGORY_NAME = "name";
    public static final String[] CATEGORY_COLS = new String[]
        {CATEGORY_ID, CATEGORY_NAME};
    public static final String FOOD_ITEM_ID = "_id";
    public static final String FOOD_ITEM_NAME = "name";
    public static final String FOOD_ITEM_DESCRIPTION = "description";
    public static final String FOOD_ITEM_SLUG = "slug";
    public static final String FOOD_ITEM_CATEGORY = "category_id";
    public static final String[] FOOD_ITEM_COLS = new String[]
        {FOOD_ITEM_ID, FOOD_ITEM_NAME, FOOD_ITEM_DESCRIPTION, FOOD_ITEM_SLUG, FOOD_ITEM_CATEGORY};

    private static final String TAG = "DBAdapter";
    private static final String DB_NAME = "miam_miam.db";
    private static final String CATEGORY_TABLE = "category";
    private static final String FOOD_ITEM_TABLE = "food_item";
    private static final int DB_VERSION = 1;

    private static final String CREATE_CATEGORY_TABLE = "CREATE TABLE "
        + CATEGORY_TABLE + " ("
        + CATEGORY_ID + " INTEGER PRIMARY KEY, "
        + CATEGORY_NAME + " TEXT NOT NULL);";
    private static final String CREATE_FOOD_ITEM_TABLE = "CREATE TABLE "
        + FOOD_ITEM_TABLE + " ("
        + FOOD_ITEM_ID + " INTEGER PRIMARY KEY, "
        + FOOD_ITEM_NAME + " TEXT NOT NULL, "
        + FOOD_ITEM_DESCRIPTION + " TEXT NOT NULL, "
        + FOOD_ITEM_SLUG + " TEXT NOT NULL, "
        + FOOD_ITEM_CATEGORY + " INTEGER NOT NULL);";

    public DBAdapter(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_FOOD_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FOOD_ITEM_TABLE);
        onCreate(db);
    }

    public void flush() {
        onUpgrade(getWritableDatabase(), 0, 1);
    }

    public long createCategory(String name) {
        ContentValues values = new ContentValues();
        values.put(CATEGORY_NAME, name);

        return getWritableDatabase().insert(CATEGORY_TABLE, null, values);
    }

    public long createFoodItem(String name, String description, String slug, long categoryId) {
        ContentValues values = new ContentValues();
        values.put(FOOD_ITEM_NAME, name);
        values.put(FOOD_ITEM_DESCRIPTION, description);
        values.put(FOOD_ITEM_SLUG, slug);
        values.put(FOOD_ITEM_CATEGORY, categoryId);

        return getWritableDatabase().insert(FOOD_ITEM_TABLE, null, values);

    }

    public Cursor getCategories() {
        return getReadableDatabase().query(CATEGORY_TABLE, CATEGORY_COLS, null, null, null, null, null);
    }

    public Cursor getCategory(String id) {
        Cursor c = getReadableDatabase().query(true, CATEGORY_TABLE, CATEGORY_COLS, CATEGORY_ID + "=" + id, null,
            null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getFoodItem(long foodId) {
        return getReadableDatabase().query(FOOD_ITEM_TABLE, FOOD_ITEM_COLS, FOOD_ITEM_ID + "=" + foodId, null,
            null, null, null, null);
    }

    public Cursor getFoodItems(long categoryId) {
        return getReadableDatabase().query(FOOD_ITEM_TABLE, FOOD_ITEM_COLS, FOOD_ITEM_CATEGORY + "=" + categoryId, null,
            null, null, null, null);
    }

    public long count() {
        return DatabaseUtils.queryNumEntries(getReadableDatabase(), CATEGORY_TABLE);
    }

    public List<Food> getFood(long categoryId) {
        List<Food> foodList = new ArrayList<Food>();
        Cursor c = getFoodItems(categoryId);

        c.moveToFirst();
        while (!c.isAfterLast()) {
            long foodId = c.getLong(c.getColumnIndexOrThrow(FOOD_ITEM_ID));
            String foodName = c.getString(c.getColumnIndexOrThrow(FOOD_ITEM_NAME));
            String foodDescription = c.getString(c.getColumnIndexOrThrow(FOOD_ITEM_DESCRIPTION));
            String foodSlug = c.getString(c.getColumnIndexOrThrow(FOOD_ITEM_SLUG));
            Food food = new Food(foodId, foodName, foodDescription, foodSlug, categoryId);
            foodList.add(food);
            c.moveToNext();
        }

        c.close();
        return foodList;
    }

    /*
     * Load data from the resource xml file into the database
     */
    public void loadInitialData(XmlPullParser data) {
        XmlPullParser parser = data;
        try {
            int eventType = parser.getEventType();
            long categoryId = 0;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                if (name != null && eventType == XmlPullParser.START_TAG) {
                    if (name.equals("category")) {
                        String categoryName = parser.getAttributeValue(0);
                        categoryId = createCategory(categoryName);
                    }
                    else if (name.equals("food")) {
                        String foodName = parser.getAttributeValue(0);
                        String foodDescription = parser.getAttributeValue(1);
                        String foodSlug = parser.getAttributeValue(2);
                        createFoodItem(foodName, foodDescription, foodSlug, categoryId);
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            Log.e(TAG, "Problem parsing the data file", e);
        }
    }
}

package com.tlam.miam;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;


public class FoodDetail extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        long foodId = extras.getLong(DBAdapter.FOOD_ITEM_ID);

        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getFoodItem(foodId);

        c.moveToFirst();
        while (!c.isAfterLast()) {
            String foodName = c.getString(c.getColumnIndexOrThrow(DBAdapter.FOOD_ITEM_NAME));
            String foodDescription = c.getString(c.getColumnIndexOrThrow(DBAdapter.FOOD_ITEM_DESCRIPTION));
            String foodSlug = c.getString(c.getColumnIndexOrThrow(DBAdapter.FOOD_ITEM_SLUG));
            Log.i("FoodDetail", foodName + "; " + foodDescription + "; " + foodSlug);
            c.moveToNext();
        }

        c.close();
        db.close();
    }

}

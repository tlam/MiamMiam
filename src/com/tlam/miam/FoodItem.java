package com.tlam.miam;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

public class FoodItem extends ListActivity {

    public static final String CATEGORY_ID = "category_id";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.food_item);

        Bundle extras = getIntent().getExtras();
        long categoryId = extras.getLong(CATEGORY_ID);

        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getFoodItems(categoryId);

        String[] foodItems = new String[c.getCount()];

        c.moveToFirst();
        int i = 0;
        while (!c.isAfterLast()) {
            String foodName = c.getString(c.getColumnIndexOrThrow(DBAdapter.FOOD_ITEM_NAME));
            String foodDescription = c.getString(c.getColumnIndexOrThrow(DBAdapter.FOOD_ITEM_DESCRIPTION));
            foodItems[i] = foodName + " - " + foodDescription;
            i++;
            c.moveToNext();
        }

        c.close();

        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, foodItems));
    }
}

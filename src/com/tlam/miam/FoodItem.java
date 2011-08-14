package com.tlam.miam;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

public class FoodItem extends Activity {

    public static final String CATEGORY_ID = "category_id";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_item);
        TextView name = (TextView)findViewById(R.id.name);
        TextView description = (TextView)findViewById(R.id.description);

        Bundle extras = getIntent().getExtras();
        long categoryId = extras.getLong(CATEGORY_ID);

        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getFoodItems(categoryId);

        c.moveToFirst();
        while (c.isAfterLast() == false) {
            String foodName = c.getString(c.getColumnIndexOrThrow(DBAdapter.FOOD_ITEM_NAME));
            String foodDescription = c.getString(c.getColumnIndexOrThrow(DBAdapter.FOOD_ITEM_DESCRIPTION));
            name.setText(foodName);
            description.setText(foodDescription);
            c.moveToNext();
        }

        c.close();
    }
}

package com.tlam.miam;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


public class FoodDetail extends Activity {

    private static final String TAG = "FoodDetail";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detail);

        Bundle extras = getIntent().getExtras();
        long foodId = extras.getLong(DBAdapter.FOOD_ITEM_ID);
        String foodDetailName = extras.getString(FoodItem.FOOD);
        setTitle(foodDetailName);

        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getFoodItem(foodId);

        c.moveToFirst();
        while (!c.isAfterLast()) {
            String name = c.getString(c.getColumnIndexOrThrow(DBAdapter.FOOD_ITEM_NAME));
            String description = c.getString(c.getColumnIndexOrThrow(DBAdapter.FOOD_ITEM_DESCRIPTION));
            String foodSlug = c.getString(c.getColumnIndexOrThrow(DBAdapter.FOOD_ITEM_SLUG));
            Log.i("FoodDetail", name + "; " + description + "; " + foodSlug);
            try {
                ImageView foodImage = (ImageView)findViewById(R.id.food_image);
                int iconId = getResources().getIdentifier(foodSlug, "drawable", getPackageName());
                Drawable d = getResources().getDrawable(iconId);
                foodImage.setImageDrawable(d);
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Food image not found for " + name, e);
            }
            TextView foodDescription = (TextView)findViewById(R.id.food_detail_description);
            foodDescription.setText(description);
            c.moveToNext();
        }

        c.close();
        db.close();
    }

}

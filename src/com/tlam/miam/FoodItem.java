package com.tlam.miam;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FoodItem extends ListActivity {

    public static final String CATEGORY_ID = "category_id";
    private static final String TAG = "FoodItem";
    private List<Food> m_food = null;
    private FoodAdapter m_adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list);

        this.m_food = new ArrayList<Food>();
        Bundle extras = getIntent().getExtras();
        long categoryId = extras.getLong(CATEGORY_ID);

        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getFoodItems(categoryId);

        c.moveToFirst();
        while (!c.isAfterLast()) {
            String foodName = c.getString(c.getColumnIndexOrThrow(DBAdapter.FOOD_ITEM_NAME));
            String foodDescription = c.getString(c.getColumnIndexOrThrow(DBAdapter.FOOD_ITEM_DESCRIPTION));
            String foodSlug = c.getString(c.getColumnIndexOrThrow(DBAdapter.FOOD_ITEM_SLUG));
            Food food = new Food(foodName, foodDescription, foodSlug, categoryId);
            this.m_food.add(food);
            c.moveToNext();
        }

        c.close();

        Log.i("IMAGETEST", this.getFilesDir().getPath());

        this.m_adapter = new FoodAdapter(this, R.layout.food_item, this.m_food);
        setListAdapter(this.m_adapter);
    }

    private class FoodAdapter extends ArrayAdapter<Food> {

        private List<Food> items;

        public FoodAdapter(Context context, int textViewResourceId, List<Food> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.food_item, null);
            }

            Food food = items.get(position);
            if (food != null) {
                TextView foodName = (TextView)v.findViewById(R.id.food_name);
                if (foodName != null) {
                    foodName.setText(food.getName());
                }

                TextView foodDescription = (TextView)v.findViewById(R.id.food_description);
                if (foodDescription != null) {
                    foodDescription.setText(food.getDescription());
                }

                try {
                    ImageView foodIcon = (ImageView)v.findViewById(R.id.food_icon);
                    int iconId = getResources().getIdentifier(food.getSlug(), "drawable", getPackageName());
                    Drawable d = getResources().getDrawable(iconId);
                    foodIcon.setImageDrawable(d);
                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Food icon not found for " + food.getName(), e);
                }
            }

            return v;
        }
    }
}

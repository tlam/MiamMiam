package com.tlam.miam;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xmlpull.v1.XmlPullParser;

public class FoodItem extends ListActivity {

    public static final String CATEGORY_ID = "category_id";
    private ArrayList<Food> m_food = null;
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
            Food food = new Food(foodName, foodDescription, categoryId);
            this.m_food.add(food);
            c.moveToNext();
        }

        c.close();

        this.m_adapter = new FoodAdapter(this, R.layout.food_item, this.m_food);
        setListAdapter(this.m_adapter);
    }

    private class FoodAdapter extends ArrayAdapter<Food> {

        private ArrayList<Food> items;

        public FoodAdapter(Context context, int textViewResourceId, ArrayList<Food> items) {
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
                    Log.i("FOODADAPTER", "" + food.getDescription());
                    foodDescription.setText(food.getDescription());
                }
            }

            return v;
        }
    }
}

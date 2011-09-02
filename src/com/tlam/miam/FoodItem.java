package com.tlam.miam;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class FoodItem extends ListActivity {

    public static final String CATEGORY = "category";
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_FOOD = "category_food";
    private static final String TAG = "FoodItem";
    private ArrayList<Food> m_food = null;
    private FoodAdapter m_adapter;
    private long categoryId;
    private String categoryName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list);

        this.m_food = new ArrayList<Food>();
        Bundle extras = getIntent().getExtras();
        categoryName = extras.getString(CATEGORY);
        categoryId = extras.getLong(CATEGORY_ID);
        setTitle(categoryName);

        DBAdapter db = new DBAdapter(this);
        this.m_food = db.getFood(categoryId);

        this.m_adapter = new FoodAdapter(this, R.layout.food_item, this.m_food);
        setListAdapter(this.m_adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        /*
        Intent i = new Intent(this, FoodDetail.class);
        i.putExtra(DBAdapter.FOOD_ITEM_ID, this.m_food.get(position).getId());
        startActivityForResult(i, 1);
        */
        Intent i = new Intent(this, DetailGallery.class);
        i.putExtra(DBAdapter.FOOD_ITEM_ID, this.m_food.get(position).getId());
        i.putExtra(CATEGORY, categoryName);
        i.putExtra(CATEGORY_ID, categoryId);
        startActivityForResult(i, 1);
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

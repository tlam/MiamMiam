package com.tlam.miam;

import java.lang.Math;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class FoodDetail extends Activity {

    public static final String MAX_INDEX = "maxIndex";
    public static final String NEXT = "next";
    public static final String PREV = "prev";
    private static final String TAG = "FoodDetail";
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private GestureDetector gestureDetector;
    private long[] m_pks;
    private long prev;
    private long next;
    private Intent i;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detail);

        Bundle extras = getIntent().getExtras();
        long foodId = extras.getLong(DBAdapter.FOOD_ITEM_ID);
        i = new Intent(this, FoodDetail.class);
        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getFoodItem(foodId);

        c.moveToFirst();
        long categoryId = 0;
        while (!c.isAfterLast()) {
            String name = c.getString(c.getColumnIndexOrThrow(DBAdapter.FOOD_ITEM_NAME));
            String description = c.getString(c.getColumnIndexOrThrow(DBAdapter.FOOD_ITEM_DESCRIPTION));
            String foodSlug = c.getString(c.getColumnIndexOrThrow(DBAdapter.FOOD_ITEM_SLUG));
            categoryId = c.getLong(c.getColumnIndexOrThrow(DBAdapter.FOOD_ITEM_CATEGORY));
            Log.i("FoodDetail", name + "; " + description + "; " + foodSlug);
            try {
                ImageView foodImage = (ImageView)findViewById(R.id.food_image);
                int iconId = getResources().getIdentifier(foodSlug, "drawable", getPackageName());
                Drawable d = getResources().getDrawable(iconId);
                foodImage.setImageDrawable(d);
            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Food image not found for " + name, e);
            }
            TextView foodName = (TextView)findViewById(R.id.food_detail_name);
            TextView foodDescription = (TextView)findViewById(R.id.food_detail_description);
            foodName.setText(name);
            foodDescription.setText(description);
            setTitle(name);
            c.moveToNext();
        }

        c = db.getFoodItems(categoryId);
        int numItems = c.getCount();
        m_pks = new long[numItems];
        c.moveToFirst();
        int index = 0;
        long foodPk = 0;
        while (!c.isAfterLast()) {
            foodPk = c.getLong(c.getColumnIndexOrThrow(DBAdapter.FOOD_ITEM_ID));
            m_pks[index] = foodPk;
            c.moveToNext();
            index++;
        }

        c.close();
        db.close();

        int maxIndex = m_pks.length - 1;
        int position = Arrays.binarySearch(m_pks, foodId);
        int prevPosition = Math.max(0, position - 1);
        int nextPosition = Math.min(maxIndex, position + 1);

        prev = m_pks[prevPosition];
        next = m_pks[nextPosition];

        if (prev == foodId) {
            prev = 0;
        }

        if (next == foodId) {
            next = 0;
        }
        gestureDetector = new GestureDetector(new MyGestureDetector());

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        else {
            return false;
        }
    }

    private class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Log.i("FoodDetail", "Left Swipe");
                    if (next > 0) {
                        i.putExtra(DBAdapter.FOOD_ITEM_ID, next);
                        startActivityForResult(i, 1);
                    }
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Log.i("FoodDetail", "Right Swipe");
                    if (prev > 0) {
                        i.putExtra(DBAdapter.FOOD_ITEM_ID, prev);
                        startActivityForResult(i, 1);
                    }
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }
    }
}

package com.tlam.miam;

import android.app.Activity;
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

    private static final String TAG = "FoodDetail";
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private GestureDetector gestureDetector;

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
            TextView foodName = (TextView)findViewById(R.id.food_detail_name);
            TextView foodDescription = (TextView)findViewById(R.id.food_detail_description);
            foodName.setText(name);
            foodDescription.setText(description);
            c.moveToNext();
        }

        c.close();
        db.close();

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
                    //Toast.makeText(SelectFilterActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
                    Log.i("FoodDetail", "Left Swipe");
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    //Toast.makeText(SelectFilterActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
                    Log.i("FoodDetail", "Right Swipe");
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }
    }
}

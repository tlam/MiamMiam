package com.tlam.miam;

import android.app.Activity;
import android.content.res.TypedArray;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DetailGallery extends Activity
{
    private List<Food> mFood;
    private List<Integer> mImageIds;
    private String mCategory;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_gallery);

        Bundle extras = getIntent().getExtras();
        mCategory = extras.getString(FoodItem.CATEGORY);
        int position = extras.getInt(FoodItem.FOOD_POSITION);
        long categoryId = extras.getLong(FoodItem.CATEGORY_ID);
        DBAdapter db = new DBAdapter(this);
        mFood = db.getFood(categoryId);

        mImageIds = new ArrayList<Integer>();

        for (int i=0; i < mFood.size(); i++) {
            mImageIds.add(getResources().getIdentifier(mFood.get(i).getSlug(), "drawable", getPackageName()));
        }

        Gallery gallery = (Gallery)findViewById(R.id.gallery);
        gallery.setAdapter(new ImageAdapter(this));
        gallery.setSelection(position, true);
        setTitle(mCategory + " - " + mFood.get(position).getName());


        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String foodName = mFood.get(position).getName();
                Toast.makeText(DetailGallery.this, foodName, Toast.LENGTH_SHORT).show();
                setTitle(mCategory + " - " + foodName);
            }
        });

        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView a, View v, int position, long id) {
                String foodName = mFood.get(position).getName();
                setTitle(mCategory + " - " + foodName);

                //TextView galleryName = (TextView)findViewById(R.id.gallery_name);
                //galleryName.setText(foodName);
            }

            @Override
            public void onNothingSelected(AdapterView a) {
            }
        });

        db.close();
    }

    public class ImageAdapter extends BaseAdapter {
        int mGalleryItemBackground;
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
            TypedArray attr = mContext.obtainStyledAttributes(R.styleable.HelloGallery);
            mGalleryItemBackground = attr.getResourceId(
                    R.styleable.HelloGallery_android_galleryItemBackground, 0);
            attr.recycle();
        }

        public int getCount() {
            return mImageIds.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(mContext);

            imageView.setImageResource(mImageIds.get(position));
            //imageView.setLayoutParams(new Gallery.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            //imageView.setBackgroundResource(mGalleryItemBackground);

            return imageView;
        }
    }
}

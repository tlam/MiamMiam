package com.tlam.miam;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.TypedArray;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Gallery;
import android.widget.Toast;

public class DetailGallery extends Activity
{
    private ArrayList<Food> mFood;
    private ArrayList<Integer> mImageIds;
    private String category;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_gallery);

        Bundle extras = getIntent().getExtras();
        category = extras.getString(FoodItem.CATEGORY);
        setTitle(category);
        long categoryId = extras.getLong(FoodItem.CATEGORY_ID);
        DBAdapter db = new DBAdapter(this);
        mFood = db.getFood(categoryId);

        mImageIds = new ArrayList<Integer>();

        for (int i=0; i < mFood.size(); i++) {
            mImageIds.add(getResources().getIdentifier(mFood.get(i).getSlug(), "drawable", getPackageName()));
        }

        Gallery gallery = (Gallery) findViewById(R.id.gallery);
        gallery.setAdapter(new ImageAdapter(this));

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String foodName = mFood.get(position).getName();
                Toast.makeText(DetailGallery.this, foodName, Toast.LENGTH_SHORT).show();
                setTitle(category + " - " + foodName);
            }
        });
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
            imageView.setLayoutParams(new Gallery.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundResource(mGalleryItemBackground);

            return imageView;
        }
    }
}

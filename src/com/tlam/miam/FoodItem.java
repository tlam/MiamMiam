package com.tlam.miam;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FoodItem extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_item);
        TextView name = (TextView)findViewById(R.id.name);
        TextView description = (TextView)findViewById(R.id.description);

        name.setText("Believe");
        description.setText("Cher - Believe");
        /*
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String chinese = extras.getString(DimSumDBAdapter.KEY_CHINESE_NAME);
            String english = extras.getString(DimSumDBAdapter.KEY_ENGLISH_NAME);
            if (chinese != null) {
                chineseName.setText(chinese);
            }
            if (english != null) {
                englishName.setText(english);
            }
        }
        */
    }
}

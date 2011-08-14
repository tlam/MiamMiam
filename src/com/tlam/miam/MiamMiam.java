package com.tlam.miam;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MiamMiam extends ListActivity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        String[] foodCategories = getResources().getStringArray(R.array.food_categories);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, foodCategories));

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
/*
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                    Toast.LENGTH_SHORT).show();
            }

        });*/
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, FoodItem.class);
        startActivityForResult(i, 1);
    }
}

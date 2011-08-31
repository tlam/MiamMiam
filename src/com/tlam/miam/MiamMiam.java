package com.tlam.miam;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MiamMiam extends ListActivity
{
    private DBAdapter db;
    private Cursor cursor;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);
        db = new DBAdapter(this);
        db.open();
        db.flush();
        fillData();
        //db.close();
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        TextView category = (TextView)v.findViewById(R.id.category_name);
        Intent i = new Intent(this, FoodItem.class);
        i.putExtra(FoodItem.CATEGORY, category.getText());
        i.putExtra(FoodItem.CATEGORY_ID, id);
        startActivityForResult(i, 1);
    }

    private void fillData() {
        if (db.count() <= 0) {
            db.loadInitialData(getResources().getXml(R.xml.miam_miam));
        }

        cursor = db.getCategories();
        startManagingCursor(cursor);

        String[] from = new String[] {DBAdapter.CATEGORY_NAME};
        int[] to = new int[] { R.id.category_name };
        SimpleCursorAdapter categories = new SimpleCursorAdapter(this,
            R.layout.category_item, cursor, from, to);
        setListAdapter(categories);
    }
}

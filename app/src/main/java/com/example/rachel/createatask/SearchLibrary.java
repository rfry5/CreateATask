package com.example.rachel.createatask;

import android.app.ActionBar;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchLibrary extends AppCompatActivity {

    private ArrayList<MyImage> images;
    private ItemListView itemListView;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_library);

        SearchView searchView = (SearchView) findViewById(R.id.search_items);
        searchView.setIconifiedByDefault(false);


//        // Construct the data source
//        images = new ArrayList();
//        // Create the adapter to convert the array to views
//        itemListView = new ItemListView(this, images);
//        // Attach the adapter to a ListView
//        listView = (ListView) findViewById(R.id.main_list_view);
//        listView.setAdapter(itemListView);
    }
}

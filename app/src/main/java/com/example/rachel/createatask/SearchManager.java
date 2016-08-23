//package com.example.rachel.createatask;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//
//public class SearchManager extends AppCompatActivity {
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//        Log.d("search", "search triggered");
//        // Get the intent, verify the action and get the query
//        Intent intent = getIntent();
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            doMySearch(query);
//        }
//    }
//
//}

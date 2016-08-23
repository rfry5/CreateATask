package com.example.rachel.createatask;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.VideoView;

import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.R.id.list;
import static com.example.rachel.createatask.AndroidDatabaseManager.indexInfo.index;


public class InfoAdapter extends ArrayAdapter<ItemInfo> implements Filterable {

    private SearchView mSearchView;
    private TextView mTextView;
    DatabaseHelp helper;
    TextView txtName, txtSku, txtLocation, txtDescription;
    private ImageView mImageView;
    private VideoView mVideoView;
    private List<ItemInfo> itemInfo;
    SQLiteDatabase db;
    private Filter filter;
    private final String TAG = "DEBUG_TAG";

    //Changed info from original, FOR BASEADAPTER
    private ArrayList<ItemInfo> original;
    private ArrayList<ItemInfo> filtItems;
    Context c;


    public InfoAdapter(Context context, ArrayList<ItemInfo> items) {
        super(context, 0, items);
        this.original = new ArrayList<ItemInfo>(items);
        printArray(original);
        printArray(items);
        this.filtItems = new ArrayList<ItemInfo>(items);
        this.c = context;
//        DatabaseHelp helper = new DatabaseHelp(context);
//        helper.getAll().size();
//        System.out.println("in info adapter" + helper.getAll().size());
    }

    public void printArray(ArrayList<ItemInfo> stuff){
        System.out.println("Printing out Array List");
        for(ItemInfo s : stuff){
            System.out.println(s.getItemname());
        }
    }
//    @Override
//    public void add(ItemInfo item){
//        System.out.println("add in infoadapter " + item);
//        original.add(item);
//    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        DatabaseHelp helper = new DatabaseHelp(c);
//        helper.getAll().size();
//        System.out.println("INSIDE GET VIEW " + helper.getAll().size());
        // Get the data item for this position
        ItemInfo item = getItem(position);
        System.out.println("Position in getView " + position);
        System.out.println("Size of orignial in getview " + original.size());
        System.out.println("In get view at adapter LINE 81 " + item.getID());
        System.out.println("In get view at adapter LINE 82 " + item.getItemname());

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_listview, parent, false);
        }
        // Lookup view for data population
        txtName = (TextView) convertView.findViewById(R.id.item_name_listview);
        txtSku = (TextView) convertView.findViewById(R.id.sku_listview);
        txtLocation = (TextView) convertView.findViewById(R.id.location_listview);
        txtDescription = (TextView) convertView.findViewById(R.id.description_listview);
        mImageView = (ImageView) convertView.findViewById(R.id.item_img_icon);

        // Populate the data into the template view using the data object
        txtName.setText(item.getItemname());
        txtSku.setText(item.getSku());
        txtLocation.setText(item.getLocation());
        txtDescription.setText(item.getDescription());
        System.out.println(item.getDescription());
        mImageView.setImageBitmap(getImage(item.getThumbnail()));

        // Return the completed view to render on screen
        return convertView;
    }

    Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

//
//    @Override
//    public long getItemId(int position) {
//        return position;
////        return original.indexOf(getItem(position));
//    }

    @Override
    public Filter getFilter()
    {
        if (filter == null)
            filter = new ItemListFilter();

        System.out.println("INSIDE GET FILTER");
        return filter;
    }

    private class ItemListFilter extends Filter {
        //Scope of variables... treat like new file
        DatabaseHelp helper = new DatabaseHelp(c);

        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            System.out.println("INSIDE PERFORM FILTERING");
            FilterResults results = new FilterResults();
            String prefix = constraint.toString().toLowerCase();

            if (prefix == null || prefix.length() == 0)
            {
                System.out.println("INSIDE prefix == null ");
                ArrayList<ItemInfo> list = new ArrayList<ItemInfo>(original);
                results.values = list;
                System.out.println("Array list from inside prefix== null, output is results " + results.values.toString());
                results.count = list.size();
                System.out.println("Array list from inside prefix== null, output is results count " + results.count);
            }
            else
            {
                System.out.println("INSIDE prefix not null ");
                original = helper.getAll();
                ArrayList<ItemInfo> list = new ArrayList<ItemInfo>(original);
                ArrayList<ItemInfo> nlist = new ArrayList<ItemInfo>();
                int count = list.size();

                System.out.println("count of original list size " + count);
//                System.out.println(list.get(5).getItemname());

                for (int i=0; i<count; i++){
                    ItemInfo item = list.get(i);
                    System.out.println("Inside for loop adding items " + item.getItemname());
                    String value = item.getItemname().toLowerCase();
                    String skuValue = item.getSku().toLowerCase();
                    String locationValue = item.getLocation().toLowerCase();

                    if (value.startsWith(prefix)){
                        System.out.println("Value starts with prefix " + item.getItemname());
                        nlist.add(item);
                        System.out.println("Value starts with prefix size " + nlist.size());
                    }
                    if (skuValue.startsWith(prefix)){
                        System.out.println("Value starts with prefix " + item.getSku());
                        nlist.add(item);
                        System.out.println("Value starts with prefix size " + nlist.size());
                    }
                    if (locationValue.startsWith(prefix)){
                        System.out.println("Value starts with prefix " + item.getLocation());
                        nlist.add(item);
                        System.out.println("Value starts with prefix size " + nlist.size());
                    }

                }
                results.values = nlist;
                results.count = nlist.size();
                System.out.println("NLIST RESULTS " + nlist.size());

            }
            return results;
        }
//
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            System.out.println("INSIDE PUBLISH RESULTS " );
            filtItems = (ArrayList<ItemInfo>)results.values;
            clear();
            int count = filtItems.size();
            for (int i=0; i<count; i++)
            {
                ItemInfo item = (ItemInfo) filtItems.get(i);
                add(item);
            }
        }
    }
}


//
//    @Override
//    public int getCount() {
//        return original.size();
//    }
//
//    @Override
//    public ItemInfo getItem(int position) {
//        return original.get(position);
//    }


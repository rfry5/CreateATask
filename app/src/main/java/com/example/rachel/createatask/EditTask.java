package com.example.rachel.createatask;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.roughike.bottombar.OnTabSelectedListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.rachel.createatask.R.id.location;
import static com.example.rachel.createatask.R.id.sku;

public class EditTask extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Bitmap mImageBitmap;
    String selected_ID = "";

    private VideoView mVideoView;
    private Uri mVideoUri;
    private String mCurrentPhotoPath;
    private ImageView mImageView;
    private Uri mCapturedImageURI;
    private Uri mCapturedVideoURI;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_VIDEO_CAPTURE = 3;
    private static int VID_OR_PIC = 1;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int RESULT_LOAD_IMAGE = 4;
    private static final int RESULT_LOAD_VIDEO = 5;
    SQLiteDatabase db;


    //For database
    DatabaseHelp helper = new DatabaseHelp(this);

    //For Bottom Bar navigation
    private BottomBar mBottomBar;

    //For save button, play button over video and item information input
    Button bSaveButton;
    ImageButton mPlayButton;
    EditText etItemName, etSku, etLocation, etDescription;
    String spinTypeString, spinSizeString;
    Spinner mSpinner;

    //For prompt command text
    EditText etCom1, etCom2, etCom3, etCom4, etCom5, etCom6;


    //Setting button listener for take photo and take video
    Button.OnClickListener mTakePicSOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
                    activeTakePhoto();
                }
            };

    Button.OnClickListener mTakeVidOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dispatchTakeVideoIntent();
                    activeTakeVideo();
                }
            };


    //When upload from gallery button is clicked (gallUpload is defined as onClick in new_create xml file)
    public void galleryUpload(View view){
        System.out.println("in gallery upload");
        final Dialog dialog = new Dialog(EditTask.this);
        dialog.setContentView(R.layout.upload_dialog_box);
        dialog.show();
        dialog.findViewById(R.id.from_gallery_click).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                activeGalleryPhoto();
            }
        });
        dialog.findViewById(R.id.video_gallery).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                activeGalleryVideo();
            }
        });
    }

    //Upload PICTURE from Gallery
    private void activeGalleryPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    //Upload VIDEO from Gallery
    private void activeGalleryVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_VIDEO);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_item_info);

        etItemName = (EditText) findViewById(R.id.item_name_display);
        etSku = (EditText) findViewById(R.id.sku_display);
        etDescription = (EditText) findViewById(R.id.description_display);
        etLocation = (EditText) findViewById(R.id.location_display);
        mImageView = (ImageView) findViewById(R.id.imageView_display);
        mVideoView = (VideoView) findViewById(R.id.videoView_display);
        mPlayButton = (ImageButton) findViewById(R.id.play_button2);
        etCom1 = (EditText) findViewById(R.id.command_line1);
        etCom2 = (EditText) findViewById(R.id.command_line2);
        etCom3 = (EditText) findViewById(R.id.command_line3);
        etCom4 = (EditText) findViewById(R.id.command_line4);
        etCom5 = (EditText) findViewById(R.id.command_line5);
        etCom6 = (EditText) findViewById(R.id.command_line6);
//        mSpinner = (Spinner) findViewById(R.id.type_spinner);

        //Getting info when Listview was clicked from library (SearchableActivity) activity
        Intent i = getIntent();
        String item = i.getStringExtra("item");
        String sku = i.getStringExtra("sku");
        String location = i.getStringExtra("location");
        String description = i.getStringExtra("description");
        String image = i.getStringExtra("image");
        String video = i.getStringExtra("video");
        String post = i.getStringExtra("position");
        String c1 = i.getStringExtra("com1");
        String c2 = i.getStringExtra("com2");
        String c3 = i.getStringExtra("com3");
        String c4 = i.getStringExtra("com4");
        String c5 = i.getStringExtra("com5");
        String c6 = i.getStringExtra("com6");
        spinTypeString = i.getStringExtra("spinType");
        spinSizeString = i.getStringExtra("spinSize");

        System.out.println("IN EDIT TASK SPINTYPE STRING " + spinTypeString);

        System.out.println("IN EDIT TASK LINE 161 " + c1);
        //ADDED TODAY
        byte[] imageBlob = i.getByteArrayExtra("thumbnailblob"); //just added
        /////
        etItemName.setText(item);
        etSku.setText(sku);
        etLocation.setText(location);
        etDescription.setText(description);
        etCom1.setText(c1);
        etCom2.setText(c2);
        etCom3.setText(c3);
        etCom4.setText(c4);
        etCom5.setText(c5);
        etCom6.setText(c6);

        //Spinner for size selection
        Spinner spinner = (Spinner) findViewById(R.id.size_spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.size_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Setting spinner state
        String compareValueSize = spinSizeString;
        System.out.println("Spin size string" + spinSizeString);
        if (!compareValueSize.equals(null)) {
            int spinnerPositionSize = adapter.getPosition(compareValueSize);
            spinner.setSelection(spinnerPositionSize);
            System.out.println("IN EDIT TASK SPINNER COMPARE VALUE ");
        }

        //Spinner for item type selection
        Spinner spinnerType = (Spinner) findViewById(R.id.type_spinner);
        spinnerType.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerType.setAdapter(adapterType);

        //Setting Spinner state
        String compareValue = spinTypeString;
        if (!compareValue.equals(null)) {
            int spinnerPosition = adapterType.getPosition(compareValue);
            spinnerType.setSelection(spinnerPosition);
            System.out.println("IN EDIT TASK SPINNER COMPARE VALUE ");
        }

        //If the image or the video is null, this crashes
        mImageBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(image),200,250);
        mImageView.setImageBitmap(mImageBitmap);
        if (video == null){
            System.out.println("No video file exists");
        } else if (video != null) {
            mVideoView.setVideoPath(video);
        mVideoView.seekTo(100);
        mVideoView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mVideoView.isPlaying())
                {
                    return false;
                }
                else
                {
                    mVideoView.start();
                    mPlayButton.setVisibility(View.GONE);
                    return false;
                }

            }
        });
        }

        //For update button
        bSaveButton = (Button) findViewById(R.id.update_button);
        bSaveButton.setOnClickListener(this);

        //Bottom Bar navigation
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.useFixedMode(); //Shows all titles with more than 3 buttons
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemHome) {
                    // The user selected item number one.
                    startActivity(new Intent(EditTask.this, MainActivity.class));
                } else if (menuItemId == R.id.bottomBarItemTwo) {
                    startActivity(new Intent(EditTask.this, SearchableActivity.class));
                } else if (menuItemId == R.id.bottomBarItemThree) {
                    startActivity(new Intent(EditTask.this, Dashboard.class));
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemTwo) {
                    // The user reselected item number one, scroll your content to top.
                }
            }
        });


        mImageBitmap = null;
        mVideoUri = null;

        Button picSBtn = (Button) findViewById(R.id.take_small_photo_button);
        setBtnListenerOrDisable(
                picSBtn,
                mTakePicSOnClickListener,
                MediaStore.ACTION_IMAGE_CAPTURE
        );

        Button vidBtn = (Button) findViewById(R.id.take_video_button);
        setBtnListenerOrDisable(
                vidBtn,
                mTakeVidOnClickListener,
                MediaStore.ACTION_VIDEO_CAPTURE
        );
    }

    //Saving spinner states
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        switch (parent.getId()) {
            case R.id.size_spinner:
                // An item was selected. You can retrieve the selected item using
                parent.getItemAtPosition(position);
                spinSizeString = parent.getItemAtPosition(position).toString();

                System.out.println("spinnerSize in onItemSwitch " + parent.getItemAtPosition(position));

                break;

            case R.id.type_spinner:

                parent.getItemAtPosition(position);
                spinTypeString = parent.getItemAtPosition(position).toString();

                System.out.println("spinnerType in onItemSwitch " + parent.getItemAtPosition(position));

                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //Creating URI from file
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }


    /** Create a File for saving an image or video */
    //Changed () to int type
    private static File getOutputMediaFile(int type){

        //"" to send it to Pictures folder in gallery
        //File path is pointing to gallery
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "");

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        //Time stamp image and name the image (still blank, allocating file)
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void activeTakeVideo(){
        VID_OR_PIC = 1;
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(takeVideoIntent.resolveActivity(getPackageManager()) != null){
            mCapturedVideoURI = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedVideoURI);
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);        }
    }

    //take a photo
    private void activeTakePhoto() {
        VID_OR_PIC = 2;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            //Creating space for photo
            mCapturedImageURI =  getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            //Now fill URI with picture
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println(requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        mPlayButton = (ImageButton) findViewById(R.id.play_button2);

        if (VID_OR_PIC == 2 && requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            String picturePath = mCapturedImageURI.getPath();
            System.out.println(picturePath);
            MyImage image = new MyImage();
            image.setTitle("Test");
            image.setDescription("test take a photo and add it to list view");
            image.setDatetime(System.currentTimeMillis());
            image.setPath(picturePath);
            mImageBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(image.getPath()),200,250);
            mImageView.setImageBitmap(mImageBitmap);
            mImageView.setVisibility(View.VISIBLE);
        }
        // this is for video
        if(VID_OR_PIC == 1 && requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK){
            Toast.makeText(this, "Video saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
            mVideoView.setVideoPath(mCapturedVideoURI.getPath());
            mVideoView.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mVideoView.isPlaying())
                    {
                        return false;
                    }
                    else
                    {
                        mVideoView.start();
                        mPlayButton.setVisibility(View.GONE);
                        return false;
                    }
                }
            });
            mVideoView.setVisibility(View.VISIBLE);
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null !=data){
            //Gallery is opened, now select photo and display it in imageview and save
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            System.out.println("PATH OF THE SELECTED PHOTO" + picturePath);

            //Displaying it to the imageview
            if (mCapturedImageURI == null){
                mImageBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(picturePath),200,250);
                mImageView.setImageBitmap(mImageBitmap);
                mImageView.setVisibility(View.VISIBLE);
            }
            mCapturedImageURI = Uri.fromFile(new File(picturePath));
        }
        if (requestCode == RESULT_LOAD_VIDEO && resultCode == RESULT_OK && null !=data){
            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String videoPath = cursor.getString(columnIndex);
            cursor.close();
            mVideoView.setVideoPath(videoPath);
            mCapturedVideoURI = Uri.fromFile(new File(videoPath));
            mVideoView.seekTo(100);
            mVideoView.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mVideoView.isPlaying())
                    {
                        return false;
                    }
                    else
                    {
                        mVideoView.start();
                        mPlayButton.setVisibility(View.GONE);
                        return false;
                    }
                }
            });
        }
    }


    // create the image file and name
    private File createImageFile() throws IOException {
        Log.d("PIPE", "Line 231 -- naming the picture");
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Log.d("FILE", mCurrentPhotoPath);
        return image;
    }


    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void setBtnListenerOrDisable(
            Button btn,
            Button.OnClickListener onClickListener,
            String intentName
    ) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(onClickListener);
        } else {
            btn.setText(
                    getText(R.string.cannot).toString() + " " + btn.getText());
            btn.setClickable(false);
        }
    }


    //Bottom bar navigation
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }


    //Updating database

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_button:

                etItemName = (EditText) findViewById(R.id.item_name_display);
                etSku = (EditText) findViewById(R.id.sku_display);
                etDescription = (EditText) findViewById(R.id.description_display);
                etLocation = (EditText) findViewById(R.id.location_display);
                etCom1 = (EditText) findViewById(R.id.command_line1);
                etCom2 = (EditText) findViewById(R.id.command_line2);
                etCom3 = (EditText) findViewById(R.id.command_line3);
                etCom4 = (EditText) findViewById(R.id.command_line4);
                etCom5 = (EditText) findViewById(R.id.command_line5);
                etCom6 = (EditText) findViewById(R.id.command_line6);


                //Gathering new data from the R.id views
                String itemUpdate= etItemName.getText().toString();
                String skuUpdate = etSku.getText().toString();
                String locationUpdate = etLocation.getText().toString();
                String descriptionUpdate = etDescription.getText().toString();
                String com1Update = etCom1.getText().toString();
                String com2Update = etCom2.getText().toString();
                String com3Update = etCom3.getText().toString();
                String com4Update = etCom4.getText().toString();
                String com5Update = etCom5.getText().toString();
                String com6Update = etCom6.getText().toString();
                String spinTypeUpdate = spinTypeString;
                String spintSizeUpdate = spinSizeString;
                System.out.println("IN UPDATE SPINNER TYPE " + spinTypeUpdate);


                //Getting values passed from Listview click
                Intent i = getIntent();
                String post = i.getStringExtra("position");
                String image = i.getStringExtra("image");
                String video = i.getStringExtra("video");
//                String com1 = i.getStringExtra("com1");
                Integer position = Integer.valueOf(String.valueOf(post));
                System.out.println("In update with position " + position);

                //////ADDED TODAY
                byte[] imageBlob = i.getByteArrayExtra("thumbnailblob"); //just added
                ////////

                //Have to take photo with update or it will crash!! Same with video
//                String picturePath = mCapturedImageURI.getPath();
//                mVideoView.setVideoPath(mCapturedVideoURI.getPath());

//                if (mCapturedImageURI.getPath() !=null) {
//                    mCapturedImageURI.getPath();
//                }
//
//                if (mCapturedVideoURI.getPath() !=null) {
//                    mVideoView.setVideoPath(mCapturedVideoURI.getPath());
//                }

                //Setting the values/item inputs
                ItemInfo c = new ItemInfo();
                c.setID(position);
                c.setItemname(itemUpdate);
                c.setSku(skuUpdate);
                c.setLocation(locationUpdate);
                c.setDescription(descriptionUpdate);
                c.setCom1(com1Update);
                c.setCom2(com2Update);
                c.setCom3(com3Update);
                c.setCom4(com4Update);
                c.setCom5(com5Update);
                c.setCom6(com6Update);
                c.setSpinType(spinTypeUpdate);
                c.setSpinSize(spintSizeUpdate);

                ////////ADDED TODAY
                if (mImageBitmap ==null){
                    //I haven't done anything to change the picture, don't need to change the blob
                    c.setThumbnail(imageBlob);
                } else {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream(); //Just added this and line below
                    mImageBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
                    c.setThumbnail(stream.toByteArray());
                }
                //The update button is not updating the blob to the corresponding photo, and this is why
                // the thumbnail isn't updating in the listview
                // if the photo was not updated, leave the mImageBitmap the same
                // if the photo was updated, need to setThumbnail to the corresponding Blob
                // NEED TO PASS THE BLOB THROUGH THE INTENT
//                c.setThumbnail();
                ///////

                //Setting URI path to a string
//                if (mVideoView !=null) {
//                    c.setVideo(mVideoView.toString());
//                }

                if (mCapturedImageURI ==null) {
                    c.setPicture(image);
                    System.out.println("Image path when update is clicked" + image);
                } else {
                    c.setPicture(mCapturedImageURI.getPath());
                }

                System.out.println(mCapturedImageURI);

//                } else {
//                    c.setPicture(mCapturedImageURI.getPath());
//                }

                if (mCapturedVideoURI ==null){
                    c.setVideo(video);
                    System.out.println("Video path when update is clicked" + video);
                }

                if (mCapturedVideoURI != null){
                    System.out.println("IN UPDATE VIDEO" + video);
                    c.setVideo(mCapturedVideoURI.getPath());
                }

                System.out.println("HERE" + mCapturedVideoURI);
//                } else {
//                    c.setVideo(mCapturedVideoURI.getPath());
//                }

//                c.setPicture(mCapturedImageURI.getPath());
//                c.setVideo(mVideoView.toString());

                //Calling udateItems function in DatabaseHelp
                helper.updateItems(c);


                Toast temp = Toast.makeText(EditTask.this, "Update Successful", Toast.LENGTH_SHORT);
                temp.show();
                temp.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                temp.show();

                startActivity(new Intent(this, SearchableActivity.class));

                break;

        }


    }
}
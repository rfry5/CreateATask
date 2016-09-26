package com.example.rachel.createatask;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.attr.checked;
import static android.R.attr.data;
import static android.R.attr.id;
import static android.R.attr.password;
import static android.R.attr.path;
import static android.R.attr.type;
import static android.net.Uri.fromFile;
import static android.os.Build.VERSION_CODES.M;
import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static com.example.rachel.createatask.R.layout.audio;
import android.widget.AdapterView.OnItemSelectedListener;

public class CreateTask extends AppCompatActivity implements View.OnClickListener, OnItemSelectedListener {


    private Bitmap mImageBitmap;

    private VideoView mVideoView;
    private Uri mVideoUri;
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


    //For database
    DatabaseHelp helper = new DatabaseHelp(this);

    //For Bottom Bar navigation
    private BottomBar mBottomBar;

    //For save button, play button over video and item information input
    Button bSaveButton, bRecord;
    ImageButton mPlayButton;
    EditText etItemName, etSku, etLocation, etDescription;
    String spinnerType, spinnerSize;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_create); //CHANGED FROM NEW_CREATE

        etItemName = (EditText) findViewById(R.id.item_name);
        etSku = (EditText) findViewById(R.id.sku);
        etDescription = (EditText) findViewById(R.id.description);
        etLocation = (EditText) findViewById(R.id.location);
        etCom1 = (EditText) findViewById(R.id.command_line1);
        etCom2 = (EditText) findViewById(R.id.command_line2);
        etCom3 = (EditText) findViewById(R.id.command_line3);
        etCom4 = (EditText) findViewById(R.id.command_line4);
        etCom5 = (EditText) findViewById(R.id.command_line5);
        etCom6 = (EditText) findViewById(R.id.command_line6);

        //Spinner for size selection
        Spinner spinner = (Spinner) findViewById(R.id.size_spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.size_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Spinner for item type selection
        Spinner spinnerType = (Spinner) findViewById(R.id.type_spinner);
        spinnerType.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerType.setAdapter(adapterType);


        //For save button
        bSaveButton = (Button) findViewById(R.id.save_button);
        bSaveButton.setOnClickListener(this);

//        //Audio
//        Intent i = getIntent();
//        String audio = i.getStringExtra("audio");
//        System.out.println("RETURN BACK TO CREATE TASK " + audio);

        //Bottom Bar navigation
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.useFixedMode(); //Shows all titles with more than 3 buttons
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setDefaultTabPosition(0);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemHome) {
                    // The user selected item number one.
                    startActivity(new Intent(CreateTask.this, MainActivity.class));
                } else if (menuItemId == R.id.bottomBarItemTwo) {
                    startActivity(new Intent(CreateTask.this, SearchableActivity.class));
                } else if (menuItemId == R.id.bottomBarItemThree) {
                    startActivity(new Intent(CreateTask.this, Dashboard.class));
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemTwo) {
                    // The user reselected item number one, scroll your content to top.
                }
            }
        });

        mImageView = (ImageView) findViewById(R.id.imageView1);
        mVideoView = (VideoView) findViewById(R.id.videoView1);
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

    ///Saving spinner state
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        switch (parent.getId()) {
            case R.id.size_spinner:
                // An item was selected. You can retrieve the selected item using
                parent.getItemAtPosition(pos);
                spinnerSize = parent.getItemAtPosition(pos).toString();

                System.out.println("spinnerSize in onItemSwitch " + parent.getItemAtPosition(pos));

                break;

            case R.id.type_spinner:

                parent.getItemAtPosition(pos);
                spinnerType = parent.getItemAtPosition(pos).toString();

                System.out.println("spinnerType in onItemSwitch " + parent.getItemAtPosition(pos));

                break;

        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    ////When upload from gallery button is clicked (gallUpload is defined as onClick in new_create xml file)
    public void gallUpload(View view){
        System.out.println("in gallery upload");
        final Dialog dialog = new Dialog(CreateTask.this);
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
        dialog.findViewById(R.id.exit_dialog).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();;
            }
        });
    }

    ////Creating URI from file
    private static Uri getOutputMediaFileUri(int type){
        return fromFile(getOutputMediaFile(type));
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
        System.out.println("THE NAME OF THE PATH: Path is "+mediaFile.getAbsolutePath());
        return mediaFile;
    }

    //Upload PICTURE from Gallery
    private void activeGalleryPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    private void activeGalleryVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_VIDEO);
    }

    //Take video
    private void activeTakeVideo(){
        VID_OR_PIC = 1;
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(takeVideoIntent.resolveActivity(getPackageManager()) != null){
            mCapturedVideoURI = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedVideoURI);
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    //Take photo
    private void activeTakePhoto() {
            VID_OR_PIC = 2;
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                //The three lines below send the picture to the gallery, but the app crashes when exiting the camera
//                ContentValues values = new ContentValues();
//                values.put(MediaStore.Images.Media.TITLE, "thing.jpg");
//                Uri mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

                //The two lines below work when taking and saving a picture with a filepath, but it does not
                //show up in the gallery unless I restart the phone
                mCapturedImageURI = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

                System.out.println("INSIDE TAKE PICTURE INTENT "+ mCapturedImageURI.getPath());
                System.out.println("TAKEPICINTENT "+ takePictureIntent.getExtras());
                Toast.makeText(this, "IN HERE Image saved to:\n" + takePictureIntent.getExtras(), Toast.LENGTH_LONG).show();
                //Now fill URI with picture
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            }
    }

    //Need this so the photo shows up in the gallery (for some reason it wouldn't otherwise)
    private void scanMedia(String path) {
        File file = new File(path);
        Uri uri = fromFile(file);
        Intent scanFileIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        sendBroadcast(scanFileIntent);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println(requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        mPlayButton = (ImageButton) findViewById(R.id.play_button);

        if (VID_OR_PIC == 2 && requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Image saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
            //This is here because the photo won't appear in the gallery unless I have this...
            scanMedia(mCapturedImageURI.getPath());

            mImageBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mCapturedImageURI.getPath()),200,250);
            mImageView.setImageBitmap(mImageBitmap);
            mImageView.setVisibility(View.VISIBLE);
        }
        // this is for video
        if(VID_OR_PIC == 1 && requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK){
            Toast.makeText(this, "Video saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
            mVideoView.setVideoPath(mCapturedVideoURI.getPath());
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



//    //Bottom bar navigation
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        // Necessary to restore the BottomBar's state, otherwise we would
//        // lose the current tab on orientation change.
//        mBottomBar.onSaveInstanceState(outState);
//    }


    //Saving item information to database
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_button:

                EditText itemname = (EditText) findViewById(R.id.item_name);
                EditText sku = (EditText) findViewById(R.id.sku);
                EditText location = (EditText) findViewById(R.id.location);
                EditText description = (EditText) findViewById(R.id.description);

                System.out.println("IN SAVE " + spinnerType);
                System.out.println("IN SAVE " + spinnerSize);

                String itemnamestring = itemname.getText().toString();
                String skustring = sku.getText().toString();
                String locationstring = location.getText().toString();
                String descriptionstring = description.getText().toString();

                //For command line text
                String c1 = etCom1.getText().toString();
                System.out.println(c1);
                String c2 = etCom2.getText().toString();
                System.out.println(c2);
                String c3 = etCom3.getText().toString();
                String c4 = etCom4.getText().toString();
                String c5 = etCom5.getText().toString();
                String c6 = etCom6.getText().toString();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mImageBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
                System.out.println("MIMAGE BITMAP IN CREATETASK SAVE ");

                //Passing through database (setting them here, getting them in database help)
                ItemInfo c = new ItemInfo();
                c.setItemname(itemnamestring);
                c.setSku(skustring);
                c.setLocation(locationstring);
                c.setDescription(descriptionstring);
                c.setThumbnail(stream.toByteArray());
                //Setting prompt command lines
                c.setCom1(c1);
                c.setCom2(c2);
                c.setCom3(c3);
                c.setCom4(c4);
                c.setCom5(c5);
                c.setCom6(c6);
                //Setting spinners
                c.setSpinType(spinnerType);
                c.setSpinSize(spinnerSize);


                //If the null, return null, if I uploaded photo, set it to that path, if I took photo, set it to that path
                //Setting URI path to a string
                if (mCapturedImageURI == null){
                    c.setPicture(null);
                } else if (mCapturedImageURI !=null){
                    c.setPicture(mCapturedImageURI.getPath());
                    System.out.println(mCapturedImageURI.getPath());
                }

                //If the video path is null, return null
                if (mCapturedVideoURI == null){
                    c.setVideo(null);
                    System.out.println("IN NULL FILEPATH FOR VIDEO" );
                } else if (mCapturedVideoURI.getPath() != null){
                    System.out.println("FILE PATH OF UPLOADED PHOTO" + mCapturedVideoURI.getPath());
                    c.setVideo(mCapturedVideoURI.getPath());
                }
//                System.out.println(mCapturedVideoURI.getPath());
//                c.setThumbnail(stream.toByteArray());

                //Inserting in database
                helper.insertItem(c);

                Toast temp = Toast.makeText(CreateTask.this, "Save Successful", Toast.LENGTH_SHORT);
                temp.show();
                temp.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                temp.show();

                startActivity(new Intent(this, MainActivity.class));

                break;

//            case R.id.record_audio_button:
//                System.out.println("In record click");
//                startActivity(new Intent(this, RecordAudio.class));
        }
    }
}


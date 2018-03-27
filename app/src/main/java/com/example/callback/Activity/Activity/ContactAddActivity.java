package com.example.callback.Activity.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.callback.Activity.ContactModel.ContactModel;
import com.example.callback.Activity.DbHandler.DbContacts;
import com.example.callback.Activity.MainActivity;
import com.example.callback.BuildConfig;
import com.example.callback.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class ContactAddActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 2;
    private static final String IMAGE_DIRECTORY_NAME = "HelloCamera";
    ImageView back;
    ImageView save, photo;
    EditText name, per_ph, off_ph, home_ph, addr, gender, email;
    RadioGroup rgrp;
    RadioButton rb;
    ContactModel model;
    DbContacts dbContacts;
    String gen = "", off, home, cname;
    Uri selectedImage = null;
    PopupWindow pw;
    LinearLayout ll;
    TextView cam, gal;
    Uri fileUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);

        dbContacts = DbContacts.getsInstance(this);

        ll = (LinearLayout) findViewById(R.id.ll_details);

        name = (EditText) findViewById(R.id.c_name);
        per_ph = (EditText) findViewById(R.id.et_pph);
        off_ph = (EditText) findViewById(R.id.et_off);
        home_ph = (EditText) findViewById(R.id.et_home);
        addr = (EditText) findViewById(R.id.et_addr);
        //gender = (EditText)findViewById(R.id.et_gender);
        email = (EditText) findViewById(R.id.et_email);
        rgrp = (RadioGroup) findViewById(R.id.radio_gender);
        // m = (RadioButton)findViewById(R.id.radio_m);
        //fm = (RadioButton)findViewById(R.id.radio_fm);
        rgrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                rb = (RadioButton) findViewById(checkedId);
                gen = rb.getText().toString();


            }
        });

        back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(this);

        photo = (ImageView) findViewById(R.id.add_photo);
        photo.setVisibility(View.VISIBLE);
        photo.setOnClickListener(this);

        save = (ImageView) findViewById(R.id.iv_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(off_ph.getText().toString()) && off_ph.getText().toString().length() == 8) {
                    off = "033" + off_ph.getText().toString();
                } else {
                    off = off_ph.getText().toString();
                }
                if (!TextUtils.isEmpty(home_ph.getText().toString()) && home_ph.getText().toString().length() == 8) {
                    home = "033" + home_ph.getText().toString();
                } else {
                    home = home_ph.getText().toString();
                }
                cname = name.getText().toString();
                if (!cname.equals("")) {
                    cname = cname.substring(0, 1).toUpperCase() + cname.substring(1).toLowerCase();
                }
                model = new ContactModel(cname, per_ph.getText().toString(), off, home);
                model.setAddr(addr.getText().toString());
                model.setGender(gen);
                model.setEmail(email.getText().toString());
                if (selectedImage != null && fileUri == null)
                    model.setPath(selectedImage.toString());
                else if(selectedImage == null && fileUri != null)
                    model.setPath(fileUri.toString());
                else model.setPath(null);
                if (validateDetails()) {
                    dbContacts.addContact(model);
                    finish();
                } else {
                    Toast.makeText(ContactAddActivity.this, "INVALID ENTRY FIELDS", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean validateDetails() {
        if (!isName(name.getText().toString())) {
            name.setError("Invalid Name");
            return false;
        }
        if (!isPhoneNumber(per_ph.getText().toString())) {
            per_ph.setError("Invalid Number");
            return false;
        }
        if (!off_ph.getText().toString().equals("")) {
            if (!isPhoneNumber(off)) {
                off_ph.setError("Invalid Number");
                return false;
            }
        }
        if (!home_ph.getText().toString().equals("")) {
            if (!isPhoneNumber(home)) {
                home_ph.setError("Invalid Number");
                return false;
            }
        }
        if (!email.getText().toString().equals("")) {
            if (!isEmail(email.getText().toString())) {
                email.setError("Invalid Email Address");
                return false;
            }
        }
        if (gen.equals("")) {
            Toast.makeText(this, "Gender Not Set", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isPhoneNumber(String s) {
        String mobile = "^[1-9]{2}[0-9]{8}$";
        String landline = "^[0-3]{3}[0-9]{8}$";
        Pattern pattern1 = Pattern.compile(mobile);
        Pattern pattern2 = Pattern.compile(landline);
        Matcher matcher1 = pattern1.matcher(s);
        Matcher matcher2 = pattern2.matcher(s);
        return (matcher1.matches() || matcher2.matches());
    }

    private boolean isName(String s) {
        String name = "^[a-zA-Z]{1,}";
        if (!s.equals("")) {
            s = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
        }
        //return s instanceof String;
        //String name = "[A-Z][a-z]+( [A-Z][a-z]+)?";
        Pattern pattern = Pattern.compile(name);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches() || s instanceof String;
    }

    private boolean isEmail(String s) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.add_photo:

                //showAlert();
                final Dialog picOptions = new Dialog(this);
                picOptions.setContentView(R.layout.photo_popup);
                picOptions.setTitle("SELECT");
                picOptions.show();

                /*LayoutInflater inflator = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflator.inflate(R.layout.photo_popup, null);
                pw = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                //pw.setFocusable(true);
                //pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
                //pw.showAtLocation(ll, Gravity.CENTER, 0, 0);*/

                cam = (TextView) picOptions.findViewById(R.id.opt_camera);
                cam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ActivityCompat.checkSelfPermission(ContactAddActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ContactAddActivity.this, new String[]{Manifest.permission.CAMERA}, 4);
                            return;
                        }
                        picOptions.dismiss();
                        captureImage();
                    }
                });
                gal = (TextView) picOptions.findViewById(R.id.opt_gallery);
                gal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        picOptions.dismiss();
                        Intent photoPicker = new Intent(
                                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        photoPicker.setType("image/*");
                        startActivityForResult(photoPicker, RESULT_LOAD_IMAGE);
                    }
                });

                break;
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type) {
        //return Uri.fromFile(getOutputMediaFile(type));
        return FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


    private void previewCapturedImage() {
        try {
            // hide video preview
            //videoPreview.setVisibility(View.GONE);

            photo.setVisibility(View.VISIBLE);
            photo.setImageURI(fileUri);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            photo.setImageURI(selectedImage);
            //cursor.close();
        } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            }
        }
    }

}

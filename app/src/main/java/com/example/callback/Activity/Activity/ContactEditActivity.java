package com.example.callback.Activity.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.constraint.ConstraintLayout;
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
import com.example.callback.BuildConfig;
import com.example.callback.R;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class ContactEditActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 2;
    private static final String IMAGE_DIRECTORY_NAME = "HelloCamera";
    EditText name, per_ph, off_ph, home_ph, addr, email, gender;
    RadioButton bm, bf;
    RadioGroup rbg;
    ImageView back, edit, photo;
    String temp = "", gen;
    ContactModel model, m;
    DbContacts db;
    Bundle t;
    Uri selectedImage = null;
    String prevPath = null;
    PopupWindow pw;
    TextView cam, gal;
    ConstraintLayout cl;

    String picturePath;
    private Uri fileUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_edit);

        db = DbContacts.getsInstance(this);
        t = getIntent().getExtras();

        name = (EditText) findViewById(R.id.edit_name);
        per_ph = (EditText) findViewById(R.id.edit_per);
        off_ph = (EditText) findViewById(R.id.edit_office);
        home_ph = (EditText) findViewById(R.id.edit_home);
        addr = (EditText) findViewById(R.id.edit_addr);
        email = (EditText) findViewById(R.id.edit_email);
        gender = (EditText) findViewById(R.id.edit_gender);
        // bm = (RadioButton)findViewById(R.id.edit_male);
        //bf = (RadioButton)findViewById(R.id.edit_female);
        //rbg = (RadioGroup)findViewById(R.id.rbg_gender);
        photo = (ImageView) findViewById(R.id.edit_photo);
        cl = (ConstraintLayout) findViewById(R.id.photo_edit);

        int id = Integer.parseInt(t.getString("id"));
        m = db.getContact(id);

        prevPath = m.getPath();
        if (m.getPath() != null) photo.setImageURI(Uri.parse(m.getPath()));
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog picOptions = new Dialog(ContactEditActivity.this);
                picOptions.setContentView(R.layout.photo_popup);
                picOptions.setTitle("SELECT");
                picOptions.show();

                /*LayoutInflater inflator = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflator.inflate(R.layout.photo_popup, null);
                pw = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                //pw.setFocusable(true);
                //pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
                pw.showAtLocation(cl, Gravity.CENTER, 0, 0);*/

                cam = (TextView) picOptions.findViewById(R.id.opt_camera);
                cam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ActivityCompat.checkSelfPermission(ContactEditActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ContactEditActivity.this, new String[]{Manifest.permission.CAMERA}, 4);
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
            }
        });

        temp = t.getString("name");
        //name.setText(temp);
        name.setText(m.getName());

        temp = t.getString("per_ph");
        //per_ph.setText(temp);
        per_ph.setText(m.getPersonal_ph());

        temp = t.getString("office_ph");
        //off_ph.setText(temp);
        off_ph.setText(m.getOffice_ph());

        temp = t.getString("home_ph");
        //home_ph.setText(temp);
        home_ph.setText(m.getHome_ph());

        temp = t.getString("email");
        //email.setText(temp);
        email.setText(m.getEmail());

        temp = t.getString("address");
        //addr.setText(temp);
        addr.setText(m.getAddr());

        temp = t.getString("gender");
        //gender.setText(temp);
        gender.setText(m.getGender());
       /* switch (temp){
            case "Male": bm.setChecked(true);bf.setChecked(false);
                break;
            case "Female": bf.setChecked(true);bm.setChecked(false);
                break;
            case "": bm.setChecked(false);bm.setChecked(false);
                break;
        }*/

       /* rbg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton d = (RadioButton)findViewById(checkedId);
                gen = d.getText().toString();
                Log.e("gender",gen);
            }
        });*/

        model = new ContactModel();

        back = (ImageView) findViewById(R.id.edit_back);
        edit = (ImageView) findViewById(R.id.iv_edit);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    model.setId(Integer.parseInt(t.getString("id")));
                    model.setName(name.getText().toString());
                    model.setPersonal_ph(per_ph.getText().toString());
                    if (!TextUtils.isEmpty(off_ph.getText().toString()) && off_ph.getText().toString().length() == 8 && off_ph.getText().toString().charAt(0) != '0') {
                        off_ph.setText("033" + off_ph.getText().toString());
                    }
                    model.setOffice_ph(off_ph.getText().toString());
                    if (!TextUtils.isEmpty(home_ph.getText().toString()) && home_ph.getText().toString().length() == 8 && home_ph.getText().toString().charAt(0) != '0') {
                        home_ph.setText("033" + home_ph.getText().toString());
                    }
                    model.setHome_ph(home_ph.getText().toString());
                    model.setEmail(email.getText().toString());
                    model.setAddr(addr.getText().toString());
                    model.setGender(gender.getText().toString());
                    if (selectedImage != null && fileUri == null)
                        model.setPath(selectedImage.toString());
                    else if (selectedImage == null && fileUri !=null)
                        model.setPath(fileUri.toString());
                    else
                        model.setPath(prevPath);
                    db.update(model);
                    finish();
                } else {
                    Toast.makeText(ContactEditActivity.this, "INVALID ENTRY FIELDS", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean validate() {
        if (!isName(name.getText().toString())) {
            name.setError("Invalid Name");
            return false;
        }
        if (!isPhoneNumber(per_ph.getText().toString())) {
            per_ph.setError("Invalid Number");
            return false;
        }
        if (!TextUtils.isEmpty(off_ph.getText().toString())) {
            if (!isPhoneNumber(off_ph.getText().toString())) {
                off_ph.setError("Invalid Number");
                return false;
            }
        }
        if (!TextUtils.isEmpty(home_ph.getText().toString())) {
            if (!isPhoneNumber(home_ph.getText().toString())) {
                home_ph.setError("Invalid Number");
                return false;
            }
        }
        if (!TextUtils.isEmpty(email.getText().toString())) {
            if (!isEmail(email.getText().toString())) {
                email.setError("Invalid Email Address");
                return false;
            }
        }
        if (TextUtils.isEmpty(gender.getText().toString())) {
            Toast.makeText(this, "Gender Not Set", Toast.LENGTH_LONG).show();
            return false;
        }
        /*if(gender.getText().toString()!="Male" && gender.getText().toString()!="Female") {
            gender.setError("Use Given Options");
            return false;
        }*/
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
        if (!s.equals("")) {
            s = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
        }
        //return s instanceof String;
        String name = "^[a-zA-Z]{1,}";
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            //ImageView imageView = (ImageView) findViewById(R.id.img_contact);
//            photo.setImageURI(Uri.parse(picturePath));
            photo.setImageURI(selectedImage);
        }else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            }
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

}

package com.example.callback.Activity.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.callback.Activity.ContactModel.ContactModel;
import com.example.callback.Activity.DbHandler.DbContacts;
import com.example.callback.R;
import com.squareup.picasso.Picasso;

public class ContactDetails extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 1;
    TextView name, per_ph, office_ph, home_ph, email, addr, gender;
    ImageView call1, call2, call3, sms1, sms2, sms3, del, back, edit;
    ImageView app_sms, device_sms, contactPhoto;
    ConstraintLayout cl;
    String temp;
    int id, flag = 0;
    Toolbar detail;
    ContactModel model;
    DbContacts db;
    PopupWindow pw;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        db = DbContacts.getsInstance(this);

        detail = (Toolbar) findViewById(R.id.include);
        del = (ImageView) findViewById(R.id.detail_del);
        back = (ImageView) findViewById(R.id.detail_back);
        edit = (ImageView) findViewById(R.id.detail_edit);
        contactPhoto = (ImageView) findViewById(R.id.img_contact);
        contactPhoto.setVisibility(View.VISIBLE);
        cl = (ConstraintLayout)findViewById(R.id.cl_details);

        back.setOnClickListener(this);
       // contactPhoto.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        final Bundle details = new Bundle();
        temp = bundle.getString("id");
        details.putString("id", temp);
        Log.e("deleted id", temp);
        id = Integer.parseInt(temp);

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int t = db.delete((Integer) id);
                finish();
            }
        });

        model = db.getContact(id);

        if(model.getPath() != null) {
            contactPhoto.setImageURI(Uri.parse(model.getPath()));

        }

       /* temp = bundle.getString("name");
        details.putString("name", temp);*/
        name = (TextView) findViewById(R.id.write_name);
        name.setText(model.getName());
        details.putString("name", model.getName());

        /*temp = bundle.getString("per_ph");
        details.putString("per_ph", temp);*/
        per_ph = (TextView) findViewById(R.id.write_per);
        per_ph.setText(model.getPersonal_ph());
        details.putString("per_ph", model.getPersonal_ph());

        /*temp = bundle.getString("office_ph");
        details.putString("office_ph", temp);*/
        office_ph = (TextView) findViewById(R.id.write_office);
        office_ph.setText(model.getOffice_ph());
        details.putString("office_ph", model.getOffice_ph());

        /*temp = bundle.getString("home_ph");
        details.putString("home_ph", temp);*/
        home_ph = (TextView) findViewById(R.id.write_home);
        home_ph.setText(model.getHome_ph());
        details.putString("home_ph", model.getHome_ph());

        /*temp = bundle.getString("email");
        details.putString("email", temp);*/
        email = (TextView) findViewById(R.id.email);
        email.setText(model.getEmail());
        details.putString("email", model.getEmail());

       /* temp = bundle.getString("address");
        details.putString("address", temp);*/
        addr = (TextView) findViewById(R.id.write_addr);
        addr.setText(model.getAddr());
        details.putString("address", model.getAddr());

       /* temp = bundle.getString("gender");
        details.putString("gender", temp);*/
        gender = (TextView) findViewById(R.id.write_gender);
        gender.setText(model.getGender());
        //Log.e("temph",gender.getText().toString());
        details.putString("gender", model.getGender());

        call1 = (ImageView) findViewById(R.id.per_ph);
        call1.setOnClickListener(this);
        sms1 = (ImageView) findViewById(R.id.per_sms);
        sms1.setOnClickListener(this);

        call2 = (ImageView) findViewById(R.id.off_call);
        call2.setOnClickListener(this);
        sms2 = (ImageView) findViewById(R.id.off_sms);
        sms2.setOnClickListener(this);

        call3 = (ImageView) findViewById(R.id.home_call);
        call3.setOnClickListener(this);
        sms3 = (ImageView) findViewById(R.id.home_sms);
        sms3.setOnClickListener(this);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                Intent i = new Intent(ContactDetails.this, ContactEditActivity.class);
                i.putExtras(details);
                ContactDetails.this.startActivity(i);
               // ContactDetails.this.finish();
            }
        });

        //finish();

    }

    private void updateDetails(int id) {
        ContactModel model = db.getContact(id);
        //name = (TextView)findViewById(R.id.write_name);
        if(model.getPath() != null)
            contactPhoto.setImageURI(Uri.parse(model.getPath()));
        name.setText(model.getName());
        per_ph.setText(model.getPersonal_ph());
        office_ph.setText(model.getOffice_ph());
        home_ph.setText(model.getHome_ph());
        email.setText(model.getEmail());
        addr.setText(model.getAddr());
        gender.setText(model.getGender());
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.per_ph:
                ActivityCompat.requestPermissions(ContactDetails.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                TextView tv1 = (TextView) findViewById(R.id.write_per);
                i = new Intent(Intent.ACTION_CALL);
                Toast.makeText(this, "Calling...", Toast.LENGTH_SHORT).show();
                i.setData(Uri.parse("tel:" + String.valueOf(tv1.getText().toString().trim())));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(i);
                break;

            case R.id.per_sms:

                TextView tv4 = (TextView)findViewById(R.id.write_per);
                sendSMS(this, tv4.getText().toString());
                /*i = new Intent(Intent.ACTION_SENDTO);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.setType("vnd.android-dir/mms-sms");
                i.setData(Uri.parse("sms:" + tv4.getText().toString()));*/

                /*SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(et.getText().toString().trim(),null,et_sms.getText().toString(),null,null);
                Toast.makeText(getApplicationContext(),"Message Sent",Toast.LENGTH_LONG).show();*/

                //startActivity(i);

                break;

            case R.id.off_call:
                ActivityCompat.requestPermissions(ContactDetails.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                TextView tv2 = (TextView)findViewById(R.id.write_office);
                i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:"+String.valueOf(tv2.getText().toString().trim())));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(i);
                break;

            case R.id.off_sms:
                TextView office = (TextView)findViewById(R.id.write_office);
                sendSMS(this, office.getText().toString());
                break;

            case R.id.home_call:
                ActivityCompat.requestPermissions(ContactDetails.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                TextView tv3 = (TextView)findViewById(R.id.write_home);
                i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:"+String.valueOf(tv3.getText().toString().trim())));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(i);
                break;

            case R.id.home_sms:
                TextView home = (TextView)findViewById(R.id.write_home);
                sendSMS(this, home.getText().toString());
                break;

            case R.id.detail_back:finish();
                break;

            /*case R.id.img_contact:
                Intent photoPicker = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPicker.setType("image*//*");
                startActivityForResult(photoPicker, RESULT_LOAD_IMAGE);
                break;*/
        }
    }

    private void sendSMS(final Context c, final String number) {

        final Dialog opt = new Dialog(this);
        opt.setTitle("SELECT OPTION");
        opt.setContentView(R.layout.popup_sms);
        opt.show();

        /*LayoutInflater inflator = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layout = (ViewGroup)inflator.inflate(R.layout.popup_sms, null);
        pw = new PopupWindow(layout, Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT, true);
        //pw.setFocusable(true);
        //pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
        pw.showAtLocation(cl, Gravity.CENTER, 0, 0);*/
        app_sms = (ImageView)opt.findViewById(R.id.img_def);
        app_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pw.dismiss();
                opt.dismiss();
                Intent i = new Intent(c, SMSActivity.class);
                Bundle b = new Bundle();
                b.putString("number", number);
                i.putExtras(b);
                startActivity(i);
            }
        });
        device_sms = (ImageView)opt.findViewById(R.id.img_device);
        device_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pw.dismiss();
                opt.dismiss();
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.setType("vnd.android-dir/mms-sms");
                i.setData(Uri.parse("sms:" + number));
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDetails(id);
    }
}

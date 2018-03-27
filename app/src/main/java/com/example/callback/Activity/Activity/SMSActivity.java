package com.example.callback.Activity.Activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.callback.R;

public class SMSActivity extends AppCompatActivity {

    EditText cname,sms;
    String temp,num;
    Button send,cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        ActivityCompat.requestPermissions(SMSActivity.this, new String[]{Manifest.permission.SEND_SMS}, 2);
        //ActivityCompat.requestPermissions(SMSActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);

        Bundle c = getIntent().getExtras();
        temp = c.getString("number");

        cname = (EditText)findViewById(R.id.et_cname);
        cname.setText(temp);
        num = cname.getText().toString();

        sms = (EditText)findViewById(R.id.sms);
        send = (Button)findViewById(R.id.btn_send);
        cancel = (Button)findViewById(R.id.btn_cancel);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String SENT = "SMS_SENT";
//                PendingIntent i = PendingIntent.getBroadcast(getApplicationContext(), 0,new Intent(SENT), 0);
//                ActivityCompat.requestPermissions(SMSActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(cname.getText().toString().trim(),null,sms.getText().toString(),null,null);
                Toast.makeText(getApplicationContext(),"Message Sent",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}

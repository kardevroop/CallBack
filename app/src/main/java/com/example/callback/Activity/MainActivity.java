package com.example.callback.Activity;

import android.Manifest;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.callback.Activity.Activity.ContactAddActivity;
import com.example.callback.Activity.Activity.SMSActivity;
import com.example.callback.Activity.Adapter.ContactsAdapter;
import com.example.callback.Activity.ContactModel.ContactModel;
import com.example.callback.Activity.DbHandler.DbContacts;
import com.example.callback.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    RecyclerView rv;
    //public static DbContacts db;
    DbContacts db;
    public static ArrayList<ContactModel> contacts;
    public static ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        rv = (RecyclerView) findViewById(R.id.rv_contacts);
        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(this);
        contacts = new ArrayList<>();
        // db = new DbContacts(this);
        db = DbContacts.getsInstance(this);
        contacts = db.getAllContacts();
        Log.e("value of i:","21");
        adapter = new ContactsAdapter(this, contacts);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.addItemDecoration(new DividerItemDecoration(MainActivity.this,
                DividerItemDecoration.VERTICAL));
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menus, menu);
        MenuItem menuItem = menu.findItem(R.id.search_list);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search_list));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    //adapter.filter("");
                    //listView.clearTextFilter();
                    StringBuffer buff = new StringBuffer(newText);
                    buff.setCharAt(0, Character.toUpperCase(newText.charAt(0)));
                    newText = buff.toString();
                    ArrayList<ContactModel> arrayList = adapter.filter(contacts, newText);
                    adapter.notifyAdapter(arrayList);
                    rv.scrollToPosition(0);
                }else {
                    adapter.notifyAdapter(contacts);
                }
                return false;
            }
        });
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add:
                Intent intent = new Intent(MainActivity.this, ContactAddActivity.class);
                startActivity(intent);
                //Toast.makeText(this, "HELLO", Toast.LENGTH_LONG).show();
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 3);
        //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 4);
    }

    @Override
    protected void onResume() {
        super.onResume();
        contacts.clear();
        contacts = db.getAllContacts();

        adapter.notifyAdapter(contacts);
    }
}

package com.example.callback.Activity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.callback.Activity.Activity.ContactDetails;
import com.example.callback.Activity.CircleTransform.CircleTransform;
import com.example.callback.Activity.ContactModel.ContactModel;
import com.example.callback.Activity.MainActivity;
import com.example.callback.Activity.ViewHolder.SingleContactView;
import com.example.callback.R;

import java.util.ArrayList;

/**
 * Created by Devroop Kar on 18-08-2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<SingleContactView> {
    Context c;
    ArrayList<ContactModel> contacts;

    public ContactsAdapter(Context context, ArrayList<ContactModel> contacts) {
        this.c = context;
        this.contacts = contacts;
    }

    @Override
    public SingleContactView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, null);
        return new SingleContactView(view);
    }

    @Override
    public void onBindViewHolder(SingleContactView holder, final int position) {
        holder.name.setText(contacts.get(position).getName());
        holder.phone.setText(contacts.get(position).getPersonal_ph());
        if(contacts.get(position).getPath() != null)
            Glide.with(c)
                .load(Uri.parse(contacts.get(position).getPath()))
                .error(R.mipmap.ic_photo)
                .transform(new CircleTransform(c))
                .into(holder.contactPic);
        else
            Glide.with(c)
                    .load(R.drawable.mat)
                    .error(R.mipmap.ic_photo)
                    .transform(new CircleTransform(c))
                    .into(holder.contactPic);
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c ,ContactDetails.class);
                Bundle details = new Bundle();
                details.putString("id", Integer.toString(contacts.get(position).getId()));
               /* details.putString("name", contacts.get(position).getName());
                details.putString("per_ph", contacts.get(position).getPersonal_ph());
                details.putString("office_ph", contacts.get(position).getOffice_ph());
                details.putString("home_ph", contacts.get(position).getHome_ph());
                details.putString("address", contacts.get(position).getAddr());
                details.putString("email", contacts.get(position).getEmail());
                details.putString("gender", contacts.get(position).getGender());*/
                i.putExtras(details);
                c.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }


    public void notifyAdapter( ArrayList<ContactModel> contacts){
        this.contacts=contacts;
        notifyDataSetChanged();
    }

    public ArrayList<ContactModel> filter(ArrayList<ContactModel> contacts, String newText) {
        ArrayList<ContactModel> arr = new ArrayList<>();
        ContactModel model;
        for(ContactModel x : contacts){
            if(x.getName().contains(newText)){
                model = new ContactModel();
                model.setId(x.getId());
                model.setName(x.getName());
                model.setPersonal_ph(x.getPersonal_ph());
                model.setOffice_ph(x.getOffice_ph());
                model.setHome_ph(x.getHome_ph());
                model.setGender(x.getGender());
                model.setAddr(x.getAddr());
                model.setEmail(x.getEmail());
                arr.add(model);
            }
        }
        return arr;
    }
}

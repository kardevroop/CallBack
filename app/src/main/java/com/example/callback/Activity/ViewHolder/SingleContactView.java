package com.example.callback.Activity.ViewHolder;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.callback.R;

/**
 * Created by Devroop Kar on 18-08-2017.
 */

public class SingleContactView extends RecyclerView.ViewHolder {

    public TextView name,phone;
    public ConstraintLayout cl;
    public LinearLayout ll,ll2,llbase;
    public ImageView contactPic;

    public SingleContactView(View itemView) {
        super(itemView);

        name = (TextView)itemView.findViewById(R.id.tv_name);
        phone = (TextView)itemView.findViewById(R.id.tv_number);
        cl = (ConstraintLayout) itemView.findViewById(R.id.cl_contact);
        ll = (LinearLayout)itemView.findViewById(R.id.ll_contact);
        contactPic = (ImageView) itemView.findViewById(R.id.img_pic);

        /*name = (TextView)itemView.findViewById(R.id.tv_cname);
        phone = (TextView)itemView.findViewById(R.id.tv_cphone);
        ll = (LinearLayout)itemView.findViewById(R.id.ll_base);*/

    }
}

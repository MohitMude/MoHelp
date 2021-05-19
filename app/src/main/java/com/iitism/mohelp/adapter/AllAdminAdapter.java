package com.iitism.mohelp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iitism.mohelp.R;
import com.iitism.mohelp.model.AllAdminModel;
import com.iitism.mohelp.student.QuerySendingActivity;

import java.util.List;

public class AllAdminAdapter extends RecyclerView.Adapter<AllAdminAdapter.ViewHolder> {


    Context context;
    private List<AllAdminModel> adminlist;
    private static int currentPosition = -1;


    public AllAdminAdapter(Context context, List<AllAdminModel> adminlist) {
        this.context = context;
        this.adminlist = adminlist;
    }


    @Override
   public void onBindViewHolder(@NonNull AllAdminAdapter.ViewHolder holder, int position) {

        String name,email,phone,title;
        holder.linearLayout.setTag(position);
        AllAdminModel model =adminlist.get(position);
        name="Name: " + model.getName();
        title="Title: " + model.getTitle();
        email="Email: " + model.getEmail();
        phone="Phone: " + model.getPhone();

        holder.txt_name.setText(name);
        holder.txt_phone.setText(phone);
        holder.txt_email.setText(email);
        holder.txt_title.setText(title);



    }
    @Override
    public int getItemCount() {
        return adminlist.size();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_all_admin,parent,false);
        currentPosition=-1;
        return  new ViewHolder(view);
    }


    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_name,txt_title,txt_email,txt_phone;
        LinearLayout linearLayout;
        View line;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_name=itemView.findViewById(R.id.admin_card_name);
            txt_title=itemView.findViewById(R.id.admin_card_title);
            txt_email=itemView.findViewById(R.id.admin_card_email);
            txt_phone=itemView.findViewById(R.id.admin_card_phone);
            linearLayout=itemView.findViewById(R.id.card_layout);
            line=itemView.findViewById(R.id.line_view);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //open new activity
                    Intent i=new Intent(context, QuerySendingActivity.class);
                    int position=(int)v.getTag();
                    i.putExtra("uid",adminlist.get(position).getAdminUId());
                    i.putExtra("name",adminlist.get(position).getName());
                    i.putExtra("title",adminlist.get(position).getTitle());
                    context.startActivity(i);
                }
            });

        }
    }

}

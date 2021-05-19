package com.iitism.mohelp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.iitism.mohelp.R;
import com.iitism.mohelp.admin.ReplyActivity;
import com.iitism.mohelp.model.PendingModel;
import com.iitism.mohelp.student.QuerySendingActivity;

public class PendingAdapter extends FirebaseRecyclerAdapter<PendingModel,PendingAdapter.ViewHolder> {

    Context context;

    public PendingAdapter( Context context,@NonNull FirebaseRecyclerOptions<PendingModel> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull PendingModel model) {
        holder.linearLayout.setTag(position);

        String name,admNo,branch,hostel,email,phone,query,reply,link;
        String studentUid;
        name="Name: " + model.getName();
        admNo="Admission No: " + model.getAdmission_No();
        branch="Branch: " + model.getBranch();
        hostel="Hostel: " + model.getHostel();
        email="Email: " + model.getEmail();
        phone="Phone: " + model.getPhone();
        query="Query: " + model.getQuery();
        link=model.getLink();
        reply=model.getReply();
        studentUid=model.getUid();
        Log.println(Log.ASSERT,"recycler view",link);
        if(reply.equals(" "))
        {
           holder.txt_name.setText(name);
           holder.txt_adm_no.setText(admNo);
           holder.txt_branch.setText(branch);
           holder.txt_hostel.setText(hostel);
           holder.txt_email.setText(email);
           holder.txt_phone.setText(phone);
           holder.txt_query.setText(query);
           holder.txt_link.setText(link);

           if(link.equals(" "))
           {
               holder.txt_link.setVisibility(View.GONE);
               holder.linearLayout.removeView(holder.txt_link);
           }

        }
        else
        {
            holder.hideLayout();
        }


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, ReplyActivity.class);
                i.putExtra("uid",studentUid);
                i.putExtra("name",model.getName());
                i.putExtra("admission_no",model.getAdmission_No());
                i.putExtra("branch",model.getBranch());
                i.putExtra("hostel",model.getHostel());
                i.putExtra("email",model.getEmail());
                i.putExtra("phone",model.getPhone());
                i.putExtra("query",model.getQuery());
                i.putExtra("link",model.getLink());
                context.startActivity(i);
                notifyDataSetChanged();
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_pending,parent,false);
        return  new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name,txt_adm_no,txt_branch,txt_hostel,txt_email,txt_phone;
        TextView txt_query,txt_link;
        LinearLayout linearLayout;

        final LinearLayout.LayoutParams param;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name=itemView.findViewById(R.id.pending_name);
            txt_adm_no=itemView.findViewById(R.id.pending_card_adm_no);
            txt_branch=itemView.findViewById(R.id.pending_card_branch);
            txt_hostel=itemView.findViewById(R.id.pending_card_hostel);
            txt_email=itemView.findViewById(R.id.pending_card_email);
            txt_phone=itemView.findViewById(R.id.pending_card_phone);
            txt_query=itemView.findViewById(R.id.pending_card_query);
            txt_link=itemView.findViewById(R.id.pending_card_link);
            linearLayout=itemView.findViewById(R.id.card_layout_admin);

            param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);



        }
            private void hideLayout ()
            {
                param.height = 0;
                itemView.setLayoutParams(param);
            }


        }


}

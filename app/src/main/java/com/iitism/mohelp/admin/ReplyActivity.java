package com.iitism.mohelp.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iitism.mohelp.R;

import java.util.Objects;

public class ReplyActivity extends AppCompatActivity implements View.OnClickListener{

    String name,email,branch,hostel,admNo,phone,stuUid,adminUid,query,link,reply;
    TextView txt_name,txt_adm_no,txt_branch,txt_hostel,txt_email,txt_phone;
    TextView txt_query,txt_link;
    TextInputEditText edt_txt_reply;
    Button btn_send;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference studentReference,adminReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        txt_name=findViewById(R.id.reply_name);
        txt_adm_no=findViewById(R.id.reply_adm_no);
        txt_branch=findViewById(R.id.reply_branch);
        txt_hostel=findViewById(R.id.reply_hostel);
        txt_email=findViewById(R.id.reply_email);
        txt_phone=findViewById(R.id.reply_phone);
        txt_query=findViewById(R.id.reply_query);
        txt_link=findViewById(R.id.reply_link);
        edt_txt_reply=findViewById(R.id.edt_txt_reply);
        btn_send=findViewById(R.id.btn_send_reply);

        Bundle b=getIntent().getExtras();
        if(b!=null)
        {
            stuUid= (String) b.get("uid");
            name="Name: " + (String)b.get("name");
            admNo="Admission No: " + (String)b.get("admission_no");
            branch="Branch: " + (String)b.get("branch");
            hostel="Hostel: " + (String)b.get("hostel");
            email="Email: " + (String)b.get("email");
            phone="Phone No: " + (String)b.get("phone");
            query="Query: " + (String)b.get("query");
            link=(String)b.get("link");

            txt_name.setText(name);
            txt_adm_no.setText(admNo);
            txt_branch.setText(branch);
            txt_hostel.setText(hostel);
            txt_email.setText(email);
            txt_phone.setText(phone);
            txt_query.setText(query);
            txt_link.setText(link);
            if(link.equals(" "))
                txt_link.setVisibility(View.GONE);
        }




        firebaseAuth=FirebaseAuth.getInstance();
        adminUid= Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        studentReference=firebaseDatabase.getReference("student").child(stuUid).child("admins").child(adminUid);
        adminReference=firebaseDatabase.getReference("admin").child(adminUid).child("students").child(stuUid);

        btn_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==btn_send)
        {
            reply=edt_txt_reply.getText().toString().trim();

            if(TextUtils.isEmpty(reply))
            {
                edt_txt_reply.setError("please enter your reply");
                return ;
            }

            adminReference.child("Reply").setValue(reply);
            studentReference.child("Reply").setValue(reply);

            Toast.makeText(getApplicationContext(),"Sent.",Toast.LENGTH_SHORT).show();
            finish();

        }
    }
}

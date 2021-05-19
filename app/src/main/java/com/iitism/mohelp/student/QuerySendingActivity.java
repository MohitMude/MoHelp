package com.iitism.mohelp.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iitism.mohelp.R;

import java.util.Objects;

public class QuerySendingActivity extends AppCompatActivity implements View.OnClickListener {
    String name,title,link,text;
    String studentUId,adminUid;
    String stuName,stuAdmno,stuHostel,stuBranch,stuPhone,stuEmail;
    String adminName,adminTitle;

    TextView txt_name,txt_title;
    TextInputEditText edt_link,edt_text;
    Button btn_send;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference studentReference,adminReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_sending);

        txt_name=findViewById(R.id.txt_send_name);
        txt_title=findViewById(R.id.txt_send_title);
        edt_link=findViewById(R.id.edt_txt_link);
        edt_text=findViewById(R.id.edt_txt_text);
        btn_send=findViewById(R.id.btn_send_query);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        studentReference=firebaseDatabase.getReference("student");
        adminReference=firebaseDatabase.getReference("admin");

        studentUId= Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        studentReference.child(studentUId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.println(Log.ASSERT,"data","query sending activity");
                stuName=snapshot.child("Name").getValue(String.class);
                stuBranch=snapshot.child("Branch").getValue(String.class);
                stuAdmno=snapshot.child("Admission_No").getValue(String.class);
                stuHostel=snapshot.child("Hostel").getValue(String.class);
                stuPhone=snapshot.child("Phone").getValue(String.class);
                stuEmail=snapshot.child("Email").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Bundle b=getIntent().getExtras();
        if(b!=null)
        {
             adminUid= (String) b.get("uid");
             adminName=(String)b.get("name");
             adminTitle=(String)b.get("title");
        }
        String a,c;
        a="Name: " + adminName;
        c="Title: " + adminTitle;
        txt_name.setText(a);
        txt_title.setText(c);



        btn_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==btn_send)
        {
            link=edt_link.getText().toString().trim();
            text=edt_text.getText().toString().trim();

            if(TextUtils.isEmpty(text))
            {
                Toast.makeText(QuerySendingActivity.this,"Please enter something in query",Toast.LENGTH_SHORT).show();
                return ;
            }
            if(TextUtils.isEmpty(link))
            {
                link="";
            }

            adminReference.child(adminUid).child("students").child(studentUId).child("Query").setValue(text);
            studentReference.child(studentUId).child("admins").child(adminUid).child("Query").setValue(text);
            adminReference.child(adminUid).child("students").child(studentUId).child("Reply").setValue(" ");
            studentReference.child(studentUId).child("admins").child(adminUid).child("Reply").setValue(" ");
            studentReference.child(studentUId).child("admins").child(adminUid).child("Name").setValue(adminName);
            studentReference.child(studentUId).child("admins").child(adminUid).child("Title").setValue(adminTitle);
            studentReference.child(studentUId).child("admins").child(adminUid).child("Link").setValue(link);
            adminReference.child(adminUid).child("students").child(studentUId).child("Email").setValue(stuEmail);
            adminReference.child(adminUid).child("students").child(studentUId).child("Name").setValue(stuName);
            adminReference.child(adminUid).child("students").child(studentUId).child("Admission_No").setValue(stuAdmno);
            adminReference.child(adminUid).child("students").child(studentUId).child("Branch").setValue(stuBranch);
            adminReference.child(adminUid).child("students").child(studentUId).child("Hostel").setValue(stuHostel);
            adminReference.child(adminUid).child("students").child(studentUId).child("Phone").setValue(stuPhone);
            adminReference.child(adminUid).child("students").child(studentUId).child("Link").setValue(link);



            Toast.makeText(getApplicationContext(),"Uploaded!",Toast.LENGTH_SHORT).show();
            finish();



        }
    }
}

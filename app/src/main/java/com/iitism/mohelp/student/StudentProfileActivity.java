package com.iitism.mohelp.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.iitism.mohelp.R;
import com.squareup.picasso.Picasso;

public class StudentProfileActivity extends AppCompatActivity {
    String name,admNo,branch,hostel,email,phone,uid;
    TextView txt_name,txt_ad,txt_branch,txt_hostel,txt_email,txt_phone;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        txt_name=findViewById(R.id.txt_profile_name);
        txt_ad=findViewById(R.id.txt_profile_adm_no);
        txt_email=findViewById(R.id.txt_profile_email);
        txt_phone=findViewById(R.id.txt_profile_phone);
        txt_branch=findViewById(R.id.txt_profile_branch);
        txt_hostel=findViewById(R.id.txt_profile_hostel);


        firebaseAuth=FirebaseAuth.getInstance();
        uid=firebaseAuth.getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("student").child(uid);

        dataloader();
    }

    public void dataloader()
    {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //String name,email,phone,gender,id;
                name="Name: "+ snapshot.child("Name").getValue(String.class);
                email="Email: "+ snapshot.child("Email").getValue(String.class);
                phone="Ph: "+ snapshot.child("Phone").getValue(String.class);
                admNo="Admission No: " + snapshot.child("Admission_No").getValue(String.class);
                branch="Branch: " + snapshot.child("Branch").getValue(String.class);
                hostel="Hostel: " + snapshot.child("Hostel").getValue(String.class);


                txt_name.setText(name);
                txt_email.setText(email);
                txt_phone.setText(phone);
                txt_ad.setText(admNo);
                txt_branch.setText(branch);
                txt_hostel.setText(hostel);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(),error.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });



    }
}

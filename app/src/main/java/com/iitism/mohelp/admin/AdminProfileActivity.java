package com.iitism.mohelp.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iitism.mohelp.R;

public class AdminProfileActivity extends AppCompatActivity {

    String name,title,email,phone,uid;
    TextView txt_name,txt_title,txt_email,txt_phone;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        txt_name=findViewById(R.id.txt_profile_name);
        txt_email=findViewById(R.id.txt_profile_email);
        txt_title=findViewById(R.id.txt_profile_title);
        txt_phone=findViewById(R.id.txt_profile_phone);


        firebaseAuth=FirebaseAuth.getInstance();
        uid=firebaseAuth.getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("admin").child(uid);

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
                title="Title: " + snapshot.child("Title").getValue(String.class);


                txt_name.setText(name);
                txt_email.setText(email);
                txt_phone.setText(phone);
                txt_title.setText(title);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(),error.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });



    }
}

package com.iitism.mohelp.student;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.iitism.mohelp.LoginActivity;
import com.iitism.mohelp.R;
import com.iitism.mohelp.adapter.AllAdminAdapter;
import com.iitism.mohelp.model.AllAdminModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudentHomeActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    LinearLayout progressLayout;
    EditText searchBox;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<AllAdminModel> list;
    List<AllAdminModel> searchList;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchBox=findViewById(R.id.search_view);
        recyclerView=findViewById(R.id.all_admin_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressLayout=findViewById(R.id.progress_layout);



        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference().child("admin");


        loadData();

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });


    }

    public void loadData()
    {
        Log.println(Log.ASSERT,"data","data loading..");
        list=new ArrayList<>();
        list.clear();
        progressLayout.setVisibility(View.VISIBLE);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.println(Log.ASSERT,"data", Objects.requireNonNull(snapshot.getKey()));
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    uid=ds.getKey();
                    String name,email,phone,title;
                    Uri[] imageUri = new Uri[1];

                    name=ds.child("Name").getValue(String.class);
                    email=ds.child("Email").getValue(String.class);
                    phone=ds.child("Phone").getValue(String.class);
                    title=ds.child("Title").getValue(String.class);



                    AllAdminModel allAdminModel= new AllAdminModel(name,title,email,phone,uid);
                    list.add(allAdminModel);

                    Log.println(Log.ASSERT,"data",name+email+phone);
                }
                adapter = new AllAdminAdapter(StudentHomeActivity.this, list);
                recyclerView.setAdapter(adapter);

                progressLayout.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                progressLayout.setVisibility(View.GONE);
            }
        });


//        if(list.size()==0)
//        {
//            Toast.makeText(getApplicationContext(),"No doctors available",Toast.LENGTH_LONG).show();
//        }

    }

    private void filter(final String s) {
        int size=list.size();
        searchList=new ArrayList<>();
        if(!searchList.isEmpty())
            searchList.clear();

        for(int i=0;i<size;i++)
        {
            AllAdminModel model=list.get(i);

            if(model.getName().toLowerCase().contains(s.toLowerCase()) ||
                    model.getEmail().toLowerCase().contains(s.toLowerCase()) ||
                    model.getPhone().toLowerCase().contains(s.toLowerCase())
                  || model.getTitle().toLowerCase().contains(s.toLowerCase()))
            {
                searchList.add(model);

            }


        }
        adapter = new AllAdminAdapter(StudentHomeActivity.this, searchList);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.menu_main_profile) {
            Intent i=new Intent(getApplicationContext(),StudentProfileActivity.class);
            startActivity(i);
        }
        else if(id==R.id.menu_main_all_query)
        {
             Toast.makeText(getApplicationContext(),"to be added",Toast.LENGTH_LONG).show();
//            Intent i=new Intent(getApplicationContext(),MyDoctorActivity.class);
//            startActivity(i);
        }
        else if(id==R.id.menu_main_logout)
        {
            firebaseAuth.signOut();
            SharedPreferences sharedPreferences=getSharedPreferences("Login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1=sharedPreferences.edit();
            editor1.putString("Student Login","");
            editor1.apply();

            Intent i=new Intent(getApplicationContext(), LoginActivity.class);
            finish();
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}

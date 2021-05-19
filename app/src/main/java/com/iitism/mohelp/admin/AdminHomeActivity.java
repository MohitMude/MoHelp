package com.iitism.mohelp.admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.iitism.mohelp.LoginActivity;
import com.iitism.mohelp.R;
import com.iitism.mohelp.adapter.PendingAdapter;
import com.iitism.mohelp.model.PendingModel;
import com.iitism.mohelp.student.StudentProfileActivity;

import java.util.Objects;

public class AdminHomeActivity extends AppCompatActivity {

    String uid;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    LinearLayout progressLayout;
    EditText searchBox;
    RecyclerView recyclerView;
    PendingAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        searchBox=findViewById(R.id.search_view);
        recyclerView=findViewById(R.id.pending_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressLayout=findViewById(R.id.progress_layout);



        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        uid=firebaseAuth.getCurrentUser().getUid();
        reference=firebaseDatabase.getReference().child("admin").child(uid).child("students");

        loadData();


    }

    public void loadData()
    {



        FirebaseRecyclerOptions<PendingModel> options=new FirebaseRecyclerOptions.Builder<PendingModel>()
                .setQuery(reference, new SnapshotParser<PendingModel>() {
                    @NonNull
                    @Override
                    public PendingModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                       PendingModel pendingModel=snapshot.getValue(PendingModel.class);

                        assert pendingModel!= null;
                        pendingModel.setUid(snapshot.getKey());
                        // Toast.makeText(getApplicationContext(), snapshot.getKey(),Toast.LENGTH_SHORT).show();
                        Log.println(Log.ASSERT,"recycler" , Objects.requireNonNull(snapshot.getKey()));
                        progressLayout.setVisibility(View.GONE);
                        return pendingModel;
                    }
                }).build();

        adapter=new PendingAdapter(AdminHomeActivity.this,options);


        recyclerView.setAdapter(adapter);

        //progressLayout.setVisibility(View.GONE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //progressLayout.setVisibility(View.VISIBLE);
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.menu_main_profile) {
            Intent i=new Intent(getApplicationContext(),AdminProfileActivity.class);
            startActivity(i);
        }
        else if(id==R.id.menu_main_all_student)
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
            editor1.putString("Admin Login","");
            editor1.apply();

            Intent i=new Intent(getApplicationContext(), LoginActivity.class);
            finish();
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}

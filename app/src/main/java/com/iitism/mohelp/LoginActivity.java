package com.iitism.mohelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iitism.mohelp.admin.AdminHomeActivity;
import com.iitism.mohelp.student.StudentHomeActivity;
import com.iitism.mohelp.student.StudentProfileActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    TextInputEditText edt_email,edt_password;
    Button btn_login;
    TextView signUpStudent,SignUpAdmin;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference studentReference,adminReference;

    String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_email=findViewById(R.id.edt_txt_login_email);
        edt_password=findViewById(R.id.edt_txt_login_password);
        signUpStudent=findViewById(R.id.txt_sign_up_student);
        SignUpAdmin=findViewById(R.id.txt_sign_up_admin);
        btn_login=findViewById(R.id.btn_login);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        studentReference=firebaseDatabase.getReference("student");
        adminReference=firebaseDatabase.getReference("admin");

        btn_login.setOnClickListener(this);
        signUpStudent.setOnClickListener(this);
        SignUpAdmin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view==btn_login)
        {
            email=edt_email.getText().toString().trim();
            password=edt_password.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                edt_email.setError("Enter email");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                edt_password.setError("Enter password");
                return;
            }

            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                             if(task.isSuccessful())
                             {
                                 checkIfEmailVerified();
                             }
                             else
                             {
                                 Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).toString(),Toast.LENGTH_SHORT).show();
                             }
                        }
                    });

        }
        else if(view==signUpStudent)
        {
            Intent i=new Intent(getApplicationContext(),SignUpStudentActivity.class);
            startActivity(i);

        }
        else if(view==SignUpAdmin)
        {
            Intent i=new Intent(getApplicationContext(),SignUpAdminActivity.class);
            startActivity(i);

        }
    }

    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            //shared pref work

           Login();




//            SharedPreferences sharedPreferences1 = getSharedPreferences("Login", Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
//                                editor1.putString("Student Login", "true");
//                                editor1.putString("Email", email);
//                                editor1.apply();
//
//            Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
//            Intent i=new Intent(getApplicationContext(),StudentHomeActivity.class);
//            startActivity(i);
//            finish();

        }
        else
        {
            AlertDialog diaBox = DialogBuild(user);
            diaBox.show();
            diaBox.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
            diaBox.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
            FirebaseAuth.getInstance().signOut();


        }
    }

    private AlertDialog DialogBuild(FirebaseUser user)
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(LoginActivity.this)
                // set message, title, and icon
                .setTitle("Email not verified")
                .setMessage("Verify to continue")
                .setIcon(R.drawable.ic_mail)

                .setPositiveButton("Send", (dialog, whichButton) -> {
                   sendVerificationEmail(user);
                })
                .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                .create();

        return myQuittingDialogBox;
    }

    private void sendVerificationEmail(FirebaseUser user)
    {


        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // email sent

                        Toast.makeText(getApplicationContext(), "Verification mail sent to: "+user.getEmail(), Toast.LENGTH_SHORT).show();

                        // after email is sent just logout the user and finish this activity
                        FirebaseAuth.getInstance().signOut();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Verification mail could not be sent.Try later..", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void Login()
    {

        Log.println(Log.ASSERT,"login","success");
        adminReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                Log.println(Log.ASSERT,"login","database admin check");
                for(DataSnapshot ds:datasnapshot.getChildren())
                {
                    if(email.equals(ds.child("Email").getValue(String.class)))
                    {
                        //more to add
                        SharedPreferences sharedPreferences1 = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.putString("Admin Login", "true");
                        editor1.apply();

                        Intent i = new Intent(getApplicationContext(), AdminHomeActivity.class);
                        finish();
                        startActivity(i);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                      Toast.makeText(getApplicationContext(),error.getMessage().trim(),Toast.LENGTH_SHORT).show();
            }
        });


        studentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                Log.println(Log.ASSERT,"login","database student check");
                for(DataSnapshot ds:datasnapshot.getChildren())
                {
                    // Log.println(Log.ASSERT,"login",ds.child(""));
                    if(email.equals(ds.child("Email").getValue(String.class)))
                    {
                        Log.println(Log.ASSERT,"login","student found");
                        //more to add
                        //String id=ds.child("IDCount").getValue(String.class);
                        SharedPreferences sharedPreferences1 = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.putString("Student Login", "true");
//                                            assert id != null;
//                                            editor1.putInt("ID",Integer.parseInt(id));
                        editor1.apply();

                        Intent i = new Intent(getApplicationContext(), StudentHomeActivity.class);
                        finish();
                        startActivity(i);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage().trim(),Toast.LENGTH_SHORT).show();
            }
        });

    }


}

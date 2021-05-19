package com.iitism.mohelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpStudentActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText edt_name,edt_adm_no,edt_branch,edt_hostel,edt_phone,edt_email,edt_password;
    Button btn_sign;
    String name,admNo,branch,hostel,phone,email,password;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseAuth.AuthStateListener mAuthListener;

    long a=6000000000L,b=9999999999L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_student);

        edt_name = findViewById(R.id.edt_txt_sign_up_stu_name);
        edt_adm_no = findViewById(R.id.edt_txt_sign_up_stu_adm_no);
        edt_branch = findViewById(R.id.edt_txt_sign_up_stu_branch);
        edt_hostel = findViewById(R.id.edt_txt_sign_up_stu_hostel);
        edt_phone = findViewById(R.id.edt_txt_sign_up_stu_phone);
        edt_email = findViewById(R.id.edt_txt_sign_up_stu_email);
        edt_password = findViewById(R.id.edt_txt_sign_up_stu_password);
        btn_sign = findViewById(R.id.btn_sign_up);

        btn_sign.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("student");


    }

        @Override
        public void onClick (View view){
            if (view == btn_sign) {
                name = edt_name.getText().toString().trim();
                admNo = edt_adm_no.getText().toString().trim();
                branch = edt_branch.getText().toString().trim();
                hostel = edt_hostel.getText().toString().trim();
                phone = edt_phone.getText().toString().trim();
                email = edt_email.getText().toString().trim();
                password = edt_password.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    edt_name.setError("Enter Name");
                    return;
                }
                if (TextUtils.isEmpty(admNo)) {
                    edt_adm_no.setError("Enter admission no");
                    return;
                }
                if (TextUtils.isEmpty(branch)) {
                    edt_branch.setError("Enter branch");
                    return;
                }
                if (TextUtils.isEmpty(hostel)) {
                    edt_hostel.setError("Enter hostel");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    edt_phone.setError("Enter phone no.");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    edt_email.setError("Enter email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    edt_password.setError("Enter password");
                    return;
                }
                long p = Long.parseLong(phone);

                if (p < a || p > b) {
                    edt_phone.setError("Enter Correct Phone");
                    return;
                }

                if (!isFromCollegeDomain(email)) {
                    edt_email.setError("Enter email of iitism domain");
                    return;
                }


                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {

                            if (task.isSuccessful()) {

                                //database work
                                String id=firebaseAuth.getCurrentUser().getUid();
                                databaseReference.child(id).child("Email").setValue(email);
                                databaseReference.child(id).child("Name").setValue(name);
                                databaseReference.child(id).child("Branch").setValue(branch);
                                databaseReference.child(id).child("Hostel").setValue(hostel);
                                databaseReference.child(id).child("Phone").setValue(phone);
                                databaseReference.child(id).child("Admission_No").setValue(admNo);

                                Log.println(Log.ASSERT,"sign","sign in");
//                                SharedPreferences sharedPreferences1 = getSharedPreferences("Login", Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
//                                editor1.putString("Student Login", "true");
//                                editor1.putString("Email", email);
//                                editor1.apply();

                                sendVerificationEmail();
//                                Intent i = new Intent(getApplicationContext(), VerificationActivity.class);
//                                startActivity(i);
//                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                            }


                        });


            }
        }


    private boolean isFromCollegeDomain(String email)
    {
        String domain="iitism.ac.in";
        return email.contains(domain);
    }

    private void sendVerificationEmail()
        {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // email sent

                            Toast.makeText(getApplicationContext(), "Verification mail sent to: "+user.getEmail(), Toast.LENGTH_SHORT).show();

                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(SignUpStudentActivity.this, LoginActivity.class));
                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    });
        }
}


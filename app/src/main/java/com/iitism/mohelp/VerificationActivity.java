package com.iitism.mohelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerificationActivity extends AppCompatActivity {
    String email,status;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    TextView txt_email,txt_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        SharedPreferences sharedPreferences=getSharedPreferences("Login", Context.MODE_PRIVATE);
        email=sharedPreferences.getString("Email","");






    }

    void send()
    {
        if(!firebaseUser.isEmailVerified()) {
            firebaseUser.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Email sent to:" + firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                            } else {
                                Log.println(Log.ASSERT, "sendEmailVerification", String.valueOf(task.getException()));
                            }
                        }
                    });
        }

    }

}



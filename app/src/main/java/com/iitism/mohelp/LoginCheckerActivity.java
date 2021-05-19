package com.iitism.mohelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.iitism.mohelp.admin.AdminHomeActivity;
import com.iitism.mohelp.student.StudentHomeActivity;

public class LoginCheckerActivity extends AppCompatActivity {
    String Studentloginvalue,Adminloginvalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences=getSharedPreferences("Login", Context.MODE_PRIVATE);
        Studentloginvalue=sharedPreferences.getString("Student Login","");
        Adminloginvalue=sharedPreferences.getString("Admin Login","");

        if(Studentloginvalue.equals("true"))
        {
            Intent i=new Intent(getApplicationContext(), StudentHomeActivity.class);
            startActivity(i);
            finish();

        }
        else if(Adminloginvalue.equals("true"))
        {
            Intent i=new Intent(getApplicationContext(), AdminHomeActivity.class);
            startActivity(i);
            finish();

        }
        else
        {
            Intent i=new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();


        }
    }
}

package com.example.fitapp_v11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Forgot_password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
    }
    public void open_sign_in(View view) {

        Intent int_menu=new Intent(this, MainActivity.class);
        Toast.makeText(this, "If email matches our records, an email to reset password will be sent to that email", Toast.LENGTH_LONG).show();
        startActivity(int_menu);
    }
}

package com.example.fitapp_v11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openregister(View view) {
        Intent in_reg=new Intent(this,Register.class);
        startActivity(in_reg);
    }

    public void open_main_menu(View view) {

        Intent int_menu=new Intent(this, MainMenu.class);
        startActivity(int_menu);
    }

    public void open_forgot_pass(View view) {
        Intent int_pass=new Intent(this, Forgot_password.class);
        startActivity(int_pass);
    }
}
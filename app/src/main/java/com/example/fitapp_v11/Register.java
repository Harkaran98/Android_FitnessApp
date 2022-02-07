package com.example.fitapp_v11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void open_main_menu(View view) {

        Intent int_menu=new Intent(this, MainMenu.class);
        startActivity(int_menu);
    }
}
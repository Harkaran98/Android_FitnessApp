package com.example.fitapp_v11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void open_main_menu(View view) {

        Intent int_menu=new Intent(this, MainMenu.class);
        startActivity(int_menu);
    }

    public void save_toast(View view) {
        Toast.makeText(this, "Data is saved", Toast.LENGTH_SHORT).show();
    }
}
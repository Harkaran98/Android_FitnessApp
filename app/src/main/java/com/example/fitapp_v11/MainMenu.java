package com.example.fitapp_v11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void open_profile(View view) {
        Intent int_profile=new Intent(this, Profile.class);
        startActivity(int_profile);
    }

    public void open_wt_tracker(View view) {
        Intent int_profile=new Intent(this, weight_tracker.class);
        startActivity(int_profile);
    }

    public void open_exercise_tracker(View view) {
        Intent int_profile=new Intent(this, exercise_tracker.class);
        startActivity(int_profile);
    }
}
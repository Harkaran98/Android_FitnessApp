package com.example.fitapp_v11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;

public class Profile extends AppCompatActivity {
    EditText weight,ht_foot,ht_inches,age;
    Spinner gender,activity_lvl,goal;
    double Bmr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        weight= (EditText) findViewById(R.id.wt_id);
        ht_foot=(EditText) findViewById(R.id.ht_foot);
        ht_inches=(EditText) findViewById(R.id.ht_inches);
        age=(EditText) findViewById(R.id.age_id);
        gender=(Spinner) findViewById(R.id.gen_id);
        activity_lvl = (Spinner) findViewById(R.id.activity_id);
        goal=(Spinner) findViewById(R.id.goal_id);
    }

    public void open_main_menu(View view) {

        Intent int_menu=new Intent(this, MainMenu.class);
        startActivity(int_menu);
    }

    public void save_toast(View view) {
        //Toast.makeText(this, "Data is saved", Toast.LENGTH_SHORT).show();
        double ht_cm= (Integer.parseInt(ht_foot.getText().toString()) * 12 + Integer.parseInt(ht_inches.getText().toString()))*2.54;
        int gender_factor=-1;
        if(gender.getSelectedItem().toString().equals("Male"))
            gender_factor=5;
        else
            gender_factor=-151;

//        Sedentary = 1.2
//        Lightly active = 1.375
//        Moderately active = 1.550
//        Very active = 1.725
//        Extra active = 1.9

        Bmr= 10*(Integer.parseInt(weight.getText().toString())*0.453) + (6.25 * (ht_cm))- (5* Integer.parseInt(age.getText().toString()))+gender_factor;
        HashMap<String, Double> activity_ref=new HashMap<>();
        activity_ref.put("Sedentary",1.2);
        activity_ref.put("Lightly Active",1.375);
        activity_ref.put("Moderately Active",1.550);
        activity_ref.put("Very Active",1.725);
        activity_ref.put("Extra Active",1.9);

        double cal = (double) (Bmr * activity_ref.get(activity_lvl.getSelectedItem().toString()));
        String adjuster=goal.getSelectedItem().toString();

            if(adjuster.equals("Weight Loss"))
                cal= (int) (cal - 0.2 * cal);
            else if (adjuster.equals("Weight Gain"))
                    cal+=500;



        Toast.makeText(this, String.valueOf(cal)+" calories", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, nutrition_tracker.class);
        Bundle bundle = new Bundle();
        bundle.putString("Calories",String.valueOf(cal));
        i.putExtras(bundle);
        startActivity(i);

    }
}
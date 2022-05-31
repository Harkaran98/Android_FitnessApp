package com.example.fitapp_v11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class Profile extends AppCompatActivity {
    EditText weight,ht_foot,ht_inches,age;
    TextView prev;
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
        prev = (TextView) findViewById(R.id.pcal_id);

        prev.setText("0000");

        //retrieve
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getValue()!=null){
                    Toast.makeText(Profile.this, snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                    prev.setText(snapshot.getValue().toString());}

//                for (DataSnapshot snap : snapshot.getChildren()) {
//
//
//                    Toast.makeText(Profile.this, "Prev cal val is- " + snap.getValue().toString(), Toast.LENGTH_SHORT).show();
//
//                }

                //Toast.makeText(Profile.this, "Data End!!!", Toast.LENGTH_SHORT).show();

//                arrayList.add("just added");
//                arrayList.add("again!!!");
//                Toast.makeText(weight_tracker.this,arrayList.get(0)+" values" , Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "error!!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void open_main_menu(View view) {

        Intent int_menu=new Intent(this, MainMenu.class);
        startActivity(int_menu);
    }

    public void save_toast(View view) {
        if (weight.getText().toString().equals("") || ht_foot.getText().toString().equals("") || ht_inches.getText().toString().equals("") || age.getText().toString().equals("")
                || gender.getSelectedItem().toString().equals("") || activity_lvl.getSelectedItem().toString().equals("") || goal.getSelectedItem().toString().equals(""))
            Toast.makeText(Profile.this, "Fill in all the data to proceed!", Toast.LENGTH_SHORT).show();
        else {
            //Toast.makeText(this, "Data is saved", Toast.LENGTH_SHORT).show();
            double ht_cm = (Integer.parseInt(ht_foot.getText().toString()) * 12 + Integer.parseInt(ht_inches.getText().toString())) * 2.54;
            int gender_factor = -1;
            if (gender.getSelectedItem().toString().equals("Male"))
                gender_factor = 5;
            else
                gender_factor = -151;

//        Sedentary = 1.2
//        Lightly active = 1.375
//        Moderately active = 1.550
//        Very active = 1.725
//        Extra active = 1.9

            Bmr = 10 * (Integer.parseInt(weight.getText().toString()) * 0.453) + (6.25 * (ht_cm)) - (5 * Integer.parseInt(age.getText().toString())) + gender_factor;
            HashMap<String, Double> activity_ref = new HashMap<>();
            activity_ref.put("Sedentary", 1.2);
            activity_ref.put("Lightly Active", 1.375);
            activity_ref.put("Moderately Active", 1.550);
            activity_ref.put("Very Active", 1.725);
            activity_ref.put("Extra Active", 1.9);

            double cal = (double) (Bmr * activity_ref.get(activity_lvl.getSelectedItem().toString()));
            String adjuster = goal.getSelectedItem().toString();

            if (adjuster.equals("Weight Loss"))
                cal = (int) (cal - 0.2 * cal);
            else if (adjuster.equals("Weight Gain"))
                cal += 500;


            Toast.makeText(this, String.valueOf(cal) + " calories", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, nutrition_tracker.class);
            Bundle bundle = new Bundle();
            bundle.putString("Calories", String.valueOf(cal));
            i.putExtras(bundle);
            startActivity(i);

            //put in database of firestore
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {

                FirebaseDatabase.getInstance().getReference().child("Profile").child(user.getUid()).setValue(cal);

//                        FirebaseDatabase.getInstance().getReference().child("exercise_tracker").child(user.getUid()).child(date_txt.getText().toString().replace('/','-'))
//                                .child(body_part).child(ex_name.getText().toString()).setValue(arrayList);
                //FirebaseDatabase.getInstance().getReference().child("weight_tracker").child(user.getUid()).setValue("Weight: " + editTxt.getText().toString() + " lbs      Date: " + editText.getText().toString());

                // Toast.makeText(weight_tracker.this, user.getUid(), Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(Profile.this, "error!!!", Toast.LENGTH_SHORT).show();
            }


        }
    }
}
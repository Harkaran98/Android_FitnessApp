package com.example.fitapp_v11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class nutrition_tracker extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();
    private Button btn, delete;
    private EditText date_txt,carbs,pro,fats,food,calories;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    int delete_idx=-1;
    int rem_cal;
   TextView cal_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_tracker);
        carbs= (EditText)findViewById(R.id.carbs_id);
        fats= (EditText)findViewById(R.id.fats_id);
        pro = (EditText)findViewById(R.id.pro_id);
        food = (EditText)findViewById(R.id.food_name);
        calories = (EditText)findViewById(R.id.calories_id);

        date_txt =(EditText) findViewById(R.id.date_id);
        btn = (Button) findViewById(R.id.add_btn);
        list = (ListView) findViewById(R.id.lview_id);
        arrayList = new ArrayList<String>();
        delete= (Button) findViewById(R.id.id_del);


        Bundle bundle = getIntent().getExtras();
        cal_txt=(TextView) findViewById(R.id.cal_id);
        if( bundle!=null){
        String cal= bundle.getString("Calories");
        cal=cal.substring(0,cal.indexOf('.'));
        cal_txt.setText(cal);}








        //addition from exercise tracker
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);

        // Here, you set the data in your ListView
        list.setAdapter(adapter);
        //gets items num (i+1) (1-indexed)
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(nutrition_tracker.this, "Selected "+String.valueOf(i+1)+" Item", Toast.LENGTH_SHORT).show();
                delete_idx=i;
            }
        });



        //add
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (date_txt.getText().toString().equals("") || calories.getText().toString().equals("") || food.getText().toString().equals(""))
                    Toast.makeText(nutrition_tracker.this, "Fill in date, calories and food to proceed!", Toast.LENGTH_SHORT).show();
                else {

                    if(delete_idx==-1){
                        arrayList.add(calories.getText().toString() + "|"+ food.getText().toString()+"|" + date_txt.getText().toString()+carbs.getText().toString()+fats.getText().toString()
                        +pro.getText().toString());
                        rem_cal-=Integer.parseInt(calories.getText().toString());
                    }
                    else{
                        arrayList.add(delete_idx,calories.getText().toString() + "|"+ food.getText().toString()+"|" + date_txt.getText().toString()+carbs.getText().toString()+fats.getText().toString()
                        +pro.getText().toString());

                        rem_cal-=Integer.parseInt(calories.getText().toString());
                        rem_cal+=Integer.parseInt(arrayList.get(delete_idx+1).substring(0,arrayList.get(delete_idx+1).indexOf('|')));
                        arrayList.remove(delete_idx+1);
                        rem_cal-=Integer.parseInt(arrayList.get(delete_idx+1));
                        delete_idx=-1;
                    }


                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {

                        FirebaseDatabase.getInstance().getReference().child("nutrition_tracker").child(user.getUid()).setValue(arrayList);
                        FirebaseDatabase.getInstance().getReference().child("nutrition_tracker_cal").child(user.getUid()).child(date_txt.getText().toString().replace('/','-')).setValue(rem_cal);
                        cal_txt.setText(String.valueOf(rem_cal));

                    }else {

                        Toast.makeText(nutrition_tracker.this, "error!!!", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });



        //delete
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    if(arrayList.size()==0){
                        Toast.makeText(nutrition_tracker.this, "Nothing to delete!", Toast.LENGTH_SHORT).show();
                    }
                    else if(delete_idx==-1) {
                        Toast.makeText(nutrition_tracker.this, "Select the item and try again", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //deletes full
                        FirebaseDatabase.getInstance().getReference().child("nutrition_tracker").child(user.getUid()).removeValue();
                        //now modify arraylist to remove particular data item and add the arraylist to firebase back again
                        rem_cal+=Integer.parseInt(arrayList.get(delete_idx).substring(0,arrayList.get(delete_idx).indexOf('|')));
                        arrayList.remove(delete_idx);
                        //reset
                        delete_idx=-1;
                        //arrayList.remove(Integer.parseInt(del_idx.getText().toString()) - 1);
                        FirebaseDatabase.getInstance().getReference().child("nutrition_tracker").child(user.getUid()).setValue(arrayList);
                        FirebaseDatabase.getInstance().getReference().child("nutrition_tracker_cal").child(user.getUid()).child(date_txt.getText().toString().replace('/','-')).setValue(rem_cal);
                        cal_txt.setText(String.valueOf(rem_cal));
                    }


                    //FirebaseDatabase.getInstance().getReference().child("weight_tracker").child(user.getUid()).setValue("Weight: " + editTxt.getText().toString() + " lbs      Date: " + editText.getText().toString());

                    // Toast.makeText(weight_tracker.this, user.getUid(), Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(nutrition_tracker.this, "Data doesn't exist!!", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }
        });



        //retrieve
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("nutrition_tracker").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();

                for(DataSnapshot snap : snapshot.getChildren()){
                    arrayList.add(snap.getValue().toString());
                }


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(nutrition_tracker.this, "error!!!", Toast.LENGTH_SHORT).show();
            }
        });

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        date_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dp=new DatePickerDialog(nutrition_tracker.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
                dp.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                dp.show();

                Toast.makeText(nutrition_tracker.this, "Dateee changed!!!", Toast.LENGTH_SHORT).show();

            }
        });

        if(arrayList.isEmpty()){

            //retrieve
            DatabaseReference ref_cal = FirebaseDatabase.getInstance().getReference().child("Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            ref_cal.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.getValue()!=null){
                        Toast.makeText(nutrition_tracker.this, snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                        String cal=snapshot.getValue().toString().substring(0,snapshot.getValue().toString().indexOf('.'));
                        cal_txt.setText(cal);
                        rem_cal= Integer.parseInt(cal); //since rem cal are same as initial cal if no food
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(nutrition_tracker.this, "error!!!", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else{
            //Here write the code for retrieving the remaining calories
            DatabaseReference ref_cal = FirebaseDatabase.getInstance().getReference().child("nutrition_tracker").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(date_txt.getText().toString());

            ref_cal.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.getValue()!=null){
                        Toast.makeText(nutrition_tracker.this, snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                        String cal=snapshot.getValue().toString().substring(0,snapshot.getValue().toString().indexOf('.'));
                        cal_txt.setText(cal); //set rem calories
                        rem_cal= Integer.parseInt(cal);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(nutrition_tracker.this, "error!!!", Toast.LENGTH_SHORT).show();
                }
            });



        }

    }

    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        date_txt.setText(dateFormat.format(myCalendar.getTime()));
    }




    }

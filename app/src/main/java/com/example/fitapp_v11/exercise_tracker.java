package com.example.fitapp_v11;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class exercise_tracker extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();
    private EditText reps, sets, date_txt, ex_name;
    private Button btn;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private Spinner ex_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_tracker);

        date_txt =(EditText) findViewById(R.id.txt_date);
        reps=(EditText) findViewById(R.id.reps_id);
        sets=(EditText) findViewById(R.id.sets_id);
        btn = (Button) findViewById(R.id.btn_ex);
        list = (ListView) findViewById(R.id.list_ex);
        arrayList = new ArrayList<String>();
        ex_name= (EditText) findViewById(R.id.name_ex);
        ex_type= (Spinner) findViewById(R.id.type_ex);

        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
        // and the array that contains the data
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);

        // Here, you set the data in your ListView
        list.setAdapter(adapter);






        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (date_txt.getText().toString().equals("") || reps.getText().toString().equals("") || sets.getText().toString().equals(""))
                    Toast.makeText(exercise_tracker.this, "Fill in all the information to enter!", Toast.LENGTH_SHORT).show();
                else {

                    arrayList.add(ex_type.getSelectedItem().toString() + "   "+ ex_name.getText().toString()+"  Reps: " + reps.getText().toString() + "  Sets: " + sets.getText().toString()
                            +"   " + date_txt.getText().toString());


                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        FirebaseDatabase.getInstance().getReference().child("weight_tracker").child(user.getUid()).setValue(arrayList);
                        //FirebaseDatabase.getInstance().getReference().child("weight_tracker").child(user.getUid()).setValue("Weight: " + editTxt.getText().toString() + " lbs      Date: " + editText.getText().toString());

                        // Toast.makeText(weight_tracker.this, user.getUid(), Toast.LENGTH_SHORT).show();
                    }else {

                        Toast.makeText(exercise_tracker.this, "error!!!", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                }
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
                DatePickerDialog dp=new DatePickerDialog(exercise_tracker.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
                dp.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                dp.show();

            }
        });





    }

    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        date_txt.setText(dateFormat.format(myCalendar.getTime()));
    }

    public void add_exercise(View view) {





    }
}
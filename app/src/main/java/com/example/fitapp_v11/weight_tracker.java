package com.example.fitapp_v11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.Objects;

public class weight_tracker extends AppCompatActivity {

    private EditText editTxt,editText;
    private Button btn;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    final Calendar myCalendar= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_tracker);

        editTxt = (EditText) findViewById(R.id.wt_entry);
        btn = (Button) findViewById(R.id.button);
        list = (ListView) findViewById(R.id.list_id);
        editText=(EditText) findViewById(R.id.date_txt);
        arrayList = new ArrayList<String>();

        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
        // and the array that contains the data
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);

        // Here, you set the data in your ListView
        list.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editText.getText().toString().equals("") || editTxt.getText().toString().equals(""))
                    Toast.makeText(weight_tracker.this, "Fill in both Weight and Date to add!", Toast.LENGTH_SHORT).show();
                else {

                    arrayList.add("Weight: " + editTxt.getText().toString() + " lbs      Date: " + editText.getText().toString());


                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        FirebaseDatabase.getInstance().getReference().child("weight_tracker").child(user.getUid()).setValue(arrayList);
                        //FirebaseDatabase.getInstance().getReference().child("weight_tracker").child(user.getUid()).setValue("Weight: " + editTxt.getText().toString() + " lbs      Date: " + editText.getText().toString());

                        // Toast.makeText(weight_tracker.this, user.getUid(), Toast.LENGTH_SHORT).show();
                    }else {

                        Toast.makeText(weight_tracker.this, "error!!!", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });


        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("weight_tracker").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();
                //Toast.makeText(weight_tracker.this, "Data Ret!!!", Toast.LENGTH_SHORT).show();
                for(DataSnapshot snap : snapshot.getChildren()){

                    arrayList.add(snap.getValue().toString());
                }
//                arrayList.add("just added");
//                arrayList.add("again!!!");
//                Toast.makeText(weight_tracker.this,arrayList.get(0)+" values" , Toast.LENGTH_SHORT).show();

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(weight_tracker.this, "error!!!", Toast.LENGTH_SHORT).show();
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
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(weight_tracker.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



    }
    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(dateFormat.format(myCalendar.getTime()));
    }

}






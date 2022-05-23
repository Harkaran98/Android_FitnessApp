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
import android.widget.Spinner;
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

public class exercise_tracker extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();
    private EditText reps, sets, date_txt, ex_name, other, del_idx;
    private Button btn, delete;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private Spinner ex_type;
    String body_part="";
    int delete_idx=-1;

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
        other = (EditText) findViewById(R.id.other_id);
        delete= (Button) findViewById(R.id.del);


//        del_idx = (EditText) findViewById(R.id.delete_idx);

        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
        // and the array that contains the data
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);

        // Here, you set the data in your ListView
        list.setAdapter(adapter);
        //gets items num (i+1) (1-indexed)
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(exercise_tracker.this, "Selected "+String.valueOf(i+1)+" Item", Toast.LENGTH_SHORT).show();
                delete_idx=i;
            }
        });



        //add
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (date_txt.getText().toString().equals("") || reps.getText().toString().equals("") || sets.getText().toString().equals(""))
                    Toast.makeText(exercise_tracker.this, "Fill in all the information to enter!", Toast.LENGTH_SHORT).show();
                else {

                    if(ex_type.getSelectedItem().toString().equals("Other"))
                        body_part= other.getText().toString();
                    else
                        body_part= ex_type.getSelectedItem().toString();
                    if(delete_idx==-1){
                    arrayList.add(body_part + "|"+ ex_name.getText().toString()+"|Reps:" + reps.getText().toString() + "|Sets:" + sets.getText().toString()
                            +"|" + date_txt.getText().toString());}
                    else{
                        arrayList.add(delete_idx,body_part + "|"+ ex_name.getText().toString()+"|Reps:" + reps.getText().toString() + "|Sets:" + sets.getText().toString()
                                +"|" + date_txt.getText().toString());
                        arrayList.remove(delete_idx+1);
                        delete_idx=-1;

                    }


                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {

                        FirebaseDatabase.getInstance().getReference().child("exercise_tracker").child(user.getUid()).setValue(arrayList);

//                        FirebaseDatabase.getInstance().getReference().child("exercise_tracker").child(user.getUid()).child(date_txt.getText().toString().replace('/','-'))
//                                .child(body_part).child(ex_name.getText().toString()).setValue(arrayList);
                        //FirebaseDatabase.getInstance().getReference().child("weight_tracker").child(user.getUid()).setValue("Weight: " + editTxt.getText().toString() + " lbs      Date: " + editText.getText().toString());

                        // Toast.makeText(weight_tracker.this, user.getUid(), Toast.LENGTH_SHORT).show();
                    }else {

                        Toast.makeText(exercise_tracker.this, "error!!!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(exercise_tracker.this, "Nothing to delete!", Toast.LENGTH_SHORT).show();
                    }
                    else if(delete_idx==-1) {
                        Toast.makeText(exercise_tracker.this, "Select the item and try again", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //deletes full
                        FirebaseDatabase.getInstance().getReference().child("exercise_tracker").child(user.getUid()).removeValue();
                        //now modify arraylist to remove particular data item and add the arraylist to firebase back again
                        arrayList.remove(delete_idx);
                        //reset
                        delete_idx=-1;
                        //arrayList.remove(Integer.parseInt(del_idx.getText().toString()) - 1);
                        FirebaseDatabase.getInstance().getReference().child("exercise_tracker").child(user.getUid()).setValue(arrayList);
                    }


                    //FirebaseDatabase.getInstance().getReference().child("weight_tracker").child(user.getUid()).setValue("Weight: " + editTxt.getText().toString() + " lbs      Date: " + editText.getText().toString());

                    // Toast.makeText(weight_tracker.this, user.getUid(), Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(exercise_tracker.this, "Data doesn't exist!!", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }
        });



        //retrieve
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("exercise_tracker").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                //Toast.makeText(weight_tracker.this, "Data Ret!!!", Toast.LENGTH_SHORT).show();
                int count=0;
                int deletion_item=1;
                for(DataSnapshot snap : snapshot.getChildren()){

//                    Toast.makeText(exercise_tracker.this, snap.getValue().toString().
//                            substring(snap.getValue().toString().indexOf('[')+1,snap.getValue().toString().lastIndexOf(']')), Toast.LENGTH_SHORT).show();

                    arrayList.add(snap.getValue().toString());
                    count++;
                }
//                arrayList.add("just added");
//                arrayList.add("again!!!");
//                Toast.makeText(weight_tracker.this,arrayList.get(0)+" values" , Toast.LENGTH_SHORT).show();

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(exercise_tracker.this, "error!!!", Toast.LENGTH_SHORT).show();
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





    public void update_entry(View view) {




    }
}
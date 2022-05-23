package com.example.fitapp_v11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class nutrition_tracker extends AppCompatActivity {

   TextView cal_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_tracker);

        Bundle bundle = getIntent().getExtras();
        cal_txt=(TextView) findViewById(R.id.cal_id);
        if( bundle!=null){
        String cal= bundle.getString("Calories");
        cal=cal.substring(0,cal.indexOf('.'));
        cal_txt.setText(cal);}

    }
}
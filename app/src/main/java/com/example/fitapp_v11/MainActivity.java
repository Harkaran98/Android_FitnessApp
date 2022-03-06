package com.example.fitapp_v11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
   // private EditText pass;
    private TextInputEditText pass;
    private EditText email;

    private Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        email=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        login_btn=findViewById(R.id.sign_in);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email=email.getText().toString();
                String txt_pass=pass.getText().toString();
                if(txt_email.equals("") || txt_pass.equals("")){
                    Toast.makeText(MainActivity.this, "Please fill both, username and password!", Toast.LENGTH_SHORT).show();
                }
                else
                    login_user(txt_email,txt_pass);
            }
        });


    }

    private void login_user(String txt_email, String txt_pass) {

    mAuth.signInWithEmailAndPassword(txt_email,txt_pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
        @Override
        public void onSuccess(AuthResult authResult) {
            open_main_menu();
        }

    });
    mAuth.signInWithEmailAndPassword(txt_email,txt_pass).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(MainActivity.this, "Incorrect credentials, please try again!", Toast.LENGTH_SHORT).show();
        }
    });

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        //updateUI(currentUser);
//    }

    public void openregister(View view) {
        Intent in_reg=new Intent(this,Register.class);
        startActivity(in_reg);
    }

    public void open_main_menu() {

        Intent int_menu=new Intent(this, MainMenu.class);
        startActivity(int_menu);
    }

    public void open_forgot_pass(View view) {
        Intent int_pass=new Intent(this, Forgot_password.class);
        startActivity(int_pass);
    }
}
package com.example.fitapp_v11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {



    private EditText email;
    private TextInputEditText pass;
    private Button reg_btn;
    private EditText email_confirm;
    private TextInputEditText pass_confirm;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email=findViewById(R.id.reg_email);
        email_confirm=findViewById(R.id.reg_email2);
        pass=findViewById(R.id.reg_pass);
        pass_confirm=findViewById(R.id.reg_pass2);
        reg_btn=findViewById(R.id.btn_reg);
        auth=FirebaseAuth.getInstance();

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email=email.getText().toString();
                String txt_pass=pass.getText().toString();
                String txt_email2=email_confirm.getText().toString();
                String txt_pass2=pass_confirm.getText().toString();
                if(txt_email.equals("") || txt_pass.equals("")){
                    Toast.makeText(Register.this, "Please fill both, username and password!", Toast.LENGTH_SHORT).show();
                }
                else if(!txt_email.equals(txt_email2)){
                    Toast.makeText(Register.this, "Email and Confirm Email are not the same!", Toast.LENGTH_SHORT).show();
                }
                else if(!txt_pass.equals(txt_pass2)){
                    Toast.makeText(Register.this, "Password and Confirm Password are not the same!", Toast.LENGTH_SHORT).show();

                } else if(txt_pass.length()<6){
                    Toast.makeText(Register.this, "Password should be atleast 6 characters long!", Toast.LENGTH_SHORT).show();

                }
                else{
                   // Toast.makeText(Register.this, txt_pass+" "+txt_email, Toast.LENGTH_SHORT).show();
                    register_user(txt_email,txt_pass);
                }

            }
        });
    }

    private void register_user(String txt_email, String txt_pass) {

        auth.createUserWithEmailAndPassword(txt_email,txt_pass).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Register.this, "Registration Successful!", Toast.LENGTH_LONG).show();
                    Intent int_sign=new Intent(Register.this,MainActivity.class);
                    startActivity(int_sign);
                }
                else{
                    Toast.makeText(Register.this, "Registration Failed! Incorrect email or check your internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void open_main_menu(View view) {

        Intent int_menu=new Intent(this, MainMenu.class);
        startActivity(int_menu);
    }
}
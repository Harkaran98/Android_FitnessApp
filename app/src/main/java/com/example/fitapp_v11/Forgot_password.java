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
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_password extends AppCompatActivity {

    private EditText email;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        email=findViewById(R.id.email_recover);
        btn=findViewById(R.id.btn_recover);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em=email.getText().toString();
                if(em.equals("")){
                    Toast.makeText(Forgot_password.this, "Empty email!", Toast.LENGTH_SHORT).show();
                }
                else{


                    FirebaseAuth.getInstance().sendPasswordResetEmail(em).addOnCompleteListener(Forgot_password.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Forgot_password.this, "Password Reset Sent to the email!", Toast.LENGTH_LONG).show();
                                Intent int_sign=new Intent(Forgot_password.this,MainActivity.class);
                                startActivity(int_sign);
                            }
                            else{
                                Toast.makeText(Forgot_password.this, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
    public void open_sign_in(View view) {

        Intent int_menu=new Intent(this, MainActivity.class);
        Toast.makeText(this, "If email matches our records, an email to reset password will be sent to that email", Toast.LENGTH_LONG).show();
        startActivity(int_menu);
    }
}

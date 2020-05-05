package com.example.flickrapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {

    Button signupBtn;
    EditText EDemail, EDpassword;
    FirebaseAuth mAuth;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signupBtn  = findViewById(R.id.signup_button);
        EDemail    =  findViewById(R.id.signup_email);
        EDpassword = findViewById(R.id.signup_password);
        pb         = findViewById(R.id.progressBarSignUp);

        mAuth = FirebaseAuth.getInstance();
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manualSignIn();
            }
        });

    }

    private void manualSignIn(){
        String eml = EDemail.getText().toString().trim();
        String psw = EDpassword.getText().toString().trim();


        if(TextUtils.isEmpty(eml)){
            EDemail.setError("Email is required");
            return;
        }
        if(TextUtils.isEmpty(psw)){
            EDpassword.setError("Password is required");
            return;
        }
        pb.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(eml,psw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Signup.this , "User Created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                }
                else{
                    Toast.makeText(Signup.this , "Error:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        pb.setVisibility(View.GONE);
    }
}

package com.example.umair.tourguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity {

    //declaring variables
    EditText LName, LEmail, LBarID, LPassword;
    Button btn_SignUp;
    TextView btn_redirect;
    ProgressBar pgBar;
    FirebaseAuth myFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        LName = findViewById(R.id.LName);
        LEmail = findViewById(R.id.LEmail);
        LPassword = findViewById(R.id.LPassword);
        btn_SignUp = findViewById(R.id.L_btn_signup);
        btn_redirect = findViewById(R.id.L_redirect_signin);
        pgBar = findViewById(R.id.progressBar);
        myFirebaseAuth = FirebaseAuth.getInstance();

        if (myFirebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = LEmail.getText().toString().trim();
                String Password = LPassword.getText().toString().trim();

                if (TextUtils.isEmpty(Email)){
                    LEmail.setError("Email Field cannot be Blank!");
                    return;
                }
                if (TextUtils.isEmpty(Password)){
                    LPassword.setError("Password Field cannot be Blank");
                    return;
                }
                if (Password.length() < 6){
                    LPassword.setError("Password must be 6 characters long");
                    return;
                }
                pgBar.setVisibility(View.VISIBLE);

                //Authenticating the user
                myFirebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(signup.this, "Successfully Registered!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else {
                            Toast.makeText(signup.this, "Error Occurred!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            pgBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}

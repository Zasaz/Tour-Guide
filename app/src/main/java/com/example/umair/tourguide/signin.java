package com.example.umair.tourguide;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signin extends AppCompatActivity {

    //declaring variables
    EditText  LEmail, LPassword;
    Button btn_SignUp;
    TextView btn_redirect;
    ProgressBar pgBar;
    FirebaseAuth myFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signin);

        LEmail = findViewById(R.id.editText6);
        LPassword = findViewById(R.id.editText4);
        btn_SignUp = (Button) findViewById(R.id.button10);
        btn_redirect = findViewById(R.id.textView13);
        pgBar = findViewById(R.id.progressBar2);
        myFirebaseAuth = FirebaseAuth.getInstance();

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

                //authenticating the user
                myFirebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(signin.this, "Successfully Registered!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(signin.this, "Error Occurred!" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            pgBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}

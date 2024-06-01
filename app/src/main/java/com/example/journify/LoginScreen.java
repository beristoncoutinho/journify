package com.example.journify;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import com.google.firebase.auth.FirebaseAuth;

public class LoginScreen extends AppCompatActivity {
    EditText email, password;
    Button login;
    TextView signin;
    ProgressBar progressbar;
    private static final String CHANNEL_ID = "my_channel_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        email = findViewById(R.id.emailText);
        password = findViewById(R.id.Password);
        signin = findViewById(R.id.Sign_text_btn);
        login = findViewById(R.id.Login_btn);
        login.setOnClickListener((v) -> loginuser());
        signin.setOnClickListener((v) ->singin());
        progressbar=findViewById(R.id.progress);
    }
    void singin(){

        Intent i =new Intent(LoginScreen.this, RegisterActivity.class);
        startActivity(i);
        finish();
    }
    private void loginuser() {
        String emailInput = email.getText().toString().trim();
        String passwordInput = password.getText().toString().trim();
        boolean isValidate = validate(emailInput, passwordInput);
        if (!isValidate) {
            return;
        }

        loginAccountInFirebase(emailInput, passwordInput);
    }

    void loginAccountInFirebase(String emailInput, String passwordInput) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(task -> {
            changeInProgress(false);

            if (task.isSuccessful()) {
                //main activity
//                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                    startActivity(new Intent(LoginScreen.this, MainActivity.class));
                    finish();

//                } else {
//                    Toast.makeText(LoginScreen.this, "Email not verified,please verify your email", Toast.LENGTH_SHORT).show();
//
//                }


            } else {
                Toast.makeText(LoginScreen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

        void changeInProgress ( boolean inProgress){
            if (inProgress) {
                progressbar.setVisibility(View.VISIBLE);
                login.setVisibility(View.GONE);
            } else {
                progressbar.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);

            }
        }
        boolean validate (String emailInput, String passwordInput){

            if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                email.setError("Email is Invalid");
                return false;
            }
            if (passwordInput.length() < 6) {
                password.setError("Password length is invalid");
                return false;
            }
            return true;
        }
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to quit?");
        builder.setPositiveButton("QUIT", (dialog, which) -> finish());
        builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}


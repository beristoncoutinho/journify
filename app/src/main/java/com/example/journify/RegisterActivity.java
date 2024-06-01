package com.example.journify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    EditText email, password, confirmPassword;
    Button signin;
    ProgressBar progressbar;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.emailText);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.Confirm);
        progressbar = findViewById(R.id.progressBar);
        login = findViewById(R.id.Login_text_btn);
        signin = findViewById(R.id.signin);
        signin.setOnClickListener(v -> createAccount());
        login.setOnClickListener(view -> {
            Intent iLogin =new Intent(RegisterActivity.this, LoginScreen.class);
            startActivity(iLogin);
            finish();


        });
    }

    void createAccount() {
        String emailInput = email.getText().toString().trim();
        String passwordInput = password.getText().toString().trim();
        String confirmPasswordI =  confirmPassword.getText().toString().trim();
        boolean isValidate=validate(emailInput,passwordInput,confirmPasswordI);
        if(!isValidate){
            return;
        }
        CreateAccountInFirebase(emailInput, passwordInput);
    }
    void CreateAccountInFirebase(String emailInput,String passwordInput){
        changeInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(RegisterActivity.this,
                task -> {
            changeInProgress(false);
                    if(task.isSuccessful()){

                        Toast.makeText(RegisterActivity.this, "Successfully Created Check Email to verify", Toast.LENGTH_SHORT).show();

                        firebaseAuth.signOut();
                        //if email verification is needed
//                        Objects.requireNonNull(firebaseAuth.getCurrentUser()).sendEmailVerification();


                        finish();
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                });


    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressbar.setVisibility(View.VISIBLE);
            signin.setVisibility(View.GONE);
        }
        else{
            progressbar.setVisibility(View.GONE);
            signin.setVisibility(View.VISIBLE);

        }
    }

    boolean validate(String emailInput, String passwordInput, String confirmPasswordI) {

        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Email is Invalid");
            return false;


        }
        if (passwordInput.length() < 6) {
            password.setError("Password length is invalid");
            return false;

        }
        if (!passwordInput.equals(confirmPasswordI)){
            confirmPassword.setError("Password not matched");
            return false;
        }

        return true;
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to quit?");
        builder.setPositiveButton("QUIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
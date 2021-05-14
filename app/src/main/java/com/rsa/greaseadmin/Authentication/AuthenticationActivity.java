package com.rsa.greaseadmin.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rsa.greaseadmin.R;

public class AuthenticationActivity extends AppCompatActivity {

    private TextInputEditText email, password;
    private Button submitButton;

    private FirebaseAuth mAuth;

    private String mEmail, mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        intiValues();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDetails();
            }
        });
    }

    private void validateDetails() {
        mEmail = email.getText().toString();
        mPassword = password.getText().toString();

        if (mEmail.isEmpty() || mPassword.isEmpty()) {
            Toast.makeText(this, "please fill the details", Toast.LENGTH_SHORT).show();
        } else {
            signInUser();
        }
    }

    private void signInUser() {
        mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(AuthenticationActivity.this, VerifyAdminActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(AuthenticationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void intiValues() {
        email = (TextInputEditText) findViewById(R.id.authentication_email);
        password = (TextInputEditText) findViewById(R.id.authentication_password);
        submitButton = (Button) findViewById(R.id.authentication_submit_btn);

        mAuth = FirebaseAuth.getInstance();
    }
}
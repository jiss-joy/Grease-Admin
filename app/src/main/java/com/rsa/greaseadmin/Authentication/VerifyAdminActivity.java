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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rsa.greaseadmin.MainActivity;
import com.rsa.greaseadmin.R;

public class VerifyAdminActivity extends AppCompatActivity {

    private TextInputEditText verificationPin;
    private Button submitButton;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private CollectionReference userRef;

    private String mVerificationPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_admin);

        intiValues();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData() {
        mVerificationPin = verificationPin.getText().toString();
        if (mVerificationPin.isEmpty()) {
            Toast.makeText(this, "Pin cannot be empty", Toast.LENGTH_SHORT).show();
        } else if (mVerificationPin.length() < 6 || mVerificationPin.length() > 6) {
            Toast.makeText(this, "6 characters required", Toast.LENGTH_SHORT).show();
        } else {
            authenticate();
        }
    }

    private void authenticate() {
        userRef.document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String pin = task.getResult().getString("adminPin");
                    if (mVerificationPin.equals(pin)) {
                        Toast.makeText(VerifyAdminActivity.this, "Verification Successful", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(VerifyAdminActivity.this, MainActivity.class));
                    } else {
                        mAuth.signOut();
                        Toast.makeText(VerifyAdminActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }

    private void intiValues() {
        verificationPin = (TextInputEditText) findViewById(R.id.verification_pin);
        submitButton = (Button) findViewById(R.id.verification_submit_btn);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userRef = db.collection("Users");
    }
}
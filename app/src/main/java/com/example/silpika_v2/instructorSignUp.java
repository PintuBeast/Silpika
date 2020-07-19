package com.example.silpika_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class instructorSignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText ed1, ed2;
    Button b1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_sign_up);
        

        mAuth = FirebaseAuth.getInstance();

        ed1 = (EditText) findViewById(R.id.tv_username);
        ed2 = (EditText) findViewById(R.id.tv_password);
        b1 = (Button) findViewById(R.id.btn_signup);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUpUser();
            }
        });

    }

    public void signUpUser() {

        String username=ed1.getText().toString();
        String password=ed2.getText().toString();

        if (username.isEmpty()) {
            ed1.setError("Please Enter Email");
            ed1.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            ed1.setError("Please Enter Valid Email");
            ed1.requestFocus();
            return;

        }

        if (password.isEmpty()) {
            ed2.setError("Please Enter Password");
            ed2.requestFocus();
            return;

        }

        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(instructorSignUp.this,"Authenticated!!",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });


                        } else {

                            Toast.makeText(instructorSignUp.this,"Authentication Failed!!", Toast.LENGTH_LONG).show();
                            updateUI(null);

                        }


                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            if (currentUser.isEmailVerified()) {
                //startActivity(Intent(this, DashboardActivity::class.java));
                instructorSignUp.this.finish();
            } else {
                Toast.makeText(instructorSignUp.this, "Please verify Email!!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(instructorSignUp.this, "Authentication Failed!!", Toast.LENGTH_LONG).show();
        }


    }


}

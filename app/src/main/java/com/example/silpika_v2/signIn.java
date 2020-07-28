package com.example.silpika_v2;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class signIn extends AppCompatActivity {

    private FirebaseAuth mAuth;


    EditText ed1, ed2;
    Button b1,b2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();

        ed1=(EditText)findViewById(R.id.tv_username);
        ed2=(EditText)findViewById(R.id.tv_password);
        b1=(Button)findViewById(R.id.btn_signIn);
        b2=(Button)findViewById(R.id.btn_signUp);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {

                                  @Override
                                  public void onClick(View v) {
                                      Intent i=new Intent(getApplicationContext(),signUp.class);
                                      startActivity(i);
                                  }
                              }
        );

    }
    public void Login() {

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

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {

                            Toast.makeText(signIn.this,"Login Failed!!",Toast.LENGTH_LONG).show();
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

    private void updateUI(final FirebaseUser currentUser) {

        if(currentUser!=null) {


            if(currentUser.isEmailVerified()) {
                currentUser.getIdToken(true);
                currentUser.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() { // 1
                    @Override
                    public void onSuccess(GetTokenResult result) {


                        if (result.getClaims().get("admin") != null) {
                            boolean isAdmin = (boolean) result.getClaims().get("admin"); // 2
                            if (isAdmin) { // 3
                                // Show moderator UI
                                showModeratorUI();
                            } else {
                                // Show regular user UI.
                                showRegularUI();
                            }
                        }
                        else {
                            showRegularUI();
                        }
                    }
                });

            }
            else {
                Toast.makeText(signIn.this,"Please verify Email!!",Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(signIn.this,"Authentication Failed!!",Toast.LENGTH_LONG).show();
        }
    }

    private void showModeratorUI() {
        Intent i=new Intent(getApplicationContext(),InstructorMain.class);
        startActivity(i);

    }

    private void showRegularUI() {
        Intent i=new Intent(getApplicationContext(),StudentMain.class);
        startActivity(i);

    }


}



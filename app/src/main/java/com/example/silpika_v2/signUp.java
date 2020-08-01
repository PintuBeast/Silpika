package com.example.silpika_v2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;


public class signUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    private FirebaseFunctions mFunctions;

    EditText ed1, ed2;
    Button b1,b2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mAuth = FirebaseAuth.getInstance();

        ed1 = (EditText) findViewById(R.id.tv_username);
        ed2 = (EditText) findViewById(R.id.tv_password);
        b1 = (Button) findViewById(R.id.btn_signup_Instructor);
        b2 = (Button) findViewById(R.id.btn_signup_Student);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUpUser(true);
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUpUser(false);
            }
        });

    }

    public void signUpUser(Boolean isTeacher) {

        final String username=ed1.getText().toString();
        String password=ed2.getText().toString();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        mFunctions=FirebaseFunctions.getInstance();
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

                            final FirebaseUser userF = mAuth.getCurrentUser();
                            updateUI(userF);
                            userF.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                User user=new User(username,true );
                                                FirebaseDatabase.getInstance().getReference("Users")
                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            if (isTeacher)
                                                            {  callCloudFunction(username);
                                                                Toast.makeText(signUp.this,"Teacher Account created!",Toast.LENGTH_LONG).show();
                                                            }

                                                            else
                                                            {
                                                                Toast.makeText(signUp.this,"Student Account created!",Toast.LENGTH_LONG).show();
                                                            }




                                                        }
                                                        else {
                                                            Toast.makeText(signUp.this, task.getException() .getMessage()  ,Toast.LENGTH_LONG).show();

                                                        }

                                                    }
                                                });



                                                Toast.makeText(signUp.this,"Authenticated!!",Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });


                        } else {

                           // Toast.makeText(signUp.this,"Authentication Failed!!", Toast.LENGTH_LONG).show();
                            updateUI(null);

                        }


                    }
                });

    }

    private Task<String> callCloudFunction (String username) {

//        String username=ed1.getText().toString();

        FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
        Map<String, Object> data = new HashMap<>();

        data.put("text", username);
        data.put("push", true);


        return mFunctions
                //.getHttpsCallable("helloWorldCloudFunction")
                .getHttpsCallable("addAdminRole")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        Log.d("addAdmin",(String) task.getResult().getData());
                        return (String) task.getResult().getData();
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
                finish();
            } else {
                Toast.makeText(signUp.this, "Please verify Email!!", Toast.LENGTH_LONG).show();
            }
        } else {
           // Toast.makeText(signUp.this, "Authentication Failed!!", Toast.LENGTH_LONG).show();
        }


    }


}

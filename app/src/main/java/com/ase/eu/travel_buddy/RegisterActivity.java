package com.ase.eu.travel_buddy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_user;
    private EditText et_pass;
    private Button registerBtn;
    private FirebaseAuth mAuth;
    private static String TAG= "Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        et_user = (EditText) findViewById(R.id.et_newuser);
        et_pass = (EditText) findViewById(R.id.et_newpass);
        registerBtn = (Button) findViewById(R.id.btnRegister);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fieldsCheckSuccessful()) fireBaseRegister();
                else Toast.makeText(RegisterActivity.this,"Error",Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean fieldsCheckSuccessful() {
        boolean fildsOk=true;

        if(et_user.getText().equals("") || et_user.getText().equals(null))
        {
            et_user.setError(getString(R.string.fillReq));
            fildsOk = false;
        }
        else if(!et_pass.getText().toString().contains("@")){
            et_user.setError(getString(R.string.specialChar));
        }

        if(et_pass.getText().equals("") || et_pass.getText().equals(null))
        {
            et_pass.setError(getString(R.string.fillReq));
            fildsOk = false;
        }

        return fildsOk;
    }

    private void fireBaseRegister()
    {
        mAuth.createUserWithEmailAndPassword(et_user.getText().toString(), et_pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            Intent myIntent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(myIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            et_user.setText("");
                            et_pass.setText("");
                        }

                        // ...
                    }
                });
    }
}

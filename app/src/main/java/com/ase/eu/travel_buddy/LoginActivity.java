package com.ase.eu.travel_buddy;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser mUser;

    private static UserPreferences userPreferences = UserPreferences.getInstance();

    private SharedPreferences sharedPreferences = null;
    private SharedPreferencesBuilder sharedPreferencesBuilder = null;

    private EditText et_user;
    private EditText et_pass;
    private Button loginBtn;
    private static String TAG= "Login";
    private CheckBox cb ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        sharedPreferencesBuilder = new SharedPreferencesBuilder(sharedPreferences);

        mAuth = FirebaseAuth.getInstance();

        et_user = (EditText) findViewById(R.id.et_username);
        et_pass = (EditText) findViewById(R.id.et_pass);
        loginBtn = (Button) findViewById(R.id.btnLogin);
        cb=(CheckBox) findViewById(R.id.cb_remember);

        if(sharedPreferencesBuilder.isCustomConfig()) {
            Boolean checkBoxValue = sharedPreferencesBuilder.loadInfo("box");
            String userValue = sharedPreferencesBuilder.loadUserInfo("user");
            String passValue = sharedPreferencesBuilder.loadUserInfo("pass");
            cb.setChecked(checkBoxValue);
            et_user.setText(userValue);
            et_pass.setText(passValue);
        }


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fieldsCheckSuccessful()) firebaseLogin();
                else Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_LONG).show();
            }
        });

        shareMenuInitializer();
    }

    private boolean fieldsCheckSuccessful() {
        boolean fildsOk=true;

        if(et_user.getText().equals("") || et_user.getText().equals(null))
        {
            et_user.setError(getString(R.string.fillReq));
            fildsOk = false;
        }

        if(et_pass.getText().equals("") || et_pass.getText().equals(null))
        {
            et_pass.setError(getString(R.string.fillReq));
            fildsOk = false;
        }

        return fildsOk;
    }

    private boolean isRememberMeChecked()
    {
        if(cb.isChecked()) return true;
        else return false;
    }

    private void setInfoToBeSavedLocally(FirebaseUser user)
    {
        userPreferences.setRememberMe(true);
        userPreferences.setUsername(et_user.getText().toString());
        userPreferences.setPassword(et_pass.getText().toString());
        sharedPreferencesBuilder.saveInfo("box",userPreferences.isRememberMe());
        sharedPreferencesBuilder.saveUserInfo("user","pass",userPreferences.getUsername(),userPreferences.getPassword());
        sharedPreferencesBuilder.setCustomConfig();
    }

    public void firebaseLogin()
    {
        mAuth.signInWithEmailAndPassword(et_user.getText().toString(), et_pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(isRememberMeChecked()){
                                setInfoToBeSavedLocally(user);
                                sharedPreferencesBuilder.setCustomConfig();
                            }
                            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(myIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            et_user.setText("");
                            et_pass.setText("");
                        }

                        // ...
                    }
                });
    }

    public void shareMenuInitializer()
    {
        final ImageView menuIcon = new ImageView(this);
        menuIcon.setImageResource(R.drawable.menu);
        final FloatingActionButton fab = new FloatingActionButton.Builder(this).setContentView(menuIcon).build();

        SubActionButton.Builder builder = new SubActionButton.Builder(this);

        ImageView registerIcon = new ImageView(this);
        registerIcon.setImageResource(R.drawable.register);
        SubActionButton registerBtn = builder.setContentView(registerIcon).build();

        ImageView infoIcon = new ImageView(this);
        infoIcon.setImageResource(R.drawable.information);
        SubActionButton infoBtn = builder.setContentView(infoIcon).build();

        ImageView exitIcon = new ImageView(this);
        exitIcon.setImageResource(R.drawable.exit);
        SubActionButton exitBtn = builder.setContentView(exitIcon).build();


        final FloatingActionMenu fam = new FloatingActionMenu.Builder(this)
                .addSubActionView(exitBtn)
                .addSubActionView(infoBtn)
                .addSubActionView(registerBtn)
                .attachTo(fab)
                .build();

        fam.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
                menuIcon.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION,90);
                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(menuIcon,pvhR);
                animator.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
                menuIcon.setRotation(90);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION,0);
                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(menuIcon,pvhR);
                animator.start();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LoginActivity.this,"Btn facebook",Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(myIntent);
                fam.close(true);
            }
        });

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this,"Btn twitter",Toast.LENGTH_LONG).show();
                fam.close(true);
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser() != null) FirebaseAuth.getInstance().signOut();
                System.exit(0);
                fam.close(true);
            }
        });

    }
}

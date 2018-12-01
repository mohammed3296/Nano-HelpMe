package com.example.mohammedabdullah3296.helpme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mohammedabdullah3296.helpme.models.User;
import com.example.mohammedabdullah3296.helpme.utils.Sesstion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private final String EMAILKEY = "EMAILKEY";
    private final String PASSWORDKEY = "PASSWORDKEY";
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    ProgressDialog progressDialog; // R.style.AppTheme_Dark_Dialog
    private FirebaseAuth auth;
    private String email0;
    private String password0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (savedInstanceState != null) {
            email0 = savedInstanceState.getString(EMAILKEY);
            password0 = savedInstanceState.getString(PASSWORDKEY);
        }
        if (auth.getCurrentUser() != null && Sesstion.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            return;
        }

        setContentView(R.layout.activity_login);
        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        ButterKnife.bind(this);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startPageIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(startPageIntent);
                finish();
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed(getString(R.string.sdfds44fdGfd));
            return;
        }

        email0 = _emailText.getText().toString().trim();
        password0 = _passwordText.getText().toString().trim();
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            _loginButton.setEnabled(false);
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.cvjkbfjck546));
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            auth.signInWithEmailAndPassword(email0, password0)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                progressDialog.dismiss();
                                Snackbar.make(_loginButton, R.string.auth_failed, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                _loginButton.setEnabled(true);
                            } else {
                                DatabaseReference database = FirebaseDatabase.getInstance().getReference();

                                database.child("users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);
                                        onLoginSuccess(user);
                                        Log.e("??>::", user.getEmail());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });

                            }
                        }
                    });


        } else {
            Snackbar.make(_loginButton, R.string.no_internet_conn, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public void onLoginSuccess(User user) {
        _loginButton.setEnabled(true);
        try {
            Sesstion.getInstance(this).userLogin(user);
            Intent startPageIntent = new Intent(LoginActivity.this, HomeActivity.class);
            startPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            progressDialog.dismiss();
            startActivity(startPageIntent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onLoginFailed(String error) {
        _loginButton.setEnabled(true);
        Snackbar.make(_loginButton, error, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(getString(R.string.xfbd54xf23f));
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError(getString(R.string.asdfgh874));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        //moveTaskToBack(true);
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EMAILKEY, email0);
        outState.putString(PASSWORDKEY, password0);
    }
}

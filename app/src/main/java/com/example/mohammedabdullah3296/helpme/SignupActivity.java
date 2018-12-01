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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {
    private final String TAG = "SignupActivity=========";
    private final String EMAILKEY = "EMAILKEY";
    private final String PASSWORDKEY = "PASSWORDKEY";
    private final String FIRSTNAMEKEY = "FIRSTNAMEKEY";
    private final String SECONDNAMEKEY = "SECONDNAMEKEY";
    private final String PHONEKEY = "PHONEKEY";


    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.second_name)
    EditText _second_name;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_mobile)
    EditText _input_mobile;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;
    @BindView(R.id.input_reEnterPassword)
    EditText input_reEnterPassword;
    ProgressDialog progressDialog;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private String firstName0;
    private String secondName0;
    private String email0;
    private String mobileNumber0;
    private String password0;

    //  RequestQueue queue = Volley.newRequestQueue(this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        if (savedInstanceState != null) {
            email0 = savedInstanceState.getString(EMAILKEY);
            password0 = savedInstanceState.getString(PASSWORDKEY);
            firstName0 = savedInstanceState.getString(FIRSTNAMEKEY);
            secondName0 = savedInstanceState.getString(SECONDNAMEKEY);
            mobileNumber0 = savedInstanceState.getString(PHONEKEY);

        }
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startPageIntent = new Intent(SignupActivity.this, LoginActivity.class);
                startPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(startPageIntent);
                finish();
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
    }

    private void writeNewUser(String userId, String firstname, String secondName, String email, String phone) {
        User user = new User(userId, firstname, secondName, email, phone, "");
        mDatabase.child("users").child(userId).setValue(user);
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }
        firstName0 = _nameText.getText().toString().trim();
        secondName0 = _second_name.getText().toString().trim();
        email0 = _emailText.getText().toString().trim();
        mobileNumber0 = _input_mobile.getText().toString().trim();
        password0 = _passwordText.getText().toString().trim();
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            //_signupButton.setEnabled(false);
            progressDialog = new ProgressDialog(SignupActivity.this); // R.style.AppTheme_Dark_Dialog
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.createing_account));
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            auth.createUserWithEmailAndPassword(email0, password0)
                    .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                onSignupFailed();
                            } else {
                                writeNewUser(auth.getUid(), firstName0, secondName0, email0, mobileNumber0);
                                onSignupSuccess();
                            }
                        }
                    });

        } else {
            Snackbar.make(_signupButton, R.string.no_internet_conn, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }

    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        try {
            auth.signOut();
            Intent startPageIntent = new Intent(SignupActivity.this, LoginActivity.class);
            startPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(startPageIntent);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSignupFailed() {
        Snackbar.make(_signupButton, R.string.registeration_failed, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String name = _nameText.getText().toString().trim();
        String email = _emailText.getText().toString().trim();
        String password = _passwordText.getText().toString().trim();
        String mobileNumber = _input_mobile.getText().toString().trim();
        String re_enter = input_reEnterPassword.getText().toString().trim();
        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError(getString(R.string.qscjrjvnbf));
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(getString(R.string.qwrmkfljbvfol));
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobileNumber.isEmpty() || mobileNumber.length() < 11 || mobileNumber.length() > 11) {
            _input_mobile.setError(getString(R.string.dkspojvs5));
            valid = false;
        } else {
            _input_mobile.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError(getString(R.string.dsmvnsm5654));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (!re_enter.equals(password)) {
            input_reEnterPassword.setError(getString(R.string.pfsfvjsl5));
            valid = false;
        } else {
            input_reEnterPassword.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
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
        outState.putString(FIRSTNAMEKEY, firstName0);
        outState.putString(SECONDNAMEKEY, secondName0);
        outState.putString(PHONEKEY, mobileNumber0);
    }
}

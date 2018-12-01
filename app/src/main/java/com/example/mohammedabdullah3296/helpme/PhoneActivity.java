package com.example.mohammedabdullah3296.helpme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mohammedabdullah3296.helpme.utils.Sesstion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PhoneActivity extends AppCompatActivity {
    private Button saveChangesButton;
    private EditText changeStatusTextView;
    private DatabaseReference storeUserDefaultDataBase;
    private FirebaseAuth mAuth;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);


        saveChangesButton = (Button) findViewById(R.id.save_status_button);
        changeStatusTextView = (EditText) findViewById(R.id.input_mobile);
        loading = new ProgressDialog(this);
        String old_status = getIntent().getExtras().get("user_status").toString();
        changeStatusTextView.setText(old_status);
        mAuth = FirebaseAuth.getInstance();
        String current_user_id = mAuth.getCurrentUser().getUid();
        storeUserDefaultDataBase = FirebaseDatabase.getInstance().getReference().child("users").child(current_user_id);


        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = changeStatusTextView.getText().toString();
                changeProfileStatus(status);
            }
        });
    }

    private void changeProfileStatus(final String status) {

        if (status.isEmpty() || status.length() < 11 || status.length() > 11) {
            changeStatusTextView.setError(getString(R.string.valid_number));
        } else {
            changeStatusTextView.setError(null);
            loading.setTitle(getString(R.string.sdfghjkjhg));
            loading.setMessage(getString(R.string.sdfghgf));
            loading.show();

            storeUserDefaultDataBase.child("phone").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Sesstion.getInstance(PhoneActivity.this).editPhoneUser(status);
                        Intent intentSettings = new Intent(PhoneActivity.this, SettingsActivity.class);
                        startActivity(intentSettings);
                        Toast.makeText(PhoneActivity.this, R.string.profile_update, Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(PhoneActivity.this, R.string.error_try_again, Toast.LENGTH_SHORT).show();
                    }
                    loading.dismiss();
                }
            });
        }
    }
}

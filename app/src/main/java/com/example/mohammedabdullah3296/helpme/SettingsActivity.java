package com.example.mohammedabdullah3296.helpme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohammedabdullah3296.helpme.utils.Sesstion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SettingsActivity extends AppCompatActivity {
    private final static int Gallery_PIK = 1;
    private de.hdodenhof.circleimageview.CircleImageView settingsDisplayImage;
    private TextView settingsDisplayUserName;
    private TextView settingsDisplayUserEmail;
    private TextView settingsDisplayPhone;
    private ImageView settingsChangeImageButton;
    private ImageView settingsChangePhoneButton;
    private DatabaseReference retriveUserDefaultDataBase;
    private FirebaseAuth mAuth;
    private StorageReference storProfileImage;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        storProfileImage = FirebaseStorage.getInstance().getReference().child("Profile_Images");

        settingsDisplayImage = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profile);
        settingsDisplayUserName = (TextView) findViewById(R.id.user___name);
        settingsDisplayUserEmail = (TextView) findViewById(R.id.user___email);
        settingsDisplayPhone = (TextView) findViewById(R.id.user___phone);
        settingsChangeImageButton = (ImageView) findViewById(R.id.edit_profile);
        settingsChangePhoneButton = (ImageView) findViewById(R.id.edit_phone);

        if (!TextUtils.isEmpty(Sesstion.getInstance(this).getUser().getProfileImage()))

        {
            Picasso.with(SettingsActivity.this)
                    .load(Sesstion.getInstance(this).getUser().getProfileImage())
                    .placeholder(R.drawable.profile).into(settingsDisplayImage);
        }

        settingsDisplayUserName.setText(Sesstion.getInstance(this).getUser().getFirstName()
                + " " + Sesstion.getInstance(this).getUser().getSecondName());
        settingsDisplayUserEmail.setText(Sesstion.getInstance(this).getUser().getEmail());
        settingsDisplayPhone.setText(Sesstion.getInstance(this).getUser().getPhone());


        String online_user_id = mAuth.getCurrentUser().getUid();
        retriveUserDefaultDataBase = FirebaseDatabase.getInstance().getReference().child("users").child(online_user_id);


        settingsChangeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1)
                        .start(SettingsActivity.this);
            }
        });

        settingsChangePhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldStatus = settingsDisplayPhone.getText().toString();
                Intent StatusIntent = new Intent(SettingsActivity.this, PhoneActivity.class);
                StatusIntent.putExtra("user_status", oldStatus);
                startActivity(StatusIntent);

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                finish();
                return true;
        }

        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                String user_id = mAuth.getCurrentUser().getUid();
                StorageReference filePath = storProfileImage.child(user_id + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingsActivity.this, R.string.saved, Toast.LENGTH_SHORT).show();
                            final String download = task.getResult().getDownloadUrl().toString();
                            retriveUserDefaultDataBase.child("profileImage").setValue(download).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Sesstion.getInstance(SettingsActivity.this).editProfileUser(download);

                                    if (!TextUtils.isEmpty(Sesstion.getInstance(SettingsActivity.this).getUser().getProfileImage())) {
                                        Picasso.with(SettingsActivity.this)
                                                .load(Sesstion.getInstance(SettingsActivity.this).getUser().getProfileImage())
                                                .placeholder(R.drawable.profile).into(settingsDisplayImage);
                                    }
                                    Toast.makeText(SettingsActivity.this, R.string.image_ghgh, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(SettingsActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}

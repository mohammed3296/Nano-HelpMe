package com.example.mohammedabdullah3296.helpme;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohammedabdullah3296.helpme.models.Feed;
import com.example.mohammedabdullah3296.helpme.utils.Sesstion;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreatePostActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.child_name)
    EditText child_name;
    @BindView(R.id.child_age)
    EditText child_age;
    @BindView(R.id.gender)
    EditText gender;
    @BindView(R.id.child_height)
    EditText child_height;
    @BindView(R.id.child_heir)
    EditText child_heir;
    @BindView(R.id.child_eyes)
    EditText child_eyes;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.child_weight)
    EditText child_weight;
    @BindView(R.id.child_image)
    ImageView child_image;
    @BindView(R.id.chose_image)
    Button chose_image;
    @BindView(R.id.address_text)
    TextView address_text;
    @BindView(R.id.chose_address)
    Button chose_address;
    ProgressDialog progressDialog;
    private boolean mPetHasChanged = false;
    private int PICK_IMAGE_REQUEST = 2;
    private Bitmap bitmap;
    private Uri filePath;
    private DatabaseReference mDatabase;
    private String placename;
    private String latitude;
    private String longitude;
    private String address;
    //Firebase
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String downloadImage;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPetHasChanged = true;
            return false;
        }
    };
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ButterKnife.bind(this);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        if (savedInstanceState != null) {
            filePath = Uri.parse(savedInstanceState.getString("filePath111").toString());
            PICK_IMAGE_REQUEST = savedInstanceState.getInt("PICK_IMAGE_REQUEST111");
            placename = savedInstanceState.getString("placename111");
            latitude = savedInstanceState.getString("latitude111");
            longitude = savedInstanceState.getString("longitude111");
            address = savedInstanceState.getString("address111");
        }
        child_name.setOnTouchListener(mTouchListener);
        child_eyes.setOnTouchListener(mTouchListener);
        child_heir.setOnTouchListener(mTouchListener);
        child_height.setOnTouchListener(mTouchListener);
        child_weight.setOnTouchListener(mTouchListener);
        child_image.setOnTouchListener(mTouchListener);
        child_age.setOnTouchListener(mTouchListener);
        address_text.setOnTouchListener(mTouchListener);
        description.setOnTouchListener(mTouchListener);
        gender.setOnTouchListener(mTouchListener);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post_button:
                post();
                return true;
            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mPetHasChanged) {
                    NavUtils.navigateUpFromSameTask(CreatePostActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(CreatePostActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;

        }

        return (super.onOptionsItemSelected(item));
    }

    private void writeNewPost(String placename, String datetime, String description,
                              String usermail, String userphone, String childname, String childage, String childsex,
                              String height, String weight, String hair, String eyes, String latitude, String longitude,
                              String username, String userImage) {
        UUID idOne = UUID.randomUUID();
        Feed feed = new Feed(String.valueOf(idOne), downloadImage, placename, datetime, description, usermail, userphone, childname, childage, childsex,
                height, weight, hair, eyes, latitude, longitude, username, userImage);
        mDatabase.child("posters").child(String.valueOf(idOne)).setValue(feed);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(CreatePostActivity.this, R.string.sdfghjklhgfd,
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, 1000);
    }

    private void post() {
        if (!validate()) {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            return;
        }
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            progressDialog = new ProgressDialog(CreatePostActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Posting...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            UUID idOne = UUID.randomUUID();
            uploadImage(String.valueOf(idOne));
        } else {
            Snackbar.make(chose_address, R.string.no_internet_con, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }
    }

    public boolean validate() {
        boolean valid = true;
        String child_name1 = child_name.getText().toString().trim();
        String child_age1 = child_age.getText().toString().trim();
        String gender1 = gender.getText().toString().trim();
        String child_height1 = child_height.getText().toString().trim();
        String child_weight1 = child_weight.getText().toString().trim();
        String child_heir1 = child_heir.getText().toString().trim();
        String child_eyes1 = child_eyes.getText().toString().trim();
        String description1 = description.getText().toString().trim();
        String address_text1 = address_text.getText().toString().trim();
        Drawable child_image1 = child_image.getDrawable();

        if (child_name1.isEmpty() || child_name1.length() < 3) {
            child_name.setError(getString(R.string.ertyujkgfdtyuj));
            valid = false;
        } else {
            child_name.setError(null);
        }

        if (child_age1.isEmpty() || Integer.parseInt(child_age1) == 0) {
            child_age.setError(getString(R.string.dfghjghkjkhhyjh));
            valid = false;
        } else {
            child_age.setError(null);
        }
        if (gender1.isEmpty() || gender1.length() < 3) {
            gender.setError(getString(R.string.jhgsdcujikl5352));
            valid = false;
        } else {
            gender.setError(null);
        }
        if (child_height1.isEmpty() || Double.parseDouble(child_height1) == 0) {
            child_height.setError(getString(R.string.jhfhgfxhfcgjn6335));
            valid = false;
        } else {
            child_height.setError(null);
        }
        if (child_weight1.isEmpty() || Double.parseDouble(child_weight1) == 0) {
            child_weight.setError(getString(R.string.kjghchgjnkl635));
            valid = false;
        } else {
            child_weight.setError(null);
        }

        if (child_heir1.isEmpty()) {
            child_heir.setError(getString(R.string.jhgfddxcjkl635));
            valid = false;
        } else {
            child_heir.setError(null);
        }

        if (child_eyes1.isEmpty()) {
            child_eyes.setError(getString(R.string.dfghgjhnl54));
            valid = false;
        } else {
            child_eyes.setError(null);
        }
        if (address_text1.isEmpty() || address_text1.equals(getString(R.string.poioui54))) {
            address_text.setError(getString(R.string.kkhjhujgugvh789));
            Toast.makeText(this, R.string.jhvgfdfc5421, Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            address_text.setError(null);
        }

        if (child_image1 == null) {
            Toast.makeText(this, R.string.eryhyqaesdf, Toast.LENGTH_SHORT).show();
            valid = false;
        } else {

        }
        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @OnClick(R.id.chose_address)
    public void choseAddress() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(CreatePostActivity.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(chose_address, connectionResult.getErrorMessage() + "", Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                child_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                placename = String.format("%s", place.getName());
                latitude = String.valueOf(place.getLatLng().latitude);
                longitude = String.valueOf(place.getLatLng().longitude);
                address = String.format("%s", place.getAddress());
                stBuilder.append(getString(R.string.hgfygcgh656));
                stBuilder.append(placename);
                stBuilder.append("\n");
                stBuilder.append(getString(R.string.hfhtrgeserdxhcgjhvkhjnkl65));
                stBuilder.append(latitude);
                stBuilder.append("\n");
                stBuilder.append(getString(R.string.hjfghvkj635463));
                stBuilder.append(longitude);
                stBuilder.append("\n");
                stBuilder.append(getString(R.string.kuyfgthfvhkj65));
                stBuilder.append(address);
                address_text.setText(stBuilder.toString());
            }
        }
    }


    @OnClick(R.id.chose_image)
    public void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.ukyfvhgvhjkbkj65659)), PICK_IMAGE_REQUEST);
    }


    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.iuertyuierwhgi545);
        builder.setPositiveButton(R.string.fjdkbghlsd54, discardButtonClickListener);
        builder.setNegativeButton(R.string.djghsl545, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void uploadImage(String Imageid) {

        if (filePath != null) {
            StorageReference ref = storageReference.child("imagesOfChildes/" + Imageid);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            downloadImage = taskSnapshot.getDownloadUrl().toString();
                            writeNewPost(address, System.currentTimeMillis() + "", description.getText().toString().trim()
                                    , Sesstion.getInstance(CreatePostActivity.this).getUser().getEmail(), Sesstion.getInstance(CreatePostActivity.this).getUser().getPhone()
                                    , child_name.getText().toString().trim(), child_age.getText().toString(), gender.getText().toString().trim(),
                                    child_height.getText().toString().trim(), child_weight.getText().toString().trim(), child_heir.getText().toString().trim(),
                                    child_eyes.getText().toString().trim(), latitude, longitude, Sesstion.getInstance(CreatePostActivity.this).getUser().getFirstName() + " " +
                                            Sesstion.getInstance(CreatePostActivity.this).getUser().getSecondName(), Sesstion.getInstance(CreatePostActivity.this).getUser().getProfileImage());

                            //   progressDialog.dismiss();
                            Toast.makeText(CreatePostActivity.this, R.string.fdgshdl566, Toast.LENGTH_SHORT).show();
                            //   uploaded = true ;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //    progressDialog.dismiss();

                            Toast.makeText(CreatePostActivity.this, getString(R.string.dfgskhg8788) + e.getMessage(), Toast.LENGTH_SHORT).show();
                            //   uploaded = false ;
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage(getString(R.string.dfjghsjk878) + (int) progress + "%");
                        }
                    });
        }
        //  return uploaded ;
    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mPetHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("filePath111", mPetHasChanged);
        if (filePath != null)
            outState.putString("filePath111", filePath.toString());

        outState.putInt("PICK_IMAGE_REQUEST111", PICK_IMAGE_REQUEST);
        if (placename != null)
            outState.putString("placename111", placename);
        if (latitude != null)
            outState.putString("latitude111", latitude);
        if (longitude != null)
            outState.putString("longitude111", longitude);
        if (address != null)
            outState.putString("address111", address);
    }
}

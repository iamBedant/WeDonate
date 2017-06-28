package io.firebasehacks.wedonate.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import io.firebasehacks.wedonate.R;
import io.firebasehacks.wedonate.model.User;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

  ImageView profileImage;
  Button update;
  EditText name, email;
  FirebaseDatabase mDb;
  Button uploadImage;
  FirebaseUser mAuth;
  TextView phoneNo;
  int STATE_ORGANIZATION;
  String radiovalue;
  String profilePic;
  FirebaseStorage storage;
  Uri filePath;
  ProgressDialog pd;
  StorageReference storageRef;
  int PICK_IMAGE_REQUEST = 100;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    profileImage = findViewById(R.id.img_profile);
    update = findViewById(R.id.btn_update);
    update.setEnabled(false);
    name = findViewById(R.id.et_name);
    email = findViewById(R.id.et_email);
    phoneNo = findViewById(R.id.phone);
    mDb = FirebaseDatabase.getInstance();
    mAuth = FirebaseAuth.getInstance().getCurrentUser();
    uploadImage = findViewById(R.id.upload_image);
    storage = FirebaseStorage.getInstance();
    storageRef = storage.getReference().child("photos").child(mAuth.getUid());
    pd = new ProgressDialog(this);
    pd.setMessage("Uploading....");
    DatabaseReference myRef = mDb.getReference("users").child(mAuth.getUid());
    myRef.addValueEventListener(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        phoneNo.setText(mAuth.getPhoneNumber());
        if (!dataSnapshot.child("userName").getValue().toString().isEmpty()) {
          name.setText(dataSnapshot.child("userName").getValue().toString());
        }
        if (!dataSnapshot.child("email").getValue().toString().isEmpty()) {
          email.setText(dataSnapshot.child("email").getValue().toString());
        }
        if (!dataSnapshot.child("profilePic").getValue().toString().isEmpty()) {
          profilePic = dataSnapshot.child("profilePic").getValue().toString();
          Glide.with(ProfileActivity.this)
              .load(dataSnapshot.child("profilePic").getValue().toString())
              .into(profileImage);
        }

        update.setEnabled(true);
      }

      @Override public void onCancelled(DatabaseError databaseError) {

      }
    });

    uploadImage.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
      }
    });

    update.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference("users").child(mAuth.getUid());

        Map<String, Object> properties = new HashMap<>();
        properties.put("userName", name.getText().toString());
        properties.put("email", email.getText().toString());

        myRef.updateChildren(properties);
      }
    });
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == PICK_IMAGE_REQUEST
        && resultCode == RESULT_OK
        && data != null
        && data.getData() != null) {
      filePath = data.getData();

      try {
        //getting image from gallery
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

        if (filePath != null) {
          pd.show();

          StorageReference childRef = storageRef.child("image.jpg");

          //uploading the image
          UploadTask uploadTask = childRef.putFile(filePath);

          uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
              pd.dismiss();
              Toast.makeText(ProfileActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
            }
          }).addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception e) {
              pd.dismiss();
              Toast.makeText(ProfileActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT)
                  .show();
            }
          });
        } else {
          Toast.makeText(ProfileActivity.this, "Select an image", Toast.LENGTH_SHORT).show();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}

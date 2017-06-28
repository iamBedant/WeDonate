package io.firebasehacks.wedonate.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import io.firebasehacks.wedonate.R;
import io.firebasehacks.wedonate.model.User;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

  ImageView profileImage;
  Button update;
  EditText name, email;
  FirebaseDatabase mDb;
  FirebaseUser mAuth;
  TextView phoneNo;
  int STATE_ORGANIZATION;
  String radiovalue;
  String profilePic;

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
}

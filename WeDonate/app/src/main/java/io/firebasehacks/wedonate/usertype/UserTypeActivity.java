package io.firebasehacks.wedonate.usertype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import io.firebasehacks.wedonate.FirebaseFeedManager;
import io.firebasehacks.wedonate.Home;
import io.firebasehacks.wedonate.R;
import io.firebasehacks.wedonate.model.User;

public class UserTypeActivity extends AppCompatActivity {

  RadioGroup mradioGroup;
  RadioButton mRadioButtonIndividual, mRadioButtonOrganization;
  FrameLayout mFrameLayoutContainer;
  private static final int STATE_INDIVIDUAL = 0;
  private static final int STATE_ORGANIZATION = 1;
  private FirebaseAuth mAuth;
  View individualView, organizationView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_type);

    mradioGroup = findViewById(R.id.radio_group);
    mRadioButtonIndividual = findViewById(R.id.r_btn_individual);
    mRadioButtonOrganization = findViewById(R.id.r_button_organozation);
    mFrameLayoutContainer = findViewById(R.id.container);
    mradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
        // find which radio button is selected
        if (checkedId == R.id.r_btn_individual) {
          updateUi(STATE_INDIVIDUAL);
        } else if (checkedId == R.id.r_button_organozation) {
          updateUi(STATE_ORGANIZATION);
        } else {
          Toast.makeText(UserTypeActivity.this, "invalid Selection", Toast.LENGTH_SHORT).show();
        }
      }
    });

    mRadioButtonIndividual.setChecked(true);
  }

  private void updateUi(int stateOrganization) {
    Button mButtonNext;
    switch (stateOrganization) {
      case STATE_INDIVIDUAL:
        individualView = getLayoutInflater().inflate(R.layout.view_individual, null, false);
        final CheckBox money = individualView.findViewById(R.id.r_btn_money);
        final CheckBox clothes = individualView.findViewById(R.id.r_button_clothes);
        mButtonNext = individualView.findViewById(R.id.btn_next);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View view) {
            //Update Firebase
            String type = "";
            if (money.isChecked()) {
              type = type + "money";
            }
            if (clothes.isChecked()) {
              if (type.contains("money")) {
                type = type + ",";
              }
              type = type + "clothes";
            }

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            User user = new User(firebaseUser.getUid(), "User", "", "",
                STATE_INDIVIDUAL, type);
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference myRef = db.getReference("users").child(firebaseUser.getUid());
            myRef.setValue(user);

            Intent intent = new Intent(UserTypeActivity.this, Home.class);
            startActivity(intent);
          }
        });
        mFrameLayoutContainer.removeAllViews();
        mFrameLayoutContainer.addView(individualView);

        break;
      case STATE_ORGANIZATION:
        organizationView = getLayoutInflater().inflate(R.layout.view_organization, null, false);
        mButtonNext = organizationView.findViewById(R.id.btn_next);
        final RadioGroup radioGroup = organizationView.findViewById(R.id.rg);

        mButtonNext.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View view) {
            //Update Firebase
            String radiovalue = "";
            if (radioGroup.getCheckedRadioButtonId() != -1) {
              int id = radioGroup.getCheckedRadioButtonId();
              View radioButton = radioGroup.findViewById(id);
              int radioId = radioGroup.indexOfChild(radioButton);
              RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
              radiovalue = (String) btn.getText();
            }

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            User user = new User(firebaseUser.getUid(), "User", "", "",
                STATE_ORGANIZATION, radiovalue);
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference myRef = db.getReference("users").child(firebaseUser.getUid());
            myRef.setValue(user);

            Intent intent = new Intent(UserTypeActivity.this, Home.class);
            startActivity(intent);
          }
        });
        mFrameLayoutContainer.removeAllViews();
        mFrameLayoutContainer.addView(organizationView);
        break;
    }
  }
}

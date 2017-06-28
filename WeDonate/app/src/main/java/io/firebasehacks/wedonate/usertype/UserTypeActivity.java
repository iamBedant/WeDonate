package io.firebasehacks.wedonate.usertype;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import io.firebasehacks.wedonate.R;

public class UserTypeActivity extends AppCompatActivity {

  RadioGroup mradioGroup;
  RadioButton mRadioButtonIndividual, mRadioButtonOrganization;
  FrameLayout mFrameLayoutContainer;
  private static final int STATE_INDIVIDUAL = 1;
  private static final int STATE_ORGANIZATION = 2;

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
    switch (stateOrganization) {
      case STATE_INDIVIDUAL:
        individualView = getLayoutInflater().inflate(R.layout.view_individual, null, false);
        mFrameLayoutContainer.addView(individualView);
        break;
      case STATE_ORGANIZATION:

        organizationView = getLayoutInflater().inflate(R.layout.view_organization, null, false);
        mFrameLayoutContainer.addView(organizationView);
        break;
    }
  }
}

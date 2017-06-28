package io.firebasehacks.wedonate.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import io.firebasehacks.wedonate.Constants;
import io.firebasehacks.wedonate.FirebaseFeedManager;
import io.firebasehacks.wedonate.R;
import io.firebasehacks.wedonate.model.Feed;

/**
 * Created by chansek on 28/06/17.
 */

public class AddDonationActivity extends AppCompatActivity {

    Spinner whatToDonate;
    EditText quantity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donation);

        whatToDonate = findViewById(R.id.what_to_donate);
        quantity = findViewById(R.id.quantity);
    }

    public void onSubmit(View view) {
        Feed feed = new Feed();
        Long currTime = System.currentTimeMillis();
        feed.setFeedTime(currTime);
        feed.setFeedPriority(-currTime);
        feed.setFeedType(Constants.FEED_TYPE_DONATION);

        String item = (String) whatToDonate.getSelectedItem();
        String quantity = this.quantity.getText().toString();

        Map<String, Object> properties = new HashMap<>();
        properties.put("what", item);
        properties.put("quantity", quantity);

        feed.setProperties(properties);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference();
        FirebaseFeedManager.insertDonationFeedToServer(dbRef, feed);
        finish();
    }
}

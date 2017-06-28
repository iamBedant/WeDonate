package io.firebasehacks.wedonate;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import io.firebasehacks.wedonate.model.Feed;

/**
 * Created by chansek on 28/06/17.
 */
public final class FirebaseFeedManager {

    private static final String TAG = FirebaseFeedManager.class.getName();

    interface Listener<T> {
        void onRequestComplete(T response);
    }

    public static void insertDonationFeedToServer(DatabaseReference dbRef, Feed feed) {
        // Create new feed at /user-feeds/$userid/$feedid and at
        // /feeds/$feedid simultaneously
        String key = dbRef.child("feeds").push().getKey();
        feed.setFeedId(key);
        Map<String, Object> feedValues = feed.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/feeds/" + key, feedValues);
        childUpdates.put("/user-feeds/" + feed.getUserId() + "/" + key, feedValues);

        dbRef.updateChildren(childUpdates);
    }

    public static void getFeedFromFeedId(@NonNull DatabaseReference dbRef, @NonNull String feedId,
                                         final Listener<Feed> listener) {
        dbRef.child("feeds").child(feedId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Feed feed = dataSnapshot.getValue(Feed.class);
                        listener.onRequestComplete(feed);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if (databaseError != null)
                            databaseError.toException().printStackTrace();
                    }
                });
    }
}

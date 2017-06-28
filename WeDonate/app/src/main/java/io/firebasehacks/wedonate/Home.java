package io.firebasehacks.wedonate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.firebasehacks.wedonate.profile.ProfileActivity;
import java.lang.ref.WeakReference;

import io.firebasehacks.wedonate.activity.AddDonationActivity;
import io.firebasehacks.wedonate.model.Feed;

public class Home extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

  public static final String KEY_USER_TYPE = "user_type";
  private int mUserType;

  DatabaseReference mDb;
  private RecyclerView mRecyclerView;
  private FeedsAdapter mAdapter;
  ProgressBar mProgressBar;
  TextView mEmptyView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(this);

    NavigationView navigationView = findViewById(R.id.nav_view);

    mUserType = getIntent().getIntExtra(KEY_USER_TYPE, Constants.USER_TYPE_INDIVIDUAL);
    if (mUserType == Constants.USER_TYPE_INDIVIDUAL) {
      navigationView.inflateMenu(R.menu.activity_home_donor_drawer);
    } else {
      navigationView.inflateMenu(R.menu.activity_home_org_drawer);
    }
    navigationView.setNavigationItemSelectedListener(this);

    mProgressBar = findViewById(R.id.loading);
    mProgressBar.setVisibility(View.VISIBLE);
    mEmptyView = findViewById(R.id.empty_message);

    mRecyclerView = findViewById(R.id.recycler_view);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.setHasFixedSize(true);

    mDb = FirebaseDatabase.getInstance().getReference();
    fetchAndLoadFeeds("mine");
  }

  private void fetchAndLoadFeeds(String fetchType) {
    Query ref;
    switch (fetchType) {
      case "all":
      case "friends":
        ref = mDb.child("feeds").orderByChild("priority");
        break;
      case "mine":
        // TODO: 28/06/17
        String currUserId = getIntent().getStringExtra("currUserId");
        String userId = currUserId == null ? "" : currUserId;
        if (!TextUtils.isEmpty(userId)) {
          ref = mDb.child("user-feeds").child(userId).orderByChild("priority");
        } else {
          ref = mDb.child("feeds").orderByChild("priority");
        }
        break;
      default:
        throw new IllegalArgumentException("Feed Fetch Type mentioned was wrong!!!");
    }
    ref = ref.limitToFirst(250);
    ref.keepSynced(true);
    ref.addListenerForSingleValueEvent(new FeedValueEventListener(this));
    mAdapter =
        new FeedsAdapter(this, Feed.class, R.layout.item_feed_donation, FeedViewHolder.class, ref);
    mRecyclerView.setAdapter(mAdapter);
  }

  @Override public void onBackPressed() {
    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @SuppressWarnings("StatementWithEmptyBody") @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    switch (id) {
      case R.id.nav_all_donations:
        fetchAndLoadFeeds("all");
        break;
      case R.id.nav_friends_donations:
        fetchAndLoadFeeds("friends");
        break;
      case R.id.nav_my_donations:
        fetchAndLoadFeeds("mine");
        break;
      case R.id.nav_send:
        Intent intent = new Intent(Home.this, ProfileActivity.class);
        startActivity(intent);
        break;
    }

    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.fab:
        handleFab();
        break;
    }
  }

  @Override public void onDestroy() {
    super.onDestroy();
    mAdapter.cleanup();
    mRecyclerView.setAdapter(null);
  }

  private void handleFab() {
    if (mUserType == Constants.USER_TYPE_INDIVIDUAL) {
      Intent intent = new Intent(this, AddDonationActivity.class);
      startActivity(intent);
    } else {

    }
  }

  private static class FeedsAdapter extends FirebaseRecyclerAdapter<Feed, FeedViewHolder> {
    private final WeakReference<Home> mActivity;

    FeedsAdapter(Home activity, Class<Feed> modelClass, int modelLayout,
        Class<FeedViewHolder> viewHolderClass, Query ref) {
      super(modelClass, modelLayout, viewHolderClass, ref);
      mActivity = new WeakReference<>(activity);
    }

    @Override
    protected void populateViewHolder(final FeedViewHolder viewHolder, Feed feed, int position) {
      Home activity = mActivity.get();
      if (activity != null) {
        try {
          viewHolder.bind(feed);
          switch (feed.getFeedType()) {
            // TODO: 28/06/17
            case Constants.FEED_TYPE_DONATION:
              //                            activity.populateGameFeed((GameFeedViewHolder) viewHolder, feed);
              break;
            case Constants.FEED_TYPE_DON_REQ:
              //                            activity.populateChallengeFeed((ChallengeFeedViewHolder) viewHolder, feed);
              break;
          }
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }

    @Override public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      FeedViewHolder holder;
      switch (viewType) {
        case Constants.FEED_TYPE_DONATION:
          View donationView = LayoutInflater.from(parent.getContext())
              .inflate(R.layout.item_feed_donation, parent, false);
          holder = new FeedViewHolder(donationView);
          return holder;
        case Constants.FEED_TYPE_DON_REQ:
          View donReqView = LayoutInflater.from(parent.getContext())
              .inflate(R.layout.item_feed_donation, parent, false);
          holder = new FeedViewHolder(donReqView);
          return holder;
      }
      return super.onCreateViewHolder(parent, viewType);
    }

    @Override public int getItemViewType(int position) {
      Feed feed = getItem(position);
      return feed.getFeedType();
    }
  }

  private static class FeedValueEventListener implements ValueEventListener {
    private final WeakReference<Home> mActivity;

    FeedValueEventListener(Home activity) {
      mActivity = new WeakReference<>(activity);
    }

    @Override public void onDataChange(DataSnapshot dataSnapshot) {
      Home activity = mActivity.get();
      if (activity != null) {
        //onDataChange called so remove progress bar
        activity.mProgressBar.setVisibility(View.GONE);
        if (!dataSnapshot.hasChildren()) {
          // Server has no data to be shown, show the empty view
          activity.mEmptyView.setVisibility(View.VISIBLE);
          activity.mEmptyView.setText("No Donations Yet!!!");
        } else {
          activity.mEmptyView.setVisibility(View.GONE);
        }
      }
      //use helper method to add an Observer to RecyclerView
    }

    @Override public void onCancelled(DatabaseError databaseError) {
      if (databaseError != null) {
        databaseError.toException().printStackTrace();
      }
    }
  }
}

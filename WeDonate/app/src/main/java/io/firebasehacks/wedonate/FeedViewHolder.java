/*
 * Copyright (c) 2016. Chanse Games - All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package io.firebasehacks.wedonate;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Map;

import io.firebasehacks.wedonate.model.Feed;

/**
 * This view holder for holding the feed card view.
 *
 * Created by chansek on 29/05/16.
 */

public class FeedViewHolder extends RecyclerView.ViewHolder {

    private final CircleImageView mOwnerPhoto;
    private final RelativeLayout mProfileContainer;
    private final TextView mOwnerName, mFeedTime, mItemKey, mItemVal, mItem1Key, mItem1Val;

    FeedViewHolder(View itemView) {
        super(itemView);
        mOwnerPhoto = itemView.findViewById(R.id.feed_owner_photo);
        mOwnerName = itemView.findViewById(R.id.feed_owner_name);
        mFeedTime = itemView.findViewById(R.id.feed_time);
        mProfileContainer = itemView.findViewById(R.id.feed_owner_container);
        mItemKey = itemView.findViewById(R.id.score_key);
        mItemVal = itemView.findViewById(R.id.score_value);
        mItem1Key = itemView.findViewById(R.id.item1_key);
        mItem1Val = itemView.findViewById(R.id.item1_value);
    }

    public void bind(Feed feed) {
        // Set the owner name who owns the current feed
        mOwnerName.setText(feed.getUserName());
        // Set the feed time
        mFeedTime.setText(getRelativeTime(feed.getFeedTime()));
        // Set the owner profile pic
        setProfilePic(feed.getProfilePic());

        Map<String, Object> props = feed.getProperties();
        mItemVal.setText(props.get("what").toString());
        mItem1Val.setText(props.get("quantity").toString());
    }

    private void setProfilePic(String picUrl) {
        if (!TextUtils.isEmpty(picUrl)) {
            Picasso.with(mOwnerPhoto.getContext()).load(picUrl).error(R.drawable.avatar).into(mOwnerPhoto);
        } else {
            mOwnerPhoto.setImageResource(R.drawable.avatar);
        }
    }

    private String getRelativeTime(long time) {
        return DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();
    }
}
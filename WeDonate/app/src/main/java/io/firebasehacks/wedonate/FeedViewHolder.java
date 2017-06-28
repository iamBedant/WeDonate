/*
 * Copyright (c) 2016. Chanse Games - All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package io.firebasehacks.wedonate;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.firebasehacks.wedonate.model.Feed;

/**
 * This view holder for holding the feed card view.
 *
 * Created by chansek on 29/05/16.
 */

public class FeedViewHolder extends RecyclerView.ViewHolder {

    private final CircleImageView mOwnerPhoto;
    private final RelativeLayout mProfileContainer;
    private final TextView mOwnerName, mFeedTime;

    FeedViewHolder(View itemView) {
        super(itemView);
        mOwnerPhoto = (CircleImageView) itemView.findViewById(R.id.feed_owner_photo);
        mOwnerName = (TextView) itemView.findViewById(R.id.feed_owner_name);
        mFeedTime = (TextView) itemView.findViewById(R.id.feed_time);
        mProfileContainer = (RelativeLayout) itemView.findViewById(R.id.feed_owner_container);
    }

    public void bind(Feed feed) {
        // Set the owner name who owns the current feed
        mOwnerName.setText(feed.getUserName());
        // Set the feed time
        mFeedTime.setText(feed.getFeedTime() + "");
        // Set the owner profile pic
        setProfilePic(feed.getProfilePic());
    }

    private void setProfilePic(String picUrl) {
        if (!TextUtils.isEmpty(picUrl)) {
            Picasso.with(mOwnerPhoto.getContext()).load(picUrl).error(R.drawable.avatar).into(mOwnerPhoto);
        } else {
            mOwnerPhoto.setImageResource(R.drawable.avatar);
        }
    }
}
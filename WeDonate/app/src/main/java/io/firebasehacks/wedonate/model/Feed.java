package io.firebasehacks.wedonate.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chansek on 28/06/17.
 */

public class Feed {

    private String feedId;
    private long feedTime;
    private long feedPriority;
    private String userId;
    private String userName;
    private String profilePic;
    private int feedType;
    private String message;

    private Map<String, Object> properties = new HashMap<>();

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public long getFeedTime() {
        return feedTime;
    }

    public void setFeedTime(long feedTime) {
        this.feedTime = feedTime;
    }

    public long getFeedPriority() {
        return feedPriority;
    }

    public void setFeedPriority(long feedPriority) {
        this.feedPriority = feedPriority;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public int getFeedType() {
        return feedType;
    }

    public void setFeedType(int feedType) {
        this.feedType = feedType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("feedId", feedId);
        result.put("time", feedTime);
        result.put("priority", feedPriority);
        result.put("feedType", feedType);
        result.put("userId", userId);
        result.put("userName", userName);
        result.put("profilePic", profilePic);
        result.put("properties", properties);
        result.put("message", message);
        return result;
    }
}

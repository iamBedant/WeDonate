package io.firebasehacks.wedonate.model;

/**
 * Created by chansek on 28/06/17.
 */

public class User {

    private String userId;
    private String userName;
    private String email;
    private String profilePic;
    private int userType;
    private String userRole;

    public User(String uid,
                String displayName,
                String s,
                String s1,
                int stateOrganization,
                int i,
                String radiovalue) {

    }

    public User(
        String userId,
        String userName,
        String email,
        String profilePic,
        int userType,
        String userRole) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.profilePic = profilePic;
        this.userType = userType;
        this.userRole = userRole;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}

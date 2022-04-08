package com.ping_pong.android;

import java.util.ArrayList;

public class UserInfo {
    private String userId;
    private String userName;
    private boolean userState;
    private ArrayList<String> roomIds;

    public UserInfo(String userId, String userName, boolean userState, ArrayList<String> roomIds) {
        this.userId = userId;
        this.userName = userName;
        this.userState = userState;
        this.roomIds = roomIds;
    }

    public UserInfo() {

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

    public boolean isUserState() {
        return userState;
    }

    public void setUserState(boolean userState) {
        this.userState = userState;
    }

    public ArrayList<String> getRoomIds() {
        return roomIds;
    }

    public void setRoomIds(ArrayList<String> roomIds) {
        this.roomIds = roomIds;
    }
}

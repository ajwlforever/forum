package com.ajwlforever.forum.entity;

import java.util.Date;

public class User {
    private int id;
    private String username;
    private String nickname;
    private String password;
    private String salt;
    private String email;
    private int type;
    private String activeCode;
    private int status;
    private Date createTime;
    private Date activeTime;
    private String headerUrl;

    public int getId() {
        return id;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public User setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getSalt() {
        return salt;
    }

    public User setSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public int getType() {
        return type;
    }

    public User setType(int type) {
        this.type = type;
        return this;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public User setActiveCode(String activeCode) {
        this.activeCode = activeCode;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public User setStatus(int status) {
        this.status = status;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public User setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getActiveTime() {
        return activeTime;
    }

    public User setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
        return this;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public User setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", email='" + email + '\'' +
                ", type=" + type +
                ", activeCode='" + activeCode + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", activeTime=" + activeTime +
                ", headerUrl='" + headerUrl + '\'' +
                '}';
    }
}

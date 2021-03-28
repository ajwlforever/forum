package com.ajwlforever.forum.entity;

import java.util.Date;

public class LoginTicket {
    private int userId;
    private String ticket;
    private int status;  // 0-正常 1-淘汰
    private Date expired;


    public int getUserId() {
        return userId;
    }

    public LoginTicket setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public String getTicket() {
        return ticket;
    }

    public LoginTicket setTicket(String ticket) {
        this.ticket = ticket;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public LoginTicket setStatus(int status) {
        this.status = status;
        return this;
    }

    public Date getExpired() {
        return expired;
    }

    public LoginTicket setExpired(Date expired) {
        this.expired = expired;
        return this;
    }
}

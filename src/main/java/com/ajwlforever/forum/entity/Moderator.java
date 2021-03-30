package com.ajwlforever.forum.entity;

import java.util.Date;

/**
 * 版主的实体类
 * author: ajwlforever
 * id int(11) AI PK
 * user_id int(11)
 * board_id int(11)
 * create_time timestamp
 */
public class Moderator {
    private int id;
    private int userId;
    private int boardId;
    private Date createTime;

    @Override
    public String toString() {
        return "Moderator{" +
                "id=" + id +
                ", userId=" + userId +
                ", boardId=" + boardId +
                ", createTime=" + createTime +
                '}';
    }

    public int getId() {
        return id;
    }

    public Moderator setId(int id) {
        this.id = id;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Moderator setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getBoardId() {
        return boardId;
    }

    public Moderator setBoardId(int boardId) {
        this.boardId = boardId;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Moderator setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
}

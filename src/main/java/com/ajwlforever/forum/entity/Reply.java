package com.ajwlforever.forum.entity;

import java.util.Date;

/**
 * reply的实体类
 * id int(11) AI PK
 * post_id int(11)
 * user_id int(11)
 * father_id int(11)
 * reply_time timestamp
 * content text
 * status int(1) UN zerofill
 */
public class Reply {
    private int id;
    private int postId;
    private int userId;
    private int fatherId;
    private Date replyTime;
    private String content;
    private int status;

    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", postId=" + postId +
                ", userId=" + userId +
                ", fatherId=" + fatherId +
                ", replyTime=" + replyTime +
                ", content='" + content + '\'' +
                ", status=" + status +
                '}';
    }

    public int getId() {
        return id;
    }

    public Reply setId(int id) {
        this.id = id;
        return this;
    }

    public int getPostId() {
        return postId;
    }

    public Reply setPostId(int postId) {
        this.postId = postId;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Reply setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getFatherId() {
        return fatherId;
    }

    public Reply setFatherId(int fatherId) {
        this.fatherId = fatherId;
        return this;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public Reply setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Reply setContent(String content) {
        this.content = content;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Reply setStatus(int status) {
        this.status = status;
        return this;
    }
}

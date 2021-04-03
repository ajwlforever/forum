package com.ajwlforever.forum.entity;

import java.util.Date;

/**
 * 消息的实体类
 * author: ajwlforever
 * id int(11) AI PK
 * from_id int(11)
 * to_id int(11)
 * conversation_id varchar(45)
 * content text
 * status int(11)
 * create_time timestamp
 */
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String conversationId;
    private String content;
    private int status;
    private Date createTime;

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", fromId=" + fromId +
                ", toId=" + toId +
                ", conversationId='" + conversationId + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }

    public int getId() {
        return id;
    }

    public Message setId(int id) {
        this.id = id;
        return this;
    }

    public int getFromId() {
        return fromId;
    }

    public Message setFromId(int fromId) {
        this.fromId = fromId;
        return this;
    }

    public int getToId() {
        return toId;
    }

    public Message setToId(int toId) {
        this.toId = toId;
        return this;
    }

    public String getConversationId() {
        return conversationId;
    }

    public Message setConversationId(String conversationId) {
        this.conversationId = conversationId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Message setContent(String content) {
        this.content = content;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Message setStatus(int status) {
        this.status = status;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Message setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
}

package com.ajwlforever.forum.entity;

import java.util.Date;

/**
 *  帖子实体
 *  author：ajwlforever
 id int(11) AI PK
 user_id int(11)
 board_name varchar(50)
 title varchar(200)
 content longtext
 type int(2)
 level int(1)
 tags text
 status int(1)
 create_time timestamp
 reply_amount int(10) UN zerofill
 last_reply_time timestamp
 */
public class Post {
    private int id;
    private int userId;
    private String boardName;
    private String title;
    private String content;
    private int type;  //Post 的内容类型
    private int level;  //帖子的级别
    private String tags;
    private int status;  // 0-删除 1-正常状态 2-加精 3-置顶
    private Date createTime;
    private int replyAmount;  //初始为0
    private Date lastReplyTime;   //可以为空

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", userId=" + userId +
                ", boardName=" + boardName +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", level=" + level +
                ", tags='" + tags + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", replyAmount=" + replyAmount +
                ", lastReplyTime=" + lastReplyTime +
                '}';
    }

    public int getId() {
        return id;
    }

    public Post setId(int id) {
        this.id = id;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Post setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public String getBoardName() {
        return boardName;
    }

    public Post setBoardName(String boardName) {
        this.boardName = boardName;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Post setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Post setContent(String content) {
        this.content = content;
        return this;
    }

    public int getType() {
        return type;
    }

    public Post setType(int type) {
        this.type = type;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public Post setLevel(int level) {
        this.level = level;
        return this;
    }

    public String getTags() {
        return tags;
    }

    public Post setTags(String tags) {
        this.tags = tags;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Post setStatus(int status) {
        this.status = status;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Post setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public int getReplyAmount() {
        return replyAmount;
    }

    public Post setReplyAmount(int replyAmount) {
        this.replyAmount = replyAmount;
        return this;
    }

    public Date getLastReplyTime() {
        return lastReplyTime;
    }

    public Post setLastReplyTime(Date lastReplyTime) {
        this.lastReplyTime = lastReplyTime;
        return this;
    }
}

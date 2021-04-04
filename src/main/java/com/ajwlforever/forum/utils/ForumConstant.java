package com.ajwlforever.forum.utils;

/**
 * 存放整个平台的常量
 */

public interface ForumConstant {

    //  系统用户Id
    int SYSTEM_USERID = 1;
    /**
     * 激活成功
      */
    int activation_success = 0;
    /**
     * 重复激活
     */
    int activation_repeat = 1;
    /**
     * 激活失败
     */
    int activation_failed = 2;
    /*
    正常的到期时间
     */
    int DEFAULT_EXPIRED_TIME = 3600 * 12;

    /*
    长一点的
     */
    int REMEMBER_EXPIRED_TIME = 3600 * 24 * 7;
    /**
     * 帖子级别
     */
    //正常级别
    int POST_LEVEL_NORMAL = 1;
    /**
     * 帖子类容类型
     */
    //讨论 1
    int POST_TYPE_DISCUSS = 1;
    //提问  2
    int POST_TYPE_QUESTION = 2;
    //投票 3
    int POST_TYPE_POLL = 3;
    //相册 4
    int POST_TYPE_GALLERY = 4;
    //视频 5
    int POST_TYPE_VEDIO = 5 ;
    //其他 6
    int POST_TYPE_OTHER = 6;
    /**
     * 帖子的状态
     */
    // 0-删除 1-正常状态 2-加精 3-置顶
    int POST_STATUS_DELTE = 0;
    int POST_STATUS_NOMAL = 1;
    int POST_STATUS_ESSENCE = 2;

    /**
     * 分页显示
     */
     int PAGE_INDEX_LIMIT = 7;
     int PAGE_REPLY_LIMIT = 7;
     int PAGE_PROFILE_LIMIT = 10;
     int PAGE_MESSAGE_LIMIT = 10;
    /**
     * 回复的常量
     */
    //状态 0-正常 1-置顶 2-折叠 3-删除
    int REPLY_STATUS_NORMAL = 0;
    int REPLY_STATUS_TOP = 1;
    int REPLY_STATUS_FOLD = 2;
    int REPLY_STATUS_DELETE = 3;

    /**
     * 实体类型
     */
    // 1-用户 2-帖子 3-板块 4-回复
    int ENTITY_TYPE_USER = 1;
    int ENTITY_TYPE_POST = 2;
    int ENTITY_TYPE_BOARD = 3;
    int ENTITY_TYPE_REPLY = 4;
    /**
     * 消息状态
     */
    //0-未读;1-已读;2-删除;
    int MESSAGE_STATUS_UNREAD = 0;
    int MESSAGE_STATUS_READ = 1;
    int MESSAGE_STATUS_DELETE = 2;

    /**
     * 用户行为
     */
    //1-看帖 2-回复
    int USER_OPERATION_LOOK = 1;
    int USER_OPERATION_REPLY = 2;
    /**
     * 消息的类型
     */
    // 1-回复 2-关注 3-点赞
    String TOPIC_REPLY = "reply";
    String TOPIC_FOLLOW = "follow";
    String TOPIC_LIKE = "like";
    /**
     * 消息来自谁
     */
    // 1-来自登录用户 2-来自别人
    int FROM_WHO_SELF = 1;
    int FROM_WHO_OTHER = 2;
}

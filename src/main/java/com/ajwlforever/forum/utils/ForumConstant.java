package com.ajwlforever.forum.utils;

/**
 * 存放整个平台的常量
 */

public interface ForumConstant {
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

}

package com.ajwlforever.forum.utils;

/**
 * 用来存放redis中的key
 */
public class RedisKeyUtil {
    private static final String SPLIT = ":";
    private static final String PREFIX_TICKET = "ticket";

    public static String getTicketKey(String ticket){
        return PREFIX_TICKET+SPLIT+ticket;
    }
}

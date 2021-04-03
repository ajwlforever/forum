package com.ajwlforever.forum.utils;

/**
 * 用来存放redis中的key
 */
public class RedisKeyUtil {
    private static final String SPLIT = ":";
    private static final String PREFIX_TICKET = "ticket";
    private static final String PREFIX_LIKE = "like";
    private static final String PREFIX_DISLIKE = "dislike";
    private static final String PREFIX_FANS = "follower";
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_VIEW = "view";
    private static final String PREFIX_USER = "user";
    private static final String PREFIX_POST = "post";

    public static String getTicketKey(String ticket){
        return PREFIX_TICKET+SPLIT+ticket;
    }

    // like:entitytype:entityId -->  set(int)
    public static String getLikeKey(int entityType,int entityId){
        return PREFIX_LIKE+SPLIT+entityType+SPLIT+entityId;
    }
    //like:user:userId --> set(int)  收到赞的数量
    public static String getALlLikeCountKey(int userId) {
        return PREFIX_LIKE + SPLIT + "user" + SPLIT + userId;
    }
    // dislike:entitytype:entityId -->  set(int)
    public static String getDisLikeKey(int entityType,int entityId){
        return PREFIX_DISLIKE+SPLIT+entityType+SPLIT+entityId;
    }
    // dislike:user:userId --> set(int)  收到踩的数量
    public static String getALlDisLikeCountKey(int userId) {
        return PREFIX_DISLIKE + SPLIT + "user" + SPLIT + userId;
    }

    //关注者（某个实体粉丝） Fans follower:entityType:entityId
    public static String getFansKey(int entityType, int entityId){
        return PREFIX_FANS+SPLIT+entityType+SPLIT+entityId;
    }
    //关注了谁：followee:userId:entityType   -> zset(entityId,now) 按时间排序
    public static String getFolloweeKey(int entityType, int userId){
        return PREFIX_FOLLOWEE+SPLIT+userId+SPLIT+entityType;
    }
    //view:entityType:entityId : int   count计数
    public static String getViewCountKey(int entityType, int entityId){
        return PREFIX_VIEW+SPLIT+entityType+SPLIT+entityId;
    }
    //用户的足迹
    //view:userId:entityType:operation zset(postId)
    public static String getUserViewKey(int userId,int entityType,int userOp){
        return PREFIX_VIEW+SPLIT+userId+SPLIT+entityType+SPLIT+userOp;
    }
    //view:user:entityType:entityId   :  set(userId)
    public static String getViewUserKey(int entityType, int entityId){
        return PREFIX_VIEW+SPLIT+PREFIX_USER+SPLIT+entityType+SPLIT+entityId;
    }


}

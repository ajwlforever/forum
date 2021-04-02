package com.ajwlforever.forum.service;

import com.ajwlforever.forum.entity.User;
import com.ajwlforever.forum.utils.RedisKeyUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * Like的服务类
 * 点赞与点踩具体实现
 * author: ajwlforever
 */
@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    //点赞的 发起者和点赞的实体
    public void like(int userId, int entityType, int entityId, int entityUserId){
        //点赞
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                // 以likeKey为key 存放的是一个全是userId 的set， 意味着这些用户对likeKey这个实体点过赞
                String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
                String likeCountKey = RedisKeyUtil.getALlLikeCountKey(entityUserId);
                // 是否有人为这个实体点过赞，点过就取消赞，没点过要添加
                boolean isMember = redisTemplate.opsForSet().isMember(likeKey,userId);
                redisOperations.multi();   //开启事务
                if(isMember) {
                    //取消赞
                    redisTemplate.opsForSet().remove(likeKey,userId);
                    redisTemplate.opsForValue().decrement(likeCountKey);
                }else {
                    redisTemplate.opsForSet().add(likeKey,userId);
                    redisTemplate.opsForValue().increment(likeCountKey);
                }
                return redisOperations.exec();
            }
        });

    }
    // 查询某个实体的被点赞数量
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }
    //查询某人对某实体的点赞状态
    public int findEntityLikeStatus(User user, int entityType, int entityId) {
        if(user == null) return 0;
        String entityLikeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, user.getId()) ==true? 1:0;
    }
    // 查询用户的总被赞数量
    public long findUserLikeCount(int userId) {
        String userLikeCountKey = RedisKeyUtil.getALlLikeCountKey(userId);
        Integer res = (Integer)redisTemplate.opsForValue().get(userLikeCountKey);
        return  res== null? 0:res.intValue();
    }

    //点踩
    public void dislike(int userId, int entityType, int entityId, int entityUserId){
        //点踩
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                // 以likeKey为key 存放的是一个全是userId 的set， 意味着这些用户对likeKey这个实体点过赞
                String likeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
                String likeCountKey = RedisKeyUtil.getALlDisLikeCountKey(entityUserId);
                // 是否有人为这个实体点过赞，点过就取消赞，没点过要添加
                boolean isMember = redisTemplate.opsForSet().isMember(likeKey,userId);
                redisOperations.multi();   //开启事务
                if(isMember) {
                    //取消踩
                    redisTemplate.opsForSet().remove(likeKey,userId);
                    redisTemplate.opsForValue().decrement(likeCountKey);
                }else {
                    redisTemplate.opsForSet().add(likeKey,userId);
                    redisTemplate.opsForValue().increment(likeCountKey);
                }
                return redisOperations.exec();
            }
        });
    }
    // 查询某个实体的被点踩数量
    public long findEntityDisLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getDisLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }
    //查询某人对某实体的点踩状态
    public int findEntityDisLikeStatus(User user, int entityType, int entityId) {
        if(user == null) return 0;
        String entityLikeKey = RedisKeyUtil.getDisLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, user.getId()) ==true? 1:0;
    }
    // 查询用户的总被踩数量
    public long findUserDisLikeCount(int userId) {
        String userLikeCountKey = RedisKeyUtil.getALlDisLikeCountKey(userId);
        Integer res = (Integer)redisTemplate.opsForValue().get(userLikeCountKey);
        return  res== null? 0:res.intValue();
    }

}

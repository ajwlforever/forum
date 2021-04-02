package com.ajwlforever.forum.service;

import com.ajwlforever.forum.entity.User;
import com.ajwlforever.forum.utils.ForumConstant;
import com.ajwlforever.forum.utils.HostHolder;
import com.ajwlforever.forum.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.jmx.export.metadata.ManagedOperation;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 关注功能的具体实现类
 * author:ajwlfoever
 * 关注者（某个实体粉丝） Fans follower:entityType:entityId
 * 关注了谁：followee:userId:entityType   -> zset(entityId,now) 按时间排序
 */
@Service
public class FollowService implements ForumConstant {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;

    public void follow(int entityType, int entityId, int userId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations op) throws DataAccessException {
                String FansKey = RedisKeyUtil.getFansKey(entityType,entityId);
                String FolloweeKey = RedisKeyUtil.getFolloweeKey(entityType,userId);
                op.multi();
                // 用户关注了啥
                op.opsForZSet().add(FolloweeKey,entityId,System.currentTimeMillis());

                //某实体的粉丝+1
                op.opsForZSet().add(FansKey,userId,System.currentTimeMillis());


                op.exec();
                return null;
            }
        });
    }

    public void unfollow(int entityType, int entityId, int userId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations op) throws DataAccessException {
                String FansKey = RedisKeyUtil.getFansKey(entityType,entityId);
                String FolloweeKey = RedisKeyUtil.getFolloweeKey(entityType,userId);
                op.multi();
                // 用户关注了啥 -1
                op.opsForZSet().remove(FolloweeKey,entityId,System.currentTimeMillis());

                //某实体的粉丝-1
                op.opsForZSet().remove(FansKey,userId,System.currentTimeMillis());

                op.exec();
                return null;
            }
        });
    }

    //用户关注了多少实体
    public long findFolloweesCount(int userId,int entityType)
    {
        String FolloweeKey = RedisKeyUtil.getFolloweeKey(entityType,userId);
        return  redisTemplate.opsForZSet().zCard(FolloweeKey);
    }
    //某个实体有多少粉丝
    public long findFansCount(int entityType,int entityId){
        String fansKey = RedisKeyUtil.getFansKey(entityType,entityId);
        return redisTemplate.opsForZSet().zCard(fansKey);
    }
    //某个人是否关注了这个实体
    public boolean isFollow(User user, int entityType, int entityId){
        User hostUser = hostHolder.getUser();
        if(hostUser==null)
            return false;
        //查询
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityType,user.getId());
        return redisTemplate.opsForZSet().score(followeeKey,entityId) != null;
    }

    //得到某个用户关注的用户列表
    public List<Map<String, Object>> getFolloweeUserList(int userId, int offset, int limit){
        String followeeKey = RedisKeyUtil.getFolloweeKey(ENTITY_TYPE_USER,userId);
        User hostUser = hostHolder.getUser();
        //从redis取出所有用户关注的实体的id
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey,offset,offset+limit-1);
        if(targetIds == null){
            return null;
        }
        List<Map<String, Object>> res = new ArrayList<>();
        for(Integer id : targetIds)
        {
            User user = userService.selectById(id);
            if(user!=null)
            {
                //正确的userid
                Map<String, Object> map = new HashMap<>();
                //登录用户是否关注了这个用户
                map.put("isFollowd",isFollow(hostUser,ENTITY_TYPE_USER,id));
                map.put("user",user);
                map.put("followTime",redisTemplate.opsForZSet().score(followeeKey,id).longValue());
                res.add(map);
            }
        }

        return res;
    }

    public List<Map<String, Object>> getFansList(int userId,int offset,int limit){
        String fanKey = RedisKeyUtil.getFansKey(ENTITY_TYPE_USER,userId);
        User hostUser = hostHolder.getUser();
        //从redis取出所有用户关注的实体的id
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(fanKey,offset,offset+limit-1);
        if(targetIds == null){
            return null;
        }
        List<Map<String, Object>> res = new ArrayList<>();
        for(Integer id : targetIds)
        {
            User user = userService.selectById(id);
            if(user!=null)
            {
                //正确的userid
                Map<String, Object> map = new HashMap<>();
                //登录用户是否关注了这个用户
                map.put("isFollowd",isFollow(hostUser,ENTITY_TYPE_USER,id));
                map.put("user",user);
                map.put("followTime",redisTemplate.opsForZSet().score(fanKey,id).longValue());
                res.add(map);
            }
        }

        return res;
    }

}

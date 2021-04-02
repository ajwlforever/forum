package com.ajwlforever.forum.service;

import com.ajwlforever.forum.entity.User;
import com.ajwlforever.forum.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * 浏览人数的实现类
 *
 */
@Service
public class ViewService {

    @Autowired
    private RedisTemplate redisTemplate;

    // view 两种数据，1.用户是否阅读过 2.每次阅读都算在总数里
    public void viewEntity(User user,int entityType, int entityId){
        if(user == null) return;
        String viewKey = RedisKeyUtil.getViewCountKey(entityType,entityId);
        String viewUserkey = RedisKeyUtil.getViewUserKey(entityType,entityId);
        //直接+1
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {

                boolean isViewed = redisTemplate.opsForSet().isMember(viewUserkey,user.getId());
                redisOperations.multi();
                if(!isViewed){
                    redisTemplate.opsForSet().add(viewUserkey,user.getId());
                }
                redisTemplate.opsForValue().increment(viewKey);
                return redisOperations.exec();
            }
        });
    }

    public long getViewEntitycount(int entityType, int entityId){
        String viewKey = RedisKeyUtil.getViewCountKey(entityType,entityId);
        Integer res = (Integer)redisTemplate.opsForValue().get(viewKey);
        return  res== null? 0:res.intValue();
    }

    public int isViewed(User user, int entityType, int entityId){
        //用户不存在，没看过
        if(user==null) return 0;
        String viewUserkey = RedisKeyUtil.getViewUserKey(entityType,entityId);
        return redisTemplate.opsForSet().isMember(viewUserkey,user.getId()) == true? 1 : 0;
    }
    public long getEntityUserCount(int entityType, int entityId){
        String viewUserkey = RedisKeyUtil.getViewUserKey(entityType,entityId);
        return  redisTemplate.opsForSet().size(viewUserkey);
    }


}

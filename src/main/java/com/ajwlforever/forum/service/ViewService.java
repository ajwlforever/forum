package com.ajwlforever.forum.service;

import com.ajwlforever.forum.entity.Post;
import com.ajwlforever.forum.entity.User;
import com.ajwlforever.forum.utils.ForumConstant;
import com.ajwlforever.forum.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 浏览人数的实现类
 *
 */
@Service
public class ViewService implements ForumConstant {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PostService postService;

    // view 两种数据，1.用户是否阅读过 2.每次阅读都算在总数里        userOp 是用户的操作行为
    public void viewEntity(User user,int entityType, int entityId, int userOp){
        if(user == null) return;
        String viewKey = RedisKeyUtil.getViewCountKey(entityType,entityId);
        String viewUserkey = RedisKeyUtil.getViewUserKey(entityType,entityId);
        //对帖子+1
        //用户的足迹
        String UserViewkey = RedisKeyUtil.getUserViewKey(user.getId(), entityType, userOp);
        //直接+1
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {

                boolean isViewed = redisTemplate.opsForSet().isMember(viewUserkey,user.getId());
                redisOperations.multi();
                //用户访问这个实体
                redisTemplate.opsForZSet().add(UserViewkey,entityId,System.currentTimeMillis());

                if(!isViewed){
                    redisTemplate.opsForSet().add(viewUserkey,user.getId());
                }
                if(userOp != USER_OPERATION_REPLY){
                    redisTemplate.opsForValue().increment(viewKey);
                }

                return redisOperations.exec();
            }
        });
    }
    //得到用户的看/回复帖子足迹
    public List<Post> getUserViewPost(int userId,int op, int offset, int limit) {
        String UserViewKey = RedisKeyUtil.getUserViewKey(userId,ENTITY_TYPE_POST,op);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(UserViewKey,offset,offset+limit-1);
        if(targetIds == null){
            return null;
        }
        List<Post> res = new ArrayList<>();
        for(Integer id : targetIds){
            Post post = postService.selectByPostId(id);
            if(post != null)
                res.add(post);
        }
        return res;
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

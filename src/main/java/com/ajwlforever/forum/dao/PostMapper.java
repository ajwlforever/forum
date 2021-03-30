package com.ajwlforever.forum.dao;

import com.ajwlforever.forum.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 帖子的mapper
 * author: ajwlforever
 *
 * id int(11) AI PK
 *  user_id int(11)
 *  board_id int(11)
 *  title varchar(200)
 *  content longtext
 *  type int(2)
 *  level int(1)
 *  tags text
 *  status int(1)
 *  create_time timestamp
 *  reply_amount int(10) UN zerofill
 *  last_reply_time timestamp
 */
@Mapper
@Repository
public interface PostMapper {


    // 找所有帖子，userId为0是所有，>0是具体用户的帖子
    List<Post> selectAllPost(int userId,int offset, int limit);
    // 统计所有帖子的数量，userId为0是所有，>0是具体用户的帖子
    int selectPostRows(int userId);

    int insertPost(Post post);

    Post selectByPostId(int postId);

    //更新 帖子状态，回复数量，最后活跃时间
    int updateStatus(int postId,int status);
    int updateReplyAmount(int postId,int amount);
    int updateLastReplyTime(int postId, Date lastReplyTime);


}

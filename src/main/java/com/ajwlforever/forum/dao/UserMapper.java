package com.ajwlforever.forum.dao;

import com.ajwlforever.forum.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 *  user 的 Mapper
 *  author: ajwlforever
 */
@Mapper
@Repository
public interface UserMapper {

    int insertUser(User user);

    User selectById(int userId);

    User selectByName(String username);

    User selectByEmail(String email);

    int updateStatus(int userId, int status);

    int updateHeader(int userId, String headerUrl);

    int updatePassword(int userId, String password);

}

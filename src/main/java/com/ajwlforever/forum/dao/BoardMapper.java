package com.ajwlforever.forum.dao;


import com.ajwlforever.forum.entity.Board;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 板块的mapper
 * author: ajwlforever
 * id int(11) AI PK
 * board_name varchar(100)
 * board_desc varchar(500)
 */
@Mapper
@Repository
public interface BoardMapper {

    List<Board> selectAllBoards();
    Board selectByById(int id);
    Board selectByByName(String boardName);

    //插入板块
    int insertBoard(Board board);

    //修改
    int updateBoardName(int id,String boardName);
    int updateBoardDesc(int id,String boardDesc);
}

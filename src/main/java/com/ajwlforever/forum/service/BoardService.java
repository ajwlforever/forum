package com.ajwlforever.forum.service;


import com.ajwlforever.forum.dao.BoardMapper;
import com.ajwlforever.forum.entity.Board;
import com.ajwlforever.forum.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 板块服务
 * author: ajwlforever
 */
@Service
public class BoardService {
    @Autowired
    private BoardMapper boardMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;


    public List<Board> selectAllBoards(){
        return  boardMapper.selectAllBoards();
    }
    public Board selectByById(int id){
        return boardMapper.selectByById(id);
    }
    public Board selectByByName(String boardName){
        return boardMapper.selectByByName(boardName);
    }

    //插入板块
    public int insertBoard(Board board){
        board.setBoardDesc(sensitiveFilter.filter(board.getBoardDesc()));
        board.setBoardName(sensitiveFilter.filter(board.getBoardName()));
        return boardMapper.insertBoard(board);
    }

    //修改
    public int updateBoardName(int id,String boardName){
        return boardMapper.updateBoardName(id,boardName);
    }
    public int updateBoardDesc(int id,String boardDesc){
        return boardMapper.updateBoardDesc(id,boardDesc);
    }

}

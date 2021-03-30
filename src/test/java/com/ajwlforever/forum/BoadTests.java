package com.ajwlforever.forum;


import com.ajwlforever.forum.dao.BoardMapper;
import com.ajwlforever.forum.entity.Board;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 *  板块测试
 *  OK
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes =  ForumApplication.class)
public class BoadTests {

    @Autowired
    private BoardMapper boardMapper;

    @Test
    public void insertTest(){

        Board b1 = new Board().setBoardName("技术区").setBoardDesc("技术大神的所在地");
        Board b2 = new Board().setBoardName("数码区").setBoardDesc("数码设备的讨论地");
        Board b3 = new Board().setBoardName("游戏区").setBoardDesc("各种游戏");
        Board b4 = new Board().setBoardName("影视区").setBoardDesc("影视推荐与讨论");
        boardMapper.insertBoard(b1);
        boardMapper.insertBoard(b2);
        boardMapper.insertBoard(b3);
        boardMapper.insertBoard(b4);
    }

    @Test
    public void selectTest()
    {
        List<Board> boards = boardMapper.selectAllBoards();
        for(Board board : boards) System.out.println(board);
        Board b1 = boardMapper.selectByById(1);
        System.out.println(b1);
        b1 = boardMapper.selectByByName("技术区");
        System.out.println(b1);
    }

    @Test
    public  void updatetest()
    {
        Board b1 = boardMapper.selectByByName("技术区");
        System.out.println(b1);
        boardMapper.updateBoardDesc(b1.getId(),"建党一百周年，祝党生日快乐");
        boardMapper.updateBoardName(b1.getId(),"堂区");
        System.out.println(b1);
    }
}

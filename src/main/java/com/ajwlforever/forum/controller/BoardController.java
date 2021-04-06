package com.ajwlforever.forum.controller;

import com.ajwlforever.forum.entity.Board;
import com.ajwlforever.forum.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 板块 控制器
 * author： ajwlforevr
 */
@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/boards")
    public String getBoardsPage(Model model){
        List<Board> boards = boardService.selectAllBoards();
        model.addAttribute("boards",boards);

        return "/page-categories";
    }

    @GetMapping("/board/{boardName}")
    public String getBoardPage(@PathVariable("boardName") String boardName, Model model){
        return "/page-categories";
    }
}

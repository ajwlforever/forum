package com.ajwlforever.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页控制器
 * author：ajwlforever
 *
 */
@Controller
public class HomeController {

    @GetMapping({"/","/index"})
    public String getHome(Model model)
    {
        return "index";
    }
}

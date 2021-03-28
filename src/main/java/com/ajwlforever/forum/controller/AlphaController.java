package com.ajwlforever.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/alpha")
public class AlphaController {

    @GetMapping("/")
    public String getIndex()
    {
        return "/index";
    }
}

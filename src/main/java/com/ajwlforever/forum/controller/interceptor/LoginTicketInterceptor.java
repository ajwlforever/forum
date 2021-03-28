package com.ajwlforever.forum.controller.interceptor;

import com.ajwlforever.forum.entity.LoginTicket;
import com.ajwlforever.forum.entity.User;
import com.ajwlforever.forum.service.UserService;
import com.ajwlforever.forum.utils.CookieUtil;
import com.ajwlforever.forum.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor  implements HandlerInterceptor {

    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String ticket = CookieUtil.getCookie(request,"ticket");
        if( ticket!=null )
        {
            // LoginTicket
            LoginTicket loginTicket = userService.findLoginTicket(ticket);

            if(loginTicket!=null && loginTicket.getStatus()==0 && loginTicket.getExpired().after(new Date()))
            {
                //ticket is 有效
                User user = userService.selectById(loginTicket.getUserId());
                hostHolder.setUser(user);
                return true;
            }

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user!=null&&modelAndView!=null)
        {
            modelAndView.addObject("loginUser",user);
        }
    }
}

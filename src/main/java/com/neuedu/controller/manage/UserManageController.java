package com.neuedu.controller.manage;


import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/user")
public class UserManageController {

    @Autowired
    IUserService userService;
    /**
     *管理员登入
     */

    @RequestMapping(value = "login.do")
    public ServerResponse login(HttpSession session, String username, String password) {
        ServerResponse serverResponse = userService.login(username, password);
        if (serverResponse.isSuccess()) {//保存登入状态
            session.setAttribute(Const.CURRENTUSER, serverResponse.getData());
        }
        return serverResponse;
    }


}

package com.neuedu.controller;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/protal/user")
public class TestController {

    @Autowired
    IUserService userService;

    @RequestMapping(value = "/login.do")
//    public int login(@RequestParam(value = "username" ) String username,
//                          @RequestParam(value = "password" ) String password,
//                          @RequestParam(value = "email" ) String email,
//                          @RequestParam(value = "phone" ) String phone,
//                          @RequestParam(value = "question" ) String question,
//                          @RequestParam(value = "answer" ) String answer) {


    /*public int login( String username,
                           String password,
                          String email,
                           String phone,
                           String question,
                           String answer) {*/


        /*UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(password);
        userInfo.setEmail(email);
        userInfo.setPhone(phone);
        userInfo.setQuestion(question);
        userInfo.setAnswer(answer);

        userInfo.setRole(1);
        userInfo.setCreateTime(new Date());
        userInfo.setUpdateTime(new Date());*/

    public ServerResponse login(UserInfo userInfo){

        userInfo.setCreateTime(new Date());
        userInfo.setUpdateTime(new Date());
        userInfo.setRole(1);
        return userService.register(userInfo);

    }
}

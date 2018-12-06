package com.neuedu.service.impl;

import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public ServerResponse register(UserInfo userInfo) {

        int count = userInfoMapper.insert(userInfo);
        if (count>0){
            return ServerResponse.createServerResponseByError();

        }
        return ServerResponse.createServerResponseByError();
    }

    @Override
    public ServerResponse login(String username, String password) {

        //1.参数非空校验
        //普通方法
//        if (username==null||username.equals("")){

        //apache.commons包里封装的方法
        if (StringUtils.isBlank(username)){
            return ServerResponse.createServerResponseByError("用户名不能为空");
        }
//        if (password==null||password.equals("")){

        if (StringUtils.isBlank(password)){
            return ServerResponse.createServerResponseByError("密码不能为空");
        }
        //2.检查username是否存在
        int result= userInfoMapper.checkUsername(username);
        if (result<=0){
            return ServerResponse.createServerResponseByError("用户名不存在");
        }
        //3.根据用户名和密码查询

        UserInfo userInfo=userInfoMapper.selectUserByUsernameAndPassword(username,password);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError("密码错误");
        }
        //4.处理结果并返回

        userInfo.setPassword("");

        return ServerResponse.createServerResponseBySucess(null,userInfo);
    }
}

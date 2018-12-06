package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;

public interface IUserService {
    /**
     * 注册接口
     * @param userInfo
     * @return
     */
    public ServerResponse register(UserInfo userInfo);

    /**
     * 登入接口
     * @param username
     * @param password
     * @return
     */
    public ServerResponse login(String username,String password);
}

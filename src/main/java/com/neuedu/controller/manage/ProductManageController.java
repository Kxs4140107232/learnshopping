package com.neuedu.controller.manage;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.UserInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/product")
public class ProductManageController {

    /**
     *新增OR更新产品
     */

    public ServerResponse saveOrUpdate(HttpSession session, Product product){

        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        //判断用户权限
        if (userInfo.getRole()!=Const.USER_ROLE_ADMIN){
            return ServerResponse.createServerResponseByError(Const.ResponseCodeEnum.NO_PRIVILIGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILIGE.getDesc());

        }




        return null;

    }


}

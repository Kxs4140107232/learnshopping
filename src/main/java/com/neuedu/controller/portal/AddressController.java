package com.neuedu.controller.portal;


import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shopping;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IAddressService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/shopping")
public class AddressController {

    @Autowired
    IAddressService addressService;

    /**
     * 添加地址
     */
    @RequestMapping(value = "/add.do")
    public ServerResponse add(HttpSession session, Shopping shopping){
        System.out.println(shopping.getReceiverName());
        System.out.println(shopping.getReceiverAddress());
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError("需要登入");
        }
        return addressService.add(userInfo.getId(),shopping);
    }

    /**
     * 删除地址
     */
    @RequestMapping(value = "/del.do")
    public ServerResponse del(HttpSession session, Integer shoppingId){

        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError("需要登入");
        }
        return addressService.del(userInfo.getId(),shoppingId);
    }

    /**
     * 登入状态更新地址
     */
    @RequestMapping(value = "/update.do")
    public ServerResponse update(HttpSession session, Shopping shopping){

        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError("需要登入");
        }
        shopping.setUserId(userInfo.getId());
        return addressService.update(shopping);
    }


    /**
     * 选中查看具体地址
     */
    @RequestMapping(value = "/select.do")
    public ServerResponse select(HttpSession session, Integer shoppingId){

        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError("需要登入");
        }
        return addressService.select(shoppingId);
    }


    /**
     * 分页查询
     */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10")Integer pageSize){

        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null){
            return ServerResponse.createServerResponseByError("需要登入");
        }
        return addressService.list(pageNum,pageSize);
    }





}

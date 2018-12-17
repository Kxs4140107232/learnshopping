package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shopping;

public interface IAddressService {

    /**
     * 添加收货地址
     */

    public ServerResponse add(Integer userId, Shopping shopping);


    /**
     *删除收货地址
     */
    ServerResponse del(Integer userId,Integer shoppingId);

    /**
     * 更新
     */

    ServerResponse update( Shopping shopping);


    /**
     * 查看具体地址
     */

    ServerResponse select(Integer shoppingId);

    /**
     * 分页查询
     */

    ServerResponse list(Integer pageNum,Integer pageSize);





}

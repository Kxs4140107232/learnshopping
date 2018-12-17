package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ShoppingMapper;
import com.neuedu.pojo.Shopping;
import com.neuedu.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements IAddressService {

    @Autowired
    ShoppingMapper shoppingMapper;

    @Override
    public ServerResponse add(Integer userId, Shopping shopping){

        //1.参数非空
        if (shopping==null){
            return ServerResponse.createServerResponseByError("参数错误");
        }
        //2.添加
        shopping.setUserId(userId);
        shoppingMapper.insert(shopping);
        //3.返回结果
        Map<String,Integer> map= Maps.newHashMap();
        map.put("shoppingId",shopping.getId());
        return ServerResponse.createServerResponseBySuccess(map);

    }

    @Override
    public ServerResponse del(Integer userId, Integer shoppingId) {
        //1.参数非空
        if (shoppingId==null){
            return ServerResponse.createServerResponseByError("参数错误");
        }
        //2.删除
        int result=shoppingMapper.deleteByUserIdAndShoppingId(userId,shoppingId);
        //3.返回结果
        if (result>0){
            return ServerResponse.createServerResponseBySuccess();
        }
        return ServerResponse.createServerResponseByError("删除失败");
    }

    @Override
    public ServerResponse update(Shopping shopping) {

        //1.参数非空
        if (shopping==null){
            return ServerResponse.createServerResponseByError("参数错误");
        }
        //2.删除
        int result=shoppingMapper.updateBySelectiveKey(shopping);
        //3.返回结果
        if (result>0){
            return ServerResponse.createServerResponseBySuccess();
        }
        return ServerResponse.createServerResponseByError("更新失败");
    }

    @Override
    public ServerResponse select(Integer shoppingId) {

        //1.参数非空
        if (shoppingId==null){
            return ServerResponse.createServerResponseByError("参数错误");
        }

        Shopping shopping=shoppingMapper.selectByPrimaryKey(shoppingId);

        return ServerResponse.createServerResponseBySuccess(shopping);
    }

    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        List<Shopping>shoppingList=shoppingMapper.selectAll();
        PageInfo pageInfo = new PageInfo(shoppingList);

        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }


}

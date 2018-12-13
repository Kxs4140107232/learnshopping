package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {

    /**
     *
     * @param product
     * @return
     */
    ServerResponse saveOrUpdate(Product product);

    /**
     * 商品上下架
     * @param productId 商品id
     * @param status  商品状态
     * @return
     */
    ServerResponse set_sale_status(Integer productId,Integer status);

    /**
     * 商品详情
     */
    ServerResponse detail(Integer productId);

    /**
     * 后台-商品列表，分页
     */
    ServerResponse list(Integer pageNum,Integer pageSize);

    /**
     * 后台-搜索商品
     */

    ServerResponse search(Integer productId,String productName,Integer pageNum,Integer pageSize);

    /**
     * 图片上传
     */

    ServerResponse upload(MultipartFile file,String path);


}

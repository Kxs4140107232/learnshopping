package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.PayInfo;
import com.neuedu.pojo.Product;
import com.neuedu.service.IProductService;
import com.neuedu.util.DateUtils;
import com.neuedu.util.PropertiesUtils;
import com.neuedu.vo.ProductDetailVO;
import com.neuedu.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ServerResponse saveOrUpdate(Product product){
        //1.参数非空校验
        if (product==null){
            return ServerResponse.createServerResponseByError("参数为空");
        }
        //2.设置商品主图 sub_images --> 1.jpg,2.jpg,3.png
        String subImages=product.getSubImages();
        if (subImages!=null&&!subImages.equals("")){
            String[] subImageArr=subImages.split(",");
            if (subImageArr.length>0){
                //设置商品主图
                product.setMainImage(subImageArr[0]);
            }
        }
        //3.商品save or update

        if (product.getId()==null){
            //添加
            int result=productMapper.insert(product);
            if (result>0){
                return ServerResponse.createServerResponseBySuccess();
            }else {
                return ServerResponse.createServerResponseByError("添加失败");
            }
        }else{
            //更新
            int result=productMapper.updateByPrimaryKey(product);
            if (result>0){
                return ServerResponse.createServerResponseBySuccess();
            }else {
                return ServerResponse.createServerResponseByError("更新失败");
            }
        }

    }

    @Override
    public ServerResponse set_sale_status(Integer productId, Integer status) {
       //1.商品非空校验
        if (productId==null){
            return ServerResponse.createServerResponseByError("商品id参数不能为空");
        }
        if (status==null){
            return ServerResponse.createServerResponseByError("商品状态参数不能为空");
        }
       //2.更新商品状态
        Product product=new Product();
            product.setId(productId);
            product.setStatus(status);
            int result=productMapper.updateProductKeySelective(product);

       //3.返回结果

        if (result>0){
            return ServerResponse.createServerResponseBySuccess();
        }else {
            return ServerResponse.createServerResponseByError("更新失败");
        }

    }

    @Override
    public ServerResponse detail(Integer productId) {
       //1.参数非空
        if (productId==null){
            return ServerResponse.createServerResponseByError("商品id参数不能为空");
        }

       //2.根据商品id查询product
        Product product=productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.createServerResponseByError("商品不存在");

        }
        //3.product-->productDetailVO
        ProductDetailVO productDetailVO=assembleProductDetailVO(product);
       //4.返回结果

        return  ServerResponse.createServerResponseBySuccess(productDetailVO);
    }


    private ProductDetailVO assembleProductDetailVO(Product product){
        ProductDetailVO productDetailVO=new ProductDetailVO();
            productDetailVO.setCategoryId(product.getCategoryId());
            productDetailVO.setCreateTime(DateUtils.dateToStr(product.getCreateTime()));
            productDetailVO.setDetail(product.getDetail());
            productDetailVO.setImgeHost(PropertiesUtils.readByKey("imageHost"));
            productDetailVO.setName(product.getName());
            productDetailVO.setMainImage(product.getMainImage());
            productDetailVO.setId(product.getId());
            productDetailVO.setPrice(product.getPrice());
            productDetailVO.setStatus(product.getStatus());
            productDetailVO.setStock(product.getStock());
            productDetailVO.setSubImages(product.getSubImages());
            productDetailVO.setSubtitle(product.getSubtitle());
            productDetailVO.setUpdateTime(DateUtils.dateToStr(product.getUpdateTime()));
            Category category=categoryMapper.selectByPrimaryKey(product.getCategoryId());
            if (category!=null){
                productDetailVO.setParentCategoryId(category.getParentId());
            }else{
                //默认根节点
                productDetailVO.setParentCategoryId(0);
            }
        return productDetailVO;
    }

    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        //1.查询商品数据
        List<Product>productList=productMapper.selectAll();

        List<ProductListVO> productListVOList=Lists.newArrayList();
        if (productList!=null&&productList.size()>0){
            for (Product product:productList){

                ProductListVO productListVO=assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo=new PageInfo(productListVOList);
        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }

    private ProductListVO assembleProductListVO(Product product){
        ProductListVO productListVO=new ProductListVO();
         productListVO.setId(product.getId());
         productListVO.setCategoryId(product.getCategoryId());
         productListVO.setMainImage(product.getMainImage());
         productListVO.setName(product.getName());
         productListVO.setPrice(product.getPrice());
         productListVO.setStatus(product.getStatus());
         productListVO.setSubtitle(product.getSubtitle());

         return productListVO;
    }

    @Override
    public ServerResponse search(Integer productId, String productName,
                                 Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum,pageSize);

        if (productName!=null&&productName.equals("")){
            productName="%"+productName+"%";
        }
        List<Product> productList=productMapper.findProductByProductIdAndProductName(productId,productName);

        List<ProductListVO> productListVOList=Lists.newArrayList();
        if (productList!=null&&productList.size()>0){
            for (Product product:productList){
                ProductListVO productListVO=assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }

        PageInfo pageInfo=new PageInfo(productListVOList);
        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }

    @Override
    public ServerResponse upload(MultipartFile file, String path) {

        if (file==null){
            return ServerResponse.createServerResponseByError();
        }
        //1.获取图片名
        String originalFileName=file.getOriginalFilename();
        //获取图片的扩展名
        String exName=originalFileName.substring(originalFileName.lastIndexOf("."));
        //为图片生成唯一的名字
        String newFileName= UUID.randomUUID().toString()+exName;

        File pathFile=new File(path);
        if (!pathFile.exists()){
            pathFile.setWritable(true);
            pathFile.mkdirs();
        }

        File file1=new File(path,newFileName); //1 l 一和L

        try {
            file.transferTo(file1);
            //上传图片到服务器
            //
            Map<String,String>map= Maps.newHashMap();
            map.put("uri",newFileName);
            map.put("url",PropertiesUtils.readByKey("imageHost"+"/"+newFileName));
            return ServerResponse.createServerResponseBySuccess(map);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}

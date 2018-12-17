package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.*;
import com.neuedu.pojo.*;
import com.neuedu.service.IOrderService;
import com.neuedu.util.BigDecimalUtils;
import com.neuedu.util.DateUtils;
import com.neuedu.util.PropertiesUtils;
import com.neuedu.vo.CartOrderItemVO;
import com.neuedu.vo.OrderItemVO;
import com.neuedu.vo.OrderVO;
import com.neuedu.vo.ShoppingVO;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    ShoppingMapper shoppingMapper;

    @Override
    public ServerResponse createOrder(Integer userId, Integer shoppingId) {

        //1.参数非空校验
        if (shoppingId == null) {
            ServerResponse.createServerResponseByError("参数为空");
        }
        //2.根据userId查询购物车中已选中的商品-》List<Cart>
        List<Cart> cartList = cartMapper.findCartListByUserIdAndChecked(userId);

        //3.List<Cart>-->List<OrderItem>
        ServerResponse serverResponse = getCartOrderItem(userId, cartList);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }


        //4.创建订单order并将其保存到数据库
        //计算订单价格
        BigDecimal orderTotalPrice = new BigDecimal("0");
        List<OrderItem> orderItemList = (List<OrderItem>) serverResponse.getData();
        if (orderItemList == null || orderItemList.size() == 0) {
            return ServerResponse.createServerResponseByError("购物车为空");
        }
        orderTotalPrice = getOrderPrice(orderItemList);
        Order order = createOrder(userId, shoppingId, orderTotalPrice);
        if (order==null){
            return ServerResponse.createServerResponseByError("订单创建失败");
        }

        //5.将List<OrderItem>保存到数据库
        for (OrderItem orderItem:orderItemList){
            orderItem.setOrderNo(order.getOrderNo());
        }
        //批量插入
        orderItemMapper.insertBatch(orderItemList);
        //6.扣库存
        reduceProductStock(orderItemList);
        //7.购物车中清空已下单商品
        cleanCart(cartList);
        //8.返回，OrderVO
        OrderVO orderVO=assembleOrderVO(order,orderItemList,shoppingId);


        return ServerResponse.createServerResponseBySuccess(orderVO);
    }

    @Override
    public ServerResponse cancel(Integer userId, Long orderNo) {

        //1.参数非空校验
        if (orderNo==null){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //2.根据userId和orderNo查询订单
        Order order=orderMapper.findOrderByUserIdAndOrderNo(userId,orderNo);
        if (order==null){
            return ServerResponse.createServerResponseByError("订单不存在");
        }
        //3.判断订单状态并取消
        if (order.getStatus()!=Const.OrderStatusEnum.ORDER_UN_PAY.getCode()){
            return ServerResponse.createServerResponseByError("订单不可取消");
        }
        //4.返回结果
        order.setStatus(Const.OrderStatusEnum.ORDER_CANCELED.getCode());
        int result=orderMapper.updateByPrimaryKey(order);
        if (result>0){
            return ServerResponse.createServerResponseBySuccess();
        }
        return ServerResponse.createServerResponseByError("订单取消失败");



    }

    @Override
    public ServerResponse get_order_cart_product(Integer userId) {

        //1.查询购物车
        List<Cart> cartList=cartMapper.findCartListByUserIdAndChecked(userId);
        //2.List<Cart>-->List<OrderItem>
        ServerResponse serverResponse=getCartOrderItem(userId,cartList);
        if (serverResponse.isSuccess()){
            return serverResponse;
        }
        //3.组装vo
        CartOrderItemVO cartOrderItemVO=new CartOrderItemVO();
        cartOrderItemVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        List<OrderItem>orderItemList=(List<OrderItem>)serverResponse.getData();
        List<OrderItemVO>orderItemVOList=Lists.newArrayList();
        if (orderItemList==null||orderItemList.size()==0){
            return ServerResponse.createServerResponseByError("购物车空");
        }
        for (OrderItem orderItem:orderItemList){
            orderItemVOList.add(assembleOrderItemVO(orderItem));
        }
        cartOrderItemVO.setOrderItemVOList(orderItemVOList);
        cartOrderItemVO.setTotalPrice(getOrderPrice(orderItemList));
        //4.返回结果

        return ServerResponse.createServerResponseBySuccess(cartOrderItemVO);
    }

    @Override
    public ServerResponse list(Integer userId,Integer pageNum,Integer pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList=Lists.newArrayList();
        if (userId==null){
            //查询所有
            orderList=orderMapper.selectAll();
        }else {//查询当前用户
            orderList=orderMapper.findOrderByUserId(userId);

        }
        if (orderList==null||orderList.size()==0){
            return ServerResponse.createServerResponseByError("未查询到");
        }
        List<OrderVO> orderVOList=Lists.newArrayList();
        for (Order order :orderList){
            List<OrderItem> orderItemList=orderItemMapper.findOrderItemByOrderNo(order.getOrderNo());
                OrderVO orderVO=assembleOrderVO(order,orderItemList,order.getShoppingId());
                orderVOList.add(orderVO);
        }

        PageInfo pageInfo=new PageInfo(orderVOList);
        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }

    @Override
    public ServerResponse detail(Long orderNo) {
        //1.参数非空校验
        if (orderNo==null){
            return ServerResponse.createServerResponseByError("参数不能为空");
        }

        //2.查询订单
        Order order=orderMapper.findOrderByOrderNo(orderNo);
        if (order==null){
            return ServerResponse.createServerResponseByError("订单不存在");
        }

        //3.获取ordervo
        List<OrderItem> orderItemList=orderItemMapper.findOrderItemByOrderNo(order.getOrderNo());
        OrderVO orderVO=assembleOrderVO(order,orderItemList,order.getShoppingId());

        //4.返回结果
        return ServerResponse.createServerResponseBySuccess(orderVO);

    }

    private OrderVO assembleOrderVO(Order order,List<OrderItem> orderItemList,Integer shoppingId){

        OrderVO orderVO=new OrderVO();

        List<OrderItemVO> orderItemVOList=Lists.newArrayList();
        for (OrderItem orderItem:orderItemList){
            OrderItemVO orderItemVO=assembleOrderItemVO(orderItem);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        orderVO.setImageHost(PropertiesUtils.readByKey("ImageHost"));
        Shopping shopping=shoppingMapper.selectByPrimaryKey(shoppingId);
        if (shopping!=null){
            orderVO.setShoppingId(shoppingId);
            ShoppingVO shoppingVO=assmbleShoppingVO(shopping);
            orderVO.setShoppingVO(shoppingVO);
            orderVO.setReceiverName(shopping.getReceiverName());
        }
        orderVO.setStatus(order.getStatus());
        Const.OrderStatusEnum orderStatusEnum=Const.OrderStatusEnum.codeOf(order.getStatus());
        if (orderStatusEnum!=null){
            orderVO.setStatusDesc(orderStatusEnum.getDesc());
        }
        orderVO.setPostage(0);
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentType(order.getPaymentType());
        Const.PaymentEnum paymentEnum=Const.PaymentEnum.codeOf(order.getPaymentType());
        if (paymentEnum!=null){
            orderVO.setPaymentTypeDesc(paymentEnum.getDesc());
        }
        orderVO.setOrderNo(order.getOrderNo());


        return orderVO;
    }

    /**
     *
     */

    private ShoppingVO assmbleShoppingVO(Shopping shopping){
        ShoppingVO shoppingVO=new ShoppingVO();

        if (shopping!=null){
            shoppingVO.setReceiverAddress(shopping.getReceiverAddress());
            shoppingVO.setReceiverCity(shopping.getReceiverCity());
            shoppingVO.setReceiverDistrict(shopping.getReceiverDistrict());
            shoppingVO.setReceiverMobile(shopping.getReceiverMobile());
            shoppingVO.setReceiverName(shopping.getReceiverName());
            shoppingVO.setReceiverPhone(shopping.getReceiverPhone());
            shoppingVO.setReceiverProvince(shopping.getReceiverProvince());
            shoppingVO.setReceiverZip(shopping.getReceiverZip());

        }
        return  shoppingVO;
    }





    /**
     *
     */

    private OrderItemVO assembleOrderItemVO(OrderItem orderItem){
        OrderItemVO orderItemVO=new OrderItemVO();
        if (orderItem!=null){
            orderItemVO.setQuantity(orderItem.getQuantity());
            orderItemVO.setCreateTime(DateUtils.dateToStr(orderItem.getCreateTime()));
            orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
            orderItemVO.setOrderNo(orderItem.getOrderNo());
            orderItemVO.setProductId(orderItem.getProductId());
            orderItemVO.setProductImage(orderItem.getProductImage());
            orderItemVO.setProductName(orderItem.getProductName());
            orderItemVO.setToatlPrice(orderItem.getTotalPrice());
        }
        return orderItemVO;
    }






    /**
     * 清空购物车中已选择的商品
     */

    private void cleanCart (List<Cart> cartList){
        if (cartList!=null&&cartList.size()>0){
            cartMapper.batchDelete(cartList);
        }
    }



    /**
     * 扣库存
     */
    private void reduceProductStock(List<OrderItem> orderItemList){

        if (orderItemList!=null&&orderItemList.size()>0){
            for (OrderItem orderItem : orderItemList){
                Integer productId=orderItem.getProductId();
                Integer quantity=orderItem.getQuantity();
                Product product=productMapper.selectByPrimaryKey(productId);
                product.setStock(product.getStock()-quantity);
                productMapper.updateByPrimaryKey(product);
            }
        }




    }




    /**
     * 计算订单价格
     */

    private BigDecimal getOrderPrice(List<OrderItem> orderItemList) {
        BigDecimal bigDecimal = new BigDecimal("0");

        for (OrderItem orderItem : orderItemList) {
            bigDecimal = BigDecimalUtils.add(bigDecimal.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }

        return bigDecimal;

    }

    /**
     * 创建订单
     */

    private Order createOrder(Integer userId, Integer shoppingId, BigDecimal orderTotalPrice) {

        Order order = new Order();
        order.setOrderNo(generateOrderNO());
        order.setUserId(userId);
        order.setShoppingId(shoppingId);
        order.setStatus(Const.OrderStatusEnum.ORDER_UN_PAY.getCode());
        //订单金额
        order.setPayment(orderTotalPrice);
        order.setPostage(0);
        order.setPaymentType(Const.PaymentEnum.ONLINE.getCode());

        //保存订单
        int result = orderMapper.insert(order);
        if (result > 0) {
            return order;
        }

        return null;
    }

    /**
     * 生成订单编号
     */
    private Long generateOrderNO() {
        return System.currentTimeMillis() + new Random().nextInt(100);
    }


    private ServerResponse getCartOrderItem(Integer userId, List<Cart> cartList) {

        if (cartList == null || cartList.size() == 0) {
            return ServerResponse.createServerResponseByError("购物车为空");
        }

        List<OrderItem> orderItemList = Lists.newArrayList();

        for (Cart cart : cartList) {

            OrderItem orderItem = new OrderItem();
            orderItem.setUserId(userId);
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            if (product == null) {
                return ServerResponse.createServerResponseByError("id为" + cart.getProductId() + "的商品不存在");
            }
            if (product.getStatus() != Const.ProductStatusEnum.PRODUCT_ONLINE.getCode()) {
                //商品下架
                return ServerResponse.createServerResponseByError("id为" + product.getId() + "的商品已经下架");
            }
            if (product.getStock() < cart.getQuantity()) {
                //库存不足
                return ServerResponse.createServerResponseByError("id为" + product.getId() + "的商品库存不足");
            }

            orderItem.setQuantity(cart.getQuantity());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setProductId(product.getId());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setProductName(product.getName());
            orderItem.setTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(), cart.getQuantity().doubleValue()));

            orderItemList.add(orderItem);
        }
        return ServerResponse.createServerResponseBySuccess(orderItemList);


    }

}

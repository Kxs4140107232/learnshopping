# 测试分支Dev
## git命令
### git init		创建仓库
### git add name 添加到暂存区
### git commit -m "" 清空暂存区
### git status		查看暂存区状态
### git log			查看提交committed
### git reset --hard committed	版本回退
### git branch		查看分支
### git checkout -b dev	创建并切换到Dev分支
### git checkout 分支名	切换分支
### git pull		拉取
### git remote add origin git@github.com:yourname/仓库名.git 	关联
### git push -u origin master	提交到远程仓库
### git merge branchname（分支名）	分支合并
### git push origin HEAD -u	本地分支提交到远端
### git fetch		拉取远端分支
## 项目需求
### 核心-购买
#### 一、用户模块
##### 登入
##### 注册
##### 忘记密码
##### 修改密码
##### 获取用户信息
##### 退出登入
#### 二、商品模块
##### 后台
###### 添加商品
###### 修改商品
###### 删除商品
###### 商品上下架
###### 查看商品
##### 前台（用户）
###### 搜索商品
###### 查看商品详情
#### 三、类别模块
###### 添加类别
###### 修改类别
###### 删除类别
##### 查看类别
###### 查看子类
###### 查看后代类
#### 四、购物车模块
###### 添加到购物车
###### 修改购物车中某商品数量
###### 删除购物车商品
###### 全选/取消全选
###### 单选/取消单选
###### 查看购物车中商品数量
#### 五、地址模块
###### 添加地址
###### 修改地址
###### 删除地址
###### 查看地址
#### 六、订单模块
##### 前台
###### 下订单
###### 查看订单
###### 取消订单
###### 订单详情
##### 后台
######  订单列表
###### 订单详情
###### 已发货
###### 未发货
#### 七、支付模块
##### 支付宝支付
###### 支付
###### 支付回调
###### 查看支付状态
#### 八、线上部署
##### 阿里云部署
------20181204---------
### 远程分支合并Dev分支
```$xslt
    git checkout dev
    git pull origin dev
    git checkout master
    git merge dev
    git push origin master
```
## 数据库设计
###创建数据库
```$xslt
    create database learnshopping;
     user learnshopping;
```
 
### 用户表
```
create table neuedu_user(
`id`        int(11)     not null    auto_increment   comment '用户id',   
`username`  varchar(50) not null    comment '用户名',
`password`  varchar(50) not null    comment '密码',
`email`     varchar(50) not null    comment '邮箱',
`phone`     varchar(11) not null    comment '联系方法',
`question`  varchar(100)    not null    comment '密保问题',
`answer`    varchar(100)    not null    comment '答案',
`role`      int(4)          not null    default 0   comment '用户角色 0：普通用户，1：管理员',
`create_time`   datetime    comment '创建时间',
`update_time`   datetime    comment '修改时间',
 primary key(`id`),
 UNIQUE KEY `user_name_index`(`username`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;
```
### 类别表
```
create table neuedu_category(
`id`        int(11)     not null    auto_increment   comment '类别id',
`parent_id` int(11)     not null    default 0   comment '父类id',
`name`      varchar(50) not null    comment '类别名称',
`status`    int(4)      default 1   comment '类别状态 1:正常 0:废弃',
`create_time`   datetime    comment '创建时间',
`update_time`   datetime    comment '修改时间',
 primary key(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;
```


### 商品表
```$xslt
create table neuedu_product(
`id`            int(11)         not null            auto_increment   comment '商品id',
`category_id`   int(11)         not null             comment '商品所属的类别id，值引用类别表的id',
`name`          varchar(100)    not null            comment '商品名称',
`detail`        text                                comment '商品详情',
`subtitle`      varchar(200)                         comment '商品副标题',
`main_image`    varchar(200)                        comment '商品主图',
`sub_images`    varchar(200)                        comment '商品子图',
`price`         decimal(20,2)   not null            comment '商品价格，总共20位，小数2位，整数18位',
`stock`         int(11)                             comment '商品库存',
`status`        int(6)          default 1            comment '商品状态 1:在售 2:下架 3:删除',
`create_time`   datetime                              comment '创建时间',
`update_time`   datetime                            comment '修改时间',
 primary key(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;

```
### 购物车表
```
create table neuedu_cart(
`id`            int(11)     not null    auto_increment   comment '购物车id',
`user_id`       int(11)     not null    comment '用户id',
`product_id`    int(11)     not null    comment '商品id',
`quantity`      int(11)     not null    comment '购买数量',
`checked`       int(4)      default 1   comment '1：选中 0：未选中',
`create_time`   datetime                 comment '创建时间',
`update_time`   datetime                comment '修改时间',
 primary key(`id`),
 key `user_id_index` (`user_id`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;
```

### 订单表
```
create table neuedu_order(
`id`          int(11)     not null    auto_increment   comment '订单id，主键',
`order_no`    bigint(20)  not null    comment '订单编号',
`user_id`     int(11)     not null    comment '用户id',
`payment`     decimal(20,2)   not null    comment '付款总金额，单位元，保留两位小数',
`payment_type`    int(4)  not null    default 1   comment '支付方式  1：线上支付',
`status`      int(4)      not null    comment '订单状态  0-已取消  10-未付款  20-已付款  30-已发货  40-已完成  50-已关闭',          
`shopping_id` int(11)     not null    comment '收货地址id',
`postage`     int(10)     not null    default 0   comment '运费',
`payment_time`    datetime    default null    comment '付款时间',
`send_time`      datetime    default null    comment '发货时间',
`close_time`        datetime    default null    comment '关闭时间',
`end_time`      datetime    default null    comment '结束时间',
`create_time`   datetime                 comment '创建时间',
`update_time`   datetime                comment '修改时间',
 primary key(`id`),
 unique key `order_no_index` (`order_no`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;
```

### 订单明细表
```
create table neuedu_order_item(
`id`            int(11)     not null    auto_increment   comment '订单明细id，主键',
`order_no`      bigint(20)  not null    comment '订单编号',
`user_id`       int(11)     not null    comment '用户id',
`product_id`    int(11)     not null    comment '商品id',
`product_name`  varchar(100)    not null            comment '商品名称',
`product_image` varchar(100)            comment '商品主图',
`current_unit_price`    decimal(20,2)   not null    comment '商品价格，总共20位，小数2位，整数18位',
`quantity`      int(10)     not null    comment '商品的购买数量',
`total_price`   decimal(20,2)   not null    comment '商品总价格，单位元，保留两位小数',
`create_time`     datetime default null comment '已创建时间',
`update_time`    datetime default null comment '更新时间',
 primary key(`id`),
 key `order_no_index` (`order_no`) USING BTREE,
 key `order_no_user_id_index` (`order_no`,`user_id`)USING BTREE
 )ENGINE=InnoDB DEFAULT CHARSET=UTF8;
```
### 支付表
```
 create table neuedu_payinfo(
 `id`       int(11)     not null    auto_increment  comment '主键',
 `user_id`  int(11)     not null    comment '订单编号',
 `order_no` bigint(20)  not null    comment '用户id',
 `pay_platform`  int(4) not null    default 1   comment '1：支付宝 2：微信',   
 `platform_status`  varchar(50) comment '支付状态',
 `platform_number`  varchar(100)    comment '流水号',
 `create_time`     datetime default null comment '已创建时间',
 `update_time`    datetime default null comment '更新时间',
  primary key(`id`)
 )ENGINE=InnoDB DEFAULT CHARSET=UTF8;
```


### 收货地址表
```
create table neuedu_shopping(
 `id`               int(11)         not null    auto_increment,
 `user_id`          int(11)         not null,
 `receiver_name`    varchar(20)     default     null    comment '收货姓名',
 `receiver_phone`   varchar(20)     default     null    comment '收货固定电话',
 `receiver_mobile`  varchar(20)     default     null    comment '收货移动电话',
 `receiver_province`varchar(20)     default     null    comment '省份',
 `receiver_city`    varchar(20)     default     null    comment '城市',
 `receiver_district`varchar(20)     default     null    comment '区/县',
 `receiver_address` varchar(20)     default     null    comment '详细地址',
 `receiver_zip`     varchar(20)     default     null    comment '邮编',
 `create_time`      datetime      not null   comment '已创建时间',
 `update_time`      datetime         not null   comment '更新时间',
  primary key(`id`)
 )ENGINE=InnoDB DEFAULT CHARSET=UTF8;
```    
###数据库小结
索引(index):唯一索引unique，组合索引，单索引
        位置：用户表，购物车表，订单表，订单明细表




 ### 项目架构
 ```
 视图层
 控制层controler
 业务逻辑层service
        接口和实现类
 Dao层
 
```
### mybatis-generator插件(mybatis逆向代理工程)
####遇见错误：db.properties中url地址拼写错误



###搭建ssm框架
    1.pom.xml
    2.添加配置文件
      spring配置文件
      springMVC配置文件
      mybatis配置文件
      web.xml
    3.使用框架
####遇到的错误(全都是xml文件配置问题)
##### 1.spring.xml中最开始的地址未删减清楚
##### 2.spring.xml中c3p0连接池中的数据未改
##### 3.springmvc.xml里的连接器没删除


###接口
####构造方法参数的顺序与后期调用其方法的参数的顺序必须相同


###服务端接口返回前端的统一对象（封装内容）
    class ServerResponse<T>{
        int status;//接口返回状态码
        T data;//接口返回的数据
        String msg;//封装错误信息
    }

###类别模块
####1.功能介绍
        获取节点
        增加节点
        修改名称
        获取分类
        递归子节点id
####2.学习目标
        如何设计及封装无限层级的树状数据结构
        递归算法的设计思想
        如何处理复杂对象重排
        重写hashcode和equals的注意事项
####3.类别模块遇到的问题
        1.封装的内容（SeverResponse）里少一个构造方法导致泛型<T>的data与string类型的status不可转换
        2.控制层requestMapping里的地址重复，服务器报500
          “Caused by: java.lang.IllegalStateException: Ambiguous mapping. Cannot map 'c”
          
          
###商品模块
####1功能：分为前台和后台
#####前台：
        产品搜索
        动态排序列表
        商品详情
#####后端：
        商品列表
        商品搜索
        图片上传
        富文本上传
        商品详情
        商品上下架
        增加商品
        更新商品
####2.学习目标
        FTP服务的对接
        SpringMVC文件上传
        流读取Properties配置文件
        抽象POJO、BO、VO对象之间的转换关系及解决思路
        joda-time快速入门
        静态块
        Mbaties-PageHelper高效准确的分页及动态排序
        Mbaties对List遍历的实现方法
        Mbaties对where语句动态拼接
        POJO/BO business object/VO view object
        POJO/VO
        
####3.商品模块遇到问题
        （1）返回值类型一般为第二发单词，不但在被调用的方法里要设置，本类也要设置
          返回String型：public Sring
          返回ServerResponse型：public ServerResponse
         (2)图片上传不了，未解决
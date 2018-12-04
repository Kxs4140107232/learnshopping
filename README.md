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
`role`      int(4)          not null    default 0   comment '用户角色',
`create_time`   datetime    comment '创建时间',
`update_time`   datetime    comment '修改时间',
 primary key(`id`),
 UNIQUE KEY `user_name_index`(`username`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;
```
### 类别表
### 商品表
### 购物车表
### 订单表
### 订单明细表

create table neuedu_order_item(
`id`
`order_no`
`user_id`
`product_id`
`product_name`
`product_image`
`current_unit_price`
`quantity`
`total_price`
`create_time`     datetime default null comment ''
`update_time`    datetime default null comment ''
 PRIMARY KEY(`id`),
 
 
)ENGINE=InnoDB DEFAULT CHARSET=UTF8

### 支付表
 create table neuedu_payinfo(
 `id`       int(11)     not null    auto_increment  comment ''
 `user_id`
 `order_no`
 `pay_platform`     
 `platform_status`  varchar(50) comment ''
 `platform_number`
 `create_time`
 `update_time`
 )ENGINE=InnoDB DEFAULT CHARSET=UTF8



### 收货地址表
    


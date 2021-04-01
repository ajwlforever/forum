# AXForum AX论坛的设计与实现



## 一.系统数据流图

### 1.第0层数据流图

![image-20210328121540076](img%5Cimage-20210328121540076.png)

### 2.中间层数据流图

![image-20210328121625180](img%5Cimage-20210328121625180.png)



## 二、数据库设计



## 三、登录注册功能的实现

### 1.注册

在前端界面点击登录先判断所有参数不会出现基本错误，之后ajax传递参数到服务器端进行注册服务，服务结束返回参数。注册成功，则发送一封激活邮件到用户邮箱，失败，则返回失败参数。

![image-20210328062130629](img%5Cimage-20210328062130629.png)





### 2.登录

登录模块的核心是登录用户与登录凭证

![image-20210328062031565](img%5Cimage-20210328062031565.png)

登录模块的核心在于，在服务器持久存放登录用户保证多界面切换，任然保持登录，和离线后再登录凭借登录凭证再次保持登录。

## 四、个人功能

## 五、帖子回复相关功能

## 六、对实体的操作

> 实体分别为用户，帖子，回复，板块
>
> 对实体的操作包括：点赞，点踩，关注，分享，浏览（not all）

用redis存储这些操作产生的数据

- 点赞: like:entitytype:entityId -->  set(int)

  - like:user:userId --> set(int)  收到赞的数量 

- 点踩: dislike:entitytype:entityId -->  set(int)

  - dislike:user:userId --> set(int)  收到赞的数量 

-	关注:

	-	关注者（某个实体粉丝）：follower:entityType:entityId 
	-	关注了谁：followee:userId:entityType   -> zset(entityId,now) 按时间排序
	
-	浏览：view:entityType:entityId : int

  
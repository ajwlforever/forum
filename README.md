# AXForum AX论坛的设计与实现



## 一.系统数据流图

### 1.第0层数据流图

![image-20210328121540076](img%5Cimage-20210328121540076.png)

### 2.中间层数据流图

![image-20210328121625180](img%5Cimage-20210328121625180.png)



## 二、数据库设计



## 三登录注册功能的实现

### 1.注册

在前端界面点击登录先判断所有参数不会出现基本错误，之后ajax传递参数到服务器端进行注册服务，服务结束返回参数。注册成功，则发送一封激活邮件到用户邮箱，失败，则返回失败参数。

![image-20210328062130629](img%5Cimage-20210328062130629.png)





### 2.登录

登录模块的核心是登录用户与登录凭证

![image-20210328062031565](img%5Cimage-20210328062031565.png)


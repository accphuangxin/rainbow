=====================================================================
Rainbow 1.5.8-RELEASE 2015-08-13
1:增加：SoaManger类中添加invokeNoTx(), 此方法直接获取spring ioc对象进行调用，不开启数据库事务。在service中，如果不需要数据库
操作的方法，直接使用invokeNoTx()方法，可以提升性能。
2:去掉rainbow-common包中的log包,日志全部使用log4j2。分布式日志将在rainbow-plugin加入rainbow-plugin-logger
4:还源springmvc 序列化从fastjson 走jackjson。原因在于fastjson返回为一个json字符串及，通过ajax绑定表格数据时还是走jackjson

=====================================================================
Rainbow 1.5.7-RELEASE
1:升级spring4.1.6
2:升级mybatis3.2.8
3:升级mybatis-spring1.2.2
4:去掉springmvc的jackjson 改为fastjson 修改相应的spring-mvc.xml 修改web.xml的 字符编码配置、spring mvc 拦截器配置
=====================================================================
Rainbow 1.5.6-RELEASE
1:对Dao 添加多数据源方法,默认的值为applicationContext-db.xml中配置的SQL模板为sqlSessionTemplate
=====================================================================
Rainbow 1.5.5-RELEASE
1:对RainbowContext类 添加clone(); deepClone();方法
====================================================================
Rainbow 1.5.4-RELEASE 
1:对Rainbow-common包进行拆分,形成插件模式，从Rainbow-common迁出
  rainbow-plugin-auth:系统权限管理;
  rainbow-plugin-login:系统安全登录;
  rainbow-plugin-ddd:基于领域驱动设计框架;
  rainbow-plugin-quartz:任务调度服务;
  rainbow-plugin-aq:基于oracle中的advanced queue的客户端实现;
2:对BaseService类中的update,insert方法的异常进行日志打印与异常堆栈抛出


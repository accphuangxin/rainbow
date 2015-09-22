### Rainbow介绍:
 基础为:Spring mvc + Spring + mybatis封装的开发平台，适用于微服务架构。
 
##CRUD服务编写：
 

 	public class DemoService extends BaseService{
		private static final String NAMESPACE = "SYSJMX";

		public RainbowContext query(RainbowContext context) {
			super.query(context, NAMESPACE);
			return context;
		}

		public RainbowContext insert(RainbowContext context) {
			try {
				context.addAttr("guid", new ObjectId().toString());
				super.insert(context, NAMESPACE);
			} catch (Exception e) {
				context.setSuccess(false);
				context.setMsg(e.getMessage());
			}
			return context;
		}
		
		public RainbowContext update(RainbowContext context) {
			super.update(context, NAMESPACE);
			return context;
		}
		
		public RainbowContext delete(RainbowContext context) {
			super.getDao().delete(NAMESPACE, "delete", context.getAttr());
			context.getAttr().clear();
			return context;
		}
 }

## 服务调用：
	RainbowContext context = new RainbowContext("demoService", "query"); //组织请求上下文
        context.addAttr("name","rainbow");//服务传参数

	context = SoaManager.getInstance().invoke(context);//基于事务调用服务

	context = SoaManager.getInstance().invokeNoTx(context);	//基于非事务调用服务
	
	System.out.println("服务反馈信息:" + context.getMsg());
	System.out.println("服务反馈状态:" + context.isSuccess());
	System.out.println("服务反馈结果列表:" + context.getRows());

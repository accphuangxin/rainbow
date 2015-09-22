/*
 * Copyright (c) 2013, Rainbow and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software;Designed and Developed mainly by many Chinese 
 * opensource volunteers. you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License version 2 only, as published by the
 * Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Any questions about this component can be directed to it's project Web address 
 * http://code.taobao.org/svn/rainbow/trunk
 *
 */
package org.hx.rainbow.common.core.service.impl;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hx.rainbow.common.context.RainbowContext;
import org.hx.rainbow.common.core.SpringApplicationContext;
import org.hx.rainbow.common.core.service.SoaInvoker;
import org.hx.rainbow.common.exception.AppException;
import org.hx.rainbow.common.exception.SysException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service("soaInvoker")
public class SoaInvokerSpringImpl implements SoaInvoker {
	public static final Logger logger = LogManager.getLogger(SoaInvokerSpringImpl.class);
	private PlatformTransactionManager txMgr = null;
	
	@Override
	public RainbowContext invoke(RainbowContext context) {
		Status s = beginTx(context);
		RainbowContext paramContext = null;
		try {
			Object service = SpringApplicationContext.getBean(context.getService());
			Method method = service.getClass().getMethod(context.getMethod(), new Class[] { RainbowContext.class });
			paramContext = (RainbowContext) method.invoke(service, new Object[] { context });
			if ((paramContext != null) && (!paramContext.isSuccess()))
				throw new InvokeFailException();
		} catch (InvokeFailException ex) {
			if (s != null)
				s.needRollback = true;
		} catch (Throwable e) {
			logger.error(e.getMessage(), e.getCause());
			if (s != null) {
				s.needRollback = true;
			}
			if ((e.getCause() instanceof AppException)) {
				throw (AppException)e.getCause();
			}
			throw new SysException("service invoke error", e);
		} finally {
			endTx(s);
		}
		return paramContext;
	}


	private Status beginTx(RainbowContext context) {
		
		Status s = new SoaInvokerSpringImpl.Status();
		Integer txType = (Integer) context.getAttr("transactionType");
		String serverinfo = context.getService() + "." + context.getMethod();
		
		String logStr = serverinfo + ":**SoaManage Call by TxType[" + txType + "]";
		logger.info(logStr);
		
		context.removeAttr("transactionType");
		if (txType == null) {
			s.canTx = false;
			return s;
		}
		String ds = context.getDs();
		String txMangerBeanId = "transactionManager";
		if(ds != null){
			txMangerBeanId = ds + txMangerBeanId;
		}
		
		if(txMgr == null){
			txMgr = (PlatformTransactionManager) SpringApplicationContext.getBean(txMangerBeanId);
		}
		s.txManager = txMgr;
		s.def = new DefaultTransactionDefinition(txType.intValue());
		s.def.setIsolationLevel(2);
		s.canTx = true;
		if (s.txManager != null) {
			try {
				s.tx = s.txManager.getTransaction(s.def);
				logStr = serverinfo + ":**SoaManage Get New Transaction Success!";
				logger.info(logStr);
			} catch (Exception e) {
				s.canTx = false;
				logStr = serverinfo + ":**SoaManage Get New Transaction Fail!case:" + e.getMessage();
				logger.info(logStr);
			}
		} else {
			s.canTx = false;
		}
		
		return s;
	}

	private void endTx(Status s) {
		if (s.canTx) {
			if (s.needRollback) {
				String logStr = "**SoaManage Rollback Transaction!";
				System.out.println(logStr);
				s.txManager.rollback(s.tx);
			} else {
				String logStr = "**SoaManage Commit Transaction!";
				System.out.println(logStr);
				s.txManager.commit(s.tx);
			}
		}
	}
	
	
	
	private static class InvokeFailException extends RuntimeException {
		private static final long serialVersionUID = -3397017022971969250L;
		
		public Throwable fillInStackTrace() {
			return this;
		}
		
	}

	private static class Status {
		public DefaultTransactionDefinition def;
		public TransactionStatus tx;
		public boolean canTx;
		public PlatformTransactionManager txManager;

		public boolean needRollback = false;
	}

	@Override
	public RainbowContext call(RainbowContext context) {
		RainbowContext paramContext = null;
		try {
			Object service = SpringApplicationContext.getBean(context.getService());
			
			Method method = service.getClass().getMethod(context.getMethod(), new Class[] { RainbowContext.class });
			paramContext = (RainbowContext) method.invoke(service, new Object[] { context });
			
			if ((paramContext != null) && (!paramContext.isSuccess())){
				throw new InvokeFailException();
			}
		}catch (Throwable e) {
			logger.error(e.getMessage(), e.getCause());
			
			if ((e.getCause() instanceof AppException)) {
				throw (AppException)e.getCause();
			}
			
			throw new SysException("service invoke error", e);
		}
		return paramContext;
	}
}
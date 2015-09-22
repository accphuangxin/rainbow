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
package org.hx.rainbow.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.hx.rainbow.common.exception.AppException;


/**
 * 操作javaBean工具类
 * 
 * @author hx
 * 
 */
public  class JavaBeanUtil {

	
	
	/**
	 * 高性能map to bean
	 * @param bean 转换的bean对象
	 * @param map 
	 * @param allowEmptyString string类型中是否允许""赋值
	 * @author huangxin
	 */
	public  static void map2bean(Object bean, Map<String,Object> map,boolean allowEmptyString){
		map2bean(bean,map,allowEmptyString,DateUtil.DEFAULT_DATE_PATTERN);
	}
	
	/**
	 * 高性能map to bean
	 * @param bean 转换的bean对象
	 * @param map 
	 * @param allowEmptyString string类型中是否允许""赋值
	 * @param dataFormat 如果map中有日期转换为bean中String,需要转为相应格式 如：yyyy-MM-dd HH:mm:ss 
	 * @author huangxin
	 */
	@SuppressWarnings({ "rawtypes"})
	public  static void map2bean(Object bean, Map<String,Object> map,boolean allowEmptyString,String dataFormat){
		if(bean == null || map == null){
			return ;
		}
		Class beanClass = bean.getClass();
		
		 Method[] methods = beanClass.getMethods();
		 for (Method method : methods){ 
	            try 
	            { 
	                if (method.getName().startsWith("set")) 
	                { 
	                    String field = method.getName(); 
	                    field = field.substring(field.indexOf("set") + 3); 
	                    field = field.toLowerCase().charAt(0) + field.substring(1); 
	                   
	                	Object o = map.get(field);
	        			if(o == null){
	        				continue;
	        			} 
	        			if(o instanceof String){
		        			if(o.toString().trim().length() == 0 && allowEmptyString){
		        				continue; 
		            		}	
		        			method.invoke(bean, new Object[]{o}); 
	        			}else{
	        				method.invoke(bean, new Object[]{o}); 
	        			}
	        		}
	            } 
	            catch (Exception e) 
	            { 
	            	e.printStackTrace();
	            } 
	        }
	}

	
	

	/**
	 * bean to map 
	 * @param bean 转换的bean对象
	 * @param map 
	 * @return map 如果bean为null 返回null
	 * @author huangxin
	 */
	public  static Map<String,Object> bean2Map(Map<String,Object> map, Object bean){
		if(bean == null){
			return null;
		}
		if(map == null){
			map = new HashMap<String,Object>();
		}
		
		Method[] methods = bean.getClass().getMethods();
		 for (Method method : methods){ 
	            try 
	            { 
	                if (method.getName().startsWith("get")) 
	                { 
	                    String field = method.getName(); 
	                    field = field.substring(field.indexOf("get") + 3); 
	                    field = field.toLowerCase().charAt(0) + field.substring(1); 
	                   
	                  Object o =  method.invoke(bean, (Object[])null); 
	        			if(o == null){
	        				continue;
	        			} 
	        			if(o instanceof Date){
	    					SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DEFAULT_DATETIME_PATTERN);
	    					map.put(field,sdf.format(o));
	        			}else if(o instanceof String){
	        				String str = (String)o;
//	        				if(Pattern.compile("[<>]+").matcher(str).find()){
//	        					str = str.replaceAll("<", "&lt").replaceAll(">",  "&gt");
//	        				}
	    					map.put(field,str);
	    				}else{
	    					map.put(field,o);
	    				}
	        		}
	            } 
	            catch (Exception e) 
	            { 
	            	e.printStackTrace();
	            } 
	        }
		return map;
	}

	/**
	 * 将一个对象的属性值取出来放置到Map中。Map的Key为对象属性名称
	 * 
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Deprecated
	public static Map getProperties(Object bean) {
		if (bean == null) {
			return null;
		}

		Map dataMap = new HashMap();
		try {
			PropertyDescriptor origDescriptors[] = PropertyUtils
					.getPropertyDescriptors(bean);

			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				if ("class".equals(name)) {
					continue;
				}

				if (PropertyUtils.isReadable(bean, name)) {
					Object obj = PropertyUtils.getProperty(bean, name);
					if (obj == null) {
						continue;
					}
					obj = convertValue(origDescriptors[i], obj);
					dataMap.put(name, obj);
				}
			}// for end
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
		return dataMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getMapList(List beanList) {
		if (beanList == null) {
			return null;
		}

		ArrayList lstMap = new ArrayList();
		Iterator iter;
		try {
			iter=beanList.iterator();
			while(iter.hasNext()){
				Object obj=iter.next();
				Map map=getProperties(obj);
				lstMap.add(map);
			}
		} catch (AppException e) {
			e.printStackTrace();
			throw e;
		}
		return lstMap;
	}
	
	private static Object convertValue(PropertyDescriptor origDescriptor,
			Object obj) {
		if (obj == null) {
			return null;
		}

		if (obj.toString().trim().length() == 0) {
			return null;
		}
		if (origDescriptor.getPropertyType() == java.util.Date.class) {
			//同一个时间，第一次从界面层传过来时，obj为String类型;转化后为Date类型
			 if (obj instanceof Date) {
				 return obj;
			}else{
				try {
					//修改 时间转换时会把带时分秒的截掉的问题 2012-5-10 张慧峰
					if(obj.toString().length()>10)
						obj = DateUtil.toDateTime(obj.toString());
					else
						obj = DateUtil.toDate(obj.toString());
				} catch (Exception e) {
					e.printStackTrace();
					throw new AppException(e.getMessage());
				}
			}
		}
		return obj;
	}
	

	
	/**
	 * 将一个bean的属性复制到另一个bean的同名属性中
	 * zhf 2012-5-14 [修改] 用BeanUtils.copyProperties方法copy属性出错问题
	 * @param fromBean
	 * @param toBean
	 */
	public static  void copyProperties(Object fromBean ,Object toBean){
		if (fromBean == null||toBean==null) {
			return;
		}
		try {
//			BeanUtils.copyProperties(toBean, fromBean);
			PropertyDescriptor origDescriptors[] = PropertyUtils
					.getPropertyDescriptors(toBean);

			for (int i = 0; i < origDescriptors.length; i++) {
				String name = origDescriptors[i].getName();
				if ("class".equals(name)) {
					continue;
				}

				//if (PropertyUtils.isReadable(fromBean, name)||PropertyUtils.isWriteable(toBean, name)) {
				if (PropertyUtils.isReadable(fromBean, name)&&PropertyUtils.isWriteable(toBean, name)) {
					Object obj = PropertyUtils.getProperty(fromBean, name);
					if (obj == null) {
						continue;
					}
					obj = convertValue(origDescriptors[i], obj);
					BeanUtils.copyProperty(toBean, name, obj);
				}
			}// for end
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getEntityList(List mapList,Class clazz) {
       if (mapList == null) {
           return null;
       }
       ArrayList ListEntity = new ArrayList();
       Iterator iter;
       try {
           iter = mapList.iterator();
           while (iter.hasNext()) {
              Map map = (Map) iter.next();
              Object obj = clazz.newInstance();
              map2bean(obj, map, false);
              ListEntity.add(obj);
           }
       } catch (Exception e) {
           e.printStackTrace();
           throw new AppException(e.getMessage());
       }
       return ListEntity;
    }
	
	public static void main(String[] args) {
		String a = "123123123";
		Pattern p=Pattern.compile("[<>\"]+");
		Matcher m=p.matcher(a);
		System.out.println(m.find());
	}
}
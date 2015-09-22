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
package org.hx.rainbow.common.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;
import org.hx.rainbow.common.core.SpringApplicationContext;
import org.hx.rainbow.common.dao.Dao;
import org.hx.rainbow.common.exception.SysException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service("daoMybatis")
public class DaoMybatisImpl implements Dao{

	private static final String SQL_SESSION_TEMPLATE = "sqlSessionTemplate";

	
	private SqlSessionTemplate getDao(String ds){
		if(ds == null || "".equals(ds)){
			return (SqlSessionTemplate)SpringApplicationContext.getBean(SQL_SESSION_TEMPLATE);
		}else{
			return (SqlSessionTemplate)SpringApplicationContext.getBean(ds + SQL_SESSION_TEMPLATE);
		}
	}
	

	public List<Map<String, Object>> query(String namespace, String statement) {
		return query(null, namespace, statement);
	}
	
	public List<Map<String, Object>> query(String ds, String namespace, String statement) {
		List<Map<String, Object>> dataList = getDao(ds).<Map<String,Object>>selectList(changeStatement(namespace,statement));
		return dataList;
	}
	
	public List<Map<String, Object>> query(String namespace, String statement,
			int limit, int offset) {
		return query(null, namespace, statement, limit, offset);
	}
	
	public List<Map<String, Object>> query(String ds, String namespace, String statement,
			int limit, int offset) {
		RowBounds rowBounds = new RowBounds((offset-1)*limit, limit);
		List<Map<String, Object>> dataList = getDao(ds).<Map<String,Object>>selectList(changeStatement(namespace,statement),null,rowBounds);
		return dataList;
	}

	public List<Map<String, Object>> query(String namespace,String statement,
			Map<String, Object> paramData) {
		return query(null, namespace, statement, paramData);
	}
	
	public List<Map<String, Object>> query(String ds, String namespace,String statement,
			Map<String, Object> paramData) {
		List<Map<String, Object>> dataList = getDao(ds).<Map<String,Object>>selectList(changeStatement(namespace,statement),paramData);
		return dataList;
	}

	public List<Map<String, Object>> query(String namespace,String statement,
			Map<String, Object> paramData, int limit, int offset) {
		return query(null, namespace, statement, paramData, limit, offset);
	}
	
	public List<Map<String, Object>> query(String ds, String namespace, String statement,
			Map<String, Object> paramData, int limit, int offset) {
		RowBounds rowBounds = new RowBounds((offset-1)*limit, limit);
		List<Map<String, Object>> dataList = getDao(ds).<Map<String,Object>>selectList(changeStatement(namespace,statement),paramData,rowBounds);
		return dataList;
	}

	public int count(String namespace,String statement) {
		return count(null, namespace, statement);
	}
	
	public int count(String ds, String namespace,String statement) {
		return getDao(ds).<Integer>selectOne(changeStatement(namespace,statement));
	}

	
	public int count(String namespace,String statement, Map<String, Object> paramData) {
		return count(null, namespace, statement, paramData);
	}
	
	public int count(String ds, String namespace, String statement, Map<String, Object> paramData) {
		return getDao(ds).<Integer>selectOne(changeStatement(namespace,statement),paramData);
	}


	public Map<String, Object> get(String namespace,String statement,
			Map<String, Object> paramData) {
		return get(null, namespace, statement, paramData);
	}
	
	public Map<String, Object> get(String ds, String namespace,String statement,
			Map<String, Object> paramData) {
		Map<String, Object> dataMap =  getDao(ds).<Map<String, Object>>selectOne(changeStatement(namespace,statement),paramData);
		return dataMap;
	}


	public Map<String, Object> load(String namespace, String key,
			String value) {
		return load(null, namespace, key, value);
	}
	
	public Map<String, Object> load(String ds, String namespace, String key,
			String value) {
		Map<String,String> param = new HashMap<String,String>();
		param.put(key, value);
		Map<String, Object> dataMap = getDao(ds).<Map<String, Object>>selectOne(changeStatement(namespace,"load"),param);
		return dataMap;
	}

	
	public void insert(String namespace,String statement, Map<String, Object> paramData) {
		insert(null, namespace, statement, paramData);
	}
	
	public void insert(String ds, String namespace, String statement, Map<String, Object> paramData) {
		try{
			getDao(ds).insert(changeStatement(namespace,statement), paramData);
		}catch (Exception e) {
			String msg = "Error Case:" + e.getMessage() + ";paramData:" + (paramData != null ? paramData.toString() : "");
			throw new SysException(msg, e.getCause());
		}
	}

	
	public int update(String namespace,String statement, Map<String, Object> paramData) {
		return update(null, namespace, statement, paramData);
	}
	
	public int update(String ds, String namespace,String statement, Map<String, Object> paramData) {
		try{
			return getDao(ds).update(changeStatement(namespace,statement), paramData);
		}catch (Exception e) {
			String msg = "Error Case:" + e.getMessage() + ";paramData:" + (paramData != null ? paramData.toString() : "");
			throw new SysException(msg, e.getCause());
		}
	}

	
	public int delete(String namespace, String statement, Map<String, Object> paramData) {
		return delete(null, namespace, statement, paramData);
	}
	
	public int delete(String ds, String namespace,String statement, Map<String, Object> paramData) {
		try{
			return getDao(ds).delete(changeStatement(namespace,statement), paramData);
		}catch (Exception e) {
			String msg = "Error Case:" + e.getMessage() + ";paramData:" + (paramData != null ? paramData.toString() : "");
			throw new SysException(msg, e.getCause());
		}
	}
	
	private String changeStatement(String namespace,String statement){
		return namespace + "." + statement;
	}

	public String getSql(String namespace, String statement,
			Map<String, Object> paramData) {
		return getSql(null, namespace, statement, paramData);
	}

	@Override
	public String getSql(String ds, String namespace, String statement,
			Map<String, Object> paramData) {
		try{
			MappedStatement ms = getDao(ds).getConfiguration().getMappedStatement(namespace+"."+statement);
			BoundSql boundSql = ms.getBoundSql(paramData);
			return boundSql.getSql();
		}catch (Exception e) {
			return namespace+"."+statement + "is error!";
		}
	}


	
		
}
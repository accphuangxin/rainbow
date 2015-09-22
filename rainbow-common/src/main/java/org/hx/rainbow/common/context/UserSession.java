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
package org.hx.rainbow.common.context;

import java.util.Map;

public class UserSession {

	private int hash = 0;

	private String userName;
	private String loginId;
	private String password;
	private Map<String, Object> sessionData;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public Map<String, Object> getSessionData() {
		return sessionData;
	}

	public void setSessionData(Map<String, Object> sessionData) {
		this.sessionData = sessionData;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		if (hash != 0) {
			return this.hash;
		}
		final int prime = super.hashCode();
		int result = 1;
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		result += ((loginId == null) ? 0 : loginId.hashCode());
		result += ((sessionData == null) ? 0 : sessionData.hashCode());
		result += ((password == null) ? 0 : password.hashCode());
		this.hash = result;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserSession))
			return false;

		final UserSession session = (UserSession) obj;

		if (!getUserName().equals(session.getUserName())) {
			return false;
		}
		if (!getLoginId().equals(session.getLoginId())) {
			return false;
		}
		if (!getSessionData().equals(session.getSessionData())) {
			return false;
		}
		if (!getPassword().equals(session.getPassword())) {
			return false;
		}
		return true;
	}


}
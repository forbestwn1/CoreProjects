package com.nosliw.user;

import com.nosliw.common.user.HAPUser;
import com.nosliw.common.user.HAPUserInfo;
import com.nosliw.data.core.imp.io.HAPDBSource;

public class HAPManagerUser {

	private HAPDataAccess m_dataAccess;

	private static HAPManagerUser m_instance;
	
	public static HAPManagerUser getInstance() {
		if(m_instance==null) {
			m_instance = new HAPManagerUser();
		}
		return m_instance;
	}
	
	private HAPManagerUser() {
		this.m_dataAccess = new HAPDataAccess(HAPDBSource.getDefaultDBSource());
	}

	public HAPUserInfo createUser() {
		HAPUser user = this.m_dataAccess.createUser();
		return getUserInfo(user.getId());
	}
	
	public HAPUserInfo getUserInfo(String id) {
		HAPUserInfo out = null;
		HAPUser user = this.m_dataAccess.getUserById(id);
		if(user!=null) {
			out = new HAPUserInfo();
			out.setUser(user);
		}
		return out;
	}
}

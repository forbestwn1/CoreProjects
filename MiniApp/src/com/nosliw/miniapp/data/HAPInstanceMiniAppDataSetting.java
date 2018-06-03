package com.nosliw.miniapp.data;

import com.nosliw.common.utils.HAPConstant;

public class HAPInstanceMiniAppDataSetting extends HAPInstanceMiniAppData{

	private String m_status;
	
	private String m_version;
	
	private String m_id;
	
	@Override
	public String getType() {  return HAPConstant.MINIAPPDATA_TYPE_SETTING;  }

	public String getStatus() {  return this.m_status;  }
	public void setStatus(String status) {  this.m_status = status;   }

	public String getVersion() {  return this.m_version;   }
	public void setVersion(String version) {  this.m_version = version;   }
	
	public String getId() {  return this.m_id;  }
	public void setId(String id) {  this.m_id = id;  }
	
}


package com.nosliw.miniapp.data;

public abstract class HAPInstanceMiniAppData {

	abstract public String getType();
	
	private Object m_data;
	public Object getData() {   return this.m_data;   }
	public String getDataStr() { return this.m_data.toString();  }
	public void setData(Object data) {   this.m_data = data;   }

	
}

package com.nosliw.service.soccer;

import java.util.List;

public class HAPPlayerStatus {

	private String m_status;
	private Object m_statusData;
		
	private List<String> m_actions;
	
	public HAPPlayerStatus(String status, Object statusData, List<String> actions) {
		this.m_status = status;
		this.m_statusData = statusData;
		this.m_actions = actions;
	}

	public String getStatus() {   return this.m_status;  }
	public Object getStatusData() {   return this.m_statusData;   }
	public List<String> getActions(){   return this.m_actions;    }
	
}

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
	
	public String getStatusDescription() {
		StringBuffer out = new StringBuffer();
		out.append(this.m_status);
		if(this.m_status.equals(HAPPlayerLineup.STATUS_LINEUP)) {
			
		}
		else if(this.m_status.equals(HAPPlayerLineup.STATUS_WAITINGLIST)) {
			out.append(" 前面有"+this.m_statusData);
		}
		else if(this.m_status.equals(HAPPlayerLineup.STATUS_PROVIDER)) {
			out.append(" 前面有"+this.m_statusData);
		}
		else if(this.m_status.equals(HAPPlayerLineup.STATUS_NOTHING)) {
			
		}
		
		return out.toString();
	}
	
}

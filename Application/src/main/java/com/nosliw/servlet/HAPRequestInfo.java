package com.nosliw.servlet;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

import jakarta.servlet.http.HttpServletRequest;

@HAPEntityWithAttribute
public class HAPRequestInfo {

	@HAPAttribute
	public static String CLIENTID = "clientId";

	@HAPAttribute
	public static String COMMAND = "command";
	
	@HAPAttribute
	public static String PARMS = "parms";
	
	private String m_clientId;
	
	private String m_command;
	
	private String m_parms;
	
	public HAPRequestInfo(HttpServletRequest request) {
		this.m_clientId = request.getParameter(CLIENTID);
		this.m_command = request.getParameter(COMMAND);
		this.m_parms = request.getParameter(PARMS);
	}
	
	public HAPRequestInfo(String clientId, String command, String data){
		this.m_clientId = clientId;
		this.m_command = command;
		this.m_parms = data;
	}
	
	public String getClientId(){  return this.m_clientId;  }
	
	public String getCommand(){  return this.m_command;  }
	
	public String getParms(){   return this.m_parms;   }
	
}

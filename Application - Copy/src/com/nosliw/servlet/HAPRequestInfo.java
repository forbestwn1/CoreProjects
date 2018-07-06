package com.nosliw.servlet;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public class HAPRequestInfo {

	@HAPAttribute
	public static String CLIENTID = "clientId";

	@HAPAttribute
	public static String COMMAND = "command";
	
	@HAPAttribute
	public static String DATA = "data";
	
	private String m_clientId;
	
	private String m_command;
	
	private Object m_data;
	
	public HAPRequestInfo(String clientId, String command, Object data){
		this.m_clientId = clientId;
		this.m_command = command;
		this.m_data = data;
	}
	
	public String getClientId(){  return this.m_clientId;  }
	
	public String getCommand(){  return this.m_command;  }
	
	public Object getData(){   return this.m_data;   }
	
}

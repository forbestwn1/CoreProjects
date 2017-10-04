package com.nosliw.data.core.runtime.js;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public class HAPRuntimeGatewayOutput {

	@HAPAttribute
	final public static String SCRIPTS = "scripts";

	@HAPAttribute
	final public static String DATA = "data";
	
	private List<HAPJSScriptInfo> m_scripts;
	
	private Object m_data;
	
	public HAPRuntimeGatewayOutput(List<HAPJSScriptInfo> scripts, Object data){
		this.m_scripts = new ArrayList<HAPJSScriptInfo>();
		if(scripts!=null)		this.m_scripts.addAll(scripts);
		this.m_data = data;
	}
	
	public List<HAPJSScriptInfo> getScripts(){  return this.m_scripts;  } 
	
	public Object getData(){  return this.m_data;  }
}

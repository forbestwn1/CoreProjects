package com.nosliw.core.gateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.common.serialization.HAPManagerSerialize;

@HAPEntityWithAttribute
public class HAPGatewayOutput extends HAPSerializableImp{

	@HAPAttribute
	final public static String SCRIPTS = "scripts";

	@HAPAttribute
	final public static String DATA = "data";
	
	private List<HAPJSScriptInfo> m_scripts;
	
	private Object m_data;
	
	public HAPGatewayOutput(List<HAPJSScriptInfo> scripts, Object data){
		this.m_scripts = new ArrayList<HAPJSScriptInfo>();
		if(scripts!=null)		this.m_scripts.addAll(scripts);
		this.m_data = data;
	}
	
	public List<HAPJSScriptInfo> getScripts(){  return this.m_scripts;  } 
	
	public Object getData(){  return this.m_data;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(DATA, HAPManagerSerialize.getInstance().toStringValue(this.m_data, HAPSerializationFormat.JSON));
		jsonMap.put(SCRIPTS, HAPManagerSerialize.getInstance().toStringValue(this.m_scripts, HAPSerializationFormat.JSON));
	}

}

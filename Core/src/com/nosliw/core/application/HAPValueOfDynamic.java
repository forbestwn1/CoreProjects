package com.nosliw.core.application;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.valueport.HAPWithExternalValuePort;

public class HAPValueOfDynamic extends HAPSerializableImp implements HAPWithExternalValuePort{

	public static final String INTERFACEID = "interfaceId";
	
	private String m_interfaceId;

	private HAPContainerValuePorts m_valuePortsContainer;
	
	public HAPValueOfDynamic() {
		this.m_valuePortsContainer = new HAPContainerValuePorts(); 
	}

	public HAPValueOfDynamic(String interfaceId) {
		this();
		this.m_interfaceId = interfaceId;
	}

	public String getInterfaceId() {	return this.m_interfaceId;	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		if(json instanceof String) {
			this.m_interfaceId = (String)json;
		} else if(json instanceof JSONObject) {
			
		}
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(INTERFACEID, m_interfaceId);
		jsonMap.put(EXTERNALVALUEPORT, this.m_valuePortsContainer.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	public HAPContainerValuePorts getExternalValuePorts() {   return this.m_valuePortsContainer;  }
}

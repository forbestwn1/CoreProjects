package com.nosliw.core.application;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.valueport.HAPWithExternalValuePort;

@HAPEntityWithAttribute
public class HAPValueOfDynamic extends HAPSerializableImp implements HAPWithExternalValuePort{

	@HAPAttribute
	public static final String DYNAMICID = "dynamicId";
	
	private String m_dynamicId;

	private HAPContainerValuePorts m_valuePortsContainer;
	
	public HAPValueOfDynamic() {
		this.m_valuePortsContainer = new HAPContainerValuePorts(); 
	}

	public HAPValueOfDynamic(String interfaceId) {
		this();
		this.m_dynamicId = interfaceId;
	}

	public String getDynamicId() {	return this.m_dynamicId;	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		if(json instanceof String) {
			this.m_dynamicId = (String)json;
		} else if(json instanceof JSONObject) {
			
		}
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(DYNAMICID, m_dynamicId);
		jsonMap.put(EXTERNALVALUEPORT, this.m_valuePortsContainer.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	public HAPContainerValuePorts getExternalValuePorts() {   return this.m_valuePortsContainer;  }
}

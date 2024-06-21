package com.nosliw.core.application.common.interactive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.common.valueport.HAPWithValuePort;
import com.nosliw.core.application.common.valueport.HAPWrapperValuePort;

public class HAPInteractiveTaskImp extends HAPSerializableImp implements HAPInteractive, HAPWithValuePort{

	private HAPInteractiveRequest m_request;

	private List<HAPInteractiveResultTask> m_results;

	public HAPInteractiveTaskImp() {
		this.m_results = new ArrayList<HAPInteractiveResultTask>();
		this.m_request = new HAPInteractiveRequest();
	}
	
	@Override
	public HAPContainerValuePorts getInternalValuePorts() {
		HAPGroupValuePorts group = new HAPGroupValuePorts();
		
		//request
		HAPWrapperValuePort requestValuePortWrapper = new HAPWrapperValuePort(this.m_request.getInternalValuePort());
		requestValuePortWrapper.setName(HAPConstantShared.VALUEPORT_NAME_INTERACT_REQUEST);
		group.addValuePort(requestValuePortWrapper, true); 

		//result
		for(HAPInteractiveResultTask result : this.m_results) {
			HAPWrapperValuePort resultValuePortWrapper = new HAPWrapperValuePort(result.getInternalValuePort());
			resultValuePortWrapper.setName(buildResultValuePortName(result.getName()));
			group.addValuePort(resultValuePortWrapper, false); 
		}
		
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		out.addValuePortGroup(group, true);
		return out;
	}

	@Override
	public HAPContainerValuePorts getExternalValuePorts() {
		HAPGroupValuePorts group = new HAPGroupValuePorts();
		
		//request
		HAPWrapperValuePort requestValuePortWrapper = new HAPWrapperValuePort(this.m_request.getExternalValuePort());
		requestValuePortWrapper.setName(HAPConstantShared.VALUEPORT_NAME_INTERACT_REQUEST);
		group.addValuePort(requestValuePortWrapper, true); 

		//result
		for(HAPInteractiveResultTask result : this.m_results) {
			HAPWrapperValuePort resultValuePortWrapper = new HAPWrapperValuePort(result.getExternalValuePort());
			resultValuePortWrapper.setName(buildResultValuePortName(result.getName()));
			group.addValuePort(resultValuePortWrapper, false); 
		}
		
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		out.addValuePortGroup(group, true);
		return out;
	}
	
	private String buildResultValuePortName(String resultName) {
		return HAPUtilityNamingConversion.cascadeComponents(HAPConstantShared.VALUEPORT_NAME_INTERACT_RESULT, resultName, HAPConstantShared.SEPERATOR_PREFIX);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		
		JSONArray parmsArray = jsonObj.optJSONArray(REQUEST);
		if(parmsArray!=null) {
			this.m_request.buildObject(parmsArray, HAPSerializationFormat.JSON);
		}
		
		Object resutltsObj = jsonObj.opt(RESULT);
		if(resutltsObj!=null) {
			if(resutltsObj instanceof JSONObject) {
				JSONObject resultObject = (JSONObject)resutltsObj;
				for(Object key : resultObject.keySet()) {
					String name = (String)key;
					HAPInteractiveResultTask resultEle = new HAPInteractiveResultTask();
					resultEle.buildObject(resultObject.get(name), HAPSerializationFormat.JSON);
					resultEle.setName(name);
					this.m_results.add(resultEle);
				}
			}
			else if(resutltsObj instanceof JSONArray) {
				JSONArray resultArray = (JSONArray)resutltsObj;
				for(int i=0; i<resultArray.length(); i++) {
					HAPInteractiveResultTask resultEle = new HAPInteractiveResultTask();
					resultEle.buildObject(resultArray.getJSONObject(i), HAPSerializationFormat.JSON);
					this.m_results.add(resultEle);
				}
			}
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(REQUEST, HAPManagerSerialize.getInstance().toStringValue(this.m_request, HAPSerializationFormat.JSON));
		jsonMap.put(RESULT, HAPManagerSerialize.getInstance().toStringValue(this.m_results, HAPSerializationFormat.JSON));
	}
	
}

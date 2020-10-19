package com.nosliw.data.core.service.interfacee;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.data.variable.HAPVariableInfo;

@HAPEntityWithAttribute
public class HAPServiceParm extends HAPVariableInfo{

	public HAPServiceParm(){	}

	public HAPServiceParm cloneServiceParm() {
		HAPServiceParm out = new HAPServiceParm();
		this.cloneToVariableInfo(out);
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			super.buildObjectByJson(objJson);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
}

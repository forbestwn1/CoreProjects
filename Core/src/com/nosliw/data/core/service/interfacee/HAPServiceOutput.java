package com.nosliw.data.core.service.interfacee;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.data.variable.HAPVariableInfo;

@HAPEntityWithAttribute
public class HAPServiceOutput extends HAPVariableInfo{

	public HAPServiceOutput(){
	}

	public HAPServiceOutput cloneServiceOutput() {
		HAPServiceOutput out = new HAPServiceOutput();
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

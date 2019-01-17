package com.nosliw.data.core.service.interfacee;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;

@HAPEntityWithAttribute
public class HAPServiceResult extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String OUTPUT = "output";
	
	private Map<String, HAPServiceOutput> m_output;
	
	public HAPServiceResult(){
		this.m_output = new LinkedHashMap<String, HAPServiceOutput>();
	}

	public void addOutput(String name, HAPServiceOutput output) {   this.m_output.put(name, output);   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			super.buildObjectByJson(objJson);
			
			this.m_output.putAll(HAPSerializeUtility.buildMapFromJsonObject(HAPServiceOutput.class.getName(), objJson.optJSONObject(OUTPUT)));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(OUTPUT, HAPJsonUtility.buildJson(this.m_output, HAPSerializationFormat.JSON));
	}
}

package com.nosliw.data.core.service.interfacee;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPServiceResult extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String OUTPUT = "output";
	
	private Map<String, HAPServiceOutput> m_output;
	
	public HAPServiceResult(){
		this.m_output = new LinkedHashMap<String, HAPServiceOutput>();
	}

	public void addOutput(HAPServiceOutput output) {   this.m_output.put(output.getId(), output);   }
	public Map<String, HAPServiceOutput> getOutput(){   return this.m_output;  }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			super.buildObjectByJson(objJson);
			
			JSONArray outputArray = objJson.getJSONArray(OUTPUT);
			for(int i = 0; i<outputArray.length(); i++){
				HAPServiceOutput output = new HAPServiceOutput();
				output.buildObject(outputArray.get(i), HAPSerializationFormat.JSON);
				this.addOutput(output);
			}
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

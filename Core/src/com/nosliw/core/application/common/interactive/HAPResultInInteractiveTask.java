package com.nosliw.core.application.common.interactive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPResultInInteractiveTask extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String OUTPUT = "output";
	
	private List<HAPResultElementInInteractiveTask> m_output;
	
	public HAPResultInInteractiveTask(){
		this.m_output = new ArrayList<HAPResultElementInInteractiveTask>();
	}

	public void addOutput(HAPResultElementInInteractiveTask output) {   this.m_output.add(output);   }
	public List<HAPResultElementInInteractiveTask> getOutput(){   return this.m_output;  }
	
	public HAPResultInInteractiveTask cloneInteractiveResult() {
		HAPResultInInteractiveTask out = new HAPResultInInteractiveTask();
		this.cloneToInteractiveResult(out);
		return out;
	}
	
	protected void cloneToInteractiveResult(HAPResultInInteractiveTask result) {
		this.cloneToEntityInfo(result);
		for(HAPResultElementInInteractiveTask output : this.m_output) {
			result.addOutput(output.cloneInteractiveOutput());
		}
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			super.buildObjectByJson(objJson);
			
			JSONArray outputArray = objJson.getJSONArray(OUTPUT);
			for(int i=0; i<outputArray.length(); i++) {
				HAPResultElementInInteractiveTask output = new HAPResultElementInInteractiveTask();
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
		jsonMap.put(OUTPUT, HAPUtilityJson.buildJson(this.m_output, HAPSerializationFormat.JSON));
	}

}

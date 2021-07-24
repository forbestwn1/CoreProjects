package com.nosliw.data.core.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.variable.HAPVariableInfo;

@HAPEntityWithAttribute
public class HAPResultInteractive extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String OUTPUT = "output";
	
	private List<HAPVariableInfo> m_output;
	
	public HAPResultInteractive(){
		this.m_output = new ArrayList<HAPVariableInfo>();
	}

	public void addOutput(HAPVariableInfo output) {   this.m_output.add(output);   }
	public List<HAPVariableInfo> getOutput(){   return this.m_output;  }
	
	public HAPResultInteractive cloneInteractiveResult() {
		HAPResultInteractive out = new HAPResultInteractive();
		for(HAPVariableInfo output : this.m_output) {
			out.addOutput(output.cloneVariableInfo());
		}
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			super.buildObjectByJson(objJson);
			
			JSONArray outputArray = objJson.getJSONArray(OUTPUT);
			for(int i=0; i<outputArray.length(); i++) {
				HAPVariableInfo output = HAPVariableInfo.buildVariableInfoFromObject(outputArray.get(i));
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

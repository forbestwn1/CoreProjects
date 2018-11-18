package com.nosliw.data.core.process;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

public class HAPDefinitionActivityEnd extends HAPDefinitionActivity{

	@HAPAttribute
	public static String OUTPUT = "output";
	
	private HAPDefinitionDataAssociationGroup m_output;
	
	public HAPDefinitionActivityEnd() {}

	@Override
	public String getType() {		return HAPConstant.ACTIVITY_TYPE_END;	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			this.m_output = new HAPDefinitionDataAssociationGroup();
			this.m_output.buildObject(jsonObj.optJSONObject(OUTPUT), HAPSerializationFormat.JSON);
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_output!=null)		jsonMap.put(OUTPUT, this.m_output.toStringValue(HAPSerializationFormat.JSON));
	}
}

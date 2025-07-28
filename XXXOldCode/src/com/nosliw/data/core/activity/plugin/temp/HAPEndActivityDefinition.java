package com.nosliw.data.core.activity.plugin.temp;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPParserDataAssociation;

public class HAPEndActivityDefinition extends HAPDefinitionActivity{

	@HAPAttribute
	public static String OUTPUT = "output";
	
	private HAPDefinitionDataAssociation m_output;
	
	public HAPEndActivityDefinition(String type) {
		super(type);
		this.init();
	}

	private void init() {
//		if(this.m_output!=null)   HAPUtilityContext.setContextGroupInheritModeNone(this.m_output.getInfo());
	}
	
	@Override
	public String getActivityType() {		return HAPConstantShared.ACTIVITY_TYPE_END;	}
	
	public HAPDefinitionDataAssociation getOutput() {    return this.m_output;   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			this.m_output = HAPParserDataAssociation.buildDefinitionByJson(jsonObj.optJSONObject(OUTPUT));
			this.init();
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

	@Override
	public HAPDefinitionActivity cloneActivityDefinition() {
		HAPEndActivityDefinition out = new HAPEndActivityDefinition(this.getActivityType());
		this.cloneToActivityDefinition(out);
		if(this.m_output!=null)    out.m_output = this.m_output.cloneDataAssocation();
		return out;
	}
}

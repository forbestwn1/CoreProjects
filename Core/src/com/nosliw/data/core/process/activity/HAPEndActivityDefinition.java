package com.nosliw.data.core.process.activity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.script.context.HAPDefinitionDataAssociationGroup;
import com.nosliw.data.core.script.context.HAPUtilityContext;

public class HAPEndActivityDefinition extends HAPDefinitionActivity{

	@HAPAttribute
	public static String OUTPUT = "output";
	
	private HAPDefinitionDataAssociationGroup m_output;
	
	public HAPEndActivityDefinition(String type) {
		super(type);
		this.m_output = new HAPDefinitionDataAssociationGroup();
		this.init();
	}

	private void init() {
		HAPUtilityContext.setContextGroupInheritModeNone(this.m_output.getInfo());
	}
	
	@Override
	public String getType() {		return HAPConstant.ACTIVITY_TYPE_END;	}
	
	public HAPDefinitionDataAssociationGroup getOutput() {    return this.m_output;   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			this.m_output = new HAPDefinitionDataAssociationGroup();
			this.m_output.buildObject(jsonObj.optJSONObject(OUTPUT), HAPSerializationFormat.JSON);
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
}

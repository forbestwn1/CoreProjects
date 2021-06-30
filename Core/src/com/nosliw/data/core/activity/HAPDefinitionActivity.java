package com.nosliw.data.core.activity;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.HAPDefinitionEntityComplex;

@HAPEntityWithAttribute
public abstract class HAPDefinitionActivity extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String TYPE = "type";

	private String m_type;
	
	public HAPDefinitionActivity(String type) {
		this.m_type = type;
	}

	public String getType() {   return this.m_type;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public void cloneToActivityDefinition(HAPDefinitionActivity activity) {
		this.cloneToEntityInfo(activity);
		activity.m_type = this.m_type;
	}
	
	public abstract HAPDefinitionActivity cloneActivityDefinition();
	
	public abstract void parseActivityDefinition(Object obj, HAPDefinitionEntityComplex complexEntity, HAPSerializationFormat format);
}

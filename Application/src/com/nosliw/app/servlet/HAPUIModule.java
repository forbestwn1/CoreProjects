package com.nosliw.app.servlet;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.uiresource.definition.HAPUIDefinitionUnitResource;

@HAPEntityWithAttribute
public class HAPUIModule extends HAPSerializableImp{

	@HAPAttribute
	public static String UIRESOURCE = "uiResource";

	private HAPUIDefinitionUnitResource m_uiResource; 

	
	public void setUiResource(HAPUIDefinitionUnitResource uiResource) {
		this.m_uiResource = uiResource;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(UIRESOURCE, HAPSerializeManager.getInstance().toStringValue(this.m_uiResource, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_uiResource = (HAPUIDefinitionUnitResource)HAPSerializeManager.getInstance().buildObject(HAPUIDefinitionUnitResource.class.getName(), jsonObj.opt(UIRESOURCE), HAPSerializationFormat.JSON);
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return this.buildObjectByFullJson(json);
	}
	
}

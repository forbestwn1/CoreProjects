package com.nosliw.app.servlet;

import java.util.ArrayList;
import java.util.List;
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
	public static String UIRESOURCEIDS = "uiResources";

	private List<String> m_resourceIds;
	
	
	public HAPUIModule() {
		this.m_resourceIds = new ArrayList<String>();
	}
	
	public void addUiResource(String uiResourceId) {
		this.m_resourceIds.add(uiResourceId);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(UIRESOURCEIDS, HAPSerializeManager.getInstance().toStringValue(this.m_resourceIds, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return this.buildObjectByFullJson(json);
	}
	
}

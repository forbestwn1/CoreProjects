package com.nosliw.data.core.resource;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;

public abstract class HAPResourceDefinitionImp extends HAPEntityInfoImp implements HAPResourceDefinition{

	@HAPAttribute
	public static String RESOURCEID = "resourceId";

	private HAPResourceId m_resourceId;
	
	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.ENTITY;    }

	@Override
	public void setResourceId(HAPResourceId resourceId) {   this.m_resourceId = resourceId;  }

	@Override
	public HAPResourceId getResourceId() {   return this.m_resourceId; }

	@Override
	public void cloneToResourceDefinition(HAPResourceDefinition resourceDef) {
		resourceDef.setResourceId(this.getResourceId());
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject objJson = (JSONObject)json;
		super.buildObjectByJson(objJson);
		this.m_resourceId = HAPResourceIdFactory.newInstance(objJson.opt(RESOURCEID));
		return true;  
	}
	
	@Override
	public void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(RESOURCEID, HAPJsonUtility.buildJson(this.m_resourceId, HAPSerializationFormat.JSON));
	}
}

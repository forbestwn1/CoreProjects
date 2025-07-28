package com.nosliw.core.resource;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.data.core.component.HAPPathLocationBase;

public class HAPResourceDefinition extends HAPEntityInfoImp implements HAPResourceDefinitionOrId, HAPEntityInfo, HAPSerializable{

	@HAPAttribute
	public static String RESOURCEID = "resourceId";

	//resource id
	private HAPResourceId m_resourceId;

	//data for resource
	private HAPManualBrick m_entityDef;
	
	public HAPResourceDefinition(HAPResourceId resourceId) {
		this.m_resourceId = resourceId;
	}
	
	public String getResourceType() {  return this.m_resourceId.getResourceType();  }

	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.ENTITY;    }

	public HAPResourceId getResourceId() {   return this.m_resourceId; }

	public HAPManualBrick getEntityDefinition() {  return this.m_entityDef;  }

	public void setEntityId(HAPManualBrick entityDef) {   this.m_entityDef = entityDef;  }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject objJson = (JSONObject)json;
		super.buildObjectByJson(objJson);
		this.m_resourceId = HAPFactoryResourceId.newInstance(objJson.opt(RESOURCEID));
		Object localRefBase = objJson.opt(LOCALREFERENCEBASE);
		if(localRefBase!=null) {
			this.m_localReferenceBase = new HAPPathLocationBase();
			this.m_localReferenceBase.buildObject(localRefBase, HAPSerializationFormat.LITERATE);
		}
		return true;  
	}
	
	@Override
	public void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(RESOURCEID, HAPUtilityJson.buildJson(this.m_resourceId, HAPSerializationFormat.JSON));
		if(this.m_localReferenceBase!=null) {
			jsonMap.put(LOCALREFERENCEBASE, this.m_localReferenceBase.toStringValue(HAPSerializationFormat.LITERATE));
		}
	}

	public void cloneToResourceDefinition(HAPResourceDefinition resourceDef) {
		// TODO Auto-generated method stub
		
	}
}

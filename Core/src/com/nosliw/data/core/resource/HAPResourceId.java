package com.nosliw.data.core.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;

@HAPEntityWithAttribute
public abstract class HAPResourceId extends HAPSerializableImp implements HAPResourceDefinitionOrId{

	@HAPAttribute
	public static String RESOURCETYPE = "resourceType";

	@HAPAttribute
	public static String VERSION = "version";

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String STRUCUTRE = "structure";

	@HAPAttribute
	public static String SUP = "supliment";

	private String m_resourceType;
	
	private String m_version;

	//all the supplement resource in order for this resource to be valid resource
	private HAPSupplementResourceId m_supplement;

	public HAPResourceId(String type, String version) {
		this.m_resourceType = type;
		this.m_version = version;
	}
	
	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.RESOURCEID;    }

	public String getResourceType() {  return this.m_resourceType;  }
	protected void setResourceType(String type) {    this.m_resourceType = type;    }
	
	public String getVersion() {  return this.m_version;    }
	
	public abstract String getStructure();
	
	public HAPSupplementResourceId getSupplement() {  return this.m_supplement;  }
	public void setSupplement(HAPSupplementResourceId sup) {   this.m_supplement = sup;   }
	
	//literate for id part only
	public abstract String getCoreIdLiterate();
	protected abstract void buildCoreIdJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap);
	
	//parse id part literate
	protected abstract void buildCoreIdByLiterate(String idLiterate);	

	protected abstract void buildCoreIdByJSON(JSONObject jsonObj);	

	@Override
	protected String buildLiterate(){
		return HAPUtilityResourceId.buildResourceIdLiterate(this);
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(RESOURCETYPE, this.getResourceType());
		
		Map<String, String> jsonMapId = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMapId = new LinkedHashMap<String, Class<?>>();
		jsonMapId.put(STRUCUTRE, this.getStructure());
		if(this.m_supplement!=null && !this.m_supplement.isEmpty()) {
			jsonMapId.put(SUP, this.m_supplement.toStringValue(HAPSerializationFormat.JSON_FULL));
		}
		this.buildCoreIdJsonMap(jsonMapId, typeJsonMapId);
		jsonMap.put(ID, HAPUtilityJson.buildMapJson(jsonMapId, typeJsonMapId));
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(RESOURCETYPE, this.getResourceType());
		jsonMap.put(VERSION, this.getVersion());
		jsonMap.put(ID, HAPUtilityResourceId.buildResourceCoreIdLiterate(this));
	}

	@Override
	public boolean equals(Object o){
		boolean out = false;
		if(o instanceof HAPResourceId){
			HAPResourceId resourceId = (HAPResourceId)o;
			if(HAPUtilityBasic.isEquals(this.getResourceType(), resourceId.getResourceType())) {
				if(HAPUtilityBasic.isEquals(this.getVersion(), resourceId.getVersion())) {
					return HAPUtilityBasic.isEquals(this.m_supplement, resourceId.m_supplement);
				}
			}
		}
		return out;
	}
	
	@Override
	public abstract HAPResourceId clone();

	protected void cloneFrom(HAPResourceId resourceId){
		this.m_resourceType = resourceId.m_resourceType;
		this.m_version = resourceId.m_version;
		this.m_supplement = resourceId.m_supplement;
	}
}

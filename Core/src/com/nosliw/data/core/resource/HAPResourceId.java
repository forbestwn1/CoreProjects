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
	public static String RESOURCETYPEID = "resourceTypeId";

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String STRUCUTRE = "structure";

	@HAPAttribute
	public static String SUP = "supliment";

	private HAPIdResourceType m_resourceTypeId;
	
	//all the supplement resource in order for this resource to be valid resource
	private HAPSupplementResourceId m_supplement;

	public HAPResourceId(String type, String version) {
		this.m_resourceTypeId = new HAPIdResourceType(type, version); 
	}
	
	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.RESOURCEID;    }

	public abstract String getStructure();
	
	public HAPIdResourceType getResourceTypeId() {    return this.m_resourceTypeId;     }
	
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
		jsonMap.put(RESOURCETYPEID, this.m_resourceTypeId.toStringValue(HAPSerializationFormat.JSON));
		
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
		jsonMap.put(RESOURCETYPEID, this.m_resourceTypeId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ID, HAPUtilityResourceId.buildResourceCoreIdLiterate(this));
	}

	@Override
	public boolean equals(Object o){
		boolean out = false;
		if(o instanceof HAPResourceId){
			HAPResourceId resourceId = (HAPResourceId)o;
			if(HAPUtilityBasic.isEquals(this.getResourceTypeId(), resourceId.getResourceTypeId())) {
				return HAPUtilityBasic.isEquals(this.m_supplement, resourceId.m_supplement);
			}
		}
		return out;
	}
	
	@Override
	public abstract HAPResourceId clone();

	protected void cloneFrom(HAPResourceId resourceId){
		this.m_resourceTypeId = (HAPIdResourceType)resourceId.m_resourceTypeId.cloneValue();
		this.m_supplement = resourceId.m_supplement;
	}
}

package com.nosliw.data.core.dataassociation.mapping;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;

public class HAPDefinitionDataAssociationMapping extends HAPEntityInfoWritableImp implements HAPDefinitionDataAssociation{

	@HAPAttribute
	public static String TARGET = "target";

	private Map<String, HAPValueMapping> m_mappings;
	
	public HAPDefinitionDataAssociationMapping() {
		this.m_mappings = new LinkedHashMap<String, HAPValueMapping>();
	}
 
	@Override
	public String getType() {  return HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING;  }

	public void addAssociation(String targetName, HAPValueMapping mapping) {	
		if(HAPBasicUtility.isStringEmpty(targetName))  targetName = HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT;
		this.m_mappings.put(targetName, mapping);
	}
	
	public Map<String, HAPValueMapping> getMappings(){   return this.m_mappings;  }
	
	public HAPValueMapping getMapping(String targetName) {   return this.m_mappings.get(targetName);    }
	public HAPValueMapping getMapping() {   return this.m_mappings.get(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT);    }
 
	@Override
	public HAPDefinitionDataAssociationMapping cloneDataAssocation() {
		HAPDefinitionDataAssociationMapping out = new HAPDefinitionDataAssociationMapping();
		this.cloneToEntityInfo(out);
		for(String name : this.m_mappings.keySet()) {
			out.m_mappings.put(name, this.m_mappings.get(name).cloneValueMapping());
		}
		
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(TARGET, HAPJsonUtility.buildJson(this.m_mappings, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject daJsonObj = (JSONObject)json;
			this.buildEntityInfoByJson(daJsonObj);
			
			JSONObject elesJson = daJsonObj.optJSONObject(HAPValueMapping.MAPPING);
			if(elesJson!=null) {
				//seperate by target name
				Map<String, JSONObject> jsonMapByTarget = new LinkedHashMap<String, JSONObject>();
				for(Object key : elesJson.keySet()) {
					HAPTarget target = new HAPTarget((String)key);
					String targetName = target.getValueStructureName();
					JSONObject jsonByTarget = jsonMapByTarget.get(targetName);
					if(jsonByTarget==null) {
						jsonByTarget = new JSONObject();
						jsonMapByTarget.put(targetName, jsonByTarget);
					}
					jsonByTarget.put(target.getRootNodeReference().toStringValue(HAPSerializationFormat.LITERATE), elesJson.optJSONObject((String)key));
				}
				
				for(String targetName : jsonMapByTarget.keySet()) {
					HAPValueMapping mapping = new HAPValueMapping();
					JSONObject targetAssociationJson = new JSONObject();
					targetAssociationJson.put(HAPValueMapping.MAPPING, jsonMapByTarget.get(targetName));
					mapping.buildObject(targetAssociationJson, HAPSerializationFormat.JSON);
					this.addAssociation(targetName, mapping);
				}
			}
			else {
				JSONObject associationsJson = daJsonObj.optJSONObject(TARGET);
				if(associationsJson!=null) {
					for(Object key : associationsJson.keySet()) {
						String targetName = (String)key;
						HAPValueMapping association = new HAPValueMapping();
						association.buildObject(associationsJson.getJSONObject(targetName), HAPSerializationFormat.JSON);
						this.addAssociation(targetName, association);
					}
				}
			}
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

}

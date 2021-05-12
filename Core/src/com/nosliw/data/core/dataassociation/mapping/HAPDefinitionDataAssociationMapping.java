package com.nosliw.data.core.dataassociation.mapping;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;

public class HAPDefinitionDataAssociationMapping extends HAPEntityInfoWritableImp implements HAPDefinitionDataAssociation{

	@HAPAttribute
	public static String MAPPING = "mapping";

	private Map<String, HAPMapping> m_mappings;
	
	public HAPDefinitionDataAssociationMapping() {
		this.m_mappings = new LinkedHashMap<String, HAPMapping>();
	}
 
	@Override
	public String getType() {  return HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING;  }

	@Override
	public void updateInputVarName(HAPUpdateName updateName) {
		for(String name : this.m_mappings.keySet()) {
			HAPValueStructureDefinitionFlat association = this.m_mappings.get(name);
			association.updateReferenceName(updateName);
		}
	}

	@Override
	public void updateOutputVarName(HAPUpdateName updateName) {
		for(String name : this.m_mappings.keySet()) {
			HAPMapping mapping = this.m_mappings.get(name);
			mapping.updateRootName(updateName);
		}
	}

	public void addAssociation(String targetName, HAPMapping mapping) {	
		if(HAPBasicUtility.isStringEmpty(targetName))  targetName = HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT;
		this.m_mappings.put(targetName, mapping);
	}
	
	public Map<String, HAPMapping> getMappings(){   return this.m_mappings;  }
	
	public HAPMapping getMapping(String targetName) {   return this.m_mappings.get(targetName);    }
	public HAPMapping getMapping() {   return this.m_mappings.get(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT);    }
 
	@Override
	public HAPDefinitionDataAssociationMapping cloneDataAssocation() {
		HAPDefinitionDataAssociationMapping out = new HAPDefinitionDataAssociationMapping();
		this.cloneToEntityInfo(out);
		for(String name : this.m_mappings.keySet()) {
			out.m_mappings.put(name, (HAPMapping)this.m_mappings.get(name).cloneStructure());
		}
		
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(MAPPING, HAPJsonUtility.buildJson(this.m_mappings, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject daJsonObj = (JSONObject)json;
			this.buildEntityInfoByJson(daJsonObj);
			
			JSONObject elesJson = daJsonObj.optJSONObject(HAPValueStructureDefinitionFlat.FLAT);
			if(elesJson!=null) {
				//seperate by target name
				Map<String, JSONObject> jsonMapByTarget = new LinkedHashMap<String, JSONObject>();
				for(Object key : elesJson.keySet()) {
					HAPTarget target = new HAPTarget((String)key);
					String targetName = target.getTargetName();
					JSONObject jsonByTarget = jsonMapByTarget.get(targetName);
					if(jsonByTarget==null) {
						jsonByTarget = new JSONObject();
						jsonMapByTarget.put(targetName, jsonByTarget);
					}
					jsonByTarget.put(target.getRootNodeId().getFullName(), elesJson.optJSONObject((String)key));
				}
				
				for(String targetName : jsonMapByTarget.keySet()) {
					HAPValueStructureDefinitionFlat association = new HAPValueStructureDefinitionFlat();
					JSONObject targetAssociationJson = new JSONObject();
					targetAssociationJson.put(HAPValueStructureDefinitionFlat.FLAT, jsonMapByTarget.get(targetName));
					association.buildObject(targetAssociationJson, HAPSerializationFormat.JSON);
					this.addAssociation(targetName, association);
				}
			}
			else {
				JSONObject associationsJson = daJsonObj.optJSONObject(MAPPING);
				for(Object key : associationsJson.keySet()) {
					String targetName = (String)key;
					HAPValueStructureDefinitionFlat association = new HAPValueStructureDefinitionFlat();
					association.buildObject(associationsJson.getJSONObject(targetName), HAPSerializationFormat.JSON);
					this.addAssociation(targetName, association);
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

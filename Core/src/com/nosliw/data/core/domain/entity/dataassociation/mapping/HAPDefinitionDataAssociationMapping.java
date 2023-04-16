package com.nosliw.data.core.domain.entity.dataassociation.mapping;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.domain.entity.dataassociation.HAPDefinitionDataAssociation;

public class HAPDefinitionDataAssociationMapping extends HAPDefinitionDataAssociation{

	@HAPAttribute
	public static String TARGET = "target";

	private Map<String, HAPDefinitionValueMapping> m_mappings;
	
	public HAPDefinitionDataAssociationMapping() {
		super(HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING);
		this.m_mappings = new LinkedHashMap<String, HAPDefinitionValueMapping>();
	}
 
	public void addAssociation(String targetName, HAPDefinitionValueMapping mapping) {	
		if(HAPUtilityBasic.isStringEmpty(targetName))  targetName = HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT;
		this.m_mappings.put(targetName, mapping);
	}
	
	public Map<String, HAPDefinitionValueMapping> getMappings(){   return this.m_mappings;  }
	
	public HAPDefinitionValueMapping getMapping(String targetName, boolean createIfNotExist) {
		HAPDefinitionValueMapping out = null;
		if(HAPUtilityBasic.isStringEmpty(targetName))  targetName = HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT;
		out = this.m_mappings.get(targetName);
		if(out==null&&createIfNotExist) {
			out = new HAPDefinitionValueMapping();
			this.addAssociation(targetName, out);
		}
		return out;
	}
	public HAPDefinitionValueMapping getMapping(String targetName) {   return this.getMapping(targetName, false);    }
	public HAPDefinitionValueMapping getMapping() {   return this.m_mappings.get(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT);    }
 
	@Override
	public HAPDefinitionDataAssociationMapping cloneDataAssocation() {
		HAPDefinitionDataAssociationMapping out = new HAPDefinitionDataAssociationMapping();
		this.cloneToDataAssociation(out);
		return out;
	}
	
	protected void cloneToDataAssociation(HAPDefinitionDataAssociationMapping dataAssociation) {
		super.cloneToDataAssociation(dataAssociation);
		for(String name : this.m_mappings.keySet()) {
			dataAssociation.m_mappings.put(name, this.m_mappings.get(name).cloneValueMapping());
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TARGET, HAPUtilityJson.buildJson(this.m_mappings, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		try{
			JSONObject daJsonObj = (JSONObject)json;
			
			JSONObject elesJson = daJsonObj.optJSONObject(HAPDefinitionValueMapping.MAPPING);
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
					HAPDefinitionValueMapping mapping = new HAPDefinitionValueMapping();
					JSONObject targetAssociationJson = new JSONObject();
					targetAssociationJson.put(HAPDefinitionValueMapping.MAPPING, jsonMapByTarget.get(targetName));
					mapping.buildObject(targetAssociationJson, HAPSerializationFormat.JSON);
					this.addAssociation(targetName, mapping);
				}
			}
			else {
				JSONObject associationsJson = daJsonObj.optJSONObject(TARGET);
				if(associationsJson!=null) {
					for(Object key : associationsJson.keySet()) {
						String targetName = (String)key;
						HAPDefinitionValueMapping association = new HAPDefinitionValueMapping();
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

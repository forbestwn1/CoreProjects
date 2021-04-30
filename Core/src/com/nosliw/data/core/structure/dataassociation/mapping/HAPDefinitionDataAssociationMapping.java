package com.nosliw.data.core.structure.dataassociation.mapping;

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
import com.nosliw.data.core.structure.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionFlat;

public class HAPDefinitionDataAssociationMapping extends HAPEntityInfoWritableImp implements HAPDefinitionDataAssociation{

	@HAPAttribute
	public static String ASSOCIATION = "association";

	private Map<String, HAPStructureValueDefinitionFlat> m_assocations;
	
	public HAPDefinitionDataAssociationMapping() {
		this.m_assocations = new LinkedHashMap<String, HAPStructureValueDefinitionFlat>();
	}
 
	@Override
	public String getType() {  return HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING;  }

	@Override
	public void updateInputVarName(HAPUpdateName updateName) {
		for(String name : this.m_assocations.keySet()) {
			HAPStructureValueDefinitionFlat association = this.m_assocations.get(name);
			association.updateReferenceName(updateName);
		}
	}

	@Override
	public void updateOutputVarName(HAPUpdateName updateName) {
		for(String name : this.m_assocations.keySet()) {
			HAPStructureValueDefinitionFlat association = this.m_assocations.get(name);
			association.updateRootName(updateName);
		}
	}

	public void addAssociation(String targetName, HAPStructureValueDefinitionFlat association) {	
		if(HAPBasicUtility.isStringEmpty(targetName))  targetName = HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT;
		this.m_assocations.put(targetName, association);
	}
	
	public Map<String, HAPStructureValueDefinitionFlat> getAssociations(){   return this.m_assocations;  }
	
	public HAPStructureValueDefinitionFlat getAssociation(String targetName) {   return this.m_assocations.get(targetName);    }
	public HAPStructureValueDefinitionFlat getAssociation() {   return this.m_assocations.get(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT);    }
 
	@Override
	public HAPDefinitionDataAssociationMapping cloneDataAssocation() {
		HAPDefinitionDataAssociationMapping out = new HAPDefinitionDataAssociationMapping();
		this.cloneToEntityInfo(out);
		for(String name : this.m_assocations.keySet()) {
			out.m_assocations.put(name, this.m_assocations.get(name).cloneContext());
		}
		
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(ASSOCIATION, HAPJsonUtility.buildJson(this.m_assocations, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject daJsonObj = (JSONObject)json;
			this.buildEntityInfoByJson(daJsonObj);
			
			JSONObject elesJson = daJsonObj.optJSONObject(HAPStructureValueDefinitionFlat.FLAT);
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
					HAPStructureValueDefinitionFlat association = new HAPStructureValueDefinitionFlat();
					JSONObject targetAssociationJson = new JSONObject();
					targetAssociationJson.put(HAPStructureValueDefinitionFlat.FLAT, jsonMapByTarget.get(targetName));
					association.buildObject(targetAssociationJson, HAPSerializationFormat.JSON);
					this.addAssociation(targetName, association);
				}
			}
			else {
				JSONObject associationsJson = daJsonObj.optJSONObject(ASSOCIATION);
				for(Object key : associationsJson.keySet()) {
					String targetName = (String)key;
					HAPStructureValueDefinitionFlat association = new HAPStructureValueDefinitionFlat();
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

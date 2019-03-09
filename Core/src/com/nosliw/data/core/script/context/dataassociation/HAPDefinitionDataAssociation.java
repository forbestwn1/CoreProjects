package com.nosliw.data.core.script.context.dataassociation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.context.HAPContext;

public class HAPDefinitionDataAssociation extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String ASSOCIATION = "association";

	private Map<String, HAPContext> m_assocations;
	
	public HAPDefinitionDataAssociation() {
		this.m_assocations = new LinkedHashMap<String, HAPContext>();
	}
	
	public void addAssociation(String targetName, HAPContext association) {
		this.m_assocations.put(targetName, association);
	}
	
	public Map<String, HAPContext> getAssociations(){   return this.m_assocations;  }
	
	public HAPContext getAssociation(String targetName) {   return this.m_assocations.get(targetName);    }
	public HAPContext getAssociation() {   return this.m_assocations.get(HAPConstant.DATAASSOCIATION_RELATEDENTITY_DEFAULT);    }

	public HAPDefinitionDataAssociation cloneDataAssocation() {
		HAPDefinitionDataAssociation out = new HAPDefinitionDataAssociation();
		this.cloneToEntityInfo(out);
		for(String name : this.m_assocations.keySet()) {
			out.m_assocations.put(name, this.m_assocations.get(name).cloneContext());
		}
		
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ASSOCIATION, HAPJsonUtility.buildJson(this.m_assocations, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject daJsonObj = (JSONObject)json;
			this.buildEntityInfoByJson(daJsonObj);
			
			JSONObject associationsJson = daJsonObj.optJSONObject(HAPContext.ELEMENT);
			if(associationsJson!=null) {
				//seperate by target name
				Map<String, JSONObject> jsonMapByTarget = new LinkedHashMap<String, JSONObject>();
				for(Object key : associationsJson.keySet()) {
					HAPTarget target = new HAPTarget((String)key);
					String targetName = target.getTargetName();
					JSONObject jsonByTarget = jsonMapByTarget.get(targetName);
					if(jsonByTarget==null) {
						jsonByTarget = new JSONObject();
						jsonMapByTarget.put(targetName, jsonByTarget);
					}
					jsonByTarget.put(target.getRootNodeId().getFullName(), associationsJson.optJSONObject((String)key));
				}
				
				for(String targetName : jsonMapByTarget.keySet()) {
					HAPContext association = new HAPContext();
					JSONObject targetAssociationJson = new JSONObject();
					targetAssociationJson.put(HAPContext.ELEMENT, jsonMapByTarget.get(targetName));
					association.buildObject(targetAssociationJson, HAPSerializationFormat.JSON);
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

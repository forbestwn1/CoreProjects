package com.nosliw.data.core.dataassociation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPDefinitionGroupDataAssociation extends HAPSerializableImp{

	private Map<String, HAPDefinitionDataAssociation> m_dataAssociations;
	
	public HAPDefinitionGroupDataAssociation() {
		this.m_dataAssociations = new LinkedHashMap<String, HAPDefinitionDataAssociation>();
	} 
	
	public HAPDefinitionDataAssociation getDataAssociation(String name) {  return this.m_dataAssociations.get(name);   }
	public HAPDefinitionDataAssociation getDefaultDataAssociation() {  return this.m_dataAssociations.get(this.getDefaultName());   }
	
	public void addDataAssociation(String name, HAPDefinitionDataAssociation dataAssociation) {
		if(HAPBasicUtility.isStringEmpty(name))  name = this.getDefaultName();
		this.m_dataAssociations.put(name, dataAssociation);   
	}
	public void addDataAssociation(HAPDefinitionDataAssociation dataAssociation) {   this.addDataAssociation(null, dataAssociation);    }
	
	public Map<String, HAPDefinitionDataAssociation> getDataAssociations(){  return this.m_dataAssociations;   }

	@Override
	protected String buildJson(){
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		for(String name : this.m_dataAssociations.keySet()) {
			jsonMap.put(name, this.m_dataAssociations.get(name).toStringValue(HAPSerializationFormat.JSON));
		}
		return HAPJsonUtility.buildMapJson(jsonMap); 
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONArray jsonArray = (JSONArray)json;
		for(int i=0; i<jsonArray.length(); i++) {
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			HAPDefinitionDataAssociation daItem = HAPParserDataAssociation.buildDefinitionByJson(jsonObj); 
			String daName = daItem.getName();
			if(HAPBasicUtility.isStringEmpty(daName))  daName = this.getDefaultName();
			this.m_dataAssociations.put(daName, daItem);
		}
		return true;
	}
	
	private String getDefaultName() {  return HAPConstantShared.GLOBAL_VALUE_DEFAULT;   }
	
	public HAPDefinitionGroupDataAssociation cloneGroupDataAssociation() {
		HAPDefinitionGroupDataAssociation out = new HAPDefinitionGroupDataAssociation();
		for(String name : this.m_dataAssociations.keySet()) {
			out.m_dataAssociations.put(name, this.m_dataAssociations.get(name).cloneDataAssocation());
		}
		return out;
	}
}

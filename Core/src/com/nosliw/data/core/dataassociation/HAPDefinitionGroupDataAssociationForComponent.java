package com.nosliw.data.core.dataassociation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPDefinitionGroupDataAssociationForComponent extends HAPSerializableImp{

	private List<HAPDefinitionDataAssociation> m_dataAssociations;
	
	public HAPDefinitionGroupDataAssociationForComponent() {
		this.m_dataAssociations = new ArrayList<HAPDefinitionDataAssociation>();
	} 
	
//	public HAPDefinitionDataAssociation getDataAssociation(String name) {  return this.m_dataAssociations.get(name);   }
//	public HAPDefinitionDataAssociation getDefaultDataAssociation() {  return this.m_dataAssociations.get(this.getDefaultName());   }
	
//	public void addDataAssociation(String name, HAPDefinitionDataAssociation dataAssociation) {
//		if(HAPBasicUtility.isStringEmpty(name))  name = this.getDefaultName();
//		this.m_dataAssociations.put(name, dataAssociation);   
//	}
//	public void addDataAssociation(HAPDefinitionDataAssociation dataAssociation) {   this.addDataAssociation(null, dataAssociation);    }
	
	public List<HAPDefinitionDataAssociation> getDataAssociations(){  return this.m_dataAssociations;   }

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
		if(json instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray)json;
			for(int i=0; i<jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				HAPDefinitionDataAssociation daItem = buildDataAssociatioinByJson(jsonArray.getJSONObject(i));
				this.m_dataAssociations.add(daItem);
			}
		}
		else {
			HAPDefinitionDataAssociation daItem = buildDataAssociatioinByJson((JSONObject)json);
			this.m_dataAssociations.add(daItem);
		}
		
		return true;
	}
	
	private HAPDefinitionDataAssociation buildDataAssociatioinByJson(JSONObject jsonObj) {
		HAPDefinitionDataAssociation out = HAPParserDataAssociation.buildDefinitionByJson(jsonObj); 
		String daName = out.getName();
		if(HAPBasicUtility.isStringEmpty(daName))  daName = this.getDefaultName();
		out.setName(daName);
		return out;
	}
	
	private String getDefaultName() {  return HAPConstantShared.GLOBAL_VALUE_DEFAULT;   }
	
	public HAPDefinitionGroupDataAssociationForComponent cloneGroupDataAssociation() {
		HAPDefinitionGroupDataAssociationForComponent out = new HAPDefinitionGroupDataAssociationForComponent();
		for(String name : this.m_dataAssociations.keySet()) {
			out.m_dataAssociations.put(name, this.m_dataAssociations.get(name).cloneDataAssocation());
		}
		return out;
	}
}

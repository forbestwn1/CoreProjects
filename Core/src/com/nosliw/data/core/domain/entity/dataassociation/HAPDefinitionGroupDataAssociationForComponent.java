package com.nosliw.data.core.domain.entity.dataassociation;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPDefinitionGroupDataAssociationForComponent extends HAPSerializableImp{

	private List<HAPDefinitionDataAssociation> m_dataAssociations;
	
	public HAPDefinitionGroupDataAssociationForComponent() {
		this.m_dataAssociations = new ArrayList<HAPDefinitionDataAssociation>();
	}
	
	public List<HAPDefinitionDataAssociation> getDataAssociations(){  return this.m_dataAssociations;   }
	public void addDataAssociation(HAPDefinitionDataAssociation dataAssociation) 
	{
		if(HAPUtilityBasic.isStringEmpty(dataAssociation.getName()))   dataAssociation.setName(getDefaultName());
		this.m_dataAssociations.add(dataAssociation);
	}

	@Override
	protected String buildJson(){
		List<String> jsonArray = new ArrayList<String>();
		for(HAPDefinitionDataAssociation dataAssociation : this.m_dataAssociations) {
			jsonArray.add(dataAssociation.toStringValue(HAPSerializationFormat.JSON));
		}
		return HAPUtilityJson.buildArrayJson(jsonArray.toArray(new String[0]));
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
		if(HAPUtilityBasic.isStringEmpty(daName))  daName = this.getDefaultName();
		out.setName(daName);
		return out;
	}
	
	private String getDefaultName() {  return HAPConstantShared.GLOBAL_VALUE_DEFAULT;   }
	
	public HAPDefinitionGroupDataAssociationForComponent cloneGroupDataAssociation() {
		HAPDefinitionGroupDataAssociationForComponent out = new HAPDefinitionGroupDataAssociationForComponent();
		for(HAPDefinitionDataAssociation dataAssociation : this.m_dataAssociations) {
			out.addDataAssociation(dataAssociation.cloneDataAssocation());
		}
		return out;
	}
}

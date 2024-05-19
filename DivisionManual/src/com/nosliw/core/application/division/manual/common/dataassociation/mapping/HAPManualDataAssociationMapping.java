package com.nosliw.core.application.division.manual.common.dataassociation.mapping;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociation;

public class HAPManualDataAssociationMapping extends HAPManualDataAssociation{

	@HAPAttribute
	public static final String MAPPING = "mapping";

	private List<HAPManualItemValueMapping> m_items;

	public HAPManualDataAssociationMapping() {
		super(HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING);
		this.m_items = new LinkedList<HAPManualItemValueMapping>();
	}
 
	public void addItem(HAPManualItemValueMapping item) { 	this.m_items.add(item); 	}
	
	public List<HAPManualItemValueMapping> getItems(){   return this.m_items;    }

	public boolean isEmpty() {   return this.getItems().isEmpty();   }

	@Override
	public HAPManualDataAssociationMapping cloneDataAssocation() {
		HAPManualDataAssociationMapping out = new HAPManualDataAssociationMapping();
		this.cloneToDataAssociation(out);
		return out;
	}
	
	protected void cloneToDataAssociation(HAPManualDataAssociationMapping dataAssociation) {
		super.cloneToDataAssociation(dataAssociation);
		for(HAPManualItemValueMapping item : this.m_items) {
			dataAssociation.addItem(item.cloneValueMappingItem());
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(MAPPING, HAPUtilityJson.buildJson(this.m_items, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;

			Object mappingObj = jsonObj.opt(MAPPING);
			if(mappingObj==null) {
				mappingObj = jsonObj;
			}
			
			List<HAPManualItemValueMapping> items = HAPManualParserValueMapping.parses(mappingObj);
			this.m_items.addAll(items);
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}

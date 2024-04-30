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
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;

public class HAPManualDataAssociationMapping extends HAPManualDataAssociation{

	@HAPAttribute
	public static final String MAPPING = "mapping";

	@HAPAttribute
	public static final String DIRECTION = "direction";

	private List<HAPItemValueMapping> m_items;

	private String m_direction;

	public HAPManualDataAssociationMapping() {
		super(HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING);
		this.m_items = new LinkedList<HAPItemValueMapping>();
		this.m_direction = HAPConstantShared.DATAASSOCIATION_DIRECTION_DOWNSTREAM;
	}
 
	public void addItem(HAPItemValueMapping item) { 	this.m_items.add(item); 	}
	
	public List<HAPItemValueMapping> getItems(){   return this.m_items;    }

	public boolean isEmpty() {   return this.getItems().isEmpty();   }

	public String getDirection() {    return this.m_direction;    }
	public void setDirection(String direction) {    this.m_direction = direction;      }

	@Override
	public HAPManualDataAssociationMapping cloneDataAssocation() {
		HAPManualDataAssociationMapping out = new HAPManualDataAssociationMapping();
		this.cloneToDataAssociation(out);
		return out;
	}
	
	protected void cloneToDataAssociation(HAPManualDataAssociationMapping dataAssociation) {
		super.cloneToDataAssociation(dataAssociation);
		for(HAPItemValueMapping item : this.m_items) {
			dataAssociation.addItem(item.cloneValueMappingItem());
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DIRECTION, this.getDirection());
		jsonMap.put(MAPPING, HAPUtilityJson.buildJson(this.m_items, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;

			Object dirObj = jsonObj.opt(HAPDefinitionDataAssociation.DIRECTION);
			if(dirObj!=null) {
				this.m_direction = (String)dirObj;
			}
			
			Object mappingObj = jsonObj.opt(MAPPING);
			if(mappingObj==null) {
				mappingObj = jsonObj;
			}
			
			List<HAPItemValueMapping> items = HAPParserValueMapping.parses(mappingObj);
			this.m_items.addAll(items);
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

}

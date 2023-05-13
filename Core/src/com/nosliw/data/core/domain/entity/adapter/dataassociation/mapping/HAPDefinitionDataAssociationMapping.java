package com.nosliw.data.core.domain.entity.adapter.dataassociation.mapping;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.adapter.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.structure.reference.HAPReferenceElementInValueContext;

public class HAPDefinitionDataAssociationMapping extends HAPDefinitionDataAssociation{

	@HAPAttribute
	public static final String MAPPING = "mapping";

	private List<HAPItemValueMapping<HAPReferenceElementInValueContext>> m_items;

	public HAPDefinitionDataAssociationMapping() {
		super(HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING);
		this.m_items = new LinkedList<HAPItemValueMapping<HAPReferenceElementInValueContext>>();
	}
 
	public void addItem(HAPItemValueMapping<HAPReferenceElementInValueContext> item) { 	this.m_items.add(item); 	}
	
	public List<HAPItemValueMapping<HAPReferenceElementInValueContext>> getItems(){   return this.m_items;    }

	public boolean isEmpty() {   return this.getItems().isEmpty();   }

	@Override
	public HAPDefinitionDataAssociationMapping cloneDataAssocation() {
		HAPDefinitionDataAssociationMapping out = new HAPDefinitionDataAssociationMapping();
		this.cloneToDataAssociation(out);
		return out;
	}
	
	protected void cloneToDataAssociation(HAPDefinitionDataAssociationMapping dataAssociation) {
		super.cloneToDataAssociation(dataAssociation);
		for(HAPItemValueMapping item : this.m_items) {
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
			if(mappingObj==null)   mappingObj = jsonObj;
			
			List<HAPItemValueMapping<HAPReferenceElementInValueContext>> items = HAPParserValueMapping.parses(mappingObj);
			this.m_items.addAll(items);
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

}

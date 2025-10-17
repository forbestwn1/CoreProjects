package com.nosliw.core.application.common.dataassociation.definition;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPDefinitionDataAssociationMapping extends HAPDefinitionDataAssociation{

	@HAPAttribute
	public static final String MAPPING = "mapping";

	private List<HAPDefinitionMappingItemValue> m_items;

	public HAPDefinitionDataAssociationMapping() {
		super(HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING);
		this.m_items = new LinkedList<HAPDefinitionMappingItemValue>();
	}
 
	public void addItem(HAPDefinitionMappingItemValue item) { 	this.m_items.add(item); 	}
	
	public List<HAPDefinitionMappingItemValue> getItems(){   return this.m_items;    }

	public boolean isEmpty() {   return this.getItems().isEmpty();   }

	@Override
	public HAPDefinitionDataAssociationMapping cloneDataAssocation() {
		HAPDefinitionDataAssociationMapping out = new HAPDefinitionDataAssociationMapping();
		this.cloneToDataAssociation(out);
		return out;
	}
	
	protected void cloneToDataAssociation(HAPDefinitionDataAssociationMapping dataAssociation) {
		super.cloneToDataAssociation(dataAssociation);
		for(HAPDefinitionMappingItemValue item : this.m_items) {
			dataAssociation.addItem(item.cloneValueMappingItem());
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(MAPPING, HAPUtilityJson.buildJson(this.m_items, HAPSerializationFormat.JSON));
	}
	
}

package com.nosliw.core.application.division.manual.brick.dataexpression.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.common.dataexpression.HAPContainerDataExpression;

public class HAPManualDefinitionContainerDataExpression extends HAPSerializableImp{

	private List<HAPManualDefinitionDataExpressionItemInContainer> m_items;
	
	public HAPManualDefinitionContainerDataExpression() {
		this.m_items = new ArrayList<HAPManualDefinitionDataExpressionItemInContainer>();
	}
	
	public void addItem(HAPManualDefinitionDataExpressionItemInContainer item) {
		this.m_items.add(item);
	}
	
	public List<HAPManualDefinitionDataExpressionItemInContainer> getItems(){    return this.m_items;      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		List<String> itemsStr = new ArrayList<String>();
		for(HAPManualDefinitionDataExpressionItemInContainer item : this.m_items) {
			itemsStr.add(item.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(HAPContainerDataExpression.ITEM, HAPUtilityJson.buildArrayJson(itemsStr.toArray(new String[0])));
	}
	
}

package com.nosliw.core.application.division.manual.brick.scriptexpression.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.common.dataexpression.HAPContainerDataExpression;

public class HAPManualDefinitionContainerScriptExpression extends HAPSerializableImp{

	private List<HAPManualDefinitionScriptExpressionItemInContainer> m_items;
	
	public HAPManualDefinitionContainerScriptExpression() {
		this.m_items = new ArrayList<HAPManualDefinitionScriptExpressionItemInContainer>();
	}
	
	public void addItem(HAPManualDefinitionScriptExpressionItemInContainer item) {
		this.m_items.add(item);
	}
	
	public List<HAPManualDefinitionScriptExpressionItemInContainer> getItems(){    return this.m_items;      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		List<String> itemsStr = new ArrayList<String>();
		for(HAPManualDefinitionScriptExpressionItemInContainer item : this.m_items) {
			itemsStr.add(item.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(HAPContainerDataExpression.ITEM, HAPUtilityJson.buildArrayJson(itemsStr.toArray(new String[0])));
	}
	
}

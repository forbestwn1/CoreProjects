package com.nosliw.core.application.brick.dataexpression.group;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;

@HAPEntityWithAttribute
public class HAPGroupDataExpression extends HAPSerializableImp{

	@HAPAttribute
	public static String ITEM = "item";

	private List<HAPItemInGroupDataExpression> m_items;
	
	public HAPGroupDataExpression() {
		this.m_items = new ArrayList<HAPItemInGroupDataExpression>();
	}
	
	public List<HAPItemInGroupDataExpression> getItems() {    return this.m_items;     }
	
	public void addItem(HAPItemInGroupDataExpression item) {     this.m_items.add(item);       }
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		Map<String, String> itemMap = new LinkedHashMap<String, String>();
		for(HAPItemInGroupDataExpression item : this.m_items) {
			itemMap.put(item.getName(), item.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		}
		jsonMap.put(ITEM, HAPUtilityJson.buildMapJson(itemMap));
	}
	
}

package com.nosliw.core.application.common.dataexpression;

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

	private List<HAPElementInGroupDataExpression> m_items;
	
	public HAPGroupDataExpression() {
		this.m_items = new ArrayList<HAPElementInGroupDataExpression>();
	}

	public void addItem(HAPElementInGroupDataExpression item) {    this.m_items.add(item);     }
	public List<HAPElementInGroupDataExpression> getItems(){   return this.m_items;    }
	
	public String addItem(HAPDataExpression dataExpression) {
		
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		Map<String, String> itemMap = new LinkedHashMap<String, String>();
		for(HAPElementInGroupDataExpression item : this.m_items) {
			itemMap.put(item.getId(), item.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ITEM, HAPUtilityJson.buildMapJson(itemMap));
	}

}

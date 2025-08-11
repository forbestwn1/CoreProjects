package com.nosliw.core.xxx.application.common.dataexpression1;

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
public class HAPContainerDataExpression extends HAPSerializableImp{

	@HAPAttribute
	public static String ITEM = "item";

	private List<HAPElementInContainerDataExpression> m_items;
	
	private int m_idIndex;
	
	public HAPContainerDataExpression() {
		this.m_items = new ArrayList<HAPElementInContainerDataExpression>();
		this.m_idIndex = 0;
	}

	public void addItem(HAPElementInContainerDataExpression item) {    this.m_items.add(item);     }
	public List<HAPElementInContainerDataExpression> getItems(){   return this.m_items;    }
	
	public String addItem(HAPDataExpression dataExpression) {
		String id = this.m_idIndex+"";
		this.m_idIndex++;
		HAPElementInContainerDataExpression item = new HAPElementInContainerDataExpression(dataExpression);
		item.setName(id);
		item.setId(id);
		this.addItem(item);
		return id;
	}
	
	public boolean isEmpty() {   return this.m_items.isEmpty();    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		Map<String, String> itemMap = new LinkedHashMap<String, String>();
		for(HAPElementInContainerDataExpression item : this.m_items) {
			itemMap.put(item.getId(), item.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ITEM, HAPUtilityJson.buildMapJson(itemMap));
	}

}

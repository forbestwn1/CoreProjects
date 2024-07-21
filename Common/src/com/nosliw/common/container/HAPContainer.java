package com.nosliw.common.container;

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
public abstract class HAPContainer<T extends HAPItemWrapper> extends HAPSerializableImp{

	@HAPAttribute
	public static String ITEM = "item";

	private List<T> m_items;
	
	private int m_idIndex;
	
	public HAPContainer() {
		this.m_items = new ArrayList<>();
		this.m_idIndex = 0;
	}

	public String addItem(T item) {    
		if(item.getId()==null) {
			String id = this.m_idIndex+"";
			this.m_idIndex++;
			item.setName(id);
			item.setId(id);
		}
		this.m_items.add(item);
		return item.getId();
	}
	public List<T> getItems(){   return this.m_items;    }
	
	public T getDataExpressionItem(String id) {
		for(T item : this.m_items) {
			if(id.equals(item.getId())) {
				return item;
			}
		}
		return null;
	}
	
	public boolean isEmpty() {   return this.m_items.isEmpty();    }
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		Map<String, String> itemMap = new LinkedHashMap<String, String>();
		for(T item : this.m_items) {
			itemMap.put(item.getId(), item.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		}
		jsonMap.put(ITEM, HAPUtilityJson.buildMapJson(itemMap));
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		Map<String, String> itemMap = new LinkedHashMap<String, String>();
		for(T item : this.m_items) {
			itemMap.put(item.getId(), item.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ITEM, HAPUtilityJson.buildMapJson(itemMap));
	}
}

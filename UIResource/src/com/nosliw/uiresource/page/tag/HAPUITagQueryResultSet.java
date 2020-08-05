package com.nosliw.uiresource.page.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPUITagQueryResultSet extends HAPSerializableImp{

	@HAPAttribute
	public static final String ITEMS = "items";

	private List<HAPUITagQueryResult> m_item;
	
	public HAPUITagQueryResultSet() {
		this.m_item = new ArrayList<HAPUITagQueryResult>();
	}

	public void addItem(HAPUITagQueryResult item) {   this.m_item.add(item);   }
	
	public List<HAPUITagQueryResult> getItems(){    return this.m_item;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ITEMS, HAPJsonUtility.buildJson(this.m_item, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		JSONArray itemArray = jsonObj.optJSONArray(ITEMS);
		if(itemArray!=null) {
			for(int i=0; i<itemArray.length(); i++) {
				HAPUITagQueryResult item = new HAPUITagQueryResult();
				item.buildObject(itemArray.get(i), HAPSerializationFormat.JSON);
			}
		}
		return true;  
	}	
}

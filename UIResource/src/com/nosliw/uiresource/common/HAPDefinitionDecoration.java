package com.nosliw.uiresource.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;

@HAPEntityWithAttribute
public class HAPDefinitionDecoration extends HAPSerializableImp{

	@HAPAttribute
	public static String SHARE = "share";

	@HAPAttribute
	public static String PARTS = "parts";

	//decoration shared by parts
	private List<HAPInfoDecoration> m_share;
	
	//decoration specific for part
	private Map<String, List<HAPInfoDecoration>> m_parts;
	
	public HAPDefinitionDecoration() {
		this.m_share = new ArrayList<HAPInfoDecoration>();
		this.m_parts = new LinkedHashMap<String, List<HAPInfoDecoration>>();
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			
			JSONArray shareArray = jsonObj.optJSONArray(SHARE);
			if(shareArray!=null) {
				this.m_share = HAPSerializeUtility.buildListFromJsonArray(HAPInfoDecoration.class.getName(), shareArray);
			}

			JSONObject partsObj = jsonObj.optJSONObject(PARTS);
			if(partsObj!=null) {
				for(Object key : partsObj.keySet()) {
					String partName = (String)key;
					JSONArray decArray = partsObj.getJSONArray(partName);
					this.m_parts.put(partName, HAPSerializeUtility.buildListFromJsonArray(HAPInfoDecoration.class.getName(), decArray));
				}
			}
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SHARE, HAPJsonUtility.buildJson(this.m_share, HAPSerializationFormat.JSON));
		jsonMap.put(PARTS, HAPJsonUtility.buildJson(this.m_parts, HAPSerializationFormat.JSON));
	}
}

package com.nosliw.core.application.entity.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;

/*
 * Container information to locate service provider instance
 */
@HAPEntityWithAttribute
public class HAPQueryServiceDefinition extends HAPSerializableImp{

	@HAPAttribute
	public static String KEYWORDS = "keywords";

	private List<String> m_keywords;
	
	public HAPQueryServiceDefinition() {
		this.m_keywords = new ArrayList<String>();
	}
	
	public List<String> getKeywords(){    return this.m_keywords;    }
	public void addKeywords(String keyword) {    this.m_keywords.add(keyword);    }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			JSONArray keywordsArray = objJson.optJSONArray(KEYWORDS);
			if(keywordsArray!=null) {
				for(int i=0; i<keywordsArray.length(); i++) {
					this.m_keywords.add(keywordsArray.getString(i));
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(KEYWORDS, HAPUtilityJson.buildArrayJson(this.m_keywords.toArray(new String[0])));
	}
}

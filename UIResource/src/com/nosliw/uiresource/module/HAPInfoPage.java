package com.nosliw.uiresource.module;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;

@HAPEntityWithAttribute
public class HAPInfoPage extends HAPEntityInfoImp{

	@HAPAttribute
	public static String ID = "id";

	private String m_pageId;

	public HAPInfoPage() {}
	
	public HAPInfoPage(String pageId, String pageName, String pageDescription) {
		super(pageName, pageDescription);
		this.m_pageId = pageId;
	}

	public String getPageId() {  return this.m_pageId;   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			this.m_pageId = jsonObj.getString(ID);
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
		jsonMap.put(ID, this.m_pageId);
	}
}

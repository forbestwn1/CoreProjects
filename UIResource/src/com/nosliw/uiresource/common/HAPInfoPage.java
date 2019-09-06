package com.nosliw.uiresource.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;

@HAPEntityWithAttribute
public class HAPInfoPage extends HAPEntityInfoImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String DECORATION = "decoration";

	//page resource id
	private String m_pageId;

	//decoration info(decoration id and configure)
	private List<HAPInfoDecoration> m_decoration;
	
	public HAPInfoPage() {
		this.m_decoration = new ArrayList<HAPInfoDecoration>();
	}
	
	public HAPInfoPage(String pageId, String pageName, String pageDescription) {
		super(pageName, pageDescription);
		this.m_pageId = pageId;
	}

	public String getPageId() {  return this.m_pageId;   }

	public List<HAPInfoDecoration> getDecoration(){   return this.m_decoration;    }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			this.m_pageId = jsonObj.getString(ID);
			this.m_decoration = HAPSerializeUtility.buildListFromJsonArray(HAPInfoDecoration.class.getName(), jsonObj.optJSONArray(DECORATION));
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
		jsonMap.put(DECORATION, HAPJsonUtility.buildJson(this.m_decoration, HAPSerializationFormat.JSON));
	}
}

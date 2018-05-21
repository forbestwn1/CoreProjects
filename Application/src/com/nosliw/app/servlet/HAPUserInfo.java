package com.nosliw.app.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;

@HAPEntityWithAttribute
public class HAPUserInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String MINIAPPINFO = "miniAppInfos";
	
	private String m_id;
	
	private List<HAPMiniAppInfo> m_miniAppInfos;
	
	public HAPUserInfo() {
		this.m_miniAppInfos = new ArrayList<HAPMiniAppInfo>();
	}
	
	public String getId() {  return this.m_id;   }
	public void setId(String id) {   this.m_id = id;    }
	
	public void addMiniAppInfo(List<HAPMiniAppInfo> miniAppInfs) {	this.m_miniAppInfos.addAll(miniAppInfs);	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_id);
		jsonMap.put(MINIAPPINFO, HAPJsonUtility.buildJson(m_miniAppInfos, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_id = jsonObj.optString(ID);
		this.m_miniAppInfos = HAPSerializeUtility.buildListFromJsonArray(HAPMiniAppInfo.class.getName(), jsonObj.optJSONArray(MINIAPPINFO));
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return this.buildObjectByFullJson(json);
	}	
}

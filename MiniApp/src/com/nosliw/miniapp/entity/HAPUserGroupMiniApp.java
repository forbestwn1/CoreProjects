package com.nosliw.miniapp.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.serialization.HAPUtilitySerialize;

@HAPEntityWithAttribute
public class HAPUserGroupMiniApp extends HAPSerializableImp{

	@HAPAttribute
	public static String GROUP = "group";

	@HAPAttribute
	public static String MINIAPP = "miniApp";

	private HAPGroup m_group;
	
	private List<HAPMiniApp> m_miniApps;

	public HAPUserGroupMiniApp(HAPGroup group) {
		this.m_group = group;
		this.m_miniApps = new ArrayList<HAPMiniApp>();
	}

	public void addMiniApp(HAPMiniApp miniApp) {	this.m_miniApps.add(miniApp);	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(GROUP, HAPUtilityJson.buildJson(m_group, HAPSerializationFormat.JSON));
		jsonMap.put(MINIAPP, HAPUtilityJson.buildJson(m_miniApps, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		JSONObject groupJsonObj = jsonObj.optJSONObject(GROUP);
		this.m_group = (HAPGroup)HAPSerializeManager.getInstance().buildObject(HAPGroup.class.getName(), groupJsonObj, HAPSerializationFormat.JSON);
		this.m_miniApps = HAPUtilitySerialize.buildListFromJsonArray(HAPMiniApp.class.getName(), jsonObj.optJSONArray(MINIAPP));
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}	

}

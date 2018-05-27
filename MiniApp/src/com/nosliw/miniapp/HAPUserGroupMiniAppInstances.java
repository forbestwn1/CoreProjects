package com.nosliw.miniapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.serialization.HAPSerializeUtility;

@HAPEntityWithAttribute
public class HAPUserGroupMiniAppInstances  extends HAPSerializableImp{

	@HAPAttribute
	public static String GROUP = "group";

	@HAPAttribute
	public static String MINIAPPINSTANCES = "miniAppInstances";

	private HAPGroup m_group;
	
	private List<HAPMiniAppInstance> m_miniAppInstances;

	public HAPUserGroupMiniAppInstances(HAPGroup group) {
		this.m_group = group;
		this.m_miniAppInstances = new ArrayList<HAPMiniAppInstance>();
	}

	public void addMiniAppInstance(HAPMiniAppInstance miniAppInstance) {	this.m_miniAppInstances.add(miniAppInstance);	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(GROUP, HAPJsonUtility.buildJson(m_group, HAPSerializationFormat.JSON));
		jsonMap.put(MINIAPPINSTANCES, HAPJsonUtility.buildJson(m_miniAppInstances, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		JSONObject groupJsonObj = jsonObj.optJSONObject(GROUP);
		this.m_group = (HAPGroup)HAPSerializeManager.getInstance().buildObject(HAPGroup.class.getName(), groupJsonObj, HAPSerializationFormat.JSON);
		this.m_miniAppInstances = HAPSerializeUtility.buildListFromJsonArray(HAPMiniAppInstance.class.getName(), jsonObj.optJSONArray(MINIAPPINSTANCES));
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}	

}

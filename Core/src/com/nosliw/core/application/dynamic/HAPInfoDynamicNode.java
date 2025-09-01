package com.nosliw.core.application.dynamic;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public class HAPInfoDynamicNode extends HAPInfoDynamic{

	@HAPAttribute
	public static final String CHILD = "child";
	
	private Map<String, HAPInfoDynamic> m_children;
	
	@Override
	public String getType() {
		return HAPConstantShared.DYNAMICTASK_INFO_TYPE_NODE;
	}
	
	public void addChild(HAPInfoDynamic child) {
		this.m_children.put(child.getId(), child);
	}
	
	@Override
	public HAPInfoDynamic getChild(String childName) {   return this.m_children.get(childName);     }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CHILD, HAPManagerSerialize.getInstance().toStringValue(this.m_children.values(), HAPSerializationFormat.JSON));
	}

	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		
		JSONObject jsonObj = (JSONObject)json;

		JSONArray childArray = jsonObj.getJSONArray(CHILD);
		for(int i=0; i<childArray.length(); i++) {
			this.addChild(HAPInfoDynamic.parse(childArray.getJSONObject(i)));
		}
		return true;
	}
}

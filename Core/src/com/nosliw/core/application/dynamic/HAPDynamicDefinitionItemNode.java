package com.nosliw.core.application.dynamic;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

@HAPEntityWithAttribute
public class HAPDynamicDefinitionItemNode extends HAPDynamicDefinitionItem{

	@HAPAttribute
	public static final String CHILD = "child";
	
	private Map<String, HAPDynamicDefinitionItem> m_children;
	
	@Override
	public String getType() {
		return HAPConstantShared.DYNAMICDEFINITION_ITEMTYPE_NODE;
	}
	
	public void addChild(HAPDynamicDefinitionItem child) {
		this.m_children.put(child.getId(), child);
	}
	
	@Override
	public HAPDynamicDefinitionItem getChild(String childName) {   return this.m_children.get(childName);     }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CHILD, HAPManagerSerialize.getInstance().toStringValue(this.m_children.values(), HAPSerializationFormat.JSON));
	}

	public static HAPDynamicDefinitionItemNode parseNode(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPDynamicDefinitionItemNode out = new HAPDynamicDefinitionItemNode();
		HAPDynamicDefinitionItem.parseToDynamicInfo(out, jsonObj);
		
		JSONArray childArray = jsonObj.getJSONArray(CHILD);
		for(int i=0; i<childArray.length(); i++) {
			out.addChild(HAPDynamicDefinitionItem.parse(childArray.getJSONObject(i), dataRuleMan));
		}
		
		return out;
	}
}

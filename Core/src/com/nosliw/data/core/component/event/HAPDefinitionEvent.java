package com.nosliw.data.core.component.event;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.dataassociation.mapping.HAPValueMapping;
import com.nosliw.data.core.structure.HAPRootStructure;

public class HAPDefinitionEvent extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String VALUE = "value";

	//value structure
	private HAPValueMapping m_valueMapping;
	
	public HAPDefinitionEvent() {
		this.m_valueMapping = new HAPValueMapping();
	}

	public HAPValueMapping getValueMapping() {  return this.m_valueMapping;   }
	public void setValueMapping(HAPValueMapping valueMapping) {   this.m_valueMapping = valueMapping;  }
	
	public void addDataElement(String name, HAPRootStructure root) {
		this.m_valueMapping.addItem(name, root);
	}
	
	public HAPDefinitionEvent cloneEventDefinition(){
		HAPDefinitionEvent out = new HAPDefinitionEvent();
		this.cloneToEntityInfo(out);
		out.m_valueMapping = this.m_valueMapping.cloneValueMapping();
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUE, this.m_valueMapping.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_valueMapping = new HAPValueMapping();
		this.m_valueMapping.buildObject(jsonObj.optJSONObject(VALUE), HAPSerializationFormat.JSON);
		return true;  
	}
}

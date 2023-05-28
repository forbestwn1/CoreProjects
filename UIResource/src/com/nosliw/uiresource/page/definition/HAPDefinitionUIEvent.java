package com.nosliw.uiresource.page.definition;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.valuestructure1.HAPParserValueStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionFlat;

@HAPEntityWithAttribute
public class HAPDefinitionUIEvent extends HAPEntityInfoWritableImp{
	@HAPAttribute
	public static String DATA = "data";

	//context
	private HAPValueStructureDefinitionFlat m_dataDefinition;
	
	public HAPDefinitionUIEvent() {
		this.m_dataDefinition = new HAPValueStructureDefinitionFlat();
	}

	public HAPValueStructureDefinitionFlat getDataDefinition() {  return this.m_dataDefinition;   }
	public void setDataDefinition(HAPValueStructureDefinitionFlat dataDef) {   this.m_dataDefinition = dataDef;  }
	
	public void addDataElement(String name, HAPRootStructure node) {
		node.setName(name);
		this.m_dataDefinition.addRoot(node);  
	}
	
	public void cloneToBase(HAPDefinitionUIEvent event) {
		this.cloneToEntityInfo(event);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATA, this.m_dataDefinition.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		HAPParserValueStructure.parseValueStructureDefinitionFlat(jsonObj.optJSONObject(DATA), this.m_dataDefinition);
		return true;  
	}
}

package com.nosliw.uiresource.page.definition;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextEntity;
import com.nosliw.data.core.script.context.HAPContextNodeRoot;
import com.nosliw.data.core.script.context.HAPParserContext;

public class HAPDefinitionUIEvent extends HAPEntityInfoImp{
/*
	@HAPAttribute
	public static String DATA = "data";

	//context
	private HAPContext m_dataDefinition;
	
	public HAPDefinitionUIEvent() {
		this.m_dataDefinition = new HAPContext();
	}

	public HAPContext getDataDefinition() {  return this.m_dataDefinition;   }
	public void setDataDefinition(HAPContext dataDef) {   this.m_dataDefinition = dataDef;  }
	
	public void addDataElement(String name, HAPContextNodeRoot node) {  this.m_dataDefinition.addElement(name, node);  }
	
	public void cloneBasicTo(HAPContextEntity contextEntity) {
		contextEntity.m_name = this.m_name;
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
		HAPContextParser.parseContext(jsonObj.optJSONObject(DATA), this.m_dataDefinition);
		return true;  
	}
*/
}

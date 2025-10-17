package com.nosliw.core.application.common.datadefinition;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;
import com.nosliw.core.xxx.application.common.interactive1.HAPRequestParmInInteractive;

public class HAPDefinitionParm extends HAPEntityInfoImp{

	@HAPAttribute
	public static String DATADEFINITION = "dataDefinition";
	
	private HAPDataDefinitionWritableWithInit m_dataDefinition;
	
	public HAPDefinitionParm() {
		this.m_dataDefinition = new HAPDataDefinitionWritableWithInit();
	}
	
	public HAPDataDefinitionWritableWithInit getDataDefinition() {		return this.m_dataDefinition;	}
	public void setDataDefinition(HAPDataDefinitionWritableWithInit dataDef) {    this.m_dataDefinition = dataDef;       }
	
	public static HAPDefinitionParm buildParmFromObject(JSONObject jsonValue, HAPManagerDataRule dataRuleMan) {
		HAPDefinitionParm out = new HAPDefinitionParm();
		out.buildEntityInfoByJson(jsonValue);
		
		Object dataDefObj = jsonValue.opt(DATADEFINITION);
		if(dataDefObj!=null) {
			out.setDataDefinition(HAPParserDataDefinition.parseDataDefinitionWritableWithInit(dataDefObj, dataRuleMan));
		}
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATADEFINITION, this.m_dataDefinition.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPRequestParmInInteractive){
			HAPRequestParmInInteractive varInfo = (HAPRequestParmInInteractive)obj;
			return super.equals(varInfo);
		}
		return out;
	}
	
	public HAPDefinitionParm cloneParmDefinition() {
		HAPDefinitionParm out = new HAPDefinitionParm();
		this.cloneToEntityInfo(out);
		out.m_dataDefinition = this.m_dataDefinition.cloneDataDefinitionWritableWithInit();
		return out;
	}
	
}

package com.nosliw.data.core.process;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.HAPContext;

public class HAPDefinitionDataAssociationGroup extends HAPContext{

	@HAPAttribute
	public static String INFO = "info";

	private HAPInfo m_info;
	
	
	public HAPDefinitionDataAssociationGroup() {
		this.m_info = new HAPInfoImpSimple();
	}
	
	public HAPInfo getInfo() {   return this.m_info;   }
	

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INFO, HAPJsonUtility.buildJson(this.m_info, HAPSerializationFormat.JSON));
	}
	
	public HAPDefinitionDataAssociationGroup cloneDataAssocationGroup() {
		HAPDefinitionDataAssociationGroup out = new HAPDefinitionDataAssociationGroup();
		this.toContext(out);
		out.m_info = this.m_info.cloneInfo();
		return out;
	}
	
}

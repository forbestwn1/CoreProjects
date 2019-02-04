package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPDefinitionDataAssociationGroup extends HAPContext{

	@HAPAttribute
	public static String INFO = "info";

	@HAPAttribute
	public static String FLATOUTPUT = "flatOutput";
	
	private HAPInfo m_info;
	
	private boolean m_isFlatOutput;
	
	public HAPDefinitionDataAssociationGroup() {
		this.m_info = new HAPInfoImpSimple();
		this.m_isFlatOutput = true;
	}
	
	public HAPInfo getInfo() {   return this.m_info;   }
	
	public void setIsFlatOutput(boolean isFlatOutput) {  this.m_isFlatOutput = isFlatOutput;  }
	public boolean isFlatOutput() {  return this.m_isFlatOutput;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INFO, HAPJsonUtility.buildJson(this.m_info, HAPSerializationFormat.JSON));
		jsonMap.put(FLATOUTPUT, this.m_isFlatOutput+"");
		typeJsonMap.put(FLATOUTPUT, Boolean.class);
	}
	
	public HAPDefinitionDataAssociationGroup cloneDataAssocationGroup() {
		HAPDefinitionDataAssociationGroup out = new HAPDefinitionDataAssociationGroup();
		this.toContext(out);
		out.m_info = this.m_info.cloneInfo();
		out.m_isFlatOutput = this.m_isFlatOutput;
		return out;
	}
	
}

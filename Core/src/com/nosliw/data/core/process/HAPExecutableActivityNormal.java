package com.nosliw.data.core.process;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPContextFlat;

public abstract class HAPExecutableActivityNormal extends HAPExecutableActivity{

	@HAPAttribute
	public static String INPUT = "input";
	
	@HAPAttribute
	public static String OUTPUT = "output";

	private HAPDefinitionDataAssociationGroupExecutable m_input;

	private HAPDefinitionDataAssociationGroupExecutable m_output;
	
	public HAPExecutableActivityNormal(String id, HAPDefinitionActivity activityDef) {
		super(id, activityDef);
	}

	public void setInputDataAssociation(HAPDefinitionDataAssociationGroupExecutable input) {  this.m_input = input;  }

	public HAPContextFlat getInputContext() {
		if(this.m_input==null)   return null;
		return this.m_input.getContext();   
	}
	
	public void setOutputDataAssociation(HAPDefinitionDataAssociationGroupExecutable output) {  this.m_output = output;  }
	public HAPDefinitionDataAssociationGroupExecutable getOutputDataAssociation() {  return this.m_output;   }
	
	public HAPContextFlat getOutputContext() {
		if(this.m_output==null)   return null;
		return this.m_output.getContext();
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INPUT, this.m_input.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(OUTPUT, this.m_output.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(INPUT, this.m_input.toResourceData(runtimeInfo).toString());
		jsonMap.put(OUTPUT, this.m_output.toResourceData(runtimeInfo).toString());
	}	
}

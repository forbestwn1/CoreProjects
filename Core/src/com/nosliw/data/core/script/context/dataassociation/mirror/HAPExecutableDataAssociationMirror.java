package com.nosliw.data.core.script.context.dataassociation.mirror;

import java.util.Map;

import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociationImp;
import com.nosliw.data.core.script.context.dataassociation.HAPOutputStructure;

public class HAPExecutableDataAssociationMirror extends HAPExecutableDataAssociationImp{

	private HAPOutputStructure m_output;
	
	public HAPExecutableDataAssociationMirror(HAPDefinitionDataAssociationMirror definition, HAPParentContext input) {
		super(definition, input);
		this.m_output = new HAPOutputStructure();
	}
	
	@Override
	public HAPOutputStructure getOutput() {  return this.m_output;  }
	
	public void addOutputStructure(String name, HAPContextStructure context) {
		this.m_output.addOutputStructure(name, context);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
	}
}

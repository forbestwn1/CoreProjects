package com.nosliw.data.core.script.context.dataassociation.mirror;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPOutputStructure;

public class HAPExecutableDataAssociationMirror extends HAPExecutableImp implements HAPExecutableDataAssociation{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String DEFINITION = "definition";

	private HAPContextStructure m_input;
	
	private HAPDefinitionDataAssociationMirror m_definition;
	
	public HAPExecutableDataAssociationMirror(HAPDefinitionDataAssociationMirror definition) {
		this.m_definition = definition;
	}
	
	@Override
	public String getType() {  return this.m_definition.getType();  }

	@Override
	public HAPOutputStructure getOutput() {
		HAPOutputStructure out = new HAPOutputStructure();
		out.addOutputStructure(m_input);
		return out;
	}

	public void setInput(HAPContextStructure input) {   this.m_input = input;    }
	
	public String getName() {   return this.m_definition.getName();   }
	
	public HAPDefinitionDataAssociationMirror getDefinition() {  return this.m_definition;   }
	public void setDefinition(HAPDefinitionDataAssociationMirror definition) {  this.m_definition = definition;   }
	
	public HAPInfo getInfo() {  return this.m_definition.getInfo();  }
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(NAME, this.getName());
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
	}
}

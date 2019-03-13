package com.nosliw.data.core.script.context.dataassociation.none;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPOutputStructure;

public class HAPExecutableDataAssociationNone extends HAPExecutableImp implements HAPExecutableDataAssociation{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String DEFINITION = "definition";

	private HAPDefinitionDataAssociationNone m_definition;
	
	public HAPExecutableDataAssociationNone(HAPDefinitionDataAssociationNone definition) {
		this.m_definition = definition;
	}
	
	@Override
	public String getType() {  return HAPConstant.DATAASSOCIATION_TYPE_NONE;  }

	@Override
	public HAPOutputStructure getOutput() {
		HAPOutputStructure out = new HAPOutputStructure();
		return out;
	}

	public String getName() {   return this.m_definition.getName();   }
	
	public HAPDefinitionDataAssociationNone getDefinition() {  return this.m_definition;   }
	public void setDefinition(HAPDefinitionDataAssociationNone definition) {  this.m_definition = definition;   }
	
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

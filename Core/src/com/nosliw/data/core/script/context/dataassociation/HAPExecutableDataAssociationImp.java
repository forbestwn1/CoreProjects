package com.nosliw.data.core.script.context.dataassociation;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoUtility;
import com.nosliw.common.info.HAPEntityInfoWritable;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPParentContext;

public abstract class HAPExecutableDataAssociationImp  extends HAPExecutableImp implements HAPExecutableDataAssociation{

	private HAPParentContext m_input;

	private HAPDefinitionDataAssociation m_definition;

	public HAPExecutableDataAssociationImp(HAPDefinitionDataAssociation definition, HAPParentContext input) {
		this.m_definition = definition;
		this.m_input = input;
	}
	
	@Override
	public String getType() {  return this.getDefinition().getType();  }

	@Override
	public HAPParentContext getInput() {	return this.m_input;	}
	public void setInput(HAPParentContext input) {    this.m_input = input;    }

	@Override
	public String getName() {   return this.m_definition.getName();   }
	@Override
	public HAPInfo getInfo() {  return this.m_definition.getInfo();  }
	
	@Override
	public String getDescription() {   return this.m_definition.getDescription();   }
 
	@Override
	public HAPDefinitionDataAssociation getDefinition() {   return this.m_definition;   }

	@Override
	public void cloneToEntityInfo(HAPEntityInfoWritable entityInfo) {
		HAPEntityInfoUtility.cloneTo(this, entityInfo);
	}

	@Override
	public void buildEntityInfoByJson(Object json) {	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		HAPEntityInfoUtility.buildJsonMap(jsonMap, this);
		jsonMap.put(DEFINITION, this.m_definition.toStringValue(HAPSerializationFormat.JSON));

		Map<String, String> outputFlatMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> outputFlatTypeMap = new LinkedHashMap<String, Class<?>>();
		for(String name : this.getOutput().getNames()) {
			outputFlatMap.put(name, this.getOutput().getOutputStructure(name).isFlat()+"");
			outputFlatTypeMap.put(name, Boolean.class);
		}
		jsonMap.put(OUTPUT, HAPJsonUtility.buildMapJson(outputFlatMap, outputFlatTypeMap));


		Map<String, String> inputFlatMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> inputFlatTypeMap = new LinkedHashMap<String, Class<?>>();
		for(String name : this.getInput().getNames()) {
			inputFlatMap.put(name, this.getInput().getContext(name).isFlat()+"");
			inputFlatTypeMap.put(name, Boolean.class);
		}
		jsonMap.put(INPUT, HAPJsonUtility.buildMapJson(inputFlatMap, inputFlatTypeMap));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
	}

}

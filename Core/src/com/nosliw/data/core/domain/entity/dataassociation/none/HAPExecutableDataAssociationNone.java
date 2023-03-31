package com.nosliw.data.core.domain.entity.dataassociation.none;

import java.util.Map;

import com.nosliw.data.core.domain.entity.dataassociation.HAPExecutableDataAssociationImp;
import com.nosliw.data.core.domain.entity.dataassociation.HAPOutputStructure;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;

public class HAPExecutableDataAssociationNone extends HAPExecutableDataAssociationImp{

	public HAPExecutableDataAssociationNone() {}

	public HAPExecutableDataAssociationNone(HAPDefinitionDataAssociationNone definition, HAPContainerStructure input, HAPContainerStructure output) {
		super(definition, input, output);
	}
	
	@Override
	public HAPContainerStructure getInput() {	return new HAPContainerStructure();	}
	
	@Override
	public HAPOutputStructure getOutput() {
		HAPOutputStructure out = new HAPOutputStructure();
		return out;
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

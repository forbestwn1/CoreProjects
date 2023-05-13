package com.nosliw.data.core.domain.entity.adapter.interactive;

import com.nosliw.data.core.complex.HAPPluginAdapterProcessor;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.common.interactive.HAPExecutableEntityInteractive;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.runtime.HAPExecutable;

public class HAPPluginAdapterProcessorDataAssociationInteractive implements HAPPluginAdapterProcessor{

	@Override
	public String getAdapterType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object process(Object adapter, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutable parentEntityExecutable, HAPContextProcessor parentContext) {

		HAPDefinitionEntityDataAssociationInteractive adapterDef = (HAPDefinitionEntityDataAssociationInteractive)adapter;
		
		HAPExecutableEntityComplex parentComplexExe = (HAPExecutableEntityComplex)parentEntityExecutable;
		
		HAPExecutableEntityInteractive interactiveExe = (HAPExecutableEntityInteractive)childEntityExecutable;
		interactiveExe.getInteractive();
		
		
		HAPExecutableGroupDataAssociationForTask 
		
		// TODO Auto-generated method stub
		return null;
	}

}

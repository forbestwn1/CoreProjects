package com.nosliw.data.core.script.context.dataassociation.none;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPParentContext;

public class HAPProcessorDataAssociationNone {

	public static HAPExecutableDataAssociationNone processDataAssociation(HAPParentContext input, HAPDefinitionDataAssociationNone dataAssociation, HAPParentContext output, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableDataAssociationNone out = new HAPExecutableDataAssociationNone(dataAssociation, input);
		return out;
	}
	
}

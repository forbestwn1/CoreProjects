package com.nosliw.data.core.dataassociation.none;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.core.application.common.dataassociation.HAPExecutableDataAssociationNone;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.valuestructure1.HAPContainerStructure;

public class HAPProcessorDataAssociationNone {

	public static HAPExecutableDataAssociationNone processDataAssociation(HAPContainerStructure input, HAPDefinitionDataAssociationNone dataAssociation, HAPContainerStructure output, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableDataAssociationNone out = new HAPExecutableDataAssociationNone(dataAssociation, input, output);
		return out;
	}
	
}

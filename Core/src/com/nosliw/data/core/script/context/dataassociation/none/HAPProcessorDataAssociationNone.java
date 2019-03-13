package com.nosliw.data.core.script.context.dataassociation.none;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;

public class HAPProcessorDataAssociationNone {

	public static HAPExecutableDataAssociationNone processDataAssociation(HAPParentContext input, HAPDefinitionDataAssociationNone dataAssociation, HAPParentContext output, HAPInfo daProcessConfigure, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociationNone out = new HAPExecutableDataAssociationNone(dataAssociation);
		return out;
	}
	
}

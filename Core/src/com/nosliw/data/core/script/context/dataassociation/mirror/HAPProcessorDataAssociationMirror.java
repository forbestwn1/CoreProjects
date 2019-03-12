package com.nosliw.data.core.script.context.dataassociation.mirror;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;

public class HAPProcessorDataAssociationMirror {

	public static HAPExecutableDataAssociationMirror processDataAssociation(HAPParentContext input, HAPDefinitionDataAssociationMirror dataAssociation, HAPParentContext output, HAPInfo daProcessConfigure, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociationMirror out = new HAPExecutableDataAssociationMirror(dataAssociation);
		out.setInput(input.getContext());
		return out;
	}
	
}

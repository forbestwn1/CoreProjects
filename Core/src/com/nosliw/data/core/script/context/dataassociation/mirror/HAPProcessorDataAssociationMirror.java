package com.nosliw.data.core.script.context.dataassociation.mirror;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContextStructure;

public class HAPProcessorDataAssociationMirror {

	public static HAPExecutableDataAssociationMirror processDataAssociation(HAPParentContext input, HAPDefinitionDataAssociationMirror dataAssociation, HAPParentContext output, HAPInfo daProcessConfigure, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociationMirror out = new HAPExecutableDataAssociationMirror(dataAssociation, input);
		out.setInput(input);
	
		HAPParentContext outContext = output.cloneParentContext();
		for(String categaryName : input.getNames()) {
			HAPContextStructure inputContext = input.getContext(categaryName);
			HAPContextStructure outputContext = outContext.getContext(categaryName);
			HAPContextStructure mergedOutputContext = HAPUtilityContextStructure.hardMergeContextStructure(inputContext, outputContext);
			outContext.addContext(categaryName, mergedOutputContext);
		}
		
		for(String name : outContext.getNames()) {
			out.addOutputStructure(name, outContext.getContext(name));
		}
		return out;
	}
}

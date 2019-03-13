package com.nosliw.data.core.script.context.dataassociation;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.script.context.dataassociation.mapping.HAPProcessorDataAssociationMapping;
import com.nosliw.data.core.script.context.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.script.context.dataassociation.mirror.HAPProcessorDataAssociationMirror;
import com.nosliw.data.core.script.context.dataassociation.none.HAPDefinitionDataAssociationNone;
import com.nosliw.data.core.script.context.dataassociation.none.HAPProcessorDataAssociationNone;

public class HAPProcessorDataAssociation {

	private static String INFO_MODIFYSTRUCTURE = "modifyStructure";
	
	public static HAPInfo withModifyStructureTrue(HAPInfo info) {
		info.setValue(INFO_MODIFYSTRUCTURE, "true");
		return info;
	}

	public static HAPInfo withModifyStructureFalse(HAPInfo info) {
		info.setValue(INFO_MODIFYSTRUCTURE, "false");
		return info;
	}
	
	public static boolean getModifyStructure(HAPInfo info) {
		boolean defaultValue = true;
		if(info==null)  return defaultValue;
		String outStr = (String)info.getValue(INFO_MODIFYSTRUCTURE, defaultValue+"");
		return Boolean.valueOf(outStr);
	}

	public static HAPExecutableDataAssociation processDataAssociation(HAPParentContext input, HAPDefinitionDataAssociation dataAssociation, HAPParentContext output, HAPInfo configure, HAPRequirementContextProcessor contextProcessRequirement) {
		if(dataAssociation==null)  dataAssociation = new HAPDefinitionDataAssociationNone();
		String type = dataAssociation.getType();
		
		switch(type) {
		case HAPConstant.DATAASSOCIATION_TYPE_MAPPING:
			return HAPProcessorDataAssociationMapping.processDataAssociation(input, (HAPDefinitionDataAssociationMapping)dataAssociation, output, configure, contextProcessRequirement);
		
		case HAPConstant.DATAASSOCIATION_TYPE_MIRROR:
			return HAPProcessorDataAssociationMirror.processDataAssociation(input, (HAPDefinitionDataAssociationMirror)dataAssociation, output, configure, contextProcessRequirement);

		case HAPConstant.DATAASSOCIATION_TYPE_NONE:
			return HAPProcessorDataAssociationNone.processDataAssociation(input, (HAPDefinitionDataAssociationNone)dataAssociation, output, configure, contextProcessRequirement);
		}
		
		return null;
	}
}

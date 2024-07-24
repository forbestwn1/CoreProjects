package com.nosliw.core.application.division.manual.common.attachment;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickRelation;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickRelationAttachment;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperBrick;

public class HAPManualUtilityAttachment {

	public static final String ATTRIBUTE_OVERRIDE_MODE = "overide"; 
	public static final String OVERRIDE_MODE_CONFIGURABLE = "configurable"; 
	public static final String OVERRIDE_MODE_NONE = "none"; 

	public static final String ATTRIBUTE_FLAG_OVERRIDE = "flagOveride"; 


	public static void setOverridenByParent(HAPManualDefinitionWrapperBrick ele) {
		ele.getInfo().setValue(ATTRIBUTE_FLAG_OVERRIDE, Boolean.TRUE);
	}
	
	public static boolean isOverridenByParent(HAPManualDefinitionWrapperBrick ele) {
		return Boolean.TRUE.equals(ele.getInfo().getValue(ATTRIBUTE_FLAG_OVERRIDE));
	}
	
	public static boolean isOverridenByParentMode(HAPManualDefinitionWrapperBrick ele) {
		String mode = (String)ele.getInfo().getValue(ATTRIBUTE_OVERRIDE_MODE);
		if(mode==null) {
			mode =  OVERRIDE_MODE_NONE;
		}
		return OVERRIDE_MODE_CONFIGURABLE.equals(mode);
	}

	public static HAPManualDefinitionBrickRelationAttachment resolveAttachmentRelation(HAPManualDefinitionAttributeInBrick attrDef, HAPManualDefinitionBrickRelationAttachment defaultRelation) {
		HAPManualDefinitionBrickRelationAttachment out = new HAPManualDefinitionBrickRelationAttachment();
		out.mergeHard(defaultRelation);
		for(HAPManualDefinitionBrickRelation relation : attrDef.getRelations()) {
			if(relation.getType().equals(HAPConstantShared.MANUAL_RELATION_TYPE_ATTACHMENT)) {
				out.mergeHard((HAPManualDefinitionBrickRelationAttachment)relation);
			}
		}
		return out;
	}
	

}

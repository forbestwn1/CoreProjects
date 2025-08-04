package com.nosliw.core.application.division.manual.common.attachment;

import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelation;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelationAttachment;
import com.nosliw.core.application.division.manua.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manua.HAPManualWithBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperBrickRoot;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValue;

public class HAPManualUtilityAttachment {

	public static final String ATTRIBUTE_OVERRIDE_MODE = "overide"; 
	public static final String OVERRIDE_MODE_CONFIGURABLE = "configurable"; 
	public static final String OVERRIDE_MODE_NONE = "none"; 

	public static final String ATTRIBUTE_FLAG_OVERRIDE = "flagOveride"; 

	public static void setOverridenByParent(HAPManualDefinitionWrapperBrickRoot ele) {
		ele.getInfo().setValue(ATTRIBUTE_FLAG_OVERRIDE, Boolean.TRUE);
	}
	
	public static boolean isOverridenByParent(HAPManualDefinitionWrapperBrickRoot ele) {
		return Boolean.TRUE.equals(ele.getInfo().getValue(ATTRIBUTE_FLAG_OVERRIDE));
	}
	
	public static boolean isOverridenByParentMode(HAPManualDefinitionWrapperBrickRoot ele) {
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
	
	public static void processAttachment(HAPManualDefinitionBrick brickDef, HAPManualDefinitionBrickRelationAttachment defaultRelation, HAPManualContextProcessBrick processContext) {
		HAPManualDefinitionAttachment parentAttachment = brickDef.getAttachment();
		List<HAPManualDefinitionAttributeInBrick> attrsDef = brickDef.getAllAttributes();
		for(HAPManualDefinitionAttributeInBrick attrDef : attrsDef) {
			HAPManualDefinitionWrapperValue attrValueInfo = attrDef.getValueWrapper();
			String attrValueType = attrValueInfo.getValueType();
			if(attrValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK)) {
				HAPManualDefinitionBrick attrBrickDef = ((HAPManualWithBrick)attrValueInfo).getBrick();
				HAPManualDefinitionAttachment attrAttachment = attrBrickDef.getAttachment();
				attrAttachment.mergeWith(parentAttachment, HAPManualUtilityAttachment.resolveAttachmentRelation(attrDef, defaultRelation).getMode());
				
				processAttachment(attrBrickDef, defaultRelation, processContext);
			}
		}
	}
	

}

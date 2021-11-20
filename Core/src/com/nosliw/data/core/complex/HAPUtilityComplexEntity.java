package com.nosliw.data.core.complex;

import java.util.List;

import com.nosliw.common.info.HAPInfoUtility;
import com.nosliw.data.core.complex.valuestructure.HAPComplexValueStructure;
import com.nosliw.data.core.complex.valuestructure.HAPPartComplexValueStructure;
import com.nosliw.data.core.complex.valuestructure.HAPPartComplexValueStructureGroupWithEntity;
import com.nosliw.data.core.complex.valuestructure.HAPUtilityComplexValueStructure;
import com.nosliw.data.core.component.HAPUtilityComponent;

public class HAPUtilityComplexEntity {

	//merge attachment from parent to child
	public static void processAttachment(HAPDefinitionEntityComplex complexEntity, HAPDefinitionEntityComplex parentEntity, HAPConfigureComplexRelationAttachment attachmentRelation) {
		HAPUtilityComponent.mergeWithParentAttachment(complexEntity, parentEntity.getAttachmentContainer());    //build attachment
	}
	
	public static void processValueStructureInheritance(HAPDefinitionEntityComplex complexEntity, HAPDefinitionEntityComplex parentEntity, HAPConfigureComplexRelationValueStructure valueStructureRelation) {
		//expand reference
		
		 //inheritance
		if(valueStructureRelation.isInheritable()) {
			HAPComplexValueStructure parentValueStructureComplex = parentEntity.getValueStructureComplex();
			List<HAPPartComplexValueStructure> parts = parentValueStructureComplex.getParts();
			
			if(valueStructureRelation.isShareRuntimeData()) {
				//share runtime data with parent
				HAPPartComplexValueStructureGroupWithEntity part = new HAPPartComplexValueStructureGroupWithEntity(HAPUtilityComplexValueStructure.createPartInfoFromParent());
				for(HAPPartComplexValueStructure parentPart : parts) {
					part.addChild(parentPart.cloneComplexValueStructurePart());
				}
				complexEntity.getValueStructureComplex().addPart(part);
			}
			else {
				//child has own data
				HAPPartComplexValueStructureGroupWithEntity part = new HAPPartComplexValueStructureGroupWithEntity(HAPUtilityComplexValueStructure.createPartInfoFromParent());
				for(HAPPartComplexValueStructure parentPart : parts) {
					part.addChild(parentPart.cloneComplexValueStructurePart());
				}
				complexEntity.getValueStructureComplex().addPart(part);
			}
		}
		
		
	}
	
	public static void processInfo(HAPDefinitionEntityComplex complexEntity, HAPDefinitionEntityComplex parentEntity, HAPConfigureComplexRelationInfo infoRelation) {
		HAPInfoUtility.softMerge(this.m_componentEntity.getInfo(), this.m_componentContainer.getInfo());

	}
	
}

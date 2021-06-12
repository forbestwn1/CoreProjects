package com.nosliw.data.core.component.valuestructure;

import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPDefinitionEntityComplex;
import com.nosliw.data.core.component.HAPWithComplexEntity;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPRootStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;
import com.nosliw.data.core.valuestructure.resource.HAPResourceDefinitionValueStructure;

public class HAPProcessorValueStructureInComponent {

	public static HAPValueStructure process(
			HAPValueStructureInComponent valueStructureInComponent,
			HAPContextProcessAttachmentReferenceValueStructure attachmentReferenceContext,
			HAPRuntimeEnvironment runtimeEnv
			) {
		HAPValueStructure out = null;
		String type = valueStructureInComponent.getStructureType();
		if(type.equals(HAPConstantShared.STRUCTURE_TYPE_VALUEFLAT)) {
			HAPValueStructureFlatInComponent flatIn = (HAPValueStructureFlatInComponent)valueStructureInComponent;
			HAPValueStructureDefinitionFlat flatOut = new HAPValueStructureDefinitionFlat();
			flatIn.cloneToFlatValueStructure(flatOut);
			for(HAPInfoReference reference : flatIn.getReferences()) {
				List<HAPRootStructure> roots = buildRoot(reference, attachmentReferenceContext, runtimeEnv);
				for(HAPRootStructure root : roots) {
					flatOut.addRoot(root);
				}
			}
		}
		else if(type.equals(HAPConstantShared.STRUCTURE_TYPE_VALUEGROUP)) {
			HAPValueStructureGroupInComponent groupIn = (HAPValueStructureGroupInComponent)valueStructureInComponent;
			HAPValueStructureDefinitionGroup groupOut = new HAPValueStructureDefinitionGroup();
			groupIn.cloneToGroupValueStructure(groupOut);
			
			Map<String, List<HAPInfoReference>> referencesByCategary = groupIn.getReferences();
			for(String categary : referencesByCategary.keySet()) {
				for(HAPInfoReference reference : referencesByCategary.get(categary)) {
					List<HAPRootStructure> roots = buildRoot(reference, attachmentReferenceContext, runtimeEnv);
					for(HAPRootStructure root : roots) {
						groupOut.addRoot(categary, root);
					}
				}
			}
		}
		
		return out;
	}
	
	private static List<HAPRootStructure> buildRoot(HAPInfoReference referenceInfo, HAPContextProcessAttachmentReferenceValueStructure attachmentReferenceContext, HAPRuntimeEnvironment runtimeEnv){
		Object ref = referenceInfo.getReference();
		if(ref instanceof String && !HAPUtilityResourceId.isResourceIdLiterate((String)ref)) {
			//attachment
			String literate = (String)ref;
		}
		else {
			//resource
			HAPResourceId resourceId = HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE, ref);
			HAPResourceDefinition relatedResource = null;
			HAPDefinitionEntityComplex contextComplexEntity = null;
			if(attachmentReferenceContext.getComplexEntity() instanceof HAPResourceDefinition) relatedResource = (HAPResourceDefinition)attachmentReferenceContext.getComplexEntity();
			HAPResourceDefinitionValueStructure resourceDefiniton = (HAPResourceDefinitionValueStructure)runtimeEnv.getResourceDefinitionManager().getResourceDefinition(resourceId, relatedResource);
			if(resourceDefiniton instanceof HAPWithComplexEntity)  contextComplexEntity = ((HAPWithComplexEntity)resourceDefiniton).getComplexEntity();
			HAPContextProcessAttachmentReferenceValueStructure attachmentReferenceContextForRefValueStructure = new HAPContextProcessAttachmentReferenceValueStructure(contextComplexEntity, runtimeEnv);

			
		}
	}
	
}

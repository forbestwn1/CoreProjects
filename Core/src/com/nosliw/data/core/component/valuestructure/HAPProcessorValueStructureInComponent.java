package com.nosliw.data.core.component.valuestructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.component.HAPDefinitionEntityComplex;
import com.nosliw.data.core.component.HAPWithComplexEntity;
import com.nosliw.data.core.component.attachment.HAPResultProcessAttachmentReference;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.structure.HAPRootStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;
import com.nosliw.data.core.valuestructure.resource.HAPResourceDefinitionValueStructure;

public class HAPProcessorValueStructureInComponent {

	public static HAPValueStructure expandReference(
			HAPValueStructureInComponent valueStructureInComponent,
			HAPContextProcessor processContext
			) {
		HAPValueStructure out = valueStructureInComponent;
		String type = valueStructureInComponent.getStructureType();
		if(type.equals(HAPConstantShared.STRUCTURE_TYPE_VALUEFLAT)) {
			HAPValueStructureFlatInComponent flatIn = (HAPValueStructureFlatInComponent)valueStructureInComponent;
			HAPValueStructureDefinitionFlat flatOut = new HAPValueStructureDefinitionFlat();
			flatIn.cloneToFlatValueStructure(flatOut);
			for(HAPInfoEntityReference reference : flatIn.getReferences()) {
				List<HAPRootStructure> roots = buildRoot(reference, processContext);
				for(HAPRootStructure root : roots) {
					flatOut.addRoot(root);
				}
			}
			out = flatOut;
		}
		else if(type.equals(HAPConstantShared.STRUCTURE_TYPE_VALUEGROUP)) {
			HAPValueStructureGroupInComponent groupIn = (HAPValueStructureGroupInComponent)valueStructureInComponent;
			HAPValueStructureDefinitionGroup groupOut = new HAPValueStructureDefinitionGroup();
			groupIn.cloneToGroupValueStructure(groupOut);
			
			Map<String, List<HAPInfoEntityReference>> referencesByCategary = groupIn.getReferences();
			for(String categary : referencesByCategary.keySet()) {
				for(HAPInfoEntityReference reference : referencesByCategary.get(categary)) {
					List<HAPRootStructure> roots = buildRoot(reference, processContext);
					for(HAPRootStructure root : roots) {
						groupOut.addRootToCategary(categary, root);
					}
				}
			}
			out = groupOut;
		}
		
		return out;
	}
	
	private static List<HAPRootStructure> buildRoot(HAPInfoEntityReference referenceInfo, HAPContextProcessor processContext){
		List<HAPRootStructure> out = new ArrayList<HAPRootStructure>();
		Object ref = referenceInfo.getReference();
		if(ref instanceof String && !HAPUtilityResourceId.isResourceIdLiterate((String)ref)) {
			//attachment
			String literate = (String)ref;
			HAPResultProcessAttachmentReference result = processContext.processAttachmentReference(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE, literate);
			out.addAll((List<HAPRootStructure>)result.getEntity());
		}
		else {
			//resource
			HAPResourceId resourceId = HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE, ref);
			HAPResourceDefinition relatedResource = null;
			HAPDefinitionEntityComplex contextComplexEntity = null;
			if(processContext.getComplexEntity() instanceof HAPResourceDefinition) relatedResource = (HAPResourceDefinition)processContext.getComplexEntity();
			HAPResourceDefinitionValueStructure resourceDefiniton = (HAPResourceDefinitionValueStructure)processContext.getRuntimeEnvironment().getResourceDefinitionManager().getResourceDefinition(resourceId, relatedResource);
			out.addAll(resourceDefiniton.getRoots());

			//reference in resource
			if(resourceDefiniton instanceof HAPWithComplexEntity)  contextComplexEntity = ((HAPWithComplexEntity)resourceDefiniton).getComplexEntity();
			HAPContextProcessor processContextForRefValueStructure = new HAPContextProcessor(contextComplexEntity, processContext.getRuntimeEnvironment());
			List<HAPInfoEntityReference> subReferences = resourceDefiniton.getReferences();
			for(HAPInfoEntityReference subReference : subReferences) {
				out.addAll(buildRoot(subReference, processContextForRefValueStructure));
			}
		}
		return out;
	}
	
}

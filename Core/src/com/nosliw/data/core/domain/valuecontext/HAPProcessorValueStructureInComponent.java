package com.nosliw.data.core.domain.valuecontext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.component.HAPDefinitionEntityElementInContainerComponent;
import com.nosliw.data.core.component.HAPWithComplexEntity;
import com.nosliw.data.core.domain.entity.attachment.HAPResultProcessAttachmentReference;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.entity.division.manual.HAPManualEntityComplex;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceDefinition1;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.valuestructure1.resource.HAPInfoEntityReference;
import com.nosliw.data.core.valuestructure1.resource.HAPResourceDefinitionValueStructure;

public class HAPProcessorValueStructureInComponent {

	//add complex to pool
	private static String buildValueStructureComplex(HAPWithValueContext withValueStructure, String parentComplexId, HAPPoolValueStructure valueStructurePool) {
		String out = null;
		if(withValueStructure instanceof HAPDefinitionEntityElementInContainerComponent) {
			HAPDefinitionEntityElementInContainerComponent containerEle = (HAPDefinitionEntityElementInContainerComponent)withValueStructure;
			String containerComplexId = valueStructurePool.addValueStructureComplex(containerEle.getContainerValueStructureComplex(), parentComplexId);
			out = valueStructurePool.addValueStructureComplex(containerEle.getValueStructureComplex(), containerComplexId);
		}
		else {
			out = valueStructurePool.addValueStructureComplex(withValueStructure.getValueContext(), parentComplexId);
		}
		return out;
	}
	
	public static HAPValueStructure expandReference(
			HAPValueStructureInComplex valueStructureInComponent,
			HAPContextProcessor processContext
			) {
		HAPValueStructure out = valueStructureInComponent;
		String type = valueStructureInComponent.getStructureType();
		if(type.equals(HAPConstantShared.STRUCTURE_TYPE_VALUEFLAT)) {
			HAPValueStructureInComplexFlat flatIn = (HAPValueStructureInComplexFlat)valueStructureInComponent;
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
			HAPValueStructureInComplexGroup groupIn = (HAPValueStructureInComplexGroup)valueStructureInComponent;
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
			HAPResourceDefinition1 relatedResource = null;
			HAPManualEntityComplex contextComplexEntity = null;
			if(processContext.getComplexEntity() instanceof HAPResourceDefinition1) relatedResource = (HAPResourceDefinition1)processContext.getComplexEntity();
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

package com.nosliw.data.core.interactive;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPUtilityData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteriaAny;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteriaId;
import com.nosliw.data.core.data.variable.HAPVariableInfo;
import com.nosliw.data.core.dataassociation.HAPDefinitionGroupDataAssociationForTask;
import com.nosliw.data.core.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.dataassociation.mapping.HAPValueMapping;
import com.nosliw.data.core.structure.HAPElementStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafConstant;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelative;
import com.nosliw.data.core.structure.reference.HAPInfoPathReference;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.reference.HAPUtilityStructureElementReference;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;

public class HAPUtilityInteractive {

	public static HAPWithInteractive solidateRelative(HAPWithInteractive withInteractive, HAPContainerStructure parentStructures, String mode, Boolean relativeInheritRule, Set<String> elementTypes) {
		HAPWithInteractive out = withInteractive.cloneWithInteractive();
		for(HAPVariableInfo parm : out.getRequestParms()) {
			if(parm.getCriteria()==null || HAPDataTypeCriteriaAny.getCriteria().equals(parm.getCriteria())) {
				//no data criteria, then try to get it from reference
				HAPDataTypeCriteria dataTypeCriteria = getDataTypeCriteriaFromReference(parm.getReferenceInfo(), parentStructures, mode, relativeInheritRule, elementTypes);
				parm.getDataInfo().setCriteria(dataTypeCriteria);
			}
		}
		
		Map<String, HAPResultInteractive> results = out.getResults();
		for(String resultName : results.keySet()) {
			HAPResultInteractive result = results.get(resultName);
			for(HAPOutputInteractive output : result.getOutput()) {
				if(output.getCriteria()==null || HAPDataTypeCriteriaAny.getCriteria().equals(output.getCriteria())) {
					//no data criteria, then try to get it from reference
					HAPDataTypeCriteria dataTypeCriteria = getDataTypeCriteriaFromReference(output.getReferenceInfo(), parentStructures, mode, relativeInheritRule, elementTypes);
					output.setCriteria(dataTypeCriteria);
				}
			}
		}
		
		return out;
	}

	public static HAPDefinitionGroupDataAssociationForTask buildDataAssociation(HAPWithInteractive withInteractive) {
		HAPDefinitionGroupDataAssociationForTask out = new HAPDefinitionGroupDataAssociationForTask();
		HAPDefinitionDataAssociationMapping inputMapping = new HAPDefinitionDataAssociationMapping();
		for(HAPVariableInfo parm : withInteractive.getRequestParms()) {
			HAPInfoPathReference reference = parm.getReferenceInfo();
			HAPValueMapping mapping = inputMapping.getMapping(reference.getParent(), true);
			mapping.addMapping(reference.getReferencePath(), new HAPElementStructureLeafRelative(parm.getName()));
		}
		out.setInDataAssociation(inputMapping);
		
		Map<String, HAPResultInteractive> results = withInteractive.getResults();
		for(String resultName : results.keySet()) {
			HAPDefinitionDataAssociationMapping outputMapping = new HAPDefinitionDataAssociationMapping();
			for(HAPOutputInteractive output : results.get(resultName).getOutput()) {
				HAPInfoPathReference reference = output.getReferenceInfo();
				HAPValueMapping mapping = outputMapping.getMapping(null, true);
				mapping.addMapping(output.getName(), new HAPElementStructureLeafRelative(reference.getReferencePath()));
			}
			out.addOutDataAssociation(resultName, outputMapping);
		}
		return out;
	}
	
	public static HAPValueStructureDefinitionFlat buildInValueStructureFromWithInteractive(HAPWithInteractive withInteractive) {
		HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
		for(HAPVariableInfo varInfo : withInteractive.getRequestParms()) {
			out.addRoot(varInfo.getName(), new HAPElementStructureLeafData(varInfo.getCriteria()));
		}
		return out;
	}

	public static Map<String, HAPValueStructureDefinitionFlat> buildOutValueStructureFromWithInteractive(HAPWithInteractive withInteractive) {
		Map<String, HAPValueStructureDefinitionFlat> out = new LinkedHashMap<String, HAPValueStructureDefinitionFlat>();
		Map<String, HAPResultInteractive> results = withInteractive.getResults();
		for(String resultName : results.keySet()) {
			HAPValueStructureDefinitionFlat resultStructure = new HAPValueStructureDefinitionFlat();
			for(HAPOutputInteractive output : results.get(resultName).getOutput()) {
				resultStructure.addRoot(output.getName(), new HAPElementStructureLeafData(output.getCriteria()));
			}
			out.put(resultName, resultStructure);
		}
		return out;
	}
	
	private static HAPDataTypeCriteria getDataTypeCriteriaFromReference(HAPInfoPathReference referenceInfo, HAPContainerStructure parentStructures, String mode, Boolean relativeInheritRule, Set<String> elementTypes) {
		HAPDataTypeCriteria dataTypeCriteria = null;
		HAPInfoReferenceResolve resolveInfo = HAPUtilityStructureElementReference.resolveElementReference(referenceInfo, parentStructures, mode, relativeInheritRule, elementTypes);
		HAPElementStructure resolvedEle = resolveInfo.resolvedElement;
		String resolveEleType = resolvedEle.getType();
		if(resolveEleType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
			HAPElementStructureLeafData dataEle = (HAPElementStructureLeafData)resolvedEle;
			dataTypeCriteria = dataEle.getCriteria();
		}
		else if(resolveEleType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
			HAPElementStructureLeafConstant constantEle = (HAPElementStructureLeafConstant)resolvedEle;
			HAPData data = HAPUtilityData.buildDataWrapperFromObject(constantEle.getValue());
			if(data!=null)  dataTypeCriteria = new HAPDataTypeCriteriaId(data.getDataTypeId(), null);
		}
		return dataTypeCriteria;
	}
}

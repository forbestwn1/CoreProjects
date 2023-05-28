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
import com.nosliw.data.core.structure.HAPElementStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafConstant;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelative;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.reference.HAPReferenceElementInValueContext;
import com.nosliw.data.core.structure.reference.HAPUtilityStructureElementReference;
import com.nosliw.data.core.valuestructure1.HAPContainerStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionFlat;

public class HAPUtilityInteractive {

	public static void solidateRelative(HAPDefinitionInteractive withInteractive, HAPContainerStructure parentStructures, String mode, Boolean relativeInheritRule, Set<String> elementTypes) {
		for(HAPVariableInfo parm : withInteractive.getRequestParms()) {
			if(parm.getCriteria()==null || HAPDataTypeCriteriaAny.getCriteria().equals(parm.getCriteria())) {
				//no data criteria, then try to get it from reference
				HAPDataTypeCriteria dataTypeCriteria = getDataTypeCriteriaFromReference(parm.getReferenceInfo(), parentStructures, mode, relativeInheritRule, elementTypes);
				parm.getDataInfo().setCriteria(dataTypeCriteria);
			}
		}
		
		Map<String, HAPDefinitionInteractiveResult> results = withInteractive.getResults();
		for(String resultName : results.keySet()) {
			HAPDefinitionInteractiveResult result = results.get(resultName);
			solidateInteractiveResult(result, parentStructures, mode, relativeInheritRule, elementTypes);
		}
	}
	
	public static void solidateInteractiveResult(HAPDefinitionInteractiveResult result, HAPContainerStructure parentStructures, String mode, Boolean relativeInheritRule, Set<String> elementTypes) {
		for(HAPDefinitionInteractiveResultOutput output : result.getOutput()) {
			if(output.getCriteria()==null || HAPDataTypeCriteriaAny.getCriteria().equals(output.getCriteria())) {
				//no data criteria
				if(output.getReferenceInfo()!=null) {
					//try to get it from reference
					HAPDataTypeCriteria dataTypeCriteria = getDataTypeCriteriaFromReference(output.getReferenceInfo(), parentStructures, mode, relativeInheritRule, elementTypes);
					output.setCriteria(dataTypeCriteria);
				}
				else if(output.getConstantData()!=null) {
					output.setCriteria(new HAPDataTypeCriteriaId(output.getConstantData().getDataTypeId(), null));
				}
			}
		}
	}

	public static HAPDefinitionGroupDataAssociationForTask buildDataAssociation(HAPDefinitionInteractive withInteractive) {
		HAPDefinitionGroupDataAssociationForTask out = new HAPDefinitionGroupDataAssociationForTask();
		HAPDefinitionDataAssociationMapping inputMapping = new HAPDefinitionDataAssociationMapping();
		for(HAPVariableInfo parm : withInteractive.getRequestParms()) {
			HAPReferenceElementInValueContext reference = parm.getReferenceInfo();
			HAPDefinitionValueMapping mapping = inputMapping.getMapping(reference.getParentValueContextName(), true);
			mapping.addMapping(reference.getPath(), new HAPElementStructureLeafRelative(parm.getName()));
		}
		out.setInDataAssociation(inputMapping);
		
		Map<String, HAPDefinitionInteractiveResult> results = withInteractive.getResults();
		for(String resultName : results.keySet()) {
			HAPDefinitionDataAssociationMapping outputMapping = buildDataAssociationForResult(results.get(resultName));
			out.addOutDataAssociation(resultName, outputMapping);
		}
		return out;
	}
	
	public static HAPDefinitionDataAssociationMapping buildDataAssociationForResult(HAPDefinitionInteractiveResult result) {
		HAPDefinitionDataAssociationMapping out = new HAPDefinitionDataAssociationMapping();
		for(HAPDefinitionInteractiveResultOutput output : result.getOutput()) {
			HAPDefinitionValueMapping mapping = out.getMapping(null, true);
			HAPReferenceElementInValueContext reference = output.getReferenceInfo();
			HAPData constantData = output.getConstantData();
			if(reference!=null)		mapping.addMapping(output.getName(), new HAPElementStructureLeafRelative(reference.getPath()));
			else if(constantData!=null)    mapping.addMapping(output.getName(), new HAPElementStructureLeafConstant(constantData));
		}
		return out;
	}
	
	
	public static HAPValueStructureDefinitionFlat buildInValueStructureFromWithInteractive(HAPDefinitionInteractive withInteractive) {
		HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
		for(HAPVariableInfo varInfo : withInteractive.getRequestParms()) {
			out.addRoot(varInfo.getName(), new HAPElementStructureLeafData(varInfo.getCriteria()));
		}
		return out;
	}

	public static Map<String, HAPValueStructureDefinitionFlat> buildOutValueStructureFromWithInteractive(HAPDefinitionInteractive withInteractive) {
		Map<String, HAPValueStructureDefinitionFlat> out = new LinkedHashMap<String, HAPValueStructureDefinitionFlat>();
		Map<String, HAPDefinitionInteractiveResult> results = withInteractive.getResults();
		for(String resultName : results.keySet()) {
			HAPValueStructureDefinitionFlat resultStructure = new HAPValueStructureDefinitionFlat();
			for(HAPDefinitionInteractiveResultOutput output : results.get(resultName).getOutput()) {
				resultStructure.addRoot(output.getName(), new HAPElementStructureLeafData(output.getCriteria()));
			}
			out.put(resultName, resultStructure);
		}
		return out;
	}
	
	public static HAPValueStructureDefinitionFlat buildValueStructureForResule(HAPDefinitionInteractiveResult result) {
		HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
		for(HAPDefinitionInteractiveResultOutput output : result.getOutput()) {
			out.addRoot(output.getName(), new HAPElementStructureLeafData(output.getCriteria()));
		}
		return out;
	}
	
	private static HAPDataTypeCriteria getDataTypeCriteriaFromReference(HAPReferenceElementInValueContext referenceInfo, HAPContainerStructure parentStructures, String mode, Boolean relativeInheritRule, Set<String> elementTypes) {
		HAPDataTypeCriteria dataTypeCriteria = null;
		HAPInfoReferenceResolve resolveInfo = HAPUtilityStructureElementReference.resolveElementReference(referenceInfo, parentStructures, mode, relativeInheritRule, elementTypes);
		HAPElementStructure resolvedEle = resolveInfo.finalElement;
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

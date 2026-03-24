package com.nosliw.core.application.common.structure;

import java.util.List;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure.reference.HAPPathElementMapping;
import com.nosliw.core.application.common.structure.reference.HAPPathElementMappingConstantToVariable;
import com.nosliw.core.application.common.structure.reference.HAPPathElementMappingVariableToVariable;
import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.data.criteria.HAPDataTypeCriteriaId;
import com.nosliw.core.data.criteria.HAPInfoCriteria;
import com.nosliw.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.core.data.matcher.HAPMatchers;

public class HAPUtilityElement {

	public static HAPElementStructure getDescendant(HAPElementStructure element, String path) {
		HAPElementStructure out = element;
		HAPPath pathObj = new HAPPath(path);
		for(String pathSeg : pathObj.getPathSegments()) {
			if(out!=null) {
				out = out.getChild(pathSeg);
			} else {
				throw new RuntimeException();
			}
		}
		return out;
	}

	//get rid of relative, replace with solid definition
	public static HAPElementStructure solidateStructureElement(HAPElementStructure raw) {
		String type = raw.getType();
		if(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_DEFINITION.equals(type)) {
			HAPElementStructureLeafRelativeForDefinition forDefinition = (HAPElementStructureLeafRelativeForDefinition)raw;
			return forDefinition.getResolveInfo().getSolidElement().cloneStructureElement();
		}
		else if(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_VALUE.equals(type)) {
			HAPElementStructureLeafRelativeForValue forValue = (HAPElementStructureLeafRelativeForValue)raw;
			return forValue.getDefinition().cloneStructureElement();
		}
		else {
			return raw.cloneStructureElement();
		}
	}
	
	//merge origin context def with child context def to expect context out
	//also generate matchers from origin to expect
	public static void mergeElement(HAPElementStructure fromDef1, HAPElementStructure toDef1, boolean modifyStructure, List<HAPPathElementMapping> mappingPaths, String path, HAPDataTypeHelper dataTypeHelper){
		if(path==null) {
			path = "";
		}
		//merge is about solid
		HAPElementStructure fromDef = fromDef1.getSolidStructureElement();
		HAPElementStructure toDef = toDef1.getSolidStructureElement();
		String toType = toDef.getType();
		
		if(fromDef.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
			switch(toType) {
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
			{
				HAPElementStructureLeafConstant dataFrom = (HAPElementStructureLeafConstant)fromDef.getSolidStructureElement();
				HAPElementStructureLeafData dataTo = (HAPElementStructureLeafData)toDef;
				//cal matchers
				HAPMatchers matcher = HAPUtilityCriteria.mergeVariableInfo(HAPInfoCriteria.buildCriteriaInfo(new HAPDataTypeCriteriaId(dataFrom.getDataValue().getDataTypeId(), null)), dataTo.getCriteria(), dataTypeHelper); 
				mappingPaths.add(new HAPPathElementMappingConstantToVariable(dataFrom.getValue(), path, matcher));
				break;
			}
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_VALUE:
			{
				HAPElementStructureLeafConstant dataFrom = (HAPElementStructureLeafConstant)fromDef.getSolidStructureElement();
				mappingPaths.add(new HAPPathElementMappingConstantToVariable(dataFrom.getValue(), path, null));
				break;
			}
			default:
			{
				HAPErrorUtility.invalid("");
			}
			}
		}
		else if(toDef.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {  //kkkkk
			HAPErrorUtility.invalid("");
//			switch(fromDef.getType()) {
//			case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
//			{
//				HAPElementStructureLeafData dataFrom = (HAPElementStructureLeafData)fromDef;
//				 HAPElementStructureLeafConstant dataTo = (HAPElementStructureLeafConstant)toDef.getSolidStructureElement();
//				//cal matchers
//				HAPMatchers matcher = HAPUtilityCriteria.mergeVariableInfo(HAPInfoCriteria.buildCriteriaInfo(dataFrom.getCriteria()), new HAPDataTypeCriteriaId(dataTo.getDataValue().getDataTypeId(), null), runtimeEnv.getDataTypeHelper()); 
//				mappingPaths.put(path, matcher);
//				break;
//			}
//			}
		}
		else {
			if(!fromDef.getType().equals(toType))
			 {
				HAPErrorUtility.invalid("");   //not same type, error
			}
			switch(toType) {
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
			{
				HAPElementStructureLeafData dataFrom = (HAPElementStructureLeafData)fromDef.getSolidStructureElement();
				HAPElementStructureLeafData dataTo = (HAPElementStructureLeafData)toDef;
				//cal matchers
				HAPMatchers matcher = HAPUtilityCriteria.mergeVariableInfo(HAPInfoCriteria.buildCriteriaInfo(dataFrom.getCriteria()), dataTo.getCriteria(), dataTypeHelper); 
				mappingPaths.add(new HAPPathElementMappingVariableToVariable(path, matcher==null?new HAPMatchers():matcher));
				break;
			}
			default : 
			{
				mappingPaths.add(new HAPPathElementMappingVariableToVariable(path, new HAPMatchers()));
			}
			}
		}
	}
}

package com.nosliw.core.application.common.structure22;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.common.structure222.reference.HAPPathElementMapping;
import com.nosliw.core.application.common.structure222.reference.HAPPathElementMappingConstantToVariable;
import com.nosliw.core.application.common.structure222.reference.HAPPathElementMappingVariableToVariable;
import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.data.criteria.HAPDataTypeCriteriaId;
import com.nosliw.core.data.criteria.HAPInfoCriteria;
import com.nosliw.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.core.data.matcher.HAPMatchers;

public class HAPUtilityElement {

	public static HAPElementStructure traverseElement(HAPElementStructure element, String startPath, HAPProcessorStructureElement processor, Object value) {
		return traverseElement(new HAPInfoElement(element, new HAPComplexPath(startPath)), processor, value);
	}
	
	//traverse through all the context definition element, and process it
	//if return not null, then means new context element
	private static HAPElementStructure traverseElement(HAPInfoElement elementInfo, HAPProcessorStructureElement processor, Object value) {
		HAPElementStructure out = null;
		HAPElementStructure element = elementInfo.getElement(); 
		HAPComplexPath path = elementInfo.getElementPath();
		Pair<Boolean, HAPElementStructure> processOut = processor.process(elementInfo, value);
		boolean going = true;
		if(processOut!=null) {
			if(processOut.getLeft()!=null) {
				going = processOut.getLeft();
			}
			if(processOut.getRight()!=null) {
				element = processOut.getRight();
				elementInfo.setElement(element);
				out = element;
			}
		}
		if(going) {
			if(HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE.equals(element.getType())) {
				HAPElementStructureNode nodeEle = (HAPElementStructureNode)element;
				for(String childNodeName : nodeEle.getChildren().keySet()) {
					HAPElementStructure childProcessed = traverseElement(new HAPInfoElement(nodeEle.getChild(childNodeName), path.appendSegment(childNodeName)), processor, value);
					if(childProcessed!=null) {
						//replace with new element
						nodeEle.addChild(childNodeName, childProcessed);
					}
				}
			}
		}
		processor.postProcess(elementInfo, value);
		return out;
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
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
			{
				HAPElementStructureNode nodeFrom = (HAPElementStructureNode)fromDef;
				HAPElementStructureNode nodeTo = (HAPElementStructureNode)toDef;
				for(String nodeName : nodeTo.getChildren().keySet()) {
					HAPElementStructure childNodeTo = nodeTo.getChildren().get(nodeName);
					HAPElementStructure childNodeFrom = nodeFrom.getChildren().get(nodeName);
					String childPath = HAPUtilityNamingConversion.cascadePath(path, nodeName);
					if(childNodeFrom!=null || modifyStructure) {
						switch(childNodeTo.getType()) {
						case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
						{
							if(childNodeFrom==null) {
								childNodeFrom = new HAPElementStructureLeafData();
								nodeFrom.addChild(nodeName, childNodeFrom);
							}
							mergeElement(childNodeFrom, childNodeTo, modifyStructure, mappingPaths, childPath, dataTypeHelper);
							break;
						}
						case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
						{
							if(childNodeFrom==null) {
								childNodeFrom = new HAPElementStructureNode();
								nodeFrom.addChild(nodeName, childNodeFrom);
							}
							mergeElement(childNodeFrom, childNodeTo, modifyStructure, mappingPaths, childPath, dataTypeHelper);
							break;
						}
						default :
						{
							if(childNodeFrom==null) {
								childNodeFrom = childNodeTo.cloneStructureElement();
								nodeFrom.addChild(nodeName, childNodeFrom);
							}
							break;
						}
					}
				}
				break;
				}
			}
			default : 
			{
				mappingPaths.add(new HAPPathElementMappingVariableToVariable(path, new HAPMatchers()));
			}
			}
		}
	}

}

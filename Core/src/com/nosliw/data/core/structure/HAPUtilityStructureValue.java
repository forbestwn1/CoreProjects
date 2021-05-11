package com.nosliw.data.core.structure;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.valuestructure.HAPStructureValue;

public class HAPUtilityStructureValue {

	//find all data variables in context 
	public static Map<String, HAPInfoCriteria> discoverDataVariablesInStructure(HAPStructureValue structure){
		Map<String, HAPInfoCriteria> out = new LinkedHashMap<String, HAPInfoCriteria>();
		for(HAPRoot root : structure.getAllRoots()){
			if(!root.isConstant()){
				discoverDataVariableInElement(root.getLocalId(), root.getDefinition(), out);
			}
		}
		return out;
	}

	//discover data type criteria defined in context node
	//the purpose is to find variables related with data type criteria
	//the data type criteria name is full name in path, for instance, a.b.c.d
	private static void discoverDataVariableInElement(String path, HAPElement contextDefEle, Map<String, HAPInfoCriteria> criterias){
		switch(contextDefEle.getType()) {
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE:
			HAPElementLeafRelative relativeEle = (HAPElementLeafRelative)contextDefEle;
			if(relativeEle.getDefinition()!=null)		discoverDataVariableInElement(path, relativeEle.getSolidStructureElement(), criterias);
			break;
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
			HAPElementLeafData dataEle = (HAPElementLeafData)contextDefEle;
			HAPInfoCriteria varInfo = HAPInfoCriteria.buildCriteriaInfo(dataEle.getCriteria());
//			varInfo.setId(path);
			criterias.put(path, varInfo);
			break;
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
			HAPElementNode nodeEle = (HAPElementNode)contextDefEle;
			for(String childName : nodeEle.getChildren().keySet()) {
				String childPath = HAPNamingConversionUtility.cascadeComponentPath(path, childName);
				discoverDataVariableInElement(childPath, nodeEle.getChildren().get(childName), criterias);
			}
			break;
		}
	}


}

package com.nosliw.data.core.domain.valuecontext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.structure.HAPElementStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelativeForDefinition;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelativeForValue;
import com.nosliw.data.core.structure.HAPElementStructureNode;

public class HAPUtilityValueStructure {

	//find all data variables in context 
	public static Map<String, HAPInfoCriteria> discoverDataVariablesByIdInStructure(HAPDefinitionEntityValueStructure structure){
		Map<String, HAPInfoCriteria> out = new LinkedHashMap<String, HAPInfoCriteria>();
		if(structure!=null) {
			for(HAPRootStructure root : structure.getAllRoots()){
				if(!root.isConstant()){
					discoverDataVariableInElement(root.getName(), root.getDefinition(), out);
				}
			}
		}
		return out;
	}

	//discover data type criteria defined in context node
	//the purpose is to find variables related with data type criteria
	//the data type criteria name is full name in path, for instance, a.b.c.d
	private static void discoverDataVariableInElement(String path, HAPElementStructure contextDefEle, Map<String, HAPInfoCriteria> criterias){
		switch(contextDefEle.getType()) {
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_VALUE:
		{
			HAPElementStructureLeafRelativeForValue relativeEle = (HAPElementStructureLeafRelativeForValue)contextDefEle;
			if(relativeEle.getDefinition()!=null)		discoverDataVariableInElement(path, relativeEle.getSolidStructureElement(), criterias);
			else  discoverDataVariableInElement(path, relativeEle.getDefinition(), criterias);
			break;
		}
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_DEFINITION:
		{
			HAPElementStructureLeafRelativeForDefinition relativeEle = (HAPElementStructureLeafRelativeForDefinition)contextDefEle;
			discoverDataVariableInElement(path, relativeEle.getSolidStructureElement(), criterias);
			break;
		}
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
			HAPElementStructureLeafData dataEle = (HAPElementStructureLeafData)contextDefEle;
			HAPInfoCriteria varInfo = HAPInfoCriteria.buildCriteriaInfo(dataEle.getCriteria());
//			varInfo.setId(path);
			criterias.put(path, varInfo);
			break;
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
			HAPElementStructureNode nodeEle = (HAPElementStructureNode)contextDefEle;
			for(String childName : nodeEle.getChildren().keySet()) {
				String childPath = HAPUtilityNamingConversion.cascadeComponentPath(path, childName);
				discoverDataVariableInElement(childPath, nodeEle.getChildren().get(childName), criterias);
			}
			break;
		}
	}

	public static String[] getAllCategaries(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE,
		};
		return contextTypes;
	}

	public static List<String> getAllCategariesWithResolvePriority(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
		};
		return new ArrayList<>(Arrays.asList(contextTypes));
	}

	public static String[] getAllCategariesWithPriority(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
		};
		return contextTypes;
	}

	//context type that can be inherited by child
	public static String[] getInheritableCategaries(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
		};
		return contextTypes;
	}

	//visible to child
	public static String[] getVisibleCategaries(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
		};
		return contextTypes;
	}

}
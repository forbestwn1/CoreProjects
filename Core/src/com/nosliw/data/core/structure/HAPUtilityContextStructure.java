package com.nosliw.data.core.structure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinition;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinitionFlat;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinitionGroup;

public class HAPUtilityContextStructure {

	public static HAPContextStructureValueDefinition hardMergeContextStructure(HAPContextStructureValueDefinition from, HAPContextStructureValueDefinition to) {
		if(to==null)  return from;
		
		HAPContextStructureValueDefinition fromModify = toSolidContextStructure(from, to.isFlat());
		if(to.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT)) {
			((HAPContextStructureValueDefinitionFlat)to.cloneContextStructure()).hardMergeWith((HAPContextStructureValueDefinitionFlat)fromModify);
			return to;
		}
		else if(to.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
			((HAPContextStructureValueDefinitionGroup)to.cloneContextStructure()).hardMergeWith((HAPContextStructureValueDefinitionGroup)fromModify);
			return to;
		}
		else {
			return fromModify;
		}
	}
	
	public static HAPContextStructureValueDefinition toSolidContextStructure(HAPContextStructureValueDefinition context, boolean isFlat) {
		if(context==null || context.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_EMPTY)) {
			if(isFlat)  return new HAPContextStructureValueDefinitionFlat();
			else return new HAPContextStructureValueDefinitionGroup();
		}
		
		if(context.isFlat()==isFlat)   return context;
		else {
			if(context.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT)) {
				HAPContextStructureValueDefinitionGroup out = new HAPContextStructureValueDefinitionGroup();
				out.setContext(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, (HAPContextStructureValueDefinitionFlat)context);
				return out;
			}
			else if(context.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
				HAPContextStructureValueDefinitionFlat out = buildContextFromContextGroup((HAPContextStructureValueDefinitionGroup)context);
				return out;
			}
		}
		return null;
	}
	
	public static HAPContextStructureValueDefinitionFlat buildContextFromContextGroup(HAPContextStructureValueDefinitionGroup context) {
		HAPContextStructureValueDefinitionFlat out = new HAPContextStructureValueDefinitionFlat();
		
		List<String> categarys = Arrays.asList(HAPContextStructureValueDefinitionGroup.getContextTypesWithPriority());
		Collections.reverse(categarys);
		for(String categary : categarys) {
			Map<String, HAPRoot> eles = context.getElements(categary);
			for(String name : eles.keySet()) {
				out.addElement(name, eles.get(name).cloneContextDefinitionRoot());
			}
		}
		return out;
	}

}

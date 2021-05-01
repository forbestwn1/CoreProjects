package com.nosliw.data.core.structure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinition;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionFlat;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionGroup;

public class HAPUtilityContextStructure {

	public static HAPStructureValueDefinition hardMergeContextStructure(HAPStructureValueDefinition from, HAPStructureValueDefinition to) {
		if(to==null)  return from;
		
		HAPStructureValueDefinition fromModify = toSolidContextStructure(from, to.isFlat());
		if(to.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT)) {
			((HAPStructureValueDefinitionFlat)to.cloneStructure()).hardMergeWith((HAPStructureValueDefinitionFlat)fromModify);
			return to;
		}
		else if(to.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
			((HAPStructureValueDefinitionGroup)to.cloneStructure()).hardMergeWith((HAPStructureValueDefinitionGroup)fromModify);
			return to;
		}
		else {
			return fromModify;
		}
	}
	
	public static HAPStructureValueDefinition toSolidContextStructure(HAPStructureValueDefinition context, boolean isFlat) {
		if(context==null || context.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_EMPTY)) {
			if(isFlat)  return new HAPStructureValueDefinitionFlat();
			else return new HAPStructureValueDefinitionGroup();
		}
		
		if(context.isFlat()==isFlat)   return context;
		else {
			if(context.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT)) {
				HAPStructureValueDefinitionGroup out = new HAPStructureValueDefinitionGroup();
				out.setFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, (HAPStructureValueDefinitionFlat)context);
				return out;
			}
			else if(context.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
				HAPStructureValueDefinitionFlat out = buildContextFromContextGroup((HAPStructureValueDefinitionGroup)context);
				return out;
			}
		}
		return null;
	}
	
	public static HAPStructureValueDefinitionFlat buildContextFromContextGroup(HAPStructureValueDefinitionGroup context) {
		HAPStructureValueDefinitionFlat out = new HAPStructureValueDefinitionFlat();
		
		List<String> categarys = Arrays.asList(HAPStructureValueDefinitionGroup.getAllCategariesWithPriority());
		Collections.reverse(categarys);
		for(String categary : categarys) {
			Map<String, HAPRoot> eles = context.getRootsByCategary(categary);
			for(String name : eles.keySet()) {
				out.addRoot(name, eles.get(name).cloneRoot());
			}
		}
		return out;
	}

}

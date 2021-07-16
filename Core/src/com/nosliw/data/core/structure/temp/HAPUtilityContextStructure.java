package com.nosliw.data.core.structure.temp;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.structure.HAPRootStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPUtilityContextStructure {

	public static HAPValueStructure hardMergeContextStructure(HAPValueStructure from, HAPValueStructure to) {
		if(to==null)  return from;
		
		HAPValueStructure fromModify = toSolidContextStructure(from, to.isFlat());
		if(to.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT)) {
			((HAPValueStructureDefinitionFlat)to.cloneStructure()).hardMergeWith((HAPValueStructureDefinitionFlat)fromModify);
			return to;
		}
		else if(to.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
			((HAPValueStructureDefinitionGroup)to.cloneStructure()).hardMergeWith((HAPValueStructureDefinitionGroup)fromModify);
			return to;
		}
		else {
			return fromModify;
		}
	}
	
	public static HAPValueStructure toSolidContextStructure(HAPValueStructure context, boolean isFlat) {
		if(context==null || context.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_EMPTY)) {
			if(isFlat)  return new HAPValueStructureDefinitionFlat();
			else return new HAPValueStructureDefinitionGroup();
		}
		
		if(context.isFlat()==isFlat)   return context;
		else {
			if(context.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT)) {
				HAPValueStructureDefinitionGroup out = new HAPValueStructureDefinitionGroup();
				out.setFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, (HAPValueStructureDefinitionFlat)context);
				return out;
			}
			else if(context.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
				HAPValueStructureDefinitionFlat out = buildContextFromContextGroup((HAPValueStructureDefinitionGroup)context);
				return out;
			}
		}
		return null;
	}
	
	public static HAPValueStructureDefinitionFlat buildContextFromContextGroup(HAPValueStructureDefinitionGroup context) {
		HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
		
		List<String> categarys = Arrays.asList(HAPValueStructureDefinitionGroup.getAllCategariesWithPriority());
		Collections.reverse(categarys);
		for(String categary : categarys) {
			Map<String, HAPRootStructure> eles = context.getRootsByCategary(categary);
			for(String name : eles.keySet()) {
				out.addRootToCategary(name, eles.get(name).cloneRoot());
			}
		}
		return out;
	}

}

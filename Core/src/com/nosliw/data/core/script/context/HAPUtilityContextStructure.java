package com.nosliw.data.core.script.context;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;

public class HAPUtilityContextStructure {

	public static HAPContextStructure hardMergeContextStructure(HAPContextStructure from, HAPContextStructure to) {
		if(to==null)  return from;
		
		HAPContextStructure fromModify = toSolidContextStructure(from, to.isFlat());
		if(to.getType().equals(HAPConstant.CONTEXTSTRUCTURE_TYPE_FLAT)) {
			((HAPContext)to.cloneContextStructure()).hardMergeWith((HAPContext)fromModify);
			return to;
		}
		else if(to.getType().equals(HAPConstant.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
			((HAPContextGroup)to.cloneContextStructure()).hardMergeWith((HAPContextGroup)fromModify);
			return to;
		}
		else {
			return fromModify;
		}
	}
	
	public static HAPContextStructure toSolidContextStructure(HAPContextStructure context, boolean isFlat) {
		if(context==null || context.getType().equals(HAPConstant.CONTEXTSTRUCTURE_TYPE_EMPTY)) {
			if(isFlat)  return new HAPContext();
			else return new HAPContextGroup();
		}
		
		if(context.isFlat()==isFlat)   return context;
		else {
			if(context.getType().equals(HAPConstant.CONTEXTSTRUCTURE_TYPE_FLAT)) {
				HAPContextGroup out = new HAPContextGroup();
				out.setContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC, (HAPContext)context);
				return out;
			}
			else if(context.getType().equals(HAPConstant.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
				HAPContext out = buildContextFromContextGroup((HAPContextGroup)context);
				return out;
			}
		}
		return null;
	}
	
	public static HAPContext buildContextFromContextGroup(HAPContextGroup context) {
		HAPContext out = new HAPContext();
		
		List<String> categarys = Arrays.asList(HAPContextGroup.getContextTypesWithPriority());
		Collections.reverse(categarys);
		for(String categary : categarys) {
			Map<String, HAPContextDefinitionRoot> eles = context.getElements(categary);
			for(String name : eles.keySet()) {
				out.addElement(name, eles.get(name).cloneContextDefinitionRoot());
			}
		}
		return out;
	}

}

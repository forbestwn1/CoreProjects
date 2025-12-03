package com.nosliw.core.application.division.manual.common.valuecontext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure.HAPUtilityScope;
import com.nosliw.core.application.common.structure.HAPValueContextDefinition;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructureDefinition;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueContext;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueStructure;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickWrapperValueStructure;
import com.nosliw.core.application.division.manual.core.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.valueport.HAPUtilityValueStructure;

public class HAPManualUtilityValueContext {

	public static HAPManualInfoPartInValueContext createPartInfoDefault() {	return new HAPManualInfoPartInValueContext(HAPConstantShared.VALUESTRUCTUREPART_NAME_DEFAULT, HAPConstantShared.VALUESTRUCTUREPART_PRIORITY_DEFAULT);	}
	public static HAPManualInfoPartInValueContext createPartInfoExtension() {	return new HAPManualInfoPartInValueContext(HAPConstantShared.VALUESTRUCTUREPART_NAME_EXTENSION, HAPConstantShared.VALUESTRUCTUREPART_PRIORITY_EXTENSION);	}
	public static HAPManualInfoPartInValueContext createPartInfoFromParent() {	return new HAPManualInfoPartInValueContext(HAPConstantShared.VALUESTRUCTUREPART_NAME_FROMPARENT, HAPConstantShared.VALUESTRUCTUREPART_PRIORITY_FROMPARENT);	}
	
	
	public static void buildValueContextBrickFromValueContext(HAPManualDefinitionBrickValueContext valueContextBrick, HAPValueContextDefinition valueContext, HAPManualManagerBrick manualDivisionEntityMan) {
		for(HAPWrapperValueStructureDefinition uiTagDefValueStructure : valueContext.getValueStructures()) {
			HAPManualUtilityValueContext.addValueStuctureWrapperToValueContextBrick(uiTagDefValueStructure, valueContextBrick, manualDivisionEntityMan);
		}
	}
	
	public static void addValueStuctureWrapperToValueContextBrick(HAPWrapperValueStructureDefinition valueStructureWrapper, HAPManualDefinitionBrickValueContext valueContextBrick, HAPManualManagerBrick manualDivisionEntityMan) {
		HAPManualDefinitionBrickWrapperValueStructure manualWrapperBrickValueStrucutre = (HAPManualDefinitionBrickWrapperValueStructure)manualDivisionEntityMan.newBrickDefinition(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100);
		HAPManualDefinitionBrickValueStructure manualBrickValueStrucutre = (HAPManualDefinitionBrickValueStructure)manualDivisionEntityMan.newBrickDefinition(HAPManualEnumBrickType.VALUESTRUCTURE_100);
		manualBrickValueStrucutre.setValue(valueStructureWrapper.getValueStructure());
		manualWrapperBrickValueStrucutre.setValueStructure(manualBrickValueStrucutre);
		manualWrapperBrickValueStrucutre.getStructureInfo().setScope(valueStructureWrapper.getStructureInfo().getScope());
		manualWrapperBrickValueStrucutre.getStructureInfo().setInheritMode(valueStructureWrapper.getStructureInfo().getInheritMode());
		valueStructureWrapper.cloneToEntityInfo(manualWrapperBrickValueStrucutre);
		valueContextBrick.addValueStructure(manualWrapperBrickValueStrucutre);
	}

	public static List<HAPManualInfoValueStructureSorting> getAllValueStructuresSorted(HAPManualValueContext valueContext){
		List<HAPManualInfoValueStructureSorting> out = getAllValueStructures(valueContext);
		sortValueStructureInfos(out);
		return out;
	}
	
	public static List<HAPManualInfoValueStructureSorting> getAllValueStructures(HAPManualValueContext valueContext){
		List<HAPManualInfoValueStructureSorting> out = new ArrayList<HAPManualInfoValueStructureSorting>();
		for(HAPManualPartInValueContext part : valueContext.getParts()) {
			getAllChildrenValueStructure(null, part, out);
		}
		return out;
	}

	private static void getAllChildrenValueStructure(List<Integer> priorityBase, HAPManualPartInValueContext part, List<HAPManualInfoValueStructureSorting> out) {
		String partType = part.getPartType();
		if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE)) {
			HAPManualPartInValueContextSimple simplePart = (HAPManualPartInValueContextSimple)part;
			for(HAPManualWrapperStructure valueStructure : simplePart.getValueStructures()) {
				HAPManualInfoValueStructureSorting valueStructureInfo = new HAPManualInfoValueStructureSorting(valueStructure);
				valueStructureInfo.setPriority(caculatePriority(appendParentInfo(priorityBase, simplePart.getPartInfo().getPriority())));
				out.add(valueStructureInfo);
			}
		}
		else if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHENTITY)){
			HAPManualPartInValueContextGroupWithEntity groupPart = (HAPManualPartInValueContextGroupWithEntity)part;
			for(HAPManualPartInValueContext child : groupPart.getChildren()) {
				getAllChildrenValueStructure(appendParentInfo(priorityBase, child.getPartInfo().getPriority()), child, out);
			}
		}
	}

	private static List<Integer> appendParentInfo(List<Integer> basePriority, List<Integer> priority) {
		List<Integer> out = new ArrayList<Integer>();
		if(basePriority!=null) {
			out.addAll(basePriority);
		}
		if(priority!=null) {
			out.addAll(priority);
		}
		return out;
	}

	private static void sortValueStructureInfos(List<HAPManualInfoValueStructureSorting> valueStructures) {
		Collections.sort(valueStructures, new Comparator<HAPManualInfoValueStructureSorting>() {
			@Override
			public int compare(HAPManualInfoValueStructureSorting arg0, HAPManualInfoValueStructureSorting arg1) {

				//compare priority first
				int out = HAPUtilityValueStructure.sortPriority(arg0.getPriority(), arg1.getPriority());
				if(out!=0) {
					return out;
				} else {
					//if priority equal, then compare by value structure scope
					String valueStructureScope0 = arg0.getValueStructure().getStructureInfo().getScope();
					String valueStructureScope1 = arg1.getValueStructure().getStructureInfo().getScope();
					
					List<String> scopesByPriority = HAPUtilityScope.getAllScopesWithResolvePriority();
					int scopePriority0 = scopesByPriority.indexOf(valueStructureScope0);
					int scopePriority1 = scopesByPriority.indexOf(valueStructureScope1);
					return scopePriority0 - scopePriority1;
				}
			}
		});
	}

	public static void sortParts(List<HAPManualPartInValueContext> parts) {
		Collections.sort(parts, new Comparator<HAPManualPartInValueContext>() {

			@Override
			public int compare(HAPManualPartInValueContext arg0, HAPManualPartInValueContext arg1) {
				return sortPriority(arg0.getPartInfo().getPriority(), arg1.getPartInfo().getPriority());
			}
		});
	}

	private static double caculatePriority(List<Integer> in) {
		double priority = 0;
		double i0 = 1;
		for(double p : in) {
			i0 = i0 / 10;
			priority = priority + i0 * p; 
		}
		return priority;
	}

	private static int sortPriority(List<Integer> p1, List<Integer> p2) {
		return HAPUtilityValueStructure.sortPriority(caculatePriority(p1), caculatePriority(p2));
	}
	
}

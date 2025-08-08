package com.nosliw.core.application.division.manual.common.valuecontext22;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure22.HAPUtilityStructure;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualInfoValueStructureSorting;

public class HAPManualUtilityValueContext {

	public static HAPManualInfoPartInValueContext createPartInfoDefault() {	return new HAPManualInfoPartInValueContext(HAPConstantShared.VALUESTRUCTUREPART_NAME_DEFAULT, HAPConstantShared.VALUESTRUCTUREPART_PRIORITY_DEFAULT);	}
	public static HAPManualInfoPartInValueContext createPartInfoExtension() {	return new HAPManualInfoPartInValueContext(HAPConstantShared.VALUESTRUCTUREPART_NAME_EXTENSION, HAPConstantShared.VALUESTRUCTUREPART_PRIORITY_EXTENSION);	}
	public static HAPManualInfoPartInValueContext createPartInfoFromParent() {	return new HAPManualInfoPartInValueContext(HAPConstantShared.VALUESTRUCTUREPART_NAME_FROMPARENT, HAPConstantShared.VALUESTRUCTUREPART_PRIORITY_FROMPARENT);	}
	


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
				valueStructureInfo.setPriority(appendParentInfo(priorityBase, simplePart.getPartInfo().getPriority()));
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
				String groupType0 = arg0.getValueStructure().getStructureInfo().getGroupType();
				String groupType1 = arg1.getValueStructure().getStructureInfo().getGroupType();
				
				List<String> groups = HAPUtilityStructure.getAllCategariesWithResolvePriority();
				int groupPriority0 = groups.indexOf(groupType0);
				int groupPriority1 = groups.indexOf(groupType1);

				//compare priority first
				int out = sortPriority(arg0.getPriority(), arg1.getPriority());
				if(out!=0) {
					return out;
				} else {
					//if priority equal, then compare by group type
					return groupPriority1 - groupPriority0;
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


	private static int sortPriority(List<Integer> first, List<Integer> second) {
		double priority0 = 0;
		double i0 = 1;
		for(int p : first) {
			i0 = i0 / 10;
			priority0 = priority0 + i0 * p; 
		}
		
		double priority1 = 0;
		double i1 = 1;
		for(int p : second) {
			i1 = i1 / 10;
			priority1 = priority1 + i1 * p; 
		}
		
		double out = priority1-priority0;
		if(out>0) {
			return 1;
		} else if(out<0) {
			return -1;
		} else {
			return 0;
		}
	}
	
}

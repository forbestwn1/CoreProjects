package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.complex.HAPUtilityValueStructure;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.valuestructure.HAPExecutableValueStructure;
import com.nosliw.data.core.valuestructure.HAPInfoPartValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPUtilityComplexValueStructure {

	public static void processValueStructureInheritance(String valueStructureComplexId, String parentValueStructureComplexId, HAPConfigureComplexRelationValueStructure configure, HAPDomainValueStructure valueStructureDomain) {
		
	}
	
	public static List<HAPInfoPartSimple> findCandidateSimplePart(String partRef, HAPDefinitionEntityValueContext valueStructureComplex){
		List<HAPInfoPartSimple> out = getAllSimpleParts(valueStructureComplex);
		return out;
	}
	
	public static List<HAPInfoValueStructureSorting> getAllValueStructuresSorted(HAPExecutableEntityValueContext valueStructureComplex){
		List<HAPInfoValueStructureSorting> out = getAllValueStructures(valueStructureComplex);
		sortValueStructureInfos(out);
		return out;
	}
	
	public static List<HAPInfoValueStructureSorting> getAllValueStructures(HAPExecutableEntityValueContext valueStructureComplex){
		List<HAPInfoValueStructureSorting> out = new ArrayList<HAPInfoValueStructureSorting>();
		for(HAPExecutablePartValueContext part : valueStructureComplex.getParts()) {
			getAllChildren(null, part, out);
		}
		return out;
	}

	private static void sortValueStructureInfos(List<HAPInfoValueStructureSorting> valueStructures) {
		Collections.sort(valueStructures, new Comparator<HAPInfoValueStructureSorting>() {
			@Override
			public int compare(HAPInfoValueStructureSorting arg0, HAPInfoValueStructureSorting arg1) {
				String groupType0 = arg0.getValueStructure().getGroupType();
				String groupType1 = arg1.getValueStructure().getGroupType();
				
				List<String> groups = HAPUtilityValueStructure.getAllCategariesWithResolvePriority();
				int groupPriority0 = groups.indexOf(groupType0);
				int groupPriority1 = groups.indexOf(groupType1);

				//compare priority first
				int out = sortPriority(arg0.getPriority(), arg1.getPriority());
				if(out!=0)  return out;
				else {
					//if priority equal, then compare by group type
					return groupPriority1 - groupPriority0;
				}
			}
		});
	}

	private static void getAllChildren(List<Integer> priorityBase, HAPExecutablePartValueContext part, List<HAPInfoValueStructureSorting> out) {
		String partType = part.getPartType();
		if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE)) {
			HAPExecutablePartValueContextSimple simplePart = (HAPExecutablePartValueContextSimple)part;
			for(HAPWrapperExecutableValueStructure valueStructure : simplePart.getValueStructures()) {
				HAPInfoValueStructureSorting valueStructureInfo = new HAPInfoValueStructureSorting(valueStructure);
				valueStructureInfo.setPriority(appendParentInfo(priorityBase, simplePart.getPartInfo().getPriority()));
				out.add(valueStructureInfo);
			}
		}
		else if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHENTITY)){
			HAPExecutablePartValueContextGroupWithEntity groupPart = (HAPExecutablePartValueContextGroupWithEntity)part;
			for(HAPExecutablePartValueContext child : groupPart.getChildren()) {
				getAllChildren(appendParentInfo(priorityBase, child.getPartInfo().getPriority()), child, out);
			}
		}
	}

	
	public static List<HAPInfoPartSimple> getAllSimpleParts(HAPExecutableEntityValueContext valueStructureComplex){
		List<HAPInfoPartSimple> out = new ArrayList<HAPInfoPartSimple>();
		for(HAPExecutablePartValueContext part : valueStructureComplex.getParts()) {
			getAllChildren1(null, part, out);
		}
		sortSimplePartInfos(out);
		return out;
	}

	private static void getAllChildren1(List<Integer> priorityBase, HAPExecutablePartValueContext part, List<HAPInfoPartSimple> out) {
		String partType = part.getPartType();
		if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE)) {
			HAPExecutablePartValueContextSimple simplePart = (HAPExecutablePartValueContextSimple)part;
			HAPInfoPartSimple simpleInfo = new HAPInfoPartSimple(simplePart);
			simpleInfo.setPriority(appendParentInfo(priorityBase, simplePart.getPartInfo().getPriority()));
			out.add(simpleInfo);
		}
		else if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHENTITY)){
			HAPExecutablePartValueContextGroupWithEntity groupPart = (HAPExecutablePartValueContextGroupWithEntity)part;
			for(HAPExecutablePartValueContext child : groupPart.getChildren()) {
				getAllChildren(appendParentInfo(priorityBase, child.getPartInfo().getPriority()), child, out);
			}
		}
	}

	private static List<Integer> appendParentInfo(List<Integer> basePriority, List<Integer> priority) {
		List<Integer> out = new ArrayList<Integer>();
		if(basePriority!=null)  out.addAll(basePriority);
		if(priority!=null)   out.addAll(priority);
		return out;
	}

	public static HAPExecutableValueStructure buildExecuatableValueStructure(HAPDefinitionEntityValueContext valueStructureComplex) {
		HAPExecutableValueStructure out = new HAPExecutableValueStructure();
		List<HAPExecutablePartValueContextSimple> parts = getAllSimpleParts(valueStructureComplex);
		for(int i=parts.size()-1; i>=0; i--) {
			HAPExecutableValueStructure vsExe = HAPUtilityValueStructure.buildExecuatableValueStructure(parts.get(i).getValueStructureWrapper().getValueStructure());
			
		}
	}
	
	public static HAPInfoPartValueStructure cascadePartInfo(HAPInfoPartValueStructure info1, HAPInfoPartValueStructure info2) {
		HAPInfoPartValueStructure out;
		if(info1==null)   out = info2.cloneValueStructurePartInfo();
		else if(info2==null)  out = info1.cloneValueStructurePartInfo();
		else {
			out = info1.cloneValueStructurePartInfo().appendPath(info2.getReference()).appendPriority(info2.getPriority());
		}
		return out;
	}
	
	public static void sortParts(List<HAPExecutablePartValueContext> parts) {
		Collections.sort(parts, new Comparator<HAPExecutablePartValueContext>() {

			@Override
			public int compare(HAPExecutablePartValueContext arg0, HAPExecutablePartValueContext arg1) {
				return sortPriority(arg0.getPartInfo().getPriority(), arg1.getPartInfo().getPriority());
			}
		});
	}

	public static void sortSimplePartInfos(List<HAPInfoPartSimple> parts) {
		Collections.sort(parts, new Comparator<HAPInfoPartSimple>() {

			@Override
			public int compare(HAPInfoPartSimple arg0, HAPInfoPartSimple arg1) {
				return sortPriority(arg0.getPriority(), arg1.getPriority());
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
		if(out>0)   return 1;
		else if(out<0)   return -1;
		else return 0;
	}
	
	public static HAPInfoPartValueStructure createPartInfoDefault() {	return new HAPInfoPartValueStructure(HAPConstantShared.VALUESTRUCTUREPART_NAME_DEFAULT, HAPConstantShared.VALUESTRUCTUREPART_PRIORITY_DEFAULT);	}
	public static HAPInfoPartValueStructure createPartInfoFromParent() {	return new HAPInfoPartValueStructure(HAPConstantShared.VALUESTRUCTUREPART_NAME_FROMPARENT, HAPConstantShared.VALUESTRUCTUREPART_PRIORITY_FROMPARENT);	}
	
	public static void setValueStructureDefault(HAPDefinitionEntityValueContext valueStructureComplex, HAPValueStructure valueStructure) {
		valueStructureComplex.addPartSimple(valueStructure, HAPUtilityComplexValueStructure.createPartInfoDefault());
	}
	

	public static void setValueStructureFromParent(HAPWithValueContext withValueStructure, List<HAPExecutablePartValueContext> partsFromParent) {
		HAPDefinitionEntityValueContext valueStructureComplex = withValueStructure.getValueContext();
		valueStructureComplex.addPartGroup(partsFromParent, HAPUtilityComplexValueStructure.createPartInfoFromParent());
	}
	
	public static void setParentPart(HAPWithValueContext child, HAPWithValueContext parent) {	setValueStructureFromParent(child, parent.getValueContext().getValueStructures());	}

}

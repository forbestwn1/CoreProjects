package com.nosliw.core.application.valuecontext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.common.valueport.HAPIdValuePort;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickValueContext;
import com.nosliw.core.application.valuestructure.HAPRootStructure;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.data.variable.HAPIdRootElement;
import com.nosliw.data.core.data.variable.HAPIdVariable;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;

public class HAPUtilityValueContext {

	public static HAPIdValuePort createValuePortIdValueContext(HAPExecutableEntity complexEntity) {
		return new HAPIdValuePort(complexEntity.getPathFromRoot().toString(), HAPConstantShared.VALUEPORT_TYPE_VALUECONTEXT, HAPConstantShared.NAME_DEFAULT);
	}
	
	public static String getExtensionValueStructure(HAPValueContext valueContext, String groupType) {
		List<HAPInfoPartSimple> parts = getAllSimpleParts(valueContext);
		for(HAPInfoPartSimple part : parts) {
			if(HAPConstantShared.VALUESTRUCTUREPART_NAME_EXTENSION.equals(part.getSimpleValueStructurePart().getPartInfo().getName())) {
				for(HAPInfoValueStructure valueStructureWrapper : part.getSimpleValueStructurePart().getValueStructures()) {
					if(groupType.equals(valueStructureWrapper.getGroupType())) {
						return valueStructureWrapper.getValueStructureRuntimeId();
					}
				}
			}
		}
		return null;
	}
	
	public static HAPElementStructure getStructureElement(HAPIdVariable variableId, HAPDomainValueStructure valueStructureDomain) {
		HAPIdRootElement rootEleId = variableId.getRootElementId();
		HAPRootStructure root = valueStructureDomain.getValueStructureDefinitionByRuntimeId(rootEleId.getValueStructureId()).getRootByName(rootEleId.getRootName());
		return HAPUtilityStructure.getDescendant(root.getDefinition(), variableId.getElementPath().toString());
	}
	
	public static List<HAPInfoValueStructureSorting> getAllValueStructuresSorted(HAPValueContext valueContext){
		List<HAPInfoValueStructureSorting> out = getAllValueStructures(valueContext);
		sortValueStructureInfos(out);
		return out;
	}
	
	public static List<HAPInfoValueStructureSorting> getAllValueStructures(HAPValueContext valueContext){
		List<HAPInfoValueStructureSorting> out = new ArrayList<HAPInfoValueStructureSorting>();
		for(HAPPartInValueContext part : valueContext.getParts()) {
			getAllChildrenValueStructure(null, part, out);
		}
		return out;
	}

	public static Set<String> getSelfValueStructures(HAPValueContext valueContext){
		Set<String> out = new HashSet<String>();
		for(HAPPartInValueContext part : valueContext.getParts()) {
			String partType = part.getPartType();
			if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE)) {
				HAPPartInValueContextSimple simplePart = (HAPPartInValueContextSimple)part;
				String partName = simplePart.getPartInfo().getName();
				if(HAPConstantShared.VALUESTRUCTUREPART_NAME_DEFAULT.equals(partName)||HAPConstantShared.VALUESTRUCTUREPART_NAME_EXTENSION.equals(partName)) {
					for(HAPInfoValueStructure valueStructureWrapper : simplePart.getValueStructures()) {
						out.add(valueStructureWrapper.getValueStructureRuntimeId());
					}
				}
			}
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
				if(out!=0) {
					return out;
				} else {
					//if priority equal, then compare by group type
					return groupPriority1 - groupPriority0;
				}
			}
		});
	}

	private static void getAllChildrenValueStructure(List<Integer> priorityBase, HAPPartInValueContext part, List<HAPInfoValueStructureSorting> out) {
		String partType = part.getPartType();
		if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE)) {
			HAPPartInValueContextSimple simplePart = (HAPPartInValueContextSimple)part;
			for(HAPInfoValueStructure valueStructure : simplePart.getValueStructures()) {
				HAPInfoValueStructureSorting valueStructureInfo = new HAPInfoValueStructureSorting(valueStructure);
				valueStructureInfo.setPriority(appendParentInfo(priorityBase, simplePart.getPartInfo().getPriority()));
				out.add(valueStructureInfo);
			}
		}
		else if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHENTITY)){
			HAPPartInValueContextGroupWithEntity groupPart = (HAPPartInValueContextGroupWithEntity)part;
			for(HAPPartInValueContext child : groupPart.getChildren()) {
				getAllChildrenValueStructure(appendParentInfo(priorityBase, child.getPartInfo().getPriority()), child, out);
			}
		}
	}

	public static List<HAPInfoPartSimple> getAllSimpleParts(HAPValueContext valueContext){
		List<HAPInfoPartSimple> out = new ArrayList<HAPInfoPartSimple>();
		for(HAPPartInValueContext part : valueContext.getParts()) {
			getAllChildrenSimplePart(null, part, out);
		}
		sortSimplePartInfos(out);
		return out;
	}

	private static void getAllChildrenSimplePart(List<Integer> priorityBase, HAPPartInValueContext part, List<HAPInfoPartSimple> out) {
		String partType = part.getPartType();
		if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE)) {
			HAPPartInValueContextSimple simplePart = (HAPPartInValueContextSimple)part;
			HAPInfoPartSimple simpleInfo = new HAPInfoPartSimple(simplePart);
			simpleInfo.setPriority(appendParentInfo(priorityBase, simplePart.getPartInfo().getPriority()));
			out.add(simpleInfo);
		}
		else if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHENTITY)){
			HAPPartInValueContextGroupWithEntity groupPart = (HAPPartInValueContextGroupWithEntity)part;
			for(HAPPartInValueContext child : groupPart.getChildren()) {
				getAllChildrenSimplePart(appendParentInfo(priorityBase, child.getPartInfo().getPriority()), child, out);
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

	public static HAPExecutableValueStructure buildExecuatableValueStructure(HAPManualBrickValueContext valueStructureComplex) {
		HAPExecutableValueStructure out = new HAPExecutableValueStructure();
		List<HAPPartInValueContextSimple> parts = getAllSimpleParts(valueStructureComplex);
		for(int i=parts.size()-1; i>=0; i--) {
			HAPExecutableValueStructure vsExe = HAPUtilityValueStructureDomain.buildExecuatableValueStructure(parts.get(i).getValueStructureWrapper().getValueStructure());
			
		}
	}
	
	public static HAPInfoPartInValueContext cascadePartInfo(HAPInfoPartInValueContext info1, HAPInfoPartInValueContext info2) {
		HAPInfoPartInValueContext out;
		if(info1==null) {
			out = info2.cloneValueStructurePartInfo();
		} else if(info2==null) {
			out = info1.cloneValueStructurePartInfo();
		} else {
			out = info1.cloneValueStructurePartInfo().appendPath(info2.getReference()).appendPriority(info2.getPriority());
		}
		return out;
	}
	
	public static void sortParts(List<HAPPartInValueContext> parts) {
		Collections.sort(parts, new Comparator<HAPPartInValueContext>() {

			@Override
			public int compare(HAPPartInValueContext arg0, HAPPartInValueContext arg1) {
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
		if(out>0) {
			return 1;
		} else if(out<0) {
			return -1;
		} else {
			return 0;
		}
	}
	
	public static HAPInfoPartInValueContext createPartInfoDefault() {	return new HAPInfoPartInValueContext(HAPConstantShared.VALUESTRUCTUREPART_NAME_DEFAULT, HAPConstantShared.VALUESTRUCTUREPART_PRIORITY_DEFAULT);	}
	public static HAPInfoPartInValueContext createPartInfoExtension() {	return new HAPInfoPartInValueContext(HAPConstantShared.VALUESTRUCTUREPART_NAME_EXTENSION, HAPConstantShared.VALUESTRUCTUREPART_PRIORITY_EXTENSION);	}
	public static HAPInfoPartInValueContext createPartInfoFromParent() {	return new HAPInfoPartInValueContext(HAPConstantShared.VALUESTRUCTUREPART_NAME_FROMPARENT, HAPConstantShared.VALUESTRUCTUREPART_PRIORITY_FROMPARENT);	}
	
	public static void setValueStructureDefault(HAPManualBrickValueContext valueStructureComplex, HAPValueStructureInValuePort valueStructure) {
		valueStructureComplex.addPartSimple(valueStructure, HAPUtilityValueContext.createPartInfoDefault());
	}
	

	public static void setValueStructureFromParent(HAPWithValueContext withValueStructure, List<HAPPartInValueContext> partsFromParent) {
		HAPManualBrickValueContext valueStructureComplex = withValueStructure.getValueContext();
		valueStructureComplex.addPartGroup(partsFromParent, HAPUtilityValueContext.createPartInfoFromParent());
	}
	
	public static void setParentPart(HAPWithValueContext child, HAPWithValueContext parent) {	setValueStructureFromParent(child, parent.getValueContext().getValueStructures());	}

}

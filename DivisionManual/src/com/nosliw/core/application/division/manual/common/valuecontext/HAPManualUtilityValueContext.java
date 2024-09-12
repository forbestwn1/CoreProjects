package com.nosliw.core.application.division.manual.common.valuecontext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPValueContext;
import com.nosliw.core.application.HAPWithValueContext;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPRootStructure;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.common.valueport.HAPIdElement;
import com.nosliw.core.application.common.valueport.HAPIdRootElement;
import com.nosliw.core.application.common.valueport.HAPIdValuePortInBrick;
import com.nosliw.core.application.common.valueport.HAPValueStructureInValuePort11111;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;

public class HAPManualUtilityValueContext {

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
	public static String[] getVisibleToChildCategaries(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
		};
		return contextTypes;
	}
	
	public static String[] getVisibleToExternalCategaries(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC
		};
		return contextTypes;
	}


	
	public static HAPIdValuePortInBrick createValuePortIdValueContext(HAPExecutableEntity complexEntity) {
		return new HAPIdValuePortInBrick(complexEntity.getPathFromRoot().toString(), HAPConstantShared.VALUEPORT_TYPE_VALUECONTEXT, HAPConstantShared.NAME_DEFAULT);
	}
	
	public static String getExtensionValueStructure(HAPValueContext valueContext, String groupType) {
		List<HAPManualInfoPartSimple> parts = getAllSimpleParts(valueContext);
		for(HAPManualInfoPartSimple part : parts) {
			if(HAPConstantShared.VALUESTRUCTUREPART_NAME_EXTENSION.equals(part.getSimpleValueStructurePart().getPartInfo().getName())) {
				for(HAPManualInfoValueStructure valueStructureWrapper : part.getSimpleValueStructurePart().getValueStructures()) {
					if(groupType.equals(valueStructureWrapper.getGroupType())) {
						return valueStructureWrapper.getValueStructureRuntimeId();
					}
				}
			}
		}
		return null;
	}
	
	public static HAPElementStructure getStructureElement(HAPIdElement variableId, HAPDomainValueStructure valueStructureDomain) {
		HAPIdRootElement rootEleId = variableId.getRootElementId();
		HAPRootStructure root = valueStructureDomain.getValueStructureDefinitionByRuntimeId(rootEleId.getValueStructureId()).getRootByName(rootEleId.getRootName());
		return HAPUtilityStructure.getDescendant(root.getDefinition(), variableId.getElementPath().toString());
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

	public static Set<String> getSelfValueStructures(HAPValueContext valueContext){
		Set<String> out = new HashSet<String>();
		for(HAPManualPartInValueContext part : valueContext.getParts()) {
			String partType = part.getPartType();
			if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE)) {
				HAPManualPartInValueContextSimple simplePart = (HAPManualPartInValueContextSimple)part;
				String partName = simplePart.getPartInfo().getName();
				if(HAPConstantShared.VALUESTRUCTUREPART_NAME_DEFAULT.equals(partName)||HAPConstantShared.VALUESTRUCTUREPART_NAME_EXTENSION.equals(partName)) {
					for(HAPManualInfoValueStructure valueStructureWrapper : simplePart.getValueStructures()) {
						out.add(valueStructureWrapper.getValueStructureRuntimeId());
					}
				}
			}
		}
		return out;
	}
	
	private static void sortValueStructureInfos(List<HAPManualInfoValueStructureSorting> valueStructures) {
		Collections.sort(valueStructures, new Comparator<HAPManualInfoValueStructureSorting>() {
			@Override
			public int compare(HAPManualInfoValueStructureSorting arg0, HAPManualInfoValueStructureSorting arg1) {
				String groupType0 = arg0.getValueStructure().getGroupType();
				String groupType1 = arg1.getValueStructure().getGroupType();
				
				List<String> groups = getAllCategariesWithResolvePriority();
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

	private static void getAllChildrenValueStructure(List<Integer> priorityBase, HAPManualPartInValueContext part, List<HAPManualInfoValueStructureSorting> out) {
		String partType = part.getPartType();
		if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE)) {
			HAPManualPartInValueContextSimple simplePart = (HAPManualPartInValueContextSimple)part;
			for(HAPManualInfoValueStructure valueStructure : simplePart.getValueStructures()) {
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

	public static List<HAPManualInfoPartSimple> getAllSimpleParts(HAPValueContext valueContext){
		List<HAPManualInfoPartSimple> out = new ArrayList<HAPManualInfoPartSimple>();
		for(HAPManualPartInValueContext part : valueContext.getParts()) {
			getAllChildrenSimplePart(null, part, out);
		}
		sortSimplePartInfos(out);
		return out;
	}

	private static void getAllChildrenSimplePart(List<Integer> priorityBase, HAPManualPartInValueContext part, List<HAPManualInfoPartSimple> out) {
		String partType = part.getPartType();
		if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE)) {
			HAPManualPartInValueContextSimple simplePart = (HAPManualPartInValueContextSimple)part;
			HAPManualInfoPartSimple simpleInfo = new HAPManualInfoPartSimple(simplePart);
			simpleInfo.setPriority(appendParentInfo(priorityBase, simplePart.getPartInfo().getPriority()));
			out.add(simpleInfo);
		}
		else if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_GROUP_WITHENTITY)){
			HAPManualPartInValueContextGroupWithEntity groupPart = (HAPManualPartInValueContextGroupWithEntity)part;
			for(HAPManualPartInValueContext child : groupPart.getChildren()) {
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
		List<HAPManualPartInValueContextSimple> parts = getAllSimpleParts(valueStructureComplex);
		for(int i=parts.size()-1; i>=0; i--) {
			HAPExecutableValueStructure vsExe = HAPUtilityValueStructureDomain.buildExecuatableValueStructure(parts.get(i).getValueStructureWrapper().getValueStructureBlock());
			
		}
	}
	
	public static HAPManualInfoPartInValueContext cascadePartInfo(HAPManualInfoPartInValueContext info1, HAPManualInfoPartInValueContext info2) {
		HAPManualInfoPartInValueContext out;
		if(info1==null) {
			out = info2.cloneValueStructurePartInfo();
		} else if(info2==null) {
			out = info1.cloneValueStructurePartInfo();
		} else {
			out = info1.cloneValueStructurePartInfo().appendPath(info2.getReference()).appendPriority(info2.getPriority());
		}
		return out;
	}
	
	public static void sortParts(List<HAPManualPartInValueContext> parts) {
		Collections.sort(parts, new Comparator<HAPManualPartInValueContext>() {

			@Override
			public int compare(HAPManualPartInValueContext arg0, HAPManualPartInValueContext arg1) {
				return sortPriority(arg0.getPartInfo().getPriority(), arg1.getPartInfo().getPriority());
			}
		});
	}

	public static void sortSimplePartInfos(List<HAPManualInfoPartSimple> parts) {
		Collections.sort(parts, new Comparator<HAPManualInfoPartSimple>() {

			@Override
			public int compare(HAPManualInfoPartSimple arg0, HAPManualInfoPartSimple arg1) {
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
	
	public static HAPManualInfoPartInValueContext createPartInfoDefault() {	return new HAPManualInfoPartInValueContext(HAPConstantShared.VALUESTRUCTUREPART_NAME_DEFAULT, HAPConstantShared.VALUESTRUCTUREPART_PRIORITY_DEFAULT);	}
	public static HAPManualInfoPartInValueContext createPartInfoExtension() {	return new HAPManualInfoPartInValueContext(HAPConstantShared.VALUESTRUCTUREPART_NAME_EXTENSION, HAPConstantShared.VALUESTRUCTUREPART_PRIORITY_EXTENSION);	}
	public static HAPManualInfoPartInValueContext createPartInfoFromParent() {	return new HAPManualInfoPartInValueContext(HAPConstantShared.VALUESTRUCTUREPART_NAME_FROMPARENT, HAPConstantShared.VALUESTRUCTUREPART_PRIORITY_FROMPARENT);	}
	
	public static void setValueStructureDefault(HAPManualBrickValueContext valueStructureComplex, HAPValueStructureInValuePort11111 valueStructure) {
		valueStructureComplex.addPartSimple(valueStructure, HAPManualUtilityValueContext.createPartInfoDefault());
	}
	

	public static void setValueStructureFromParent(HAPWithValueContext withValueStructure, List<HAPManualPartInValueContext> partsFromParent) {
		HAPManualBrickValueContext valueStructureComplex = withValueStructure.getValueContext();
		valueStructureComplex.addPartGroup(partsFromParent, HAPManualUtilityValueContext.createPartInfoFromParent());
	}
	
	public static void setParentPart(HAPWithValueContext child, HAPWithValueContext parent) {	setValueStructureFromParent(child, parent.getValueContext().getValueStructures());	}

}

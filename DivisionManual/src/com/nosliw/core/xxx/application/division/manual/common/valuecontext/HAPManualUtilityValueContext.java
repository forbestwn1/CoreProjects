package com.nosliw.core.xxx.application.division.manual.common.valuecontext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualInfoPartInValueContext;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualPartInValueContext;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualPartInValueContextGroupWithEntity;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualPartInValueContextSimple;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualWrapperStructure;
import com.nosliw.core.application.valueport.HAPIdValuePortInBrick;
import com.nosliw.core.xxx.application.common.structure.HAPRootStructure;
import com.nosliw.core.xxx.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.xxx.application.valueport.HAPIdElement;
import com.nosliw.core.xxx.application.valueport.HAPIdRootElement;
import com.nosliw.core.xxx.application.valueport.HAPValueStructureInValuePort11111;
import com.nosliw.core.xxx.application1.HAPValueContext;
import com.nosliw.core.xxx.application1.HAPWithValueContext;

public class HAPManualUtilityValueContext {



	
	public static HAPIdValuePortInBrick createValuePortIdValueContext(HAPExecutableEntity complexEntity) {
		return new HAPIdValuePortInBrick(complexEntity.getPathFromRoot().toString(), HAPConstantShared.VALUEPORT_TYPE_VALUECONTEXT, HAPConstantShared.NAME_DEFAULT);
	}
	
	public static String getExtensionValueStructure(HAPValueContext valueContext, String groupType) {
		List<HAPManualInfoPartSimple> parts = getAllSimpleParts(valueContext);
		for(HAPManualInfoPartSimple part : parts) {
			if(HAPConstantShared.VALUESTRUCTUREPART_NAME_EXTENSION.equals(part.getSimpleValueStructurePart().getPartInfo().getName())) {
				for(HAPManualWrapperStructure valueStructureWrapper : part.getSimpleValueStructurePart().getValueStructures()) {
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
	
	public static Set<String> getSelfValueStructures(HAPValueContext valueContext){
		Set<String> out = new HashSet<String>();
		for(HAPManualPartInValueContext part : valueContext.getParts()) {
			String partType = part.getPartType();
			if(partType.equals(HAPConstantShared.VALUESTRUCTUREPART_TYPE_SIMPLE)) {
				HAPManualPartInValueContextSimple simplePart = (HAPManualPartInValueContextSimple)part;
				String partName = simplePart.getPartInfo().getName();
				if(HAPConstantShared.VALUESTRUCTUREPART_NAME_DEFAULT.equals(partName)||HAPConstantShared.VALUESTRUCTUREPART_NAME_EXTENSION.equals(partName)) {
					for(HAPManualWrapperStructure valueStructureWrapper : simplePart.getValueStructures()) {
						out.add(valueStructureWrapper.getValueStructureRuntimeId());
					}
				}
			}
		}
		return out;
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
	
	public static void sortSimplePartInfos(List<HAPManualInfoPartSimple> parts) {
		Collections.sort(parts, new Comparator<HAPManualInfoPartSimple>() {

			@Override
			public int compare(HAPManualInfoPartSimple arg0, HAPManualInfoPartSimple arg1) {
				return sortPriority(arg0.getPriority(), arg1.getPriority());
			}
		});
	}

	public static void setValueStructureDefault(HAPManualBrickValueContext valueStructureComplex, HAPValueStructureInValuePort11111 valueStructure) {
		valueStructureComplex.addPartSimple(valueStructure, HAPManualUtilityValueContext.createPartInfoDefault());
	}
	

	public static void setValueStructureFromParent(HAPWithValueContext withValueStructure, List<HAPManualPartInValueContext> partsFromParent) {
		HAPManualBrickValueContext valueStructureComplex = withValueStructure.getValueContext();
		valueStructureComplex.addPartGroup(partsFromParent, HAPManualUtilityValueContext.createPartInfoFromParent());
	}
	
	public static void setParentPart(HAPWithValueContext child, HAPWithValueContext parent) {	setValueStructureFromParent(child, parent.getValueContext().getValueStructures());	}

}

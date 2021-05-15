package com.nosliw.data.core.structure;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.dataassociation.mapping.HAPReferenceRootInMapping;
import com.nosliw.data.core.valuestructure.HAPReferenceRootInExecutable;
import com.nosliw.data.core.valuestructure.HAPReferenceRootInFlat;
import com.nosliw.data.core.valuestructure.HAPReferenceRootInGroup;

public class HAPUtilityStructureReference {

	public static HAPReferenceRoot parseRootReferenceLiterate(String rootRefLiterate, String refStructureType) {
		HAPReferenceRoot out = null;
		if(refStructureType.equals(HAPConstantShared.STRUCTURE_TYPE_VALUEGROUP)) {
			out = new HAPReferenceRootInGroup(rootRefLiterate);
		}
		else if(refStructureType.equals(HAPConstantShared.STRUCTURE_TYPE_VALUEFLAT)) {
			out = new HAPReferenceRootInFlat(rootRefLiterate);
		}
		else if(refStructureType.equals(HAPConstantShared.STRUCTURE_TYPE_VALUEEXECUTABLE)) {
			out = new HAPReferenceRootInExecutable(rootRefLiterate);
		}
		else if(refStructureType.equals(HAPConstantShared.STRUCTURE_TYPE_MAPPING)) {
			out = new HAPReferenceRootInMapping(rootRefLiterate);
		}
		else {
			out = new HAPReferenceRootUnknowType(rootRefLiterate);
		}
		return out;
	}

	public static HAPReferenceElement normalizeElementReference(HAPReferenceElement eleReference, String refStructureType) {
		if(eleReference.getRootReference().getStructureType().equals(HAPConstantShared.STRUCTURE_TYPE_UNKNOWN)) {
			HAPReferenceRootUnknowType unknowTypeEle = (HAPReferenceRootUnknowType)eleReference.getRootReference();
			eleReference.setRootReference(parseRootReferenceLiterate(unknowTypeEle.getContent(), refStructureType));
		}
		return eleReference;
	}
	
}

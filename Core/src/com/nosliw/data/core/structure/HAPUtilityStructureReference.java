package com.nosliw.data.core.structure;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.dataassociation.mapping.HAPReferenceRootInMapping;
import com.nosliw.data.core.valuestructure1.HAPReferenceRootInExecutable;
import com.nosliw.data.core.valuestructure1.HAPReferenceRootInFlat;
import com.nosliw.data.core.valuestructure1.HAPReferenceRootInGroup;

public class HAPUtilityStructureReference {

	public static HAPReferenceRootInStrucutre parseRootReferenceJson(JSONObject rootRefJson) {
		HAPReferenceRootInStrucutre out = null;
		String refStructureType = rootRefJson.getString(HAPReferenceRootInStrucutre.STRUCTURETYPE);
		if(refStructureType.equals(HAPConstantShared.STRUCTURE_TYPE_VALUEGROUP)) {
			out = new HAPReferenceRootInGroup();
		}
		else if(refStructureType.equals(HAPConstantShared.STRUCTURE_TYPE_VALUEFLAT)) {
			out = new HAPReferenceRootInFlat();
		}
		else if(refStructureType.equals(HAPConstantShared.STRUCTURE_TYPE_VALUEEXECUTABLE)) {
			out = new HAPReferenceRootInExecutable();
		}
		else if(refStructureType.equals(HAPConstantShared.STRUCTURE_TYPE_MAPPING)) {
			out = new HAPReferenceRootInMapping();
		}
		else {
			out = new HAPReferenceRootUnknowType();
		}
		out.buildObject(rootRefJson, HAPSerializationFormat.JSON);
		return out;
	}
	
	
	public static HAPReferenceRootInStrucutre parseRootReferenceLiterate(String rootRefLiterate, String refStructureType) {
		HAPReferenceRootInStrucutre out = null;
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

	public static HAPReferenceElementInStructure normalizeElementReference(HAPReferenceElementInStructure eleReference, String refStructureType) {
		if(eleReference.getRootReference().getStructureType().equals(HAPConstantShared.STRUCTURE_TYPE_UNKNOWN)) {
			HAPReferenceRootUnknowType unknowTypeEle = (HAPReferenceRootUnknowType)eleReference.getRootReference();
			eleReference.setRootReference(parseRootReferenceLiterate(unknowTypeEle.getContent(), refStructureType));
		}
		return eleReference;
	}
	
}

package com.nosliw.data.core.structure;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPUtilityStructurePath {

	public static HAPReferenceElement parseStructurePath(Object structurePathObj) {
		
	}
	
	public static HAPReferenceElement parseJsonStructurePath(JSONObject structurePathJson)  {
		
	}
	
	public static HAPReferenceElement parseLiterateStructurePath(String structurePathLiterate) {
		
	}
	
	public static HAPReferenceElement parseLiterateStructurePath(String structurePathLiterate, String refStructureType) {
		int index = structurePathLiterate.indexOf(HAPConstantShared.SEPERATOR_PATH);
		String rootRefLiterate = null;
		String path = null;
		if(index==-1){
			rootRefLiterate = structurePathLiterate;
		}
		else{
			rootRefLiterate = structurePathLiterate.substring(0, index);
			path = structurePathLiterate.substring(index+1);
		}

		HAPReferenceRoot rootRef = parseRootReferenceLiterate(rootRefLiterate);
		return new HAPReferenceElement(rootRef, path);
	}
	
	public static String toLiterateStructurePath(HAPReferenceElement path) {
		
	}
	
	public static HAPReferenceRoot parseRootReference(Object obj) {
		
	}
	
	private static HAPReferenceRoot parseRootReferenceLiterate(String rootRefLiterate) {
		
	}
	
	public static HAPReferenceRoot parseRootReferenceLiterate(String rootRefLiterate, String refStructureType) {
		
	}
}

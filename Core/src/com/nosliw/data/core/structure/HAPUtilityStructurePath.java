package com.nosliw.data.core.structure;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPUtilityStructurePath {

	public static HAPPathStructure parseStructurePath(Object structurePathObj) {
		
	}
	
	public static HAPPathStructure parseJsonStructurePath(JSONObject structurePathJson)  {
		
	}
	
	public static HAPPathStructure parseLiterateStructurePath(String structurePathLiterate) {
		
	}
	
	public static HAPPathStructure parseLiterateStructurePath(String structurePathLiterate, String refStructureType) {
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
		return new HAPPathStructure(rootRef, path);
	}
	
	public static String toLiterateStructurePath(HAPPathStructure path) {
		
	}
	
	public static HAPReferenceRoot parseRootReference(Object obj) {
		
	}
	
	private static HAPReferenceRoot parseRootReferenceLiterate(String rootRefLiterate) {
		
	}
	
}

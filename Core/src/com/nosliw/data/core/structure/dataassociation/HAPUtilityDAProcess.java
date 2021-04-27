package com.nosliw.data.core.structure.dataassociation;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;

public class HAPUtilityDAProcess {

	private static String INFO_MODIFY_INPUT_STRUCTURE = "modifyInputStructure";
	private static String INFO_MODIFY_OUTPUT_STRUCTURE = "modifyOutputStructure";

	public static HAPInfo withModifyOutputStructureConfigureTrue(HAPInfo info) {  return withModifyOutputStructureConfigure(info, true);  }

	public static HAPInfo withModifyOutputStructureConfigureFalse(HAPInfo info) {  return withModifyOutputStructureConfigure(info, false);  }

	public static HAPInfo withModifyOutputStructureConfigure(HAPInfo info, boolean value) {
		HAPInfo out = info;
		if(out==null)  out = new HAPInfoImpSimple();  
		if(value) out.setValue(INFO_MODIFY_OUTPUT_STRUCTURE, "true");
		else out.setValue(INFO_MODIFY_OUTPUT_STRUCTURE, "false");
		return out;
	}

	public static boolean ifModifyOutputStructure(HAPInfo info) {
		boolean defaultValue = true;
		if(info==null)  return defaultValue;
		String outStr = (String)info.getValue(INFO_MODIFY_OUTPUT_STRUCTURE, defaultValue+"");
		return Boolean.valueOf(outStr);
	}

	public static HAPInfo withModifyInputStructureConfigureTrue(HAPInfo info) {   return withModifyInputStructureConfigure(info, true);  }

	public static HAPInfo withModifyInputStructureConfigureFalse(HAPInfo info) {  return withModifyInputStructureConfigure(info, false); }
	
	public static HAPInfo withModifyInputStructureConfigure(HAPInfo info, boolean value) {
		HAPInfo out = info;
		if(out==null)  out = new HAPInfoImpSimple();  
		if(value)  out.setValue(INFO_MODIFY_INPUT_STRUCTURE, "true");
		else out.setValue(INFO_MODIFY_INPUT_STRUCTURE, "false");
		return out;
	}
	
	public static boolean ifModifyInputStructure(HAPInfo info) {
		boolean defaultValue = true;
		if(info==null)  return defaultValue;
		String outStr = (String)info.getValue(INFO_MODIFY_INPUT_STRUCTURE, defaultValue+"");
		return Boolean.valueOf(outStr);
	}

}

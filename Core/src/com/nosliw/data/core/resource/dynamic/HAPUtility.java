package com.nosliw.data.core.resource.dynamic;

import java.io.File;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.resource.HAPResourceDefinition1;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUtility {

	static private int index = 0;
	
	public static void exportDynamicResourceDefinition(String builderId, String resourceType, Set<HAPParmDefinition> parms, HAPResourceDefinition1 resourceDef) {
		String fileName = resourceType + "_" + builderId + "_" + index++;

		File directory = new File(HAPSystemFolderUtility.getCurrentDynamicResourceExportFolder());
	    if (! directory.exists()){
	    	directory.mkdir();
	    }
	    HAPUtilityFile.writeFile(directory.getAbsolutePath()+"/"+fileName, resourceDef.toStringValue(HAPSerializationFormat.LITERATE));
	}

}

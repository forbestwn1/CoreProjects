package com.nosliw.data.core.template;

import java.io.File;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUtility {

	static private int index = 0;
	
	public static void exportTemplateResourceDefinition(String builderId, String resourceType, Set<HAPParmDefinition> parms, HAPResourceDefinition resourceDef) {
		String fileName = resourceType + "_" + builderId + "_" + index++;

		File directory = new File(HAPSystemFolderUtility.getCurrentTemplateExportFolder());
	    if (! directory.exists()){
	    	directory.mkdir();
	    }
	    HAPFileUtility.writeFile(directory.getAbsolutePath()+"/"+fileName, resourceDef.toStringValue(HAPSerializationFormat.LITERATE));
	}

}
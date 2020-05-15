package com.nosliw.data.core.story;

import java.io.File;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUtilityStory {

	public static String getBuilderId(HAPStory story) {   return null;     }
	public static void setBuilderId(HAPStory story, String builderId) {         }

	static private int index = 0;
	
	public static void exportBuildResourceDefinition(HAPStory story, HAPResourceDefinition resourceDef) {
		String fileName = story.getResourceType() + "_" + story.getName() + "_" + index++;

		File directory = new File(HAPSystemFolderUtility.getCurrentDynamicResourceExportFolder());
	    if (! directory.exists()){
	    	directory.mkdir();
	    }
	    HAPFileUtility.writeFile(directory.getAbsolutePath()+"/"+fileName, resourceDef.toStringValue(HAPSerializationFormat.LITERATE));
	}


}

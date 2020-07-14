package com.nosliw.data.core.story.design;

import java.util.List;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUtilityDesign {

	private static final String INFO_STAGE = "stage";
	
	public static String getChangeStage(HAPChangeBatch change) {	return (String)change.getInfoValue(INFO_STAGE); 	}
	
	public static void setChangeStage(HAPChangeBatch change, String stage) {    change.getInfo().setValue(INFO_STAGE, stage);	}
	
	public static String getDesignStage(HAPDesignStory design) {
		List<HAPChangeBatch> changes = design.getChangeHistory();
		HAPChangeBatch latest = changes.get(changes.size()-1);
		return getChangeStage(latest);
	}
	
	public static HAPDesignStory readStoryDesign(String id) {
		//read content
		String file = HAPSystemFolderUtility.getStoryDesignFolder()+id+".design";
		//parse content
		HAPDesignStory out = HAPParserStoryDesign.parseFile(file);
		return out;
	}
	
	public static void saveStoryDesign(HAPDesignStory storyDesign) {
		//read content
		String file = HAPSystemFolderUtility.getStoryDesignFolder()+storyDesign.getId()+".design";
		HAPFileUtility.writeFile(file, storyDesign.toStringValue(HAPSerializationFormat.JSON));
	}
	
}

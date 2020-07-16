package com.nosliw.data.core.story.design;

import java.util.List;

import com.nosliw.common.serialization.HAPJsonUtility;
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
		//parse content
		HAPDesignStory out = HAPParserStoryDesign.parseFile(getStoryDesignFile(id));
		return out;
	}
	
	public static void saveStoryDesign(HAPDesignStory storyDesign) {
		//read content
		HAPFileUtility.writeFile(getStoryDesignFile(storyDesign.getId()), HAPJsonUtility.formatJson(storyDesign.toStringValue(HAPSerializationFormat.JSON)));
	}
	
	private static String getStoryDesignFile(String id) {
		String file = HAPSystemFolderUtility.getStoryDesignFolder()+id+"/design.json";
		return file;
	}
	
}

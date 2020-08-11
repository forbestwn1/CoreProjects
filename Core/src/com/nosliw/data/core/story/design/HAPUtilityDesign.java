package com.nosliw.data.core.story.design;

import java.util.List;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUtilityDesign {

	public static void setDesignAllStages(HAPDesignStory design, List<HAPStageInfo> stages) {    design.getInfo().setValue(HAPConstant.STORYDESIGN_INFO_STAGES, stages);      }
	public static List<HAPStageInfo> getDesignAllStages(HAPDesignStory design){    return (List<HAPStageInfo>)design.getInfoValue(HAPConstant.STORYDESIGN_INFO_STAGES);       }
	
	public static String getChangeStage(HAPDesignStep change) {	return (String)change.getInfoValue(HAPConstant.STORYDESIGN_CHANGE_INFO_STAGE); 	}
	
	public static void setChangeStage(HAPDesignStep change, String stage) {    change.getInfo().setValue(HAPConstant.STORYDESIGN_CHANGE_INFO_STAGE, stage);	}
	
	public static String getDesignStage(HAPDesignStory design) {
		List<HAPDesignStep> changes = design.getChangeHistory();
		HAPDesignStep latest = changes.get(changes.size()-1);
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

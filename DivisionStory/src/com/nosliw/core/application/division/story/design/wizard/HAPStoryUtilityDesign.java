package com.nosliw.core.application.division.story.design.wizard;

import java.util.List;
import java.util.Set;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.division.story.HAPStoryStory;
import com.nosliw.core.application.division.story.change.HAPStoryChangeItem;
import com.nosliw.core.application.division.story.change.HAPStoryManagerChange;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPStoryUtilityDesign {

	public static void setDesignAllStages(HAPStoryDesignStory design, List<HAPStoryStageInfo> stages) {    design.getInfo().setValue(HAPConstantShared.STORYDESIGN_INFO_STAGES, stages);      }
	public static List<HAPStoryStageInfo> getDesignAllStages(HAPStoryDesignStory design){    return (List<HAPStoryStageInfo>)design.getInfoValue(HAPConstantShared.STORYDESIGN_INFO_STAGES);       }
	
	public static String getChangeStage(HAPStoryDesignStep step) {	return (String)step.getInfoValue(HAPConstantShared.STORYDESIGN_CHANGE_INFO_STAGE); 	}
	
	public static void setChangeStage(HAPStoryDesignStep step, String stage) {    step.getInfo().setValue(HAPConstantShared.STORYDESIGN_CHANGE_INFO_STAGE, stage);	}
	
	public static String getDesignStage(HAPStoryDesignStory design) {
		List<HAPStoryDesignStep> changes = design.getChangeHistory();
		HAPStoryDesignStep latest = changes.get(changes.size()-1);
		return getChangeStage(latest);
	}
	
	public static HAPStoryDesignStory readStoryDesign(String id, HAPStoryManagerChange changeMan) {
		//parse content
		HAPStoryDesignStory out = HAPStoryParserStoryDesign.parseFile(getStoryDesignFile(id), changeMan);
		return out;
	}
	
	public static void saveStoryDesign(HAPStoryDesignStory storyDesign) {
		//read content
		HAPUtilityFile.writeFile(getStoryDesignFile(storyDesign.getId()), HAPUtilityJson.formatJson(storyDesign.toStringValue(HAPSerializationFormat.JSON)));
	}
	
	private static String getStoryDesignFile(String id) {
		String file = HAPSystemFolderUtility.getStoryDesignFolder()+id+"/design.json";
		return file;
	}
	
	public static void reverseChangeStep(HAPStoryStory story, HAPStoryDesignStep step, HAPStoryManagerChange changeMan) {
		List<HAPStoryChangeItem> changes = step.getChanges();
		changeMan.revertChange(story, changes);
		reverseQuestionAnswer(story, step.getQuestionair(), changeMan);
	}
	
	public static void reverseQuestionAnswer(HAPStoryStory story, HAPStoryQuestionnaire questionair, HAPStoryManagerChange changeMan) {
		Set<HAPStoryAnswer> answers = questionair.getAnswers();
		for(HAPStoryAnswer answer : answers) {
			changeMan.revertChange(story, answer.getChanges());
		}
		questionair.clearAnswer();
	}
}

package com.nosliw.data.core.story;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceUtility;
import com.nosliw.data.core.story.design.HAPBuilderStory;
import com.nosliw.data.core.story.design.HAPDesignStep;
import com.nosliw.data.core.story.design.HAPDesignStory;
import com.nosliw.data.core.story.design.HAPQuestionnaire;
import com.nosliw.data.core.story.design.HAPRequestDesign;
import com.nosliw.data.core.story.design.HAPUtilityDesign;
import com.nosliw.data.core.story.resource.HAPResourceDefinitionStory;
import com.nosliw.data.core.story.resource.HAPResourceIdStory;
import com.nosliw.data.core.story.resource.HAPStoryId;

public class HAPManagerStory {

	private long m_idIndex;
	
	private HAPManagerResourceDefinition m_resourceDefManager;

	private Map<String, HAPBuilderShow> m_resourceDefinitionBuilders;
	
	private Map<String, HAPBuilderStory> m_storyDirectors;
	
	public HAPManagerStory(HAPManagerResourceDefinition resourceDefManager) {
		this.m_resourceDefManager = resourceDefManager;
		this.m_resourceDefinitionBuilders = new LinkedHashMap<String, HAPBuilderShow>();
		this.m_storyDirectors = new LinkedHashMap<String, HAPBuilderStory>();
		this.m_idIndex = System.currentTimeMillis();
	}
	
	public HAPDesignStory newStoryDesign(String builderId) {
		HAPBuilderStory storyBuilder = this.getDesignDirector(builderId);
		HAPDesignStory out = new HAPDesignStory(this.generateId(), builderId);
		storyBuilder.initDesign(out);
		this.saveStoryDesign(out);
		return out;
	}
	
	public HAPServiceData designStory(HAPRequestDesign changeRequest) {
		HAPDesignStory design = this.getStoryDesign(changeRequest.getDesignId());
		HAPStory story = design.getStory();
		String directorId = design.getDirectorId();
		
		//clear step on top of step index
		int stepIndex = changeRequest.getStepCursor();
		List<HAPDesignStep> changeHistory = design.getChangeHistory();
		for(int i=changeHistory.size()-1; i>stepIndex; i--) {
			HAPUtilityDesign.reverseChangeStep(story, changeHistory.get(i));
			changeHistory.remove(i);
		}
		
		//clear current questionair
		HAPQuestionnaire currentQuestionair = changeHistory.get(stepIndex).getQuestionair();
		HAPUtilityDesign.reverseQuestionAnswer(story, currentQuestionair);
		
		HAPServiceData out = this.getDesignDirector(directorId).buildStory(design, changeRequest);
		if(out.isSuccess()) {
			this.saveStoryDesign(design);
		}
		return out;
	}

	public HAPDesignStory getStoryDesign(String id) {	return HAPUtilityDesign.readStoryDesign(id);	}

	public void saveStoryDesign(HAPDesignStory storyDesign) {  HAPUtilityDesign.saveStoryDesign(storyDesign);	}
	
	//get story by id
	public HAPResourceDefinitionStory getStoryResource(String id) {
		return (HAPResourceDefinitionStory)this.m_resourceDefManager.getResourceDefinition(new HAPResourceIdStory(new HAPStoryId(id)));
	}

	//convert story to particular resouce
	public HAPResourceDefinition buildShow(HAPStory story) {
		HAPResourceDefinition out = this.m_resourceDefinitionBuilders.get(story.getShowType()).buildShow(story);
		String tempFileName = HAPUtilityStory.exportBuildResourceDefinition(story, out);
		out.setResourceId(HAPResourceUtility.createFileBaseResourceId(story.getShowType(), tempFileName));
		return out;
	}

	public void registerShowBuilder(String id, HAPBuilderShow builder) {	this.m_resourceDefinitionBuilders.put(id, builder);	}
	
	public void registerDesignDirector(String id, HAPBuilderStory storyDirector) {		this.m_storyDirectors.put(id, storyDirector);	}
	private HAPBuilderStory getDesignDirector(String directorId) {    return this.m_storyDirectors.get(directorId);   }

	private String generateId() {		return (this.m_idIndex++) + "";	}
}

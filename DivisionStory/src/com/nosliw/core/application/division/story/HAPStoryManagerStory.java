package com.nosliw.core.application.division.story;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.core.application.division.story.change.HAPStoryManagerChange;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryBuilderStory;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryDesignStep;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryDesignStory;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryQuestionnaire;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryRequestDesign;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryUtilityDesign;
import com.nosliw.data.core.resource.HAPResourceDefinition1;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.story.resource.HAPResourceDefinitionStory;
import com.nosliw.data.core.story.resource.HAPResourceIdStory;
import com.nosliw.data.core.story.resource.HAPStoryId;

public class HAPStoryManagerStory {

	private long m_idIndex;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	private HAPStoryManagerChange m_changeManager;
	
	private Map<String, HAPStoryBuilderShow> m_resourceDefinitionBuilders;
	
	private Map<String, HAPStoryBuilderStory> m_storyDirectors;
	
	public HAPStoryManagerStory(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_changeManager = new HAPStoryManagerChange(runtimeEnv);
		this.m_resourceDefinitionBuilders = new LinkedHashMap<String, HAPStoryBuilderShow>();
		this.m_storyDirectors = new LinkedHashMap<String, HAPStoryBuilderStory>();
		this.m_idIndex = System.currentTimeMillis();
	}
	
	public HAPStoryManagerChange getChangeManager() {    return this.m_changeManager;     }
	
	public HAPStoryDesignStory newStoryDesign(String builderId) {
		HAPStoryBuilderStory storyBuilder = this.getDesignDirector(builderId);
		HAPStoryDesignStory out = new HAPStoryDesignStory(this.generateId(), builderId, this.m_changeManager);
		storyBuilder.initDesign(out);
		this.saveStoryDesign(out);
		return out;
	}
	
	public HAPServiceData designStory(HAPStoryRequestDesign changeRequest) {
		HAPStoryDesignStory design = this.getStoryDesign(changeRequest.getDesignId());
		HAPStoryStory story = design.getStory();
		String directorId = design.getDirectorId();
		
		//clear step on top of step index
		int stepIndex = changeRequest.getStepCursor();
		List<HAPStoryDesignStep> changeHistory = design.getChangeHistory();
		for(int i=changeHistory.size()-1; i>stepIndex; i--) {
			HAPStoryUtilityDesign.reverseChangeStep(story, changeHistory.get(i), this.getChangeManager());
			changeHistory.remove(i);
		}
		
		//clear current questionair
		HAPStoryQuestionnaire currentQuestionair = changeHistory.get(stepIndex).getQuestionair();
		HAPStoryUtilityDesign.reverseQuestionAnswer(story, currentQuestionair, this.getChangeManager());
		
		HAPServiceData out = this.getDesignDirector(directorId).buildStory(design, changeRequest);
		if(out.isSuccess()) {
			this.saveStoryDesign(design);
		}
		return out;
	}

	public HAPStoryDesignStory getStoryDesign(String id) {	return HAPStoryUtilityDesign.readStoryDesign(id, this.m_changeManager);	}

	public void saveStoryDesign(HAPStoryDesignStory storyDesign) {  HAPStoryUtilityDesign.saveStoryDesign(storyDesign);	}
	
	//get story by id
	public HAPResourceDefinitionStory getStoryResource(String id) {
		return (HAPResourceDefinitionStory)this.m_runtimeEnv.getResourceDefinitionManager().getLocalResourceDefinition(new HAPResourceIdStory(new HAPStoryId(id)));
	}

	//convert story to particular resouce
	public HAPResourceDefinition1 buildShow(HAPStoryStory story) {
		HAPResourceDefinition1 out = this.m_resourceDefinitionBuilders.get(story.getShowType()).buildShow(story);
		String tempFileName = HAPStoryUtilityStory.exportBuildResourceDefinition(story, out);
		out.setReferedResourceId(HAPUtilityResourceId.createFileBaseResourceId(story.getShowType(), tempFileName));
		return out;
	}

	public void registerShowBuilder(String id, HAPStoryBuilderShow builder) {	this.m_resourceDefinitionBuilders.put(id, builder);	}
	
	public void registerDesignDirector(String id, HAPStoryBuilderStory storyDirector) {		this.m_storyDirectors.put(id, storyDirector);	}
	private HAPStoryBuilderStory getDesignDirector(String directorId) {    return this.m_storyDirectors.get(directorId);   }

	private String generateId() {		return (this.m_idIndex++) + "";	}
}

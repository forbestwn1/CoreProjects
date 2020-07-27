package com.nosliw.uiresource.page.story;

import java.util.List;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceParm;
import com.nosliw.data.core.service.interfacee.HAPServiceResult;
import com.nosliw.data.core.service.provide.HAPManagerService;
import com.nosliw.data.core.story.HAPInfoElement;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryNode;
import com.nosliw.data.core.story.HAPUtilityConnection;
import com.nosliw.data.core.story.HAPUtilityStory;
import com.nosliw.data.core.story.design.HAPBuilderStory;
import com.nosliw.data.core.story.design.HAPChangeItem;
import com.nosliw.data.core.story.design.HAPChangeResult;
import com.nosliw.data.core.story.design.HAPDesignStep;
import com.nosliw.data.core.story.design.HAPDesignStory;
import com.nosliw.data.core.story.design.HAPQuestionGroup;
import com.nosliw.data.core.story.design.HAPQuestionItem;
import com.nosliw.data.core.story.design.HAPRequestChange;
import com.nosliw.data.core.story.design.HAPUtilityChange;
import com.nosliw.data.core.story.design.HAPUtilityDesign;
import com.nosliw.data.core.story.element.connectiongroup.HAPElementGroupBatch;
import com.nosliw.data.core.story.element.connectiongroup.HAPElementGroupSwitch;
import com.nosliw.data.core.story.element.node.HAPStoryNodeConstant;
import com.nosliw.data.core.story.element.node.HAPStoryNodeService;
import com.nosliw.data.core.story.element.node.HAPStoryNodeServiceInput;
import com.nosliw.data.core.story.element.node.HAPStoryNodeServiceInputParm;
import com.nosliw.data.core.story.element.node.HAPStoryNodeServiceOutput;
import com.nosliw.data.core.story.element.node.HAPStoryNodeVariable;

public class HAPStoryBuilderPageSimple implements HAPBuilderStory{

	public final static String BUILDERID = "pageSimple";
	
	public final static String STAGE_SERVICE = "service";
	public final static String STAGE_UI = "ui";
	public final static String STAGE_END = "end";
	
	private HAPManagerService m_serviceManager;
	
	public HAPStoryBuilderPageSimple(HAPManagerService serviceManager) {
		this.m_serviceManager = serviceManager;
	}
	
	@Override
	public void initDesign(HAPDesignStory design) {
		design.getStory().setShowType(HAPConstant.RUNTIME_RESOURCE_TYPE_UIRESOURCE);
		
		HAPDesignStep step = new HAPDesignStep();
		
		//new service node
		HAPChangeResult newServiceNodeChange = HAPUtilityChange.buildChangeNewAndApply(design.getStory(), new HAPStoryNodeService(), step.getChanges());
		
		//extra info
		HAPQuestionGroup rootQuestionGroup = new HAPQuestionGroup("Please select service.");
		HAPQuestionItem serviceItemExtraInfo = new HAPQuestionItem("select service", newServiceNodeChange.getStoryElement().getElementId());
		rootQuestionGroup.addChild(serviceItemExtraInfo);
		step.setQuestion(rootQuestionGroup);
		
		//stage
		HAPUtilityDesign.setChangeStage(step, STAGE_SERVICE);

		design.addStep(step);
	}

	@Override
	public HAPServiceData buildStory(HAPDesignStory storyDesign, HAPRequestChange answer) {
		HAPServiceData out = null;
		String stage = HAPUtilityDesign.getDesignStage(storyDesign);
		if(stage.equals(STAGE_SERVICE)) {
			out = this.processServiceStage(storyDesign, answer);
		}
		else if(stage.equals(STAGE_UI)) {
			out = this.processUIChangeStage(storyDesign, answer);
		}
		else if(stage.equals(STAGE_END)) {
			
		}
		
		return out;
	}
	
	private HAPServiceData processServiceStage(HAPDesignStory design, HAPRequestChange answer) {
		HAPStory story = design.getStory();
		
		//apply answer to story
		List<HAPChangeItem> answerChanges = answer.getChangeItems();
		HAPUtilityChange.applyChange(story, answerChanges);

		//add answer to step
		HAPDesignStep latestStep = design.getLatestStep();
		latestStep.addAnswers(answerChanges);
				
		//new step
		HAPDesignStep step = new HAPDesignStep();

		//question
		HAPQuestionGroup rootQuestionGroup = new HAPQuestionGroup("Please select ui.");
		step.setQuestion(rootQuestionGroup);

		HAPStoryNodeService serviceStoryNode = (HAPStoryNodeService)HAPUtilityStory.getStoryNodeByType(story, HAPConstant.STORYNODE_TYPE_SERVICE).iterator().next();
		HAPServiceInterface serviceInterface = this.m_serviceManager.getServiceDefinitionManager().getDefinition(serviceStoryNode.getReferenceId()).getStaticInfo().getInterface();
		
		//service input
		HAPChangeResult serviceInputNewResult = HAPUtilityChange.buildChangeNewAndApply(story, new HAPStoryNodeServiceInput(), step.getChanges());
		HAPUtilityChange.buildChangeNewAndApply(story, HAPUtilityConnection.newConnectionContain(serviceStoryNode.getId(), serviceInputNewResult.getStoryElement().getId(), HAPConstant.SERVICE_CHILD_INPUT), step.getChanges());
		
		//parms
		for(String parmName : serviceInterface.getParmNames()) {
			
			HAPQuestionGroup parmQuestionGroup = new HAPQuestionGroup("Configure Parm " + parmName);
			rootQuestionGroup.addChild(parmQuestionGroup);
			
			HAPServiceParm parmDef = serviceInterface.getParm(parmName);
			HAPChangeResult parmNewResult = HAPUtilityChange.buildChangeNewAndApply(story, new HAPStoryNodeServiceInputParm(parmDef), step.getChanges());
			HAPChangeResult parmConnectionNewResult = HAPUtilityChange.buildChangeNewAndApply(story, HAPUtilityConnection.newConnectionContain(serviceInputNewResult.getStoryElement().getId(), parmNewResult.getStoryElement().getId(), parmName), step.getChanges());

			HAPChangeResult parmConstantProviderNewResult = HAPUtilityChange.buildChangeNewAndApply(story, new HAPStoryNodeConstant(parmDef.getCriteria()), step.getChanges());
			HAPChangeResult parmConstantProviderConnectionNewResult = HAPUtilityChange.buildChangeNewAndApply(story, HAPUtilityConnection.newConnectionOnewayDataIO(parmConstantProviderNewResult.getStoryElement().getId(), parmNewResult.getStoryElement().getId(), null, null), step.getChanges());
			HAPElementGroupBatch constantBatchGroup = new HAPElementGroupBatch(story);
			constantBatchGroup.addElement(new HAPInfoElement(parmConstantProviderNewResult.getStoryElement().getElementId()));
			constantBatchGroup.addElement(new HAPInfoElement(parmConstantProviderConnectionNewResult.getStoryElement().getElementId()));
			HAPChangeResult constantProviderGroupNewResult = HAPUtilityChange.buildChangeNewAndApply(story, constantBatchGroup, step.getChanges());
			
			HAPChangeResult parmVariableProviderNewResult = HAPUtilityChange.buildChangeNewAndApply(story, new HAPStoryNodeVariable(parmName, parmDef.getCriteria()), step.getChanges());
			HAPChangeResult parmVariableProviderConnectionNewResult = HAPUtilityChange.buildChangeNewAndApply(story, HAPUtilityConnection.newConnectionOnewayDataIO(parmVariableProviderNewResult.getStoryElement().getId(), parmNewResult.getStoryElement().getId(), null, null), step.getChanges());
			HAPElementGroupBatch variableBatchGroup = new HAPElementGroupBatch(story);
			variableBatchGroup.addElement(new HAPInfoElement(parmVariableProviderNewResult.getStoryElement().getElementId()));
			variableBatchGroup.addElement(new HAPInfoElement(parmVariableProviderConnectionNewResult.getStoryElement().getElementId()));
			HAPChangeResult variableProviderGroupNewResult = HAPUtilityChange.buildChangeNewAndApply(story, variableBatchGroup, step.getChanges());

			HAPElementGroupSwitch group = new HAPElementGroupSwitch(story);
			//add constant to group
			HAPInfoElement constantGroupEle = new HAPInfoElement(constantProviderGroupNewResult.getStoryElement().getElementId());
			constantGroupEle.setName("Constant");
			group.addElement(constantGroupEle);
			//add variable to group
			HAPInfoElement varGroupEle = new HAPInfoElement(variableProviderGroupNewResult.getStoryElement().getElementId());
			varGroupEle.setName("Variable");
			group.addElement(varGroupEle);
			HAPChangeResult groupNewResult = HAPUtilityChange.buildChangeNewAndApply(story, group, step.getChanges());

			//set switch group choice
			HAPUtilityChange.buildChangePatchAndApply(story, groupNewResult.getStoryElement().getElementId(), HAPElementGroupSwitch.CHOICE, constantProviderGroupNewResult.getStoryElement().getElementId().toStringValue(HAPSerializationFormat.LITERATE), step.getChanges());

			//question item
			HAPQuestionItem groupQuestion = new HAPQuestionItem("select import for parm", groupNewResult.getStoryElement().getElementId());
			parmQuestionGroup.addChild(groupQuestion);
			
			HAPQuestionItem constantQuestion = new HAPQuestionItem("select constant", parmConstantProviderNewResult.getStoryElement().getElementId());
			parmQuestionGroup.addChild(constantQuestion);
			
			HAPQuestionItem varQuestion = new HAPQuestionItem("select variable", parmVariableProviderNewResult.getStoryElement().getElementId());
			parmQuestionGroup.addChild(varQuestion);
		}
		
		HAPStoryNode outputNode = design.getStory().addNode(new HAPStoryNodeServiceOutput());
		Map<String, HAPServiceResult> resultes = serviceInterface.getResults();
		
		design.addStep(step);

		return HAPServiceData.createSuccessData(step);
	}

	private HAPServiceData processUIChangeStage(HAPDesignStory design, HAPRequestChange changeRequest) {
		return null;
	}


}

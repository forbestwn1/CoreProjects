package com.nosliw.core.application.division.story.design.wizard.servicedriven;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nosliw.common.displayresource.HAPDisplayResourceNode;
import com.nosliw.common.displayresource.HAPDisplayValueInfo;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.core.application.brick.service.profile.HAPBlockServiceProfile;
import com.nosliw.core.application.brick.service.profile.HAPInfoServiceStatic;
import com.nosliw.core.application.common.interactive.HAPInteractive;
import com.nosliw.core.application.common.interactive.HAPInteractiveResultTask;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.common.interactive.HAPRequestParmInInteractive;
import com.nosliw.core.application.common.interactive.HAPResultElementInInteractiveTask;
import com.nosliw.core.application.common.variable.HAPVariableDataInfo;
import com.nosliw.core.application.common.variable.HAPVariableDefinition;
import com.nosliw.core.application.division.story.HAPStoryAliasElement;
import com.nosliw.core.application.division.story.HAPStoryInfoElement;
import com.nosliw.core.application.division.story.HAPStoryStory;
import com.nosliw.core.application.division.story.HAPStoryUtilityConnection;
import com.nosliw.core.application.division.story.HAPStoryUtilityStory;
import com.nosliw.core.application.division.story.HAPStoryUtilityVariable;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;
import com.nosliw.core.application.division.story.brick.HAPStoryNode;
import com.nosliw.core.application.division.story.brick.group.HAPStoryElementGroupBatch;
import com.nosliw.core.application.division.story.brick.group.HAPStoryElementGroupSwitch;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeConstant;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeModule;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeScript;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeService;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeServiceInput;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeServiceInputParm;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeServiceOutput;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeServiceOutputItem;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeUI;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeUIHtml;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeUIPage;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeUITagData;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeUITagOther;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeVariable;
import com.nosliw.core.application.division.story.change.HAPStoryChangeItem;
import com.nosliw.core.application.division.story.change.HAPStoryChangeItemNew;
import com.nosliw.core.application.division.story.change.HAPStoryManagerChange;
import com.nosliw.core.application.division.story.change.HAPStoryRequestChangeWrapper;
import com.nosliw.core.application.division.story.change.HAPStoryResultTransaction;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryAnswer;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryBuilderStory;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryDesignStep;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryDesignStory;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryQuestion;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryQuestionGroup;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryQuestionItem;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryRequestDesign;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryResponseDesign;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryStageInfo;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryUtilityDesign;
import com.nosliw.core.application.uitag.HAPManagerUITag;
import com.nosliw.core.application.uitag.HAPUITagInfo;
import com.nosliw.core.application.uitag.HAPUITageQueryData;
import com.nosliw.core.data.HAPData;
import com.nosliw.core.data.HAPDataType;
import com.nosliw.core.data.HAPDataTypeId;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.criteria.HAPParserCriteria;
import com.nosliw.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPStoryBuilderPageSimple implements HAPStoryBuilderStory{

	public final static String BUILDERID = "pageSimple";
	
	public final static String STAGE_SERVICE = "Service";
	public final static String STAGE_UI = "UI";
	public final static String STAGE_END = "Finish";

	private final static String ALIAS_LAYOUTNODE = "ALIAS_LAYOUTNODE"; 
	
	private final static HAPStoryAliasElement ELEMENT_SERVICE = new HAPStoryAliasElement("service", false);
	private final static HAPStoryAliasElement ELEMENT_MODULE = new HAPStoryAliasElement("module", false);
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	private HAPStoryManagerChange m_changeManager;
	
	private HAPManagerUITag m_uiTagManager;
	
	private List<HAPStoryStageInfo> m_stages;
	
	public HAPStoryBuilderPageSimple(HAPManagerUITag uiTagMan, HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_uiTagManager = uiTagMan;
		this.m_stages = new ArrayList<HAPStoryStageInfo>();
		this.m_stages.add(new HAPStoryStageInfo(STAGE_SERVICE, STAGE_SERVICE));
		this.m_stages.add(new HAPStoryStageInfo(STAGE_UI, STAGE_UI));
		this.m_stages.add(new HAPStoryStageInfo(STAGE_END, STAGE_END));
	}
	
	@Override
	public void initDesign(HAPStoryDesignStory design) {
		HAPStoryStory story = design.getStory();

		story.setShowType(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIRESOURCE);
		HAPStoryUtilityDesign.setDesignAllStages(design, m_stages);
		
		story.startTransaction();
		HAPStoryRequestChangeWrapper changeRequest = new HAPStoryRequestChangeWrapper(story);

		//new module
		changeRequest.addChange(new HAPStoryChangeItemNew(new HAPStoryNodeModule(), ELEMENT_MODULE));
		
		//new service
		changeRequest.addChange(new HAPStoryChangeItemNew(new HAPStoryNodeService(), ELEMENT_SERVICE));

		//module container service
		HAPStoryUtilityStory.addNodeAsChild(ELEMENT_MODULE, ELEMENT_SERVICE, HAPStoryNodeService.STORYNODE_TYPE, changeRequest, false);
		
		changeRequest.close();
		HAPStoryResultTransaction transactionResult = story.commitTransaction();
		
		HAPStoryDesignStep step = design.newStep();
		step.addChanges(transactionResult.getChanges());
		
		//extra info
		HAPStoryQuestionGroup rootQuestionGroup = new HAPStoryQuestionGroup();
		HAPStoryQuestionItem serviceItemExtraInfo = new HAPStoryQuestionItem("Please select data source", ELEMENT_SERVICE, true);
		rootQuestionGroup.addChild(serviceItemExtraInfo);
		step.setQuestion(rootQuestionGroup);
		
		//stage
		HAPStoryUtilityDesign.setChangeStage(step, STAGE_SERVICE);

		design.addStep(step);
	}

	@Override
	public HAPServiceData buildStory(HAPStoryDesignStory storyDesign, HAPStoryRequestDesign answer) {
		HAPServiceData out = null;
		String stage = HAPStoryUtilityDesign.getDesignStage(storyDesign);
		if(stage.equals(STAGE_SERVICE)) {
			out = this.processServiceStage(storyDesign, answer);
		}
		else if(stage.equals(STAGE_UI)) {
			out = this.processUIChangeStage(storyDesign, answer);
		}
		else if(stage.equals(HAPConstantShared.DESIGNSTAGE_NAME_END)){
			String[] errorMsg = {"The wizard has finished"};
			out = HAPServiceData.createFailureData(errorMsg, "Invalid flow!!!");
		}
		else {
			String[] errorMsg = {"Unrecognize step"};
			out = HAPServiceData.createFailureData(errorMsg, "Invalid flow!!!");
		}
		
		return out;
	}
	
	private HAPServiceData validateServiceAnswer(HAPStoryDesignStory design, HAPStoryRequestDesign answerRequest) {
		HAPStoryStory story = design.getStory();

		story.startTransaction();
		
		this.applyAnswer(story, answerRequest);
		
		HAPStoryNodeService serviceStoryNode = (HAPStoryNodeService)HAPStoryUtilityStory.getAllStoryNodeByType(story, HAPConstantShared.STORYNODE_TYPE_SERVICE).iterator().next();
		if(!HAPUtilityBasic.isStringEmpty(serviceStoryNode.getReferenceId())){
			//valid
			//add answer to step
			HAPStoryDesignStep latestStep = design.getLatestStep();
			latestStep.addAnswers(answerRequest.getAnswers());
			story.commitTransaction();
			return HAPServiceData.createSuccessData();
		}
		else {
			//failure
			//revert answer changes
			story.rollbackTransaction();
			//
			String[] errorMsg = {"Service should not be empty!!"};
			return HAPServiceData.createFailureData(errorMsg, "Validation Fail!!");
		}
	}
	
	private HAPServiceData processServiceStage(HAPStoryDesignStory design, HAPStoryRequestDesign answer) {
		HAPStoryStory story = design.getStory();
		
		HAPServiceData validateResult = validateServiceAnswer(design, answer);
		if(validateResult.isFail()) {
			return validateResult;
		} else {
			story.startTransaction();
			
			//new step
			HAPStoryDesignStep step = design.newStep();

			HAPStoryNodeModule moduleStoryNode = (HAPStoryNodeModule)HAPStoryUtilityStory.getAllStoryNodeByType(story, HAPConstantShared.STORYNODE_TYPE_MODULE).iterator().next();
			
			//service parm
			List<HAPParmBranchInfo> parmBranchInfos = new ArrayList<HAPParmBranchInfo>();
			List<HAPOutputBranchInfo> outputBranchInfos = new ArrayList<HAPOutputBranchInfo>();
			
			{
				//data related layer
				HAPStoryRequestChangeWrapper dataLayerChangeRequest = new HAPStoryRequestChangeWrapper(story, true, true);
	
				//get service node
				HAPStoryNodeService serviceStoryNode = (HAPStoryNodeService)HAPStoryUtilityStory.getAllStoryNodeByType(story, HAPConstantShared.STORYNODE_TYPE_SERVICE).iterator().next();
				HAPBlockServiceProfile serviceProfile = this.m_runtimeEnv.getServiceManager().getServiceInfo(serviceStoryNode.getReferenceId()).getServiceProfileInfo();
				
				HAPInteractiveTask serviceInterface = ((HAPBlockInteractiveInterfaceTask)HAPUtilityBrick.getBrick(serviceProfile.getTaskInterface(), this.m_runtimeEnv.getBrickManager())).getValue();
				HAPDisplayResourceNode serviceDisplayResource = serviceProfile.getDisplayResource();
				HAPDisplayResourceNode interfaceDisplayResource = serviceDisplayResource.getResourceNode(HAPInfoServiceStatic.INTERFACE);
				
				//service input
				HAPStoryAliasElement serviceInputNodeName = dataLayerChangeRequest.addNewChange(new HAPStoryNodeServiceInput()).getAlias();
				dataLayerChangeRequest.addNewChange(HAPStoryUtilityConnection.newConnectionContain(serviceStoryNode.getElementId(), serviceInputNodeName, HAPConstantShared.SERVICE_CHILD_INPUT, null));
				HAPDisplayResourceNode inputDisplayResource = interfaceDisplayResource.getResourceNode(HAPInteractive.REQUEST);
				
				//parms
				for(HAPRequestParmInInteractive parm : serviceInterface.getRequestParms()) {
					String parmName = parm.getName();
					HAPParmBranchInfo parmBranchInfo = new HAPParmBranchInfo();
					parmBranchInfo.parmDef = parm;
					parmBranchInfo.displayResource = inputDisplayResource.getResourceNode(parmName);

					//parm and connection to input
					HAPStoryAliasElement parmNodeName = dataLayerChangeRequest.addNewChange(new HAPStoryNodeServiceInputParm(parmBranchInfo.parmDef)).getAlias();
					dataLayerChangeRequest.addPatchChange(parmNodeName, HAPStoryElement.DISPLAYRESOURCE, inputDisplayResource.getResourceNode(parmName));
					dataLayerChangeRequest.addNewChange(HAPStoryUtilityConnection.newConnectionContain(serviceInputNodeName, parmNodeName, parmName, null));

					//constant path and group
					HAPData defaultData = parmBranchInfo.parmDef.getDefaultValue();
					parmBranchInfo.constantAlias = dataLayerChangeRequest.addNewChange(new HAPStoryNodeConstant(parmBranchInfo.parmDef.getDataInfo(), defaultData, defaultData==null)).getAlias();
					dataLayerChangeRequest.addPatchChange(parmBranchInfo.constantAlias, HAPStoryElement.DISPLAYRESOURCE, inputDisplayResource.getResourceNode(parmName));
					HAPStoryAliasElement constantConnectionNodeName = dataLayerChangeRequest.addNewChange(HAPStoryUtilityConnection.newConnectionOnewayDataIO(parmBranchInfo.constantAlias, parmNodeName, null, null)).getAlias();

					parmBranchInfo.constantGroupAlias = dataLayerChangeRequest.addNewChange(new HAPStoryElementGroupBatch()).getAlias();
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.constantGroupAlias, new HAPStoryInfoElement(parmBranchInfo.constantAlias));
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.constantGroupAlias, new HAPStoryInfoElement(constantConnectionNodeName));
					
					//variable path and group
					parmBranchInfo.variableAlias = dataLayerChangeRequest.addNewChange(new HAPStoryNodeVariable(parmBranchInfo.parmDef, defaultData)).getAlias();
					dataLayerChangeRequest.addPatchChange(parmBranchInfo.variableAlias, HAPStoryElement.DISPLAYRESOURCE, inputDisplayResource.getResourceNode(parmName));
					HAPStoryAliasElement variableConnectionNodeName = dataLayerChangeRequest.addNewChange(HAPStoryUtilityConnection.newConnectionOnewayDataIO(parmBranchInfo.variableAlias, parmNodeName, null, null)).getAlias();
					HAPStoryUtilityVariable.addVariableToNode(moduleStoryNode.getElementId(), parmBranchInfo.variableAlias, null, dataLayerChangeRequest);

					parmBranchInfo.varGroupAlias = dataLayerChangeRequest.addNewChange(new HAPStoryElementGroupBatch()).getAlias();
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.varGroupAlias, new HAPStoryInfoElement(parmBranchInfo.variableAlias));
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.varGroupAlias, new HAPStoryInfoElement(variableConnectionNodeName));

					//switch group
					parmBranchInfo.switchAlias = dataLayerChangeRequest.addNewChange(new HAPStoryElementGroupSwitch()).getAlias();
					//add constant group to switch group
					HAPStoryInfoElement constantGroupEle = new HAPStoryInfoElement(parmBranchInfo.constantGroupAlias);
					constantGroupEle.setName("Constant");
					constantGroupEle.setDisplayName("I set value now");
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.switchAlias, constantGroupEle);

					//add variable group to switch group
					HAPStoryInfoElement varGroupEle = new HAPStoryInfoElement(parmBranchInfo.varGroupAlias);
					varGroupEle.setName("Variable");
					varGroupEle.setDisplayName("User input when they use the app");
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.switchAlias, varGroupEle);

					//set switch group choice
					dataLayerChangeRequest.addPatchChange(parmBranchInfo.switchAlias, HAPStoryElementGroupSwitch.CHOICE, varGroupEle.getName());
//					dataLayerChangeRequest.addPatchChange(parmBranchInfo.switchAlias, HAPStoryElementGroupSwitch.CHOICE, constantGroupEle.getName());

					parmBranchInfos.add(parmBranchInfo);
				}

				//output
				HAPStoryAliasElement serviceOutputNodeName = dataLayerChangeRequest.addNewChange(new HAPStoryNodeServiceOutput()).getAlias();
				dataLayerChangeRequest.addNewChange(HAPStoryUtilityConnection.newConnectionContain(serviceStoryNode.getElementId(), serviceOutputNodeName, HAPConstantShared.SERVICE_CHILD_RESULT, null));
				HAPInteractiveResultTask successResult = serviceInterface.getResult("success");
				HAPDisplayResourceNode outputDisplayResource = interfaceDisplayResource.getResourceNode(HAPInteractive.RESULT);
				for(HAPResultElementInInteractiveTask output : successResult.getOutput()) {
					
					String parmName = output.getName();
					HAPOutputBranchInfo parmBranchInfo = new HAPOutputBranchInfo();
					parmBranchInfo.outputDef = HAPVariableDefinition.buildVariableInfo(parmName, output.getCriteria());
					parmBranchInfo.displayResource = outputDisplayResource.getResourceNode("success").getResourceNode("output").getResourceNode(parmName);

					//parm and connection to input
					HAPStoryAliasElement parmNodeName = dataLayerChangeRequest.addNewChange(new HAPStoryNodeServiceOutputItem(parmBranchInfo.outputDef)).getAlias();
					dataLayerChangeRequest.addPatchChange(parmNodeName, HAPStoryElement.DISPLAYRESOURCE, outputDisplayResource.getResourceNode(parmName));
					dataLayerChangeRequest.addNewChange(HAPStoryUtilityConnection.newConnectionContain(serviceOutputNodeName, parmNodeName, parmName, null));

					//variable path and group
					parmBranchInfo.variableAlias = dataLayerChangeRequest.addNewChange(new HAPStoryNodeVariable(parmBranchInfo.outputDef, null)).getAlias();
					dataLayerChangeRequest.addPatchChange(parmBranchInfo.variableAlias, HAPStoryElement.DISPLAYRESOURCE, outputDisplayResource.getResourceNode(parmName));
					HAPStoryAliasElement variableConnectionNodeName = dataLayerChangeRequest.addNewChange(HAPStoryUtilityConnection.newConnectionOnewayDataIO(parmNodeName, parmBranchInfo.variableAlias, null, null)).getAlias();
					HAPStoryUtilityVariable.addVariableToNode(moduleStoryNode.getElementId(), parmBranchInfo.variableAlias, null, dataLayerChangeRequest);

					//group
					parmBranchInfo.varGroupAlias = dataLayerChangeRequest.addNewChange(new HAPStoryElementGroupBatch()).getAlias();
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.varGroupAlias, new HAPStoryInfoElement(parmBranchInfo.variableAlias));
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.varGroupAlias, new HAPStoryInfoElement(variableConnectionNodeName));

					//switch group
					parmBranchInfo.switchAlias = dataLayerChangeRequest.addNewChange(new HAPStoryElementGroupSwitch()).getAlias();
					//add 
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.switchAlias, new HAPStoryInfoElement(parmBranchInfo.varGroupAlias));

					//set switch group choice
					dataLayerChangeRequest.addPatchChange(parmBranchInfo.switchAlias, HAPStoryElementGroupSwitch.CHOICE, true);

					outputBranchInfos.add(parmBranchInfo);
				}

				dataLayerChangeRequest.close();
			}

			{
				//build ui
				HAPStoryRequestChangeWrapper uiLayerChangeRequest = new HAPStoryRequestChangeWrapper(story, true, true);

				//page node and tree
				uiLayerChangeRequest.addNewChange(HAPStoryUtility.buildPageStoryNode(story));
				HAPStoryUIPage uiPage = HAPStoryUtility.buildUITree(story, this.m_runtimeEnv, this.m_uiTagManager, this.m_changeManager);
				HAPStoryUtilityStory.addNodeAsChild(ELEMENT_MODULE, uiPage.getStoryNodeRef(), HAPStoryNodeUIPage.STORYNODE_TYPE, uiLayerChangeRequest, true);

				//page layout node
				HAPStoryUINode pageLayoutUINode = uiPage.newChildNode(new HAPStoryNodeUIHtml(HAPUtilityFile.readFile(HAPStoryBuilderPageSimple.class, "page_html.tmp")), ALIAS_LAYOUTNODE, null, uiLayerChangeRequest, this.m_runtimeEnv, m_uiTagManager);
			
				//input parm ui
				for(HAPParmBranchInfo parmBranchInfo : parmBranchInfos) {
					
					//build ui for variable
					HAPStoryNodeVariable varNode = (HAPStoryNodeVariable)story.getElement(parmBranchInfo.variableAlias);
					parmBranchInfo.dataUIInfo = buildDataUINode(story, pageLayoutUINode, "input", varNode.getVariableInfo().getName(), new HAPDisplayValueInfo("displayName", parmBranchInfo.displayResource, parmBranchInfo.parmDef.getDisplayName()), HAPConstantShared.DATAFLOW_OUT, uiLayerChangeRequest);
					
					//variable group
					uiLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.varGroupAlias, new HAPStoryInfoElement(parmBranchInfo.dataUIInfo.rootEleRef));					
				}

				//output ui
				for(HAPOutputBranchInfo parmBranchInfo : outputBranchInfos) {
					//ui
					HAPStoryNodeVariable varNode = (HAPStoryNodeVariable)story.getElement(parmBranchInfo.variableAlias);
					parmBranchInfo.dataUIInfo = buildDataUINode(story, pageLayoutUINode, "output", varNode.getVariableInfo().getName(), new HAPDisplayValueInfo("displayName", parmBranchInfo.displayResource, parmBranchInfo.outputDef.getDisplayName()), HAPConstantShared.DATAFLOW_IN, uiLayerChangeRequest);
					
					//variable group
					uiLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.varGroupAlias, new HAPStoryInfoElement(parmBranchInfo.dataUIInfo.rootEleRef));					
				}
				uiLayerChangeRequest.close();
			}
			
			{
				//question
				HAPStoryQuestionGroup rootQuestionGroup = new HAPStoryQuestionGroup("Please select ui.");
				HAPStoryQuestionGroup parmsQuestionGroup = new HAPStoryQuestionGroup("Application Input:");
				rootQuestionGroup.addChild(parmsQuestionGroup);
				
				for(HAPParmBranchInfo parmBranchInfo : parmBranchInfos) {
					HAPStoryQuestionGroup parmQuestionGroup = new HAPStoryQuestionGroup(parmBranchInfo.dataUIInfo.displayLabel);
					parmsQuestionGroup.addChild(parmQuestionGroup);
					
					//question item
					HAPStoryQuestionItem groupQuestion = new HAPStoryQuestionItem("Please choose", parmBranchInfo.switchAlias);
					parmQuestionGroup.addChild(groupQuestion);
					
					HAPStoryNodeConstant constantStoryNode = (HAPStoryNodeConstant)story.getElement(parmBranchInfo.constantAlias);
					HAPStoryQuestionItem constantQuestion = new HAPStoryQuestionItem("Please select value for " + parmBranchInfo.dataUIInfo.displayLabel, parmBranchInfo.constantAlias, constantStoryNode.isMandatory());
					parmQuestionGroup.addChild(constantQuestion);
					
					HAPStoryQuestionItem uiDataQuestion = new HAPStoryQuestionItem("Please select UI for " + parmBranchInfo.dataUIInfo.displayLabel, parmBranchInfo.dataUIInfo.dataUINode.getStoryNodeRef());
					parmQuestionGroup.addChild(uiDataQuestion);
				}
				
				HAPStoryQuestionGroup outputsQuestionGroup = new HAPStoryQuestionGroup("Application Output:");
				rootQuestionGroup.addChild(outputsQuestionGroup);
				for(HAPOutputBranchInfo outputBranchInfo : outputBranchInfos) {
					
					outputsQuestionGroup.addChild(createOutputQuestion(outputBranchInfo.dataUIInfo));
				}
				
				step.setQuestion(rootQuestionGroup);
			}
			
			HAPStoryResultTransaction transactionResult = story.commitTransaction();
			step.getChanges().addAll(transactionResult.getChanges());
			
			design.addStep(step);

			//stage
			HAPStoryUtilityDesign.setChangeStage(step, STAGE_UI);

			return HAPServiceData.createSuccessData(new HAPStoryResponseDesign(answer.getAnswers(), step));
		}
	}

	private HAPStoryQuestion createOutputQuestion(HAPDataUIInfo dataUIInfo) {
		HAPStoryQuestionGroup parmQuestionGroup = new HAPStoryQuestionGroup(dataUIInfo.displayLabel);
		
		//question item
		HAPStoryQuestionItem groupQuestion = new HAPStoryQuestionItem("Display on UI", dataUIInfo.switchAlias);
		parmQuestionGroup.addChild(groupQuestion);
		
		if(dataUIInfo.dataUINode!=null) {
			HAPStoryQuestionItem uiDataQuestion = new HAPStoryQuestionItem("Please select UI for " + dataUIInfo.displayLabel, dataUIInfo.dataUINode.getStoryNodeRef());
			parmQuestionGroup.addChild(uiDataQuestion);
		}

		for(HAPDataUIInfo child : dataUIInfo.children) {
			HAPStoryQuestion childQuestion = createOutputQuestion(child);
			parmQuestionGroup.addChild(childQuestion);
		}
		return parmQuestionGroup;
	}
	
	private String wrappWithContainer(String html, String containerTag) {
		return "<" + containerTag + ">" + html + "</" + containerTag + ">"; 
	}
	
	private HAPDataUIInfo buildDataUINode(HAPStoryStory story, HAPStoryUINode parent, Object childId, String varName, HAPDisplayValueInfo lableInfo, String dataFlow, HAPStoryRequestChangeWrapper changeRequest) {
		HAPDataUIInfo out = new HAPDataUIInfo();
		
		//get label
		out.displayLabel = lableInfo.getDisplayValue();
		
		//group node
		HAPStoryAliasElement dataUIGroupAlias = changeRequest.addNewChange(new HAPStoryElementGroupBatch()).getAlias();
		
		String layoutTemplate = null;
		if(HAPUtilityBasic.isStringNotEmpty(out.displayLabel)) {
			layoutTemplate = "uiDataWithTitle.tmp";
		} else {
			layoutTemplate = "uiDataWithoutTitle.tmp";
		}
		
		HAPStoryUINode layoutUINode = parent.newChildNode(new HAPStoryNodeUIHtml(HAPUtilityFile.readFile(HAPStoryBuilderPageSimple.class, layoutTemplate)), null, childId, changeRequest, this.m_runtimeEnv, m_uiTagManager);
		changeRequest.addPatchChangeGroupAppendElement(dataUIGroupAlias, new HAPStoryInfoElement(layoutUINode.getStoryNodeRef()));
		if(HAPUtilityBasic.isStringNotEmpty(out.displayLabel)) {
			out.labelUINode = layoutUINode.newChildNode(new HAPStoryNodeUIHtml(this.wrappWithContainer(out.displayLabel, "span")), null, "label", changeRequest, this.m_runtimeEnv, m_uiTagManager);
			changeRequest.addPatchChange(out.labelUINode.getStoryNodeRef(), HAPStoryElement.DISPLAYRESOURCE, lableInfo.getDisplayResource());
			changeRequest.addPatchChangeGroupAppendElement(dataUIGroupAlias, new HAPStoryInfoElement(out.labelUINode.getStoryNodeRef()));
		}

		//data tag
		HAPStoryUINode dataUINode = null;
		HAPStoryUIDataInfo uiDataInfo = layoutUINode.getDataInfo(varName);
		HAPVariableDataInfo dataTypeInfo = uiDataInfo.getDataType();
		HAPDataTypeCriteria dataTypeCriteria = HAPParserCriteria.getInstance().parseCriteria("test.string;1.0.0");     //dataTypeInfo.getCriteria();  kkkkkk
		Set<HAPDataTypeId> dataTypeIds = dataTypeCriteria.getValidDataTypeId(this.m_runtimeEnv.getDataTypeHelper());
		HAPDataTypeId dataTypeId = dataTypeIds.iterator().next();
		HAPDataType dataType = this.m_runtimeEnv.getDataTypeManager().getDataType(dataTypeId);
		boolean isComplex = dataType.getIsComplex();
		if(isComplex) {
			if(dataTypeId.getFullName().contains("array")){
				//array
//				HAPStoryNodeUITagData uiDataStoryNode = new HAPStoryNodeUITagData("loop", story.getNextId(), uiDataInfo, dataFlow);
				
				HAPUITagInfo uiTagInfo = this.m_uiTagManager.getDefaultUITagData(new HAPUITageQueryData(dataTypeCriteria));
				HAPStoryNodeUITagData uiDataStoryNode = new HAPStoryNodeUITagData(uiTagInfo.getId(), story.getNextId(), uiDataInfo, dataFlow, uiTagInfo.getMatchers());

				uiDataStoryNode.addAttribute("data", varName);
				if(!HAPConstantShared.DATAFLOW_IN.equals(dataFlow)) {
					uiDataStoryNode.addAttribute(HAPConstantShared.UIRESOURCE_ATTRIBUTE_GROUP, HAPConstantShared.UIRESOURCE_ATTRIBUTE_GROUP_DATAVALIDATION);
				}
				dataUINode = layoutUINode.newChildNode(uiDataStoryNode, null, "uiData", changeRequest, this.m_runtimeEnv, m_uiTagManager);
				changeRequest.addPatchChangeGroupAppendElement(dataUIGroupAlias, new HAPStoryInfoElement(dataUINode.getStoryNodeRef()));
				
				HAPDataUIInfo dataUIInfo = buildDataUINode(story, dataUINode, null, "element", createChildDisplayLabelInfo("element", lableInfo.getDisplayResource()), dataFlow, changeRequest);
				if(dataUIInfo!=null) {
					out.children.add(dataUIInfo);
					changeRequest.addPatchChangeGroupAppendElement(dataUIGroupAlias, new HAPStoryInfoElement(dataUIInfo.rootEleRef));
				}

				out.dataUINode = dataUINode;
			}
			else if(dataTypeId.getFullName().contains("map")){
				//map
				List<String> names = HAPUtilityCriteria.getCriteriaChildrenNames(dataTypeCriteria);
				for(String name : names) {
					HAPDataUIInfo dataUIInfo = buildDataUINode(story, layoutUINode, "uiData", varName+"."+name, createChildDisplayLabelInfo(name, lableInfo.getDisplayResource()), dataFlow, changeRequest);
					if(dataUIInfo!=null) {
						out.children.add(dataUIInfo);
						changeRequest.addPatchChangeGroupAppendElement(dataUIGroupAlias, new HAPStoryInfoElement(dataUIInfo.rootEleRef));
					}
				}
			}
		}
		else {
			//simple
			HAPUITagInfo uiTagInfo = this.m_uiTagManager.getDefaultUITagData(new HAPUITageQueryData(dataTypeCriteria));
			if(uiTagInfo!=null) {
				HAPStoryNodeUITagData uiDataStoryNode = new HAPStoryNodeUITagData(uiTagInfo.getId(), story.getNextId(), uiDataInfo, dataFlow, uiTagInfo.getMatchers());
				uiDataStoryNode.addAttribute("data", varName);
				if(!HAPConstantShared.DATAFLOW_IN.equals(dataFlow)) {
					uiDataStoryNode.addAttribute(HAPConstantShared.UIRESOURCE_ATTRIBUTE_GROUP, HAPConstantShared.UIRESOURCE_ATTRIBUTE_GROUP_DATAVALIDATION);
				}
				dataUINode = layoutUINode.newChildNode(uiDataStoryNode, null, "uiData", changeRequest, this.m_runtimeEnv, m_uiTagManager);
				changeRequest.addPatchChangeGroupAppendElement(dataUIGroupAlias, new HAPStoryInfoElement(dataUINode.getStoryNodeRef()));

				//validation
				HAPStoryNodeUITagOther uiErrorStoryNode = new HAPStoryNodeUITagOther("uierror", story.getNextId());
				uiErrorStoryNode.addAttribute("target", uiDataStoryNode.getId());
				uiErrorStoryNode.addAttribute("data", HAPConstantShared.UIRESOURCE_CONTEXTELEMENT_NAME_UIVALIDATIONERROR);
				HAPStoryUINode uiErrorNode = layoutUINode.newChildNode(uiErrorStoryNode, null, "uiError", changeRequest, this.m_runtimeEnv, m_uiTagManager);
				changeRequest.addPatchChangeGroupAppendElement(dataUIGroupAlias, new HAPStoryInfoElement(uiErrorNode.getStoryNodeRef()));

				out.dataUINode = dataUINode;
			} else {
				return null;
			}
		}
		out.rootEleRef = dataUIGroupAlias;
		
		out.switchAlias = changeRequest.addNewChange(new HAPStoryElementGroupSwitch()).getAlias();
		changeRequest.addPatchChangeGroupAppendElement(out.switchAlias, new HAPStoryInfoElement(out.rootEleRef));					
		changeRequest.addPatchChange(out.switchAlias, HAPStoryElementGroupSwitch.CHOICE, true);
		
		return out;
	}
	
	private HAPDisplayValueInfo createChildDisplayLabelInfo(String childName, HAPDisplayResourceNode parentDisplayResourceNode) {
		return new HAPDisplayValueInfo("displayName", parentDisplayResourceNode.getResourceNode("children."+childName), childName);
	}

	
	private HAPServiceData processUIChangeStage(HAPStoryDesignStory design, HAPStoryRequestDesign answerRequest) {
		HAPStoryStory story = design.getStory();

		HAPServiceData validateResult = validateUIAnswer(design, answerRequest);
		if(validateResult.isFail()) {
			return validateResult;
		} else {
			story.startTransaction();
			
			//new step
			HAPStoryDesignStep step = design.newStep();

			HAPStoryRequestChangeWrapper changeRequest = new HAPStoryRequestChangeWrapper(story, true, true);

			HAPStoryUIPage uiTree = HAPStoryUtility.buildUITree(story, this.m_runtimeEnv, this.m_uiTagManager, this.m_changeManager);
			
			//add submit button
			boolean hasOutUI = false;
			List<HAPStoryUINode> allUINodes = uiTree.getAllUINodes();
			for(HAPStoryUINode uiNode : allUINodes) {
				HAPStoryNodeUI uiStoryNode = uiNode.getStoryNode();
				if(HAPConstantShared.STORYNODE_TYPE_UITAGDATA.equals(uiStoryNode.getType())) {
					HAPStoryNodeUITagData uiDataStoryNode = (HAPStoryNodeUITagData)uiStoryNode;
					if(uiDataStoryNode.isEnable()) {
						if(HAPConstantShared.DATAFLOW_OUT.equals(uiDataStoryNode.getAttributeValue(HAPStoryNodeUITagData.ATTRIBUTE_DATAFLOW))) {
							hasOutUI = true;
						}
					}
				}
			}
			if(hasOutUI==true) {
				//submit
				changeRequest.addNewChange(new HAPStoryNodeScript(HAPUtilityFile.readFile(this.getClass(), "submit.script")));
				HAPStoryUINode pageLayoutUINode = uiTree.getUINodeByStoryElementId(story.getElement(ALIAS_LAYOUTNODE).getElementId().getId());
				pageLayoutUINode.newChildNode(new HAPStoryNodeUIHtml(HAPUtilityFile.readFile(HAPStoryBuilderPageSimple.class, "submit.tmp")), null, "submit", changeRequest, this.m_runtimeEnv, m_uiTagManager);
			}
			else {
				//without submit, throught page init
				changeRequest.addNewChange(new HAPStoryNodeScript(HAPUtilityFile.readFile(this.getClass(), "pageinit.script")));
			}
			
			changeRequest.close();
			
			//question
			HAPStoryQuestionGroup rootQuestionGroup = new HAPStoryQuestionGroup("Finish.");
			step.setQuestion(rootQuestionGroup);

			HAPStoryResultTransaction transactionResult = story.commitTransaction();
			step.getChanges().addAll(transactionResult.getChanges());

			design.addStep(step);

			//stage
			HAPStoryUtilityDesign.setChangeStage(step, HAPConstantShared.DESIGNSTAGE_NAME_END);

			return HAPServiceData.createSuccessData(new HAPStoryResponseDesign(answerRequest.getAnswers(), step));
		}
	}

	private HAPServiceData validateUIAnswer(HAPStoryDesignStory design, HAPStoryRequestDesign answerRequest) {
		HAPStoryStory story = design.getStory();

		story.startTransaction();
		
		this.applyAnswer(story, answerRequest);
		
		List<String> errorMsgs = new ArrayList<String>();
		Set<HAPStoryNode> constantStoryNodes = HAPStoryUtilityStory.getStoryNodeByType(story, HAPConstantShared.STORYNODE_TYPE_CONSTANT);
		for(HAPStoryNode storyNode : constantStoryNodes) {
			HAPStoryNodeConstant constantStoryNode = (HAPStoryNodeConstant)storyNode;
			if(constantStoryNode.isMandatory() && constantStoryNode.getData()==null) {
				errorMsgs.add("Constant " + constantStoryNode.getName() + " should not be empty!!!!");
			}
		}
		
		if(errorMsgs.isEmpty()) {
			//valid
			//add answer to step
			HAPStoryDesignStep latestStep = design.getLatestStep();
			latestStep.addAnswers(answerRequest.getAnswers());
			story.commitTransaction();
			return HAPServiceData.createSuccessData();
		}
		else {
			//failure
			//revert answer changes
			story.rollbackTransaction();
			//
			return HAPServiceData.createFailureData(errorMsgs.toArray(new String[0]), "Validation Fail!!");
		}
	}
	
	private void applyAnswer(HAPStoryStory story, HAPStoryRequestDesign answerRequest) {
		{
			HAPStoryRequestChangeWrapper changeRequest = new HAPStoryRequestChangeWrapper(story, true, true);
			for(HAPStoryChangeItem change : answerRequest.getExtraChanges()) {   
				changeRequest.addChange(change);
			}
			List<HAPStoryChangeItem> allChanges = changeRequest.close();
		}

		for(HAPStoryAnswer answer : answerRequest.getAnswers()){
			HAPStoryRequestChangeWrapper changeRequest = new HAPStoryRequestChangeWrapper(story, true, true);
			changeRequest.addChanges(answer.getChanges());
			List<HAPStoryChangeItem> allChanges = changeRequest.close();
			answer.setChanges(allChanges);
		}
	}
	
	class HAPOutputBranchInfo{
		public HAPVariableDefinition outputDef;
		public HAPDisplayResourceNode displayResource;
		public HAPDataUIInfo dataUIInfo;
		public HAPStoryAliasElement variableAlias;
		public HAPStoryAliasElement varGroupAlias;
		public HAPStoryAliasElement switchAlias;
	}
	
	class HAPParmBranchInfo{
		public HAPRequestParmInInteractive parmDef;
		public HAPDisplayResourceNode displayResource;
		public HAPDataUIInfo dataUIInfo;
		public HAPStoryAliasElement variableAlias;
		public HAPStoryAliasElement constantAlias;
		public HAPStoryAliasElement varGroupAlias;
		public HAPStoryAliasElement constantGroupAlias;
		public HAPStoryAliasElement switchAlias;
	}
	
	class HAPDataUIInfo{
		public HAPStoryUINode dataUINode;
		public HAPStoryUINode labelUINode;
		public String displayLabel;
		public String displayDiscription;
		public HAPStoryAliasElement rootEleRef;
		public HAPStoryAliasElement switchAlias;
		public List<HAPDataUIInfo> children = new ArrayList<HAPDataUIInfo>();
	}
}

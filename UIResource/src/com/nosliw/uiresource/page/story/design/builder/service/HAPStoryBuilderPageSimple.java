package com.nosliw.uiresource.page.story.design.builder.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.displayresource.HAPDisplayResourceNode;
import com.nosliw.common.displayresource.HAPDisplayValueInfo;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.data.HAPDataType;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPDataTypeManager;
import com.nosliw.data.core.data.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPManagerExpression;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceOutput;
import com.nosliw.data.core.service.interfacee.HAPServiceParm;
import com.nosliw.data.core.service.interfacee.HAPServiceResult;
import com.nosliw.data.core.service.provide.HAPInfoServiceStatic;
import com.nosliw.data.core.service.provide.HAPManagerService;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.data.core.story.HAPAliasElement;
import com.nosliw.data.core.story.HAPInfoElement;
import com.nosliw.data.core.story.HAPManagerStory;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryElement;
import com.nosliw.data.core.story.HAPStoryNode;
import com.nosliw.data.core.story.HAPUtilityConnection;
import com.nosliw.data.core.story.HAPUtilityStory;
import com.nosliw.data.core.story.change.HAPChangeItem;
import com.nosliw.data.core.story.change.HAPChangeItemNew;
import com.nosliw.data.core.story.change.HAPRequestChangeWrapper;
import com.nosliw.data.core.story.change.HAPResultTransaction;
import com.nosliw.data.core.story.design.HAPAnswer;
import com.nosliw.data.core.story.design.HAPBuilderStory;
import com.nosliw.data.core.story.design.HAPDesignStep;
import com.nosliw.data.core.story.design.HAPDesignStory;
import com.nosliw.data.core.story.design.HAPQuestion;
import com.nosliw.data.core.story.design.HAPQuestionGroup;
import com.nosliw.data.core.story.design.HAPQuestionItem;
import com.nosliw.data.core.story.design.HAPRequestDesign;
import com.nosliw.data.core.story.design.HAPResponseDesign;
import com.nosliw.data.core.story.design.HAPStageInfo;
import com.nosliw.data.core.story.design.HAPUtilityDesign;
import com.nosliw.data.core.story.element.connectiongroup.HAPElementGroupBatch;
import com.nosliw.data.core.story.element.connectiongroup.HAPElementGroupSwitch;
import com.nosliw.data.core.story.element.node.HAPStoryNodeConstant;
import com.nosliw.data.core.story.element.node.HAPStoryNodeService;
import com.nosliw.data.core.story.element.node.HAPStoryNodeServiceInput;
import com.nosliw.data.core.story.element.node.HAPStoryNodeServiceInputParm;
import com.nosliw.data.core.story.element.node.HAPStoryNodeServiceOutput;
import com.nosliw.data.core.story.element.node.HAPStoryNodeServiceOutputItem;
import com.nosliw.data.core.story.element.node.HAPStoryNodeVariable;
import com.nosliw.uiresource.common.HAPUtilityCommon;
import com.nosliw.uiresource.page.story.element.HAPStoryNodeUIData;
import com.nosliw.uiresource.page.story.element.HAPStoryNodeUIHtml;
import com.nosliw.uiresource.page.story.model.HAPUIDataInfo;
import com.nosliw.uiresource.page.story.model.HAPUINode;
import com.nosliw.uiresource.page.story.model.HAPUITree;
import com.nosliw.uiresource.page.story.model.HAPUtility;
import com.nosliw.uiresource.page.tag.HAPUITagManager;
import com.nosliw.uiresource.page.tag.HAPUITagInfo;
import com.nosliw.uiresource.page.tag.HAPUITageQueryData;

public class HAPStoryBuilderPageSimple implements HAPBuilderStory{

	public final static String BUILDERID = "pageSimple";
	
	public final static String STAGE_SERVICE = "Service";
	public final static String STAGE_UI = "UI";
	public final static String STAGE_END = "Finish";

	private final static HAPAliasElement ELEMENT_SERVICE = new HAPAliasElement("service", false);
	
	private HAPManagerService m_serviceManager;
	private HAPUITagManager m_uiTagManager;
	private HAPRequirementContextProcessor m_contextProcessRequirement;
	private HAPDataTypeManager m_dataTypeMan = null;
	private HAPManagerStory m_storyManager;
	
	private List<HAPStageInfo> m_stages;
	
	public HAPStoryBuilderPageSimple(
			HAPManagerStory storyManager,
			HAPManagerService serviceManager, 
			HAPUITagManager uiTagMan, 
			HAPManagerResourceDefinition resourceDefMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPRuntime runtime, 
			HAPManagerExpression expressionMan,
			HAPManagerServiceDefinition serviceDefinitionManager,
			HAPDataTypeManager dataTypeMan) {
		this.m_storyManager = storyManager;
		this.m_serviceManager = serviceManager;
		this.m_uiTagManager = uiTagMan;
		this.m_dataTypeMan = dataTypeMan;
		this.m_contextProcessRequirement = HAPUtilityCommon.getDefaultContextProcessorRequirement(resourceDefMan, dataTypeHelper, runtime, expressionMan, serviceDefinitionManager);
		this.m_stages = new ArrayList<HAPStageInfo>();
		this.m_stages.add(new HAPStageInfo(STAGE_SERVICE, STAGE_SERVICE));
		this.m_stages.add(new HAPStageInfo(STAGE_UI, STAGE_UI));
		this.m_stages.add(new HAPStageInfo(STAGE_END, STAGE_END));
	}
	
	@Override
	public void initDesign(HAPDesignStory design) {
		HAPStory story = design.getStory();

		story.setShowType(HAPConstant.RUNTIME_RESOURCE_TYPE_UIRESOURCE);
		HAPUtilityDesign.setDesignAllStages(design, m_stages);
		
		story.startTransaction();
		HAPRequestChangeWrapper changeRequest = new HAPRequestChangeWrapper(story);

		changeRequest.addChange(new HAPChangeItemNew(new HAPStoryNodeService(), ELEMENT_SERVICE));
		changeRequest.close();
		HAPResultTransaction transactionResult = story.commitTransaction();
		
		HAPDesignStep step = design.newStep();
		step.addChanges(transactionResult.getChanges());
		
		//extra info
		HAPQuestionGroup rootQuestionGroup = new HAPQuestionGroup();
		HAPQuestionItem serviceItemExtraInfo = new HAPQuestionItem("Please select data source", ELEMENT_SERVICE, true);
		rootQuestionGroup.addChild(serviceItemExtraInfo);
		step.setQuestion(rootQuestionGroup);
		
		//stage
		HAPUtilityDesign.setChangeStage(step, STAGE_SERVICE);

		design.addStep(step);
	}

	@Override
	public HAPServiceData buildStory(HAPDesignStory storyDesign, HAPRequestDesign answer) {
		HAPServiceData out = null;
		String stage = HAPUtilityDesign.getDesignStage(storyDesign);
		if(stage.equals(STAGE_SERVICE)) {
			out = this.processServiceStage(storyDesign, answer);
		}
		else if(stage.equals(STAGE_UI)) {
			out = this.processUIChangeStage(storyDesign, answer);
		}
		else if(stage.equals(HAPConstant.DESIGNSTAGE_NAME_END)){
			String[] errorMsg = {"The wizard has finished"};
			out = HAPServiceData.createFailureData(errorMsg, "Invalid flow!!!");
		}
		else {
			String[] errorMsg = {"Unrecognize step"};
			out = HAPServiceData.createFailureData(errorMsg, "Invalid flow!!!");
		}
		
		return out;
	}
	
	private HAPServiceData validateServiceAnswer(HAPDesignStory design, HAPRequestDesign answerRequest) {
		HAPStory story = design.getStory();

		this.applyAnswer(story, answerRequest);
		
		HAPStoryNodeService serviceStoryNode = (HAPStoryNodeService)HAPUtilityStory.getAllStoryNodeByType(story, HAPConstant.STORYNODE_TYPE_SERVICE).iterator().next();
		if(!HAPBasicUtility.isStringEmpty(serviceStoryNode.getReferenceId())){
			//valid
			//add answer to step
			HAPDesignStep latestStep = design.getLatestStep();
			latestStep.addAnswers(answerRequest.getAnswers());
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
	
	private HAPServiceData processServiceStage(HAPDesignStory design, HAPRequestDesign answer) {
		HAPStory story = design.getStory();
		
		story.startTransaction();
		
		HAPServiceData validateResult = validateServiceAnswer(design, answer);
		if(validateResult.isFail())   return validateResult;
		else {
			//new step
			HAPDesignStep step = design.newStep();

			//service parm
			List<HAPParmBranchInfo> parmBranchInfos = new ArrayList<HAPParmBranchInfo>();
			List<HAPOutputBranchInfo> outputBranchInfos = new ArrayList<HAPOutputBranchInfo>();
			
			{
				//data related layer
				HAPRequestChangeWrapper dataLayerChangeRequest = new HAPRequestChangeWrapper(story, true, true);
	
				//get service node
				HAPStoryNodeService serviceStoryNode = (HAPStoryNodeService)HAPUtilityStory.getAllStoryNodeByType(story, HAPConstant.STORYNODE_TYPE_SERVICE).iterator().next();
				HAPInfoServiceStatic staticServiceInfo = this.m_serviceManager.getServiceDefinitionManager().getDefinition(serviceStoryNode.getReferenceId()).getStaticInfo();
				HAPServiceInterface serviceInterface = staticServiceInfo.getInterface();
				HAPDisplayResourceNode serviceDisplayResource = staticServiceInfo.getDisplayResource();
				HAPDisplayResourceNode interfaceDisplayResource = serviceDisplayResource.getResourceNode(HAPInfoServiceStatic.INTERFACE);
				
				//service input
				HAPAliasElement serviceInputNodeName = dataLayerChangeRequest.addNewChange(new HAPStoryNodeServiceInput()).getAlias();
				dataLayerChangeRequest.addNewChange(HAPUtilityConnection.newConnectionContain(serviceStoryNode.getElementId(), serviceInputNodeName, HAPConstant.SERVICE_CHILD_INPUT));
				HAPDisplayResourceNode inputDisplayResource = interfaceDisplayResource.getResourceNode(HAPServiceInterface.PARM);
				
				//parms
				for(String parmName : serviceInterface.getParmNames()) {
					HAPParmBranchInfo parmBranchInfo = new HAPParmBranchInfo();
					parmBranchInfo.parmDef = serviceInterface.getParm(parmName);
					parmBranchInfo.displayResource = inputDisplayResource.getResourceNode(parmName);

					//parm and connection to input
					HAPAliasElement parmNodeName = dataLayerChangeRequest.addNewChange(new HAPStoryNodeServiceInputParm(parmBranchInfo.parmDef)).getAlias();
					dataLayerChangeRequest.addPatchChange(parmNodeName, HAPStoryElement.DISPLAYRESOURCE, inputDisplayResource.getResourceNode(parmName));
					dataLayerChangeRequest.addNewChange(HAPUtilityConnection.newConnectionContain(serviceInputNodeName, parmNodeName, parmName));

					//constant path and group
					parmBranchInfo.constantAlias = dataLayerChangeRequest.addNewChange(new HAPStoryNodeConstant(parmBranchInfo.parmDef.getCriteria(), parmBranchInfo.parmDef.getDefaultValue()==null)).getAlias();
					dataLayerChangeRequest.addPatchChange(parmBranchInfo.constantAlias, HAPStoryElement.DISPLAYRESOURCE, inputDisplayResource.getResourceNode(parmName));
					HAPAliasElement constantConnectionNodeName = dataLayerChangeRequest.addNewChange(HAPUtilityConnection.newConnectionOnewayDataIO(parmBranchInfo.constantAlias, parmNodeName, null, null)).getAlias();

					parmBranchInfo.constantGroupAlias = dataLayerChangeRequest.addNewChange(new HAPElementGroupBatch()).getAlias();
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.constantGroupAlias, new HAPInfoElement(parmBranchInfo.constantAlias));
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.constantGroupAlias, new HAPInfoElement(constantConnectionNodeName));
					
					//variable path and group
					parmBranchInfo.variableAlias = dataLayerChangeRequest.addNewChange(new HAPStoryNodeVariable(parmBranchInfo.parmDef)).getAlias();
					dataLayerChangeRequest.addPatchChange(parmBranchInfo.variableAlias, HAPStoryElement.DISPLAYRESOURCE, inputDisplayResource.getResourceNode(parmName));
					HAPAliasElement variableConnectionNodeName = dataLayerChangeRequest.addNewChange(HAPUtilityConnection.newConnectionOnewayDataIO(parmBranchInfo.variableAlias, parmNodeName, null, null)).getAlias();

					parmBranchInfo.varGroupAlias = dataLayerChangeRequest.addNewChange(new HAPElementGroupBatch()).getAlias();
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.varGroupAlias, new HAPInfoElement(parmBranchInfo.variableAlias));
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.varGroupAlias, new HAPInfoElement(variableConnectionNodeName));

					//switch group
					parmBranchInfo.switchAlias = dataLayerChangeRequest.addNewChange(new HAPElementGroupSwitch()).getAlias();
					//add constant group to switch group
					HAPInfoElement constantGroupEle = new HAPInfoElement(parmBranchInfo.constantGroupAlias);
					constantGroupEle.setName("Constant");
					constantGroupEle.setDisplayName("I set value now");
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.switchAlias, constantGroupEle);

					//add variable group to switch group
					HAPInfoElement varGroupEle = new HAPInfoElement(parmBranchInfo.varGroupAlias);
					varGroupEle.setName("Variable");
					varGroupEle.setDisplayName("User input when they use the app");
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.switchAlias, varGroupEle);

					//set switch group choice
					dataLayerChangeRequest.addPatchChange(parmBranchInfo.switchAlias, HAPElementGroupSwitch.CHOICE, varGroupEle.getName());

					parmBranchInfos.add(parmBranchInfo);
				}

				//output
				HAPAliasElement serviceOutputNodeName = dataLayerChangeRequest.addNewChange(new HAPStoryNodeServiceOutput()).getAlias();
				dataLayerChangeRequest.addNewChange(HAPUtilityConnection.newConnectionContain(serviceStoryNode.getElementId(), serviceOutputNodeName, HAPConstant.SERVICE_CHILD_RESULT));
				HAPServiceResult successResult = serviceInterface.getResult("success");
				HAPDisplayResourceNode outputDisplayResource = interfaceDisplayResource.getResourceNode(HAPServiceInterface.RESULT);
				Map<String, HAPServiceOutput> output = successResult.getOutput(); 
				for(String parmName : output.keySet()) {
					HAPOutputBranchInfo parmBranchInfo = new HAPOutputBranchInfo();
					parmBranchInfo.outputDef = output.get(parmName);
					parmBranchInfo.displayResource = outputDisplayResource.getResourceNode("success").getResourceNode("output").getResourceNode(parmName);

					//parm and connection to input
					HAPAliasElement parmNodeName = dataLayerChangeRequest.addNewChange(new HAPStoryNodeServiceOutputItem(parmBranchInfo.outputDef)).getAlias();
					dataLayerChangeRequest.addPatchChange(parmNodeName, HAPStoryElement.DISPLAYRESOURCE, outputDisplayResource.getResourceNode(parmName));
					dataLayerChangeRequest.addNewChange(HAPUtilityConnection.newConnectionContain(serviceOutputNodeName, parmNodeName, parmName));

					//variable path and group
					parmBranchInfo.variableAlias = dataLayerChangeRequest.addNewChange(new HAPStoryNodeVariable(parmBranchInfo.outputDef)).getAlias();
					dataLayerChangeRequest.addPatchChange(parmBranchInfo.variableAlias, HAPStoryElement.DISPLAYRESOURCE, outputDisplayResource.getResourceNode(parmName));
					HAPAliasElement variableConnectionNodeName = dataLayerChangeRequest.addNewChange(HAPUtilityConnection.newConnectionOnewayDataIO(parmNodeName, parmBranchInfo.variableAlias, null, null)).getAlias();

					//group
					parmBranchInfo.varGroupAlias = dataLayerChangeRequest.addNewChange(new HAPElementGroupBatch()).getAlias();
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.varGroupAlias, new HAPInfoElement(parmBranchInfo.variableAlias));
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.varGroupAlias, new HAPInfoElement(variableConnectionNodeName));

					//switch group
					parmBranchInfo.switchAlias = dataLayerChangeRequest.addNewChange(new HAPElementGroupSwitch()).getAlias();
					//add 
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.switchAlias, new HAPInfoElement(parmBranchInfo.varGroupAlias));

					//set switch group choice
					dataLayerChangeRequest.addPatchChange(parmBranchInfo.switchAlias, HAPElementGroupSwitch.CHOICE, true);

					outputBranchInfos.add(parmBranchInfo);
				}

				dataLayerChangeRequest.close();
			}
			
			{
				//build ui
				HAPRequestChangeWrapper uiLayerChangeRequest = new HAPRequestChangeWrapper(story, true, true);

				//page node and tree
				uiLayerChangeRequest.addNewChange(HAPUtility.buildPageStoryNode(story));
				HAPUITree uiTree = HAPUtility.buildUITree(story, this.m_contextProcessRequirement, this.m_uiTagManager, this.m_storyManager.getChangeManager());

				//page layout node
				HAPUINode pageLayoutUINode = uiTree.newChildNode(new HAPStoryNodeUIHtml(HAPFileUtility.readFile(HAPStoryBuilderPageSimple.class, "page_html.tmp")), null, uiLayerChangeRequest, m_contextProcessRequirement, m_uiTagManager);
			
				//input parm ui
				for(HAPParmBranchInfo parmBranchInfo : parmBranchInfos) {
					//build ui for variable
					HAPStoryNodeVariable varNode = (HAPStoryNodeVariable)story.getElement(parmBranchInfo.variableAlias);
					parmBranchInfo.dataUIInfo = buildDataUINode(pageLayoutUINode, "input", varNode.getVariableInfo().getName(), new HAPDisplayValueInfo("displayName", parmBranchInfo.displayResource, parmBranchInfo.parmDef.getDisplayName()), HAPConstant.DATAFLOW_OUT, uiLayerChangeRequest);
					
					//variable group
					uiLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.varGroupAlias, new HAPInfoElement(parmBranchInfo.dataUIInfo.rootEleRef));					
				}

				HAPUINode submitUINode = pageLayoutUINode.newChildNode(new HAPStoryNodeUIHtml(HAPFileUtility.readFile(HAPStoryBuilderPageSimple.class, "submit.tmp")), "submit", uiLayerChangeRequest, m_contextProcessRequirement, m_uiTagManager);

				//output ui
				for(HAPOutputBranchInfo parmBranchInfo : outputBranchInfos) {
					//ui
					HAPStoryNodeVariable varNode = (HAPStoryNodeVariable)story.getElement(parmBranchInfo.variableAlias);
					parmBranchInfo.dataUIInfo = buildDataUINode(pageLayoutUINode, "output", varNode.getVariableInfo().getName(), new HAPDisplayValueInfo("displayName", parmBranchInfo.displayResource, parmBranchInfo.outputDef.getDisplayName()), HAPConstant.DATAFLOW_IN, uiLayerChangeRequest);
					
					//variable group
					uiLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.varGroupAlias, new HAPInfoElement(parmBranchInfo.dataUIInfo.rootEleRef));					
				}
				uiLayerChangeRequest.close();
			}
			
			{
				//question
				HAPQuestionGroup rootQuestionGroup = new HAPQuestionGroup("Please select ui.");
				HAPQuestionGroup parmsQuestionGroup = new HAPQuestionGroup("Application Input:");
				rootQuestionGroup.addChild(parmsQuestionGroup);
				
				for(HAPParmBranchInfo parmBranchInfo : parmBranchInfos) {
					HAPQuestionGroup parmQuestionGroup = new HAPQuestionGroup(parmBranchInfo.dataUIInfo.displayLabel);
					parmsQuestionGroup.addChild(parmQuestionGroup);
					
					//question item
					HAPQuestionItem groupQuestion = new HAPQuestionItem("Please choose", parmBranchInfo.switchAlias);
					parmQuestionGroup.addChild(groupQuestion);
					
					HAPStoryNodeConstant constantStoryNode = (HAPStoryNodeConstant)story.getElement(parmBranchInfo.constantAlias);
					HAPQuestionItem constantQuestion = new HAPQuestionItem("Please select value for " + parmBranchInfo.dataUIInfo.displayLabel, parmBranchInfo.constantAlias, constantStoryNode.isMandatory());
					parmQuestionGroup.addChild(constantQuestion);
					
					HAPQuestionItem uiDataQuestion = new HAPQuestionItem("Please select UI for " + parmBranchInfo.dataUIInfo.displayLabel, parmBranchInfo.dataUIInfo.dataUINode.getStoryNodeRef());
					parmQuestionGroup.addChild(uiDataQuestion);
				}
				
				HAPQuestionGroup outputsQuestionGroup = new HAPQuestionGroup("Application Output:");
				rootQuestionGroup.addChild(outputsQuestionGroup);
				for(HAPOutputBranchInfo outputBranchInfo : outputBranchInfos) {
					
					outputsQuestionGroup.addChild(createOutputQuestion(outputBranchInfo.dataUIInfo));
				}
				
				step.setQuestion(rootQuestionGroup);
			}
			
			HAPResultTransaction transactionResult = story.commitTransaction();
			step.getChanges().addAll(transactionResult.getChanges());
			
			design.addStep(step);

			//stage
			HAPUtilityDesign.setChangeStage(step, STAGE_UI);

			return HAPServiceData.createSuccessData(new HAPResponseDesign(answer.getAnswers(), step));
		}
	}

	private HAPQuestion createOutputQuestion(HAPDataUIInfo dataUIInfo) {
		HAPQuestionGroup parmQuestionGroup = new HAPQuestionGroup(dataUIInfo.displayLabel);
		
		//question item
		HAPQuestionItem groupQuestion = new HAPQuestionItem("Display on UI", dataUIInfo.switchAlias);
		parmQuestionGroup.addChild(groupQuestion);
		
		for(HAPDataUIInfo child : dataUIInfo.children) {
			HAPQuestion childQuestion = createOutputQuestion(child);
			parmQuestionGroup.addChild(childQuestion);
		}
		return parmQuestionGroup;
	}
	
	private String wrappWithContainer(String html, String containerTag) {
		return "<" + containerTag + ">" + html + "</" + containerTag + ">"; 
	}
	
	private HAPDataUIInfo buildDataUINode(HAPUINode parent, Object childId, String varName, HAPDisplayValueInfo lableInfo, String dataFlow, HAPRequestChangeWrapper changeRequest) {
		HAPDataUIInfo out = new HAPDataUIInfo();
		
		//get label
		out.displayLabel = lableInfo.getDisplayValue();
		
		//group node
		HAPAliasElement dataUIGroupAlias = changeRequest.addNewChange(new HAPElementGroupBatch()).getAlias();
		
		String layoutTemplate = null;
		if(HAPBasicUtility.isStringNotEmpty(out.displayLabel)) layoutTemplate = "uiDataWithTitle.tmp";
		else layoutTemplate = "uiDataWithoutTitle.tmp";
		
		HAPUINode layoutUINode = parent.newChildNode(new HAPStoryNodeUIHtml(HAPFileUtility.readFile(HAPStoryBuilderPageSimple.class, layoutTemplate)), childId, changeRequest, m_contextProcessRequirement, m_uiTagManager);
		changeRequest.addPatchChangeGroupAppendElement(dataUIGroupAlias, new HAPInfoElement(layoutUINode.getStoryNodeRef()));
		if(HAPBasicUtility.isStringNotEmpty(out.displayLabel)) {
			out.labelUINode = layoutUINode.newChildNode(new HAPStoryNodeUIHtml(this.wrappWithContainer(out.displayLabel, "span")), "label", changeRequest, m_contextProcessRequirement, m_uiTagManager);
			changeRequest.addPatchChange(out.labelUINode.getStoryNodeRef(), HAPStoryElement.DISPLAYRESOURCE, lableInfo.getDisplayResource());
			changeRequest.addPatchChangeGroupAppendElement(dataUIGroupAlias, new HAPInfoElement(out.labelUINode.getStoryNodeRef()));
		}

		//data tag
		HAPUINode dataUINode = null;
		HAPUIDataInfo uiDataInfo = layoutUINode.getDataInfo(varName);
		HAPDataTypeCriteria dataTypeCriteria = uiDataInfo.getDataTypeCriteria();
		HAPDataTypeHelper dataTypeHelper = null;
		Set<HAPDataTypeId> dataTypeIds = dataTypeCriteria.getValidDataTypeId(dataTypeHelper);
		HAPDataTypeId dataTypeId = dataTypeIds.iterator().next();
		HAPDataType dataType = m_dataTypeMan.getDataType(dataTypeId);
		boolean isComplex = dataType.getIsComplex();
		if(isComplex) {
			if(dataTypeId.getFullName().contains("array")){
				//array
				HAPStoryNodeUIData uiDataStoryNode = new HAPStoryNodeUIData("loop", uiDataInfo, dataFlow);
				uiDataStoryNode.addAttribute("data", varName);
				dataUINode = layoutUINode.newChildNode(uiDataStoryNode, "uiData", changeRequest, m_contextProcessRequirement, m_uiTagManager);
				changeRequest.addPatchChangeGroupAppendElement(dataUIGroupAlias, new HAPInfoElement(dataUINode.getStoryNodeRef()));
				
				HAPDataUIInfo dataUIInfo = buildDataUINode(dataUINode, null, "element", createChildDisplayLabelInfo("element", lableInfo.getDisplayResource()), dataFlow, changeRequest);
				out.children.add(dataUIInfo);
				changeRequest.addPatchChangeGroupAppendElement(dataUIGroupAlias, new HAPInfoElement(dataUIInfo.rootEleRef));
			}
			else if(dataTypeId.getFullName().contains("map")){
				//map
				List<String> names = HAPCriteriaUtility.getCriteriaChildrenNames(dataTypeCriteria);
				for(String name : names) {
					HAPDataUIInfo dataUIInfo = buildDataUINode(layoutUINode, "uiData", varName+"."+name, createChildDisplayLabelInfo(name, lableInfo.getDisplayResource()), dataFlow, changeRequest);
					out.children.add(dataUIInfo);
					changeRequest.addPatchChangeGroupAppendElement(dataUIGroupAlias, new HAPInfoElement(dataUIInfo.rootEleRef));
				}
			}
		}
		else {
			//simple
			HAPUITagInfo uiTagInfo = this.m_uiTagManager.getDefaultUITag(new HAPUITageQueryData(dataTypeCriteria));
			HAPStoryNodeUIData uiDataStoryNode = new HAPStoryNodeUIData(uiTagInfo.getTag(), uiDataInfo, dataFlow);
			uiDataStoryNode.addAttribute("data", varName);
			dataUINode = layoutUINode.newChildNode(uiDataStoryNode, "uiData", changeRequest, m_contextProcessRequirement, m_uiTagManager);
			changeRequest.addPatchChangeGroupAppendElement(dataUIGroupAlias, new HAPInfoElement(dataUINode.getStoryNodeRef()));
			out.dataUINode = dataUINode;
		}
		
		out.rootEleRef = dataUIGroupAlias;
		
		out.switchAlias = changeRequest.addNewChange(new HAPElementGroupSwitch()).getAlias();
		changeRequest.addPatchChangeGroupAppendElement(out.switchAlias, new HAPInfoElement(out.rootEleRef));					
		changeRequest.addPatchChange(out.switchAlias, HAPElementGroupSwitch.CHOICE, true);
		
		return out;
	}
	
	private HAPDisplayValueInfo createChildDisplayLabelInfo(String childName, HAPDisplayResourceNode parentDisplayResourceNode) {
		return new HAPDisplayValueInfo("displayName", parentDisplayResourceNode.getResourceNode("children."+childName), childName);
	}

	
	private HAPServiceData processUIChangeStage(HAPDesignStory design, HAPRequestDesign answerRequest) {
		HAPStory story = design.getStory();

		story.startTransaction();
		
		HAPServiceData validateResult = validateUIAnswer(design, answerRequest);
		if(validateResult.isFail())   return validateResult;
		else {
			this.applyAnswer(story, answerRequest);
			
			//new step
			HAPDesignStep step = design.newStep();

			//question
			HAPQuestionGroup rootQuestionGroup = new HAPQuestionGroup("Finish.");
			step.setQuestion(rootQuestionGroup);

			HAPResultTransaction transactionResult = story.commitTransaction();
			step.getChanges().addAll(transactionResult.getChanges());

			design.addStep(step);

			//stage
			HAPUtilityDesign.setChangeStage(step, HAPConstant.DESIGNSTAGE_NAME_END);

			return HAPServiceData.createSuccessData(new HAPResponseDesign(answerRequest.getAnswers(), step));
		}
	}

	private HAPServiceData validateUIAnswer(HAPDesignStory design, HAPRequestDesign answerRequest) {
		HAPStory story = design.getStory();

		this.applyAnswer(story, answerRequest);
		
		List<String> errorMsgs = new ArrayList<String>();
		Set<HAPStoryNode> constantStoryNodes = HAPUtilityStory.getStoryNodeByType(story, HAPConstant.STORYNODE_TYPE_CONSTANT);
		for(HAPStoryNode storyNode : constantStoryNodes) {
			HAPStoryNodeConstant constantStoryNode = (HAPStoryNodeConstant)storyNode;
			if(constantStoryNode.isMandatory() && constantStoryNode.getData()==null) {
				errorMsgs.add("Constant " + constantStoryNode.getName() + " should not be empty!!!!");
			}
		}
		
		if(errorMsgs.isEmpty()) {
			//valid
			//add answer to step
			HAPDesignStep latestStep = design.getLatestStep();
			latestStep.addAnswers(answerRequest.getAnswers());
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
	
	private void applyAnswer(HAPStory story, HAPRequestDesign answerRequest) {
		{
			HAPRequestChangeWrapper changeRequest = new HAPRequestChangeWrapper(story, true, true);
			for(HAPChangeItem change : answerRequest.getExtraChanges()) {   
				changeRequest.addChange(change);
			}
			List<HAPChangeItem> allChanges = changeRequest.close();
		}

		for(HAPAnswer answer : answerRequest.getAnswers()){
			HAPRequestChangeWrapper changeRequest = new HAPRequestChangeWrapper(story, true, true);
			changeRequest.addChanges(answer.getChanges());
			List<HAPChangeItem> allChanges = changeRequest.close();
			answer.setChanges(allChanges);
		}
	}
	
	class HAPOutputBranchInfo{
		public HAPServiceOutput outputDef;
		public HAPDisplayResourceNode displayResource;
		public HAPDataUIInfo dataUIInfo;
		public HAPAliasElement variableAlias;
		public HAPAliasElement varGroupAlias;
		public HAPAliasElement switchAlias;
	}
	
	class HAPParmBranchInfo{
		public HAPServiceParm parmDef;
		public HAPDisplayResourceNode displayResource;
		public HAPDataUIInfo dataUIInfo;
		public HAPAliasElement variableAlias;
		public HAPAliasElement constantAlias;
		public HAPAliasElement varGroupAlias;
		public HAPAliasElement constantGroupAlias;
		public HAPAliasElement switchAlias;
	}
	
	class HAPDataUIInfo{
		public HAPUINode dataUINode;
		public HAPUINode labelUINode;
		public String displayLabel;
		public String displayDiscription;
		public HAPAliasElement rootEleRef;
		public HAPAliasElement switchAlias;
		public List<HAPDataUIInfo> children = new ArrayList<HAPDataUIInfo>();
	}
}

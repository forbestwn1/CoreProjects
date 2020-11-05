package com.nosliw.uiresource.page.story.design;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.nosliw.data.core.data.variable.HAPVariableInfo;
import com.nosliw.data.core.expression.HAPManagerExpression;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceOutput;
import com.nosliw.data.core.service.interfacee.HAPServiceParm;
import com.nosliw.data.core.service.interfacee.HAPServiceResult;
import com.nosliw.data.core.service.provide.HAPManagerService;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.data.core.story.HAPAliasElement;
import com.nosliw.data.core.story.HAPInfoElement;
import com.nosliw.data.core.story.HAPReferenceElement;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPUtilityConnection;
import com.nosliw.data.core.story.HAPUtilityStory;
import com.nosliw.data.core.story.change.HAPChangeItem;
import com.nosliw.data.core.story.change.HAPChangeItemNew;
import com.nosliw.data.core.story.change.HAPRequestChange;
import com.nosliw.data.core.story.change.HAPRequestChangeWrapper;
import com.nosliw.data.core.story.change.HAPResultTransaction;
import com.nosliw.data.core.story.design.HAPAnswer;
import com.nosliw.data.core.story.design.HAPBuilderStory;
import com.nosliw.data.core.story.design.HAPDesignStep;
import com.nosliw.data.core.story.design.HAPDesignStory;
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
import com.nosliw.uiresource.page.tag.HAPUITagQueryResult;
import com.nosliw.uiresource.page.tag.HAPUITageQuery;

public class HAPStoryBuilderPageSimple implements HAPBuilderStory{

	public final static String BUILDERID = "pageSimple";
	
	public final static String STAGE_SERVICE = "service";
	public final static String STAGE_UI = "ui";
	public final static String STAGE_END = "end";

	private final static HAPAliasElement ELEMENT_SERVICE = new HAPAliasElement("service", false);
	
	private HAPManagerService m_serviceManager;
	private HAPUITagManager m_uiTagManager;
	private HAPRequirementContextProcessor m_contextProcessRequirement;
	private HAPDataTypeManager m_dataTypeMan = null;

	private List<HAPStageInfo> m_stages;
	
	public HAPStoryBuilderPageSimple(
			HAPManagerService serviceManager, 
			HAPUITagManager uiTagMan, 
			HAPManagerResourceDefinition resourceDefMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPRuntime runtime, 
			HAPManagerExpression expressionMan,
			HAPManagerServiceDefinition serviceDefinitionManager,
			HAPDataTypeManager dataTypeMan) {
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
		HAPRequestChange changeRequest = new HAPRequestChange();
		changeRequest.addChange(new HAPChangeItemNew(new HAPStoryNodeService(), ELEMENT_SERVICE));
		story.change(changeRequest);
		HAPResultTransaction transactionResult = story.commitTransaction();
		
		HAPDesignStep step = design.newStep();
		step.addChanges(transactionResult.getChanges());
		
		//extra info
		HAPQuestionGroup rootQuestionGroup = new HAPQuestionGroup("Please select service.");
		HAPQuestionItem serviceItemExtraInfo = new HAPQuestionItem("select service", ELEMENT_SERVICE);
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
		else if(stage.equals(STAGE_END)) {
			
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
				HAPServiceInterface serviceInterface = this.m_serviceManager.getServiceDefinitionManager().getDefinition(serviceStoryNode.getReferenceId()).getStaticInfo().getInterface();
				
				//service input
				HAPAliasElement serviceInputNodeName = dataLayerChangeRequest.addNewChange(new HAPStoryNodeServiceInput()).getAlias();
				dataLayerChangeRequest.addNewChange(HAPUtilityConnection.newConnectionContain(serviceStoryNode.getElementId(), serviceInputNodeName, HAPConstant.SERVICE_CHILD_INPUT));
				
				//parms
				for(String parmName : serviceInterface.getParmNames()) {
					HAPParmBranchInfo parmBranchInfo = new HAPParmBranchInfo();
					parmBranchInfo.parmName = parmName;

					//parm and connection to input
					HAPServiceParm parmDef = serviceInterface.getParm(parmName);
					
					HAPAliasElement parmNodeName = dataLayerChangeRequest.addNewChange(new HAPStoryNodeServiceInputParm(parmDef)).getAlias();
					dataLayerChangeRequest.addNewChange(HAPUtilityConnection.newConnectionContain(serviceInputNodeName, parmNodeName, parmName));

					parmBranchInfo.constantAlias = dataLayerChangeRequest.addNewChange(new HAPStoryNodeConstant(parmDef.getCriteria())).getAlias();
					
					HAPAliasElement constantConnectionNodeName = dataLayerChangeRequest.addNewChange(HAPUtilityConnection.newConnectionOnewayDataIO(parmBranchInfo.constantAlias, parmNodeName, null, null)).getAlias();

					//constant path and group
					parmBranchInfo.constantGroupAlias = dataLayerChangeRequest.addNewChange(new HAPElementGroupBatch()).getAlias();
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.constantGroupAlias, new HAPInfoElement(parmBranchInfo.constantAlias));
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.constantGroupAlias, new HAPInfoElement(constantConnectionNodeName));
					
					//variable path and group
					String variableName = parmName;
					parmBranchInfo.variableAlias = dataLayerChangeRequest.addNewChange(new HAPStoryNodeVariable(parmDef)).getAlias();
					HAPAliasElement variableConnectionNodeName = dataLayerChangeRequest.addNewChange(HAPUtilityConnection.newConnectionOnewayDataIO(parmBranchInfo.variableAlias, parmNodeName, null, null)).getAlias();

					parmBranchInfo.varGroupAlias = dataLayerChangeRequest.addNewChange(new HAPElementGroupBatch()).getAlias();
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.varGroupAlias, new HAPInfoElement(parmBranchInfo.variableAlias));
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.varGroupAlias, new HAPInfoElement(variableConnectionNodeName));

					//switch group
					parmBranchInfo.switchAlias = dataLayerChangeRequest.addNewChange(new HAPElementGroupSwitch()).getAlias();
					//add constant to group
					HAPInfoElement constantGroupEle = new HAPInfoElement(parmBranchInfo.constantGroupAlias);
					constantGroupEle.setName("Constant");
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.switchAlias, constantGroupEle);

					//add variable to group
					HAPInfoElement varGroupEle = new HAPInfoElement(parmBranchInfo.varGroupAlias);
					varGroupEle.setName("Variable");
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.switchAlias, varGroupEle);

					//set switch group choice
					dataLayerChangeRequest.addPatchChange(parmBranchInfo.switchAlias, HAPElementGroupSwitch.CHOICE, constantGroupEle.getName());

					parmBranchInfos.add(parmBranchInfo);
				}

				//output
				HAPAliasElement serviceOutputNodeName = dataLayerChangeRequest.addNewChange(new HAPStoryNodeServiceOutput()).getAlias();
				dataLayerChangeRequest.addNewChange(HAPUtilityConnection.newConnectionContain(serviceStoryNode.getElementId(), serviceOutputNodeName, HAPConstant.SERVICE_CHILD_RESULT));
				HAPServiceResult successResult = serviceInterface.getResult("success");
				Map<String, HAPServiceOutput> output = successResult.getOutput(); 
				for(String parmName : output.keySet()) {
					HAPOutputBranchInfo parmBranchInfo = new HAPOutputBranchInfo();
					parmBranchInfo.parmName = parmName;

					//parm and connection to input
					HAPServiceOutput parmDef = output.get(parmName);
					
					HAPAliasElement parmNodeName = dataLayerChangeRequest.addNewChange(new HAPStoryNodeServiceOutputItem(parmDef)).getAlias();
					dataLayerChangeRequest.addNewChange(HAPUtilityConnection.newConnectionContain(serviceOutputNodeName, parmNodeName, parmName));

					//variable path and group
					String variableName = parmName;
					parmBranchInfo.variableAlias = dataLayerChangeRequest.addNewChange(new HAPStoryNodeVariable(HAPVariableInfo.buildVariableInfo(variableName, parmDef.getCriteria()))).getAlias();
					HAPAliasElement variableConnectionNodeName = dataLayerChangeRequest.addNewChange(HAPUtilityConnection.newConnectionOnewayDataIO(parmNodeName, parmBranchInfo.variableAlias, null, null)).getAlias();

					//group
					parmBranchInfo.varGroupAlias = dataLayerChangeRequest.addNewChange(new HAPElementGroupBatch()).getAlias();
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.varGroupAlias, new HAPInfoElement(parmBranchInfo.variableAlias));
					dataLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.varGroupAlias, new HAPInfoElement(variableConnectionNodeName));

					outputBranchInfos.add(parmBranchInfo);
				}

				dataLayerChangeRequest.close();
			}
			
			{
				//build ui
				HAPRequestChangeWrapper uiLayerChangeRequest = new HAPRequestChangeWrapper(story, true, true);

				//page node and tree
				uiLayerChangeRequest.addNewChange(HAPUtility.buildPageStoryNode(story));
				HAPUITree uiTree = HAPUtility.buildUITree(story, this.m_contextProcessRequirement, this.m_uiTagManager);

				//page layout node
				HAPUINode pageLayoutUINode = uiTree.newChildNode(new HAPStoryNodeUIHtml(HAPFileUtility.readFile(HAPStoryBuilderPageSimple.class, "page_html.tmp")), null, uiLayerChangeRequest, m_contextProcessRequirement, m_uiTagManager);
			
				for(HAPParmBranchInfo parmBranchInfo : parmBranchInfos) {
					//ui
					HAPStoryNodeVariable varNode = (HAPStoryNodeVariable)story.getElement(parmBranchInfo.variableAlias);
					HAPUINode dataUINode = buildDataUINode(pageLayoutUINode, "input", varNode.getVariableInfo().getName(), parmBranchInfo.parmName, uiLayerChangeRequest);
					parmBranchInfo.dataUINode = dataUINode; 
					
					//variable group
					for(HAPReferenceElement eleRef : dataUINode.getAllStoryElements()) {
						uiLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.varGroupAlias, new HAPInfoElement(eleRef));
					}
				}

				for(HAPOutputBranchInfo parmBranchInfo : outputBranchInfos) {
					//ui
					HAPStoryNodeVariable varNode = (HAPStoryNodeVariable)story.getElement(parmBranchInfo.variableAlias);
					HAPUINode dataUINode = buildDataUINode(pageLayoutUINode, "output", varNode.getVariableInfo().getName(), parmBranchInfo.parmName, uiLayerChangeRequest);
					parmBranchInfo.dataUINode = dataUINode; 
					
					//variable group
					for(HAPReferenceElement eleRef : dataUINode.getAllStoryElements()) {
						uiLayerChangeRequest.addPatchChangeGroupAppendElement(parmBranchInfo.varGroupAlias, new HAPInfoElement(eleRef));
					}
				}
				uiLayerChangeRequest.close();
			}
			
			{
				//question
				HAPQuestionGroup rootQuestionGroup = new HAPQuestionGroup("Please select ui.");
				
				for(HAPParmBranchInfo parmBranchInfo : parmBranchInfos) {
					HAPQuestionGroup parmQuestionGroup = new HAPQuestionGroup("Configure Input Parm " + parmBranchInfo.parmName);
					rootQuestionGroup.addChild(parmQuestionGroup);
					
					//question item
					HAPQuestionItem groupQuestion = new HAPQuestionItem("select import for parm", parmBranchInfo.switchAlias);
					parmQuestionGroup.addChild(groupQuestion);
					
					HAPQuestionItem constantQuestion = new HAPQuestionItem("select constant", parmBranchInfo.constantAlias);
					parmQuestionGroup.addChild(constantQuestion);
					
					HAPQuestionItem uiDataQuestion = new HAPQuestionItem("select ui tag", parmBranchInfo.dataUINode.getStoryNodeRef());
					parmQuestionGroup.addChild(uiDataQuestion);
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

	
	private HAPUINode buildDataUINode(HAPUINode parent, Object childId, String varName, String label, HAPRequestChangeWrapper changeRequest) {
		
		HAPUINode layoutUINode = parent.newChildNode(new HAPStoryNodeUIHtml(HAPFileUtility.readFile(HAPStoryBuilderPageSimple.class, "uiData.tmp")), childId, changeRequest, m_contextProcessRequirement, m_uiTagManager);

		HAPUINode labelUINode = layoutUINode.newChildNode(new HAPStoryNodeUIHtml(label), "label", changeRequest, m_contextProcessRequirement, m_uiTagManager);
		
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
				HAPStoryNodeUIData uiDataStoryNode = new HAPStoryNodeUIData("loop", uiDataInfo);
				uiDataStoryNode.addAttribute("data", varName);
				dataUINode = layoutUINode.newChildNode(uiDataStoryNode, "uiData", changeRequest, m_contextProcessRequirement, m_uiTagManager);
				HAPUINode elementUINode = buildDataUINode(dataUINode, null, "element", null, changeRequest);
			}
			else if(dataTypeId.getFullName().contains("map")) {
				//map
				List<String> names = HAPCriteriaUtility.getCriteriaChildrenNames(dataTypeCriteria);
				for(String name : names) {
					buildDataUINode(parent, name, varName+"."+name, name, changeRequest);
				}
			}
		}
		else {
			//simple
			HAPUITagQueryResult uiTagInfo = this.m_uiTagManager.getDefaultUITag(new HAPUITageQuery(dataTypeCriteria));
			HAPStoryNodeUIData uiDataStoryNode = new HAPStoryNodeUIData(uiTagInfo.getTag(), uiDataInfo);
			uiDataStoryNode.addAttribute("data", varName);
			dataUINode = layoutUINode.newChildNode(uiDataStoryNode, "uiData", changeRequest, m_contextProcessRequirement, m_uiTagManager);
		}
		return layoutUINode;
	}
	
	private HAPServiceData processUIChangeStage(HAPDesignStory design, HAPRequestDesign answerRequest) {
		HAPStory story = design.getStory();

		story.startTransaction();

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
		HAPUtilityDesign.setChangeStage(step, STAGE_END);

		return HAPServiceData.createSuccessData(new HAPResponseDesign(answerRequest.getAnswers(), step));
	}

	private void applyAnswer(HAPStory story, HAPRequestDesign answerRequest) {
		HAPRequestChangeWrapper changeRequest = new HAPRequestChangeWrapper(story, true, false);
		for(HAPAnswer answer : answerRequest.getAnswers()){		changeRequest.addChanges(answer.getChanges());	}
		for(HAPChangeItem change : answerRequest.getExtraChanges()) {   changeRequest.addChange(change);    }
		changeRequest.close();
	}
	
	class HAPOutputBranchInfo{
		public String parmName;
		public HAPAliasElement variableAlias;
		public HAPAliasElement varGroupAlias;
		public HAPUINode dataUINode; 
	}
	
	class HAPParmBranchInfo{
		public String parmName;
		public HAPAliasElement variableAlias;
		public HAPAliasElement constantAlias;
		public HAPAliasElement varGroupAlias;
		public HAPAliasElement constantGroupAlias;
		public HAPAliasElement switchAlias;
		public HAPUINode dataUINode; 
	}
}

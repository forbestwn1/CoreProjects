package com.nosliw.core.application.division.manual;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPPluginDivision;
import com.nosliw.core.application.HAPWrapperBrickRoot;
import com.nosliw.core.application.common.dataexpressionimp.HAPPluginProcessorEntityWithVariableDataExpression;
import com.nosliw.core.application.common.scriptexpression.HAPPluginProcessorEntityWithVariableScriptExpression;
import com.nosliw.core.application.common.withvariable.HAPManagerWithVariablePlugin;
import com.nosliw.core.application.common.withvariable.HAPPluginProcessorEntityWithVariable;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociation.HAPManaualPluginAdapterProcessorDataAssociation;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociation.HAPManualPluginParserAdapterDataAssociation;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociationforexpression.HAPManaualPluginAdapterProcessorDataAssociationForExpression;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociationforexpression.HAPManualPluginParserAdapterDataAssociationForExpression;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask.HAPManaualPluginAdapterProcessorDataAssociationForTask;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask.HAPManualPluginParserAdapterDataAssociationForTask;
import com.nosliw.core.application.division.manual.brick.container.HAPManualBrickContainer;
import com.nosliw.core.application.division.manual.brick.container.HAPManualDefinitionBrickContainer;
import com.nosliw.core.application.division.manual.brick.container.HAPManualDefinitionBrickContainerList;
import com.nosliw.core.application.division.manual.brick.container.HAPManualPluginProcessorBlockContainerList;
import com.nosliw.core.application.division.manual.brick.data.HAPManualPluginParserBlockData;
import com.nosliw.core.application.division.manual.brick.dataexpression.group.HAPManualPluginParserBlockDataExpressionGroup;
import com.nosliw.core.application.division.manual.brick.dataexpression.group.HAPManualPluginProcessorBlockDataExpressionGroup;
import com.nosliw.core.application.division.manual.brick.dataexpression.lib.HAPManualPluginParserBlockDataExpressionElementInLibrary;
import com.nosliw.core.application.division.manual.brick.dataexpression.lib.HAPManualPluginParserBlockDataExpressionLibrary;
import com.nosliw.core.application.division.manual.brick.dataexpression.lib.HAPManualPluginProcessorBlockDataExpressionElementInLibrary;
import com.nosliw.core.application.division.manual.brick.dataexpression.lib.HAPManualPluginProcessorBlockDataExpressionLibrary;
import com.nosliw.core.application.division.manual.brick.interactive.interfacee.expression.HAPManualPluginParserBlockSimpleInteractiveInterfaceExpression;
import com.nosliw.core.application.division.manual.brick.interactive.interfacee.expression.HAPManualPluginProcessorBlockSimpleInteractiveInterfaceExpression;
import com.nosliw.core.application.division.manual.brick.interactive.interfacee.task.HAPManualPluginParserBlockSimpleInteractiveInterfaceTask;
import com.nosliw.core.application.division.manual.brick.interactive.interfacee.task.HAPManualPluginProcessorBlockSimpleInteractiveInterfaceTask;
import com.nosliw.core.application.division.manual.brick.module.HAPManualPluginParserBlockModule;
import com.nosliw.core.application.division.manual.brick.module.HAPManualPluginProcessorBlockModule;
import com.nosliw.core.application.division.manual.brick.scriptexpression.group.HAPManualPluginParserBlockScriptExpressionGroup;
import com.nosliw.core.application.division.manual.brick.scriptexpression.group.HAPManualPluginProcessorBlockScriptExpressionGroup;
import com.nosliw.core.application.division.manual.brick.service.provider.HAPManualPluginParserBlockServiceProvider;
import com.nosliw.core.application.division.manual.brick.service.provider.HAPManualPluginProcessorBlockSimpleImpServiceProvider;
import com.nosliw.core.application.division.manual.brick.task.flow.HAPManualPluginParserBlockTaskFlowActivityTask;
import com.nosliw.core.application.division.manual.brick.task.flow.HAPManualPluginParserBlockTaskFlowFlow;
import com.nosliw.core.application.division.manual.brick.task.flow.HAPManualPluginProcessorBlockTaskFlowActivityTask;
import com.nosliw.core.application.division.manual.brick.task.flow.HAPManualPluginProcessorBlockTaskFlowFlow;
import com.nosliw.core.application.division.manual.brick.task.script.task.HAPManualPluginParserBlockTaskTaskScript;
import com.nosliw.core.application.division.manual.brick.task.script.task.HAPManualPluginProcessorBlockTaskTaskScript;
import com.nosliw.core.application.division.manual.brick.test.complex.script.HAPManualPluginParserBlockTestComplexScript;
import com.nosliw.core.application.division.manual.brick.test.complex.script.HAPManualPluginProcessorBlockComplexTestComplexScript;
import com.nosliw.core.application.division.manual.brick.test.complex.task.HAPManualPluginParserBlockTestComplexTask;
import com.nosliw.core.application.division.manual.brick.test.complex.task.HAPManualPluginProcessorBlockComplexTestComplexTask;
import com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1.HAPManualPluginParserBlockTestComplex1;
import com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1.HAPManualPluginProcessorBlockComplexImpTestComplex1;
import com.nosliw.core.application.division.manual.brick.ui.uicontent.HAPManualDefinitionBlockComplexUIWrapperContentInCustomerTagDebugger;
import com.nosliw.core.application.division.manual.brick.ui.uicontent.HAPManualPluginParserBlockComplexUIContent;
import com.nosliw.core.application.division.manual.brick.ui.uicontent.HAPManualPluginParserBlockComplexUICustomerTag;
import com.nosliw.core.application.division.manual.brick.ui.uicontent.HAPManualPluginParserBlockComplexUICustomerTagDebugger;
import com.nosliw.core.application.division.manual.brick.ui.uicontent.HAPManualPluginParserBlockComplexUIPage;
import com.nosliw.core.application.division.manual.brick.ui.uicontent.HAPManualPluginProcessorBlockUIContent;
import com.nosliw.core.application.division.manual.brick.ui.uicontent.HAPManualPluginProcessorBlockUICustomerTag;
import com.nosliw.core.application.division.manual.brick.ui.uicontent.HAPManualPluginProcessorBlockUICustomerTagDebugger;
import com.nosliw.core.application.division.manual.brick.ui.uicontent.HAPManualPluginProcessorBlockUIPage;
import com.nosliw.core.application.division.manual.brick.ui.uicontent.HAPManualPluginProcessorUIWrapperContentInCustomerTagDebugger;
import com.nosliw.core.application.division.manual.brick.value.HAPManualPluginParserBlockValue;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickWrapperValueStructure;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualPluginParserBrickImpValueContext;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualPluginParserBrickImpValueStructure;
import com.nosliw.core.application.division.manual.brick.wrapperbrick.HAPManualPluginParserBrickWrapperBrick;
import com.nosliw.core.application.division.manual.brick.wrapperbrick.HAPManualPluginProcessorBlockSimpleImpWrapperBrick;
import com.nosliw.core.application.division.manual.brick.wrappertask.HAPManualPluginParserBlockTaskWrapper;
import com.nosliw.core.application.division.manual.brick.wrappertask.HAPManualPluginProcessorBlockSimpleImpTaskWrapper;
import com.nosliw.core.application.division.manual.common.attachment.HAPManualUtilityAttachment;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionInfoBrickLocation;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImp;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionProcessorBrickNodeDownwardWithPath;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrickLocation;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrickTraverse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityParserBrickFormatJson;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperBrickRoot;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValue;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValueReferenceResource;
import com.nosliw.core.application.division.manual.executable.HAPInfoBrickType;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualManagerBrick implements HAPPluginDivision, HAPManagerWithVariablePlugin{

	private Map<String, HAPManualDefinitionPluginParserBrick> m_brickParserPlugin;
	private Map<String, HAPManualPluginProcessorBrick> m_brickProcessorPlugin;
	private Map<String, HAPManualPluginProcessorBlock> m_blockProcessorPlugin;
	private Map<String, HAPManualPluginProcessorAdapter> m_adapterProcessorPlugin;
	private Map<String, HAPInfoBrickType> m_brickTypeInfo;
	
	private Map<String, HAPPluginProcessorEntityWithVariable> m_withVariableProcessorPlugin;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManualManagerBrick(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_brickParserPlugin = new LinkedHashMap<String, HAPManualDefinitionPluginParserBrick>();
		this.m_brickProcessorPlugin = new LinkedHashMap<String, HAPManualPluginProcessorBrick>();
		this.m_blockProcessorPlugin = new LinkedHashMap<String, HAPManualPluginProcessorBlock>();
		this.m_adapterProcessorPlugin = new LinkedHashMap<String, HAPManualPluginProcessorAdapter>();
		this.m_brickTypeInfo = new LinkedHashMap<String, HAPInfoBrickType>();
		this.m_withVariableProcessorPlugin = new LinkedHashMap<String, HAPPluginProcessorEntityWithVariable>();
		init();
	}
	
	@Override
	public Set<HAPIdBrickType> getBrickTypes() {   return null;   }
	
	@Override
	public HAPBundle getBundle(HAPIdBrick brickId) {
		HAPBundle out = new HAPBundle();
		
		Map<String, HAPManualDefinitionWrapperBrickRoot> definitions = new LinkedHashMap();
		
		HAPManualDefinitionInfoBrickLocation entityLocationInfo = HAPManualDefinitionUtilityBrickLocation.getBrickLocationInfo(brickId);
		//branch
		Map<String, HAPManualDefinitionInfoBrickLocation> branchInfos = HAPManualDefinitionUtilityBrickLocation.getBranchBrickLocationInfos(entityLocationInfo.getBasePath().getPath());
		for(String branchName : branchInfos.keySet()) {
			HAPManualContextProcessBrick processContext = new HAPManualContextProcessBrick(out, branchName, this.m_runtimeEnv, this);
			HAPManualWrapperBrickRoot rootBrick = (HAPManualWrapperBrickRoot)createRootBrick(branchInfos.get(branchName), processContext);
			definitions.put(branchName, rootBrick.getDefinition());
		}
		
		//main 
		{
			HAPManualContextProcessBrick processContext = new HAPManualContextProcessBrick(out, HAPConstantShared.NAME_ROOTBRICK_MAIN, this.m_runtimeEnv, this);
			HAPManualWrapperBrickRoot rootBrick = (HAPManualWrapperBrickRoot)createRootBrick(entityLocationInfo, processContext);
			definitions.put(HAPConstantShared.NAME_ROOTBRICK_MAIN, rootBrick.getDefinition());
		}
		
		
		out.setExtraData(definitions);
		
		return out;
	}

	
	
	private HAPWrapperBrickRoot createRootBrick(HAPManualDefinitionInfoBrickLocation entityLocationInfo, HAPManualContextProcessBrick processContext) {
		HAPManualDefinitionContextParse parseContext = new HAPManualDefinitionContextParse(entityLocationInfo.getBasePath().getPath(), HAPConstantShared.BRICK_DIVISION_MANUAL);
		
		HAPSerializationFormat format = entityLocationInfo.getFormat();
		
		String content = HAPUtilityFile.readFile(entityLocationInfo.getFiile());

		//get definition
		HAPManualDefinitionWrapperBrickRoot brickDefWrapper = this.parseBrickDefinitionWrapper(content, entityLocationInfo.getBrickTypeId(), format, parseContext);
		HAPManualDefinitionBrick brickDef = brickDefWrapper.getBrick();
		
		//build parent and 
		
		
		//build path from root
		HAPManualDefinitionUtilityBrickTraverse.traverseBrickTreeLeaves(brickDefWrapper, new HAPManualDefinitionProcessorBrickNodeDownwardWithPath() {
			@Override
			public boolean processBrickNode(HAPManualDefinitionBrick rootEntityInfo, HAPPath path, Object data) {
				if(path!=null&&!path.isEmpty()) {
					HAPManualDefinitionAttributeInBrick attr = HAPManualDefinitionUtilityBrick.getDescendantAttribute(rootEntityInfo, path);
					attr.setPathFromRoot(path);
				}
				return true;
			}
		}, brickDefWrapper);
		

		//normalize division infor in referred resource id
		normalizeDivisionInReferredResource(brickDefWrapper);
		
		//build attachment
		HAPManualUtilityAttachment.processAttachment(brickDef, null, processContext);

		//process constant
		HAPManualUtilityScriptExpressionConstant.discoverScriptExpressionConstantInBrick(brickDef, this);
		Map<String, Map<String, Object>> scriptExpressionResults = HAPManualUtilityScriptExpressionConstant.calculateScriptExpressionConstants(brickDef, m_runtimeEnv, this);
		HAPManualUtilityScriptExpressionConstant.solidateScriptExpressionConstantInBrick(brickDef, scriptExpressionResults, this);
		
		//build executable tree
		HAPManualWrapperBrickRoot out = new HAPManualWrapperBrickRoot(HAPManualUtilityProcessor.buildExecutableTree(brickDef, processContext, this));
		out.setName(processContext.getRootBrickName());
		out.setDefinition(brickDefWrapper);
		processContext.getCurrentBundle().addRootBrickWrapper(out);
		
		//brick init
		HAPManualUtilityProcessor.initBricks(processContext, this, m_runtimeEnv);

		//init
		HAPManualUtilityProcessor.processComplexBrickInit(processContext);

		HAPManualUtilityProcessor.processComplexBrickNormalizeBrickPath(processContext);
		
		//complex entity, build value context domain, create extension value structure if needed
//		HAPManualUtilityValueContextProcessor.processValueContext(out.getBrickWrapper(), processContext, this, this.m_runtimeEnv);

		//build value context in complex block
		HAPManualUtilityValueContextProcessor.buildValueContext(processContext, this, this.m_runtimeEnv);
		
		//build other value port
		HAPManualUtilityProcessor.processOtherValuePortBuild(processContext);
		
		//generate extra value structure for variable extension
		HAPManualUtilityValueContextProcessor.buildExtensionValueStructure(processContext, this, this.m_runtimeEnv);
		
		//
		HAPManualUtilityValueContextProcessor.processInheritageAndRelativeElement(null, processContext);
		
		
		//variable resolve (variable extension)-----impact data container
		HAPManualUtilityProcessor.processComplexVariableResolve(processContext);
	
		//build var criteria infor in var info container according to value port def
		HAPManualUtilityProcessor.processComplexVariableInfoResolve(processContext);
		
		//variable criteria discovery ---- impact data container and value structure in context domain
		HAPManualUtilityProcessor.processComplexValueContextDiscovery(processContext);
		
		//update value port element according to var info container after discovery
//		HAPManualUtilityProcessor.processComplexValuePortUpdate(processContext);
		
		//process entity
		HAPManualUtilityProcessor.processBrick(processContext, this.getBrickManager());
		
		//process adapter
		HAPManualUtilityProcessor.processAdapterInAttribute(processContext);
		
		HAPManualUtilityProcessor.cleanupEmptyValueStructure(processContext);
		
		return out;
	}
	
	private void normalizeDivisionInReferredResource(HAPManualDefinitionWrapperBrickRoot brickWrapper) {
		HAPManualDefinitionUtilityBrickTraverse.traverseBrickTreeLeaves(brickWrapper, new HAPManualDefinitionProcessorBrickNodeDownwardWithPath() {

			@Override
			public boolean processBrickNode(HAPManualDefinitionBrick rootBrick, HAPPath path, Object data) {
				if(path==null||path.isEmpty()) {
					return true;
				}
				
				HAPManualDefinitionAttributeInBrick attr = HAPManualDefinitionUtilityBrick.getDescendantAttribute(rootBrick, path);
				HAPManualDefinitionWrapperValue valueWrapper = attr.getValueWrapper();
				if(valueWrapper.getValueType().equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)){
					HAPManualDefinitionWrapperValueReferenceResource resourceIdValueWrapper = (HAPManualDefinitionWrapperValueReferenceResource)valueWrapper;
					resourceIdValueWrapper.setResourceId(m_runtimeEnv.getBrickManager().normalizeResourceIdWithDivision(resourceIdValueWrapper.getResourceId(), HAPConstantShared.BRICK_DIVISION_MANUAL));
				}
				return true;
			}
			
		}, HAPConstantShared.BRICK_DIVISION_MANUAL);
	}
	
	
	private void init() {

		this.registerBlockPluginInfo(HAPEnumBrickType.MODULE_100, new HAPInfoBrickType(true), new HAPManualPluginParserBlockModule(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockModule(this.m_runtimeEnv, this));
		
		this.registerBlockPluginInfo(HAPEnumBrickType.TEST_COMPLEX_1_100, new HAPInfoBrickType(true), new HAPManualPluginParserBlockTestComplex1(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockComplexImpTestComplex1(this.m_runtimeEnv, this));
		this.registerBlockPluginInfo(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100, new HAPInfoBrickType(true), new HAPManualPluginParserBlockTestComplexScript(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockComplexTestComplexScript(this.m_runtimeEnv, this));
		this.registerBlockPluginInfo(HAPEnumBrickType.TEST_COMPLEX_TASK_100, new HAPInfoBrickType(true, HAPConstantShared.TASK_TYPE_TASK), new HAPManualPluginParserBlockTestComplexTask(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockComplexTestComplexTask(this.m_runtimeEnv, this));

		this.registerBlockPluginInfo(HAPEnumBrickType.INTERACTIVETASKINTERFACE_100, new HAPInfoBrickType(false), new HAPManualPluginParserBlockSimpleInteractiveInterfaceTask(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockSimpleInteractiveInterfaceTask(this.m_runtimeEnv, this));
		this.registerBlockPluginInfo(HAPEnumBrickType.INTERACTIVEEXPRESSIONINTERFACE_100, new HAPInfoBrickType(false), new HAPManualPluginParserBlockSimpleInteractiveInterfaceExpression(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockSimpleInteractiveInterfaceExpression(this.m_runtimeEnv, this));

		
		this.registerBlockPluginInfo(HAPEnumBrickType.SERVICEPROVIDER_100, new HAPInfoBrickType(false, HAPConstantShared.TASK_TYPE_TASK), new HAPManualPluginParserBlockServiceProvider(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockSimpleImpServiceProvider(this.m_runtimeEnv, this));

		this.registerBlockPluginInfo(HAPEnumBrickType.TASK_TASK_SCRIPT_100, new HAPInfoBrickType(false, HAPConstantShared.TASK_TYPE_TASK), new HAPManualPluginParserBlockTaskTaskScript(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockTaskTaskScript(this.m_runtimeEnv, this));
		
		this.registerBlockPluginInfo(HAPEnumBrickType.TASK_TASK_FLOW_100, new HAPInfoBrickType(true, HAPConstantShared.TASK_TYPE_TASK), new HAPManualPluginParserBlockTaskFlowFlow(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockTaskFlowFlow(this.m_runtimeEnv, this));
		this.registerBlockPluginInfo(HAPEnumBrickType.TASK_TASK_ACTIVITYTASK_100, new HAPInfoBrickType(true), new HAPManualPluginParserBlockTaskFlowActivityTask(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockTaskFlowActivityTask(this.m_runtimeEnv, this));
		
		this.registerBlockPluginInfo(HAPEnumBrickType.WRAPPERBRICK_100, new HAPInfoBrickType(false), new HAPManualPluginParserBrickWrapperBrick(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockSimpleImpWrapperBrick(this.m_runtimeEnv, this));
		this.registerBlockPluginInfo(HAPEnumBrickType.TASKWRAPPER_100, new HAPInfoBrickType(false), new HAPManualPluginParserBlockTaskWrapper(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockSimpleImpTaskWrapper(this.m_runtimeEnv, this));

		this.registerBlockPluginInfo(HAPEnumBrickType.DATAEXPRESSIONLIB_100, new HAPInfoBrickType(false), new HAPManualPluginParserBlockDataExpressionLibrary(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockDataExpressionLibrary(this.m_runtimeEnv, this)); 
		this.registerBlockPluginInfo(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100, new HAPInfoBrickType(false, HAPConstantShared.TASK_TYPE_EXPRESSION), new HAPManualPluginParserBlockDataExpressionElementInLibrary(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockDataExpressionElementInLibrary(this.m_runtimeEnv, this)); 
		this.registerBlockPluginInfo(HAPEnumBrickType.DATAEXPRESSIONGROUP_100, new HAPInfoBrickType(true, HAPConstantShared.TASK_TYPE_EXPRESSION), new HAPManualPluginParserBlockDataExpressionGroup(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockDataExpressionGroup(this.m_runtimeEnv, this)); 

		this.registerBlockPluginInfo(HAPEnumBrickType.SCRIPTEXPRESSIONGROUP_100, new HAPInfoBrickType(true, HAPConstantShared.TASK_TYPE_EXPRESSION), new HAPManualPluginParserBlockScriptExpressionGroup(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockScriptExpressionGroup(this.m_runtimeEnv, this)); 

//		this.registerBlockPluginInfo(HAPEnumBrickType.SCRIPTEXPRESSIONLIB_100, new HAPInfoBrickType(false), new HAPManualPluginParserBlockScriptExpressionLibrary(this, this.m_runtimeEnv), new HAPPluginProcessorBlockScriptExpressionLibrary()); 
//		this.registerBlockPluginInfo(HAPEnumBrickType.SCRIPTEXPRESSIONLIBELEMENT_100, new HAPInfoBrickType(false, HAPConstantShared.TASK_TYPE_EXPRESSION), new HAPManualPluginParserBlockScriptExpressionElementInLibrary(this, this.m_runtimeEnv), new HAPPluginProcessorBlockScriptExpressionElementInLibrary()); 

		this.registerBlockPluginInfo(HAPEnumBrickType.UICONTENT_100, new HAPInfoBrickType(true), new HAPManualPluginParserBlockComplexUIContent(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockUIContent(this.m_runtimeEnv, this)); 
		this.registerBlockPluginInfo(HAPEnumBrickType.UIPAGE_100, new HAPInfoBrickType(true), new HAPManualPluginParserBlockComplexUIPage(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockUIPage(this.m_runtimeEnv, this)); 
		this.registerBlockPluginInfo(HAPEnumBrickType.UICUSTOMERTAG_100, new HAPInfoBrickType(true), new HAPManualPluginParserBlockComplexUICustomerTag(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockUICustomerTag(this.m_runtimeEnv, this)); 
		this.registerBlockPluginInfo(HAPEnumBrickType.UICUSTOMERTAGDEBUGGER_100, new HAPInfoBrickType(true), new HAPManualPluginParserBlockComplexUICustomerTagDebugger(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockUICustomerTagDebugger(this.m_runtimeEnv, this)); 
		this.registerBlockPluginInfo(HAPEnumBrickType.UIWRAPPERCONTENTCUSTOMERTAGDEBUGGER_100, new HAPInfoBrickType(true), new HAPManualDefinitionPluginParserBrickImp(HAPEnumBrickType.UIWRAPPERCONTENTCUSTOMERTAGDEBUGGER_100, HAPManualDefinitionBlockComplexUIWrapperContentInCustomerTagDebugger.class, this, this.m_runtimeEnv), new HAPManualPluginProcessorUIWrapperContentInCustomerTagDebugger(this.m_runtimeEnv, this)); 

		this.registerBlockPluginInfo(HAPEnumBrickType.DATA_100, new HAPInfoBrickType(false), new HAPManualPluginParserBlockData(this, this.m_runtimeEnv), null); 
		this.registerBlockPluginInfo(HAPEnumBrickType.VALUE_100, new HAPInfoBrickType(false), new HAPManualPluginParserBlockValue(this, this.m_runtimeEnv), null); 

		
		this.registerBlockPluginInfo(HAPEnumBrickType.CONTAINER_100, new HAPInfoBrickType(false), new HAPManualDefinitionPluginParserBrickImp(HAPEnumBrickType.CONTAINER_100, HAPManualDefinitionBrickContainer.class, this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockImpEmpty(HAPEnumBrickType.CONTAINER_100, HAPManualBrickContainer.class, this.m_runtimeEnv, this)); 
		this.registerBlockPluginInfo(HAPEnumBrickType.CONTAINERLIST_100, new HAPInfoBrickType(false), new HAPManualDefinitionPluginParserBrickImp(HAPEnumBrickType.CONTAINERLIST_100, HAPManualDefinitionBrickContainerList.class, this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockContainerList(this.m_runtimeEnv, this)); 
		
		
		this.registerAdapterPluginInfo(HAPEnumBrickType.DATAASSOCIATION_100, new HAPInfoBrickType(false), new HAPManualPluginParserAdapterDataAssociation(this, this.m_runtimeEnv), new HAPManaualPluginAdapterProcessorDataAssociation(this.m_runtimeEnv, this));
		this.registerAdapterPluginInfo(HAPEnumBrickType.DATAASSOCIATIONFORTASK_100, new HAPInfoBrickType(false), new HAPManualPluginParserAdapterDataAssociationForTask(this, this.m_runtimeEnv), new HAPManaualPluginAdapterProcessorDataAssociationForTask(this.m_runtimeEnv, this));
		this.registerAdapterPluginInfo(HAPEnumBrickType.DATAASSOCIATIONFOREXPRESSION_100, new HAPInfoBrickType(false), new HAPManualPluginParserAdapterDataAssociationForExpression(this, this.m_runtimeEnv), new HAPManaualPluginAdapterProcessorDataAssociationForExpression(this.m_runtimeEnv, this));

		
		this.registerBlockPluginInfo(HAPManualEnumBrickType.VALUESTRUCTURE_100, new HAPInfoBrickType(false), new HAPManualPluginParserBrickImpValueStructure(this, this.m_runtimeEnv), null);
		this.registerBlockPluginInfo(HAPManualEnumBrickType.VALUECONTEXT_100, new HAPInfoBrickType(false), new HAPManualPluginParserBrickImpValueContext(this, this.m_runtimeEnv), null);
		this.registerBlockPluginInfo(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100, new HAPInfoBrickType(), new HAPManualDefinitionPluginParserBrickImp(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100, HAPManualDefinitionBrickWrapperValueStructure.class, this, this.m_runtimeEnv), null);
		
		this.registerBrickTypeInfo(HAPManualEnumBrickType.VALUESTRUCTURE_100, new HAPInfoBrickType(false));
		this.registerBrickTypeInfo(HAPManualEnumBrickType.VALUECONTEXT_100, new HAPInfoBrickType(false));
		this.registerBrickTypeInfo(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100, new HAPInfoBrickType(false));
		
		this.registerWithVariableEntityProcessPlugin(new HAPPluginProcessorEntityWithVariableDataExpression(this.m_runtimeEnv));
		this.registerWithVariableEntityProcessPlugin(new HAPPluginProcessorEntityWithVariableScriptExpression(this.m_runtimeEnv, this));
		
	}

	public HAPManualDefinitionBrick parseBrickDefinition(Object entityObj, HAPIdBrickType brickTypeId, HAPSerializationFormat format, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionPluginParserBrick entityParserPlugin = this.getBrickParsePlugin(brickTypeId);
		return entityParserPlugin.parse(entityObj, format, parseContext);
	}

	public HAPManualDefinitionWrapperBrickRoot parseBrickDefinitionWrapper(Object entityObj, HAPIdBrickType brickTypeId, HAPSerializationFormat format, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionWrapperBrickRoot out = null;
		switch(format) {
		case JSON:
			out = HAPManualDefinitionUtilityParserBrickFormatJson.parseRootBrickWrapper((JSONObject)HAPUtilityJson.toJsonObject(entityObj), brickTypeId, parseContext, this, this.getBrickManager());
			break;
		case HTML:
			out = new HAPManualDefinitionWrapperBrickRoot(parseBrickDefinition(entityObj, brickTypeId, HAPSerializationFormat.HTML, parseContext));
			break;
		case JAVASCRIPT:
			break;
		default:
		}
		return out;
	}

	public void registerBrickTypeInfo(HAPIdBrickType brickTypeId,  HAPInfoBrickType brickTypeInfo) {	this.m_brickTypeInfo.put(brickTypeId.getKey(), brickTypeInfo); 	}
	
	public void registerBlockPluginInfo(HAPIdBrickType brickTypeId, HAPInfoBrickType brickTypeInfo, HAPManualDefinitionPluginParserBrick brickParserPlugin, HAPManualPluginProcessorBlock blockProcessPlugin) {
		this.m_brickParserPlugin.put(brickTypeId.getKey(), brickParserPlugin);
		this.m_brickProcessorPlugin.put(brickTypeId.getKey(), blockProcessPlugin);
		this.m_blockProcessorPlugin.put(brickTypeId.getKey(), blockProcessPlugin);
		this.registerBrickTypeInfo(brickTypeId, brickTypeInfo);
	}

	public void registerAdapterPluginInfo(HAPIdBrickType brickTypeId, HAPInfoBrickType brickTypeInfo, HAPManualDefinitionPluginParserBrick brickParserPlugin, HAPManualPluginProcessorAdapter adapterProcessPlugin) {
		this.m_brickParserPlugin.put(brickTypeId.getKey(), brickParserPlugin);
		this.m_brickProcessorPlugin.put(brickTypeId.getKey(), adapterProcessPlugin);
		this.m_adapterProcessorPlugin.put(brickTypeId.getKey(), adapterProcessPlugin);
		this.registerBrickTypeInfo(brickTypeId, brickTypeInfo);
	}

	public HAPInfoBrickType getBrickTypeInfo(HAPIdBrickType brickTypeId) {	return this.m_brickTypeInfo.get(brickTypeId.getKey());	}
	public HAPManualDefinitionBrick newBrickDefinition(HAPIdBrickType brickType) {    return this.getBrickParsePlugin(brickType).newBrick();      }
	public HAPManualBrick newBrick(HAPIdBrickType brickType, HAPBundle bundle) {    return this.getBrickProcessPlugin(brickType).newInstance(bundle);      }
	
	public HAPManualDefinitionPluginParserBrick getBrickParsePlugin(HAPIdBrickType entityTypeId) {   return this.m_brickParserPlugin.get(entityTypeId.getKey());    }
	public HAPManualPluginProcessorBrick getBrickProcessPlugin(HAPIdBrickType entityTypeId) {   return this.m_brickProcessorPlugin.get(entityTypeId.getKey());    }
	public HAPManualPluginProcessorBlock getBlockProcessPlugin(HAPIdBrickType entityTypeId) {   return this.m_blockProcessorPlugin.get(entityTypeId.getKey());    }
	public HAPManualPluginProcessorAdapter getAdapterProcessPlugin(HAPIdBrickType entityTypeId) {   return this.m_adapterProcessorPlugin.get(entityTypeId.getKey());    }

	@Override
	public HAPPluginProcessorEntityWithVariable getWithVariableEntityProcessPlugin(String entityType) {  return this.m_withVariableProcessorPlugin.get(entityType);  }
	public void registerWithVariableEntityProcessPlugin(HAPPluginProcessorEntityWithVariable plugin) {    this.m_withVariableProcessorPlugin.put(plugin.getEntityType(), plugin);     }

	
	private HAPManagerApplicationBrick getBrickManager() {   return this.m_runtimeEnv.getBrickManager();    }

}

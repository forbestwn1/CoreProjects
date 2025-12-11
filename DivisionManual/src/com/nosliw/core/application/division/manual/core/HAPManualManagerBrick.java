package com.nosliw.core.application.division.manual.core;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPPluginDivision;
import com.nosliw.core.application.HAPWrapperBrickRoot;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionInfoBrickLocation;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityBrickLocation;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityParserBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperBrickRoot;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualInfoBrickType;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorAdapter;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlock;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualProcessBrick;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;
import com.nosliw.core.application.entity.datarule.HAPProcessorRuleInBundle;
import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.runtime.HAPRuntimeInfo;
import com.nosliw.core.runtime.HAPRuntimeManager;

@Component
public class HAPManualManagerBrick implements HAPPluginDivision{

	private Map<String, HAPManualDefinitionPluginParserBrick> m_brickParserPlugin;
	private Map<String, HAPManualPluginProcessorBrick> m_brickProcessorPlugin;
	private Map<String, HAPManualPluginProcessorBlock> m_blockProcessorPlugin;
	private Map<String, HAPManualPluginProcessorAdapter> m_adapterProcessorPlugin;
	private Map<String, HAPManualInfoBrickType> m_brickTypeInfo;
	
	private HAPManagerApplicationBrick m_brickManager;
	
	private HAPDataTypeHelper m_dataTypeHelper;
	
	private HAPManagerResource m_resourceMan;
	
	private HAPParserDataExpression m_dataExpressionParser;

	private HAPRuntimeManager m_runtimeMan;

	private HAPManagerDataRule m_dataRuleManager;
	
	public HAPManualManagerBrick() {
		this.m_brickParserPlugin = new LinkedHashMap<String, HAPManualDefinitionPluginParserBrick>();
		this.m_brickProcessorPlugin = new LinkedHashMap<String, HAPManualPluginProcessorBrick>();
		this.m_blockProcessorPlugin = new LinkedHashMap<String, HAPManualPluginProcessorBlock>();
		this.m_adapterProcessorPlugin = new LinkedHashMap<String, HAPManualPluginProcessorAdapter>();
		this.m_brickTypeInfo = new LinkedHashMap<String, HAPManualInfoBrickType>();

		init();
	}
	
	@Autowired
	private void setBrickManager(HAPManagerApplicationBrick brickMan) {  this.m_brickManager = brickMan;  }
	
	@Autowired
	private void setDataTypeHelper(HAPDataTypeHelper dataTypeHelper) {   this.m_dataTypeHelper = dataTypeHelper;    }
	
	@Autowired
	private void setResourceManager(HAPManagerResource resourceMan) {    this.m_resourceMan = resourceMan;      }
	
	@Autowired
	private void setExpressionParser(HAPParserDataExpression dataExpressionParser) {    this.m_dataExpressionParser = dataExpressionParser;      }
	
	@Autowired
	private void setRuntimeManager(HAPRuntimeManager runtimeMan) {    this.m_runtimeMan = runtimeMan;      }
	
	@Autowired
	private void setDataRuleManager(HAPManagerDataRule dataRuleManager) {   this.m_dataRuleManager = dataRuleManager;        }
	
	@Override
	public String getDivisionName() {   return HAPConstantShared.BRICK_DIVISION_MANUAL;   }
	
	@Override
	public Set<HAPIdBrickType> getBrickTypes() {   return null;   }
	
	@Override
	public HAPBundle getBundle(HAPIdBrick brickId, HAPRuntimeInfo runtimeInfo) {
		HAPBundle out = new HAPBundle();
		
		HAPManualDefinitionInfoBrickLocation entityLocationInfo = HAPManualDefinitionUtilityBrickLocation.getBrickLocationInfo(brickId);

		Map<String, HAPManualDefinitionWrapperBrickRoot> definitions = new LinkedHashMap();

		//bundle info
		if(!entityLocationInfo.getIsSingleFile()) {
			//if folder based, try to get bundle info
			File bundleInfoFile = new File(entityLocationInfo.getBasePath().getPath()+"/bundle.json");
			if(bundleInfoFile.exists()) {
				//if bundle file exists, parse bundle file
				JSONObject bundleInfoObj = new JSONObject(HAPUtilityFile.readFile(bundleInfoFile));
				Object dynamicTaskObj = bundleInfoObj.opt(HAPBundle.DYNAMIC);
				if(dynamicTaskObj!=null) {
					out.getDynamicTaskInfo().buildObject(dynamicTaskObj, HAPSerializationFormat.JSON);
				}
			}

			//if folder based, try to get branch info
			//branch
			Map<String, HAPManualDefinitionInfoBrickLocation> branchInfos = HAPManualDefinitionUtilityBrickLocation.getBranchBrickLocationInfos(entityLocationInfo.getBasePath().getPath());
			for(String branchName : branchInfos.keySet()) {
				HAPManualWrapperBrickRoot rootBrick = (HAPManualWrapperBrickRoot)createRootBrick(branchInfos.get(branchName), new HAPManualContextProcessBrick(out, branchName, this, this.m_brickManager, this.m_dataTypeHelper, this.m_resourceMan, runtimeInfo));
				definitions.put(branchName, rootBrick.getDefinition());
			}
		}
		
		//main 
		{
			HAPManualWrapperBrickRoot rootBrick = (HAPManualWrapperBrickRoot)createRootBrick(entityLocationInfo, new HAPManualContextProcessBrick(out, HAPConstantShared.NAME_ROOTBRICK_MAIN, this, this.m_brickManager, this.m_dataTypeHelper, this.m_resourceMan, runtimeInfo));
			definitions.put(HAPConstantShared.NAME_ROOTBRICK_MAIN, rootBrick.getDefinition());
		}

		//process data rule
		HAPProcessorRuleInBundle.process(out, m_dataRuleManager, this.m_brickManager);
		
		out.setExtraData(definitions);
		
		return out;
	}

	@Autowired
	private void setBickInfoProviders(List<HAPManualProviderBrickInfo> brickInfoProviders) {
		for(HAPManualProviderBrickInfo brickInfoProvider : brickInfoProviders) {
			this.addBrickInfoProvider(brickInfoProvider);
		}
	}
	
	@Autowired
	private void setBrickInfoProvidersMultiple(List<HAPManualProviderBrickInfoMultiple> providers) {
		for(HAPManualProviderBrickInfoMultiple providerMultiple : providers) {
			for(HAPManualProviderBrickInfo brickInfoProvider : providerMultiple.getProviders()) {
				this.addBrickInfoProvider(brickInfoProvider);
			}
		}
	}
	
	private void addBrickInfoProvider(HAPManualProviderBrickInfo brickInfoProvider) {
		HAPIdBrickType brickTypeId = brickInfoProvider.getBrickTypeId();
		if(brickInfoProvider.getBrickParser()!=null) {
			this.m_brickParserPlugin.put(brickTypeId.getKey(), brickInfoProvider.getBrickParser());
		}
		
		HAPManualPluginProcessorBrick processor = brickInfoProvider.getBrickProcessor();
		if(processor!=null) {
			this.m_brickProcessorPlugin.put(brickTypeId.getKey(), processor);
			if(processor instanceof HAPManualPluginProcessorAdapter) {
				this.m_adapterProcessorPlugin.put(brickTypeId.getKey(), (HAPManualPluginProcessorAdapter)processor);
			}
			else if(processor instanceof HAPManualPluginProcessorBlock) {
				this.m_blockProcessorPlugin.put(brickTypeId.getKey(), (HAPManualPluginProcessorBlock)processor);
			}
		}
		
		if(brickInfoProvider.getBrickTypeInfo()!=null) {
			this.m_brickTypeInfo.put(brickTypeId.getKey(), brickInfoProvider.getBrickTypeInfo());
		}
	}
	
	private HAPWrapperBrickRoot createRootBrick(HAPManualDefinitionInfoBrickLocation entityLocationInfo, HAPManualContextProcessBrick processContext) {
		HAPManualDefinitionContextParse parseContext = new HAPManualDefinitionContextParse(entityLocationInfo.getBasePath().getPath(), HAPConstantShared.BRICK_DIVISION_MANUAL, this, this.m_brickManager);
		HAPSerializationFormat format = entityLocationInfo.getFormat();
		String content = HAPUtilityFile.readFile(entityLocationInfo.getFiile());

		//get definition
		HAPManualDefinitionWrapperBrickRoot brickDefWrapper = HAPManualDefinitionUtilityParserBrick.parseBrickDefinitionWrapper(content, entityLocationInfo.getBrickTypeId(), format, parseContext);
		HAPWrapperBrickRoot out = HAPManualProcessBrick.processRootBrick(brickDefWrapper, this.m_runtimeMan, this.m_dataExpressionParser, processContext);
		return out;
	}

	public void registerBrickTypeInfo(HAPIdBrickType brickTypeId,  HAPManualInfoBrickType brickTypeInfo) {	this.m_brickTypeInfo.put(brickTypeId.getKey(), brickTypeInfo); 	}
	
	public void registerBlockPluginInfo(HAPIdBrickType brickTypeId, HAPManualInfoBrickType brickTypeInfo, HAPManualDefinitionPluginParserBrick brickParserPlugin, HAPManualPluginProcessorBlock blockProcessPlugin) {
		this.m_brickParserPlugin.put(brickTypeId.getKey(), brickParserPlugin);
		this.m_brickProcessorPlugin.put(brickTypeId.getKey(), blockProcessPlugin);
		this.m_blockProcessorPlugin.put(brickTypeId.getKey(), blockProcessPlugin);
		this.registerBrickTypeInfo(brickTypeId, brickTypeInfo);
	}

	public void registerAdapterPluginInfo(HAPIdBrickType brickTypeId, HAPManualInfoBrickType brickTypeInfo, HAPManualDefinitionPluginParserBrick brickParserPlugin, HAPManualPluginProcessorAdapter adapterProcessPlugin) {
		this.m_brickParserPlugin.put(brickTypeId.getKey(), brickParserPlugin);
		this.m_brickProcessorPlugin.put(brickTypeId.getKey(), adapterProcessPlugin);
		this.m_adapterProcessorPlugin.put(brickTypeId.getKey(), adapterProcessPlugin);
		this.registerBrickTypeInfo(brickTypeId, brickTypeInfo);
	}

	public HAPManualInfoBrickType getBrickTypeInfo(HAPIdBrickType brickTypeId) {	return this.m_brickTypeInfo.get(brickTypeId.getKey());	}
	public HAPManualDefinitionBrick newBrickDefinition(HAPIdBrickType brickType) {    
		return this.getBrickParsePlugin(brickType).newBrick();      
	}
	public HAPManualBrick newBrick(HAPIdBrickType brickType, HAPBundle bundle) {    return this.getBrickProcessPlugin(brickType).newInstance(bundle, this);      }
	public HAPManualBrick newBrickWithInit(HAPIdBrickType brickType, HAPBundle bundle) {
		HAPManualBrick out = this.newBrick(brickType, bundle);
		out.init();
		return out;
	}
	
	public HAPManualDefinitionPluginParserBrick getBrickParsePlugin(HAPIdBrickType entityTypeId) {   return this.m_brickParserPlugin.get(entityTypeId.getKey());    }
	public HAPManualPluginProcessorBrick getBrickProcessPlugin(HAPIdBrickType entityTypeId) {   return this.m_brickProcessorPlugin.get(entityTypeId.getKey());    }
	public HAPManualPluginProcessorBlock getBlockProcessPlugin(HAPIdBrickType entityTypeId) {   return this.m_blockProcessorPlugin.get(entityTypeId.getKey());    }
	public HAPManualPluginProcessorAdapter getAdapterProcessPlugin(HAPIdBrickType entityTypeId) {   return this.m_adapterProcessorPlugin.get(entityTypeId.getKey());    }


	
	
	private void init() {
/*
		this.registerBlockPluginInfo(HAPEnumBrickType.MODULE_100, new HAPManualInfoBrickType(true), new HAPManualPluginParserBlockModule(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockModule(this.m_runtimeEnv, this));
		
		this.registerBlockPluginInfo(HAPEnumBrickType.TEST_COMPLEX_1_100, new HAPManualInfoBrickType(true), new HAPManualPluginParserBlockTestComplex1(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockComplexImpTestComplex1(this.m_runtimeEnv, this));
		this.registerBlockPluginInfo(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100, new HAPManualInfoBrickType(true), new HAPManualPluginParserBlockTestComplexScript(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockComplexTestComplexScript(this.m_runtimeEnv, this));
		this.registerBlockPluginInfo(HAPEnumBrickType.TEST_COMPLEX_TASK_100, new HAPManualInfoBrickType(true, HAPConstantShared.TASK_TYPE_TASK), new HAPManualPluginParserBlockTestComplexTask(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockComplexTestComplexTask(this.m_runtimeEnv, this));

		this.registerBlockPluginInfo(HAPEnumBrickType.INTERACTIVETASKINTERFACE_100, new HAPManualInfoBrickType(false), new HAPManualPluginParserBlockSimpleInteractiveInterfaceTask(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockSimpleInteractiveInterfaceTask(this.m_runtimeEnv, this));
		this.registerBlockPluginInfo(HAPEnumBrickType.INTERACTIVEEXPRESSIONINTERFACE_100, new HAPManualInfoBrickType(false), new HAPManualPluginParserBlockSimpleInteractiveInterfaceExpression(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockSimpleInteractiveInterfaceExpression(this.m_runtimeEnv, this));

		
		this.registerBlockPluginInfo(HAPEnumBrickType.SERVICEPROVIDER_100, new HAPManualInfoBrickType(false, HAPConstantShared.TASK_TYPE_TASK), new HAPManualPluginParserBlockServiceProvider(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockSimpleImpServiceProvider(this.m_runtimeEnv, this));

		this.registerBlockPluginInfo(HAPEnumBrickType.TASK_TASK_SCRIPT_100, new HAPManualInfoBrickType(false, HAPConstantShared.TASK_TYPE_TASK), new HAPManualPluginParserBlockTestComplexTaskScript(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockTestComplexTaskScript(this.m_runtimeEnv, this));
		
		this.registerBlockPluginInfo(HAPEnumBrickType.TASK_TASK_FLOW_100, new HAPManualInfoBrickType(true, HAPConstantShared.TASK_TYPE_TASK), new HAPManualPluginParserBlockTaskFlowFlow(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockTaskFlowFlow(this.m_runtimeEnv, this));
		this.registerBlockPluginInfo(HAPEnumBrickType.TASK_TASK_ACTIVITYTASK_100, new HAPManualInfoBrickType(true), new HAPManualPluginParserBlockTaskFlowActivityTask(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockTaskFlowActivityTask(this.m_runtimeEnv, this));
		this.registerBlockPluginInfo(HAPEnumBrickType.TASK_TASK_ACTIVITYDYNAMIC_100, new HAPManualInfoBrickType(true), new HAPManualPluginParserBlockTaskFlowActivityDynamic(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockTaskFlowActivityDynamic(this.m_runtimeEnv, this));
		
		this.registerBlockPluginInfo(HAPEnumBrickType.WRAPPERBRICK_100, new HAPManualInfoBrickType(false), new HAPManualPluginParserBrickWrapperBrick(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockSimpleImpWrapperBrick(this.m_runtimeEnv, this));
		this.registerBlockPluginInfo(HAPEnumBrickType.TASKWRAPPER_100, new HAPManualInfoBrickType(false), new HAPManualPluginParserBlockTaskWrapper(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockSimpleImpTaskWrapper(this.m_runtimeEnv, this));

		this.registerBlockPluginInfo(HAPEnumBrickType.DATAEXPRESSIONLIB_100, new HAPManualInfoBrickType(false), new HAPManualPluginParserBlockDataExpressionLibrary(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockDataExpressionLibrary(this.m_runtimeEnv, this)); 
		this.registerBlockPluginInfo(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100, new HAPManualInfoBrickType(false, HAPConstantShared.TASK_TYPE_EXPRESSION), new HAPManualPluginParserBlockDataExpressionStandAlone(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockDataExpressionStandAlone(this.m_runtimeEnv, this)); 
		this.registerBlockPluginInfo(HAPEnumBrickType.DATAEXPRESSIONGROUP_100, new HAPManualInfoBrickType(true, HAPConstantShared.TASK_TYPE_EXPRESSION), new HAPManualPluginParserBlockDataExpressionGroup(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockDataExpressionGroup(this.m_runtimeEnv, this)); 

		this.registerBlockPluginInfo(HAPEnumBrickType.SCRIPTEXPRESSIONGROUP_100, new HAPManualInfoBrickType(true, HAPConstantShared.TASK_TYPE_EXPRESSION), new HAPManualPluginParserBlockScriptExpressionGroup(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockScriptExpressionGroup(this.m_runtimeEnv, this)); 

//		this.registerBlockPluginInfo(HAPEnumBrickType.SCRIPTEXPRESSIONLIB_100, new HAPManualInfoBrickType(false), new HAPManualPluginParserBlockScriptExpressionLibrary(this, this.m_runtimeEnv), new HAPPluginProcessorBlockScriptExpressionLibrary()); 
//		this.registerBlockPluginInfo(HAPEnumBrickType.SCRIPTEXPRESSIONLIBELEMENT_100, new HAPManualInfoBrickType(false, HAPConstantShared.TASK_TYPE_EXPRESSION), new HAPManualPluginParserBlockScriptExpressionElementInLibrary(this, this.m_runtimeEnv), new HAPPluginProcessorBlockScriptExpressionElementInLibrary()); 

		this.registerBlockPluginInfo(HAPEnumBrickType.UICONTENT_100, new HAPManualInfoBrickType(true), new HAPManualPluginParserBlockComplexUIContent(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockUIContent(this.m_runtimeEnv, this)); 
		this.registerBlockPluginInfo(HAPEnumBrickType.UIPAGE_100, new HAPManualInfoBrickType(true), new HAPManualPluginParserBlockComplexUIPage(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockUIPage(this.m_runtimeEnv, this)); 
		this.registerBlockPluginInfo(HAPEnumBrickType.UICUSTOMERTAG_100, new HAPManualInfoBrickType(true), new HAPManualPluginParserBlockComplexUICustomerTag(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockUICustomerTag(this.m_runtimeEnv, this)); 
		this.registerBlockPluginInfo(HAPEnumBrickType.UICUSTOMERTAGDEBUGGER_100, new HAPManualInfoBrickType(true), new HAPManualPluginParserBlockComplexUICustomerTagDebugger(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockUICustomerTagDebugger(this.m_runtimeEnv, this)); 
		this.registerBlockPluginInfo(HAPEnumBrickType.UIWRAPPERCONTENTCUSTOMERTAGDEBUGGER_100, new HAPManualInfoBrickType(true), new HAPManualDefinitionPluginParserBrickImp(HAPEnumBrickType.UIWRAPPERCONTENTCUSTOMERTAGDEBUGGER_100, HAPManualDefinitionBlockComplexUIWrapperContentInCustomerTagDebugger.class, this, this.m_runtimeEnv), new HAPManualPluginProcessorUIWrapperContentInCustomerTagDebugger(this.m_runtimeEnv, this)); 

		this.registerBlockPluginInfo(HAPEnumBrickType.DATA_100, new HAPManualInfoBrickType(false), new HAPManualPluginParserBlockData(this, this.m_runtimeEnv), null); 
		this.registerBlockPluginInfo(HAPEnumBrickType.VALUE_100, new HAPManualInfoBrickType(false), new HAPManualPluginParserBlockValue(this, this.m_runtimeEnv), null); 

		
		this.registerBlockPluginInfo(HAPEnumBrickType.CONTAINER_100, new HAPManualInfoBrickType(false), new HAPManualDefinitionPluginParserBrickImp(HAPEnumBrickType.CONTAINER_100, HAPManualDefinitionBrickContainer.class, this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockImpEmpty(HAPEnumBrickType.CONTAINER_100, HAPManualBrickContainer.class, this.m_runtimeEnv, this)); 
		this.registerBlockPluginInfo(HAPEnumBrickType.CONTAINERLIST_100, new HAPManualInfoBrickType(false), new HAPManualDefinitionPluginParserBrickImp(HAPEnumBrickType.CONTAINERLIST_100, HAPManualDefinitionBrickContainerList.class, this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockContainerList(this.m_runtimeEnv, this)); 
		
		
		this.registerAdapterPluginInfo(HAPEnumBrickType.DATAASSOCIATION_100, new HAPManualInfoBrickType(false), new HAPManualPluginParserAdapterDataAssociation(this, this.m_runtimeEnv), new HAPManaualPluginAdapterProcessorDataAssociation(this.m_runtimeEnv, this));
		this.registerAdapterPluginInfo(HAPEnumBrickType.DATAASSOCIATIONFORTASK_100, new HAPManualInfoBrickType(false), new HAPManualPluginParserAdapterDataAssociationForTask(this, this.m_runtimeEnv), new HAPManaualPluginAdapterProcessorDataAssociationForTask(this.m_runtimeEnv, this));
		this.registerAdapterPluginInfo(HAPEnumBrickType.DATAASSOCIATIONFOREXPRESSION_100, new HAPManualInfoBrickType(false), new HAPManualPluginParserAdapterDataAssociationForExpression(this, this.m_runtimeEnv), new HAPManaualPluginAdapterProcessorDataAssociationForExpression(this.m_runtimeEnv, this));

		
		this.registerBlockPluginInfo(HAPManualEnumBrickType.VALUESTRUCTURE_100, new HAPManualInfoBrickType(false), new HAPManualPluginParserBrickImpValueStructure(this, this.m_runtimeEnv), null);
		this.registerBlockPluginInfo(HAPManualEnumBrickType.VALUECONTEXT_100, new HAPManualInfoBrickType(false), new HAPManualPluginParserBrickImpValueContext(this, this.m_runtimeEnv), null);
		this.registerBlockPluginInfo(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100, new HAPManualInfoBrickType(), new HAPManualDefinitionPluginParserBrickImp(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100, HAPManualDefinitionBrickWrapperValueStructure.class, this, this.m_runtimeEnv), null);
		
		this.registerBrickTypeInfo(HAPManualEnumBrickType.VALUESTRUCTURE_100, new HAPManualInfoBrickType(false));
		this.registerBrickTypeInfo(HAPManualEnumBrickType.VALUECONTEXT_100, new HAPManualInfoBrickType(false));
		this.registerBrickTypeInfo(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100, new HAPManualInfoBrickType(false));
		
		this.registerWithVariableEntityProcessPlugin(new HAPBasicPluginProcessorEntityWithVariableDataExpression(this.m_runtimeEnv));
		this.registerWithVariableEntityProcessPlugin(new HAPPluginProcessorEntityWithVariableScriptExpression(this.m_runtimeEnv, this));
*/		
	}

}

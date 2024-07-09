package com.nosliw.core.application.division.manual;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPHandlerDownward;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPPluginDivision;
import com.nosliw.core.application.HAPUtilityBrickTraverse;
import com.nosliw.core.application.HAPWrapperBrickRoot;
import com.nosliw.core.application.HAPWrapperValueOfBrick;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociation.HAPManaualPluginAdapterProcessorDataAssociation;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociation.HAPManualPluginParserAdapterDataAssociation;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociationforexpression.HAPManaualPluginAdapterProcessorDataAssociationForExpression;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociationforexpression.HAPManualPluginParserAdapterDataAssociationForExpression;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask.HAPManaualPluginAdapterProcessorDataAssociationForTask;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask.HAPManualPluginParserAdapterDataAssociationForTask;
import com.nosliw.core.application.division.manual.brick.container.HAPManualBrickContainer;
import com.nosliw.core.application.division.manual.brick.container.HAPManualBrickContainerList;
import com.nosliw.core.application.division.manual.brick.service.provider.HAPManualPluginParserBlockServiceProvider;
import com.nosliw.core.application.division.manual.brick.service.provider.HAPManualPluginProcessorBlockSimpleImpServiceProvider;
import com.nosliw.core.application.division.manual.brick.taskwrapper.HAPManualPluginParserBlockTaskWrapper;
import com.nosliw.core.application.division.manual.brick.taskwrapper.HAPManualPluginProcessorBlockSimpleImpTaskWrapper;
import com.nosliw.core.application.division.manual.brick.test.complex.script.HAPManualPluginParserBlockTestComplexScript;
import com.nosliw.core.application.division.manual.brick.test.complex.script.HAPManualPluginProcessorBlockComplexTestComplexScript;
import com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1.HAPManualPluginParserBlockTestComplex1;
import com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1.HAPManualPluginProcessorBlockComplexImpTestComplex1;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickWrapperValueStructure;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualPluginParserBrickImpValueContext;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualPluginParserBrickImpValueStructure;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAdapter;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickAdapter;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionInfoBrickLocation;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImp;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionProcessorBrickNodeDownwardWithPath;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrickLocation;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrickTraverse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityParserBrickFormatJson;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValue;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValueBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValueReferenceResource;
import com.nosliw.core.application.division.manual.executable.HAPHandlerDownwardImpTreeNode;
import com.nosliw.core.application.division.manual.executable.HAPInfoBrickType;
import com.nosliw.core.application.division.manual.executable.HAPManualAdapter;
import com.nosliw.core.application.division.manual.executable.HAPManualAttributeInBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickAdapter;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlock;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlockComplex;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlockSimple;
import com.nosliw.core.application.division.manual.executable.HAPManualUtilityBrickTraverse;
import com.nosliw.core.application.division.manual.executable.HAPTreeNodeBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualManagerBrick implements HAPPluginDivision{

	private Map<String, HAPManualDefinitionPluginParserBrick> m_brickParserPlugin;
	private Map<String, HAPPluginProcessorBrick> m_brickProcessorPlugin;
	private Map<String, HAPPluginProcessorBlock> m_blockProcessorPlugin;
	private Map<String, HAPPluginProcessorAdapter> m_adapterProcessorPlugin;
	private Map<String, HAPInfoBrickType> m_brickTypeInfo;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManualManagerBrick(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_brickParserPlugin = new LinkedHashMap<String, HAPManualDefinitionPluginParserBrick>();
		this.m_brickProcessorPlugin = new LinkedHashMap<String, HAPPluginProcessorBrick>();
		this.m_blockProcessorPlugin = new LinkedHashMap<String, HAPPluginProcessorBlock>();
		this.m_adapterProcessorPlugin = new LinkedHashMap<String, HAPPluginProcessorAdapter>();
		this.m_brickTypeInfo = new LinkedHashMap<String, HAPInfoBrickType>();
		init();
	}
	
	@Override
	public Set<HAPIdBrickType> getBrickTypes() {   return null;   }
	
	@Override
	public HAPBundle getBundle(HAPIdBrick brickId) {
		
		HAPManualDefinitionInfoBrickLocation entityLocationInfo = HAPManualDefinitionUtilityBrickLocation.getEntityLocationInfo(brickId);
		
		HAPManualDefinitionContextParse parseContext = new HAPManualDefinitionContextParse(entityLocationInfo.getBasePath().getPath(), brickId.getDivision());
		
		HAPSerializationFormat format = entityLocationInfo.getFormat();
		
		String content = HAPUtilityFile.readFile(entityLocationInfo.getFiile());

		//get definition
		HAPManualDefinitionWrapperBrick brickWrapper = this.parseEntityDefinitionInfo(content, brickId.getBrickTypeId(), format, parseContext);
		HAPManualDefinitionBrick brickDef = brickWrapper.getBrick();
		
		//build parent and 
		
		
		//build path from root
		HAPManualDefinitionUtilityBrickTraverse.traverseEntityTreeLeaves(brickWrapper, new HAPManualDefinitionProcessorBrickNodeDownwardWithPath() {
			@Override
			public boolean processBrickNode(HAPManualDefinitionWrapperBrick rootEntityInfo, HAPPath path, Object data) {
				if(path!=null&&!path.isEmpty()) {
					HAPManualDefinitionAttributeInBrick attr = (HAPManualDefinitionAttributeInBrick)HAPManualDefinitionUtilityBrick.getDescdentTreeNode(rootEntityInfo, path);
					attr.setPathFromRoot(path);
				}
				return true;
			}
		}, brickWrapper);
		

		//normalize division infor in referred resource id
		normalizeDivisionInReferredResource(brickWrapper);
		
		//process definition
		HAPBundle out = new HAPBundle();
		
		//store manual definition
		out.setExtraData(brickWrapper);

		HAPManualContextProcessBrick processContext = new HAPManualContextProcessBrick(out, this.m_runtimeEnv, this);

		//build executable tree
		out.setBrickWrapper(new HAPManualWrapperBrickRoot(HAPManualUtilityProcessor.buildExecutableTree(brickDef, processContext, this)));

		if(HAPManualDefinitionUtilityBrick.isBrickComplex(brickId.getBrickTypeId(), this)) {
			
			//init
			processComplexBrickInit(out.getBrickWrapper(), processContext);

			//complex entity, build value context domain, create extension value structure if needed
			HAPManualUtilityValueStructureDomain.buildValueStructureDomain(out.getBrickWrapper(), processContext, this, this.m_runtimeEnv);

			//variable resolve (variable extension)-----impact data container
			processComplexBrickVariableResolve(out.getBrickWrapper(), processContext);
		
			//variable criteria discovery ---- impact data container and value structure in context domain
			
			//
			
		}

		//process entity
		processEntity(out.getBrickWrapper(), processContext, this.getBrickManager());
		
		//process adapter
		processAdapter(out.getBrickWrapper(), processContext, this.getBrickManager());
		

		out.setExtraData(brickWrapper);
//		this.getEntityProcessorInfo(entityId.getEntityTypeId()).getProcessorPlugin().process(entityDefInfo);
		
		return out;
	}

	private void processComplexBrickInit(HAPWrapperBrickRoot rootBrickWrapper, HAPManualContextProcessBrick processContext) {
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrickComplex(rootBrickWrapper, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPWrapperBrickRoot brickWrapper, HAPPath path, Object data) {
				HAPManualBrickBlockComplex complexBrick = (HAPManualBrickBlockComplex)brickWrapper.getBrick();
				((HAPPluginProcessorBlockComplex)getBlockProcessPlugin(complexBrick.getBrickType())).processInit(path, processContext);
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPWrapperBrickRoot brickWrapper, HAPPath path, Object data) {
				HAPManualBrickBlockComplex complexBrick = (HAPManualBrickBlockComplex)brickWrapper.getBrick();
				((HAPPluginProcessorBlockComplex)getBlockProcessPlugin(complexBrick.getBrickType())).postProcessInit(path, processContext);
			}

		}, getBrickManager(), this, null);
	}
	
	private void processComplexBrickVariableResolve(HAPWrapperBrickRoot rootBrickWrapper, HAPManualContextProcessBrick processContext) {
		HAPManualUtilityBrickTraverse.traverseTreeWithLocalBrickComplex(rootBrickWrapper, new HAPHandlerDownward() {

			@Override
			public boolean processBrickNode(HAPWrapperBrickRoot brickWrapper, HAPPath path, Object data) {
				HAPManualBrickBlockComplex complexBrick = (HAPManualBrickBlockComplex)brickWrapper.getBrick();
				((HAPPluginProcessorBlockComplex)getBlockProcessPlugin(complexBrick.getBrickType())).processVariableResolve(path, processContext);
				return true;
			}

			@Override
			public void postProcessBrickNode(HAPWrapperBrickRoot brickWrapper, HAPPath path, Object data) {
				HAPManualBrickBlockComplex complexBrick = (HAPManualBrickBlockComplex)brickWrapper.getBrick();
				((HAPPluginProcessorBlockComplex)getBlockProcessPlugin(complexBrick.getBrickType())).postProcessVariableResolve(path, processContext);
			}

		}, getBrickManager(), this, null);
	}
	
	
	
	private void normalizeDivisionInReferredResource(HAPManualDefinitionWrapperBrick brickWrapper) {
		HAPManualDefinitionUtilityBrickTraverse.traverseEntityTreeLeaves(brickWrapper, new HAPManualDefinitionProcessorBrickNodeDownwardWithPath() {

			@Override
			public boolean processBrickNode(HAPManualDefinitionWrapperBrick rootBrickWrapper, HAPPath path, Object data) {
				if(path==null||path.isEmpty()) {
					return true;
				}
				
				HAPManualDefinitionAttributeInBrick attr = HAPManualDefinitionUtilityBrick.getDescendantAttribute(rootBrickWrapper.getBrick(), path);
				HAPManualDefinitionWrapperValue valueWrapper = attr.getValueWrapper();
				if(valueWrapper.getValueType().equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)){
					HAPManualDefinitionWrapperValueReferenceResource resourceIdValueWrapper = (HAPManualDefinitionWrapperValueReferenceResource)valueWrapper;
					resourceIdValueWrapper.setResourceId(m_runtimeEnv.getBrickManager().normalizeResourceIdWithDivision(resourceIdValueWrapper.getResourceId(), HAPConstantShared.BRICK_DIVISION_MANUAL));
				}
				return true;
			}
			
		}, HAPConstantShared.BRICK_DIVISION_MANUAL);
	}
	
	private void processAdapter(HAPWrapperBrickRoot entityInfo, HAPManualContextProcessBrick processContext, HAPManagerApplicationBrick brickMan) {
		HAPUtilityBrickTraverse.traverseTree(
				entityInfo, 
			new HAPHandlerDownwardImpTreeNode() {
					
				@Override
				protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
					HAPTreeNode treeNodeDef = HAPManualDefinitionUtilityBrick.getDefTreeNodeFromExeTreeNode(treeNode, processContext.getCurrentBundle());
					if(treeNodeDef instanceof HAPManualDefinitionAttributeInBrick) {
						HAPManualDefinitionAttributeInBrick attrDef = (HAPManualDefinitionAttributeInBrick)treeNodeDef;
						HAPManualAttributeInBrick attrExe = (HAPManualAttributeInBrick)treeNode;
						
						if(HAPManualDefinitionUtilityBrick.isAdapterAutoProcess(attrDef, brickMan)) {
							Set<HAPManualDefinitionAdapter> adapterDefs = attrDef.getAdapters();
							Map<String, HAPManualAdapter> adapterExeByName = new LinkedHashMap<String, HAPManualAdapter>();
							for(HAPManualAdapter adapter : attrExe.getManualAdapters()) {
								adapterExeByName.put(adapter.getName(), adapter);
							}
							
							for(HAPManualDefinitionAdapter adapterDef : adapterDefs) {
								HAPManualAdapter adapterExe = adapterExeByName.get(adapterDef.getName()); 
								HAPManualDefinitionWrapperValueBrick adapterWrapperDef = (HAPManualDefinitionWrapperValueBrick)adapterDef.getValueWrapper();
								HAPPluginProcessorAdapter adapterProcessPlugin = getAdapterProcessPlugin(adapterWrapperDef.getBrick().getBrickTypeId());
								
								HAPManualBrickAdapter brick = (HAPManualBrickAdapter)((HAPWrapperValueOfBrick)adapterExe.getValueWrapper()).getBrick();
								adapterProcessPlugin.process(brick, (HAPManualDefinitionBrickAdapter)adapterWrapperDef.getBrick(), new HAPManualContextProcessAdapter(processContext.getCurrentBundle(), treeNode.getTreeNodeInfo().getPathFromRoot(), m_runtimeEnv));
							}
							
							return true;
						}
						else {
							return false;
						}
					}
					else {
						return true;
					}
				}
			},
			brickMan,
			processContext);
	}
	
	private void processEntity(HAPWrapperBrickRoot entityInfo, HAPManualContextProcessBrick processContext, HAPManagerApplicationBrick brickMan) {
		HAPManualManagerBrick manualBrickMan = this;
		HAPUtilityBrickTraverse.traverseTreeWithLocalBrick(
				entityInfo, 
			new HAPHandlerDownwardImpTreeNode() {
					
				@Override
				protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
					HAPTreeNode treeNodeDef = HAPManualDefinitionUtilityBrick.getDefTreeNodeFromExeTreeNode(treeNode, processContext.getCurrentBundle());
					HAPIdBrickType entityTypeId = null;
					boolean process = true;
					HAPManualBrickBlock block = null;
					if(treeNodeDef instanceof HAPManualDefinitionWrapperBrick) {
						entityTypeId = ((HAPManualDefinitionWrapperBrick)treeNodeDef).getBrickTypeId();
					}
					else {
						HAPManualDefinitionAttributeInBrick attrDef = (HAPManualDefinitionAttributeInBrick)treeNodeDef;
						entityTypeId = ((HAPManualWithBrick)attrDef.getValueWrapper()).getBrickTypeId();
						process = HAPManualUtilityProcessor.isAttributeAutoProcess(attrDef, brickMan);
					}
					Pair<HAPManualDefinitionBrick, HAPManualBrick> brickPair = HAPManualDefinitionUtilityBrick.getBrickPair(treeNode.getTreeNodeInfo().getPathFromRoot(), processContext.getCurrentBundle());
					
					if(process) {
						if(HAPManualUtilityBrick.isBrickComplex(entityTypeId, manualBrickMan)) {
							HAPPluginProcessorBlockComplex plugin = (HAPPluginProcessorBlockComplex)getBlockProcessPlugin(entityTypeId);
							plugin.processBrick(treeNode.getTreeNodeInfo().getPathFromRoot(), processContext);
						}
						else {
							HAPPluginProcessorBlockSimple plugin = (HAPPluginProcessorBlockSimple)getBlockProcessPlugin(entityTypeId);
							plugin.process((HAPManualBrickBlockSimple)brickPair.getRight(), (HAPManualDefinitionBrickBlockSimple)brickPair.getLeft(), processContext);
						}
						return true;
					}
					else {
						return false;
					}
				}

				@Override
				public void postProcessTreeNode(HAPTreeNodeBrick treeNode, Object data) {
					HAPTreeNode treeNodeDef = HAPManualDefinitionUtilityBrick.getDefTreeNodeFromExeTreeNode(treeNode, processContext.getCurrentBundle());
					HAPIdBrickType entityTypeId = null;
					boolean process = true;
					HAPManualBrickBlock block = null;
					if(treeNodeDef instanceof HAPManualDefinitionWrapperBrick) {
						entityTypeId = ((HAPManualDefinitionWrapperBrick)treeNodeDef).getBrickTypeId();
					}
					else {
						HAPManualDefinitionAttributeInBrick attrDef = (HAPManualDefinitionAttributeInBrick)treeNodeDef;
						entityTypeId = ((HAPManualWithBrick)attrDef.getValueWrapper()).getBrickTypeId();
						process = HAPManualUtilityProcessor.isAttributeAutoProcess(attrDef, brickMan);
					}
					Pair<HAPManualDefinitionBrick, HAPManualBrick> brickPair = HAPManualDefinitionUtilityBrick.getBrickPair(treeNode.getTreeNodeInfo().getPathFromRoot(), processContext.getCurrentBundle());
					
					if(process) {
						if(HAPManualDefinitionUtilityBrick.isBrickComplex(entityTypeId, manualBrickMan)) {
							HAPPluginProcessorBlockComplex plugin = (HAPPluginProcessorBlockComplex)getBlockProcessPlugin(entityTypeId);
							plugin.postProcessBrick(treeNode.getTreeNodeInfo().getPathFromRoot(), processContext);
						}
						else {
							HAPPluginProcessorBlockSimple plugin = (HAPPluginProcessorBlockSimple)getBlockProcessPlugin(entityTypeId);
							plugin.postProcess((HAPManualBrickBlockSimple)brickPair.getRight(), (HAPManualDefinitionBrickBlockSimple)brickPair.getLeft(), processContext);
						}
					}
				}

			},
			brickMan,
			processContext);
	}

	
	private void init() {
		this.registerBlockPluginInfo(HAPEnumBrickType.TEST_COMPLEX_1_100, new HAPInfoBrickType(true), new HAPManualPluginParserBlockTestComplex1(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockComplexImpTestComplex1(this.m_runtimeEnv));
		this.registerBlockPluginInfo(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100, new HAPInfoBrickType(true), new HAPManualPluginParserBlockTestComplexScript(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockComplexTestComplexScript(this.m_runtimeEnv));

		this.registerBlockPluginInfo(HAPEnumBrickType.SERVICEPROVIDER_100, new HAPInfoBrickType(false, HAPConstantShared.TASK_TYPE_TASK), new HAPManualPluginParserBlockServiceProvider(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockSimpleImpServiceProvider(this.m_runtimeEnv));

		this.registerBlockPluginInfo(HAPEnumBrickType.TASKWRAPPER_100, new HAPInfoBrickType(false), new HAPManualPluginParserBlockTaskWrapper(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockSimpleImpTaskWrapper(this.m_runtimeEnv));
//
//		this.registerBlockPluginInfo(HAPEnumBrickType.DATAEXPRESSIONLIB_100, new HAPInfoBrickType(false), new HAPManualPluginParserBlockDataExpressionLibrary(this, this.m_runtimeEnv), new HAPPluginProcessorBlockDataExpressionLibrary()); 
//		this.registerBlockPluginInfo(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100, new HAPInfoBrickType(false, HAPConstantShared.TASK_TYPE_EXPRESSION), new HAPManualPluginParserBlockDataExpressionElementInLibrary(this, this.m_runtimeEnv), new HAPPluginProcessorBlockDataExpressionElementInLibrary()); 
//		this.registerBlockPluginInfo(HAPEnumBrickType.DATAEXPRESSIONGROUP_100, new HAPInfoBrickType(true, HAPConstantShared.TASK_TYPE_EXPRESSION), new HAPManualPluginParserBlockDataExpressionGroup(this, this.m_runtimeEnv), new HAPPluginProcessorBlockDataExpressionGroup()); 
//
//		this.registerBlockPluginInfo(HAPEnumBrickType.SCRIPTEXPRESSIONLIB_100, new HAPInfoBrickType(false), new HAPManualPluginParserBlockScriptExpressionLibrary(this, this.m_runtimeEnv), new HAPPluginProcessorBlockScriptExpressionLibrary()); 
//		this.registerBlockPluginInfo(HAPEnumBrickType.SCRIPTEXPRESSIONLIBELEMENT_100, new HAPInfoBrickType(false, HAPConstantShared.TASK_TYPE_EXPRESSION), new HAPManualPluginParserBlockScriptExpressionElementInLibrary(this, this.m_runtimeEnv), new HAPPluginProcessorBlockScriptExpressionElementInLibrary()); 

		this.registerBlockPluginInfo(HAPEnumBrickType.CONTAINER_100, new HAPInfoBrickType(false), new HAPManualDefinitionPluginParserBrickImp(HAPEnumBrickType.CONTAINER_100, HAPManualBrickContainer.class, this, this.m_runtimeEnv), new HAPPluginProcessorBlockSimpleImpEmpty(HAPEnumBrickType.CONTAINER_100, null, this.m_runtimeEnv)); 
		this.registerBlockPluginInfo(HAPEnumBrickType.CONTAINERLIST_100, new HAPInfoBrickType(false), new HAPManualDefinitionPluginParserBrickImp(HAPEnumBrickType.CONTAINERLIST_100, HAPManualBrickContainerList.class, this, this.m_runtimeEnv), new HAPPluginProcessorBlockSimpleImpEmpty(HAPEnumBrickType.CONTAINERLIST_100, null, this.m_runtimeEnv)); 
		
		
		this.registerAdapterPluginInfo(HAPEnumBrickType.DATAASSOCIATION_100, new HAPInfoBrickType(false), new HAPManualPluginParserAdapterDataAssociation(this, this.m_runtimeEnv), new HAPManaualPluginAdapterProcessorDataAssociation(this.m_runtimeEnv));
		this.registerAdapterPluginInfo(HAPEnumBrickType.DATAASSOCIATIONFORTASK_100, new HAPInfoBrickType(false), new HAPManualPluginParserAdapterDataAssociationForTask(this, this.m_runtimeEnv), new HAPManaualPluginAdapterProcessorDataAssociationForTask(this.m_runtimeEnv));
		this.registerAdapterPluginInfo(HAPEnumBrickType.DATAASSOCIATIONFOREXPRESSION_100, new HAPInfoBrickType(false), new HAPManualPluginParserAdapterDataAssociationForExpression(this, this.m_runtimeEnv), new HAPManaualPluginAdapterProcessorDataAssociationForExpression(this.m_runtimeEnv));

		
		this.registerBlockPluginInfo(HAPManualEnumBrickType.VALUESTRUCTURE_100, new HAPInfoBrickType(false), new HAPManualPluginParserBrickImpValueStructure(this, this.m_runtimeEnv), null);
		this.registerBlockPluginInfo(HAPManualEnumBrickType.VALUECONTEXT_100, new HAPInfoBrickType(false), new HAPManualPluginParserBrickImpValueContext(this, this.m_runtimeEnv), null);
		this.registerBlockPluginInfo(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100, new HAPInfoBrickType(), new HAPManualDefinitionPluginParserBrickImp(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100, HAPManualBrickWrapperValueStructure.class, this, this.m_runtimeEnv), null);
		
		this.registerBrickTypeInfo(HAPManualEnumBrickType.VALUESTRUCTURE_100, new HAPInfoBrickType(false));
		this.registerBrickTypeInfo(HAPManualEnumBrickType.VALUECONTEXT_100, new HAPInfoBrickType(false));
		this.registerBrickTypeInfo(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100, new HAPInfoBrickType(false));
	}

	public HAPManualDefinitionBrick parseEntityDefinition(Object entityObj, HAPIdBrickType entityTypeId, HAPSerializationFormat format, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionPluginParserBrick entityParserPlugin = this.getBrickParsePlugin(entityTypeId);
		return entityParserPlugin.parse(entityObj, format, parseContext);
	}

	private HAPManualDefinitionWrapperBrick parseEntityDefinitionInfo(Object entityObj, HAPIdBrickType entityTypeId, HAPSerializationFormat format, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionWrapperBrick out = null;
		switch(format) {
		case JSON:
			out = HAPManualDefinitionUtilityParserBrickFormatJson.parseBrickInfo((JSONObject)HAPUtilityJson.toJsonObject(entityObj), entityTypeId, parseContext, this, this.getBrickManager());
			break;
		case HTML:
			break;
		case JAVASCRIPT:
			break;
		default:
		}
		return out;
	}

	public void registerBrickTypeInfo(HAPIdBrickType brickTypeId,  HAPInfoBrickType brickTypeInfo) {	this.m_brickTypeInfo.put(brickTypeId.getKey(), brickTypeInfo); 	}
	
	public void registerBlockPluginInfo(HAPIdBrickType brickTypeId, HAPInfoBrickType brickTypeInfo, HAPManualDefinitionPluginParserBrick brickParserPlugin, HAPPluginProcessorBlock blockProcessPlugin) {
		this.m_brickParserPlugin.put(brickTypeId.getKey(), brickParserPlugin);
		this.m_brickProcessorPlugin.put(brickTypeId.getKey(), blockProcessPlugin);
		this.m_blockProcessorPlugin.put(brickTypeId.getKey(), blockProcessPlugin);
		this.registerBrickTypeInfo(brickTypeId, brickTypeInfo);
	}

	public void registerAdapterPluginInfo(HAPIdBrickType brickTypeId, HAPInfoBrickType brickTypeInfo, HAPManualDefinitionPluginParserBrick brickParserPlugin, HAPPluginProcessorAdapter adapterProcessPlugin) {
		this.m_brickParserPlugin.put(brickTypeId.getKey(), brickParserPlugin);
		this.m_brickProcessorPlugin.put(brickTypeId.getKey(), adapterProcessPlugin);
		this.m_adapterProcessorPlugin.put(brickTypeId.getKey(), adapterProcessPlugin);
		this.registerBrickTypeInfo(brickTypeId, brickTypeInfo);
	}

	public HAPInfoBrickType getBrickTypeInfo(HAPIdBrickType brickTypeId) {	return this.m_brickTypeInfo.get(brickTypeId.getKey());	}
	public HAPManualDefinitionBrick newBrickDefinition(HAPIdBrickType brickType) {    return this.getBrickParsePlugin(brickType).newBrick();      }
	public HAPManualBrick newBrick(HAPIdBrickType brickType) {    return this.getBrickProcessPlugin(brickType).newInstance();      }
	
	private HAPManualDefinitionPluginParserBrick getBrickParsePlugin(HAPIdBrickType entityTypeId) {   return this.m_brickParserPlugin.get(entityTypeId.getKey());    }
	private HAPPluginProcessorBrick getBrickProcessPlugin(HAPIdBrickType entityTypeId) {   return this.m_brickProcessorPlugin.get(entityTypeId.getKey());    }
	private HAPPluginProcessorBlock getBlockProcessPlugin(HAPIdBrickType entityTypeId) {   return this.m_blockProcessorPlugin.get(entityTypeId.getKey());    }
	private HAPPluginProcessorAdapter getAdapterProcessPlugin(HAPIdBrickType entityTypeId) {   return this.m_adapterProcessorPlugin.get(entityTypeId.getKey());    }

	private HAPManagerApplicationBrick getBrickManager() {   return this.m_runtimeEnv.getBrickManager();    }

}

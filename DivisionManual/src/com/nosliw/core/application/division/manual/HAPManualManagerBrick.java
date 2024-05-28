package com.nosliw.core.application.division.manual;

import java.util.LinkedHashMap;
import java.util.List;
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
import com.nosliw.core.application.HAPAdapter;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBrickAdapter;
import com.nosliw.core.application.HAPBrickBlock;
import com.nosliw.core.application.HAPBrickBlockComplex;
import com.nosliw.core.application.HAPBrickBlockSimple;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPHandlerDownwardImpTreeNode;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPPluginDivision;
import com.nosliw.core.application.HAPTreeNodeBrick;
import com.nosliw.core.application.HAPUtilityBrickTraverse;
import com.nosliw.core.application.HAPWrapperBrickRoot;
import com.nosliw.core.application.HAPWrapperValue;
import com.nosliw.core.application.HAPWrapperValueOfBrick;
import com.nosliw.core.application.HAPWrapperValueOfReferenceResource;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociation.HAPManaualPluginAdapterProcessorDataAssociation;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociation.HAPManualPluginParserAdapterDataAssociation;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask.HAPManaualPluginAdapterProcessorDataAssociationForTask;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask.HAPManualPluginParserAdapterDataAssociationForTask;
import com.nosliw.core.application.division.manual.brick.service.provider.HAPManualPluginParserBlockServiceProvider;
import com.nosliw.core.application.division.manual.brick.service.provider.HAPManualPluginProcessorBlockSimpleImpServiceProvider;
import com.nosliw.core.application.division.manual.brick.taskwrapper.HAPManualPluginParserBlockTaskWrapper;
import com.nosliw.core.application.division.manual.brick.taskwrapper.HAPManualPluginProcessorBlockSimpleImpTaskWrapper;
import com.nosliw.core.application.division.manual.brick.test.complex.script.HAPManualPluginParserBlockTestComplexScript;
import com.nosliw.core.application.division.manual.brick.test.complex.script.HAPManualPluginProcessorBlockComplexTestComplexScript;
import com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1.HAPManualPluginParserBlockTestComplex1;
import com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1.HAPManualPluginProcessorBlockComplexImpTestComplex1;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualPluginParserBrickImpValueContext;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualPluginParserBrickImpValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualManagerBrick implements HAPPluginDivision{

	private Map<String, HAPPluginParserBrick> m_brickParserPlugin;
	private Map<String, HAPPluginProcessorBlock> m_blockProcessorPlugin;
	private Map<String, HAPPluginProcessorAdapter> m_adapterProcessorPlugin;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManualManagerBrick(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_brickParserPlugin = new LinkedHashMap<String, HAPPluginParserBrick>();
		this.m_blockProcessorPlugin = new LinkedHashMap<String, HAPPluginProcessorBlock>();
		this.m_adapterProcessorPlugin = new LinkedHashMap<String, HAPPluginProcessorAdapter>();
		init();
	}
	
	@Override
	public Set<HAPIdBrickType> getBrickTypes() {   return null;   }
	
	@Override
	public HAPBundle getBundle(HAPIdBrick brickId) {
		
		HAPManualInfoBrickLocation entityLocationInfo = HAPUtilityBrickLocation.getEntityLocationInfo(brickId);
		
		HAPManualContextParse parseContext = new HAPManualContextParse(entityLocationInfo.getBasePath().getPath(), brickId.getDivision());
		
		HAPSerializationFormat format = entityLocationInfo.getFormat();
		
		String content = HAPUtilityFile.readFile(entityLocationInfo.getFiile());

		//get definition
		HAPManualWrapperBrick brickWrapper = this.parseEntityDefinitionInfo(content, brickId.getBrickTypeId(), format, parseContext);
		HAPManualBrick brickDef = brickWrapper.getBrick();
		
		//build parent and 
		
		
		//build path from root
		HAPUtilityDefinitionBrickTraverse.traverseEntityTreeLeaves(brickWrapper, new HAPManualProcessorBrickNodeDownwardWithPath() {
			@Override
			public boolean processBrickNode(HAPManualWrapperBrick rootEntityInfo, HAPPath path, Object data) {
				if(path!=null&&!path.isEmpty()) {
					HAPManualAttribute attr = (HAPManualAttribute)HAPManualUtilityBrick.getDescdentTreeNode(rootEntityInfo, path);
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

		HAPManualContextProcessBrick processContext = new HAPManualContextProcessBrick(out, this.m_runtimeEnv);

		//build executable tree
		HAPBrick rootBrickExe = this.newBrickInstance(brickDef);
		buildExecutableTree(brickDef, rootBrickExe, processContext);
		out.setBrickWrapper(new HAPWrapperBrickRoot(rootBrickExe));

		if(HAPManualUtilityBrick.isBrickComplex(brickId.getBrickTypeId(), getBrickManager())) {
			//complex entity, build value context domain
			HAPUtilityValueStructureDomain.buildValueStructureDomain(out.getBrickWrapper(), processContext, this.m_runtimeEnv);
		}

		//process entity
		processEntity(out.getBrickWrapper(), processContext, this.getBrickManager());
		
		//process adapter
		processAdapter(out.getBrickWrapper(), processContext, this.getBrickManager());
		

		out.setExtraData(brickWrapper);
//		this.getEntityProcessorInfo(entityId.getEntityTypeId()).getProcessorPlugin().process(entityDefInfo);
		
		return out;
	}

	private void normalizeDivisionInReferredResource(HAPManualWrapperBrick brickWrapper) {
		HAPUtilityDefinitionBrickTraverse.traverseEntityTreeLeaves(brickWrapper, new HAPManualProcessorBrickNodeDownwardWithPath() {

			@Override
			public boolean processBrickNode(HAPManualWrapperBrick rootBrickWrapper, HAPPath path, Object data) {
				if(path==null||path.isEmpty()) {
					return true;
				}
				
				HAPManualAttribute attr = HAPManualUtilityBrick.getDescendantAttribute(rootBrickWrapper.getBrick(), path);
				HAPManualWrapperValue valueWrapper = attr.getValueWrapper();
				if(valueWrapper.getValueType().equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)){
					HAPManualWrapperValueReferenceResource resourceIdValueWrapper = (HAPManualWrapperValueReferenceResource)valueWrapper;
					resourceIdValueWrapper.setResourceId(m_runtimeEnv.getBrickManager().normalizeResourceIdWithDivision(resourceIdValueWrapper.getResourceId(), HAPConstantShared.BRICK_DIVISION_MANUAL));
				}
				return true;
			}
			
		}, HAPConstantShared.BRICK_DIVISION_MANUAL);
	}
	
	private void processAdapter(HAPWrapperBrickRoot entityInfo, HAPManualContextProcessBrick processContext, HAPManagerApplicationBrick brickMan) {
		HAPUtilityBrickTraverse.traverseExecutableTree(
				entityInfo, 
			new HAPHandlerDownwardImpTreeNode() {
					
				@Override
				protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
					HAPTreeNode treeNodeDef = HAPManualUtilityBrick.getDefTreeNodeFromExeTreeNode(treeNode, processContext.getCurrentBundle());
					if(treeNodeDef instanceof HAPManualAttribute) {
						HAPManualAttribute attrDef = (HAPManualAttribute)treeNodeDef;
						HAPAttributeInBrick attrExe = (HAPAttributeInBrick)treeNode;
						
						if(HAPManualUtilityBrick.isAdapterAutoProcess(attrDef, brickMan)) {
							Set<HAPManualAdapter> adapterDefs = attrDef.getAdapters();
							Map<String, HAPAdapter> adapterExeByName = new LinkedHashMap<String, HAPAdapter>();
							for(HAPAdapter adapter : attrExe.getAdapters()) {
								adapterExeByName.put(adapter.getName(), adapter);
							}
							
							for(HAPManualAdapter adapterDef : adapterDefs) {
								HAPAdapter adapterExe = adapterExeByName.get(adapterDef.getName()); 
								HAPManualWrapperValueBrick adapterWrapperDef = (HAPManualWrapperValueBrick)adapterDef.getValueWrapper();
								HAPPluginProcessorAdapter adapterProcessPlugin = getAdapterProcessPlugin(adapterWrapperDef.getBrick().getBrickTypeId());
								
								HAPBrickAdapter brick = (HAPBrickAdapter)((HAPWrapperValueOfBrick)adapterExe.getValueWrapper()).getBrick();
								adapterProcessPlugin.process(brick, (HAPManualBrickAdapter)adapterWrapperDef.getBrick(), new HAPManualContextProcessAdapter(processContext.getCurrentBundle(), treeNode.getTreeNodeInfo().getPathFromRoot(), m_runtimeEnv));
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
		HAPUtilityBrickTraverse.traverseTree(
				entityInfo, 
			new HAPHandlerDownwardImpTreeNode() {
					
				@Override
				protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
					HAPTreeNode treeNodeDef = HAPManualUtilityBrick.getDefTreeNodeFromExeTreeNode(treeNode, processContext.getCurrentBundle());
					HAPIdBrickType entityTypeId = null;
					boolean process = true;
					HAPBrickBlock block = null;
					if(treeNodeDef instanceof HAPManualWrapperBrick) {
						entityTypeId = ((HAPManualWrapperBrick)treeNodeDef).getBrickTypeId();
					}
					else {
						HAPManualAttribute attrDef = (HAPManualAttribute)treeNodeDef;
						entityTypeId = ((HAPManualWithBrick)attrDef.getValueWrapper()).getBrickTypeId();
						process = HAPManualUtilityBrick.isAttributeAutoProcess(attrDef, brickMan);
					}
					Pair<HAPManualBrick, HAPBrick> brickPair = HAPManualUtilityBrick.getBrickPair(treeNode.getTreeNodeInfo().getPathFromRoot(), processContext.getCurrentBundle());
					
					if(process) {
						if(HAPManualUtilityBrick.isBrickComplex(entityTypeId, brickMan)) {
							HAPPluginProcessorBlockComplex plugin = (HAPPluginProcessorBlockComplex)getBlockProcessPlugin(entityTypeId);
							plugin.processBrick(treeNode.getTreeNodeInfo().getPathFromRoot(), processContext);
						}
						else {
							HAPPluginProcessorBlockSimple plugin = (HAPPluginProcessorBlockSimple)getBlockProcessPlugin(entityTypeId);
							plugin.process((HAPBrickBlockSimple)brickPair.getRight(), (HAPManualBrickBlockSimple)brickPair.getLeft(), processContext);
						}
						return true;
					}
					else {
						return false;
					}
				}
			},
			brickMan,
			processContext);
	}

	
	private void buildExecutableTree(HAPManualBrick brickDef, HAPBrick brick, HAPManualContextProcessBrick processContext) {
		HAPBundle bundle = processContext.getCurrentBundle();
		
		HAPIdBrickType entityTypeId = brickDef.getBrickTypeId();

		if(this.getBrickManager().getBrickTypeInfo(entityTypeId).getIsComplex()) {
			((HAPBrickBlockComplex)brick).setValueStructureDomain(bundle.getValueStructureDomain());
		}
		
		List<HAPManualAttribute> attrsDef = brickDef.getAllAttributes();
		for(HAPManualAttribute attrDef : attrsDef) {
			if(HAPManualUtilityBrick.isAttributeAutoProcess(attrDef, this.getBrickManager())) {
				HAPAttributeInBrick attrExe = new HAPAttributeInBrick();
				attrExe.setName(attrDef.getName());
				brick.setAttribute(attrExe);

				HAPManualWrapperValue attrValueInfo = attrDef.getValueWrapper();
				String attrValueType = attrValueInfo.getValueType();
				if(attrValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK)) {
					HAPManualBrick attrBrickDef = ((HAPManualWithBrick)attrValueInfo).getBrick();
					HAPBrick attrBrick = this.newBrickInstance(attrBrickDef);
					attrExe.setValueWrapper(new HAPWrapperValueOfBrick(attrBrick));
					buildExecutableTree(attrBrickDef, attrBrick, processContext);
				}
				else if(attrValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)) {
					//resource reference
					HAPManualWrapperValueReferenceResource resourceRefValueDef = (HAPManualWrapperValueReferenceResource)attrValueInfo;
					HAPWrapperValueOfReferenceResource resourceRefValue = new HAPWrapperValueOfReferenceResource(resourceRefValueDef.getResourceId());
					attrExe.setValueWrapper(resourceRefValue);
				}
				
				//adapter
				for(HAPManualAdapter defAdapterInfo : attrDef.getAdapters()) {
					HAPManualWrapperValue adapterValueWrapper = defAdapterInfo.getValueWrapper();
					String adapterValueType = adapterValueWrapper.getValueType();
					
					HAPWrapperValue adapterValueWrapperExe = null;
					if(adapterValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK)) {
						//brick
						HAPManualWrapperValueBrick adpaterValueDefWrapperBrick = (HAPManualWrapperValueBrick)adapterValueWrapper;
						HAPBrickAdapter adapterBrick = (HAPBrickAdapter)this.newBrickInstance(adpaterValueDefWrapperBrick.getBrick());
						adapterValueWrapperExe = new HAPWrapperValueOfBrick(adapterBrick);
						buildExecutableTree(adpaterValueDefWrapperBrick.getBrick(), adapterBrick, processContext);
					}
					else if(adapterValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)) {
						//resource reference
						HAPManualWrapperValueReferenceResource adpaterValueDefWrapperResourceRef = (HAPManualWrapperValueReferenceResource)adapterValueWrapper;
						adapterValueWrapperExe = new HAPWrapperValueOfReferenceResource(adpaterValueDefWrapperResourceRef.getResourceId());
					}
					HAPAdapter adapter = new HAPAdapter(adapterValueWrapperExe);
					defAdapterInfo.cloneToEntityInfo(adapter);
					attrExe.addAdapter(adapter);
				}
			}
		}
	}
	
	private HAPBrick newBrickInstance(HAPManualBrick brickDef) {
		return this.getBrickManager().newBrickInstance(brickDef.getBrickTypeId());
	}
	
	private void init() {
		this.registerBlockPluginInfo(HAPEnumBrickType.TEST_COMPLEX_1_100, new HAPManualPluginParserBlockTestComplex1(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockComplexImpTestComplex1());
		this.registerBlockPluginInfo(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100, new HAPManualPluginParserBlockTestComplexScript(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockComplexTestComplexScript());

		this.registerBlockPluginInfo(HAPEnumBrickType.SERVICEPROVIDER_100, new HAPManualPluginParserBlockServiceProvider(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockSimpleImpServiceProvider());

		this.registerBlockPluginInfo(HAPEnumBrickType.TASKWRAPPER_100, new HAPManualPluginParserBlockTaskWrapper(this, this.m_runtimeEnv), new HAPManualPluginProcessorBlockSimpleImpTaskWrapper());

		this.registerAdapterPluginInfo(HAPEnumBrickType.DATAASSOCIATION_100, new HAPManualPluginParserAdapterDataAssociation(this, this.m_runtimeEnv), new HAPManaualPluginAdapterProcessorDataAssociation(this.m_runtimeEnv));
		this.registerAdapterPluginInfo(HAPEnumBrickType.DATAASSOCIATIONFORTASK_100, new HAPManualPluginParserAdapterDataAssociationForTask(this, this.m_runtimeEnv), new HAPManaualPluginAdapterProcessorDataAssociationForTask(this.m_runtimeEnv));

		
		this.registerBlockPluginInfo(HAPManualEnumBrickType.VALUESTRUCTURE_100, new HAPManualPluginParserBrickImpValueStructure(this, this.m_runtimeEnv), null);
		this.registerBlockPluginInfo(HAPManualEnumBrickType.VALUECONTEXT_100, new HAPManualPluginParserBrickImpValueContext(this, this.m_runtimeEnv), null);
	}

	public HAPManualBrick parseEntityDefinition(Object entityObj, HAPIdBrickType entityTypeId, HAPSerializationFormat format, HAPManualContextParse parseContext) {
		HAPPluginParserBrick entityParserPlugin = this.getBrickParsePlugin(entityTypeId);
		return entityParserPlugin.parse(entityObj, format, parseContext);
	}

	private HAPManualWrapperBrick parseEntityDefinitionInfo(Object entityObj, HAPIdBrickType entityTypeId, HAPSerializationFormat format, HAPManualContextParse parseContext) {
		HAPManualWrapperBrick out = null;
		switch(format) {
		case JSON:
			out = HAPUtilityParserBrickFormatJson.parseBrickInfo((JSONObject)HAPUtilityJson.toJsonObject(entityObj), entityTypeId, parseContext, this, this.getBrickManager());
			break;
		case HTML:
			break;
		case JAVASCRIPT:
			break;
		default:
		}
		return out;
	}
	
	
	public void registerBlockPluginInfo(HAPIdBrickType brickTypeId, HAPPluginParserBrick brickParserPlugin, HAPPluginProcessorBlock blockProcessPlugin) {
		this.m_brickParserPlugin.put(brickTypeId.getKey(), brickParserPlugin);
		this.m_blockProcessorPlugin.put(brickTypeId.getKey(), blockProcessPlugin);
	}

	public void registerAdapterPluginInfo(HAPIdBrickType brickTypeId, HAPPluginParserBrick brickParserPlugin, HAPPluginProcessorAdapter adapterProcessPlugin) {
		this.m_brickParserPlugin.put(brickTypeId.getKey(), brickParserPlugin);
		this.m_adapterProcessorPlugin.put(brickTypeId.getKey(), adapterProcessPlugin);
	}

	private HAPPluginParserBrick getBrickParsePlugin(HAPIdBrickType entityTypeId) {   return this.m_brickParserPlugin.get(entityTypeId.getKey());    }
	private HAPPluginProcessorBlock getBlockProcessPlugin(HAPIdBrickType entityTypeId) {   return this.m_blockProcessorPlugin.get(entityTypeId.getKey());    }
	private HAPPluginProcessorAdapter getAdapterProcessPlugin(HAPIdBrickType entityTypeId) {   return this.m_adapterProcessorPlugin.get(entityTypeId.getKey());    }

	private HAPManagerApplicationBrick getBrickManager() {   return this.m_runtimeEnv.getBrickManager();    }

}

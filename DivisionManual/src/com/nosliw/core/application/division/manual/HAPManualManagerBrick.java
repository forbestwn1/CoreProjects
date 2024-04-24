package com.nosliw.core.application.division.manual;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBrickComplex;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPBundleComplex;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPHandlerDownwardImpTreeNode;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPInfoTreeNode;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPPluginDivision;
import com.nosliw.core.application.HAPTreeNodeBrick;
import com.nosliw.core.application.HAPUtilityBrickTraverse;
import com.nosliw.core.application.HAPWrapperAdapter;
import com.nosliw.core.application.HAPWrapperAdapterWithBrick;
import com.nosliw.core.application.HAPWrapperAdapterWithReferenceResource;
import com.nosliw.core.application.HAPWrapperBrick;
import com.nosliw.core.application.HAPWrapperValueInAttributeBrick;
import com.nosliw.core.application.HAPWrapperValueInAttributeReferenceResource;
import com.nosliw.core.application.division.manual.brick.test.complex.script.HAPPluginParserBrickImpTestComplexScript;
import com.nosliw.core.application.division.manual.brick.test.complex.script.HAPPluginProcessorBrickComplexTestComplexScript;
import com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1.HAPPluginParserBrickImpTestComplex1;
import com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1.HAPPluginProcessorBrickDefinitionComplexImpTestComplex1;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualPluginParserBrickImpValueContext;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualPluginParserBrickImpValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualManagerBrick implements HAPPluginDivision{

	private Map<String, HAPManualInfoBrickProcessor> m_entityProcessorInfo;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManualManagerBrick(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_entityProcessorInfo = new LinkedHashMap<String, HAPManualInfoBrickProcessor>();
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
		HAPUtilityDefinitionBrickTraverse.traverseEntityTreeLeaves(brickWrapper, new HAPManualProcessorEntityDownward() {
			@Override
			public boolean processEntityNode(HAPManualWrapperBrick rootEntityInfo, HAPPath path, Object data) {
				if(path!=null&&!path.isEmpty()) {
					HAPManualAttribute attr = (HAPManualAttribute)HAPManualUtilityBrick.getDescdentTreeNode(rootEntityInfo, path);
					attr.setPathFromRoot(path);
				}
				return true;
			}
		}, brickWrapper);
		
		
		//process definition
		HAPBundle out = null;
		
		if(HAPManualUtilityBrick.isBrickComplex(brickId.getBrickTypeId(), getBrickManager())) {
			//complex entity
			HAPBundleComplex complexEntityBundle = new HAPBundleComplex();
			complexEntityBundle.setExtraData(brickWrapper);
			
			HAPManualContextProcess processContext = new HAPManualContextProcess(complexEntityBundle);
			
			//build executable tree
			HAPBrickComplex rootEntityExe = (HAPBrickComplex)this.newBrickInstance(brickDef);
			rootEntityExe.setTreeNodeInfo(new HAPInfoTreeNode(new HAPPath(), null));
			buildExecutableTree(brickDef, rootEntityExe, processContext);
			complexEntityBundle.setBrickWrapper(new HAPWrapperBrick(rootEntityExe));
			
			//value context
			HAPUtilityValueStructureDomain.buildValueStructureDomain(complexEntityBundle.getBrickWrapper(), processContext, this.m_runtimeEnv);

			//process entity
			processEntity(complexEntityBundle.getBrickWrapper(), processContext, this.getBrickManager());
			
			//process adapter
			processAdapter(complexEntityBundle.getBrickWrapper(), processContext, this.getBrickManager());
			
			
			out = complexEntityBundle;
		}
		else {
			//simple entity
//			HAPBundle out = new HAPBundle();
		}
		
		out.setExtraData(brickWrapper);
//		this.getEntityProcessorInfo(entityId.getEntityTypeId()).getProcessorPlugin().process(entityDefInfo);
		
		return out;
	}

	
	private void processAdapter(HAPWrapperBrick entityInfo, HAPManualContextProcess processContext, HAPManagerApplicationBrick brickMan) {
		HAPUtilityBrickTraverse.traverseTree(
				entityInfo, 
			new HAPHandlerDownwardImpTreeNode() {
					
				@Override
				protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
					HAPTreeNode treeNodeDef = HAPManualUtilityBrick.getDefTreeNodeFromExeTreeNode(treeNode, processContext.getCurrentBundle());
					HAPIdBrickType entityTypeId = null;
					boolean process = true;
					if(treeNodeDef instanceof HAPManualAttribute) {
						HAPManualAttribute attrDef = (HAPManualAttribute)treeNodeDef;
						entityTypeId = ((HAPManualWithBrick)attrDef.getValueInfo()).getBrickTypeId();
						process = HAPManualUtilityBrick.isAttributeAutoProcess(attrDef, brickMan);
						
						if(process) {
							if(HAPManualUtilityBrick.isBrickComplex(entityTypeId, brickMan)) {
								HAPPluginProcessorBrickComplex plugin = (HAPPluginProcessorBrickComplex)getBrickProcessPlugin(entityTypeId);
								plugin.processBrick(treeNode.getTreeNodeInfo().getPathFromRoot(), processContext);
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
	
	private void processEntity(HAPWrapperBrick entityInfo, HAPManualContextProcess processContext, HAPManagerApplicationBrick brickMan) {
		HAPUtilityBrickTraverse.traverseTree(
				entityInfo, 
			new HAPHandlerDownwardImpTreeNode() {
					
				@Override
				protected boolean processTreeNode(HAPTreeNodeBrick treeNode, Object data) {
					HAPTreeNode treeNodeDef = HAPManualUtilityBrick.getDefTreeNodeFromExeTreeNode(treeNode, processContext.getCurrentBundle());
					HAPIdBrickType entityTypeId = null;
					boolean process = true;
					if(treeNodeDef instanceof HAPManualWrapperBrick) {
						entityTypeId = ((HAPManualWrapperBrick)treeNodeDef).getBrickTypeId();
					}
					else {
						HAPManualAttribute attrDef = (HAPManualAttribute)treeNodeDef;
						entityTypeId = ((HAPManualWithBrick)attrDef.getValueInfo()).getBrickTypeId();
						process = HAPManualUtilityBrick.isAttributeAutoProcess(attrDef, brickMan);
					}
					
					if(process) {
						if(HAPManualUtilityBrick.isBrickComplex(entityTypeId, brickMan)) {
							HAPPluginProcessorBrickComplex plugin = (HAPPluginProcessorBrickComplex)getBrickProcessPlugin(entityTypeId);
							plugin.processBrick(treeNode.getTreeNodeInfo().getPathFromRoot(), processContext);
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

	
	private void buildExecutableTree(HAPManualBrick brickDef, HAPBrick brick, HAPManualContextProcess processContext) {
		HAPBundle bundle = processContext.getCurrentBundle();
		
		HAPIdBrickType entityTypeId = brickDef.getBrickTypeId();

		if(this.getBrickManager().getBrickTypeInfo(entityTypeId).getIsComplex()) {
			((HAPBrickComplex)brick).setValueStructureDomain(((HAPBundleComplex)bundle).getValueStructureDomain());
		}
		
		List<HAPManualAttribute> attrsDef = brickDef.getAllAttributes();
		for(HAPManualAttribute attrDef : attrsDef) {
			if(HAPManualUtilityBrick.isAttributeAutoProcess(attrDef, this.getBrickManager())) {
				HAPAttributeInBrick attrExe = new HAPAttributeInBrick();
				attrExe.setName(attrDef.getName());
				brick.setAttribute(attrExe);

				HAPManualWrapperValueInAttribute attrValueInfo = attrDef.getValueInfo();
				String attrValueType = attrValueInfo.getValueType();
				if(attrValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK)) {
					HAPManualBrick attrBrickDef = ((HAPManualWithBrick)attrValueInfo).getBrick();
					HAPBrick attrBrick = this.newBrickInstance(attrBrickDef);
					attrExe.setValueWrapper(new HAPWrapperValueInAttributeBrick(attrBrick));
					buildExecutableTree(attrBrickDef, attrBrick, processContext);
				}
				else if(attrValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)) {
					//resource reference
					HAPManualWrapperValueInAttributeReferenceResource resourceRefValueDef = (HAPManualWrapperValueInAttributeReferenceResource)attrValueInfo;
					HAPWrapperValueInAttributeReferenceResource resourceRefValue = new HAPWrapperValueInAttributeReferenceResource(resourceRefValueDef.getResourceId());
					attrExe.setValueWrapper(resourceRefValue);
				}
				
				//adapter
				for(HAPManualInfoAdapter defAdapterInfo : attrDef.getAdapters()) {
					HAPManualWrapperValueInAttribute adapterValueWrapper = defAdapterInfo.getValueWrapper();
					String adapterValueType = adapterValueWrapper.getValueType();
					
					HAPWrapperAdapter adapterValueWrapperExe = null;
					if(adapterValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK)) {
						//brick
						HAPManualWrapperValueInAttributeBrick adpaterValueDefWrapperBrick = (HAPManualWrapperValueInAttributeBrick)adapterValueWrapper;
						HAPBrick adapterBrick = this.newBrickInstance(adpaterValueDefWrapperBrick.getBrick());
						adapterValueWrapperExe = new HAPWrapperAdapterWithBrick(adapterBrick);
						buildExecutableTree(adpaterValueDefWrapperBrick.getBrick(), adapterBrick, processContext);
					}
					else if(adapterValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)) {
						//resource reference
						HAPManualWrapperValueInAttributeReferenceResource adpaterValueDefWrapperResourceRef = (HAPManualWrapperValueInAttributeReferenceResource)adapterValueWrapper;
						adapterValueWrapperExe = new HAPWrapperAdapterWithReferenceResource(adpaterValueDefWrapperResourceRef.getResourceId());
					}
					defAdapterInfo.cloneToEntityInfo(adapterValueWrapperExe);
					attrExe.addAdapter(adapterValueWrapperExe);
				}
			}
		}
	}
	
	private HAPBrick newBrickInstance(HAPManualBrick brickDef) {
		return this.getBrickManager().newBrickInstance(brickDef.getBrickTypeId());
	}
	
	private void init() {
		this.registerEntityProcessorInfo(HAPEnumBrickType.TEST_COMPLEX_1_100, new HAPManualInfoBrickProcessor(new HAPPluginParserBrickImpTestComplex1(this, this.m_runtimeEnv), new HAPPluginProcessorBrickDefinitionComplexImpTestComplex1()));
		this.registerEntityProcessorInfo(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100, new HAPManualInfoBrickProcessor(new HAPPluginParserBrickImpTestComplexScript(this, this.m_runtimeEnv), new HAPPluginProcessorBrickComplexTestComplexScript()));

		this.registerEntityProcessorInfo(HAPManualEnumBrickType.VALUESTRUCTURE_100, new HAPManualInfoBrickProcessor(new HAPManualPluginParserBrickImpValueStructure(this, this.m_runtimeEnv), null));
		this.registerEntityProcessorInfo(HAPManualEnumBrickType.VALUECONTEXT_100, new HAPManualInfoBrickProcessor(new HAPManualPluginParserBrickImpValueContext(this, this.m_runtimeEnv), null));
	}

	public HAPManualBrick parseEntityDefinition(Object entityObj, HAPIdBrickType entityTypeId, HAPSerializationFormat format, HAPManualContextParse parseContext) {
		HAPPluginParserBrick entityParserPlugin = this.getBrickProcessorInfo(entityTypeId).getParserPlugin();
		return entityParserPlugin.parse(entityObj, format, parseContext);
	}

	private HAPManualWrapperBrick parseEntityDefinitionInfo(Object entityObj, HAPIdBrickType entityTypeId, HAPSerializationFormat format, HAPManualContextParse parseContext) {
		HAPManualWrapperBrick out = null;
		switch(format) {
		case JSON:
			out = HAPUtilityParserBrickFormatJson.parseEntityInfo((JSONObject)HAPUtilityJson.toJsonObject(entityObj), entityTypeId, parseContext, this, this.getBrickManager());
			break;
		case HTML:
			break;
		case JAVASCRIPT:
			break;
		default:
		}
		return out;
	}
	
	public void registerEntityProcessorInfo(HAPIdBrickType entityTypeId, HAPManualInfoBrickProcessor processorInfo) {	this.m_entityProcessorInfo.put(entityTypeId.getKey(), processorInfo);	}
	
	private HAPManualInfoBrickProcessor getBrickProcessorInfo(HAPIdBrickType entityTypeId) {	return this.m_entityProcessorInfo.get(entityTypeId.getKey());	}
	private HAPPluginParserBrick getBrickParsePlugin(HAPIdBrickType entityTypeId) {   return this.getBrickProcessorInfo(entityTypeId).getParserPlugin();    }
	private HAPPluginProcessorBrick getBrickProcessPlugin(HAPIdBrickType entityTypeId) {   return this.getBrickProcessorInfo(entityTypeId).getProcessorPlugin();    }

	private HAPManagerApplicationBrick getBrickManager() {   return this.m_runtimeEnv.getBrickManager();    }

}

package com.nosliw.core.application.division.manual;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.nosliw.core.application.HAPInfoBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPPluginProcessorBrick;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPUtilityBrickTraverse;
import com.nosliw.core.application.HAPWrapperBrick;
import com.nosliw.core.application.HAPWrapperValueInAttributeBrick;
import com.nosliw.core.application.HAPWrapperValueInAttributeReferenceResource;
import com.nosliw.core.application.division.manual.brick.test.complex.script.HAPPluginProcessorBrickComplexTestComplexScript;
import com.nosliw.core.application.division.manual.brick.test.complex.script.HAPPluginParserBrickImpTestComplexScript;
import com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1.HAPPluginParserBrickImpTestComplex1;
import com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1.HAPPluginProcessorBrickDefinitionComplexImpTestComplex1;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualPluginParserBrickImpValueContext;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualPluginParserBrickImpValueStructure;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualManagerBrick implements HAPPluginProcessorBrick{

	private Map<String, HAPManualInfoBrickProcessor> m_entityProcessorInfo;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManualManagerBrick(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_entityProcessorInfo = new LinkedHashMap<String, HAPManualInfoBrickProcessor>();
		init();
	}
	
	@Override
	public HAPBundle process(HAPIdBrick entityId) {
		
		HAPManualInfoBrickLocation entityLocationInfo = HAPUtilityBrickLocation.getEntityLocationInfo(entityId);
		
		HAPManualContextParse parseContext = new HAPManualContextParse(entityLocationInfo.getBasePath().getPath(), entityId.getDivision());
		
		HAPSerializationFormat format = entityLocationInfo.getFormat();
		
		String content = HAPUtilityFile.readFile(entityLocationInfo.getFiile());

		//get definition
		HAPManualWrapperBrick entityDefInfo = this.parseEntityDefinitionInfo(content, entityId.getBrickTypeId(), format, parseContext);
		
		//build parent and 
		
		
		//build path from root
		HAPUtilityDefinitionBrickTraverse.traverseEntityTreeLeaves(entityDefInfo, new HAPManualProcessorEntityDownward() {
			@Override
			public boolean processEntityNode(HAPManualWrapperBrick rootEntityInfo, HAPPath path, Object data) {
				if(path!=null&&!path.isEmpty()) {
					HAPManualAttribute attr = (HAPManualAttribute)HAPUtilityDefinitionBrick.getDescdentTreeNode(rootEntityInfo, path);
					attr.setPathFromRoot(path);
				}
				return true;
			}
		}, entityDefInfo);
		
		
		//process definition
		HAPBundle out = null;
		
		if(HAPUtilityBrick.isBrickComplex(entityId.getBrickTypeId(), getBrickManager())) {
			//complex entity
			HAPBundleComplex complexEntityBundle = new HAPBundleComplex();
			complexEntityBundle.setExtraData(entityDefInfo);
			
			HAPManualContextProcess processContext = new HAPManualContextProcess(complexEntityBundle);
			
			//build executable tree
			HAPBrickComplex rootEntityExe = (HAPBrickComplex)buildExecutableTree(entityDefInfo.getBrick(), processContext);
			complexEntityBundle.setBrickWrapper(new HAPWrapperBrick(rootEntityExe));
			
			//value context
			HAPUtilityValueStructureDomain.buildValueStructureDomain(complexEntityBundle.getBrickWrapper(), processContext, this.m_runtimeEnv);

			//process entity
			processEntity(complexEntityBundle.getBrickWrapper(), processContext, this.getBrickManager());
			
			out = complexEntityBundle;
		}
		else {
			//simple entity
//			HAPBundle out = new HAPBundle();
		}
		
		out.setExtraData(entityDefInfo);
//		this.getEntityProcessorInfo(entityId.getEntityTypeId()).getProcessorPlugin().process(entityDefInfo);
		
		return out;
	}

	private void processEntity(HAPWrapperBrick entityInfo, HAPManualContextProcess processContext, HAPManagerApplicationBrick entityMan) {
		HAPUtilityBrickTraverse.traverseTree(
				entityInfo, 
			new HAPHandlerDownwardImpTreeNode() {
					
				@Override
				protected boolean processTreeNode(HAPTreeNode treeNode, Object data) {
					HAPTreeNode treeNodeDef = HAPUtilityDefinitionBrick.getDefTreeNodeFromExeTreeNode(treeNode, processContext.getCurrentBundle());
					HAPIdBrickType entityTypeId = null;
					boolean process = true;
					if(treeNodeDef instanceof HAPManualWrapperBrick) {
						entityTypeId = ((HAPManualWrapperBrick)treeNodeDef).getBrickTypeId();
					}
					else {
						HAPManualAttribute attrDef = (HAPManualAttribute)treeNodeDef;
						entityTypeId = ((HAPManualWithBrick)attrDef.getValueInfo()).getBrickTypeId();
						process = HAPUtilityDefinitionBrick.isAttributeAutoProcess(attrDef, entityMan);
					}
					
					if(process) {
						if(HAPUtilityBrick.isBrickComplex(entityTypeId, entityMan)) {
							HAPPluginProcessorBrickComplex plugin = (HAPPluginProcessorBrickComplex)getBrickProcessPlugin(entityTypeId);
							plugin.processBrick(treeNode.getPathFromRoot(), processContext);
						}
						return true;
					}
					else {
						return false;
					}
				}
			}, 
			processContext);
	}

	
	private HAPBrick buildExecutableTree(HAPManualBrick brickDef, HAPManualContextProcess processContext) {
		HAPBundle bundle = processContext.getCurrentBundle();
		HAPIdBrickType entityTypeId = brickDef.getBrickTypeId();
		
		HAPBrick entityExe = null;
		HAPInfoBrickType brickTypeInfo = this.getBrickManager().getBrickTypeInfo(brickDef.getBrickTypeId());
		if(brickTypeInfo.getIsComplex()) {
			HAPPluginProcessorBrickComplex processPlugin = (HAPPluginProcessorBrickComplex)this.getBrickProcessPlugin(entityTypeId);
			entityExe = processPlugin.newExecutable();
			((HAPBrickComplex)entityExe).setValueStructureDomain(((HAPBundleComplex)bundle).getValueStructureDomain());
		} else {
			HAPPluginProcessorBrickSimple simplePlugin = (HAPPluginProcessorBrickSimple)this.getBrickProcessPlugin(entityTypeId);
			entityExe = simplePlugin.newExecutable();
		}
		entityExe.setBrickTypeInfo(brickTypeInfo);
		
		List<HAPManualAttribute> attrsDef = brickDef.getAllAttributes();
		for(HAPManualAttribute attrDef : attrsDef) {
			if(HAPUtilityDefinitionBrick.isAttributeAutoProcess(attrDef, this.getBrickManager())) {
				HAPAttributeInBrick attrExe = new HAPAttributeInBrick();
				attrExe.setPathFromRoot(attrDef.getPathFromRoot());
				attrExe.setName(attrDef.getName());
				HAPManualWrapperValueInAttribute attrValueInfo = attrDef.getValueInfo();
				String valueType = attrValueInfo.getValueType();
				if(attrValueInfo instanceof HAPManualWithBrick) {
					HAPBrick attrEntity = buildExecutableTree(((HAPManualWithBrick)attrValueInfo).getBrick(), processContext);
					attrExe.setValueInfo(new HAPWrapperValueInAttributeBrick(attrEntity));
				}
				else if(valueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)) {
					//resource reference
					HAPManualWrapperValueInAttributeReferenceResource resourceRefValueDef = (HAPManualWrapperValueInAttributeReferenceResource)attrValueInfo;
					HAPWrapperValueInAttributeReferenceResource resourceRefValue = new HAPWrapperValueInAttributeReferenceResource(HAPUtilityResourceId.normalizeResourceId(resourceRefValueDef.getResourceId()));
					resourceRefValue.solidate(this.getBrickManager());
				}
				entityExe.setAttribute(attrExe);
				
//				//adapter
//				for(HAPInfoAdapterDefinition defAdapterInfo : embededAttributeDef.getDefinitionAdapters()) {
//					HAPExecutableEntity adapterEntityExe = this.m_processorAdapterPlugins.get(defAdapterInfo.getValueType()).newExecutable();
//					adapterEntityExe.setDefinitionEntityId(defAdapterInfo.getEntityIdValue());
//					HAPInfoAdapterExecutable exeAdapterInfo = new HAPInfoAdapterExecutable(defAdapterInfo.getValueType(), adapterEntityExe);
//					defAdapterInfo.cloneToEntityInfo(exeAdapterInfo);
//					attrExe.getValue().addAdapter(exeAdapterInfo);
//				}
			}
		}
		return entityExe;
	}
	
	private void init() {
		this.registerEntityProcessorInfo(HAPEnumBrickType.TEST_COMPLEX_1_100, new HAPManualInfoBrickProcessor(new HAPPluginParserBrickImpTestComplex1(this, this.m_runtimeEnv), new HAPPluginProcessorBrickDefinitionComplexImpTestComplex1()));
		this.registerEntityProcessorInfo(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100, new HAPManualInfoBrickProcessor(new HAPPluginParserBrickImpTestComplexScript(this, this.m_runtimeEnv), new HAPPluginProcessorBrickComplexTestComplexScript()));

		this.registerEntityProcessorInfo(HAPEnumBrickType.VALUESTRUCTURE_100, new HAPManualInfoBrickProcessor(new HAPManualPluginParserBrickImpValueStructure(this, this.m_runtimeEnv), null));
		this.registerEntityProcessorInfo(HAPEnumBrickType.VALUECONTEXT_100, new HAPManualInfoBrickProcessor(new HAPManualPluginParserBrickImpValueContext(this, this.m_runtimeEnv), null));
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

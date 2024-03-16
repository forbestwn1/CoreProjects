package com.nosliw.core.application.division.manual;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
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
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPPluginProcessorBrick;
import com.nosliw.core.application.HAPUtilityEntity;
import com.nosliw.core.application.HAPUtilityEntityExecutableTraverse;
import com.nosliw.core.application.HAPWrapperBrick;
import com.nosliw.core.application.HAPWrapperValueInAttributeEntity;
import com.nosliw.core.application.division.manual.brick.test.complex.script.HAPPluginEntityProcessorComplexTestComplexScript;
import com.nosliw.core.application.division.manual.brick.test.complex.script.HAPPluginParserEntityImpTestComplexScript;
import com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1.HAPPluginEntityProcessorComplexTestComplex1;
import com.nosliw.core.application.division.manual.brick.test.complex.testcomplex1.HAPPluginParserEntityImpTestComplex1;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPPluginParserEntityImpValueContext;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPPluginParserEntityImpValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManagerEntityDivisionManual implements HAPPluginProcessorBrick{

	private Map<String, HAPInfoEntityProcessor> m_entityProcessorInfo;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManagerEntityDivisionManual(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_entityProcessorInfo = new LinkedHashMap<String, HAPInfoEntityProcessor>();
		init();
	}
	
	@Override
	public HAPBundle process(HAPIdBrick entityId) {
		
		HAPInfoEntityLocation entityLocationInfo = HAPUtilityEntityLocation.getEntityLocationInfo(entityId);
		
		HAPContextParse parseContext = new HAPContextParse(entityLocationInfo.getBasePath().getPath(), entityId.getDivision());
		
		HAPSerializationFormat format = entityLocationInfo.getFormat();
		
		String content = HAPUtilityFile.readFile(entityLocationInfo.getFiile());

		//get definition
		HAPManualInfoEntity entityDefInfo = this.parseEntityDefinitionInfo(content, entityId.getEntityTypeId(), format, parseContext);
		
		//build parent and 
		
		
		//build path from root
		HAPUtilityEntityDefinitionTraverse.traverseEntityTreeLeaves(entityDefInfo, new HAPManualProcessorEntityDownward() {
			@Override
			public boolean processEntityNode(HAPManualInfoEntity rootEntityInfo, HAPPath path, Object data) {
				if(path!=null&&!path.isEmpty()) {
					HAPManualAttribute attr = (HAPManualAttribute)HAPUtilityDefinitionEntity.getDescdentTreeNode(rootEntityInfo, path);
					attr.setPathFromRoot(path);
				}
				return true;
			}
		}, entityDefInfo);
		
		
		//process definition
		HAPBundle out = null;
		
		if(HAPUtilityEntity.isEntityComplex(entityId.getEntityTypeId(), getEntityManager())) {
			//complex entity
			HAPBundleComplex complexEntityBundle = new HAPBundleComplex();
			complexEntityBundle.setExtraData(entityDefInfo);
			
			HAPContextProcess processContext = new HAPContextProcess(complexEntityBundle);
			
			//build executable tree
			HAPBrickComplex rootEntityExe = (HAPBrickComplex)buildExecutableTree(entityDefInfo.getEntity(), processContext);
			complexEntityBundle.setEntityInfo(new HAPWrapperBrick(rootEntityExe));
			
			//value context
			HAPUtilityValueStructureDomain.buildValueStructureDomain(complexEntityBundle.getEntityInfo(), processContext, this.m_runtimeEnv);

			//process entity
			processEntity(complexEntityBundle.getEntityInfo(), processContext, this.getEntityManager());
			
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

	private void processEntity(HAPWrapperBrick entityInfo, HAPContextProcess processContext, HAPManagerApplicationBrick entityMan) {
		HAPUtilityEntityExecutableTraverse.traverseExecutableEntity(
				entityInfo, 
			new HAPHandlerDownwardImpTreeNode() {
					
				@Override
				protected boolean processTreeNode(HAPTreeNode treeNode, Object data) {
					HAPTreeNode treeNodeDef = HAPUtilityDefinitionEntity.getDefTreeNodeFromExeTreeNode(treeNode, processContext.getCurrentBundle());
					HAPIdBrickType entityTypeId = null;
					boolean process = true;
					if(treeNodeDef instanceof HAPManualInfoEntity) {
						entityTypeId = ((HAPManualInfoEntity)treeNodeDef).getEntityTypeId();
					}
					else {
						HAPManualAttribute attrDef = (HAPManualAttribute)treeNodeDef;
						entityTypeId = ((HAPManualWithEntity)attrDef.getValueInfo()).getEntityTypeId();
						process = HAPUtilityDefinitionEntity.isAttributeAutoProcess(attrDef, entityMan);
					}
					
					if(process) {
						if(HAPUtilityEntity.isEntityComplex(entityTypeId, entityMan)) {
							HAPPluginProcessorEntityDefinitionComplex plugin = (HAPPluginProcessorEntityDefinitionComplex)getEntityProcessPlugin(entityTypeId);
							plugin.processEntity(treeNode.getPathFromRoot(), processContext);
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

	
	private HAPBrick buildExecutableTree(HAPManualEntity entityDef, HAPContextProcess processContext) {
		HAPBundle entityBundle = processContext.getCurrentBundle();
		HAPIdBrickType entityTypeId = entityDef.getEntityTypeId();
		
		HAPBrick entityExe = null;
		boolean isComplex = HAPUtilityEntity.isEntityComplex(entityDef.getEntityTypeId(), getEntityManager());
		if(isComplex) {
			HAPPluginProcessorEntityDefinitionComplex processPlugin = (HAPPluginProcessorEntityDefinitionComplex)this.getEntityProcessPlugin(entityTypeId);
			entityExe = processPlugin.newExecutable();
		} else {
			HAPPluginProcessorEntityDefinitionSimple simplePlugin = (HAPPluginProcessorEntityDefinitionSimple)this.getEntityProcessPlugin(entityTypeId);
			entityExe = simplePlugin.newExecutable();
		}
		entityExe.setBrickTypeId(entityTypeId);
		
		List<HAPManualAttribute> attrsDef = entityDef.getAllAttributes();
		for(HAPManualAttribute attrDef : attrsDef) {
			if(HAPUtilityDefinitionEntity.isAttributeAutoProcess(attrDef, this.getEntityManager())) {
				HAPAttributeInBrick attrExe = new HAPAttributeInBrick();
				attrExe.setPathFromRoot(attrDef.getPathFromRoot());
				attrExe.setName(attrDef.getName());
				HAPManualInfoAttributeValue attrValueInfo = attrDef.getValueInfo();
				if(attrValueInfo instanceof HAPManualWithEntity) {
					HAPBrick attrEntity = buildExecutableTree(((HAPManualWithEntity)attrValueInfo).getEntity(), processContext);
					attrExe.setValueInfo(new HAPWrapperValueInAttributeEntity(attrEntity));
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
		this.registerEntityProcessorInfo(HAPEnumBrickType.TEST_COMPLEX_1_100, new HAPInfoEntityProcessor(new HAPPluginParserEntityImpTestComplex1(this, this.m_runtimeEnv), new HAPPluginEntityProcessorComplexTestComplex1()));
		this.registerEntityProcessorInfo(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100, new HAPInfoEntityProcessor(new HAPPluginParserEntityImpTestComplexScript(this, this.m_runtimeEnv), new HAPPluginEntityProcessorComplexTestComplexScript()));

		this.registerEntityProcessorInfo(HAPEnumBrickType.VALUESTRUCTURE_100, new HAPInfoEntityProcessor(new HAPPluginParserEntityImpValueStructure(this, this.m_runtimeEnv), null));
		this.registerEntityProcessorInfo(HAPEnumBrickType.VALUECONTEXT_100, new HAPInfoEntityProcessor(new HAPPluginParserEntityImpValueContext(this, this.m_runtimeEnv), null));
	}

	public HAPManualEntity parseEntityDefinition(Object entityObj, HAPIdBrickType entityTypeId, HAPSerializationFormat format, HAPContextParse parseContext) {
		HAPPluginParserEntity entityParserPlugin = this.getEntityProcessorInfo(entityTypeId).getParserPlugin();
		return entityParserPlugin.parse(entityObj, format, parseContext);
	}

	private HAPManualInfoEntity parseEntityDefinitionInfo(Object entityObj, HAPIdBrickType entityTypeId, HAPSerializationFormat format, HAPContextParse parseContext) {
		HAPManualInfoEntity out = null;
		switch(format) {
		case JSON:
			out = HAPUtilityParserEntityFormatJson.parseEntityInfo((JSONObject)HAPUtilityJson.toJsonObject(entityObj), entityTypeId, parseContext, this, this.getEntityManager());
			break;
		case HTML:
			break;
		case JAVASCRIPT:
			break;
		default:
		}
		return out;
	}
	
	public void registerEntityProcessorInfo(HAPIdBrickType entityTypeId, HAPInfoEntityProcessor processorInfo) {	this.m_entityProcessorInfo.put(entityTypeId.getKey(), processorInfo);	}
	
	private HAPInfoEntityProcessor getEntityProcessorInfo(HAPIdBrickType entityTypeId) {	return this.m_entityProcessorInfo.get(entityTypeId.getKey());	}
	private HAPPluginParserEntity getEntityParsePlugin(HAPIdBrickType entityTypeId) {   return this.getEntityProcessorInfo(entityTypeId).getParserPlugin();    }
	private HAPPluginProcessorEntityDefinition getEntityProcessPlugin(HAPIdBrickType entityTypeId) {   return this.getEntityProcessorInfo(entityTypeId).getProcessorPlugin();    }

	private HAPManagerApplicationBrick getEntityManager() {   return this.m_runtimeEnv.getEntityManager();    }
}

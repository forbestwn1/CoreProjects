package com.nosliw.data.core.entity.division.manual;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.interfac.HAPTreeNode;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.entity.HAPAttributeExecutable;
import com.nosliw.data.core.entity.HAPEntityBundle;
import com.nosliw.data.core.entity.HAPEntityBundleComplex;
import com.nosliw.data.core.entity.HAPEntityExecutable;
import com.nosliw.data.core.entity.HAPEntityExecutableComplex;
import com.nosliw.data.core.entity.HAPEnumEntityType;
import com.nosliw.data.core.entity.HAPIdEntity;
import com.nosliw.data.core.entity.HAPIdEntityType;
import com.nosliw.data.core.entity.HAPInfoAttributeValueEntity;
import com.nosliw.data.core.entity.HAPInfoEntity;
import com.nosliw.data.core.entity.HAPManagerEntity;
import com.nosliw.data.core.entity.HAPPluginProcessorEntity;
import com.nosliw.data.core.entity.HAPProcessorEntityExecutableDownwardImpTreeNode;
import com.nosliw.data.core.entity.HAPUtilityEntity;
import com.nosliw.data.core.entity.HAPUtilityEntityExecutableTraverse;
import com.nosliw.data.core.entity.division.manual.test.complex.script.HAPPluginEntityProcessorComplexTestComplexScript;
import com.nosliw.data.core.entity.division.manual.test.complex.script.HAPPluginParserEntityImpTestComplexScript;
import com.nosliw.data.core.entity.division.manual.test.complex.testcomplex1.HAPPluginEntityProcessorComplexTestComplex1;
import com.nosliw.data.core.entity.division.manual.test.complex.testcomplex1.HAPPluginParserEntityImpTestComplex1;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPPluginParserEntityImpValueContext;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPPluginParserEntityImpValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManagerEntityDivisionManual implements HAPPluginProcessorEntity{

	private Map<String, HAPInfoEntityProcessor> m_entityProcessorInfo;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManagerEntityDivisionManual(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_entityProcessorInfo = new LinkedHashMap<String, HAPInfoEntityProcessor>();
		init();
	}
	
	@Override
	public HAPEntityBundle process(HAPIdEntity entityId) {
		
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
		HAPEntityBundle out = null;
		
		if(HAPUtilityEntity.isEntityComplex(entityId.getEntityTypeId(), getEntityManager())) {
			//complex entity
			HAPEntityBundleComplex complexEntityBundle = new HAPEntityBundleComplex();
			complexEntityBundle.setExtraData(entityDefInfo);
			
			HAPContextProcess processContext = new HAPContextProcess(complexEntityBundle);
			
			//build executable tree
			HAPEntityExecutableComplex rootEntityExe = (HAPEntityExecutableComplex)buildExecutableTree(entityDefInfo.getEntity(), processContext);
			complexEntityBundle.setEntityInfo(new HAPInfoEntity(rootEntityExe));
			
			//value context
			HAPUtilityValueStructureDomain.buildValueStructureDomain(complexEntityBundle.getEntityInfo(), processContext, this.m_runtimeEnv);

			//process entity
			processEntity(complexEntityBundle.getEntityInfo(), processContext, this.getEntityManager());
			
			out = complexEntityBundle;
		}
		else {
			//simple entity
//			HAPEntityBundle out = new HAPEntityBundle();
		}
		
		out.setExtraData(entityDefInfo);
//		this.getEntityProcessorInfo(entityId.getEntityTypeId()).getProcessorPlugin().process(entityDefInfo);
		
		return out;
	}

	private void processEntity(HAPInfoEntity entityInfo, HAPContextProcess processContext, HAPManagerEntity entityMan) {
		HAPUtilityEntityExecutableTraverse.traverseExecutableEntity(
				entityInfo, 
			new HAPProcessorEntityExecutableDownwardImpTreeNode() {
					
				@Override
				protected boolean processTreeNode(HAPTreeNode treeNode, Object data) {
					HAPTreeNode treeNodeDef = HAPUtilityDefinitionEntity.getDefTreeNodeFromExeTreeNode(treeNode, processContext.getCurrentBundle());
					HAPIdEntityType entityTypeId = null;
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

	
	private HAPEntityExecutable buildExecutableTree(HAPManualEntity entityDef, HAPContextProcess processContext) {
		HAPEntityBundle entityBundle = processContext.getCurrentBundle();
		HAPIdEntityType entityTypeId = entityDef.getEntityTypeId();
		
		HAPEntityExecutable entityExe = null;
		boolean isComplex = HAPUtilityEntity.isEntityComplex(entityDef.getEntityTypeId(), getEntityManager());
		if(isComplex) {
			HAPPluginProcessorEntityDefinitionComplex processPlugin = (HAPPluginProcessorEntityDefinitionComplex)this.getEntityProcessPlugin(entityTypeId);
			entityExe = processPlugin.newExecutable();
		} else {
			HAPPluginProcessorEntityDefinitionSimple simplePlugin = (HAPPluginProcessorEntityDefinitionSimple)this.getEntityProcessPlugin(entityTypeId);
			entityExe = simplePlugin.newExecutable();
		}
		entityExe.setEntityTypeId(entityTypeId);
		
		List<HAPManualAttribute> attrsDef = entityDef.getAllAttributes();
		for(HAPManualAttribute attrDef : attrsDef) {
			if(HAPUtilityDefinitionEntity.isAttributeAutoProcess(attrDef, this.getEntityManager())) {
				HAPAttributeExecutable attrExe = new HAPAttributeExecutable();
				attrExe.setPathFromRoot(attrDef.getPathFromRoot());
				attrExe.setName(attrDef.getName());
				HAPManualInfoAttributeValue attrValueInfo = attrDef.getValueInfo();
				if(attrValueInfo instanceof HAPManualWithEntity) {
					HAPEntityExecutable attrEntity = buildExecutableTree(((HAPManualWithEntity)attrValueInfo).getEntity(), processContext);
					attrExe.setValueInfo(new HAPInfoAttributeValueEntity(attrEntity));
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
		this.registerEntityProcessorInfo(HAPEnumEntityType.TEST_COMPLEX_1_100, new HAPInfoEntityProcessor(new HAPPluginParserEntityImpTestComplex1(this, this.m_runtimeEnv), new HAPPluginEntityProcessorComplexTestComplex1()));
		this.registerEntityProcessorInfo(HAPEnumEntityType.TEST_COMPLEX_SCRIPT_100, new HAPInfoEntityProcessor(new HAPPluginParserEntityImpTestComplexScript(this, this.m_runtimeEnv), new HAPPluginEntityProcessorComplexTestComplexScript()));

		this.registerEntityProcessorInfo(HAPEnumEntityType.VALUESTRUCTURE_100, new HAPInfoEntityProcessor(new HAPPluginParserEntityImpValueStructure(this, this.m_runtimeEnv), null));
		this.registerEntityProcessorInfo(HAPEnumEntityType.VALUECONTEXT_100, new HAPInfoEntityProcessor(new HAPPluginParserEntityImpValueContext(this, this.m_runtimeEnv), null));
	}

	public HAPManualEntity parseEntityDefinition(Object entityObj, HAPIdEntityType entityTypeId, HAPSerializationFormat format, HAPContextParse parseContext) {
		HAPPluginParserEntity entityParserPlugin = this.getEntityProcessorInfo(entityTypeId).getParserPlugin();
		return entityParserPlugin.parse(entityObj, format, parseContext);
	}

	private HAPManualInfoEntity parseEntityDefinitionInfo(Object entityObj, HAPIdEntityType entityTypeId, HAPSerializationFormat format, HAPContextParse parseContext) {
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
	
	public void registerEntityProcessorInfo(HAPIdEntityType entityTypeId, HAPInfoEntityProcessor processorInfo) {	this.m_entityProcessorInfo.put(entityTypeId.getKey(), processorInfo);	}
	
	private HAPInfoEntityProcessor getEntityProcessorInfo(HAPIdEntityType entityTypeId) {	return this.m_entityProcessorInfo.get(entityTypeId.getKey());	}
	private HAPPluginParserEntity getEntityParsePlugin(HAPIdEntityType entityTypeId) {   return this.getEntityProcessorInfo(entityTypeId).getParserPlugin();    }
	private HAPPluginProcessorEntityDefinition getEntityProcessPlugin(HAPIdEntityType entityTypeId) {   return this.getEntityProcessorInfo(entityTypeId).getProcessorPlugin();    }

	private HAPManagerEntity getEntityManager() {   return this.m_runtimeEnv.getEntityManager();    }
}

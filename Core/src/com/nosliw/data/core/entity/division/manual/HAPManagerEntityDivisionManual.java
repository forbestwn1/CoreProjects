package com.nosliw.data.core.entity.division.manual;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityExecutable;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.domain.entity.HAPEmbededExecutable;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPInfoAdapterDefinition;
import com.nosliw.data.core.domain.entity.HAPInfoAdapterExecutable;
import com.nosliw.data.core.domain.entity.HAPReferenceExternal;
import com.nosliw.data.core.domain.entity.test.complex.script.HAPPluginEntityProcessorComplexTestComplexScript;
import com.nosliw.data.core.domain.entity.test.complex.testcomplex1.HAPPluginEntityProcessorComplexTestComplex1;
import com.nosliw.data.core.entity.HAPAttributeExecutable;
import com.nosliw.data.core.entity.HAPEntityBundle;
import com.nosliw.data.core.entity.HAPEntityBundleComplex;
import com.nosliw.data.core.entity.HAPEntityExecutable;
import com.nosliw.data.core.entity.HAPEntityExecutableComplex;
import com.nosliw.data.core.entity.HAPEnumEntityType;
import com.nosliw.data.core.entity.HAPIdEntity;
import com.nosliw.data.core.entity.HAPIdEntityType;
import com.nosliw.data.core.entity.HAPInfoAttributeValue;
import com.nosliw.data.core.entity.HAPInfoAttributeValueEntity;
import com.nosliw.data.core.entity.HAPInfoAttributeValueReferenceResource;
import com.nosliw.data.core.entity.HAPInfoEntity;
import com.nosliw.data.core.entity.HAPInfoEntityType;
import com.nosliw.data.core.entity.HAPManagerEntity;
import com.nosliw.data.core.entity.HAPPluginProcessorEntity;
import com.nosliw.data.core.entity.HAPUtilityEntity;
import com.nosliw.data.core.entity.division.manual.test.complex.script.HAPPluginParserEntityImpTestComplexScript;
import com.nosliw.data.core.entity.division.manual.test.complex.testcomplex1.HAPPluginParserEntityImpTestComplex1;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPPluginParserEntityImpValueContext;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPPluginParserEntityImpValueStructure;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
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
		HAPUtilityEntityDefinition.traverseEntityTreeLeaves(entityDefInfo, new HAPManualProcessorEntityDownward() {
			@Override
			public boolean processEntityNode(HAPManualInfoEntity rootEntityInfo, HAPPath path, Object data) {
				if(path!=null&&!path.isEmpty()) {
					HAPManualAttribute attr = rootEntityInfo.getEntity().getDescendantAttribute(path);
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
			
			HAPUtilityValueStructureDomain.buildValueStructureDomain(complexEntityBundle.getEntityInfo(), processContext, this.m_runtimeEnv);
			
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

	private HAPEntityExecutable buildExecutableTree(HAPManualEntity entityDef, HAPContextProcess processContext) {
		HAPEntityBundle entityBundle = processContext.getCurrentBundle();
		HAPIdEntityType entityTypeId = entityDef.getEntityTypeId();
		
		HAPEntityExecutable entityExe = null;
		boolean isComplex = HAPUtilityEntity.isEntityComplex(entityDef.getEntityTypeId(), getEntityManager());
		if(isComplex) {
			HAPPluginProcessorEntityDefinitionComplex processPlugin = (HAPPluginProcessorEntityDefinitionComplex)this.m_entityProcessorInfo.get(entityTypeId.getKey()).getProcessorPlugin();
			entityExe = processPlugin.newExecutable();
		} else {
			HAPPluginProcessorEntityDefinitionSimple simplePlugin = (HAPPluginProcessorEntityDefinitionSimple)this.m_entityProcessorInfo.get(entityTypeId.getKey()).getProcessorPlugin();
			entityExe = simplePlugin.newExecutable();
		}
		entityExe.setEntityTypeId(entityTypeId);
		
		List<HAPManualAttribute> attrsDef = entityDef.getAllAttributes();
		for(HAPManualAttribute attrDef : attrsDef) {
			if(HAPUtilityEntityDefinition.isAttributeAutoProcess(attrDef, this.getEntityManager())) {
				HAPAttributeExecutable attrExe = new HAPAttributeExecutable();
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
	
	private HAPInfoAttributeValue buildExecutableTree(HAPManualInfoAttributeValue attributeValueInfo, HAPContextProcess processContext) {
		HAPInfoAttributeValue out;
		String defAttrValueType = attributeValueInfo.getValueType();
		if(defAttrValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)) {
			//refer to external resource
			HAPManualInfoAttributeValueReferenceResource resourceRefAttrInfo = (HAPManualInfoAttributeValueReferenceResource)attributeValueInfo;
			HAPInfoResourceIdNormalize globalId = HAPUtilityResourceId.normalizeResourceId(resourceRefAttrInfo.getResourceId());
			out = new HAPInfoAttributeValueReferenceResource(globalId);
		}
		else {
			//internal
			HAPManualEntity entityDef = null;
			if(defAttrValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_ENTITY)) {
				HAPManualInfoAttributeValueEntity entityAttrInfo = (HAPManualInfoAttributeValueEntity)attributeValueInfo;
				entityDef = entityAttrInfo.getEntity();
			}
			
			HAPUtilityEntity.isEntityComplex(entityId.getEntityTypeId(), getEntityManager())
			HAPInfoEntityType entityTypeInfo = attributeValueInfo.getEntityTypeInfo();
			
			HAPEntityExecutable entityExe = buildExecutableTree(entityDef, processContext);
			out = new HAPInfoAttributeValueEntity(entityExe);
		}
		return out;
	}
	
	private Pair<String, Object> buildExecutableTree(HAPManualEntity entityDefinitio, HAPContextProcessor processContext) {
		HAPExecutableBundle complexResourceBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal defDomain = complexResourceBundle.getDefinitionDomain();
		
		//create executable and add to domain
		Object embededValue = null;
		String embededValueType = null;
		HAPInfoEntityInDomainDefinition entityDefInfo = defDomain.getEntityInfoDefinition(entityDefinitionId);
		if(entityDefInfo.isSolid()) {
			HAPManualEntity entityDef = entityDefInfo.getEntity();
			String entityType = entityDef.getEntityType();
			HAPExecutableEntity entityExe = null;
			if(entityDefInfo.isComplexEntity()) {
				entityExe = this.m_processorComplexEntityPlugins.get(entityType).newExecutable();
				((HAPExecutableEntityComplex)entityExe).setValueStructureDomain(complexResourceBundle.getValueStructureDomain());
			} else {
				entityExe = this.m_processorSimpleEntityPlugins.get(entityType).newExecutable();
			}
			entityExe.setDefinitionEntityId(entityDefinitionId);
			embededValue = entityExe;
			embededValueType = HAPConstantShared.EMBEDEDVALUE_TYPE_ENTITY;
			
			List<HAPManualAttribute> attrsDef = entityDef.getAttributes();
			for(HAPManualAttribute attrDef : attrsDef) {
				if(attrDef.isAttributeAutoProcess()) {
					HAPEmbededDefinition embededAttributeDef = attrDef.getValue();

					HAPIdEntityInDomain attrEntityDefId = (HAPIdEntityInDomain)embededAttributeDef.getValue();
					Pair<String, Object> entityInfo = buildExecutableTree(attrEntityDefId, processContext);
					entityExe.setAttribute(attrDef.getName(), new HAPEmbededExecutable(entityInfo.getRight(), entityInfo.getLeft()), attrDef.getValueTypeInfo());
					HAPAttributeEntityExecutable attrExe = entityExe.getAttribute(attrDef.getName());
					attrExe.setAttributeAutoProcess(true);
					
					//adapter
					for(HAPInfoAdapterDefinition defAdapterInfo : embededAttributeDef.getDefinitionAdapters()) {
						HAPExecutableEntity adapterEntityExe = this.m_processorAdapterPlugins.get(defAdapterInfo.getValueType()).newExecutable();
						adapterEntityExe.setDefinitionEntityId(defAdapterInfo.getEntityIdValue());
						HAPInfoAdapterExecutable exeAdapterInfo = new HAPInfoAdapterExecutable(defAdapterInfo.getValueType(), adapterEntityExe);
						defAdapterInfo.cloneToEntityInfo(exeAdapterInfo);
						attrExe.getValue().addAdapter(exeAdapterInfo);
					}
				}
			}
		}
		else {
			//for globle complex entity
			HAPInfoResourceIdNormalize globalId = normalizeResourceId(entityDefInfo.getReferedResourceId());
			embededValue = new HAPReferenceExternal(globalId);
			embededValueType = HAPConstantShared.EMBEDEDVALUE_TYPE_EXTERNALREFERENCE;
		}
		
		return Pair.of(embededValueType, embededValue);
	}

	
	
	
	private void init() {
		this.registerEntityProcessorInfo(HAPEnumEntityType.TEST_COMPLEX_1_100, new HAPInfoEntityProcessor(new HAPPluginParserEntityImpTestComplex1(this, this.m_runtimeEnv), new HAPPluginEntityProcessorComplexTestComplexScript()));
		this.registerEntityProcessorInfo(HAPEnumEntityType.TEST_COMPLEX_SCRIPT_100, new HAPInfoEntityProcessor(new HAPPluginParserEntityImpTestComplexScript(this, this.m_runtimeEnv), new HAPPluginEntityProcessorComplexTestComplex1()));

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
	
	public void registerEntityProcessorInfo(HAPIdEntityType entityTypeId, HAPInfoEntityProcessor processorInfo) {
		this.m_entityProcessorInfo.put(entityTypeId.getKey(), processorInfo);
	}
	
	private HAPInfoEntityProcessor getEntityProcessorInfo(HAPIdEntityType entityTypeId) {
		return this.m_entityProcessorInfo.get(entityTypeId.getKey());
	}
	
	private HAPManagerEntity getEntityManager() {   return this.m_runtimeEnv.getEntityManager();    }
}

package com.nosliw.data.core.domain.entity;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPExecutablePackage;
import com.nosliw.data.core.domain.HAPExtraInfoEntityInDomainExecutable;
import com.nosliw.data.core.domain.HAPIdComplexEntityInGlobal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainExecutable;
import com.nosliw.data.core.domain.HAPManagerDomainEntityDefinition;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.HAPUtilityExport;
import com.nosliw.data.core.domain.attachment.HAPUtilityAttachment;
import com.nosliw.data.core.domain.container.HAPContainerEntityDefinition;
import com.nosliw.data.core.domain.container.HAPContainerEntityExecutable;
import com.nosliw.data.core.domain.container.HAPElementContainerDefinition;
import com.nosliw.data.core.domain.container.HAPElementContainerExecutable;
import com.nosliw.data.core.domain.container.HAPUtilityContainerEntity;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityValueStructureDomain;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManagerDomainEntityExecutable {

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	//pulug in for processing complex entity
	private Map<String, HAPPluginEntityProcessorComplex> m_processorComplexEntityPlugins;

	private Map<String, HAPPluginEntityProcessorSimple> m_processorSimpleEntityPlugins;
	
	private Map<String, HAPPluginAdapterProcessor> m_processorAdapterPlugins;
	
	private Map<HAPResourceIdSimple, HAPExecutableBundle> m_complexResourceBundles;
	
	public HAPManagerDomainEntityExecutable(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_processorComplexEntityPlugins = new LinkedHashMap<String, HAPPluginEntityProcessorComplex>();
		this.m_processorSimpleEntityPlugins = new LinkedHashMap<String, HAPPluginEntityProcessorSimple>();
		this.m_processorAdapterPlugins = new LinkedHashMap<String, HAPPluginAdapterProcessor>();
		this.m_complexResourceBundles = new LinkedHashMap<HAPResourceIdSimple, HAPExecutableBundle>();
	}

	public Set<String> getComplexEntityTypes(){     return this.m_processorComplexEntityPlugins.keySet();     }
	
	public HAPExecutablePackage getExecutablePackage(HAPResourceId resourceId) {
		HAPExecutablePackage out = new HAPExecutablePackage();

		//figure out root entity
		HAPIdComplexEntityInGlobal gloablId = getComplexEntityGolbalIdResourceId(resourceId);
		out.setMainEntityId(gloablId);
		
		//find all related complex resource
		Set<HAPResourceIdSimple> dependency = new HashSet<HAPResourceIdSimple>();
		buildDependencyGroup(gloablId.getResourceInfo().getRootResourceIdSimple(), dependency);
		for(HAPResourceIdSimple bundleId : dependency) {
			out.addDependency(bundleId);
		}
		
		HAPUtilityExport.exportExecutablePackage(out, this, this.m_runtimeEnv.getRuntime().getRuntimeInfo());
		return out;
	}

	public HAPExecutableBundle getComplexEntityResourceBundle(HAPResourceIdSimple complexEntityResourceId) {
		return buildComplexEntityResourceBundle(complexEntityResourceId);

//		HAPExecutableBundle out = this.m_complexResourceBundles.get(complexEntityResourceId);
//		if(out==null) {
//			out = buildComplexEntityResourceBundle(complexEntityResourceId);
//		}
//		return out;
	}
	
	public void processComplexEntity(HAPIdEntityInDomain exeEntityId, HAPContextProcessor processContext) {
		if(processContext.getCurrentBundle().getExecutableDomain().getEntityInfoExecutable(exeEntityId).isLocalEntity()) {
			HAPPluginEntityProcessorComplex processPlugin = this.m_processorComplexEntityPlugins.get(exeEntityId.getEntityType());
			processPlugin.process(exeEntityId, processContext);
		}
	}
	
	public HAPExecutableEntity processSimpleEntity(HAPIdEntityInDomain defEntityId, HAPContextProcessor processContext) {
		HAPPluginEntityProcessorSimple processPlugin = this.m_processorSimpleEntityPlugins.get(defEntityId.getEntityType());
		return processPlugin.process(defEntityId, processContext);
	}
	
	public Object processEmbededAdapter(HAPInfoAdapter adapterDefinition, HAPExecutableEntity parentExe, Object childObj, HAPContextProcessor processContext){
		HAPPluginAdapterProcessor processPlugin = this.m_processorAdapterPlugins.get(adapterDefinition.getValueType());
		
		HAPExecutable childExe = null;
		HAPContextProcessor childProcessorContext = processContext;
		
		if(childObj instanceof HAPExecutable) {
			childExe = (HAPExecutable)childObj;
		}
		else if(childObj instanceof HAPIdEntityInDomain) {
			HAPIdEntityInDomain childEntityId = (HAPIdEntityInDomain)childObj;
			HAPInfoEntityInDomainExecutable entityInfo = processContext.getCurrentExecutableDomain().getEntityInfoExecutable(childEntityId);	
			if(entityInfo.isLocalEntity()) {
				childExe = entityInfo.getEntity();
			}
			else {
				HAPIdComplexEntityInGlobal globalEntityId = processContext.getCurrentExecutableDomain().getExternalEntityGlobalId(childEntityId);
				HAPExecutableBundle childBundle = this.getComplexEntityResourceBundle(globalEntityId.getResourceInfo().getRootResourceIdSimple());
				childExe = childBundle.getExecutableDomain().getEntityInfoExecutable(globalEntityId.getEntityIdInDomain()).getEntity();
				childProcessorContext = new HAPContextProcessor(childBundle, processContext.getRuntimeEnvironment());
			}
		}
		
		return processPlugin.process(adapterDefinition.getValue(), childExe, childProcessorContext, parentExe, processContext);
	}
	
	public void registerComplexEntityProcessorPlugin(HAPPluginEntityProcessorComplex complexEntityProcessorPlugin) {	this.m_processorComplexEntityPlugins.put(complexEntityProcessorPlugin.getEntityType(), complexEntityProcessorPlugin);	}
	public void registerSimpleEntityProcessorPlugin(HAPPluginEntityProcessorSimple simpleEntityProcessorPlugin) {	this.m_processorSimpleEntityPlugins.put(simpleEntityProcessorPlugin.getEntityType(), simpleEntityProcessorPlugin);	}
	public void registerAdapterProcessorPlugin(HAPPluginAdapterProcessor processorAdapterPlugin) {	this.m_processorAdapterPlugins.put(processorAdapterPlugin.getAdapterType(), processorAdapterPlugin);	}
	

	private void buildDependencyGroup(HAPResourceIdSimple complexEntityResourceId, Set<HAPResourceIdSimple> dependency) {
		if(!dependency.contains(complexEntityResourceId)) {
			dependency.add(complexEntityResourceId);
			HAPExecutableBundle bundle = this.getComplexEntityResourceBundle(complexEntityResourceId);
			Set<HAPResourceIdSimple> bundleDependency = bundle.getComplexResourceDependency();
			for(HAPResourceIdSimple id : bundleDependency) {
				buildDependencyGroup(id, dependency);
			}
		}
	}
	
	private HAPExecutableBundle buildComplexEntityResourceBundle(HAPResourceIdSimple complexEntityResourceId) {
		//build definition
		HAPDomainEntityDefinitionGlobal globalDefDomain = new HAPDomainEntityDefinitionGlobal(this.getDomainEntityDefinitionManager(), this.getResourceDefinitionManager());
		HAPResourceDefinition resourceDef = this.getResourceDefinitionManager().getResourceDefinition(complexEntityResourceId, globalDefDomain);

		//build executable
		HAPContextProcessor processContext = new HAPContextProcessor(new HAPExecutableBundle(complexEntityResourceId, globalDefDomain), m_runtimeEnv);
		this.processBundle(processContext);
		return processContext.getCurrentBundle();
	}

	private void processBundle(HAPContextProcessor processContext) {
		HAPExecutableBundle complexResourcePackage = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal defDomain = complexResourcePackage.getDefinitionDomain();
		HAPDomainEntityExecutableResourceComplex exeDomain = complexResourcePackage.getExecutableDomain();
		
		//build complex entity tree
		HAPIdEntityInDomain rootEntityIdExe = buildExecutableTree(complexResourcePackage.getDefinitionRootEntityId(), processContext);
		
		//build attachment domain(build attachment tree, merge attachment value)
		HAPUtilityAttachment.buildAttachmentDomain(rootEntityIdExe, processContext);
		
		//process value structure
		HAPUtilityValueStructureDomain.buildValueStructureDomain(rootEntityIdExe, processContext);
		
		//process root entity
		this.processComplexEntity(complexResourcePackage.getExecutableRootEntityId(), processContext);
		
		//process adapter
		
	}

	private HAPManagerResourceDefinition getResourceDefinitionManager() {    return this.m_runtimeEnv.getResourceDefinitionManager();      }
	private HAPManagerDomainEntityDefinition getDomainEntityDefinitionManager() {     return this.m_runtimeEnv.getDomainEntityDefinitionManager();      }
	
	private HAPIdEntityInDomain buildExecutableTree(HAPIdEntityInDomain complexEntityDefinitionId, HAPContextProcessor processContext) {
		HAPExecutableBundle complexResourceBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal defDomain = complexResourceBundle.getDefinitionDomain();
		HAPDomainEntityExecutableResourceComplex exeDomain = complexResourceBundle.getExecutableDomain();
		
		//create executable and add to domain
		HAPIdEntityInDomain out = null;
		HAPInfoEntityInDomainDefinition entityDefInfo = defDomain.getSolidEntityInfoDefinition(complexEntityDefinitionId, null);
		if(entityDefInfo.isSolid()) {
			HAPDefinitionEntityInDomainComplex complexEntityDef = (HAPDefinitionEntityInDomainComplex)entityDefInfo.getEntity();
			String entityType = complexEntityDef.getEntityType();
			HAPExecutableEntityComplex exeEntity = this.m_processorComplexEntityPlugins.get(entityType).newExecutable();
			HAPExtraInfoEntityInDomainExecutable exeExtraInfo = HAPUtilityDomain.buildExecutableExtraInfo(entityDefInfo);
			out = complexResourceBundle.addExecutableEntity(exeEntity, exeExtraInfo);
			HAPExecutableEntityComplex complexEntityExe = exeDomain.getEntityInfoExecutable(out).getEntity();
			
			List<HAPAttributeEntityDefinition> attrsDef = complexEntityDef.getAttributes();
			for(HAPAttributeEntityDefinition attrDef : attrsDef) {
				if(attrDef.getValueTypeInfo().getIsComplex()) {
					if(attrDef.getEntityType().equals(HAPConstantShared.ENTITYATTRIBUTE_TYPE_NORMAL)){
						HAPAttributeEntityDefinitionNormal simpleAttrDef = (HAPAttributeEntityDefinitionNormal)attrDef;
						HAPEmbededDefinition embededAttributeDef = simpleAttrDef.getValue();
						HAPIdEntityInDomain attrEntityDefId = (HAPIdEntityInDomain)embededAttributeDef.getValue();
						HAPIdEntityInDomain attrEntityExeId = buildExecutableTree(attrEntityDefId, processContext);
						complexEntityExe.setNormalAttribute(attrDef.getName(), new HAPEmbededExecutable(attrEntityExeId), attrDef.getValueTypeInfo());
					} 
					else if(attrDef.getEntityType().equals(HAPConstantShared.ENTITYATTRIBUTE_TYPE_CONTAINER)) {
						HAPAttributeEntityDefinitionContainer containerAttrDef = (HAPAttributeEntityDefinitionContainer)attrDef;
						HAPContainerEntityDefinition defContainer = containerAttrDef.getValue();
						HAPContainerEntityExecutable exeContainer = HAPUtilityContainerEntity.buildExecutionContainer(defContainer.getContainerType());
						List<HAPElementContainerDefinition> defEles = defContainer.getAllElements();
						for(HAPElementContainerDefinition defEle : defEles) {
							HAPIdEntityInDomain defEleId = (HAPIdEntityInDomain)defEle.getEmbededElementEntity().getValue();
							HAPInfoEntityInDomainDefinition eleEntityInfo = defDomain.getEntityInfoDefinition(defEleId);
							HAPIdEntityInDomain eleEntityExeId = buildExecutableTree(defEleId, processContext);
							HAPElementContainerExecutable eleExe = HAPUtilityContainerEntity.buildExecutableContainerElement(defEle, defEleId.getEntityId());
							eleExe.setEmbededElementEntity(new HAPEmbededExecutable(eleEntityExeId));
							exeContainer.addEntityElement(eleExe);
						}
						complexEntityExe.setContainerAttribute(attrDef.getName(), exeContainer, attrDef.getValueTypeInfo());
					}
				}
			}
		}
		else {
			//for globle complex entity
			HAPIdComplexEntityInGlobal globalId = getComplexEntityGolbalIdResourceId(entityDefInfo.getReferedResourceId());
			HAPExtraInfoEntityInDomainExecutable exeExtraInfo = HAPUtilityDomain.buildExecutableExtraInfo(entityDefInfo);
			out = complexResourceBundle.addExecutableEntity(globalId, exeExtraInfo);
		}
		
		return out;
	}
	
	//
	private HAPIdComplexEntityInGlobal getComplexEntityGolbalIdResourceId(HAPResourceId resourceId) {
		HAPInfoResourceIdNormalize normalizedResourceInfo = this.getResourceDefinitionManager().normalizeResourceId(resourceId);
		HAPInfoEntityInDomainExecutable entityInfo = this.getComplexEntityResourceBundle(normalizedResourceInfo.getRootResourceIdSimple()).getEntityInfoExecutable(normalizedResourceInfo);
		return new HAPIdComplexEntityInGlobal(normalizedResourceInfo, entityInfo.getEntityId());
	}
}

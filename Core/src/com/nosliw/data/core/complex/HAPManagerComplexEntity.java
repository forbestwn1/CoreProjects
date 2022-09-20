package com.nosliw.data.core.complex;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPContainerEntity;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.HAPEmbededEntity;
import com.nosliw.data.core.domain.HAPExtraInfoEntityInDomainExecutable;
import com.nosliw.data.core.domain.HAPIdComplexEntityInGlobal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoContainerElement;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainExecutable;
import com.nosliw.data.core.domain.HAPManagerDomainEntityDefinition;
import com.nosliw.data.core.domain.HAPExecutablePackage;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.HAPUtilityExport;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManagerComplexEntity {

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	//pulug in for processing complex entity
	private Map<String, HAPPluginComplexEntityProcessor> m_processorPlugins;

	private Map<String, HAPPluginAdapterProcessor> m_processorAdapterPlugins;
	
	private Map<HAPResourceIdSimple, HAPExecutableBundle> m_complexResourceBundles;
	
	public HAPManagerComplexEntity(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_processorPlugins = new LinkedHashMap<String, HAPPluginComplexEntityProcessor>();
		this.m_complexResourceBundles = new LinkedHashMap<HAPResourceIdSimple, HAPExecutableBundle>();
	}

	public HAPExecutablePackage getExecutablePackage(HAPResourceId resourceId) {
		HAPExecutablePackage out = new HAPExecutablePackage();

		//figure out root entity
		HAPIdComplexEntityInGlobal gloablId = getComplexEntityGolbalIdResourceId(resourceId);
		out.setMainEntityId(gloablId);
		
		//find all related complex resource
		Set<HAPResourceIdSimple> dependency = new HashSet<HAPResourceIdSimple>();
		buildDependencyGroup(gloablId.getRootResourceId(), dependency);
		for(HAPResourceIdSimple bundleId : dependency) {
			out.addDependency(bundleId);
		}
		
		HAPUtilityExport.exportExecutablePackage(out, this);
		return out;
	}

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
	
	public HAPExecutableBundle getComplexEntityResourceBundle(HAPResourceIdSimple complexEntityResourceId) {
		HAPExecutableBundle out = this.m_complexResourceBundles.get(complexEntityResourceId);
		if(out==null) {
			out = buildComplexEntityResourceBundle(complexEntityResourceId);
		}
		return out;
	}
	
	private HAPExecutableBundle buildComplexEntityResourceBundle(HAPResourceIdSimple complexEntityResourceId) {
		//build definition
		HAPDomainEntityDefinitionGlobal globalDefDomain = new HAPDomainEntityDefinitionGlobal(this.getDomainEntityDefinitionManager(), this.getResourceDefinitionManager());
		this.getResourceDefinitionManager().getResourceDefinition(complexEntityResourceId, globalDefDomain);
		//build executable
		HAPContextProcessor processContext = new HAPContextProcessor(new HAPExecutableBundle(complexEntityResourceId, globalDefDomain), m_runtimeEnv);
		this.process(processContext);
		return processContext.getCurrentComplexResourceBundle();
	}

	private void process(HAPContextProcessor processContext) {
		HAPExecutableBundle complexResourcePackage = processContext.getCurrentComplexResourceBundle();
		HAPDomainEntityDefinitionGlobal defDomain = complexResourcePackage.getDefinitionDomain();
		HAPDomainEntityExecutableResourceComplex exeDomain = complexResourcePackage.getExecutableDomain();
		
		//build complex entity tree
		HAPIdEntityInDomain rootEntityIdExe = buildExecutableTree(complexResourcePackage.getDefinitionRootEntityId(), processContext);
		
		//build attachment domain(build attachment tree, merge attachment value)
		HAPUtilityAttachment.buildAttachmentDomain(rootEntityIdExe, processContext);
		
		//process value structure
		HAPUtilityValueStructure.buildValueStructureDomain(rootEntityIdExe, processContext);
		
		//process entity
		HAPPluginComplexEntityProcessor processPlugin = this.m_processorPlugins.get(processContext.getCurrentComplexResourceId().getResourceType());
		processPlugin.process(complexResourcePackage.getExecutableRootEntityId(), processContext);
		
		//process adapter
		
	}

	private HAPManagerResourceDefinition getResourceDefinitionManager() {    return this.m_runtimeEnv.getResourceDefinitionManager();      }
	private HAPManagerDomainEntityDefinition getDomainEntityDefinitionManager() {     return this.m_runtimeEnv.getDomainEntityManager();      }
	
	private HAPIdEntityInDomain buildExecutableTree(HAPIdEntityInDomain complexEntityDefinitionId, HAPContextProcessor processContext) {
		HAPExecutableBundle complexResourcePackage = processContext.getCurrentComplexResourceBundle();
		HAPDomainEntityDefinitionGlobal defDomain = complexResourcePackage.getDefinitionDomain();
		HAPDomainEntityExecutableResourceComplex exeDomain = complexResourcePackage.getExecutableDomain();
		
		//create executable and add to domain
		HAPIdEntityInDomain out = null;
		HAPInfoEntityInDomainDefinition entityDefInfo = defDomain.getSolidEntityInfoDefinition(complexEntityDefinitionId, null);
		if(entityDefInfo.isSolid()) {
			HAPDefinitionEntityInDomainComplex complexEntityDef = (HAPDefinitionEntityInDomainComplex)entityDefInfo.getEntity();
			String entityType = complexEntityDef.getEntityType();
			HAPExecutableEntityComplex exeEntity = this.m_processorPlugins.get(entityType).newExecutable();
			HAPExtraInfoEntityInDomainExecutable exeExtraInfo = HAPUtilityDomain.buildExecutableExtraInfo(entityDefInfo);
			out = complexResourcePackage.addExecutableEntity(complexEntityDefinitionId, exeEntity, exeExtraInfo);
			HAPExecutableEntityComplex complexEntityExe = exeDomain.getEntityInfoExecutable(out).getEntity();
			
			//build executable for simple complex attribute
			Map<String, HAPEmbededEntity> simpleAttributes = complexEntityDef.getSimpleAttributes();
			for(String attrName : simpleAttributes.keySet()) {
				HAPIdEntityInDomain attrEntityDefId = simpleAttributes.get(attrName).getEntityId();
				HAPInfoEntityInDomainDefinition entityInfo = defDomain.getEntityInfoDefinition(attrEntityDefId);
				if(entityInfo.isComplexEntity()) {
					HAPIdEntityInDomain attrEntityExeId = buildExecutableTree(attrEntityDefId, processContext);
					complexEntityExe.setNormalComplexAttribute(attrName, attrEntityExeId);
				}
			}
			
			//build executable for container complex attribute
			Map<String, HAPContainerEntity> containerAttributes = complexEntityDef.getContainerAttributes();
			for(String attrName : containerAttributes.keySet()) {
				HAPContainerEntity defContainer = containerAttributes.get(attrName);
				HAPContainerEntity exeContainer = HAPUtilityDomain.buildContainer(defContainer.getContainerType());
				List<HAPInfoContainerElement> defEleInfos = defContainer.getAllElementsInfo();
				for(HAPInfoContainerElement defEleInfo : defEleInfos) {
					HAPIdEntityInDomain defEleId = defEleInfo.getEmbededElementEntity().getEntityId();
					HAPInfoEntityInDomainDefinition eleEntityInfo = defDomain.getEntityInfoDefinition(defEleId);
					if(eleEntityInfo.isComplexEntity()) {
						HAPIdEntityInDomain eleEntityExeId = buildExecutableTree(defEleId, processContext);
						HAPInfoContainerElement exeEleInfo = defEleInfo.cloneContainerElementInfo();
						exeEleInfo.setEmbededElementEntity(new HAPEmbededEntity(eleEntityExeId));
						exeContainer.addEntityElement(exeEleInfo);
					}
				}
				complexEntityExe.setContainerAttributeElementComplex(attrName, exeContainer);
			}
		}
		else {
			HAPIdComplexEntityInGlobal globalId = getComplexEntityGolbalIdResourceId(entityDefInfo.getResourceId());
			HAPExtraInfoEntityInDomainExecutable exeExtraInfo = HAPUtilityDomain.buildExecutableExtraInfo(entityDefInfo);
			out = complexResourcePackage.addExecutableEntity(complexEntityDefinitionId, globalId, exeExtraInfo);
			complexResourcePackage.addComplexResourceDependency(globalId.getRootResourceId());
		}
		
		return out;
	}
	
	public void registerProcessorPlugin(HAPPluginComplexEntityProcessor processorPlugin) {	this.m_processorPlugins.put(processorPlugin.getEntityType(), processorPlugin);	}
	
	private HAPIdComplexEntityInGlobal getComplexEntityGolbalIdResourceId(HAPResourceId resourceId) {
		HAPInfoResourceIdNormalize normalizedResourceInfo = this.getResourceDefinitionManager().normalizeResourceId(resourceId);
		HAPInfoEntityInDomainExecutable entityInfo = this.getComplexEntityResourceBundle(normalizedResourceInfo.getRootResourceIdSimple()).getEntityInfoExecutable(normalizedResourceInfo);
		return new HAPIdComplexEntityInGlobal(normalizedResourceInfo.getRootResourceIdSimple(), normalizedResourceInfo.getPath(), entityInfo.getEntityId());
	}
	

}

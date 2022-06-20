package com.nosliw.data.core.complex;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPBundleComplexResource;
import com.nosliw.data.core.domain.HAPContainerEntity;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.HAPEmbededEntity;
import com.nosliw.data.core.domain.HAPExtraInfoEntityInDomainExecutable;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoContainerElement;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPManagerDomainEntityDefinition;
import com.nosliw.data.core.domain.HAPPackageExecutable;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.HAPUtilityExport;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManagerComplexEntity {

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	private HAPGeneratorId m_idGenerator;

	//pulug in for processing complex entity
	private Map<String, HAPPluginComplexEntityProcessor> m_processorPlugins;

	private Map<String, HAPPluginAdapterProcessor> m_processorAdapterPlugins;
	
	
	private Map<HAPResourceIdSimple, HAPBundleComplexResource> m_complexResourceBundles;
	
	public HAPManagerComplexEntity(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_processorPlugins = new LinkedHashMap<String, HAPPluginComplexEntityProcessor>();
		this.m_complexResourceBundles = new LinkedHashMap<HAPResourceIdSimple, HAPBundleComplexResource>();
		this.m_idGenerator = new HAPGeneratorId();
	}

	public HAPPackageExecutable getExecutablePackage(HAPResourceId resourceId) {
		HAPPackageExecutable out = new HAPPackageExecutable();

		HAPInfoResourceIdNormalize normalizedResourceInfo = this.getResourceDefinitionManager().normalizeResourceId(resourceId);
		buildComplexEntityResourceBundle(new HAPContextProcessor(out.getComplexResourcePackageGroup(), (HAPResourceIdSimple)normalizedResourceInfo.getRootResourceId(), this.m_runtimeEnv));
		
		//figure out root entity
		
//		HAPResourceIdSimple mainResourceId = (HAPResourceIdSimple)normalizedResourceInfo.getRootResourceId();
//		HAPPackageComplexResource mainComplexResourcePackage = out.getComplexResourcePackage(mainResourceId);
//		mainComplexResourcePackage.getDefinitionDomain().getResourceDefinitionByResourceId(resourc);
//		
//		HAPUtilityDomain.getEntityDescent(entityId, p, globalDomain)
		
		HAPUtilityExport.exportExecutablePackage(out);
		
		return out;
	}

	public HAPBundleComplexResource getComplexEntityResourceBundle(HAPResourceIdSimple complexEntityResourceId) {
		HAPBundleComplexResource out = this.m_complexResourceBundles.get(complexEntityResourceId);
		if(out==null) {
			out = buildComplexEntityResourceBundle(complexEntityResourceId);
		}
		return out;
	}
	
	private HAPBundleComplexResource buildComplexEntityResourceBundle(HAPResourceIdSimple complexEntityResourceId) {
		HAPResourceIdSimple complexEntityResourceId = processContext.getCurrentComplexResourceId();
		HAPBundleComplexResource out = processContext.getCurrentComplexResourcePackage();
		if(out==null) {
			//if not load in package
			out = this.m_complexResourceBundles.get(complexEntityResourceId);
			if(out==null) {
				//build definition
				HAPDomainEntityDefinitionGlobal globalDefDomain = new HAPDomainEntityDefinitionGlobal(this.m_idGenerator, this.getDomainEntityDefinitionManager(), this.getResourceDefinitionManager());
				this.getResourceDefinitionManager().getResourceDefinition(complexEntityResourceId, globalDefDomain);
				out = new HAPBundleComplexResource(globalDefDomain, this.m_idGenerator);
				out.setRootResourceId(complexEntityResourceId);
				processContext.addComplexResourcePackage(out);
				//build executable
				this.process(processContext);
			}
			else {
				//if already generated
				processContext.addComplexResourcePackage(out);
			}
		}
		return out;
	}

	//build complex resource package within executable package
	private HAPBundleComplexResource buildComplexEntityResourceBundle(HAPContextProcessor processContext) { 
		HAPResourceIdSimple complexEntityResourceId = processContext.getCurrentComplexResourceId();
		HAPBundleComplexResource out = processContext.getCurrentComplexResourcePackage();
		if(out==null) {
			//if not load in package
			out = this.m_complexResourceBundles.get(complexEntityResourceId);
			if(out==null) {
				//build definition
				HAPDomainEntityDefinitionGlobal globalDefDomain = new HAPDomainEntityDefinitionGlobal(this.m_idGenerator, this.getDomainEntityDefinitionManager(), this.getResourceDefinitionManager());
				this.getResourceDefinitionManager().getResourceDefinition(complexEntityResourceId, globalDefDomain);
				out = new HAPBundleComplexResource(globalDefDomain, this.m_idGenerator);
				out.setRootResourceId(complexEntityResourceId);
				processContext.addComplexResourcePackage(out);
				//build executable
				this.process(processContext);
			}
			else {
				//if already generated
				processContext.addComplexResourcePackage(out);
			}
		}
		return out;
	}
	
	private void process(HAPContextProcessor processContext) {
		HAPBundleComplexResource complexResourcePackage = processContext.getCurrentComplexResourcePackage();
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
		HAPBundleComplexResource complexResourcePackage = processContext.getCurrentComplexResourcePackage();
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
			HAPInfoResourceIdNormalize normalizedResourceId = this.getResourceDefinitionManager().normalizeResourceId(entityDefInfo.getResourceId());
			buildComplexEntityResourceBundle(new HAPContextProcessor(processContext.getComplexResourcePackageGroup(), (HAPResourceIdSimple)normalizedResourceId.getRootResourceId()));
			HAPExtraInfoEntityInDomainExecutable exeExtraInfo = HAPUtilityDomain.buildExecutableExtraInfo(entityDefInfo);
			buildComplexEntityResourceBundle(HAPUtilityDomain.buildNewProcessorContext(processContext, normalizedResourceId.getRootResourceIdSimple()));
			out = complexResourcePackage.addExecutableEntity(complexEntityDefinitionId, normalizedResourceId, exeExtraInfo);
		}
		
		return out;
	}
	
	public void registerProcessorPlugin(HAPPluginComplexEntityProcessor processorPlugin) {	this.m_processorPlugins.put(processorPlugin.getEntityType(), processorPlugin);	}
}

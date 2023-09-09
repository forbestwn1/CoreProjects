package com.nosliw.data.core.domain.entity;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPExecutablePackage;
import com.nosliw.data.core.domain.HAPExtraInfoEntityInDomainExecutable;
import com.nosliw.data.core.domain.HAPIdComplexEntityInGlobal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainExecutable;
import com.nosliw.data.core.domain.HAPManagerDomainEntityDefinition;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.HAPUtilityEntityDefinition;
import com.nosliw.data.core.domain.HAPUtilityEntityExecutable;
import com.nosliw.data.core.domain.HAPUtilityExport;
import com.nosliw.data.core.domain.attachment.HAPUtilityAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachmentImpEntity;
import com.nosliw.data.core.domain.entity.data.HAPDefinitionEntityData;
import com.nosliw.data.core.domain.entity.expression.script.HAPDefinitionEntityExpressionScriptGroup;
import com.nosliw.data.core.domain.entity.expression.script.HAPExecutableEntityExpressionScriptGroup;
import com.nosliw.data.core.domain.entity.value.HAPDefinitionEntityValue;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityValueStructureDomain;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskTaskEntity;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.imp.rhino.task.HAPRuntimeTaskExecuteRhinoTaskEntity;

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
		//only process local entity
		if(processContext.getCurrentBundle().getExecutableDomain().getEntityInfoExecutable(exeEntityId).isLocalEntity()) {
			HAPPluginEntityProcessorComplex processPlugin = this.m_processorComplexEntityPlugins.get(exeEntityId.getEntityType());
			processPlugin.processEntity(exeEntityId, processContext);
		}
	}
	
	public HAPExecutableEntity processSimpleEntity(HAPIdEntityInDomain defEntityId, HAPContextProcessor processContext) {
		HAPPluginEntityProcessorSimple processPlugin = this.m_processorSimpleEntityPlugins.get(defEntityId.getEntityType());
		return processPlugin.processEntity(defEntityId, processContext);
	}
	
	public Object processEmbededAdapter(HAPInfoAdapter adapterDefinition, HAPExecutableEntity parentExe, Object childObj, HAPContextProcessor processContext){
		HAPPluginAdapterProcessor processPlugin = this.m_processorAdapterPlugins.get(adapterDefinition.getValueType());
		
		HAPExecutable childExe = null;
		HAPContextProcessor childProcessorContext = processContext;
		
		if(childObj instanceof HAPExecutable) {
			childExe = (HAPExecutable)childObj;
		}
		else if(childObj instanceof HAPIdEntityInDomain) {
			Pair<HAPExecutableEntity, HAPContextProcessor> result = HAPUtilityDomain.resolveExecutableEntityId((HAPIdEntityInDomain)childObj, processContext);
			childExe = result.getLeft();
			childProcessorContext = result.getRight();
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

		//solidate definition entity
		HAPUtilityEntityDefinition.solidateLocalResourceReference(resourceDef.getEntityId(), globalDefDomain, getResourceDefinitionManager());
		
		//build executable
		HAPContextProcessor processContext = new HAPContextProcessor(new HAPExecutableBundle(complexEntityResourceId, globalDefDomain), m_runtimeEnv);
		this.processBundle(processContext);
		return processContext.getCurrentBundle();
	}

	private void processBundle(HAPContextProcessor processContext) {
		HAPExecutableBundle complexResourcePackage = processContext.getCurrentBundle();
		
		//build complex entity tree
		HAPIdEntityInDomain rootEntityIdExe = (HAPIdEntityInDomain)buildExecutableTree(complexResourcePackage.getDefinitionRootEntityId(), processContext);
		
		//build attachment domain(build attachment tree, merge attachment value)
		HAPUtilityAttachment.buildAttachmentDomain(rootEntityIdExe, processContext);
		
		//build constant
		buildConstant(rootEntityIdExe, processContext);
		
		//calculate plain script expression value
		calculatePlainScriptExpression(rootEntityIdExe, processContext);
		
		//process value structure
		HAPUtilityValueStructureDomain.buildValueStructureDomain(rootEntityIdExe, processContext);
		
		HAPIdEntityInDomain exeEntityId = complexResourcePackage.getExecutableRootEntityId();
		if(processContext.getCurrentBundle().getExecutableDomain().getEntityInfoExecutable(exeEntityId).isLocalEntity()) {
			this.processComplexEntityInit(complexResourcePackage.getExecutableRootEntityId(), processContext);
		
			this.processComplexEntityValueContextExtension(complexResourcePackage.getExecutableRootEntityId(), processContext);
		
			HAPDomainValueStructure valueContextDomain = processContext.getCurrentValueStructureDomain();
			do {
				valueContextDomain.setIsDirty(false);
				this.processComplexEntityValueContextDiscovery(complexResourcePackage.getExecutableRootEntityId(), processContext);
			}while(valueContextDomain.getIsDirty()==true);
		
			this.processComplexEntityProcess(complexResourcePackage.getExecutableRootEntityId(), processContext);
		}
	}

	private void calculatePlainScriptExpression(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableComplexEntityTree(
				complexEntityExecutableId, 
				new HAPProcessorEntityExecutable() {

					private void calculatePlainScriptExpression(HAPIdEntityInDomain entityId, HAPContextProcessor processContext) {
						HAPExecutableEntityComplex executableEntity = processContext.getCurrentBundle().getExecutableDomain().getEntityInfoExecutable(entityId).getEntity();
						HAPIdEntityInDomain plainScriptExpressionEntityId = executableEntity.getComplexEntityAttributeValue(HAPExecutableEntityComplex.PLAINSCRIPTEEXPRESSIONGROUP);
						if(plainScriptExpressionEntityId!=null) {
							HAPExecutableEntityExpressionScriptGroup plainScriptExpressionGroupEntityExe = (HAPExecutableEntityExpressionScriptGroup)processContext.getCurrentExecutableDomain().getEntityInfoExecutable(plainScriptExpressionEntityId).getEntity();
							m_processorComplexEntityPlugins.get(plainScriptExpressionEntityId.getEntityType()).processEntity(plainScriptExpressionEntityId, processContext);

							//not auto process
							executableEntity.getAttribute(HAPExecutableEntityComplex.PLAINSCRIPTEEXPRESSIONGROUP).setAttributeAutoProcess(false);
							
							HAPIdEntityInDomain complexEntityDefinitionId = processContext.getCurrentBundle().getDefinitionEntityIdByExecutableEntityId(plainScriptExpressionEntityId);
							HAPDefinitionEntityExpressionScriptGroup plainScriptExpressionGroupEntityDef = (HAPDefinitionEntityExpressionScriptGroup)processContext.getCurrentDefinitionDomain().getEntityInfoDefinition(complexEntityDefinitionId).getEntity();
							if(plainScriptExpressionGroupEntityDef.getEntityElements().size()>0) {
								HAPInfoRuntimeTaskTaskEntity taskInfo = new HAPInfoRuntimeTaskTaskEntity(processContext.getCurrentBundle(), plainScriptExpressionEntityId, Object.class);
								HAPRuntimeTaskExecuteRhinoTaskEntity task = new HAPRuntimeTaskExecuteRhinoTaskEntity(taskInfo, processContext.getRuntimeEnvironment());
								HAPServiceData out = processContext.getRuntimeEnvironment().getRuntime().executeTaskSync(task);
								JSONObject resultJson = (JSONObject)out.getData();
								for(Object key : resultJson.keySet()) {
									String name = (String)key;
									executableEntity.setPlainScriptExpressionValue(name, resultJson.getString(name));
								}
							}
						}
					}
					
					@Override
					public void processComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext) {
						calculatePlainScriptExpression(entityId, processContext);
					}

					@Override
					public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute,	HAPContextProcessor processContext) {
						calculatePlainScriptExpression((HAPIdEntityInDomain)parentEntity.getAttribute(attribute).getValue().getValue(), processContext);
						if(attribute.equals(HAPExecutableEntityComplex.PLAINSCRIPTEEXPRESSIONGROUP))  return false;
						return true;
					}
				}, processContext);
	}
	
	private void buildConstant(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableComplexEntityTree(
				complexEntityExecutableId, 
				new HAPProcessorEntityExecutable() {

					private void buildConstant(HAPExecutableEntityComplex executableEntity) {
						{
							Map<String, HAPAttachment> attachments = processContext.getCurrentBundle().getAttachmentDomain().getAttachmentsByType(executableEntity.getAttachmentContainerId(), HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUE);
							for(String name : attachments.keySet()) {
								HAPAttachmentImpEntity attachmentEntity = (HAPAttachmentImpEntity)attachments.get(name);
								HAPDefinitionEntityValue valueEntity = (HAPDefinitionEntityValue)attachmentEntity.getEntity();
								executableEntity.addValueConstant(name, valueEntity.getValue());
							}
						}

						{
							Map<String, HAPAttachment> attachments = processContext.getCurrentBundle().getAttachmentDomain().getAttachmentsByType(executableEntity.getAttachmentContainerId(), HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATA);
							for(String name : attachments.keySet()) {
								HAPAttachmentImpEntity attachmentEntity = (HAPAttachmentImpEntity)attachments.get(name);
								HAPDefinitionEntityData dataEntity = (HAPDefinitionEntityData)attachmentEntity.getEntity();
								executableEntity.addDataConstant(name, dataEntity.getData());
							}
						}
					}
					
					@Override
					public void processComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext) {
						HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
						HAPExecutableEntityComplex executableEntity = currentBundle.getExecutableDomain().getEntityInfoExecutable(complexEntityExecutableId).getEntity();
						this.buildConstant(executableEntity);
					}

					@Override
					public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute,	HAPContextProcessor processContext) {
						HAPIdEntityInDomain childId = (HAPIdEntityInDomain)parentEntity.getAttributeValue(attribute);
						HAPExecutableEntityComplex executableEntity = processContext.getCurrentExecutableDomain().getEntityInfoExecutable(childId).getEntity();
						this.buildConstant(executableEntity);
						return true;
					}
			
				}, processContext);
	}
	
	private void processComplexEntityInit(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableEntityTree(
				complexEntityExecutableId, 
			new HAPProcessorEntityExecutable() {

				@Override
				public void processComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext) {
					m_processorComplexEntityPlugins.get(entityId.getEntityType()).processValueContext(entityId, processContext);
				}

				@Override
				public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute,	HAPContextProcessor processContext) {
					HAPAttributeEntityExecutable attr = parentEntity.getAttribute(attribute);
					String entityType = attr.getValueTypeInfo().getValueType();
					
					if(HAPUtilityEntityExecutable.isAttributeLocal(parentEntity, attribute, processContext)) {
						//only process local entity 
						if(attr.getValueTypeInfo().getIsComplex()) {
							HAPIdEntityInDomain attrEntityId = (HAPIdEntityInDomain)attr.getValue().getValue();
							m_processorComplexEntityPlugins.get(entityType).processValueContext(attrEntityId, processContext);
						}
						else {
							HAPExecutableEntity attrEntity = (HAPExecutableEntity)attr.getValue().getValue();
							m_processorSimpleEntityPlugins.get(entityType).process(attrEntity, processContext);
						}
					}
					return true;
				}

				@Override
				public void postProcessAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
					HAPAttributeEntityExecutable attr = parentEntity.getAttribute(attribute);
					String entityType = attr.getValueTypeInfo().getValueType();
					
					if(HAPUtilityEntityExecutable.isAttributeLocal(parentEntity, attribute, processContext)) {
						//only process local entity 
						if(attr.getValueTypeInfo().getIsComplex()) {
							HAPIdEntityInDomain attrEntityId = (HAPIdEntityInDomain)attr.getValue().getValue();
							m_processorComplexEntityPlugins.get(entityType).postProcessValueContext(attrEntityId, processContext);
						}
					}
					
					//adapter
					Pair<HAPExecutable, HAPContextProcessor> attributeResolved = HAPUtilityDomain.resolveAttributeExecutableEntity(parentEntity, attribute, processContext);
					for(HAPInfoAdapterExecutable adpater : attr.getValue().getExecutableAdapters()) {
						m_processorAdapterPlugins.get(adpater.getValueType()).preProcess(adpater.getExecutableEntityValue(), attributeResolved.getLeft(), attributeResolved.getRight(), parentEntity, processContext);
					}
				}

				@Override
				public void postProcessComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext) {
					m_processorComplexEntityPlugins.get(entityId.getEntityType()).postProcessValueContext(entityId, processContext);
				}
			}, 
			processContext);
	}

	private void processComplexEntityValueContextExtension(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableEntityTree(
				complexEntityExecutableId, 
			new HAPProcessorEntityExecutable() {

				@Override
				public void processComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext) {
					m_processorComplexEntityPlugins.get(entityId.getEntityType()).processValueContextExtension(entityId, processContext);
				}

				@Override
				public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute,	HAPContextProcessor processContext) {
					HAPAttributeEntityExecutable attr = parentEntity.getAttribute(attribute);
					String entityType = attr.getValueTypeInfo().getValueType();

					if(HAPUtilityEntityExecutable.isAttributeLocal(parentEntity, attribute, processContext)) {
						if(attr.getValueTypeInfo().getIsComplex()) {
							HAPIdEntityInDomain attrEntityId = (HAPIdEntityInDomain)attr.getValue().getValue();
							m_processorComplexEntityPlugins.get(entityType).processValueContextExtension(attrEntityId, processContext);
						}
					}
					return true;
				}

				@Override
				public void postProcessAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
					HAPAttributeEntityExecutable attr = parentEntity.getAttribute(attribute);
					String entityType = attr.getValueTypeInfo().getValueType();

					if(HAPUtilityEntityExecutable.isAttributeLocal(parentEntity, attribute, processContext)) {
						if(attr.getValueTypeInfo().getIsComplex()) {
							HAPIdEntityInDomain attrEntityId = (HAPIdEntityInDomain)attr.getValue().getValue();
							m_processorComplexEntityPlugins.get(entityType).postProcessValueContextExtension(attrEntityId, processContext);
						}
					}
					
					//adapter
					Pair<HAPExecutable, HAPContextProcessor> attributeResolved = HAPUtilityDomain.resolveAttributeExecutableEntity(parentEntity, attribute, processContext);
					for(HAPInfoAdapterExecutable adpater : attr.getValue().getExecutableAdapters()) {
						m_processorAdapterPlugins.get(adpater.getValueType()).processValueContextExtension(adpater.getExecutableEntityValue(), attributeResolved.getLeft(), attributeResolved.getRight(), parentEntity, processContext);
					}
				}

				@Override
				public void postProcessComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext) {
					m_processorComplexEntityPlugins.get(entityId.getEntityType()).postProcessValueContextExtension(entityId, processContext);
				}
			}, 
			processContext);
	}

	private void processComplexEntityValueContextDiscovery(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableEntityTree(
				complexEntityExecutableId, 
			new HAPProcessorEntityExecutable() {

				@Override
				public void processComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext) {
					m_processorComplexEntityPlugins.get(entityId.getEntityType()).processValueContextDiscovery(entityId, processContext);
				}

				@Override
				public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute,	HAPContextProcessor processContext) {
					HAPAttributeEntityExecutable attr = parentEntity.getAttribute(attribute);
					String entityType = attr.getValueTypeInfo().getValueType();

					if(HAPUtilityEntityExecutable.isAttributeLocal(parentEntity, attribute, processContext)) {
						if(attr.getValueTypeInfo().getIsComplex()) {
							HAPIdEntityInDomain attrEntityId = (HAPIdEntityInDomain)attr.getValue().getValue();
							m_processorComplexEntityPlugins.get(entityType).processValueContextDiscovery(attrEntityId, processContext);
						}
					}
					return true;
				}

				@Override
				public void postProcessAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
					HAPAttributeEntityExecutable attr = parentEntity.getAttribute(attribute);
					String entityType = attr.getValueTypeInfo().getValueType();

					if(HAPUtilityEntityExecutable.isAttributeLocal(parentEntity, attribute, processContext)) {
						if(attr.getValueTypeInfo().getIsComplex()) {
							HAPIdEntityInDomain attrEntityId = (HAPIdEntityInDomain)attr.getValue().getValue();
							m_processorComplexEntityPlugins.get(entityType).postProcessValueContextDiscovery(attrEntityId, processContext);
						}
					}
					
					//adapter
					Pair<HAPExecutable, HAPContextProcessor> attributeResolved = HAPUtilityDomain.resolveAttributeExecutableEntity(parentEntity, attribute, processContext);
					for(HAPInfoAdapterExecutable adpater : attr.getValue().getExecutableAdapters()) {
						m_processorAdapterPlugins.get(adpater.getValueType()).processValueContextDiscovery(adpater.getExecutableEntityValue(), attributeResolved.getLeft(), attributeResolved.getRight(), parentEntity, processContext);
					}
				}

				@Override
				public void postProcessComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext) {
					m_processorComplexEntityPlugins.get(entityId.getEntityType()).postProcessValueContextDiscovery(entityId, processContext);
				}
			}, 
			processContext);
	}

	private void processComplexEntityProcess(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableEntityTree(
			complexEntityExecutableId, 
			new HAPProcessorEntityExecutable() {

				@Override
				public void processComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext) {
					m_processorComplexEntityPlugins.get(entityId.getEntityType()).processEntity(entityId, processContext);
				}

				@Override
				public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute,	HAPContextProcessor processContext) {
					HAPAttributeEntityExecutable attr = parentEntity.getAttribute(attribute);
					String entityType = attr.getValueTypeInfo().getValueType();

					if(HAPUtilityEntityExecutable.isAttributeLocal(parentEntity, attribute, processContext)) {
						if(attr.getValueTypeInfo().getIsComplex()) {
							HAPIdEntityInDomain attrEntityId = (HAPIdEntityInDomain)attr.getValue().getValue();
							m_processorComplexEntityPlugins.get(entityType).processEntity(attrEntityId, processContext);
						}
					}
					return true;
				}

				@Override
				public void postProcessAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
					HAPAttributeEntityExecutable attr = parentEntity.getAttribute(attribute);
					String entityType = attr.getValueTypeInfo().getValueType();

					if(HAPUtilityEntityExecutable.isAttributeLocal(parentEntity, attribute, processContext)) {
						if(attr.getValueTypeInfo().getIsComplex()) {
							HAPIdEntityInDomain attrEntityId = (HAPIdEntityInDomain)attr.getValue().getValue();
							m_processorComplexEntityPlugins.get(entityType).postProcessEntity(attrEntityId, processContext);
						}
					}

					//adapter
					Pair<HAPExecutable, HAPContextProcessor> attributeResolved = HAPUtilityDomain.resolveAttributeExecutableEntity(parentEntity, attribute, processContext);
					for(HAPInfoAdapterExecutable adpater : attr.getValue().getExecutableAdapters()) {
						m_processorAdapterPlugins.get(adpater.getValueType()).postProcess(adpater.getExecutableEntityValue(), attributeResolved.getLeft(), attributeResolved.getRight(), parentEntity, processContext);
					}
				}

				@Override
				public void postProcessComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext) {
					m_processorComplexEntityPlugins.get(entityId.getEntityType()).postProcessEntity(entityId, processContext);
				}
			}, 
			processContext);
	}

	private HAPManagerResourceDefinition getResourceDefinitionManager() {    return this.m_runtimeEnv.getResourceDefinitionManager();      }
	private HAPManagerDomainEntityDefinition getDomainEntityDefinitionManager() {     return this.m_runtimeEnv.getDomainEntityDefinitionManager();      }
	
	private Object buildExecutableTree(HAPIdEntityInDomain entityDefinitionId, HAPContextProcessor processContext) {
		HAPExecutableBundle complexResourceBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal defDomain = complexResourceBundle.getDefinitionDomain();
		
		//create executable and add to domain
		Object out = null;
		HAPInfoEntityInDomainDefinition entityDefInfo = defDomain.getEntityInfoDefinition(entityDefinitionId);
		if(entityDefInfo.isSolid()) {
			HAPDefinitionEntityInDomain entityDef = entityDefInfo.getEntity();
			String entityType = entityDef.getEntityType();
			HAPExecutableEntity entityExe = null;
			if(entityDefInfo.isComplexEntity()) {
				entityExe = this.m_processorComplexEntityPlugins.get(entityType).newExecutable();
				HAPExtraInfoEntityInDomainExecutable exeExtraInfo = HAPUtilityDomain.buildExecutableExtraInfo(entityDefInfo);
				out = complexResourceBundle.addExecutableEntity((HAPExecutableEntityComplex)entityExe, exeExtraInfo);
			}
			else {
				entityExe = this.m_processorSimpleEntityPlugins.get(entityType).newExecutable();
				out = entityExe;
			}
			entityExe.setDefinitionEntityId(entityDefinitionId);
			
			List<HAPAttributeEntityDefinition> attrsDef = entityDef.getAttributes();
			for(HAPAttributeEntityDefinition attrDef : attrsDef) {
				if(attrDef.isAttributeAutoProcess()) {
					HAPEmbededDefinition embededAttributeDef = attrDef.getValue();

					HAPIdEntityInDomain attrEntityDefId = (HAPIdEntityInDomain)embededAttributeDef.getValue();
					Object attrEntityObject = buildExecutableTree(attrEntityDefId, processContext);
					entityExe.setAttribute(attrDef.getName(), new HAPEmbededExecutable(attrEntityObject), attrDef.getValueTypeInfo());
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

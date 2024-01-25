package com.nosliw.data.core.domain.entity;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPExecutablePackage;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
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
		HAPInfoResourceIdNormalize normalizedResourceId = normalizeResourceId(resourceId);
		out.setMainEntityId(normalizedResourceId);
		
		//find all related complex resource
		Set<HAPResourceIdSimple> dependency = new HashSet<HAPResourceIdSimple>();
		buildDependencyGroup(normalizedResourceId.getRootResourceIdSimple(), dependency);
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
		HAPExecutableBundle complexResourceBundle = processContext.getCurrentBundle();
		
		//build complex entity tree
		HAPExecutableEntityComplex rootComplexEntityExe = (HAPExecutableEntityComplex)buildExecutableTree(complexResourceBundle.getDefinitionRootEntityId(), processContext).getRight();
		complexResourceBundle.getExecutableDomain().setRootEntity(rootComplexEntityExe);
		
		//build parent path in entity
		populateParentPath(rootComplexEntityExe, processContext);
		
		//build attachment domain(build attachment tree, merge attachment value)
		HAPUtilityAttachment.buildAttachmentDomain(rootComplexEntityExe, processContext);
		
		//build custom constant value
		extendConstantValue(rootComplexEntityExe, processContext);
		
		//build constant
		buildConstant(rootComplexEntityExe, processContext);
		
		//calculate plain script expression value
		calculatePlainScriptExpression(rootComplexEntityExe, processContext);
		
		//process value structure
		HAPUtilityValueStructureDomain.buildValueStructureDomain(rootComplexEntityExe, processContext);

		this.processComplexEntityInit(rootComplexEntityExe, processContext);
		
		this.processComplexEntityValueContextExtension(rootComplexEntityExe, processContext);
	
		HAPDomainValueStructure valueContextDomain = processContext.getCurrentValueStructureDomain();
		do {
			valueContextDomain.setIsDirty(false);
			this.processComplexEntityValueContextDiscovery(rootComplexEntityExe, processContext);
		}while(valueContextDomain.getIsDirty()==true);
	
		//process entity
		this.processEntity(rootComplexEntityExe, processContext);
		
		//process adapter
		this.processAdapter(rootComplexEntityExe, processContext);
		
		HAPUtilityDomain.buildExternalResourceDependency(complexResourceBundle.getExecutableDomain(), processContext);
	}

	
	private void populateParentPath(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableTree(complexEntityExecutable, new HAPProcessorEntityExecutableDownwardImpAttribute() {

			@Override
			public void processRootEntity(HAPExecutableEntity rootEntity, HAPContextProcessor processContext) {	
				processContext.getCurrentBundle().setEntityPathById(rootEntity.getId(), new HAPPath().toString());
			}

			@Override
			public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute,	HAPContextProcessor processContext) {
				parentEntity.getAttribute(attribute).setParentEntity(attribute, parentEntity);
				
				HAPExecutableEntity entityExe =  parentEntity.getAttributeValueEntity(attribute);
				processContext.getCurrentBundle().setEntityPathById(entityExe.getId(), entityExe.getPathFromRoot().toString());

				return true;
			}
			
		}, processContext);
	}
	
	private void calculatePlainScriptExpression(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableComplexEntityTree(
				complexEntityExecutable, 
				new HAPProcessorEntityExecutableDownward() {

					private void calculatePlainScriptExpression(HAPExecutableEntityComplex parentExecutableEntity, HAPPath pathFromRoot, HAPContextProcessor processContext) {
						HAPExecutableEntityComplex plainScriptExpressionGroupEntityExe = parentExecutableEntity.getComplexEntityAttributeValue(HAPExecutableEntityComplex.PLAINSCRIPTEEXPRESSIONGROUP);
						if(plainScriptExpressionGroupEntityExe!=null) {
							m_processorComplexEntityPlugins.get(plainScriptExpressionGroupEntityExe.getEntityType()).processEntity(plainScriptExpressionGroupEntityExe, processContext);

							//not auto process
							parentExecutableEntity.getAttribute(HAPExecutableEntityComplex.PLAINSCRIPTEEXPRESSIONGROUP).setAttributeAutoProcess(false);
							
							HAPIdEntityInDomain complexEntityDefinitionId = plainScriptExpressionGroupEntityExe.getDefinitionEntityId(); 
							HAPDefinitionEntityExpressionScriptGroup plainScriptExpressionGroupEntityDef = (HAPDefinitionEntityExpressionScriptGroup)processContext.getCurrentDefinitionDomain().getEntityInfoDefinition(complexEntityDefinitionId).getEntity();
							if(plainScriptExpressionGroupEntityDef.getEntityElements().size()>0) {
								HAPInfoRuntimeTaskTaskEntity taskInfo = new HAPInfoRuntimeTaskTaskEntity(processContext.getCurrentBundle(), pathFromRoot, Object.class);
								HAPRuntimeTaskExecuteRhinoTaskEntity task = new HAPRuntimeTaskExecuteRhinoTaskEntity(taskInfo, processContext.getRuntimeEnvironment());
								HAPServiceData out = processContext.getRuntimeEnvironment().getRuntime().executeTaskSync(task);
								JSONObject resultJson = (JSONObject)out.getData();
								for(Object key : resultJson.keySet()) {
									String name = (String)key;
									parentExecutableEntity.setPlainScriptExpressionValue(name, resultJson.getString(name));
								}
							}
						}
					}
					
					@Override
					public boolean processEntityNode(HAPExecutableEntity rootEntity, HAPPath path, HAPContextProcessor processContext) {
						if(!this.isRoot(path)) {
							Pair<HAPExecutableEntity, String> attrInfo = this.getParentAttributeInfo(rootEntity, path);
							if(attrInfo.getRight().equals(HAPExecutableEntityComplex.PLAINSCRIPTEEXPRESSIONGROUP)) {
								this.calculatePlainScriptExpression((HAPExecutableEntityComplex)attrInfo.getLeft(), path, processContext);
								return false;
							}
							return true;
						}
						return true;
					}

					@Override
					public void postProcessEntityNode(HAPExecutableEntity rootEntity, HAPPath path,	HAPContextProcessor processContext) {	}
				}, processContext);
	}
	
	private void extendConstantValue(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableLocalComplexEntityTree(
				complexEntityExecutable, 
				new HAPProcessorEntityExecutableDownwardImpAttribute() {

					@Override
					public void processRootEntity(HAPExecutableEntity complexEntity, HAPContextProcessor processContext) {
						m_processorComplexEntityPlugins.get(complexEntity.getEntityType()).extendConstantValue((HAPExecutableEntityComplex)complexEntity, processContext);
					}

					@Override
					public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute,	HAPContextProcessor processContext) {
						HAPExecutableEntityComplex executableEntity = (HAPExecutableEntityComplex)parentEntity.getAttributeValueEntity(attribute);
						m_processorComplexEntityPlugins.get(executableEntity.getEntityType()).extendConstantValue(executableEntity, processContext);
						return true;
					}
			
				}, processContext);
	}
	
	private void buildConstant(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableLocalComplexEntityTree(
				complexEntityExecutable, 
				new HAPProcessorEntityExecutableDownwardImpAttribute() {

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
					public void processRootEntity(HAPExecutableEntity executableEntity, HAPContextProcessor processContext) {
						this.buildConstant((HAPExecutableEntityComplex)executableEntity);
					}

					@Override
					public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute,	HAPContextProcessor processContext) {
						HAPExecutableEntityComplex complexEntity = (HAPExecutableEntityComplex)parentEntity.getAttributeValueEntity(attribute);
						this.buildConstant(complexEntity);
						return true;
					}
			
				}, processContext);
	}
	
	private void processComplexEntityInit(HAPExecutableEntityComplex complexEntity, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableEntityTree(
				complexEntity, 
			new HAPProcessorEntityExecutableDownwardImpAttribute() {

				@Override
				public void processRootEntity(HAPExecutableEntity complexEntity, HAPContextProcessor processContext) {
					m_processorComplexEntityPlugins.get(complexEntity.getEntityType()).processValueContext((HAPExecutableEntityComplex)complexEntity, processContext);
				}

				@Override
				public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute,	HAPContextProcessor processContext) {
					HAPAttributeEntityExecutable attr = parentEntity.getAttribute(attribute);
					String entityType = attr.getValueTypeInfo().getValueType();
					
					if(HAPUtilityEntityExecutable.isAttributeLocal(parentEntity, attribute, processContext)) {
						//only process local entity 
						if(attr.getValueTypeInfo().getIsComplex()) {
							m_processorComplexEntityPlugins.get(entityType).processValueContext((HAPExecutableEntityComplex)parentEntity.getAttributeValueEntity(attribute), processContext);
						}
						else {
							m_processorSimpleEntityPlugins.get(entityType).process(parentEntity.getAttributeValueEntity(attribute), processContext);
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
							m_processorComplexEntityPlugins.get(entityType).postProcessValueContext((HAPExecutableEntityComplex)parentEntity.getAttributeValueEntity(attribute), processContext);
						}
					}
					
					//adapter
					Pair<HAPExecutable, HAPContextProcessor> attributeResolved = HAPUtilityDomain.resolveAttributeExecutableEntity(parentEntity, attribute, processContext);
					for(HAPInfoAdapterExecutable adpater : attr.getValue().getExecutableAdapters()) {
						m_processorAdapterPlugins.get(adpater.getValueType()).preProcess(adpater.getExecutableEntityValue(), attributeResolved.getLeft(), attributeResolved.getRight(), parentEntity, processContext);
					}
				}

				@Override
				public void postProcessRootEntity(HAPExecutableEntity complexEntity, HAPContextProcessor processContext) {
					m_processorComplexEntityPlugins.get(complexEntity.getEntityType()).postProcessValueContext((HAPExecutableEntityComplex)complexEntity, processContext);
				}
			}, 
			processContext);
	}

	private void processComplexEntityValueContextExtension(HAPExecutableEntityComplex complexEntity, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableEntityTree(
				complexEntity, 
			new HAPProcessorEntityExecutableDownwardImpAttribute() {

				@Override
				public void processRootEntity(HAPExecutableEntity complexEntity, HAPContextProcessor processContext) {
					m_processorComplexEntityPlugins.get(complexEntity.getEntityType()).processValueContextExtension((HAPExecutableEntityComplex)complexEntity, processContext);
				}

				@Override
				public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute,	HAPContextProcessor processContext) {
					HAPAttributeEntityExecutable attr = parentEntity.getAttribute(attribute);
					String entityType = attr.getValueTypeInfo().getValueType();

					if(HAPUtilityEntityExecutable.isAttributeLocal(parentEntity, attribute, processContext)) {
						if(attr.getValueTypeInfo().getIsComplex()) {
							m_processorComplexEntityPlugins.get(entityType).processValueContextExtension((HAPExecutableEntityComplex)parentEntity.getAttributeValueEntity(attribute), processContext);
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
							m_processorComplexEntityPlugins.get(entityType).postProcessValueContextExtension((HAPExecutableEntityComplex)parentEntity.getAttributeValueEntity(attribute), processContext);
						}
					}
					
					//adapter
					Pair<HAPExecutable, HAPContextProcessor> attributeResolved = HAPUtilityDomain.resolveAttributeExecutableEntity(parentEntity, attribute, processContext);
					for(HAPInfoAdapterExecutable adpater : attr.getValue().getExecutableAdapters()) {
						m_processorAdapterPlugins.get(adpater.getValueType()).processValueContextExtension(adpater.getExecutableEntityValue(), attributeResolved.getLeft(), attributeResolved.getRight(), parentEntity, processContext);
					}
				}

				@Override
				public void postProcessRootEntity(HAPExecutableEntity complexEntity, HAPContextProcessor processContext) {
					m_processorComplexEntityPlugins.get(complexEntity.getEntityType()).postProcessValueContextExtension((HAPExecutableEntityComplex)complexEntity, processContext);
				}
			}, 
			processContext);
	}

	private void processComplexEntityValueContextDiscovery(HAPExecutableEntityComplex complexEntity, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableEntityTree(
			complexEntity, 
			new HAPProcessorEntityExecutableDownwardImpAttribute() {

				@Override
				public void processRootEntity(HAPExecutableEntity complexEntity, HAPContextProcessor processContext) {
					m_processorComplexEntityPlugins.get(complexEntity.getEntityType()).processValueContextDiscovery((HAPExecutableEntityComplex)complexEntity, processContext);
				}

				@Override
				public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute,	HAPContextProcessor processContext) {
					HAPAttributeEntityExecutable attr = parentEntity.getAttribute(attribute);
					String entityType = attr.getValueTypeInfo().getValueType();

					if(HAPUtilityEntityExecutable.isAttributeLocal(parentEntity, attribute, processContext)) {
						if(attr.getValueTypeInfo().getIsComplex()) {
							m_processorComplexEntityPlugins.get(entityType).processValueContextDiscovery((HAPExecutableEntityComplex)parentEntity.getAttributeValueEntity(attribute), processContext);
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
							m_processorComplexEntityPlugins.get(entityType).postProcessValueContextDiscovery((HAPExecutableEntityComplex)parentEntity.getAttributeValueEntity(attribute), processContext);
						}
					}
					
					//adapter
					Pair<HAPExecutable, HAPContextProcessor> attributeResolved = HAPUtilityDomain.resolveAttributeExecutableEntity(parentEntity, attribute, processContext);
					for(HAPInfoAdapterExecutable adpater : attr.getValue().getExecutableAdapters()) {
						m_processorAdapterPlugins.get(adpater.getValueType()).processValueContextDiscovery(adpater.getExecutableEntityValue(), attributeResolved.getLeft(), attributeResolved.getRight(), parentEntity, processContext);
					}
				}

				@Override
				public void postProcessRootEntity(HAPExecutableEntity complexEntity, HAPContextProcessor processContext) {
					m_processorComplexEntityPlugins.get(complexEntity.getEntityType()).postProcessValueContextDiscovery((HAPExecutableEntityComplex)complexEntity, processContext);
				}
			}, 
			processContext);
	}

	private void processEntity(HAPExecutableEntityComplex complexEntity, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableEntityTree(
				complexEntity, 
			new HAPProcessorEntityExecutableDownwardImpAttribute() {

				@Override
				public void processRootEntity(HAPExecutableEntity complexEntity, HAPContextProcessor processContext) {
					m_processorComplexEntityPlugins.get(complexEntity.getEntityType()).processEntity((HAPExecutableEntityComplex)complexEntity, processContext);
				}

				@Override
				public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute,	HAPContextProcessor processContext) {
					HAPAttributeEntityExecutable attr = parentEntity.getAttribute(attribute);
					String entityType = attr.getValueTypeInfo().getValueType();

					if(HAPUtilityEntityExecutable.isAttributeLocal(parentEntity, attribute, processContext)) {
						if(attr.getValueTypeInfo().getIsComplex()) {
							m_processorComplexEntityPlugins.get(entityType).processEntity((HAPExecutableEntityComplex)parentEntity.getAttributeValueEntity(attribute), processContext);
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
							m_processorComplexEntityPlugins.get(entityType).postProcessEntity((HAPExecutableEntityComplex)parentEntity.getAttributeValueEntity(attribute), processContext);
						}
					}

					//adapter
					Pair<HAPExecutable, HAPContextProcessor> attributeResolved = HAPUtilityDomain.resolveAttributeExecutableEntity(parentEntity, attribute, processContext);
					for(HAPInfoAdapterExecutable adpater : attr.getValue().getExecutableAdapters()) {
						m_processorAdapterPlugins.get(adpater.getValueType()).postProcess(adpater.getExecutableEntityValue(), attributeResolved.getLeft(), attributeResolved.getRight(), parentEntity, processContext);
					}
				}

				@Override
				public void postProcessRootEntity(HAPExecutableEntity complexEntity, HAPContextProcessor processContext) {
					m_processorComplexEntityPlugins.get(complexEntity.getEntityType()).postProcessEntity((HAPExecutableEntityComplex)complexEntity, processContext);
				}
			}, 
			processContext);
	}

	private void processAdapter(HAPExecutableEntityComplex complexEntity, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableEntityTree(
				complexEntity, 
			new HAPProcessorEntityExecutableDownwardImpAttribute() {

				@Override
				public void processRootEntity(HAPExecutableEntity rootEntity, HAPContextProcessor processContext) {	}

				@Override
				public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute,	HAPContextProcessor processContext) {
					HAPAttributeEntityExecutable attr = parentEntity.getAttribute(attribute);
					//adapter
					Pair<HAPExecutable, HAPContextProcessor> attributeResolved = HAPUtilityDomain.resolveAttributeExecutableEntity(parentEntity, attribute, processContext);
					for(HAPInfoAdapterExecutable adpater : attr.getValue().getExecutableAdapters()) {
						m_processorAdapterPlugins.get(adpater.getValueType()).postProcess(adpater.getExecutableEntityValue(), attributeResolved.getLeft(), attributeResolved.getRight(), parentEntity, processContext);
					}
					return true;
				}
			}, 
			processContext);
	}

	private HAPManagerResourceDefinition getResourceDefinitionManager() {    return this.m_runtimeEnv.getResourceDefinitionManager();      }
	private HAPManagerDomainEntityDefinition getDomainEntityDefinitionManager() {     return this.m_runtimeEnv.getDomainEntityDefinitionManager();      }
	
	private Pair<String, Object> buildExecutableTree(HAPIdEntityInDomain entityDefinitionId, HAPContextProcessor processContext) {
		HAPExecutableBundle complexResourceBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal defDomain = complexResourceBundle.getDefinitionDomain();
		
		//create executable and add to domain
		Object embededValue = null;
		String embededValueType = null;
		HAPInfoEntityInDomainDefinition entityDefInfo = defDomain.getEntityInfoDefinition(entityDefinitionId);
		if(entityDefInfo.isSolid()) {
			HAPDefinitionEntityInDomain entityDef = entityDefInfo.getEntity();
			String entityType = entityDef.getEntityType();
			HAPExecutableEntity entityExe = null;
			if(entityDefInfo.isComplexEntity()) {
				entityExe = this.m_processorComplexEntityPlugins.get(entityType).newExecutable();
				((HAPExecutableEntityComplex)entityExe).setValueStructureDomain(complexResourceBundle.getValueStructureDomain());
			}
			else		entityExe = this.m_processorSimpleEntityPlugins.get(entityType).newExecutable();
			entityExe.setDefinitionEntityId(entityDefinitionId);
			embededValue = entityExe;
			embededValueType = HAPConstantShared.EMBEDEDVALUE_TYPE_ENTITY;
			
			List<HAPAttributeEntityDefinition> attrsDef = entityDef.getAttributes();
			for(HAPAttributeEntityDefinition attrDef : attrsDef) {
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

	private HAPInfoResourceIdNormalize normalizeResourceId(HAPResourceId resourceId) {
		return this.getResourceDefinitionManager().normalizeResourceId(resourceId);
	}
	
}

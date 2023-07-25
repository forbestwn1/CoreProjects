package com.nosliw.data.core.domain;

import java.util.List;
import java.util.Set;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.container.HAPContainerEntityExecutable;
import com.nosliw.data.core.domain.container.HAPElementContainer;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityExecutable;
import com.nosliw.data.core.domain.entity.HAPEmbededExecutable;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPInfoAdapter;
import com.nosliw.data.core.domain.entity.HAPProcessorEntityExecutable;
import com.nosliw.data.core.domain.entity.HAPProcessorEntityExecutable1;

public class HAPUtilityEntityExecutable {

	public static boolean isAttributeLocal(HAPExecutableEntity parentEntity, String attributeName, HAPContextProcessor processContext) {
		Object attrValue = parentEntity.getAttribute(attributeName).getValue().getValue();
		if(attrValue instanceof HAPIdEntityInDomain) {
			HAPInfoEntityInDomainExecutable entityInfo = processContext.getCurrentExecutableDomain().getEntityInfoExecutable((HAPIdEntityInDomain)attrValue);	
			return entityInfo.isLocalEntity();
		}
		else  return true;
	}
	
	//traverse only leaves that is local complex entity
	public static void traverseExecutableLocalComplexEntityTree(HAPIdEntityInDomain entityId, HAPProcessorEntityExecutable processor, HAPContextProcessor processContext) {
		traverseExecutableComplexEntityTree(
			entityId, 
			new HAPProcessorEntityExecutable() {
				@Override
				public void processComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext) {
					processor.processComplexRoot(entityId, processContext);
				}

				@Override
				public boolean process(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
					Object attrValue = parentEntity.getAttribute(attribute).getValue().getValue();
					if(attrValue instanceof HAPIdEntityInDomain) {
						HAPInfoEntityInDomainExecutable attrEntityInfo = processContext.getCurrentExecutableDomain().getEntityInfoExecutable((HAPIdEntityInDomain)attrValue);
						if(attrEntityInfo.getEntity()!=null) {
							return processor.process(parentEntity, attribute, processContext);
						}
					}
					return false;
				}
			}, 
			processContext);
	}
	
	//traverse only leafs that is complex entity
	public static void traverseExecutableComplexEntityTree(HAPIdEntityInDomain entityId, HAPProcessorEntityExecutable processor, HAPContextProcessor processContext) {
		traverseExecutableTree(
			entityId, 
			new HAPProcessorEntityExecutable() {
				@Override
				public void processComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext) {
					processor.processComplexRoot(entityId, processContext);
				}

				@Override
				public boolean process(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
					HAPAttributeEntityExecutable attr = parentEntity.getAttribute(attribute);
					if(attr.getValueTypeInfo().getIsComplex()) {
						return processor.process(parentEntity, attribute, processContext);
					}
					return false;
				}
			}, 
			processContext);
	}
	
	//traverse only entity leaves that marked as auto process
	public static void traverseExecutableEntityTree(HAPIdEntityInDomain entityId, HAPProcessorEntityExecutable processor, HAPContextProcessor processContext) {
		traverseExecutableTree(
			entityId, 
			new HAPProcessorEntityExecutable() {
				@Override
				public void processComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext) {
					processor.processComplexRoot(entityId, processContext);
				}

				@Override
				public boolean process(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
					HAPAttributeEntityExecutable attr = parentEntity.getAttribute(attribute);
					if(attr.isAttributeAutoProcess()) {
						Object attrValue = attr.getValue().getValue();
						if(attrValue instanceof HAPIdEntityInDomain || attrValue instanceof HAPExecutableEntity) {
							return processor.process(parentEntity, attribute, processContext);
						}
					}
					return false;
				}
			}, 
			processContext);
	}
	
	//traverse all leave (complex, simiple, solid, not solid ...)
	public static void traverseExecutableTree(HAPIdEntityInDomain entityId, HAPProcessorEntityExecutable processor, HAPContextProcessor processContext) {
		processor.processComplexRoot(entityId, processContext);
		traverseExecutableTree(processContext.getCurrentExecutableDomain().getEntityInfoExecutable(entityId).getEntity(), processor, processContext);
	}
	
	private static void traverseExecutableTree(HAPExecutableEntity parentEntity, HAPProcessorEntityExecutable processor, HAPContextProcessor processContext) {
		HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain(); 
		List<HAPAttributeEntityExecutable> attrsExe = parentEntity.getAttributes();
		for(HAPAttributeEntityExecutable attrExe : attrsExe) {
			if(processor.process(parentEntity, attrExe.getName(), processContext)) {
				HAPEmbededExecutable embeded = attrExe.getValue();
				Object attrValue = embeded.getValue();
				if(attrValue instanceof HAPIdEntityInDomain) {
					HAPInfoEntityInDomainExecutable attrEntityInfo = exeDomain.getEntityInfoExecutable((HAPIdEntityInDomain)attrValue);
					if(attrEntityInfo.getEntity()!=null) {
						traverseExecutableTree(attrEntityInfo.getEntity(), processor, processContext);
					}
				}
				else if(attrValue instanceof HAPExecutableEntity) {
					traverseExecutableTree((HAPExecutableEntity)attrValue, processor, processContext);
				}
			}
		}
	}
	

	
	
	
	public static void traverseExecutableComplexEntityTreeSolidOnly(HAPIdEntityInDomain entityId, HAPProcessorEntityExecutable1 processor, HAPContextProcessor processContext) {
		traverseExecutableComplexEntityTree1(entityId, new HAPProcessorEntityExecutable1() {
			@Override
			public void process(HAPInfoEntityInDomainExecutable entityInfo, Set<HAPInfoAdapter> adapters,	HAPInfoEntityInDomainExecutable parentEntityInfo, HAPContextProcessor processContext) {
				if(entityInfo.getEntity()!=null) {
					processor.process(entityInfo, adapters, parentEntityInfo, processContext);
				}
			}
		}, processContext);
	}

	public static void traverseExecutableComplexEntityTree1(HAPIdEntityInDomain entityId, HAPProcessorEntityExecutable1 processor, HAPContextProcessor processContext) {
		traverseExecutableComplexEntityTree1(processContext.getCurrentExecutableDomain().getEntityInfoExecutable(entityId), null, null, processor, processContext);
	}

	private static void traverseExecutableComplexEntityTree1(HAPInfoEntityInDomainExecutable entityInfo, Set<HAPInfoAdapter> adapter, HAPInfoEntityInDomainExecutable parentEntityInfo, HAPProcessorEntityExecutable1 processor, HAPContextProcessor processContext) {
		//process current entity
		processor.process(entityInfo, adapter, parentEntityInfo, processContext);
		
		HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain(); 

		HAPExecutableEntityComplex complexEntity = entityInfo.getEntity();
		if(complexEntity!=null) {
			List<HAPAttributeEntityExecutable> attrsExe = complexEntity.getAttributes();
			for(HAPAttributeEntityExecutable attrExe : attrsExe) {
				if(attrExe.getValueTypeInfo().getIsComplex()) {
					HAPEmbededExecutable complexSimpleAttrExe = attrExe.getValue();
					//process complex normal attribute
					HAPInfoEntityInDomainExecutable attrEntityInfo = exeDomain.getEntityInfoExecutable((HAPIdEntityInDomain)complexSimpleAttrExe.getValue());
					traverseExecutableComplexEntityTree1(attrEntityInfo, complexSimpleAttrExe.getAdapters(), entityInfo, processor, processContext);
				}
			}
		}
	}
	

	private static void traverseExecutableEntityTree(HAPInfoEntityInDomainExecutable entityInfo, Set<HAPInfoAdapter> adapter, HAPInfoEntityInDomainExecutable parentEntityInfo, HAPProcessorEntityExecutable1 processor, HAPContextProcessor processContext) {
		//process current entity
		processor.process(entityInfo, adapter, parentEntityInfo, processContext);
		
		HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain(); 

		HAPExecutableEntityComplex complexEntity = entityInfo.getEntity();
		if(complexEntity!=null) {
			List<HAPAttributeEntityExecutable> attrsExe = complexEntity.getAttributes();
			for(HAPAttributeEntityExecutable attrExe : attrsExe) {
				if(attrExe.getValueTypeInfo().getIsComplex()) {
					Object attrObj = attrExe.getValue();
					if(attrObj instanceof HAPContainerEntityExecutable) {
						//process container complex attribute
						HAPContainerEntityExecutable containerAttrExe = (HAPContainerEntityExecutable)attrObj;
						if(attrExe.getValueTypeInfo().getIsComplex()) {
							List<HAPElementContainer> eleInfos = containerAttrExe.getAllElements();
							for(HAPElementContainer eleInfo : eleInfos) {
								HAPEmbededExecutable eleEntity = (HAPEmbededExecutable)eleInfo.getEmbededElementEntity();
								HAPInfoEntityInDomainExecutable eleEntityInfo = exeDomain.getEntityInfoExecutable((HAPIdEntityInDomain)eleEntity.getValue()); 
								traverseExecutableComplexEntityTree1(eleEntityInfo, eleEntity.getAdapters(), entityInfo, processor, processContext);
							}
						}
					}
					else if(attrObj instanceof HAPEmbededExecutable) {
						//process complex normal attribute
						HAPEmbededExecutable complexSimpleAttrExe = (HAPEmbededExecutable)attrObj;
						HAPInfoEntityInDomainExecutable attrEntityInfo = exeDomain.getEntityInfoExecutable((HAPIdEntityInDomain)complexSimpleAttrExe.getValue());
						traverseExecutableComplexEntityTree1(attrEntityInfo, complexSimpleAttrExe.getAdapters(), entityInfo, processor, processContext);
					}
				}
			}
		}
	}
	
}

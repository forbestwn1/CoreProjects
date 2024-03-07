package com.nosliw.data.core.entity.division.manual;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.domain.valuecontext.HAPExecutableEntityValueContext;
import com.nosliw.data.core.domain.valuecontext.HAPWrapperExecutableValueStructure;
import com.nosliw.data.core.entity.HAPEntityBundle;
import com.nosliw.data.core.entity.HAPEntityBundleComplex;
import com.nosliw.data.core.entity.HAPEntityExecutableComplex;
import com.nosliw.data.core.entity.HAPProcessorEntityExecutableDownwardImpTreeNode;
import com.nosliw.data.core.entity.HAPTreeNode;
import com.nosliw.data.core.entity.HAPUtilityEntityExecutable;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPDefinitionEntityValueContext;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPDefinitionEntityWrapperValueStructure;

public class HAPUtilityValueStructureDomain {

	public static void buildValueStructureDomain(HAPEntityExecutableComplex complexEntity, HAPContextProcess processContext) {
		
		buildValueStructureComplexTree(complexEntity, processContext);
		
		buildExtensionValueStructure(complexEntity, processContext);
		
		normalizeValuePort(complexEntity, processContext);
		
		mergeValueStructure(complexEntity, processContext);
	}

	//build value structure in complex tree and add to value structure domain
	private static void buildValueStructureComplexTree(HAPEntityExecutableComplex complexEntity, HAPContextProcess processContext) {
		HAPUtilityEntityExecutable.traverseExecutableLocalComplexEntityTree(complexEntity, new HAPProcessorEntityExecutableDownwardImpTreeNode() {

			@Override
			protected boolean processTreeNode(HAPTreeNode treeNode, Object data) {
				HAPContextProcess processContext = (HAPContextProcess)data;
				HAPEntityBundleComplex bundle = (HAPEntityBundleComplex)processContext.getCurrentBundle();
				HAPManualEntityComplex rootEntityDef = (HAPManualEntityComplex)bundle.getExtraData();
				HAPManualInfoAttributeValueWithEntity attrValueInfoDef = (HAPManualInfoAttributeValueWithEntity)rootEntityDef.getDescendantValueInfo(treeNode.getPathFromRoot());
				HAPManualEntityComplex complexEntityDef = (HAPManualEntityComplex)attrValueInfoDef.getEntity();
				HAPDefinitionEntityValueContext valueContextEntityDef = complexEntityDef.getValueContextEntity();
				
				
				//value context
				HAPExecutableEntityValueContext valueContextExe = new HAPExecutableEntityValueContext();
				if(valueContextEntityDef!=null) {
					{
						List<HAPWrapperExecutableValueStructure> wrappers = new ArrayList<HAPWrapperExecutableValueStructure>();
						for(HAPDefinitionEntityWrapperValueStructure part : valueContextEntityDef.getValueStructures()) {
							HAPInfoEntityInDomainDefinition valueStructureDefInfo = definitionGlobalDomain.getEntityInfoDefinition(part.getValueStructureId());
							String valueStructureExeId = valueStructureDomain.newValueStructure((HAPDefinitionEntityValueStructure)valueStructureDefInfo.getEntity(), part.getInfo(), part.getName());
							HAPWrapperExecutableValueStructure valueStructureWrapperExe = new HAPWrapperExecutableValueStructure(valueStructureExeId);
							valueStructureWrapperExe.cloneFromDefinition(part);
							wrappers.add(valueStructureWrapperExe);

							//solidate plain script expression
							valueStructureDomain.getValueStructureDefInfoByRuntimeId(valueStructureExeId).getValueStructure().solidateConstantScript(complexEntityExe.getPlainScriptExpressionValues());
						}
						valueContextExe.addPartSimple(wrappers, HAPUtilityValueContext.createPartInfoDefault(), valueStructureDomain);
					}
				}
				complexEntityExe.setValueContext(valueContextExe);
				
			}


			
			
			private void process(HAPEntityExecutableComplex complexEntityExe, HAPContextProcess processContext) {
				
				HAPDefinitionEntityValueContext valueContextEntityDef = complexEntityDef.;
				
				
				
				
				HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();
				HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
				HAPDomainValueStructure valueStructureDomain = exeDomain.getValueStructureDomain();

				HAPIdEntityInDomain entityIdDef = complexEntityExe.getDefinitionEntityId();

				HAPInfoEntityInDomainDefinition complexEntityInfoDef = definitionGlobalDomain.getEntityInfoDefinition(entityIdDef);
				HAPManualEntityComplex complexEntityDef = (HAPManualEntityComplex)complexEntityInfoDef.getEntity();
				
				HAPDefinitionEntityValueContext valueContextEntityDef = null;
				HAPIdEntityInDomain valueContextEntityId = complexEntityDef.getValueContextEntityId();
				if(valueContextEntityId!=null) {
					valueContextEntityDef = (HAPDefinitionEntityValueContext)definitionGlobalDomain.getEntityInfoDefinition(valueContextEntityId).getEntity();
				}

				//value context
				HAPExecutableEntityValueContext valueContextExe = new HAPExecutableEntityValueContext();
				if(valueContextEntityDef!=null) {
					{
						List<HAPWrapperExecutableValueStructure> wrappers = new ArrayList<HAPWrapperExecutableValueStructure>();
						for(HAPDefinitionEntityWrapperValueStructure part : valueContextEntityDef.getValueStructures()) {
							HAPInfoEntityInDomainDefinition valueStructureDefInfo = definitionGlobalDomain.getEntityInfoDefinition(part.getValueStructureId());
							String valueStructureExeId = valueStructureDomain.newValueStructure((HAPDefinitionEntityValueStructure)valueStructureDefInfo.getEntity(), part.getInfo(), part.getName());
							HAPWrapperExecutableValueStructure valueStructureWrapperExe = new HAPWrapperExecutableValueStructure(valueStructureExeId);
							valueStructureWrapperExe.cloneFromDefinition(part);
							wrappers.add(valueStructureWrapperExe);

							//solidate plain script expression
							valueStructureDomain.getValueStructureDefInfoByRuntimeId(valueStructureExeId).getValueStructure().solidateConstantScript(complexEntityExe.getPlainScriptExpressionValues());
						}
						valueContextExe.addPartSimple(wrappers, HAPUtilityValueContext.createPartInfoDefault(), valueStructureDomain);
					}
				}
				complexEntityExe.setValueContext(valueContextExe);
			}
			
			@Override
			public void processRootEntity(HAPEntityExecutable complexEntity, Object data) {
				process((HAPEntityExecutableComplex)complexEntity, (HAPContextProcess)data);
			}

			@Override
			public boolean processAttribute(HAPEntityExecutable parentEntity, String attribute, Object data) {
				HAPEntityExecutableComplex parentComplexEntity = (HAPEntityExecutableComplex)parentEntity;
				process(parentComplexEntity.getComplexEntityAttributeValue(attribute), (HAPContextProcess)data);
				return true;
			}
		}, processContext);
	}

	private static void createExtensionPart(HAPExecutableEntityValueContext valueContextExe, HAPDomainValueStructure valueStructureDomain) {
		List<HAPWrapperExecutableValueStructure> wrappers = new ArrayList<HAPWrapperExecutableValueStructure>();
		for(String groupType : HAPUtilityValueStructure.getAllCategaries()) {
			String valueStructureExeId = valueStructureDomain.newValueStructure();
			HAPWrapperExecutableValueStructure valueStructureWrapperExe = new HAPWrapperExecutableValueStructure(valueStructureExeId);
			valueStructureWrapperExe.setGroupType(groupType);
			wrappers.add(valueStructureWrapperExe);
		}
		valueContextExe.addPartSimple(wrappers, HAPUtilityValueContext.createPartInfoExtension(), valueStructureDomain);
	}

	//create extension part
	private static void buildExtensionValueStructure(HAPExecutableEntityComplex complexEntity, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableLocalComplexEntityTree(complexEntity, new HAPProcessorEntityExecutableDownwardImpAttribute() {
			@Override
			public void processRootEntity(HAPExecutableEntity complexEntity, HAPContextProcessor processContext) {
				createExtensionPart(((HAPExecutableEntityComplex)complexEntity).getValueContext(), processContext.getCurrentValueStructureDomain());
			}

			@Override
			public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
				HAPExecutableEntityComplex parentComplexEntity = (HAPExecutableEntityComplex)parentEntity;

				HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();
				HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
				HAPDomainValueStructure valueStructureDomain = exeDomain.getValueStructureDomain();

				HAPExecutableEntityComplex entityExe = parentComplexEntity.getComplexEntityAttributeValue(attribute);
				HAPIdEntityInDomain entityIdDef = entityExe.getDefinitionEntityId();

				HAPExecutableEntityValueContext valueContext = entityExe.getValueContext();

				HAPInfoParentComplex parentInfo = definitionGlobalDomain.getComplexEntityParentInfo(entityIdDef);
				HAPConfigureProcessorValueStructure valueStructureConfig = parentInfo==null?null:parentInfo.getParentRelationConfigure().getValueStructureRelationMode();

				//extension value structure
				if(valueStructureConfig==null||!HAPConstantShared.INHERITMODE_RUNTIME.equals(valueStructureConfig.getInheritProcessorConfigure().getMode())) {
					createExtensionPart(valueContext, valueStructureDomain);
				}
				return true;
			}}, processContext);
	}

	
	//merge value structure between paren and child
	private static void mergeValueStructure(HAPExecutableEntityComplex complexEntity, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableLocalComplexEntityTree(complexEntity, new HAPProcessorEntityExecutableDownwardImpAttribute() {
			@Override
			public void processRootEntity(HAPExecutableEntity complexEntity, HAPContextProcessor processContext) {	}

			@Override
			public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
				HAPExecutableEntityComplex parentComplexEntity = (HAPExecutableEntityComplex)parentEntity;

				HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();
				HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
				HAPDomainValueStructure valueStructureDomain = exeDomain.getValueStructureDomain();

				HAPExecutableEntityComplex entityExe = parentComplexEntity.getComplexEntityAttributeValue(attribute);
				HAPIdEntityInDomain entityIdDef = entityExe.getDefinitionEntityId();

				HAPExecutableEntityValueContext valueContext = entityExe.getValueContext();

				HAPInfoParentComplex parentInfo = definitionGlobalDomain.getComplexEntityParentInfo(entityIdDef);
				HAPConfigureProcessorValueStructure valueStructureConfig = parentInfo==null?null:parentInfo.getParentRelationConfigure().getValueStructureRelationMode();

				HAPExecutableEntityValueContext parentValueContext = parentComplexEntity.getValueContext();
				
				//process static
				
				//process relative
				List<HAPInfoValueStructureSorting> valueStructureInfos = HAPUtilityValueContext.getAllValueStructures(valueContext);
				for(HAPInfoValueStructureSorting valueStructureInfo : valueStructureInfos) {
					HAPWrapperExecutableValueStructure valueStructureWrapper = valueStructureInfo.getValueStructure();
					HAPDefinitionEntityValueStructure valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(valueStructureWrapper.getValueStructureRuntimeId());
					List<HAPServiceData> errors = new ArrayList<HAPServiceData>();
					Set<HAPRefValuePort> dependency = new HashSet<HAPRefValuePort>();
					HAPUtilityProcessRelativeElement.processRelativeInStructure(valueStructure, valueStructureConfig==null?null:valueStructureConfig.getRelativeProcessorConfigure(), dependency, errors, processContext);
				}

				//inheritance
				processInteritance(valueContext, parentValueContext, valueStructureConfig.getInheritProcessorConfigure(), valueStructureDomain);
				return true;
			}}, processContext);
	}

	private static void processInteritance(HAPExecutableEntityValueContext valueContext, HAPExecutableEntityValueContext parentValueContext, HAPConfigureProcessorInherit valueStructureInheritConfig, HAPDomainValueStructure valueStructureDomain) {
		String inheritMode = valueStructureInheritConfig.getMode();
		if(!inheritMode.equals(HAPConstantShared.INHERITMODE_NONE)) {
			List<HAPExecutablePartValueContext> newParts = new ArrayList<HAPExecutablePartValueContext>();
			for(HAPExecutablePartValueContext part : parentValueContext.getParts()) {
				HAPExecutablePartValueContext newPart = part.inheritValueContextPart(valueStructureDomain, inheritMode, valueStructureInheritConfig.getGroupTypes());
				if(!newPart.isEmpty()) {
					newParts.add(newPart);
				}
			}
			valueContext.addPartGroup(newParts, HAPUtilityValueContext.createPartInfoFromParent());
		}
	}
	
	private static void normalizeValuePort(HAPExecutableEntityComplex complexEntity, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableLocalComplexEntityTree(complexEntity, new HAPProcessorEntityExecutableDownwardImpAttribute() {
			@Override
			public void processRootEntity(HAPExecutableEntity complexEntity, HAPContextProcessor processContext) {	}

			@Override
			public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
				HAPExecutableEntityComplex parentComplexEntity = (HAPExecutableEntityComplex)parentEntity;

				HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();
				HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
				HAPDomainValueStructure valueStructureDomain = exeDomain.getValueStructureDomain();

				HAPExecutableEntityComplex entityExe = parentComplexEntity.getComplexEntityAttributeValue(attribute);
				HAPIdEntityInDomain entityIdDef = entityExe.getDefinitionEntityId();

				HAPExecutableEntityValueContext valueContext = entityExe.getValueContext();

				HAPInfoParentComplex parentInfo = definitionGlobalDomain.getComplexEntityParentInfo(entityIdDef);
				HAPConfigureProcessorValueStructure valueStructureConfig = parentInfo==null?null:parentInfo.getParentRelationConfigure().getValueStructureRelationMode();

				HAPExecutableEntityValueContext parentValueContext = parentComplexEntity.getValueContext();
				
				//process static
				
				//process relative
				List<HAPInfoValueStructureSorting> valueStructureInfos = HAPUtilityValueContext.getAllValueStructures(valueContext);
				for(HAPInfoValueStructureSorting valueStructureInfo : valueStructureInfos) {
					HAPWrapperExecutableValueStructure valueStructureWrapper = valueStructureInfo.getValueStructure();
					HAPDefinitionEntityValueStructure valueStructure = valueStructureDomain.getValueStructureDefinitionByRuntimeId(valueStructureWrapper.getValueStructureRuntimeId());
					HAPUtilityValueStructure.traverseElement(valueStructure, new HAPProcessorStructureElement() {

						private void process(HAPElementStructureLeafRelative relativeEle) {
							HAPReferenceRootElement rootRef = relativeEle.getReference(); 
							HAPRefValuePort valuePortRef = rootRef.getValuePortRef();
							if(valuePortRef==null) {
								String valuePortName = rootRef.getValuePortName();
								HAPIdValuePort valuePortId = null;
								if(valuePortName==null) {
									valuePortId = HAPUtilityValuePort.getDefaultValuePortIdInEntity(parentComplexEntity);
								}
								else if(valuePortName.equals(HAPConstantShared.VALUEPORT_NAME_SELF)) {
									valuePortId = HAPUtilityValueContext.createValuePortIdValueContext(entityExe);
								}
								valuePortRef = new HAPRefValuePort(valuePortId);
								rootRef.setValuePortRef(valuePortRef);
							}
						}
						
						@Override
						public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
							HAPElementStructure defStructureElement = eleInfo.getElement();
							HAPElementStructure out = defStructureElement;
							switch(defStructureElement.getType()) {
							case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_DEFINITION:
							{
								process((HAPElementStructureLeafRelativeForDefinition)defStructureElement);
								break;
							}
							case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_VALUE:
							{
								process((HAPElementStructureLeafRelativeForValue)defStructureElement);
								break;
							}
							}
							
							return null;
						}

						@Override
						public void postProcess(HAPInfoElement eleInfo, Object value) {}
					}, null);
					
					List<HAPServiceData> errors = new ArrayList<HAPServiceData>();
					Set<HAPRefValuePort> dependency = new HashSet<HAPRefValuePort>();
					HAPUtilityProcessRelativeElement.processRelativeInStructure(valueStructure, valueStructureConfig==null?null:valueStructureConfig.getRelativeProcessorConfigure(), dependency, errors, processContext);
				}

				//inheritance
				processInteritance(valueContext, parentValueContext, valueStructureConfig.getInheritProcessorConfigure(), valueStructureDomain);
				return true;
			}}, processContext);
	}
}

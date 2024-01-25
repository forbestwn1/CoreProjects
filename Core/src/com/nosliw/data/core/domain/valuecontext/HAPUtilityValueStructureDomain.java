package com.nosliw.data.core.domain.valuecontext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPInfoParentComplex;
import com.nosliw.data.core.domain.HAPUtilityEntityExecutable;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.HAPProcessorEntityExecutableDownwardImpAttribute;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueContext;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionWrapperValueStructure;
import com.nosliw.data.core.domain.valueport.HAPIdValuePort;
import com.nosliw.data.core.domain.valueport.HAPReferenceRootElement;
import com.nosliw.data.core.domain.valueport.HAPUtilityValuePort;
import com.nosliw.data.core.structure.HAPElementStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelative;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelativeForDefinition;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelativeForValue;
import com.nosliw.data.core.structure.HAPInfoElement;
import com.nosliw.data.core.structure.HAPProcessorStructureElement;

public class HAPUtilityValueStructureDomain {

	public static void buildValueStructureDomain(HAPExecutableEntityComplex complexEntity, HAPContextProcessor processContext) {
		
		buildValueStructureComplexTree(complexEntity, processContext);
		
		buildExtensionValueStructure(complexEntity, processContext);
		
		normalizeValuePort(complexEntity, processContext);
		
		mergeValueStructure(complexEntity, processContext);
	}

	//build value structure in complex tree and add to value structure domain
	private static void buildValueStructureComplexTree(HAPExecutableEntityComplex complexEntity, HAPContextProcessor processContext) {
		HAPUtilityEntityExecutable.traverseExecutableLocalComplexEntityTree(complexEntity, new HAPProcessorEntityExecutableDownwardImpAttribute() {
			
			private void process(HAPExecutableEntityComplex complexEntityExe) {
				HAPDomainEntityDefinitionGlobal definitionGlobalDomain = processContext.getCurrentDefinitionDomain();
				HAPDomainEntityExecutableResourceComplex exeDomain = processContext.getCurrentExecutableDomain();
				HAPDomainValueStructure valueStructureDomain = exeDomain.getValueStructureDomain();

				HAPIdEntityInDomain entityIdDef = complexEntityExe.getDefinitionEntityId();

				HAPInfoEntityInDomainDefinition complexEntityInfoDef = definitionGlobalDomain.getEntityInfoDefinition(entityIdDef);
				HAPDefinitionEntityInDomainComplex complexEntityDef = (HAPDefinitionEntityInDomainComplex)complexEntityInfoDef.getEntity();
				
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
						for(HAPDefinitionWrapperValueStructure part : valueContextEntityDef.getValueStructures()) {
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
			public void processRootEntity(HAPExecutableEntity complexEntity, HAPContextProcessor processContext) {
				process((HAPExecutableEntityComplex)complexEntity);
			}

			@Override
			public boolean processAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {
				HAPExecutableEntityComplex parentComplexEntity = (HAPExecutableEntityComplex)parentEntity;
				process(parentComplexEntity.getComplexEntityAttributeValue(attribute));
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
					Set<HAPIdValuePort> dependency = new HashSet<HAPIdValuePort>();
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
				if(!newPart.isEmpty()) newParts.add(newPart);
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
							HAPIdValuePort valuePortId = rootRef.getValuePortId();
							if(valuePortId==null) {
								String valuePortName = rootRef.getValuePortName();
								if(valuePortName==null) {
									valuePortId = HAPUtilityValuePort.getDefaultValuePortIdInEntity(parentComplexEntity);
								}
								else if(valuePortName.equals(HAPConstantShared.VALUEPORT_NAME_SELF)) {
									valuePortId = HAPUtilityValuePort.createValuePortIdValueContext(entityExe);
								}
								rootRef.setValuePortId(valuePortId);
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
					Set<HAPIdValuePort> dependency = new HashSet<HAPIdValuePort>();
					HAPUtilityProcessRelativeElement.processRelativeInStructure(valueStructure, valueStructureConfig==null?null:valueStructureConfig.getRelativeProcessorConfigure(), dependency, errors, processContext);
				}

				//inheritance
				processInteritance(valueContext, parentValueContext, valueStructureConfig.getInheritProcessorConfigure(), valueStructureDomain);
				return true;
			}}, processContext);
	}
}

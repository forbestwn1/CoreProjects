package com.nosliw.data.core.domain.entity;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.common.constant.HAPWithConstantDefinition;
import com.nosliw.core.application.common.structure.HAPStructure1;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.division.manual.HAPManualBrickComplex;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachmentImpEntity;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.data.HAPDefinitionEntityData;
import com.nosliw.data.core.domain.entity.value.HAPDefinitionEntityValue;
import com.nosliw.data.core.value.HAPValue;

public class HAPUtilityComplexConstant {

	public static HAPData getConstantData(String constantName, HAPExecutableEntityComplex complexEntityExe, HAPContextProcessor processContext) {
		HAPData out = null;
		HAPAttachmentImpEntity attachment = (HAPAttachmentImpEntity)processContext.getCurrentBundle().getAttachmentDomain().getAttachment(complexEntityExe.getAttachmentContainerId(), HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATA, constantName);
		if(attachment!=null) {
			HAPDefinitionEntityData dataEntity = (HAPDefinitionEntityData)attachment.getEntity();
			out = dataEntity.getData();
		}
		return out;
	}

	public static Object getConstantValue(String constantName, HAPExecutableEntityComplex complexEntityExe, HAPContextProcessor processContext) {
		Object out = null;
		HAPAttachmentImpEntity attachment = (HAPAttachmentImpEntity)processContext.getCurrentBundle().getAttachmentDomain().getAttachment(complexEntityExe.getAttachmentContainerId(), HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUE, constantName);
		if(attachment!=null) {
			HAPDefinitionEntityValue valueEntity = (HAPDefinitionEntityValue)attachment.getEntity();
			out = valueEntity.getValue();
		}	
		return out;
	}
	
	public static Object getConstantValueOrData(String constantName, HAPExecutableEntityComplex complexEntityExe, HAPContextProcessor processContext) {
		Object out = null;
		out = getConstantValue(constantName, complexEntityExe, processContext);
		if(out==null)	out = getConstantData(constantName, complexEntityExe, processContext);
		return out;
	}
	
	
	
	
	public static Map<String, HAPData> getConstantsData(HAPManualBrickComplex complexEntityDef, HAPDomainValueStructure valueStructureDomain){
	
	}
	
	public static Map<String, HAPData> getConstantsData(HAPWithConstantDefinition defWithConstant, HAPStructure1 structure){
		Map<String, HAPData> out = new LinkedHashMap<String, HAPData>();
		for(HAPDefinitionConstant constantDef : getDataConstantsDefinition(defWithConstant, structure)) {
			HAPData data = constantDef.getData();
			if(data!=null)	out.put(constantDef.getId(), data);
		}
		return out;
	}
	
	public static Map<String, Object> getConstantsValue(HAPWithConstantDefinition defWithConstant, HAPStructure1 structure){
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		for(HAPDefinitionConstant constantDef : getValueConstantsDefinition(defWithConstant, structure)) {
			out.put(constantDef.getId(), constantDef.getValue());
		}
		return out;
	}
	
	public static Set<HAPDefinitionConstant> getDataConstantsDefinition(HAPWithConstantDefinition defWithConstant, HAPStructure1 structure){
		Set<HAPDefinitionConstant> out = null;
		out = defWithConstant.getConstantDefinitions();
		if(out==null) {
			//try to build constant from attachment and context
			if(defWithConstant instanceof HAPWithAttachment) {
				out = buildDataConstantDefinition(((HAPWithAttachment)defWithConstant).getAttachmentContainer(), structure);
			}
		}
		
		if(out==null)   throw new RuntimeException();
		return out;
	}

	public static Set<HAPDefinitionConstant> getValueConstantsDefinition(HAPWithConstantDefinition defWithConstant, HAPStructure1 structure){
		Set<HAPDefinitionConstant> out = null;
		out = defWithConstant.getConstantDefinitions();
		if(out==null) {
			//try to build constant from attachment and context
			if(defWithConstant instanceof HAPWithAttachment) {
				out = buildConstantDefinition(((HAPWithAttachment)defWithConstant).getAttachmentContainer(), structure);
			}
		}
		
		if(out==null)   throw new RuntimeException();
		return out;
	}

	public static Map<String, Object> getConstantsValue(HAPWithConstantDefinition withConstantDef){
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		for(HAPDefinitionConstant def : withConstantDef.getConstantDefinitions()) {
			out.put(def.getId(), def.getValue());
		}
		return out;
	}

	public static Map<String, HAPData> getConstantsData(HAPWithConstantDefinition withConstantDef){
		Map<String, HAPData> out = new LinkedHashMap<String, HAPData>();
		for(HAPDefinitionConstant def : withConstantDef.getConstantDefinitions()) {
			if(def.isData()) {
				out.put(def.getId(), def.getData());
			}
		}
		return out;
	}

	public static Set<HAPDefinitionConstant> buildConstantDefinition(HAPDefinitionEntityContainerAttachment attContainer, HAPStructure1 structure){
		Set<HAPDefinitionConstant> out = new HashSet<HAPDefinitionConstant>();
		out.addAll(buildConstantDefinition(attContainer));
		
		Map<String, Object> constantsValue = HAPUtilityStructure.discoverConstantValue(structure);
		for(String id : constantsValue.keySet()) {
			HAPDefinitionConstant constantDef = new HAPDefinitionConstant(id, constantsValue.get(id));
			out.add(constantDef);
		}
		return out;
	}

	public static Set<HAPDefinitionConstant> buildDataConstantDefinition(HAPDefinitionEntityContainerAttachment attContainer, HAPStructure1 context){
		return filterOutDataConstant(buildConstantDefinition(attContainer, context));
	}
	
	public static Set<HAPDefinitionConstant> buildConstantDefinition(HAPDefinitionEntityContainerAttachment attContainer){
		Set<HAPDefinitionConstant> out = new HashSet<HAPDefinitionConstant>();
		Map<String, HAPAttachment> attrs = attContainer.getAttachmentByType(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUE);
		for(String id : attrs.keySet()) {
			HAPDefinitionConstant constantDef = new HAPDefinitionConstant();
			HAPAttachmentImpEntity attr = (HAPAttachmentImpEntity)attrs.get(id);
			attr.cloneToEntityInfo(constantDef);
			HAPValue value = new HAPValue();
			value.buildObject(attr.getEntity(), HAPSerializationFormat.JSON);
			constantDef.setValue(value.getValue());
			out.add(constantDef);
		}
		return out;
	}

	public static Set<HAPDefinitionConstant> buildDataConstantDefinition(HAPDefinitionEntityContainerAttachment attContainer){
		return filterOutDataConstant(buildConstantDefinition(attContainer));
	}

	public static Map<String, Object> buildConstantValue(HAPDefinitionEntityContainerAttachment attContainer){
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		Set<HAPDefinitionConstant> constants = buildConstantDefinition(attContainer);
		for(HAPDefinitionConstant constant : constants) {
			out.put(constant.getId(), constant.getValue());
		}
		return out;
	}
	
	public static Map<String, HAPData> buildConstantData(HAPDefinitionEntityContainerAttachment attContainer){
		Map<String, HAPData> out = new LinkedHashMap<String, HAPData>();
		Set<HAPDefinitionConstant> constants = buildDataConstantDefinition(attContainer);
		for(HAPDefinitionConstant constant : constants) {
			out.put(constant.getId(), constant.getData());
		}
		return out;
	}
	
	private static Set<HAPDefinitionConstant> filterOutDataConstant(Set<HAPDefinitionConstant> constants){
		Set<HAPDefinitionConstant> out = new HashSet<HAPDefinitionConstant>();
		for(HAPDefinitionConstant constant : constants) {
			if(constant.isData()) {
				out.add(constant);
			}
		}
		return out;
		
	}

}

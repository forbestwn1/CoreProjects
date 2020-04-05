package com.nosliw.data.core;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.component.attachment.HAPAttachmentEntity;

public class HAPUtilityDataComponent {

	public static Map<String, HAPDefinitionConstant> buildConstantDefinition(HAPAttachmentContainer attContainer){
		Map<String, HAPDefinitionConstant> out = new LinkedHashMap<String, HAPDefinitionConstant>();
		Map<String, HAPAttachment> attrs = attContainer.getAttachmentByType(HAPConstant.RUNTIME_RESOURCE_TYPE_DATA);
		for(String name : attrs.keySet()) {
			HAPDefinitionConstant constantDef = new HAPDefinitionConstant();
			HAPAttachmentEntity attr = (HAPAttachmentEntity)attrs.get(name);
			attr.cloneToEntityInfo(constantDef);
			constantDef.setValue(attr.getEntity());
			out.put(constantDef.getName(), constantDef);
		}
		return out;
	}

	public static Map<String, HAPDefinitionConstant> buildDataConstantDefinition(HAPAttachmentContainer attContainer){
		Map<String, HAPDefinitionConstant> out = new LinkedHashMap<String, HAPDefinitionConstant>();
		Map<String, HAPDefinitionConstant> constants = buildConstantDefinition(attContainer);
		for(String name : constants.keySet()) {
			HAPDefinitionConstant constant = constants.get(name);
			if(constant.isData()) {
				out.put(name, constant);
			}
		}
		return out;
	}

	public static Map<String, Object> buildConstantValue(HAPAttachmentContainer attContainer){
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		Map<String, HAPDefinitionConstant> constants = buildConstantDefinition(attContainer);
		for(String name : constants.keySet()) {
			HAPDefinitionConstant constant = constants.get(name);
			out.put(name, constant.getValue());
		}
		return out;
	}
	
}

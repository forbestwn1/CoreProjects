package com.nosliw.uiresource.page.processor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.service.use.HAPExecutableServiceUse;
import com.nosliw.uiresource.page.execute.HAPExecutableUIBody;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;
import com.nosliw.uiresource.page.tag.HAPUITagId;
import com.nosliw.uiresource.page.tag.HAPManagerUITag;

public class HAPProcessorUIServiceEscalate {

	public static void process(HAPExecutableUIUnit exeUnit, HAPManagerUITag uiTagMan) {
		HAPExecutableUIBody body = exeUnit.getBody();
		if(HAPConstantShared.UIRESOURCE_TYPE_TAG.equals(exeUnit.getType())) {
			HAPExecutableUIUnitTag exeTag = (HAPExecutableUIUnitTag)exeUnit;
			if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(exeTag.getUIUnitTagDefinition().getTagName())).getContext().getInfo())) {
				Map<String, HAPExecutableServiceUse> mappedServiceDefs = new LinkedHashMap<String, HAPExecutableServiceUse>();
				
				Map<String, String> nameMapping = HAPNamingConversionUtility.parsePropertyValuePairs(exeTag.getAttributes().get(HAPConstantShared.UITAG_PARM_SERVICE));
				exeTag.setServiceMapping(nameMapping);
				Map<String, HAPExecutableServiceUse> exeServiceDefs = body.getServiceUses();
				for(String serviceName : exeServiceDefs.keySet()) {
					String mappedName = nameMapping.get(serviceName);
					if(mappedName==null)   mappedName = serviceName;
					mappedServiceDefs.put(mappedName, exeServiceDefs.get(serviceName));
				}
				escalate(exeTag.getParent(), mappedServiceDefs, uiTagMan);
			}
		}

		//child tag
		for(HAPExecutableUIUnitTag childTag : body.getUITags()) {
			process(childTag, uiTagMan);
		}
	}
	
	private static void escalate(HAPExecutableUIUnit exeUnit, Map<String, HAPExecutableServiceUse> servicesDef, HAPManagerUITag uiTagMan) {
		HAPExecutableUIBody body = exeUnit.getBody();
		if(HAPConstantShared.UIRESOURCE_TYPE_RESOURCE.equals(exeUnit.getType())){
			for(String serviceName : servicesDef.keySet()) {
				if(body.getServiceUse(serviceName)==null) {
					body.addServiceUse(serviceName, servicesDef.get(serviceName));
				}
			}
		}
		else {
			if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(((HAPExecutableUIUnitTag)exeUnit).getUIUnitTagDefinition().getTagName())).getContext().getInfo())) {
				escalate(exeUnit.getParent(), servicesDef, uiTagMan);
			}
		}
	}
}

package com.nosliw.uiresource.page.processor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.service.use.HAPExecutableServiceUse;
import com.nosliw.data.core.structure.temp.HAPUtilityContext;
import com.nosliw.ui.tag.HAPManagerUITag;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit1;
import com.nosliw.uiresource.page.execute.HAPExecutableUITag;
import com.nosliw.uiresource.page.tag.HAPUITagId;

public class HAPProcessorUIService {

	public static void escalate(HAPExecutableUIUnit1 exeUnit, HAPManagerUITag uiTagMan) {
		HAPExecutableUIUnit body = exeUnit.getBody();
		if(HAPConstantShared.UIRESOURCE_TYPE_TAG.equals(exeUnit.getType())) {
			HAPExecutableUITag exeTag = (HAPExecutableUITag)exeUnit;
			if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(exeTag.getUIUnitTagDefinition().getTagName())).getValueStructureDefinition().getInfo())) {
				Map<String, HAPExecutableServiceUse> mappedServiceDefs = new LinkedHashMap<String, HAPExecutableServiceUse>();
				
				Map<String, String> nameMapping = HAPUtilityNamingConversion.parsePropertyValuePairs(exeTag.getAttributes().get(HAPConstantShared.UITAG_PARM_SERVICE));
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
		for(HAPExecutableUITag childTag : body.getUITags()) {
			escalate(childTag, uiTagMan);
		}
	}
	
	private static void escalate(HAPExecutableUIUnit1 exeUnit, Map<String, HAPExecutableServiceUse> servicesDef, HAPManagerUITag uiTagMan) {
		HAPExecutableUIUnit body = exeUnit.getBody();
		if(HAPConstantShared.UIRESOURCE_TYPE_RESOURCE.equals(exeUnit.getType())){
			for(String serviceName : servicesDef.keySet()) {
				if(body.getServiceUse(serviceName)==null) {
					body.addServiceUse(serviceName, servicesDef.get(serviceName));
				}
			}
		}
		else {
			if(HAPUtilityContext.getContextGroupEscalateMode(uiTagMan.getUITagDefinition(new HAPUITagId(((HAPExecutableUITag)exeUnit).getUIUnitTagDefinition().getTagName())).getValueStructureDefinition().getInfo())) {
				escalate(exeUnit.getParent(), servicesDef, uiTagMan);
			}
		}
	}
}

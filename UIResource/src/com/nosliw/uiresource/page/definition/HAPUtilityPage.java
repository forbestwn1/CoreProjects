package com.nosliw.uiresource.page.definition;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPNameMapping;
import com.nosliw.data.core.service.definition.HAPManagerServiceDefinition;
import com.nosliw.data.core.service.use.HAPWithServiceUse;
import com.nosliw.data.core.structure.temp.HAPUtilityContext;
import com.nosliw.uiresource.page.tag.HAPManagerUITag;
import com.nosliw.uiresource.page.tag.HAPUITagId;

public class HAPUtilityPage {

	public static void solveServiceProvider(HAPDefinitionUIUnit uiUnitDef, HAPWithServiceUse parent, HAPManagerServiceDefinition serviceDefinitionMan) {
//		HAPUtilityService.solveServiceProvider(uiUnitDef, parent, uiUnitDef.getAttachmentContainer(), uiUnitDef.getNameMapping(), serviceDefinitionMan);
		
//		Map<String, HAPDefinitionServiceProvider> parentProviders = parent!=null?parent.getServiceProviderDefinitions() : new LinkedHashMap<String, HAPDefinitionServiceProvider>();
//		HAPNameMapping nameMapping = uiUnitDef.getNameMapping();
//		Map<String, HAPDefinitionServiceProvider> mappedParentProviders = (Map<String, HAPDefinitionServiceProvider>)nameMapping.mapEntity(parentProviders, HAPConstant.RUNTIME_RESOURCE_TYPE_SERVICE);
//		
//		Set<HAPDefinitionServiceProvider> providers = HAPUtilityServiceUse.buildServiceProvider(uiUnitDef.getExternalMapping(), mappedParentProviders, serviceDefinitionMan);
//		for(HAPDefinitionServiceProvider provider : providers)	uiUnitDef.getServiceDefinition().addServiceProviderDefinition(provider);
		
		for(HAPDefinitionUITag uiTag : uiUnitDef.getUITags()) {
//			solveServiceProvider(uiTag, uiUnitDef, serviceDefinitionMan);
		}
	}
	
	public static void buildUIUnitNameMapping(HAPDefinitionUIUnit uiUnitDef) {
		uiUnitDef.setNameMapping(HAPNameMapping.newNamingMapping(uiUnitDef.getAttributes().get(HAPConstantShared.UITAG_PARM_MAPPING)));
	}
	
	public static String getTagInheritableMode(String tagName, HAPManagerUITag uiTagMan) {
		return HAPUtilityContext.getContextGroupInheritMode(uiTagMan.getUITagDefinition(new HAPUITagId(tagName)).getValueStructureDefinition().getInfo());
	}

	public static Set<HAPDefinitionUITag> getUITagByName(HAPDefinitionUIUnit uiUnit, String tagName){
		Set<HAPDefinitionUITag> out = new HashSet<HAPDefinitionUITag>();
		getUITagByName(uiUnit, tagName, out);
		return out;
	}

	private static void getUITagByName(HAPDefinitionUIUnit uiUnit, String tagName, Set<HAPDefinitionUITag> out){
		if(HAPConstantShared.UIRESOURCE_TYPE_TAG.equals(uiUnit.getType())){
			HAPDefinitionUITag tagUnit = (HAPDefinitionUITag)uiUnit;
			if(tagUnit.getTagName().equals(tagName)){
				out.add(tagUnit);
			}
		}
		
		for(HAPDefinitionUITag tagUnit : uiUnit.getUITags()){
			getUITagByName(tagUnit, tagName, out);
		}
	}
}

package com.nosliw.uiresource.page.definition;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPAttachmentContainer;
import com.nosliw.data.core.component.HAPNameMapping;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.data.core.service.provide.HAPUtilityService;
import com.nosliw.data.core.service.use.HAPWithServiceUse;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.page.tag.HAPUITagId;
import com.nosliw.uiresource.page.tag.HAPUITagManager;

public class HAPUtilityPage {

	public static void solveServiceProvider(HAPDefinitionUIUnit uiUnitDef, HAPWithServiceUse parent, HAPManagerServiceDefinition serviceDefinitionMan) {
		HAPUtilityService.solveServiceProvider(uiUnitDef, parent, uiUnitDef.getAttachmentContainer(), uiUnitDef.getNameMapping(), serviceDefinitionMan);
		
//		Map<String, HAPDefinitionServiceProvider> parentProviders = parent!=null?parent.getServiceProviderDefinitions() : new LinkedHashMap<String, HAPDefinitionServiceProvider>();
//		HAPNameMapping nameMapping = uiUnitDef.getNameMapping();
//		Map<String, HAPDefinitionServiceProvider> mappedParentProviders = (Map<String, HAPDefinitionServiceProvider>)nameMapping.mapEntity(parentProviders, HAPConstant.RUNTIME_RESOURCE_TYPE_SERVICE);
//		
//		Set<HAPDefinitionServiceProvider> providers = HAPUtilityServiceUse.buildServiceProvider(uiUnitDef.getExternalMapping(), mappedParentProviders, serviceDefinitionMan);
//		for(HAPDefinitionServiceProvider provider : providers)	uiUnitDef.getServiceDefinition().addServiceProviderDefinition(provider);
		for(HAPDefinitionUITag uiTag : uiUnitDef.getUITags()) {
			solveServiceProvider(uiTag, uiUnitDef, serviceDefinitionMan);
		}
	}
	
	public static void buildUIUnitNameMapping(HAPDefinitionUIUnit uiUnitDef) {
		uiUnitDef.setNameMapping(HAPNameMapping.newNamingMapping(uiUnitDef.getAttributes().get(HAPConstant.UITAG_PARM_MAPPING)));
	}
	
	public static void solveExternalMapping(HAPDefinitionUIUnit uiUnitDef, HAPAttachmentContainer parentExternalMapping, HAPUITagManager uiTagMan) {
		buildUIUnitNameMapping(uiUnitDef);
		
		//if attribute has mapping, then do mapping first
		HAPAttachmentContainer mapped = uiUnitDef.getNameMapping().mapAttachment(parentExternalMapping);
		
		//get inherit mode
		String inheritableMode = HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT;
		if(HAPConstant.UIRESOURCE_TYPE_TAG.equals(uiUnitDef.getType())){
			inheritableMode = getTagInheritableMode(((HAPDefinitionUITag)uiUnitDef).getTagName(), uiTagMan);
		}		
		//merge
		uiUnitDef.getAttachmentContainer().merge(parentExternalMapping, inheritableMode);
		
		for(HAPDefinitionUITag uiTag : uiUnitDef.getUITags()) {
			solveExternalMapping(uiTag, uiUnitDef.getAttachmentContainer(), uiTagMan);
		}
	}
	
	private static String getTagInheritableMode(String tagName, HAPUITagManager uiTagMan) {
		return HAPUtilityContext.getContextGroupInheritMode(uiTagMan.getUITagDefinition(new HAPUITagId(tagName)).getContext().getInfo());
	}
	
	public static void getUITagByName(HAPDefinitionUIUnit resourceUnit, String tagName, Set<HAPDefinitionUITag> out){
		
		if(HAPConstant.UIRESOURCE_TYPE_TAG.equals(resourceUnit.getType())){
			HAPDefinitionUITag tagUnit = (HAPDefinitionUITag)resourceUnit;
			if(tagUnit.getTagName().equals(tagName)){
				out.add(tagUnit);
			}
		}
		
		for(HAPDefinitionUITag tagUnit : resourceUnit.getUITags()){
			getUITagByName(tagUnit, tagName, out);
		}
	}

	public static HAPDefinitionUIPage getPageDefinitionById(String id, HAPParserPage pageParser, HAPUIResourceManager uiResourceManager){
		String file = HAPFileUtility.getUIPageFolder()+id+".res";
		HAPDefinitionUIPage uiResourceDef = pageParser.parseFile(file);
		uiResourceDef = processInclude(uiResourceDef, pageParser, uiResourceManager);
		return uiResourceDef;
	}
	
	private static HAPDefinitionUIPage processInclude(HAPDefinitionUIPage uiResourceDef, HAPParserPage uiResourceParser, HAPUIResourceManager uiResourceMan) {
		Set<HAPDefinitionUITag> includeTags = new HashSet<HAPDefinitionUITag>();
		HAPUtilityPage.getUITagByName(uiResourceDef, HAPConstant.UITAG_NAME_INCLUDE, includeTags);
		for(HAPDefinitionUITag includeTagResource : includeTags){
			//include resource
			String includeResourceName = includeTagResource.getAttributes().get(HAPConstant.UITAG_NAME_INCLUDE_PARM_SOURCE);
			HAPDefinitionUIPage uiResource = getPageDefinitionById(includeResourceName, uiResourceParser, uiResourceMan);
			uiResourceParser.parseContent(includeTagResource, uiResource.getSource());
		}
		return uiResourceDef; 
	}
}

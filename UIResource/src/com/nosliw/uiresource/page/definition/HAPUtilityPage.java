package com.nosliw.uiresource.page.definition;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.component.HAPNameMapping;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.data.core.service.provide.HAPUtilityService;
import com.nosliw.data.core.service.use.HAPWithServiceUse;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.page.tag.HAPUITagId;
import com.nosliw.uiresource.page.tag.HAPManagerUITag;
import com.nosliw.uiresource.resource.HAPResourceIdUIResource;

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
	
	public static void solveAttachment(HAPDefinitionUIUnit uiUnitDef, HAPAttachmentContainer parentAttachment, HAPManagerUITag uiTagMan) {
		buildUIUnitNameMapping(uiUnitDef);
		
		//if attribute has mapping, then do mapping first
		HAPAttachmentContainer mapped = uiUnitDef.getNameMapping().mapAttachment(parentAttachment);
		
		//get inherit mode
		String inheritableMode = HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT;
		if(HAPConstant.UIRESOURCE_TYPE_TAG.equals(uiUnitDef.getType())){
			inheritableMode = getTagInheritableMode(((HAPDefinitionUITag)uiUnitDef).getTagName(), uiTagMan);
		}		
		//merge
		uiUnitDef.getAttachmentContainer().merge(parentAttachment, inheritableMode);
		
		for(HAPDefinitionUITag uiTag : uiUnitDef.getUITags()) {
			solveAttachment(uiTag, uiUnitDef.getAttachmentContainer(), uiTagMan);
		}
	}
	
	private static String getTagInheritableMode(String tagName, HAPManagerUITag uiTagMan) {
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

	public static HAPDefinitionUIPage processInclude(HAPDefinitionUIPage uiResourceDef, HAPParserPage uiResourceParser, HAPUIResourceManager uiResourceMan, HAPManagerResourceDefinition resourceDefManager) {
		Set<HAPDefinitionUITag> includeTags = new HashSet<HAPDefinitionUITag>();
		HAPUtilityPage.getUITagByName(uiResourceDef, HAPConstant.UITAG_NAME_INCLUDE, includeTags);
		for(HAPDefinitionUITag includeTagResource : includeTags){
			//include resource
			String includeResourceName = includeTagResource.getAttributes().get(HAPConstant.UITAG_NAME_INCLUDE_PARM_SOURCE);
//			HAPDefinitionUIPage uiResource = getPageDefinitionById(includeResourceName, uiResourceParser, uiResourceMan);
			HAPDefinitionUIPage uiResource = (HAPDefinitionUIPage)resourceDefManager.getResourceDefinition(new HAPResourceIdUIResource(includeResourceName));
			uiResource = processInclude(uiResource, uiResourceParser, uiResourceMan, resourceDefManager);
			uiResourceParser.parseAndBuildUIDefinition(includeTagResource, uiResource.getSource());
		}
		return uiResourceDef; 
	}
}

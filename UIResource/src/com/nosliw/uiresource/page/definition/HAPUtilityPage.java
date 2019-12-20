package com.nosliw.uiresource.page.definition;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.resource.external.HAPDefinitionExternalMapping;
import com.nosliw.data.core.resource.external.HAPDefinitionExternalMappingEle;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;
import com.nosliw.data.core.service.use.HAPWithServiceProvider;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.page.tag.HAPUITagId;
import com.nosliw.uiresource.page.tag.HAPUITagManager;

public class HAPUtilityPage {

	public static void solveServiceProvider(HAPDefinitionUIUnit uiUnitDef, HAPWithServiceProvider parent, HAPManagerServiceDefinition serviceDefinitionMan) {
		Map<String, HAPDefinitionServiceProvider> parentProviders = parent.getServiceProviderDefinitions();
		Map<String, HAPDefinitionServiceProvider> mappedParentProviders = parentProviders;
		Map<String, Map<String, String>> nameMapping = uiUnitDef.getNameMapping();
		if(nameMapping!=null && !nameMapping.isEmpty()) {
			Map<String, String> mapping = nameMapping.get(HAPConstant.RUNTIME_RESOURCE_TYPE_SERVICE);
			if(mapping!=null && !mapping.isEmpty()) {
				for(String pName : mapping.keySet()) {
					HAPDefinitionServiceProvider provider = parentProviders.get(pName).clone();
					provider.setName(mapping.get(pName));
					mappedParentProviders.put(mapping.get(pName), provider);
				}
			}
		}
		
		Set<HAPDefinitionServiceProvider> providers = HAPUtilityServiceUse.buildServiceProvider(uiUnitDef.getExternalMapping(), mappedParentProviders, serviceDefinitionMan);
		for(HAPDefinitionServiceProvider provider : providers)	uiUnitDef.getServiceDefinition().addServiceProviderDefinition(provider);
		for(HAPDefinitionUITag uiTag : uiUnitDef.getUITags()) {
			solveServiceProvider(uiTag, uiUnitDef, serviceDefinitionMan);
		}
	}
	
	public static void buildUIUnitNameMapping(HAPDefinitionUIUnit uiUnitDef) {
		Map<String, String> nameMapping = HAPNamingConversionUtility.parsePropertyValuePairs(uiUnitDef.getAttributes().get(HAPConstant.UITAG_PARM_MAPPING));
		if(nameMapping!=null) {
			for(String complexName : nameMapping.keySet()) {
				String[] segs = HAPNamingConversionUtility.parseNameSegments(complexName);
				String type = segs[0];
				String name = segs[1];
				uiUnitDef.addNameMapping(type, nameMapping.get(complexName), name);
			}
		}
	}
	
	public static void solveExternalMapping(HAPDefinitionUIUnit uiUnitDef, HAPDefinitionExternalMapping parentExternalMapping, HAPUITagManager uiTagMan) {
		buildUIUnitNameMapping(uiUnitDef);
		
		//if attribute has mapping, then do mapping first
		HAPDefinitionExternalMapping mapped;
		Map<String, Map<String, String>> nameMapping = uiUnitDef.getNameMapping();
		if(nameMapping==null || nameMapping.isEmpty())  mapped = parentExternalMapping;
		else {
			mapped = new HAPDefinitionExternalMapping();
			for(String type : nameMapping.keySet()) {
				Map<String, String> byParentName = nameMapping.get(type);
				for(String parentName : byParentName.keySet()) {
					String childName = byParentName.get(parentName);
					HAPDefinitionExternalMappingEle ele = parentExternalMapping.getElement(type, parentName);
					ele = ele.clone();
					ele.setName(childName);
					mapped.addElement(type, ele);
				}
			}
		}
		
		//get inherit mode
		String inheritableMode = HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT;
		if(HAPConstant.UIRESOURCE_TYPE_TAG.equals(uiUnitDef.getType())){
			inheritableMode = getTagInheritableMode(((HAPDefinitionUITag)uiUnitDef).getTagName(), uiTagMan);
		}		
		//merge
		uiUnitDef.getExternalMapping().merge(parentExternalMapping, inheritableMode);
		
		for(HAPDefinitionUITag uiTag : uiUnitDef.getUITags()) {
			solveExternalMapping(uiTag, uiUnitDef.getExternalMapping(), uiTagMan);
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

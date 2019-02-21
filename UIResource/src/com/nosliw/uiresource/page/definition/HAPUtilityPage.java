package com.nosliw.uiresource.page.definition;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.uiresource.HAPUIResourceManager;

public class HAPUtilityPage {

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
		String file = HAPFileUtility.getUIResourceFolder()+id+".res";
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

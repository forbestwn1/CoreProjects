package com.nosliw.uiresource;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.uiresource.module.HAPDefinitionModule;
import com.nosliw.uiresource.module.HAPParserModule;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitResource;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitTag;
import com.nosliw.uiresource.page.definition.HAPParserUIResource;
import com.nosliw.uiresource.page.definition.HAPUIDefinitionUnitUtility;

public class HAPUtilityUIResource {

	public static HAPDefinitionModule getUIModuleDefinitionById(String id, HAPParserModule moduleParser, HAPUIResourceManager uiResourceManager){
		String file = HAPFileUtility.getUIModuleFolder()+id+".res";
		HAPDefinitionModule moduleDef = moduleParser.parseFile(file);
		return moduleDef;
	}

	
	public static HAPDefinitionUIUnitResource getUIResourceDefinitionById(String id, HAPParserUIResource uiResourceParser, HAPUIResourceManager uiResourceManager){
		String file = HAPFileUtility.getUIResourceFolder()+id+".res";
		HAPDefinitionUIUnitResource uiResourceDef = uiResourceParser.parseFile(file);
		uiResourceDef = processInclude(uiResourceDef, uiResourceParser, uiResourceManager);
		return uiResourceDef;
	}
	
	private static HAPDefinitionUIUnitResource processInclude(HAPDefinitionUIUnitResource uiResourceDef, HAPParserUIResource uiResourceParser, HAPUIResourceManager uiResourceMan) {
		Set<HAPDefinitionUIUnitTag> includeTags = new HashSet<HAPDefinitionUIUnitTag>();
		HAPUIDefinitionUnitUtility.getUITagByName(uiResourceDef, HAPConstant.UITAG_NAME_INCLUDE, includeTags);
		for(HAPDefinitionUIUnitTag includeTagResource : includeTags){
			//include resource
			String includeResourceName = includeTagResource.getAttributes().get(HAPConstant.UITAG_NAME_INCLUDE_PARM_SOURCE);
			HAPDefinitionUIUnitResource uiResource = getUIResourceDefinitionById(includeResourceName, uiResourceParser, uiResourceMan);
			uiResourceParser.parseContent(includeTagResource, uiResource.getSource());
		}
		return uiResourceDef; 
	}


}

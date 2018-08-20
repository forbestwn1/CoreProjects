package com.nosliw.uiresource.page.definition;

import java.util.Set;

import com.nosliw.common.utils.HAPConstant;

public class HAPUIDefinitionUnitUtility {

	public static void getUITagByName(HAPDefinitionUIUnit resourceUnit, String tagName, Set<HAPDefinitionUIUnitTag> out){
		
		if(HAPConstant.UIRESOURCE_TYPE_TAG.equals(resourceUnit.getType())){
			HAPDefinitionUIUnitTag tagUnit = (HAPDefinitionUIUnitTag)resourceUnit;
			if(tagUnit.getTagName().equals(tagName)){
				out.add(tagUnit);
			}
		}
		
		for(HAPDefinitionUIUnitTag tagUnit : resourceUnit.getUITags()){
			getUITagByName(tagUnit, tagName, out);
		}
	}
	
}

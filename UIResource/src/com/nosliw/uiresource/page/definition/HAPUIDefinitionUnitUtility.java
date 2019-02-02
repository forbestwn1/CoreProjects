package com.nosliw.uiresource.page.definition;

import java.util.Set;

import com.nosliw.common.utils.HAPConstant;

public class HAPUIDefinitionUnitUtility {

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
	
}

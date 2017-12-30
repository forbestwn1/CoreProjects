package com.nosliw.uiresource.definition;

import java.util.Set;

import com.nosliw.common.utils.HAPConstant;

public class HAPUIDefinitionUnitUtility {

	public static void getUITagByName(HAPUIDefinitionUnit resourceUnit, String tagName, Set<HAPUIDefinitionUnitTag> out){
		
		if(HAPConstant.UIRESOURCE_TYPE_TAG.equals(resourceUnit.getType())){
			HAPUIDefinitionUnitTag tagUnit = (HAPUIDefinitionUnitTag)resourceUnit;
			if(tagUnit.getTagName().equals(tagName)){
				out.add(tagUnit);
			}
		}
		
		for(HAPUIDefinitionUnitTag tagUnit : resourceUnit.getUITags()){
			getUITagByName(tagUnit, tagName, out);
		}
	}
	
}

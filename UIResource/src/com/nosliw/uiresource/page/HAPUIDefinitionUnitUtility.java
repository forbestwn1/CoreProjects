package com.nosliw.uiresource.page;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.constant.HAPConstantDef;

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

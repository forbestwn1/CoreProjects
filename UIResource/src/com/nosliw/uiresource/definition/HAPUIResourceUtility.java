package com.nosliw.uiresource.definition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.expression.HAPExpression;

public class HAPUIResourceUtility {

	public static List<HAPExpression> discoverExpressionsInUIResource(HAPUIDefinitionUnitResource uiResource){
		List<HAPExpression> out = new ArrayList<HAPExpression>();
		discoverExpressionsInUIResourceUnit(uiResource, out);		
		return out;
	}
	
	private static void discoverExpressionsInUIResourceUnit(HAPUIDefinitionUnit uiResourceUnit, List<HAPExpression> out){
		Map<String, HAPExpression> expressions = uiResourceUnit.getExpressionUnit().getExpressions();
		out.addAll(expressions.values());
		
		Iterator<HAPUIDefinitionUnitTag> it = uiResourceUnit.getUITags().iterator();
		while(it.hasNext()){
			discoverExpressionsInUIResourceUnit(it.next(), out);
		}
	}
	
}

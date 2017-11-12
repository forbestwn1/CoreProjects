package com.nosliw.uiresource.resource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.uiresource.definition.HAPEmbededScriptExpression;
import com.nosliw.uiresource.definition.HAPEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.definition.HAPEmbededScriptExpressionInContent;
import com.nosliw.uiresource.definition.HAPUIDefinitionUnit;
import com.nosliw.uiresource.definition.HAPUIDefinitionUnitResource;
import com.nosliw.uiresource.definition.HAPUIDefinitionUnitTag;
import com.nosliw.uiresource.expression.HAPScriptExpression;

public class HAPResourceUtility {

	public static void processResourceDependency(HAPUIDefinitionUnitResource uiResource, HAPResourceManagerRoot resourceMan){
		 Set<HAPResourceId> dependencyResourceIds = new LinkedHashSet();
		
		//resources need by expression
		List<HAPExpression> expressions = discoverExpressionsInUIResource(uiResource);
		for(HAPExpression exp : expressions){
			List<HAPResourceId> expressionDependency = HAPExpressionUtility.discoverResources(exp);
			dependencyResourceIds.addAll(expressionDependency);
		}
		
		//resource need by tag
		
		Iterator<HAPResourceId> it = dependencyResourceIds.iterator();
		while(it.hasNext()){
			HAPResourceId resourceId = it.next();
			uiResource.getResourceDependency().add(new HAPResourceDependent(resourceId));
		}
	}
	
	private static List<HAPExpression> discoverExpressionsInUIResource(HAPUIDefinitionUnitResource uiResource){
		List<HAPExpression> out = new ArrayList<HAPExpression>();
		discoverExpressionsInUIResourceUnit(uiResource, out);		
		return out;
	}
	
	private static void discoverExpressionsInUIResourceUnit(HAPUIDefinitionUnit uiResourceUnit, List<HAPExpression> out){
		out.addAll(getExpressions(uiResourceUnit));
		
		Iterator<HAPUIDefinitionUnitTag> it = uiResourceUnit.getUITags().iterator();
		while(it.hasNext()){
			discoverExpressionsInUIResourceUnit(it.next(), out);
		}
	}
	
	private static Set<HAPExpression> getExpressions(HAPUIDefinitionUnit uiDefinitionUnit){
		Set<HAPExpression> all = new HashSet<HAPExpression>();
		for(HAPEmbededScriptExpressionInContent scriptExpressionInContent : uiDefinitionUnit.getScriptExpressionsInContent())		all.addAll(getExpressions(scriptExpressionInContent));
		for(HAPEmbededScriptExpressionInAttribute scriptExpressionInAttribute : uiDefinitionUnit.getScriptExpressionsInAttributes())    all.addAll(getExpressions(scriptExpressionInAttribute));
		for(HAPEmbededScriptExpressionInAttribute scriptExpressionInAttribute : uiDefinitionUnit.getScriptExpressionsInTagAttributes())		all.addAll(getExpressions(scriptExpressionInAttribute));
		return all;
	}
	
	private static Set<HAPExpression> getExpressions(HAPEmbededScriptExpression embededScriptExpression){
		Set<HAPExpression> out = new HashSet<HAPExpression>();
		for(HAPScriptExpression scriptExpression : embededScriptExpression.getScriptExpressions()){
			out.addAll(scriptExpression.getExpressions().values());
		}
		return out;
	}
}

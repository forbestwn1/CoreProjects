package com.nosliw.uiresource.definition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.uiresource.HAPEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.HAPEmbededScriptExpressionInContent;
import com.nosliw.uiresource.HAPUIResourceIdGenerator;
import com.nosliw.uiresource.expression.HAPUIResourceExpressionUnit;

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
	
	/**
	 * Get all expression definitions in ui definition (content, attribute, expression block)
	 * Exception expression definitions in Constant Definition
	 * @return
	 */
	public static Set<HAPExpressionDefinition> getExpressionDefinitions(HAPUIDefinitionUnit uiDefinitionUnit){
		Set<HAPExpressionDefinition> all = new HashSet<HAPExpressionDefinition>();
		for(HAPEmbededScriptExpressionInContent embededScriptExpression : uiDefinitionUnit.getScriptExpressionsInContent())	all.addAll(embededScriptExpression.getScriptExpression().getExpressionDefinitions());
		for(HAPEmbededScriptExpressionInAttribute embededScriptExpression : uiDefinitionUnit.getScriptExpressionsInAttributes())	all.addAll(embededScriptExpression.getScriptExpression().getExpressionDefinitions());
		for(HAPEmbededScriptExpressionInAttribute embededScriptExpression : uiDefinitionUnit.getScriptExpressionsInTagAttributes())		all.addAll(embededScriptExpression.getScriptExpression().getExpressionDefinitions());
		return all;
	}
	
	/**
	 * Calculate all the constant values in ConstantDef
	 * @param parentConstants
	 * @param idGenerator
	 * @param expressionMan
	 * @param runtime
	 */
	public static void calculateConstantDefs(
			HAPUIDefinitionUnit uiDefinitionUnit,
			Map<String, HAPConstantDef> parentConstants,
			HAPUIResourceIdGenerator idGenerator, 
			HAPExpressionManager expressionMan, 
			HAPRuntime runtime){
		//build context constants
		Map<String, HAPConstantDef> contextConstants = new LinkedHashMap<String, HAPConstantDef>();
		if(parentConstants!=null)   contextConstants.putAll(parentConstants);
		contextConstants.putAll(uiDefinitionUnit.getConstants());
		
		//process all constants defined in this domain
		HAPConstantUtility.processConstantDefs(contextConstants, idGenerator, expressionMan, runtime);
		
		//process constants in child
		for(HAPUIDefinitionUnitTag tag : uiDefinitionUnit.getUITags()){
			calculateConstantDefs(tag, contextConstants, idGenerator, expressionMan, runtime);
		}
	}
	
	public static void processResourceDependency(HAPUIDefinitionUnitResource uiResource, HAPResourceManagerRoot resourceMan){
		 Set<HAPResourceId> dependencyResourceIds = new LinkedHashSet();
		
		//resources need by expression
		List<HAPExpression> expressions = HAPUIResourceUtility.discoverExpressionsInUIResource(uiResource);
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
	
	public static void processUIResource(HAPUIDefinitionUnitResource uiResource, HAPExpressionManager expressionManager, HAPResourceManagerRoot resourceMan){
		uiResource.setExpressionUnit(new HAPUIResourceExpressionUnit(uiResource, null, expressionManager));
		processResourceDependency(uiResource, resourceMan);
		uiResource.processed();
	}
	
	
}

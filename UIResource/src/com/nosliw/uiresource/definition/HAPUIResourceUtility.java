package com.nosliw.uiresource.definition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
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
import com.nosliw.uiresource.expression.HAPScriptExpression;
import com.nosliw.uiresource.expression.HAPUIResourceExpressionContext;
import com.nosliw.uiresource.tag.HAPUITagContextElment;
import com.nosliw.uiresource.tag.HAPUITagDefinition;

public class HAPUIResourceUtility {

	public static List<HAPExpression> discoverExpressionsInUIResource(HAPUIDefinitionUnitResource uiResource){
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
		for(HAPEmbededScriptExpressionInContent embededScriptExpression : uiDefinitionUnit.getScriptExpressionsInContent())		all.addAll(embededScriptExpression.getScriptExpression().getExpressions().values());
		for(HAPEmbededScriptExpressionInAttribute embededScriptExpression : uiDefinitionUnit.getScriptExpressionsInAttributes())	all.addAll(embededScriptExpression.getScriptExpression().getExpressions().values());
		for(HAPEmbededScriptExpressionInAttribute embededScriptExpression : uiDefinitionUnit.getScriptExpressionsInTagAttributes())		all.addAll(embededScriptExpression.getScriptExpression().getExpressions().values());
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
		//build expression context
		processExpressionContext(null, uiResource);
		
		//process all script expressions in resource
		processScriptExpression(uiResource);
		
		//discovery resources required
		processResourceDependency(uiResource, resourceMan);
		uiResource.processed();
	}
	
	private static void processExpressionContext(HAPUIDefinitionUnit parent, HAPUIDefinitionUnit uiDefinition){

		HAPUIResourceExpressionContext expContext = uiDefinition.getExpressionContext();

		//variables
		switch(uiDefinition.getType()){
		case HAPConstant.UIRESOURCE_TYPE_RESOURCE:
			//for resource
			expContext.addVariables(discoverDataVariablesInContext(((HAPUIDefinitionUnitResource)uiDefinition).getContext()));
			break;
		case HAPConstant.UIRESOURCE_TYPE_TAG:
			//for tag
			HAPUITagDefinition uiTagDefinition = null;
			if(uiTagDefinition.isInheritContext()){
				//add parent 
				expContext.addVariables(parent.getExpressionContext().getVariables());
			}
			for(HAPUITagContextElment contextEle : uiTagDefinition.getContextDefinitions()){
				
			}
			break;
		}

		HAPUIResourceExpressionContext parentExpContext = parent==null?null:parent.getExpressionContext();
		
		//build data constants
		if(parent!=null)	expContext.addConstants(parentExpContext.getConstants());
		Map<String, HAPConstantDef> constantDefs = uiDefinition.getConstants();
		for(String name : constantDefs.keySet()){
			HAPData data = constantDefs.get(name).getDataValue();
			if(data!=null)		expContext.addConstant(name, data);
		}
		
		//get all expressions definitions
		//expression from parent first
		if(parent!=null){
			Map<String, HAPExpressionDefinition> parentExpDefs = parentExpContext.getExpressionDefinitions();
			for(String id : parentExpDefs.keySet())    expContext.addExpressionDefinition(parentExpDefs.get(id));
		}
		//expression from current
		for(HAPExpressionDefinition expDef : uiDefinition.getOtherExpressionDefinitions())	expContext.addExpressionDefinition(expDef);
		
		//prepress expressions
		//preprocess attributes operand in expressions
//		HAPUIResourceExpressionUtility.processAttributeOperandInExpression(m_expressionDefinitionSuite, m_variables);
		
		//children ui tags
		Iterator<HAPUIDefinitionUnitTag> its = uiDefinition.getUITags().iterator();
		while(its.hasNext()){
			HAPUIDefinitionUnitTag uiTag = its.next();
			processExpressionContext(uiDefinition, uiTag);
		}
		
		
	}
	
	private static void processScriptExpression(HAPUIDefinitionUnit uiDefinitionUnit){
		HAPUIResourceExpressionContext expContext = uiDefinitionUnit.getExpressionContext();
		for(HAPEmbededScriptExpressionInContent embededScriptExpression : uiDefinitionUnit.getScriptExpressionsInContent()){
			HAPScriptExpression scriptExpression = embededScriptExpression.getScriptExpression();
			scriptExpression.processExpressions(expContext);
			scriptExpression.discoverVarialbes();
		}
		
		for(HAPUIDefinitionUnit child : uiDefinitionUnit.getUITags()){
			processScriptExpression(child);
		}
	}
	
	public static Map<String, HAPDataTypeCriteria> discoverDataVariablesInContext(HAPContext context){
		Map<String, HAPDataTypeCriteria> out = new LinkedHashMap<String, HAPDataTypeCriteria>();
		for(String rootName : context.getElements().keySet()){
			processCriteria(rootName, context.getElements().get(rootName), out);
		}
		return out;
	}
	
	private static void processCriteria(String path, HAPContextNode node, Map<String, HAPDataTypeCriteria> criterias){
		HAPContextNodeDefinition definition = node.getDefinition();
		if(definition!=null){
			criterias.put(path, definition.getValue());
		}
		else{
			Map<String, HAPContextNode> children = node.getChildren();
			for(String childName : children.keySet()){
				String childPath = HAPNamingConversionUtility.cascadeComponentPath(path, childName);
				processCriteria(childPath, children.get(childName), criterias);
			}
		}
	}

}

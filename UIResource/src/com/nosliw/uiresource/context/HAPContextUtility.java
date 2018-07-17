package com.nosliw.uiresource.context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.uiresource.expression.HAPEmbededScriptExpression;
import com.nosliw.uiresource.expression.HAPRuntimeTaskExecuteEmbededExpression;
import com.nosliw.uiresource.expression.HAPUIResourceExpressionContext;
import com.nosliw.uiresource.page.HAPConstantDef;
import com.nosliw.uiresource.page.HAPUIDefinitionUnit;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitTag;
import com.nosliw.uiresource.tag.HAPUITagDefinitionContext;
import com.nosliw.uiresource.tag.HAPUITagId;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPContextUtility {

	public static void processExpressionContext(HAPUIDefinitionUnit parent, HAPUIDefinitionUnit uiDefinition, HAPDataTypeHelper dataTypeHelper, HAPUITagManager uiTagMan, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager){

		HAPUIResourceExpressionContext expContext = uiDefinition.getExpressionContext();

		//find all data variables from context definition 
		expContext.addVariables(discoverDataVariablesInContext(uiDefinition.getContext().getPublicContext()));
		expContext.addVariables(discoverDataVariablesInContext(uiDefinition.getContext().getInternalContext()));
		expContext.addVariables(discoverDataVariablesInContext(uiDefinition.getContext().getExcludedContext()));
		
		//parent expression context
		HAPUIResourceExpressionContext parentExpContext = parent==null?null:parent.getExpressionContext();
		
		//build data constants from local and parent
		//local constants override parent constants
		if(parent!=null)	expContext.addConstants(parentExpContext.getConstants());
		Map<String, HAPConstantDef> constantDefs = uiDefinition.getConstantDefs();
		for(String name : constantDefs.keySet()){
			HAPData data = constantDefs.get(name).getDataValue();
			if(data!=null)		expContext.addConstant(name, data);
		}
		
		//get all support expressions definitions from local and parent
		//local expression override expression from parent
		if(parent!=null){
			Map<String, HAPDefinitionExpression> parentExpDefs = parentExpContext.getExpressionDefinitions();
			for(String id : parentExpDefs.keySet()) {
				expContext.addExpressionDefinition(id, parentExpDefs.get(id));
			}
		}
		//expression from current
		for(String expName : uiDefinition.getOtherExpressionDefinitions().keySet())	expContext.addExpressionDefinition(expName, uiDefinition.getOtherExpressionDefinitions().get(expName));
		
		//children ui tags
		Iterator<HAPUIDefinitionUnitTag> its = uiDefinition.getUITags().iterator();
		while(its.hasNext()){
			HAPUIDefinitionUnitTag uiTag = its.next();
			buildUITagContext(uiDefinition.getContext(), uiTag, dataTypeHelper, uiTagMan, runtime, expressionManager);
			processExpressionContext(uiDefinition, uiTag, dataTypeHelper, uiTagMan, runtime, expressionManager);
		}
	}
	
	//build context for ui Tag
	private static void buildUITagContext(HAPContextGroup parentContext, HAPUIDefinitionUnitTag uiTag, HAPDataTypeHelper dataTypeHelper, HAPUITagManager uiTagMan, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager){
		//get contextDef from uiTag first
		HAPUITagDefinitionContext contextDefinition = uiTag.getContextDefinition();
		//if not exist, then from tag definition
		if(contextDefinition==null){
			contextDefinition = uiTagMan.getUITagDefinition(new HAPUITagId(uiTag.getTagName())).getContext();
			uiTag.setContextDefinition(contextDefinition);
		}

		Map<String, String> constants = uiTag.getAttributes();
		
		if(contextDefinition.isInherit()){
			//add public context from parent
			for(String rootEleName : parentContext.getPublicContext().getElements().keySet()){
				HAPContextNodeRootRelative relativeEle = new HAPContextNodeRootRelative();
				relativeEle.setPath(rootEleName);
				uiTag.getContext().getPublicContext().addElement(rootEleName, processContextDefinitionElement(rootEleName, relativeEle, parentContext, dataTypeHelper, constants, runtime, expressionManager));
			}
		}

		//element defined in tag definition
		processContextDefinition(parentContext, contextDefinition, uiTag.getContext(), constants, dataTypeHelper, uiTagMan, runtime, expressionManager);
	}
	
	public static void processContextDefinition(HAPContextGroup parentContext, HAPContextGroup defContext, HAPContextGroup outContext, Map<String, String> constants, HAPDataTypeHelper dataTypeHelper, HAPUITagManager uiTagMan, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager) {
		for(String contextType : HAPContextGroup.getContextTypes()){
			Map<String, HAPContextNodeRoot> defEles = defContext.getElements(contextType);
			for(String name : defEles.keySet()){
				String realName = getSolidName(name, constants, runtime, expressionManager);
				outContext.addElement(realName, processContextDefinitionElement(realName, defEles.get(name), parentContext, dataTypeHelper, constants, runtime, expressionManager), contextType);
			}
		}
	}
	
	
	//convert context definition to context 
	private static HAPContextNodeRoot processContextDefinitionElement(String defRootEleName, HAPContextNodeRoot defContextElement, HAPContextGroup parentContext, HAPDataTypeHelper dataTypeHelper, Map<String, String> constants, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager){
		String type = defContextElement.getType();
		switch(type){
			case HAPConstant.UIRESOURCE_ROOTTYPE_ABSOLUTE:
			{
				HAPContextNodeRootAbsolute defContextElementAbsolute = (HAPContextNodeRootAbsolute)defContextElement;
				HAPContextNodeRootAbsolute out = new HAPContextNodeRootAbsolute();
				out.setDefaultValue(defContextElementAbsolute.getDefaultValue());
				buildSolidContextNode(defContextElementAbsolute, out, constants, runtime, expressionManager);
				return out;
			}
			case HAPConstant.UIRESOURCE_ROOTTYPE_RELATIVE:
			{
				HAPContextNodeRootRelative defContextElementRelative = (HAPContextNodeRootRelative)defContextElement;
				HAPContextNodeRootRelative out = new HAPContextNodeRootRelative();
				HAPContextPath path = new HAPContextPath(getSolidName(defContextElementRelative.getPathStr(), constants, runtime, expressionManager));
				out.setPath(path);
				
				HAPContextNode parentNode = getReferencedParentContextNode(path, parentContext);
				if(parentNode!=null){
					Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
					merge(parentNode, defContextElementRelative, out, matchers, new HAPContextPath(defRootEleName, null), dataTypeHelper);
					//remove all the void matchers
					Map<String, HAPMatchers> noVoidMatchers = new LinkedHashMap<String, HAPMatchers>();
					for(String p : matchers.keySet()){
						HAPMatchers match = matchers.get(p);
						if(!match.isVoid()){
							noVoidMatchers.put(p, match);
						}
					}
					out.setMatchers(noVoidMatchers);
				}
				else{
					throw new RuntimeException();
				}
				return out;
			}	
		}
		return null;
	}
	
	//find all data variables in context 
	private static Map<String, HAPVariableInfo> discoverDataVariablesInContext(HAPContext context){
		Map<String, HAPVariableInfo> out = new LinkedHashMap<String, HAPVariableInfo>();
		for(String rootName : context.getElements().keySet()){
			discoverCriteriaInContextNode(rootName, (HAPContextNode)context.getElements().get(rootName), out);
		}
		return out;
	}

	//evaluate embeded script expression
	private static String getSolidName(String name, Map<String, String> constants, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager){
		HAPEmbededScriptExpression se = new HAPEmbededScriptExpression(null, name, expressionManager);
		HAPRuntimeTaskExecuteEmbededExpression task = new HAPRuntimeTaskExecuteEmbededExpression(se, null, new LinkedHashMap<String, Object>(constants));
		HAPServiceData serviceData = runtime.executeTaskSync(task);
		if(serviceData.isSuccess())   return (String)serviceData.getData();
		else return null;
	}

	//build context node with solid name
	//def : original node
	//solid : out, sold node
	private static void buildSolidContextNode(HAPContextNode def, HAPContextNode solid, Map<String, String> constants, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager){
		solid.setDefinition(def.getDefinition());
		for(String name : def.getChildren().keySet()){
			String solidName = getSolidName(name, constants, runtime, expressionManager);
			HAPContextNode child = def.getChildren().get(name);
			HAPContextNode solidChild = new HAPContextNode();
			buildSolidContextNode(child, solidChild, constants, runtime, expressionManager);
			solid.addChild(solidName, solidChild);
		}
	}
	
	//go through different context group type to find referenced node in parent. 
	private static HAPContextNode getReferencedParentContextNode(HAPContextPath path, HAPContextGroup parentContext){
		String[] contextTypes = {
				HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC,
				HAPConstant.UIRESOURCE_CONTEXTTYPE_INTERNAL,
				HAPConstant.UIRESOURCE_CONTEXTTYPE_EXCLUDED
				};
		//find candidates, path similar
		List<Object[]> candidates = new ArrayList<Object[]>();
		for(String contextType : contextTypes){
			Object[] nodeInfo = parentContext.getContext(contextType).discoverChild(path);
			if(nodeInfo[0]!=null)   candidates.add(nodeInfo);
		}

		//find best node from candidate
		//remaining path is shortest
		Object[] parentNodeInfo = null;
		int length = 99999;
		for(Object[] candidate : candidates) {
			String remainingPath = (String)candidate[1];
			if(remainingPath==null) {
				parentNodeInfo = candidate;
				break;
			}
			else {
				if(remainingPath.length()<length) {
					length = remainingPath.length();
					parentNodeInfo = candidate;
				}
			}
		}
		
		HAPContextNode parentNode = null;
		if(parentNodeInfo!=null) {
			if(parentNodeInfo[1]==null) {
				//exactly match with path
				parentNode = (HAPContextNode)parentNodeInfo[0];
			}
			else {
				//nof exactly match with path
				HAPContextNode candidateNode = (HAPContextNode)parentNodeInfo[0];
				HAPContextNodeCriteria nodeCriteria = candidateNode.getDefinition();
				if(nodeCriteria!=null) {
					//data type node
					HAPDataTypeCriteria parentCriteria = HAPCriteriaUtility.getChildCriteriaByPath(nodeCriteria.getValue(), (String)parentNodeInfo[1]);
					if(parentCriteria!=null) {
						parentNode = new HAPContextNode(); 
						parentNode.setDefinition(new HAPContextNodeCriteria(parentCriteria));
					}
				}
			}
		}
		
		return parentNode;
	}
	
	//merge parent context def with child context def to another context out
	//also generate matchers from parent to child
	private static void merge(HAPContextNode parent, HAPContextNode def, HAPContextNode out, Map<String, HAPMatchers> matchers, HAPContextPath path, HAPDataTypeHelper dataTypeHelper){
		HAPContextNodeCriteria parentDefinition = parent.getDefinition();
		if(parentDefinition!=null){
			HAPContextNodeCriteria defDefinition = null;
			if(def!=null)			defDefinition = def.getDefinition();
			if(defDefinition==null)   out.setDefinition(parentDefinition);
			else{
				out.setDefinition(defDefinition);
				//cal matchers
				HAPMatchers matcher = dataTypeHelper.convertable(parentDefinition.getValue(), defDefinition.getValue());
				matchers.put(path.getPath(), matcher);
			}
		}
		else{
			for(String name : parent.getChildren().keySet()){
				HAPContextNode outChild = new HAPContextNode();
				out.addChild(name, outChild);
				HAPContextNode defChild = null;
				if(def!=null && def.getChildren().get(name)!=null){
					defChild = def.getChildren().get(name);
				}
				merge(parent.getChildren().get(name), defChild, outChild, matchers, path.appendSegment(name), dataTypeHelper);
			}
		}
	}
	
	//discover data type criteria defined in context node
	//the purpose is to find variables related with data type criteria
	//the data type criteria name is full name in path, for instance, a.b.c.d
	private static void discoverCriteriaInContextNode(String path, HAPContextNode node, Map<String, HAPVariableInfo> criterias){
		HAPContextNodeCriteria definition = node.getDefinition();
		if(definition!=null){
			criterias.put(path, new HAPVariableInfo(definition.getValue()));
		}
		else{
			Map<String, HAPContextNode> children = node.getChildren();
			for(String childName : children.keySet()){
				String childPath = HAPNamingConversionUtility.cascadeComponentPath(path, childName);
				discoverCriteriaInContextNode(childPath, children.get(childName), criterias);
			}
		}
	}
	
}

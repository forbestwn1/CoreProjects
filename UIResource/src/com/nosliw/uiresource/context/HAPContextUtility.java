package com.nosliw.uiresource.context;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.uiresource.definition.HAPConstantDef;
import com.nosliw.uiresource.definition.HAPUIDefinitionUnit;
import com.nosliw.uiresource.definition.HAPUIDefinitionUnitTag;
import com.nosliw.uiresource.expression.HAPEmbededScriptExpression;
import com.nosliw.uiresource.expression.HAPRuntimeTaskExecuteEmbededExpression;
import com.nosliw.uiresource.expression.HAPUIResourceExpressionContext;
import com.nosliw.uiresource.tag.HAPUITagDefinitionContext;
import com.nosliw.uiresource.tag.HAPUITagDefinitionContextElementAbsolute;
import com.nosliw.uiresource.tag.HAPUITagDefinitionContextElment;
import com.nosliw.uiresource.tag.HAPUITagDefinitionContextElmentRelative;
import com.nosliw.uiresource.tag.HAPUITagId;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPContextUtility {

	public static void processExpressionContext(HAPUIDefinitionUnit parent, HAPUIDefinitionUnit uiDefinition, HAPDataTypeHelper dataTypeHelper, HAPUITagManager uiTagMan, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager){

		HAPUIResourceExpressionContext expContext = uiDefinition.getExpressionContext();

		//find all data variables 
		expContext.addVariables(discoverDataVariablesInContext(uiDefinition.getContext().getPublicContext()));
		expContext.addVariables(discoverDataVariablesInContext(uiDefinition.getContext().getInternalContext()));
		expContext.addVariables(discoverDataVariablesInContext(uiDefinition.getContext().getExcludedContext()));
		
		HAPUIResourceExpressionContext parentExpContext = parent==null?null:parent.getExpressionContext();
		
		//build data constants
		if(parent!=null)	expContext.addConstants(parentExpContext.getConstants());
		Map<String, HAPConstantDef> constantDefs = uiDefinition.getConstantDefs();
		for(String name : constantDefs.keySet()){
			HAPData data = constantDefs.get(name).getDataValue();
			if(data!=null)		expContext.addConstant(name, data);
		}
		
		//get all expressions definitions
		//expression from parent first
		if(parent!=null){
			Map<String, HAPDefinitionExpression> parentExpDefs = parentExpContext.getExpressionDefinitions();
			for(String id : parentExpDefs.keySet())    expContext.addExpressionDefinition(id, parentExpDefs.get(id));
		}
		//expression from current
		for(String expName : uiDefinition.getOtherExpressionDefinitions().keySet())	expContext.addExpressionDefinition(expName, uiDefinition.getOtherExpressionDefinitions().get(expName));
		
		//children ui tags
		Iterator<HAPUIDefinitionUnitTag> its = uiDefinition.getUITags().iterator();
		while(its.hasNext()){
			HAPUIDefinitionUnitTag uiTag = its.next();
			buildUITagContext(uiDefinition, uiTag, dataTypeHelper, uiTagMan, runtime, expressionManager);
			processExpressionContext(uiDefinition, uiTag, dataTypeHelper, uiTagMan, runtime, expressionManager);
		}
	}
	
	private static void buildUITagContext(HAPUIDefinitionUnit parent, HAPUIDefinitionUnitTag uiTag, HAPDataTypeHelper dataTypeHelper, HAPUITagManager uiTagMan, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager){
		//get contextDef from uiTag first
		HAPUITagDefinitionContext contextDefinition = uiTag.getContextDefinition();
		//if not exist, then from tag definition
		if(contextDefinition==null){
			contextDefinition = uiTagMan.getUITagDefinition(new HAPUITagId(uiTag.getTagName())).getContext();
			uiTag.setContextDefinition(contextDefinition);
		}

		if(contextDefinition.isInherit()){
			//add public context from parent
			for(String rootEleName : parent.getContext().getPublicContext().getElements().keySet()){
				HAPUITagDefinitionContextElmentRelative relativeEle = new HAPUITagDefinitionContextElmentRelative(rootEleName);
				uiTag.getContext().getPublicContext().addElement(rootEleName, processUITagDefinitionContextElement(rootEleName, relativeEle, parent, dataTypeHelper, uiTag, runtime, expressionManager));
			}
		}

		//element defined in tag definition
		for(String contextType : HAPContextGroup.getContextTypes()){
			Map<String, HAPUITagDefinitionContextElment> defEles = contextDefinition.getElements(contextType);
			for(String name : defEles.keySet()){
				String realName = getSolidName(name, uiTag, runtime, expressionManager);
				uiTag.getContext().addElement(realName, processUITagDefinitionContextElement(realName, defEles.get(name), parent, dataTypeHelper, uiTag, runtime, expressionManager), contextType);
			}
		}
	}
	
	//find all data variables in context 
	private static Map<String, HAPVariableInfo> discoverDataVariablesInContext(HAPContext context){
		Map<String, HAPVariableInfo> out = new LinkedHashMap<String, HAPVariableInfo>();
		for(String rootName : context.getElements().keySet()){
			processCriteria(rootName, (HAPContextNode)context.getElements().get(rootName), out);
		}
		return out;
	}

	//evaluate embeded script expression
	private static String getSolidName(String name, HAPUIDefinitionUnit uiDefinition, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager){
		HAPEmbededScriptExpression se = new HAPEmbededScriptExpression(null, name, expressionManager);
		HAPRuntimeTaskExecuteEmbededExpression task = new HAPRuntimeTaskExecuteEmbededExpression(se, null, new LinkedHashMap<String, Object>(uiDefinition.getAttributes()));
		HAPServiceData serviceData = runtime.executeTaskSync(task);
		if(serviceData.isSuccess())   return (String)serviceData.getData();
		else return null;
	}

	//process all the name get solid name and create new contextNode
	private static HAPContextNode buildSolidContextNode(HAPContextNode contextNode, HAPUIDefinitionUnit uiDefinition, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager){
		HAPContextNode out = new HAPContextNode();
		buildSolidContextNode(contextNode, out, uiDefinition, runtime, expressionManager);
		return out;
	}
	
	private static void buildSolidContextNode(HAPContextNode def, HAPContextNode solid, HAPUIDefinitionUnit uiDefinition, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager){
		solid.setDefinition(def.getDefinition());
		for(String name : def.getChildren().keySet()){
			String solidName = getSolidName(name, uiDefinition, runtime, expressionManager);
			HAPContextNode child = def.getChildren().get(name);
			HAPContextNode soldChild = buildSolidContextNode(child, uiDefinition, runtime, expressionManager);
			solid.addChild(solidName, soldChild);
		}
	}
	
	private static HAPContextNode getReferencedParentContextNode(HAPContextPath path, HAPUIDefinitionUnit parentUnit){
		String[] contextTypes = {
				HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC,
				HAPConstant.UIRESOURCE_CONTEXTTYPE_INTERNAL,
				HAPConstant.UIRESOURCE_CONTEXTTYPE_EXCLUDED
				};
		HAPContextNode parentNode = null;
		for(String contextType : contextTypes){
			parentNode = parentUnit.getContext().getContext(contextType).getChild(path);
			if(parentNode!=null)   break;
		}
		return parentNode;
	}
	
	//convert context element in ui tag to context element in ui resource/tag
	private static HAPContextNodeRoot processUITagDefinitionContextElement(String defRootEleName, HAPUITagDefinitionContextElment defContextElement, HAPUIDefinitionUnit parentUnit, HAPDataTypeHelper dataTypeHelper, HAPUIDefinitionUnit uiDefinition, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager){
		String type = defContextElement.getType();
		switch(type){
			case HAPConstant.UIRESOURCE_ROOTTYPE_ABSOLUTE:
			{
				HAPUITagDefinitionContextElementAbsolute defContextElementAbsolute = (HAPUITagDefinitionContextElementAbsolute)defContextElement;
				HAPContextNodeRootAbsolute out = new HAPContextNodeRootAbsolute();
				out.setDefaultValue(defContextElementAbsolute.getDefaultValue());
				buildSolidContextNode(defContextElementAbsolute, out, uiDefinition, runtime, expressionManager);
				return out;
			}
			case HAPConstant.UIRESOURCE_ROOTTYPE_RELATIVE:
			{
				HAPUITagDefinitionContextElmentRelative defContextElementRelative = (HAPUITagDefinitionContextElmentRelative)defContextElement;
				HAPContextNodeRootRelative out = new HAPContextNodeRootRelative();
				HAPContextPath path = new HAPContextPath(getSolidName(defContextElementRelative.getPath(), uiDefinition, runtime, expressionManager));
				out.setPath(path);
				
				HAPContextNode parentNode = getReferencedParentContextNode(path, parentUnit);
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
				return out;
			}	
		}
		return null;
	}
	
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
	
	
	private static void processCriteria(String path, HAPContextNode node, Map<String, HAPVariableInfo> criterias){
		HAPContextNodeCriteria definition = node.getDefinition();
		if(definition!=null){
			criterias.put(path, new HAPVariableInfo(definition.getValue()));
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

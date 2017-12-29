package com.nosliw.uiresource.context;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.uiresource.definition.HAPConstantDef;
import com.nosliw.uiresource.definition.HAPUIDefinitionUnit;
import com.nosliw.uiresource.definition.HAPUIDefinitionUnitResource;
import com.nosliw.uiresource.definition.HAPUIDefinitionUnitTag;
import com.nosliw.uiresource.expression.HAPEmbededScriptExpression;
import com.nosliw.uiresource.expression.HAPRuntimeTaskExecuteEmbededExpression;
import com.nosliw.uiresource.expression.HAPUIResourceExpressionContext;
import com.nosliw.uiresource.tag.HAPUITagDefinition;
import com.nosliw.uiresource.tag.HAPUITagDefinitionContext;
import com.nosliw.uiresource.tag.HAPUITagDefinitionContextElementAbsolute;
import com.nosliw.uiresource.tag.HAPUITagDefinitionContextElment;
import com.nosliw.uiresource.tag.HAPUITagDefinitionContextElmentRelative;
import com.nosliw.uiresource.tag.HAPUITagId;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPContextUtility {

	public static void processExpressionContext(HAPUIDefinitionUnit parent, HAPUIDefinitionUnit uiDefinition, HAPDataTypeHelper dataTypeHelper, HAPUITagManager uiTagMan, HAPRuntime runtime, HAPExpressionManager expressionManager){

		HAPUIResourceExpressionContext expContext = uiDefinition.getExpressionContext();

		//find all data variables 
		expContext.addVariables(discoverDataVariablesInContext(uiDefinition.getContext().getPublicContext()));
		expContext.addVariables(discoverDataVariablesInContext(uiDefinition.getContext().getInternalContext()));
		
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
			Map<String, HAPExpressionDefinition> parentExpDefs = parentExpContext.getExpressionDefinitions();
			for(String id : parentExpDefs.keySet())    expContext.addExpressionDefinition(parentExpDefs.get(id));
		}
		//expression from current
		for(HAPExpressionDefinition expDef : uiDefinition.getOtherExpressionDefinitions())	expContext.addExpressionDefinition(expDef);
		
		//children ui tags
		Iterator<HAPUIDefinitionUnitTag> its = uiDefinition.getUITags().iterator();
		while(its.hasNext()){
			HAPUIDefinitionUnitTag uiTag = its.next();
			processUITagContext(uiDefinition, uiTag, dataTypeHelper, uiTagMan, runtime, expressionManager);
		}
	}
	
	private void processIncludeTag(HAPUIDefinitionUnitTag includeTagResource){
		HAPUIDefinitionUnitResource rootResource = null;
		String includeResourceName = includeTagResource.getAttributes().get("source");
		String contextMapName = includeTagResource.getAttributes().get("context");
		HAPUIDefinitionUnitResource uiResource = getUIResourceDefinitionByName(includeResourceName);
		String source = uiResource.getSource();
		this.getUIResourceParser().parseContent(includeTagResource, source);

		HAPUITagDefinitionContext contextDef = new HAPUITagDefinitionContext();
		JSONObject contextMappingJson = (JSONObject)rootResource.getConstantValues().get(contextMapName);
		if(contextMappingJson!=null){
			HAPContextParser.parseContextInTagDefinition(contextMappingJson, contextDef);
		}
		
		HAPContextGroup contextGroup = includeTagResource.getContext();
		for(String contextType : contextGroup.getContextTypes()){
			HAPContext context = contextGroup.getContext(contextType);
			Map<String, HAPContextNodeRoot> elements = context.getElements();
			for(String eleName : elements.keySet()){
				HAPContextNodeRoot rootNode = elements.get(eleName);
				
				HAPUITagDefinitionContextElment defEle = contextDef.getElements(contextType).get(eleName);
				if(defEle==null){
					//not mapped, add to root context
					rootResource.getContext().getContext(contextType).addElement(eleName, rootNode);
				}
				else{
					//mapped, do mapping
					String realName = getSolidName(name, uiTag, runtime, expressionManager);
					includeTagResource.getContext().addElement(realName, processUITagDefinitionContextElement(realName, defElesPublic.get(name), parent, this.m_dataTypeHelper, uiTag, m_runtime, m_expressionMan), contextType);

					
					
					//element defined in tag definition
					for(String contextType : HAPContextGroup.getContextTypes()){
						Map<String, HAPUITagDefinitionContextElment> defElesPublic = uiTagDefinition.getContext().getElements(contextType);
						for(String name : defElesPublic.keySet()){
							String realName = getSolidName(name, uiTag, runtime, expressionManager);
							uiTag.getContext().addElement(realName, processUITagDefinitionContextElement(realName, defElesPublic.get(name), parent, dataTypeHelper, uiTag, runtime, expressionManager), contextType);
						}
					}
					
				}
				
			}
		}
		

	}
	
	
	
	private static void processUITagContext(HAPUIDefinitionUnit parent, HAPUIDefinitionUnitTag uiTag, HAPDataTypeHelper dataTypeHelper, HAPUITagManager uiTagMan, HAPRuntime runtime, HAPExpressionManager expressionManager){
		//for tag
		HAPUITagDefinition uiTagDefinition = uiTagMan.getUITagDefinition(new HAPUITagId(uiTag.getTagName()));
		if(uiTagDefinition.getContext().isInherit()){
			//add public context from parent
			for(String rootEleName : parent.getContext().getPublicContext().getElements().keySet()){
				HAPUITagDefinitionContextElmentRelative relativeEle = new HAPUITagDefinitionContextElmentRelative(rootEleName);
				uiTag.getContext().getPublicContext().addElement(rootEleName, processUITagDefinitionContextElement(rootEleName, relativeEle, parent, dataTypeHelper, uiTag, runtime, expressionManager));
			}
		}

		//element defined in tag definition
		for(String contextType : HAPContextGroup.getContextTypes()){
			Map<String, HAPUITagDefinitionContextElment> defElesPublic = uiTagDefinition.getContext().getElements(contextType);
			for(String name : defElesPublic.keySet()){
				String realName = getSolidName(name, uiTag, runtime, expressionManager);
				uiTag.getContext().addElement(realName, processUITagDefinitionContextElement(realName, defElesPublic.get(name), parent, dataTypeHelper, uiTag, runtime, expressionManager), contextType);
			}
		}
	}
	
	//find all data variables in context 
	private static Map<String, HAPDataTypeCriteria> discoverDataVariablesInContext(HAPContext context){
		Map<String, HAPDataTypeCriteria> out = new LinkedHashMap<String, HAPDataTypeCriteria>();
		for(String rootName : context.getElements().keySet()){
			processCriteria(rootName, (HAPContextNode)context.getElements().get(rootName), out);
		}
		return out;
	}

	//evaluate embeded script expression
	private static String getSolidName(String name, HAPUIDefinitionUnit uiDefinition, HAPRuntime runtime, HAPExpressionManager expressionManager){
		HAPEmbededScriptExpression se = new HAPEmbededScriptExpression(null, name, expressionManager);
		HAPRuntimeTaskExecuteEmbededExpression task = new HAPRuntimeTaskExecuteEmbededExpression(se, null, new LinkedHashMap<String, Object>(uiDefinition.getAttributes()));
		HAPServiceData serviceData = runtime.executeTaskSync(task);
		if(serviceData.isSuccess())   return (String)serviceData.getData();
		else return null;
	}

	//process all the name get solid name and create new contextNode
	private static HAPContextNode buildSolidContextNode(HAPContextNode contextNode, HAPUIDefinitionUnit uiDefinition, HAPRuntime runtime, HAPExpressionManager expressionManager){
		HAPContextNode out = new HAPContextNode();
		buildSolidContextNode(contextNode, out, uiDefinition, runtime, expressionManager);
		return out;
	}
	
	private static void buildSolidContextNode(HAPContextNode def, HAPContextNode solid, HAPUIDefinitionUnit uiDefinition, HAPRuntime runtime, HAPExpressionManager expressionManager){
		solid.setDefinition(def.getDefinition());
		for(String name : def.getChildren().keySet()){
			String solidName = getSolidName(name, uiDefinition, runtime, expressionManager);
			HAPContextNode child = def.getChildren().get(name);
			HAPContextNode soldChild = buildSolidContextNode(child, uiDefinition, runtime, expressionManager);
			solid.addChild(solidName, soldChild);
		}
	}
	
	//convert context element in ui tag to context element in ui resource/tag
	private static HAPContextNodeRoot processUITagDefinitionContextElement(String defRootEleName, HAPUITagDefinitionContextElment defContextElement, HAPUIDefinitionUnit parentUnit, HAPDataTypeHelper dataTypeHelper, HAPUIDefinitionUnit uiDefinition, HAPRuntime runtime, HAPExpressionManager expressionManager){
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
				
				HAPContextNode parentNode = parentUnit.getContext().getPublicContext().getChild(path);   //try parent's public context first 
				if(parentNode==null)   parentNode = parentUnit.getContext().getInternalContext().getChild(path); 		//if not found, then try parent's internal context
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
	
	
	private static void processCriteria(String path, HAPContextNode node, Map<String, HAPDataTypeCriteria> criterias){
		HAPContextNodeCriteria definition = node.getDefinition();
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

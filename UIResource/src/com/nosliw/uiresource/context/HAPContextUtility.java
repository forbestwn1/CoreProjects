package com.nosliw.uiresource.context;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

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
import com.nosliw.uiresource.tag.HAPUITagDefinitionContextElementAbsolute;
import com.nosliw.uiresource.tag.HAPUITagDefinitionContextElment;
import com.nosliw.uiresource.tag.HAPUITagDefinitionContextElmentRelative;
import com.nosliw.uiresource.tag.HAPUITagId;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPContextUtility {

	
	public static void processExpressionContext(HAPUIDefinitionUnit parent, HAPUIDefinitionUnit uiDefinition, HAPDataTypeHelper dataTypeHelper, HAPUITagManager uiTagMan, HAPRuntime runtime, HAPExpressionManager expressionManager){

		HAPUIResourceExpressionContext expContext = uiDefinition.getExpressionContext();

		//variables
		switch(uiDefinition.getType()){
		case HAPConstant.UIRESOURCE_TYPE_RESOURCE:
			//for resource
			expContext.addVariables(discoverDataVariablesInContext(((HAPUIDefinitionUnitResource)uiDefinition).getContext()));
			break;
		case HAPConstant.UIRESOURCE_TYPE_TAG:
			//for tag
			HAPUIDefinitionUnitTag tag = (HAPUIDefinitionUnitTag)uiDefinition;
			HAPUITagDefinition uiTagDefinition = uiTagMan.getUITagDefinition(new HAPUITagId(tag.getTagName()));
			if(uiTagDefinition.getContext().isInherit()){
				//add parent
				for(String rootEleName : parent.getContext().getElements().keySet()){
					HAPUITagDefinitionContextElmentRelative relativeEle = new HAPUITagDefinitionContextElmentRelative(rootEleName);
					tag.getContext().addElement(rootEleName, processUITagDefinitionContextElement(rootEleName, relativeEle, parent.getContext(), dataTypeHelper, uiDefinition, runtime, expressionManager));
				}
			}

			//element defined in tag definition
			Map<String, HAPUITagDefinitionContextElment> defEles = uiTagDefinition.getContext().getElements();
			for(String name : defEles.keySet()){
				String realName = getSolidName(name, uiDefinition, runtime, expressionManager);
				tag.getContext().addElement(realName, processUITagDefinitionContextElement(realName, defEles.get(name), parent.getContext(), dataTypeHelper, uiDefinition, runtime, expressionManager));
			}
			
			expContext.addVariables(discoverDataVariablesInContext(uiDefinition.getContext()));
			break;
		}

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
			processExpressionContext(uiDefinition, uiTag, dataTypeHelper, uiTagMan, runtime, expressionManager);
		}
		
		
	}
	
	public static Map<String, HAPDataTypeCriteria> discoverDataVariablesInContext(HAPContext context){
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

//	private static String getSolidName(String name){
//		return name;
//	}
	
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
	private static HAPContextNodeRoot processUITagDefinitionContextElement(String defRootEleName, HAPUITagDefinitionContextElment defContextElement, HAPContext parentContext, HAPDataTypeHelper dataTypeHelper, HAPUIDefinitionUnit uiDefinition, HAPRuntime runtime, HAPExpressionManager expressionManager){
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
				HAPContextNode parentNode = parentContext.getChild(path);
				Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
				merge(parentNode, defContextElementRelative, out, matchers, new HAPContextPath(defRootEleName, null), dataTypeHelper);
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

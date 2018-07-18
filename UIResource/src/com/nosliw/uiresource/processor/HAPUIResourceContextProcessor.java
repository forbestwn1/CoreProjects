package com.nosliw.uiresource.processor;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.uiresource.context.HAPContext;
import com.nosliw.uiresource.context.HAPContextNode;
import com.nosliw.uiresource.context.HAPContextNodeCriteria;
import com.nosliw.uiresource.expression.HAPUIResourceExpressionContext;
import com.nosliw.uiresource.page.HAPConstantDef;
import com.nosliw.uiresource.page.HAPUIDefinitionUnit;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitTag;
import com.nosliw.uiresource.tag.HAPUITagManager;
import com.nosliw.uiresource.tag.HAPUITagUtility;

public class HAPUIResourceContextProcessor {

	public static void processContext(HAPUIDefinitionUnit parent, HAPUIDefinitionUnit uiDefinition, HAPDataTypeHelper dataTypeHelper, HAPUITagManager uiTagMan, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager){

		processExpressionContext(parent, uiDefinition, dataTypeHelper, uiTagMan, runtime, expressionManager);

		//children ui tags
		Iterator<HAPUIDefinitionUnitTag> its = uiDefinition.getUITags().iterator();
		while(its.hasNext()){
			HAPUIDefinitionUnitTag uiTag = its.next();
			HAPUITagUtility.buildUITagContext(uiDefinition.getContext(), uiTag, dataTypeHelper, uiTagMan, runtime, expressionManager);
			processContext(uiDefinition, uiTag, dataTypeHelper, uiTagMan, runtime, expressionManager);
		}
	}
	
	private static void processExpressionContext(HAPUIDefinitionUnit parent, HAPUIDefinitionUnit uiDefinition, HAPDataTypeHelper dataTypeHelper, HAPUITagManager uiTagMan, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager){

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
	}
	
	//find all data variables in context 
	private static Map<String, HAPVariableInfo> discoverDataVariablesInContext(HAPContext context){
		Map<String, HAPVariableInfo> out = new LinkedHashMap<String, HAPVariableInfo>();
		for(String rootName : context.getElements().keySet()){
			discoverCriteriaInContextNode(rootName, (HAPContextNode)context.getElements().get(rootName), out);
		}
		return out;
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

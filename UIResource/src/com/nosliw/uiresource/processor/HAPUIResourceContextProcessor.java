package com.nosliw.uiresource.processor;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.data.context.HAPContext;
import com.nosliw.data.context.HAPContextNode;
import com.nosliw.data.context.HAPContextNodeCriteria;
import com.nosliw.data.context.HAPContextUtility;
import com.nosliw.data.context.HAPEnvContextProcessor;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.expressionscript.HAPContextExpressionProcess;
import com.nosliw.uiresource.page.HAPConstantDef;
import com.nosliw.uiresource.page.HAPUIDefinitionUnit;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitTag;
import com.nosliw.uiresource.tag.HAPUITagManager;
import com.nosliw.uiresource.tag.HAPUITagUtility;

public class HAPUIResourceContextProcessor {

	public static void process(HAPUIDefinitionUnit parent, HAPUIDefinitionUnit uiDefinition, HAPUITagManager uiTagMan, HAPEnvContextProcessor contextProcessorEnv){

		//process context defined within unit
		HAPContextUtility.processContextGroupDefinition(parent==null?null:parent.getContext(), uiDefinition.getContextDefinition(), uiDefinition.getContext(), uiDefinition.getAttributes(), contextProcessorEnv);
		
		processExpressionContext(parent, uiDefinition, uiTagMan, contextProcessorEnv);

		//children ui tags
		Iterator<HAPUIDefinitionUnitTag> its = uiDefinition.getUITags().iterator();
		while(its.hasNext()){
			HAPUIDefinitionUnitTag uiTag = its.next();
			HAPUITagUtility.buildUITagContext(uiDefinition.getContext(), uiTag, uiTagMan, contextProcessorEnv);
			process(uiDefinition, uiTag, uiTagMan, contextProcessorEnv);
		}
	}
	
	private static void processExpressionContext(HAPUIDefinitionUnit parent, HAPUIDefinitionUnit uiDefinition, HAPUITagManager uiTagMan, HAPEnvContextProcessor contextProcessorEnv){

		HAPContextExpressionProcess expContext = uiDefinition.getExpressionContext();

		//find all data variables from context definition 
		expContext.addVariables(discoverDataVariablesInContext(uiDefinition.getContext().getPublicContext()));
		expContext.addVariables(discoverDataVariablesInContext(uiDefinition.getContext().getProtectedContext()));
		expContext.addVariables(discoverDataVariablesInContext(uiDefinition.getContext().getInternalContext()));
		expContext.addVariables(discoverDataVariablesInContext(uiDefinition.getContext().getExcludedContext()));
		
		//parent expression context
		HAPContextExpressionProcess parentExpContext = parent==null?null:parent.getExpressionContext();
		
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

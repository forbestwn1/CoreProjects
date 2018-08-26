package com.nosliw.uiresource.processor;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.script.constant.HAPConstantDef;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextNode;
import com.nosliw.data.core.script.context.HAPContextNodeCriteria;
import com.nosliw.data.core.script.context.HAPContextNodeRootConstant;
import com.nosliw.data.core.script.context.HAPContextUtility;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.expressionscript.HAPContextScriptExpressionProcess;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitTag;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;
import com.nosliw.uiresource.tag.HAPUITagDefinitionContext;
import com.nosliw.uiresource.tag.HAPUITagId;
import com.nosliw.uiresource.tag.HAPUITagManager;
import com.nosliw.uiresource.tag.HAPUITagUtility;

public class HAPProcessorUIContext {

	public static void processUIUnitContext(HAPExecutableUIUnit uiExe, HAPContextGroup parentContext, HAPUITagManager uiTagMan, HAPEnvContextProcessor contextProcessorEnv){
		
		HAPContextGroup extContextGroup = HAPProcessorContext.processContext(uiExe.getUIUnitDefinition().getContextDefinition(), parentContext, new HAPConfigureContextProcessor(), contextProcessorEnv);
		uiExe.setContext(extContextGroup);
		
		//children ui tags
		Iterator<HAPExecutableUIUnitTag> its = uiExe.getUITags().iterator();
		while(its.hasNext()){
			HAPExecutableUIUnitTag uiTag = its.next();
			HAPContextGroup tagContext = buildUITagContext(uiTag, extContextGroup, uiTagMan, contextProcessorEnv);
			processUIUnitContext(uiTag, tagContext, uiTagMan, contextProcessorEnv);
		}
	}
	
	//build context for ui Tag
	private static HAPContextGroup buildUITagContext(HAPExecutableUIUnitTag uiTag, HAPContextGroup parentContext, HAPUITagManager uiTagMan, HAPEnvContextProcessor contextProcessorEnv){
		//get contextDef 
		HAPUITagDefinitionContext tagDefinitionContext = uiTagMan.getUITagDefinition(new HAPUITagId(uiTag.getUIUnitTagDefinition().getTagName())).getContext();

		//add attribute constant as part of tagContext
		Map<String, String> constants = uiTag.getAttributes();
		HAPContextGroup tagContext = tagDefinitionContext.clone();
		for(String cstName : constants.keySet()) {
			HAPContextNodeRootConstant cstRootNode = new HAPContextNodeRootConstant(constants.get(cstName));
			tagContext.addElement(cstName, cstRootNode, HAPConstant.UIRESOURCE_CONTEXTTYPE_INTERNAL);
		}
		
		HAPConfigureContextProcessor configure = new HAPConfigureContextProcessor();
		configure.inheritMode = tagDefinitionContext.getInheritMode();
		return HAPProcessorContext.processContext(tagContext, parentContext, configure, contextProcessorEnv);
	}
	
	
	
	
	
	
	
	private static void processExpressionContext(HAPDefinitionUIUnit parent, HAPDefinitionUIUnit uiDefinition, HAPUITagManager uiTagMan, HAPEnvContextProcessor contextProcessorEnv){

		HAPContextScriptExpressionProcess expContext = uiDefinition.getExpressionContext();

		//find all data variables from context definition 
		expContext.addVariables(discoverDataVariablesInContext(uiDefinition.getContext().getPublicContext()));
		expContext.addVariables(discoverDataVariablesInContext(uiDefinition.getContext().getProtectedContext()));
		expContext.addVariables(discoverDataVariablesInContext(uiDefinition.getContext().getInternalContext()));
		
		//parent expression context
		HAPContextScriptExpressionProcess parentExpContext = parent==null?null:parent.getExpressionContext();
		
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

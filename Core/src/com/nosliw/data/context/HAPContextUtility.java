package com.nosliw.data.context;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expressionscript.HAPEmbededScriptExpression;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteEmbededExpression;

public class HAPContextUtility {
	
	public static void processContextGroupDefinition(HAPContextGroup parentContext, HAPContextGroup defContext, HAPContextGroup outContext, Map<String, String> constants, HAPEnvContextProcessor contextProcessorEnv) {
		for(String contextType : HAPContextGroup.getContextTypes()){
			Map<String, HAPContextNodeRoot> defEles = defContext.getElements(contextType);
			for(String name : defEles.keySet()){
				String realName = getSolidName(name, constants, contextProcessorEnv);
				outContext.addElement(realName, processContextDefinitionElement(realName, defEles.get(name), parentContext, constants, contextProcessorEnv), contextType);
			}
		}
	}
	
	public static void processContextDefinition(HAPContextGroup parentContext, HAPContext defContext, HAPContext outContext, Map<String, String> constants, HAPEnvContextProcessor contextProcessorEnv) {
		Map<String, HAPContextNodeRoot> defEles = defContext.getElements();
		for(String name : defEles.keySet()){
			String realName = getSolidName(name, constants, contextProcessorEnv);
			outContext.addElement(realName, processContextDefinitionElement(realName, defEles.get(name), parentContext, constants, contextProcessorEnv));
		}
	}
	
	
	
	
	//convert context definition to context 
	public static HAPContextNodeRoot processContextDefinitionElement(String defRootEleName, HAPContextNodeRoot defContextElement, HAPContextGroup parentContext, Map<String, String> constants, HAPEnvContextProcessor contextProcessorEnv){
		String type = defContextElement.getType();
		switch(type){
			case HAPConstant.UIRESOURCE_ROOTTYPE_ABSOLUTE:
			{
				HAPContextNodeRootAbsolute defContextElementAbsolute = (HAPContextNodeRootAbsolute)defContextElement;
				HAPContextNodeRootAbsolute out = new HAPContextNodeRootAbsolute();
				out.setDefaultValue(defContextElementAbsolute.getDefaultValue());
				buildSolidContextNode(defContextElementAbsolute, out, constants, contextProcessorEnv);
				return out;
			}
			case HAPConstant.UIRESOURCE_ROOTTYPE_RELATIVE:
			{
				HAPContextNodeRootRelative defContextElementRelative = (HAPContextNodeRootRelative)defContextElement;
				HAPContextNodeRootRelative out = new HAPContextNodeRootRelative();
				HAPContextPath path = new HAPContextPath(getSolidName(defContextElementRelative.getPathStr(), constants, contextProcessorEnv));
				out.setPath(path);
				
				HAPContextNode parentNode = getReferencedParentContextNode(path, parentContext);
				if(parentNode!=null){
					Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
					merge(parentNode, defContextElementRelative, out, matchers, new HAPContextPath(defRootEleName, null), contextProcessorEnv);
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

/*	
	private static HAPContextNodeRootRelative processRelativeContextDefinitionElement(String defRootEleName1, HAPContextNodeRootRelative defContextElementRelative, HAPContextGroup parentContext, Map<String, String> constants, HAPEnvContextProcessor contextProcessorEnv){
		HAPContextNodeRootRelative out = new HAPContextNodeRootRelative();
		HAPContextPath path = new HAPContextPath(getSolidName(defContextElementRelative.getPathStr(), constants, contextProcessorEnv));
		out.setPath(path);
		
		HAPContextNode parentNode = getReferencedParentContextNode(path, parentContext);
		if(parentNode!=null){
			Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
			merge(parentNode, defContextElementRelative, out, matchers, new HAPContextPath(defRootEleName, null), contextProcessorEnv);
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
*/	
	
	//evaluate embeded script expression
	private static String getSolidName(String name, Map<String, String> constants, HAPEnvContextProcessor contextProcessorEnv){
		HAPEmbededScriptExpression se = new HAPEmbededScriptExpression(name, contextProcessorEnv.expressionManager);
		HAPRuntimeTaskExecuteEmbededExpression task = new HAPRuntimeTaskExecuteEmbededExpression(se, null, new LinkedHashMap<String, Object>(constants));
		HAPServiceData serviceData = contextProcessorEnv.runtime.executeTaskSync(task);
		if(serviceData.isSuccess())   return (String)serviceData.getData();
		else return null;
	}

	//build context node with solid name
	//def : original node
	//solid : out, sold node
	private static void buildSolidContextNode(HAPContextNode def, HAPContextNode solid, Map<String, String> constants, HAPEnvContextProcessor contextProcessorEnv){
		solid.setDefinition(def.getDefinition());
		for(String name : def.getChildren().keySet()){
			String solidName = getSolidName(name, constants, contextProcessorEnv);
			HAPContextNode child = def.getChildren().get(name);
			HAPContextNode solidChild = new HAPContextNode();
			buildSolidContextNode(child, solidChild, constants, contextProcessorEnv);
			solid.addChild(solidName, solidChild);
		}
	}
	
	//go through different context group type to find referenced node in parent. 
	private static HAPContextNode getReferencedParentContextNode(HAPContextPath path, HAPContextGroup parentContext){
		if(parentContext==null)   return null;
		
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
	private static void merge(HAPContextNode parent, HAPContextNode def, HAPContextNode out, Map<String, HAPMatchers> matchers, HAPContextPath path, HAPEnvContextProcessor contextProcessorEnv){
		HAPContextNodeCriteria parentDefinition = parent.getDefinition();
		if(parentDefinition!=null){
			HAPContextNodeCriteria defDefinition = null;
			if(def!=null)			defDefinition = def.getDefinition();
			if(defDefinition==null)   out.setDefinition(parentDefinition);
			else{
				out.setDefinition(defDefinition);
				//cal matchers
				HAPMatchers matcher = contextProcessorEnv.dataTypeHelper.convertable(parentDefinition.getValue(), defDefinition.getValue());
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
				merge(parent.getChildren().get(name), defChild, outChild, matchers, path.appendSegment(name), contextProcessorEnv);
			}
		}
	}
	
}

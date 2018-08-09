package com.nosliw.data.context;

import java.util.ArrayList;
import java.util.Arrays;
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

	public static void processContextGroupDefinition(HAPContextGroup defContext, HAPContextGroup parentContext, HAPContextGroup outContext, HAPConfigureContextProcessor configure, Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv) {
		for(String contextType : HAPContextGroup.getAllContextTypes()){
			processContextDefinition(defContext.getContext(contextType), contextType, parentContext, outContext.getContext(contextType), configure, constants, contextProcessorEnv);
		}
	}

	public static void processContextDefinitionWithoutInhert(HAPContext defContext, HAPContextGroup parentContextGroup, HAPContext outContext, HAPConfigureContextProcessor configure, Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv) {
		configure.inheritMode = HAPConfigureContextProcessor.VALUE_INHERITMODE_NONE;
		processContextDefinition(defContext, null, parentContextGroup, outContext, configure, constants, contextProcessorEnv);
	}
	
	public static void processContextDefinition(HAPContext context, String contextCategary, HAPContextGroup parentContextGroup, HAPContext outContext, HAPConfigureContextProcessor configure, Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv) {
		HAPContext solidContext = buildSolidContext(context, constants, contextProcessorEnv);
		if(parentContextGroup!=null && !HAPConfigureContextProcessor.VALUE_INHERITMODE_NONE.equals(configure.inheritMode) && Arrays.asList(HAPContextGroup.getInheritableContextTypes()).contains(contextCategary)) {
			//process inherate first
			HAPContext parentContext = parentContextGroup.getContext(contextCategary);
			Map<String, HAPContextNodeRoot> parentEles = parentContext.getElements();
			for(String eleName : parentEles.keySet()) {
				if(HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT.equals(configure.inheritMode)) {
					//parent override child
					processInheritedElement(parentContextGroup, contextCategary, eleName, outContext, configure, constants, contextProcessorEnv);
				}
				else if(HAPConfigureContextProcessor.VALUE_INHERITMODE_CHILD.equals(configure.inheritMode)) {
					//child override parent
					if(solidContext.getElement(eleName)==null) {
						processInheritedElement(parentContextGroup, contextCategary, eleName, outContext, configure, constants, contextProcessorEnv);
					}
				}
			}
		}

		//process remaining element in child context
		Map<String, HAPContextNodeRoot> childEles = solidContext.getElements();
		for(String eleName : childEles.keySet()) {
			if(outContext.getElement(eleName)==null) {
				HAPContextNodeRoot childEle = childEles.get(eleName);
				HAPContextNodeRoot outChildEle = null;
				String type = childEle.getType();
				switch(type){
					case HAPConstant.UIRESOURCE_ROOTTYPE_CONSTANT:
					{
						outChildEle = childEle;
						break;
					}
					case HAPConstant.UIRESOURCE_ROOTTYPE_ABSOLUTE:
					{
						HAPContextNodeRootAbsolute defContextElementAbsolute = (HAPContextNodeRootAbsolute)childEle;
						HAPContextNodeRootAbsolute absEle = new HAPContextNodeRootAbsolute();
						absEle.setDefaultValue(defContextElementAbsolute.getDefaultValue());
						buildSolidContextNode(defContextElementAbsolute, absEle, constants, contextProcessorEnv);
						outChildEle = absEle;
						break;
					}
					case HAPConstant.UIRESOURCE_ROOTTYPE_RELATIVE:
					{
						outChildEle = processRelativeContextDefinitionElement(eleName, (HAPContextNodeRootRelative)childEle, parentContextGroup, configure, constants, contextProcessorEnv);
						break;
					}	
				}
				outContext.addElement(eleName, outChildEle);
			}
		}
		
	}
	
	//process element that inherited from parent
	private static void processInheritedElement(HAPContextGroup parentContextGroup, String contextCategary, String eleName, HAPContext outContext, HAPConfigureContextProcessor configure, Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv) {
		HAPContextNodeRoot outEle = null;
		HAPContextNodeRoot parentEle = parentContextGroup.getContextNode(contextCategary, eleName);
		if(HAPConstant.UIRESOURCE_ROOTTYPE_CONSTANT.equals(parentEle.getType())) {
			//for constant
			outEle = parentEle;
		}
		else {
			//for variable
			HAPContextNodeRootRelative relativeEle = new HAPContextNodeRootRelative();
			relativeEle.setPath(eleName);
			relativeEle.setParentCategary(contextCategary);
			outEle = HAPContextUtility.processRelativeContextDefinitionElement(eleName, relativeEle, parentContextGroup, configure, constants, contextProcessorEnv);
		}
		outContext.addElement(eleName, outEle);
	}
	
	
//	public static void processContextGroupDefinition(HAPContextGroup defContext, HAPContextGroup parentContext, HAPContextGroup outContext, Map<String, String> constants, HAPEnvContextProcessor contextProcessorEnv) {
//		for(String contextType : HAPContextGroup.getAllContextTypes()){
//			Map<String, HAPContextNodeRoot> defEles = defContext.getElements(contextType);
//			for(String name : defEles.keySet()){
//				String realName = getSolidName(name, constants, contextProcessorEnv);
//				outContext.addElement(realName, processContextDefinitionElement(realName, defEles.get(name), parentContext, constants, contextProcessorEnv), contextType);
//			}
//		}
//	}
//	
//	public static void processContextDefinition(HAPContext defContext, HAPContextGroup parentContext, HAPContext outContext, Map<String, String> constants, HAPEnvContextProcessor contextProcessorEnv) {
//		Map<String, HAPContextNodeRoot> defEles = defContext.getElements();
//		for(String name : defEles.keySet()){
//			String realName = getSolidName(name, constants, contextProcessorEnv);
//			outContext.addElement(realName, processContextDefinitionElement(realName, defEles.get(name), parentContext, constants, contextProcessorEnv));
//		}
//	}
//	
//	//convert context definition to context 
//	public static HAPContextNodeRoot processContextDefinitionElement(String defRootEleName, HAPContextNodeRoot defContextElement, HAPContextGroup parentContext, Map<String, String> constants, HAPEnvContextProcessor contextProcessorEnv){
//		String type = defContextElement.getType();
//		switch(type){
//			case HAPConstant.UIRESOURCE_ROOTTYPE_ABSOLUTE:
//			{
//				HAPContextNodeRootAbsolute defContextElementAbsolute = (HAPContextNodeRootAbsolute)defContextElement;
//				HAPContextNodeRootAbsolute out = new HAPContextNodeRootAbsolute();
//				out.setDefaultValue(defContextElementAbsolute.getDefaultValue());
//				buildSolidContextNode(defContextElementAbsolute, out, constants, contextProcessorEnv);
//				return out;
//			}
//			case HAPConstant.UIRESOURCE_ROOTTYPE_RELATIVE:
//			{
//				return processRelativeContextDefinitionElement(defRootEleName, (HAPContextNodeRootRelative)defContextElement, parentContext, constants, contextProcessorEnv);
//			}	
//		}
//		return null;
//	}

	private static HAPContextNodeRootRelative processRelativeContextDefinitionElement(String defRootEleName, HAPContextNodeRootRelative defContextElementRelative, HAPContextGroup parentContext, HAPConfigureContextProcessor configure, Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv){
		if(parentContext==null)  throw new RuntimeException();
		List<String> categaryes = new ArrayList<String>();
		if(defContextElementRelative.getParentCategary()!=null) categaryes.add(defContextElementRelative.getParentCategary());
		else if(configure.parentCategary==null)   categaryes.addAll(Arrays.asList(HAPContextGroup.getVisibleContextTypes()));
		else   categaryes.addAll(Arrays.asList(configure.parentCategary));
		return processRelativeContextDefinitionElement(defRootEleName, defContextElementRelative, parentContext, categaryes.toArray(new String[0]), configure.relativeResolveMode, constants, contextProcessorEnv);
	}
	
	private static HAPContextNodeRootRelative processRelativeContextDefinitionElement(String defRootEleName, HAPContextNodeRootRelative defContextElementRelative, HAPContextGroup parentContext, String[] categaryes, String mode, Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv){
		HAPContextNodeRootRelative out = new HAPContextNodeRootRelative();
		HAPContextPath path = new HAPContextPath(getSolidName(defContextElementRelative.getPathStr(), constants, contextProcessorEnv));
		out.setPath(path);
		
		HAPInfoRelativeContextResolve resolveInfo = resolveReferencedParentContextNode(path, parentContext, categaryes, mode);
		out.setParentCategary(resolveInfo.parentCategary);
		HAPContextNode parentNode = resolveInfo.resolvedNode; 
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
			//not find parent node, throw exception
			throw new RuntimeException();
		}
		return out;
	}

	private static HAPContext buildSolidContext(HAPContext context, Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv) {
		HAPContext out = new HAPContext();
		for(String name : context.getElements().keySet()) {
			String solidName = getSolidName(name, constants, contextProcessorEnv);
			out.addElement(solidName, context.getElement(name).toSolidContextNode(constants, contextProcessorEnv));
		}
		return out;
	}
	
	//evaluate embeded script expression
	public static String getSolidName(String name, Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv){
		HAPEmbededScriptExpression se = new HAPEmbededScriptExpression(name, contextProcessorEnv.expressionManager);
		HAPRuntimeTaskExecuteEmbededExpression task = new HAPRuntimeTaskExecuteEmbededExpression(se, null, constants);
		HAPServiceData serviceData = contextProcessorEnv.runtime.executeTaskSync(task);
		if(serviceData.isSuccess())   return (String)serviceData.getData();
		else return null;
	}

	//build context node with solid name
	//def : original node
	//solid : out, sold node
	public static void buildSolidContextNode(HAPContextNode def, HAPContextNode solid, Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv){
		solid.setDefinition(def.getDefinition());
		for(String name : def.getChildren().keySet()){
			String solidName = getSolidName(name, constants, contextProcessorEnv);
			HAPContextNode child = def.getChildren().get(name);
			HAPContextNode solidChild = new HAPContextNode();
			buildSolidContextNode(child, solidChild, constants, contextProcessorEnv);
			solid.addChild(solidName, solidChild);
		}
	}
	
	//go through different context group categaryes to find referenced node in parent. 
	private static HAPInfoRelativeContextResolve resolveReferencedParentContextNode(HAPContextPath path, HAPContextGroup parentContext, String[] categaryes, String mode){
		if(parentContext==null)   return null;
		
		//find candidates, path similar
		List<HAPInfoRelativeContextResolve> candidates = new ArrayList<HAPInfoRelativeContextResolve>();
		for(String contextType : HAPContextGroup.getVisibleContextTypes()){
			Object[] nodeInfo = parentContext.getContext(contextType).discoverChild(path);
			if(nodeInfo[0]!=null) {
				HAPInfoRelativeContextResolve resolved = new HAPInfoRelativeContextResolve();
				resolved.referedNode = (HAPContextNode)nodeInfo[0];
				resolved.remainPath = (String)nodeInfo[1];
				resolved.parentCategary = contextType;
				candidates.add(resolved);
			}
			if(HAPConfigureContextProcessor.VALUE_RESOLVEPARENTMODE_FIRST.equals(mode))   break;
		}

		//find best node from candidate
		//remaining path is shortest
		HAPInfoRelativeContextResolve out = null;
		int length = 99999;
		for(HAPInfoRelativeContextResolve candidate : candidates) {
			String remainingPath = candidate.remainPath;
			if(remainingPath==null) {
				out = candidate;
				break;
			}
			else {
				if(remainingPath.length()<length) {
					length = remainingPath.length();
					out = candidate;
				}
			}
		}
		
		if(out!=null) {
			if(out.remainPath==null) {
				//exactly match with path
				out.resolvedNode = out.referedNode;
			}
			else {
				//nof exactly match with path
				HAPContextNode candidateNode = out.referedNode;
				HAPContextNodeCriteria nodeCriteria = candidateNode.getDefinition();
				if(nodeCriteria!=null) {
					//data type node
					HAPDataTypeCriteria parentCriteria = HAPCriteriaUtility.getChildCriteriaByPath(nodeCriteria.getValue(), out.remainPath);
					if(parentCriteria!=null) {
						out.resolvedNode = new HAPContextNode(); 
						out.resolvedNode.setDefinition(new HAPContextNodeCriteria(parentCriteria));
					}
				}
			}
		}
		
		return out;
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

class HAPInfoRelativeContextResolve{
	//which categary parent belong to
	public String parentCategary;
	//original refered node
	public HAPContextNode referedNode;
	//unmatched path part
	public String remainPath;
	//apply unmatched path, find the resolvedNode
	public HAPContextNode resolvedNode;
}


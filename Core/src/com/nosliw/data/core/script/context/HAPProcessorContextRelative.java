package com.nosliw.data.core.script.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.value.HAPJsonDataUtility;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expression.HAPVariableInfo;

public class HAPProcessorContextRelative {

	public static HAPContextGroup process(HAPContextGroup contextGroup, HAPContextGroup parentContextGroup, HAPConfigureContextProcessor configure, HAPEnvContextProcessor contextProcessorEnv) {
		HAPContextGroup out = new HAPContextGroup(contextGroup.getInfo());
		for(String categary : HAPContextGroup.getAllContextTypes()){
			Map<String, HAPContextDefinitionRoot> eles = contextGroup.getElements(categary);
			for(String eleName : eles.keySet()) {
				HAPContextDefinitionRoot node = eles.get(eleName);

				HAPContextDefinitionRoot processedRoot = new HAPContextDefinitionRoot();
				HAPContextDefinitionElement processedEle = processRelativeInContextDefinitionElement(node.getDefinition(), parentContextGroup, configure, contextProcessorEnv);
				processedRoot.setDefinition(processedEle);
				out.addElement(eleName, processedRoot, categary);
			}
		}
		return out;
	}

	public static HAPContext process(HAPContext context, HAPContextGroup parentContextGroup, HAPConfigureContextProcessor configure, HAPEnvContextProcessor contextProcessorEnv) {
		HAPContext out = new HAPContext();
		Map<String, HAPContextDefinitionRoot> eles = context.getElements();
		for(String eleName : eles.keySet()) {
			HAPContextDefinitionRoot node = eles.get(eleName);

			HAPContextDefinitionRoot processedRoot = new HAPContextDefinitionRoot();
			HAPContextDefinitionElement processedEle = processRelativeInContextDefinitionElement(node.getDefinition(), parentContextGroup, configure, contextProcessorEnv);
			processedRoot.setDefinition(processedEle);
			out.addElement(eleName, processedRoot);
		}
		return out;
	}
	
	private static HAPContextDefinitionElement processRelativeInContextDefinitionElement(HAPContextDefinitionElement defContextElement, HAPContextGroup parentContext, HAPConfigureContextProcessor configure, HAPEnvContextProcessor contextProcessorEnv) {
		HAPContextDefinitionElement out = null;
		switch(defContextElement.getType()) {
		case HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE:
			HAPContextDefinitionLeafRelative relativeContextElement = (HAPContextDefinitionLeafRelative)defContextElement;
			if(relativeContextElement.isProcessed())   out = relativeContextElement.cloneContextDefinitionElement();
			else {
				List<String> categaryes = new ArrayList<String>();
				if(relativeContextElement.getParentCategary()!=null) categaryes.add(relativeContextElement.getParentCategary());
				else if(configure.parentCategary==null)   categaryes.addAll(Arrays.asList(HAPContextGroup.getVisibleContextTypes()));
				else   categaryes.addAll(Arrays.asList(configure.parentCategary));
				
				out = processRelativeContextDefinitionElement((HAPContextDefinitionLeafRelative)defContextElement, parentContext, categaryes.toArray(new String[0]), configure.relativeResolveMode, contextProcessorEnv); 
			}
			break;
		case HAPConstant.CONTEXT_ELEMENTTYPE_NODE:
			
			break;
		default : 
			out = defContextElement.cloneContextDefinitionElement();
		}
		return out;
	}
	
	private static HAPContextDefinitionElement processRelativeContextDefinitionElement(HAPContextDefinitionLeafRelative defContextElementRelative, HAPContextGroup parentContext, String[] categaryes, String mode, HAPEnvContextProcessor contextProcessorEnv){
		HAPContextPath path = defContextElementRelative.getPath(); 
		HAPInfoRelativeContextResolve resolveInfo = HAPUtilityContext.resolveReferencedParentContextNode(path, parentContext, categaryes, mode);
		
		if(resolveInfo==null || resolveInfo.rootNode==null) 
			throw new RuntimeException();

		switch(resolveInfo.rootNode.getDefinition().getType()) {
		case HAPConstant.CONTEXT_ELEMENTTYPE_DATA:
		case HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE:
		{
			HAPContextDefinitionLeafRelative out = (HAPContextDefinitionLeafRelative)defContextElementRelative.cloneContextDefinitionElement();
			path.getRootElementId().setCategary(resolveInfo.path.getRootElementId().getCategary());
			out.setPath(path);
			
			HAPContextDefinitionElement parentNode = resolveInfo.resolvedNode; 
			if(parentNode!=null){
				Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
				merge(parentNode, defContextElementRelative, out, matchers, null, contextProcessorEnv);
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
		case HAPConstant.CONTEXT_ELEMENTTYPE_CONSTANT:
			HAPContextDefinitionLeafConstant out = new HAPContextDefinitionLeafConstant();
			Object constantValue = ((HAPContextDefinitionLeafConstant)resolveInfo.rootNode.getDefinition()).getValue();
			out.setValue(constantValue);
			return out;
		}
		
		return null;
	}


	//merge parent context def with child context def to another context out
	//also generate matchers from parent to child
	private static void merge(HAPContextDefinitionElement parent, HAPContextDefinitionElement def, HAPContextDefinitionElement out, Map<String, HAPMatchers> matchers, String path, HAPEnvContextProcessor contextProcessorEnv){
		String parentType = parent.getType();
		switch(parentType) {
		case HAPConstant.CONTEXT_ELEMENTTYPE_DATA:
			HAPContextDefinitionLeafData dataParent = (HAPContextDefinitionLeafData)parent;
			HAPContextDefinitionLeafData dataDef = (HAPContextDefinitionLeafData)def;
			HAPContextDefinitionLeafData dataOut = (HAPContextDefinitionLeafData)out;
			HAPVariableInfo parentVarInfo = dataParent.getCriteria();
			HAPVariableInfo defVarInfo = null;
			if(dataDef!=null)  defVarInfo = dataDef.getCriteria();
			if(defVarInfo==null)   dataOut.setCriteria(parentVarInfo);
			else {
				dataOut.setCriteria(defVarInfo);
				//cal matchers
				HAPMatchers matcher = contextProcessorEnv.dataTypeHelper.convertable(parentVarInfo.getCriteria(), defVarInfo.getCriteria());
				matchers.put(path, matcher);
			}
			break;
		case HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE:
			HAPContextDefinitionLeafRelative relativeParent = (HAPContextDefinitionLeafRelative)parent;
			merge(relativeParent.getDefinition(), def, out, matchers, path, contextProcessorEnv);			
			break;
		case HAPConstant.CONTEXT_ELEMENTTYPE_NODE:
			HAPContextDefinitionNode nodeParent = (HAPContextDefinitionNode)parent;
			HAPContextDefinitionNode nodeDef = (HAPContextDefinitionNode)def;
			HAPContextDefinitionNode nodeOut = (HAPContextDefinitionNode)out;
			for(String nodeName : nodeParent.getChildren().keySet()) {
				HAPContextDefinitionElement childNodeParent = nodeParent.getChildren().get(nodeName);
				HAPContextDefinitionElement childNodeDef = null;
				if(nodeDef!=null) {
					childNodeDef = nodeDef.getChildren().get(nodeName);
				}
				switch(childNodeParent.getType()) {
				case HAPConstant.CONTEXT_ELEMENTTYPE_DATA:
				{
					HAPContextDefinitionLeafData childNodeOut = new HAPContextDefinitionLeafData();
					nodeOut.addChild(nodeName, childNodeOut);
					merge(childNodeParent, childNodeDef, childNodeOut, matchers, HAPNamingConversionUtility.cascadePath(path, nodeName), contextProcessorEnv);
					break;
				}
				case HAPConstant.CONTEXT_ELEMENTTYPE_NODE:
				{
					HAPContextDefinitionNode childNodeOut = new HAPContextDefinitionNode();
					nodeOut.addChild(nodeName, childNodeOut);
					merge(childNodeParent, childNodeDef, childNodeOut, matchers, HAPNamingConversionUtility.cascadePath(path, nodeName), contextProcessorEnv);
					break;
				}
				case HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE:
				{
					HAPContextDefinitionLeafRelative childNodeOut = new HAPContextDefinitionLeafRelative();
					nodeOut.addChild(nodeName, childNodeOut);
					merge(childNodeParent, childNodeDef, childNodeOut, matchers, HAPNamingConversionUtility.cascadePath(path, nodeName), contextProcessorEnv);
					break;
				}
				case HAPConstant.CONTEXT_ELEMENTTYPE_VALUE:
				{
					nodeOut.addChild(nodeName, childNodeParent.cloneContextDefinitionElement());
					break;
				}
				case HAPConstant.CONTEXT_ELEMENTTYPE_CONSTANT:
				{
					nodeOut.addChild(nodeName, childNodeParent.cloneContextDefinitionElement());
					break;
				}
				}
				
			}
			break;
		default:
			
			break;
		}
	}

/*	
	private static HAPContextNodeRoot processRelativeContextDefinitionElement1(HAPContextNodeRootRelative defContextElementRelative, HAPContextGroup parentContext, String[] categaryes, String mode, HAPEnvContextProcessor contextProcessorEnv){
		HAPContextPath path = defContextElementRelative.getPath(); 
		HAPInfoRelativeContextResolve resolveInfo = HAPUtilityContext.resolveReferencedParentContextNode(path, parentContext, categaryes, mode);
		
		if(resolveInfo==null || resolveInfo.rootNode==null) 
			throw new RuntimeException();

		switch(resolveInfo.rootNode.getType()) {
		case HAPConstant.UIRESOURCE_ROOTTYPE_ABSOLUTE:
		case HAPConstant.UIRESOURCE_ROOTTYPE_RELATIVE:
		{
			HAPContextNodeRootRelative out = (HAPContextNodeRootRelative)defContextElementRelative.cloneContextNodeRoot(); 
			path.getRootElementId().setCategary(resolveInfo.path.getRootElementId().getCategary());
			out.setPath(path);
			
			HAPContextNode parentNode = resolveInfo.resolvedNode; 
			if(parentNode!=null){
				Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
				merge(parentNode, defContextElementRelative, out, matchers, null, contextProcessorEnv);
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
		case HAPConstant.UIRESOURCE_ROOTTYPE_CONSTANT:
			HAPContextNodeRootConstant out = new HAPContextNodeRootConstant();
			Object constantValue = ((HAPContextNodeRootConstant)resolveInfo.rootNode).getValue();
			out.setValue(HAPJsonDataUtility.getValue(constantValue, path.getPath()));
			return out;
		}
		
		return null;
	}

	//merge parent context def with child context def to another context out
	//also generate matchers from parent to child
	private static void merge(HAPContextNode parent, HAPContextNode def, HAPContextNode out, Map<String, HAPMatchers> matchers, String path, HAPEnvContextProcessor contextProcessorEnv){
		HAPContextNodeCriteria parentDefinition = parent.getDefinition();
		if(parentDefinition!=null){
			HAPContextNodeCriteria defDefinition = null;
			if(def!=null)			defDefinition = def.getDefinition();
			if(defDefinition==null)   out.setDefinition(parentDefinition);
			else{
				out.setDefinition(defDefinition);
				//cal matchers
				HAPMatchers matcher = contextProcessorEnv.dataTypeHelper.convertable(parentDefinition.getValue(), defDefinition.getValue());
				matchers.put(path, matcher);
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
				merge(parent.getChildren().get(name), defChild, outChild, matchers, HAPNamingConversionUtility.cascadePath(path, name), contextProcessorEnv);
			}
		}
	}
*/
}

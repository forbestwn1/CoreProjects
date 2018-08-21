package com.nosliw.data.core.script.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPMatchers;

public class HAPProcessorContextRelative {

	public static HAPContextGroup processRelativeContextNode(HAPContextGroup contextGroup, HAPContextGroup parentContextGroup, HAPConfigureContextProcessor configure, HAPEnvContextProcessor contextProcessorEnv) {
		HAPContextGroup out = new HAPContextGroup(contextGroup.getInfo());
		for(String categary : HAPContextGroup.getAllContextTypes()){
			Map<String, HAPContextNodeRoot> eles = contextGroup.getElements(categary);
			for(String eleName : eles.keySet()) {
				HAPContextNodeRoot node = eles.get(eleName);
				if(node.getType().equals(HAPConstant.UIRESOURCE_ROOTTYPE_RELATIVE)) {
					out.addElement(eleName, processRelativeContextDefinitionElement(categary, eleName, (HAPContextNodeRootRelative)node, parentContextGroup, configure, contextProcessorEnv), categary);
				}
				else {
					out.addElement(eleName, node, categary);
				}
			}
		}
		return out;
	}
	
	private static HAPContextNodeRootRelative processRelativeContextDefinitionElement(String categary, String defRootEleName, HAPContextNodeRootRelative defContextElementRelative, HAPContextGroup parentContext, HAPConfigureContextProcessor configure, HAPEnvContextProcessor contextProcessorEnv){
		if(parentContext==null)  throw new RuntimeException();
		List<String> categaryes = new ArrayList<String>();
		if(defContextElementRelative.getParentCategary()!=null) categaryes.add(defContextElementRelative.getParentCategary());
		else if(configure.parentCategary==null)   categaryes.addAll(Arrays.asList(HAPContextGroup.getVisibleContextTypes()));
		else   categaryes.addAll(Arrays.asList(configure.parentCategary));
		return processRelativeContextDefinitionElement(defRootEleName, defContextElementRelative, parentContext, categaryes.toArray(new String[0]), configure.relativeResolveMode, contextProcessorEnv);
	}
	
	private static HAPContextNodeRootRelative processRelativeContextDefinitionElement(String defRootEleName, HAPContextNodeRootRelative defContextElementRelative, HAPContextGroup parentContext, String[] categaryes, String mode, HAPEnvContextProcessor contextProcessorEnv){
		HAPContextNodeRootRelative out = new HAPContextNodeRootRelative();
		HAPContextPath path = defContextElementRelative.getPath(); 
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

package com.nosliw.data.core.script.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPVariableInfo;

public class HAPUtilityContext {

	//find all data variables in context 
	public static Map<String, HAPVariableInfo> discoverDataVariablesInContext(HAPContext context){
		Map<String, HAPVariableInfo> out = new LinkedHashMap<String, HAPVariableInfo>();
		for(String rootName : context.getElements().keySet()){
			HAPContextNodeRoot node = context.getElement(rootName);
			if(node.getType().equals(HAPConstant.UIRESOURCE_ROOTTYPE_RELATIVE) || node.getType().equals(HAPConstant.UIRESOURCE_ROOTTYPE_ABSOLUTE)){
				discoverCriteriaInContextNode(rootName, (HAPContextNodeRootVariable)node, out);
			}
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
	
	//build interited node from parent
	public static HAPContextNodeRoot createInheritedElement(HAPContextNodeRoot parentNode, String contextCategary, String eleName) {
		HAPContextNodeRoot out = null;
		if(parentNode.getType().equals(HAPConstant.UIRESOURCE_ROOTTYPE_CONSTANT)) {
			out = parentNode.cloneContextNodeRoot();
		}
		else {
			HAPContextNodeRootVariable parentVarNode = (HAPContextNodeRootVariable)parentNode;
			HAPContextNodeRootRelative relativeEle = new HAPContextNodeRootRelative();
			relativeEle.setPath(contextCategary, eleName);
			parentVarNode.toContextNode(relativeEle);
			if(parentVarNode.getType().equals(HAPConstant.UIRESOURCE_ROOTTYPE_RELATIVE) && ((HAPContextNodeRootRelative)parentVarNode).isProcessed())	relativeEle.processed();
			out = relativeEle;
		}
		return out;
	}


	//build interited node from parent
	public static HAPContextNodeRoot createInheritedElement(HAPContextGroup parentContextGroup, String contextCategary, String eleName) {
		return createInheritedElement(parentContextGroup.getElement(contextCategary, eleName), contextCategary, eleName);
	}

	//go through different context group categaryes to find referenced node in parent. 
	public static HAPInfoRelativeContextResolve resolveReferencedParentContextNode(HAPContextPath contextPath, HAPContextGroup parentContext, String[] categaryes, String mode){
		if(parentContext==null)   return null;
		
		HAPContextRootNodeId refNodeId = contextPath.getRootElementId(); 
		String refPath = contextPath.getPath();
		
		//candidate categary
		List<String> categaryCandidates = new ArrayList<String>();
		if(HAPBasicUtility.isStringNotEmpty(refNodeId.getCategary()))  categaryCandidates.add(refNodeId.getCategary());  //check path first
		else if(categaryes!=null && categaryes.length>0)    categaryCandidates.addAll(Arrays.asList(categaryes));     //input
		else categaryCandidates.addAll(Arrays.asList(HAPContextGroup.getVisibleContextTypes()));               //otherwise, use visible context
		
		//find candidates, path similar
		List<HAPInfoRelativeContextResolve> candidates = new ArrayList<HAPInfoRelativeContextResolve>();
		for(String contextType : categaryCandidates){
			Object[] nodeInfo = parentContext.getContext(contextType).discoverChild(refNodeId.getName(), refPath);
			if(nodeInfo[0]!=null) {
				HAPInfoRelativeContextResolve resolved = new HAPInfoRelativeContextResolve();
				resolved.referedNode = (HAPContextNode)nodeInfo[0];
				resolved.remainPath = (String)nodeInfo[1];
				resolved.contextPath = new HAPContextPath(contextType, refNodeId.getName(), refPath);
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
			if(HAPBasicUtility.isStringEmpty(out.remainPath)) {
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
}

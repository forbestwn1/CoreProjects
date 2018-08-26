package com.nosliw.data.core.script.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public class HAPUtilityContext {

	//go through different context group categaryes to find referenced node in parent. 
	public static HAPInfoRelativeContextResolve resolveReferencedParentContextNode(HAPContextPath contextPath, HAPContextGroup parentContext, String[] categaryes, String mode){
		if(parentContext==null)   return null;
		
		HAPContextRootNodeId refNodeId = new HAPContextRootNodeId(contextPath.getRootElementName());
		String refPath = contextPath.getPath();
		
		//candidate categary
		List<String> categaryCandidates = new ArrayList<String>();
		if(HAPBasicUtility.isStringNotEmpty(refNodeId.getCategary()))  categaryCandidates.add(refNodeId.getCategary());  //check path first
		else if(categaryes!=null && categaryes.length>0)    categaryCandidates.addAll(Arrays.asList(categaryes));     //input
		else categaryCandidates.addAll(Arrays.asList(HAPContextGroup.getVisibleContextTypes()));               //otherwise, use visible context
		
		//find candidates, path similar
		List<HAPInfoRelativeContextResolve> candidates = new ArrayList<HAPInfoRelativeContextResolve>();
		for(String contextType : categaryCandidates){
			Object[] nodeInfo = parentContext.getContext(contextType).discoverChild(new HAPContextPath(refNodeId.getName(), refPath));
			if(nodeInfo[0]!=null) {
				HAPInfoRelativeContextResolve resolved = new HAPInfoRelativeContextResolve();
				resolved.referedNode = (HAPContextNode)nodeInfo[0];
				resolved.remainPath = (String)nodeInfo[1];
				resolved.parentNodeId = new HAPContextRootNodeId(contextType, refNodeId.getName());
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
}

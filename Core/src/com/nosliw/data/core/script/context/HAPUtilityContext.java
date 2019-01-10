package com.nosliw.data.core.script.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.erro.HAPErrorUtility;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPVariableInfo;

public class HAPUtilityContext {

	//build default value structure for context group
	public static JSONObject buildDefaultJsonObject(HAPContextGroup contextGroup) {
		JSONObject out = new JSONObject();
		for(String categary : contextGroup.getContextTypes()) {
			out.put(categary, buildDefaultJsonObject(contextGroup.getContext(categary)));
		}
		return out;
	}
	
	public static JSONObject buildDefaultJsonObject(HAPContext context) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		for(String contextEleName : context.getElementNames()) {
			Object value = context.getElement(contextEleName).getDefaultValue();
			if(value!=null) {
				jsonMap.put(contextEleName, value.toString());
			}
		}
		return new JSONObject(HAPJsonUtility.buildMapJson(jsonMap));
	}

	//build skeleton, it is used for data mapping operation
	public static JSONObject buildSkeletonJsonObject(HAPContext context, boolean isFlatRootName) {
		JSONObject output = new JSONObject();
		for(String contextEle : context.getElementNames()) {
			HAPContextDefinitionElement contextDefEle = context.getElement(contextEle).getDefinition();
			Object contextEleJson = buildJsonValue(contextDefEle);

			if(contextEleJson!=null) {
				JSONObject parentJsonObj = output;
				if(isFlatRootName) {
					//if flat root name, just use it
					parentJsonObj.put(contextEle, contextEleJson);
				}
				else {
					//not flat, parse categary and name from root name
					HAPContextDefinitionRootId rootId = new HAPContextDefinitionRootId(contextEle);
					String categary = rootId.getCategary();
					if(HAPBasicUtility.isStringNotEmpty(categary)) {
						parentJsonObj = output.optJSONObject(categary);
						if(parentJsonObj==null) {
							parentJsonObj = new JSONObject();
							output.put(categary, parentJsonObj);
						}
					}
					parentJsonObj.put(rootId.getName(), contextEleJson);
				}
			}
		}
		return output;
	}
	
	private static Object buildJsonValue(HAPContextDefinitionElement contextDefEle) {
		switch(contextDefEle.getType()) {
		case HAPConstant.CONTEXT_ELEMENTTYPE_CONSTANT:
		{
			HAPContextDefinitionLeafConstant constantEle = (HAPContextDefinitionLeafConstant)contextDefEle;
			return constantEle.getValue();
		}
		case HAPConstant.CONTEXT_ELEMENTTYPE_NODE:
		{
			HAPContextDefinitionNode nodeEle = (HAPContextDefinitionNode)contextDefEle;
			JSONObject out = new JSONObject();
			for(String childName : nodeEle.getChildren().keySet()) {
				Object childJsonValue = buildJsonValue(nodeEle.getChild(childName));
				if(childJsonValue!=null) {
					out.put(childName, childJsonValue);
				}
			}
			return out;
		}
		case HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE:
		{
			HAPContextDefinitionLeafRelative relativeEle = (HAPContextDefinitionLeafRelative)contextDefEle;
			return new JSONObject();
		}
		default:
			return null;
		}
	}
	
	//each relative context element represent path mapping from input context to output context during runtime
	public static Map<String, String> buildRelativePathMapping(HAPContextDefinitionRoot contextRoot, String rootName){
		return buildRelativePathMapping(contextRoot.getDefinition(), rootName);
	}
	
	public static Map<String, String> buildRelativePathMapping(HAPContextDefinitionElement contextDefEle, String rootName){
		Map<String, String> out = new LinkedHashMap<String, String>();
		processContextDefElementWithPathInfo(contextDefEle, new HAPContextDefEleProcessor() {
			@Override
			public boolean process(HAPContextDefinitionElement ele, Object value) {
				String path = (String)value;
				if(ele.getType().equals(HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE)) {
					HAPContextDefinitionLeafRelative relativeEle = (HAPContextDefinitionLeafRelative)ele;
					HAPContextPath contextPath = relativeEle.getPath();
					String from = contextPath.getFullPath();
					String to = path; 
					out.put(from, to);
					return false;
				}
				return true;
			}

			@Override
			public boolean postProcess(HAPContextDefinitionElement ele, Object value) {
				return true;
			}}, rootName);
		return out;
	}
	

	
	//traverse through all the context definition element, and process it
	public static void processContextDefElement(HAPContextDefinitionElement contextDefEle, HAPContextDefEleProcessor processor, Object value) {
		if(processor.process(contextDefEle, value)) {
			if(HAPConstant.CONTEXT_ELEMENTTYPE_NODE.equals(contextDefEle.getType())) {
				HAPContextDefinitionNode nodeEle = (HAPContextDefinitionNode)contextDefEle;
				for(String childNodeName : nodeEle.getChildren().keySet()) {
					processContextDefElement(nodeEle.getChild(childNodeName), processor, value);
				}
			}
		}
		processor.postProcess(contextDefEle, value);
	}

	public static void processContextDefElementWithPathInfo(HAPContextDefinitionElement contextDefEle, HAPContextDefEleProcessor processor, String path) {
		if(processor.process(contextDefEle, path)) {
			if(HAPConstant.CONTEXT_ELEMENTTYPE_NODE.equals(contextDefEle.getType())) {
				HAPContextDefinitionNode nodeEle = (HAPContextDefinitionNode)contextDefEle;
				for(String childNodeName : nodeEle.getChildren().keySet()) {
					processContextDefElementWithPathInfo(nodeEle.getChild(childNodeName), processor, HAPNamingConversionUtility.buildPath(path, childNodeName));
				}
			}
		}
		processor.postProcess(contextDefEle, path);
	}

	public static void processExpandedContextDefElementWithPathInfo(HAPContextDefinitionElement contextDefEle, HAPContextDefEleProcessor processor, String path) {
		HAPContextDefinitionElement solidated = contextDefEle.getSolidContextDefinitionElement();
		if(processor.process(solidated, path)) {
			if(HAPConstant.CONTEXT_ELEMENTTYPE_NODE.equals(solidated.getType())) {
				HAPContextDefinitionNode nodeEle = (HAPContextDefinitionNode)solidated;
				for(String childNodeName : nodeEle.getChildren().keySet()) {
					processContextDefElementWithPathInfo(nodeEle.getChild(childNodeName), processor, HAPNamingConversionUtility.buildPath(path, childNodeName));
				}
			}
		}
		processor.postProcess(solidated, path);
	}
	
	public static HAPContextDefinitionElement getDescendant(HAPContextDefinitionElement contextDefEle, String path) {
		HAPContextDefinitionElement out = contextDefEle;
		HAPPath pathObj = new HAPPath(path);
		for(String pathSeg : pathObj.getPathSegs()) {
			if(out!=null)			out = out.getChild(pathSeg);
		}
		return out;
	}

	public static HAPContextDefinitionElement getDescendant(HAPContext context, String path) {
		HAPContextDefinitionElement out = null;
		HAPComplexPath complexPath = new HAPComplexPath(path);
		HAPContextDefinitionRoot root = context.getElement(complexPath.getRootName());
		if(root!=null) {
			out = getDescendant(root.getDefinition(), complexPath.getPath());
		}
		return out;
	}
	
	public static HAPContextDefinitionElement getDescendant(HAPContextGroup contextGroup, String categary, String path) {
		HAPContextDefinitionElement out = null;
		HAPContext context = contextGroup.getContext(categary);
		if(context!=null)   out = getDescendant(context, path);
		return out;
	}

	public static HAPContextDefinitionElement getDescendant(HAPContextGroup contextGroup, String path) {
		HAPContextPath contextPath = new HAPContextPath(path);
		return getDescendant(contextGroup, contextPath.getRootElementId().getCategary(), contextPath.getPath());
	}
	
	public static void updateDataDescendant(HAPContextGroup contextGroup, String categary, String path, HAPContextDefinitionLeafData dataEle) {
		updateDataDescendant(contextGroup.getContext(categary), path, dataEle);
	}

	public static void updateDataDescendant(HAPContext context, String path, HAPContextDefinitionLeafData dataEle) {
		HAPComplexPath cpath = new HAPComplexPath(path);
		HAPContextDefinitionRoot root = context.getElement(cpath.getRootName());
		if(cpath.getPathSegs().length==0 && root!=null && root.getDefinition().getType().equals(HAPConstant.CONTEXT_ELEMENTTYPE_DATA)) {
			//for data root replacement, not replace whole, just replace criteria
			((HAPContextDefinitionLeafData)root.getDefinition()).setCriteria(dataEle.getCriteria());
		}
		else {
			setDescendant(context, path, dataEle);
		}
	}
	
	public static void setDescendant(HAPContextGroup contextGroup, String categary, String path, HAPContextDefinitionElement ele) {
		setDescendant(contextGroup.getContext(categary), path, ele);
	}

	public static void setDescendant(HAPContext context, String path, HAPContextDefinitionElement ele) {
		HAPComplexPath cpath = new HAPComplexPath(path);
		HAPContextDefinitionRoot root = context.getElement(cpath.getRootName());
		if(root==null) {
			root = new HAPContextDefinitionRoot();
			context.addElement(cpath.getRootName(), root);
		}
		
		if(cpath.getPathSegs().length==0) {
			if(root.getDefinition()!=null && !root.getDefinition().getType().equals(ele.getType()))  HAPErrorUtility.invalid("");  //should be same type
			root.setDefinition(ele);
		}
		else {
			String seg = cpath.getPathSegs()[0];
			HAPContextDefinitionElement parentEle = root.getDefinition();
			for(int i=0; i<cpath.getPathSegs().length-1; i++) {
				if(i==0) {
					if(parentEle==null) {
						parentEle = new HAPContextDefinitionNode();
						root.setDefinition(parentEle);
					}
				}
				String pathSeg = cpath.getPathSegs()[i]; 
				HAPContextDefinitionElement child = parentEle.getChild(pathSeg);
				if(child==null) {
					child = new HAPContextDefinitionNode();
					((HAPContextDefinitionNode)parentEle).addChild(pathSeg, child);
				}
				parentEle = child;
				seg = cpath.getPathSegs()[i+1];
			}
			if(((HAPContextDefinitionNode)parentEle).getChild(seg)!=null && !((HAPContextDefinitionNode)parentEle).getChild(seg).getType().equals(ele.getType())) 
				HAPErrorUtility.invalid("");  //should be same type
			((HAPContextDefinitionNode)parentEle).addChild(seg, ele);
		}
	}
	
	public static boolean isContextDefinitionElementConstant(HAPContextDefinitionElement ele) {   return HAPConstant.CONTEXT_ELEMENTTYPE_CONSTANT.equals(ele.getType());   }
	
	//discover all the relative elements in context def element
	public static Map<String, HAPContextDefinitionLeafRelative> isContextDefinitionElementRelative(HAPContextDefinitionElement ele) {
		Map<String, HAPContextDefinitionLeafRelative> out = new LinkedHashMap<String, HAPContextDefinitionLeafRelative>();
		discoverRelative(ele, out, null);
		return out;
	}
	
	private static void discoverRelative(HAPContextDefinitionElement ele, Map<String, HAPContextDefinitionLeafRelative> out, String path) {
		switch(ele.getType()) {
		case HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE:
			out.put(path+"", (HAPContextDefinitionLeafRelative)ele);
			break;
		case HAPConstant.CONTEXT_ELEMENTTYPE_NODE:
			HAPContextDefinitionNode nodeEle = (HAPContextDefinitionNode)ele;
			for(String subPath : nodeEle.getChildren().keySet()) {
				discoverRelative(nodeEle.getChildren().get(subPath), out, HAPNamingConversionUtility.buildPath(path, subPath));
			}
			break;
		}
	}
	
	//context root name can be like a.b.c and a.b.d
	//these two root name can be consolidated to one root name with a and child of b.c and b.d
	public static HAPContext consolidateContextRoot(HAPContext context) {
		HAPContext out = new HAPContext();
		
		for(String rootName : context.getElementNames()) {
			HAPContextDefinitionElement def = context.getElement(rootName).getDefinition();
			HAPUtilityContext.setDescendant(out, rootName, def);
		}
		return out;
	}
	
	public static String getContextGroupInheritMode(HAPInfo info) {  
		String out = HAPConfigureContextProcessor.VALUE_INHERITMODE_CHILD;
		if("false".equals(info.getValue(HAPContextGroup.INFO_INHERIT)))  out = HAPConfigureContextProcessor.VALUE_INHERITMODE_NONE;
		return out;				
	}
 
	public static void setContextGroupInheritModeNone(HAPInfo info) {		info.setValue(HAPContextGroup.INFO_INHERIT, "false");	}
	public static void setContextGroupInheritModeChild(HAPInfo info) {		info.setValue(HAPContextGroup.INFO_INHERIT, "true");	}
	
	public static boolean getContextGroupPopupMode(HAPInfo info) {  
		boolean out = true;
		if("false".equals(info.getValue(HAPContextGroup.INFO_POPUP)))  out = false;
		return out;				
	} 

	public static boolean getContextGroupEscalateMode(HAPInfo info) {  
		boolean out = false;
		if("true".equals(info.getValue(HAPContextGroup.INFO_ESCALATE)))  out = true;
		return out;				
	} 
	
	public static HAPContextFlat buildFlatContextFromContextGroup(HAPContextGroup context, Set<String> excludedInfo) {
		HAPContextFlat out = new HAPContextFlat(excludedInfo);
		
		List<String> categarys = Arrays.asList(HAPContextGroup.getContextTypesWithPriority());
		Collections.reverse(categarys);
		for(String categary : categarys) {
			Map<String, HAPContextDefinitionRoot> eles = context.getElements(categary);
			for(String name : eles.keySet()) {
				out.addElementFromContextGroup(context, categary, name);
			}
		}
		return out;
	}
	
	public static HAPContextGroup buildContextGroupFromContext(HAPContext context) {
		HAPContextGroup out = new HAPContextGroup();
		for(String rootName : context.getElementNames()) {
			HAPContextDefinitionRoot root = context.getElement(rootName);
			HAPContextDefinitionRootId rootId = new HAPContextDefinitionRootId(rootName);
			out.addElement(rootId.getName(), root, rootId.getCategary());
		}
		return out;
	}

	//find all data variables in context 
	public static Map<String, HAPVariableInfo> discoverDataVariablesInContext(HAPContext context){
		Map<String, HAPVariableInfo> out = new LinkedHashMap<String, HAPVariableInfo>();
		for(String rootName : context.getElements().keySet()){
			HAPContextDefinitionRoot node = context.getElement(rootName);
			if(!node.isConstant()){
				discoverCriteriaInContextNode(rootName, node.getDefinition(), out);
			}
		}
		return out;
	}

	//discover data type criteria defined in context node
	//the purpose is to find variables related with data type criteria
	//the data type criteria name is full name in path, for instance, a.b.c.d
	private static void discoverCriteriaInContextNode(String path, HAPContextDefinitionElement contextDefEle, Map<String, HAPVariableInfo> criterias){
		switch(contextDefEle.getType()) {
		case HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE:
			HAPContextDefinitionLeafRelative relativeEle = (HAPContextDefinitionLeafRelative)contextDefEle;
			if(relativeEle.getDefinition()!=null)		discoverCriteriaInContextNode(path, relativeEle.getSolidContextDefinitionElement(), criterias);
			break;
		case HAPConstant.CONTEXT_ELEMENTTYPE_DATA:
			HAPContextDefinitionLeafData dataEle = (HAPContextDefinitionLeafData)contextDefEle;
			criterias.put(path, dataEle.getCriteria().cloneVariableInfo());
			break;
		case HAPConstant.CONTEXT_ELEMENTTYPE_NODE:
			HAPContextDefinitionNode nodeEle = (HAPContextDefinitionNode)contextDefEle;
			for(String childName : nodeEle.getChildren().keySet()) {
				String childPath = HAPNamingConversionUtility.cascadeComponentPath(path, childName);
				discoverCriteriaInContextNode(childPath, nodeEle.getChildren().get(childName), criterias);
			}
			break;
		}
	}

	
	//build interited node from parent
	public static HAPContextDefinitionRoot createRelativeContextDefinitionRoot(HAPContextDefinitionRoot parentNode, String contextCategary, String refPath, Set<String> excludedInfo) {
		HAPContextDefinitionRoot out = null;
		
		if(parentNode.isConstant()) {
			out = parentNode.cloneContextDefinitionRoot();
		}
		else {
			out = new HAPContextDefinitionRoot();
			out.setInfo(parentNode.getInfo().cloneInfo(excludedInfo));
			HAPContextDefinitionLeafRelative relativeEle = new HAPContextDefinitionLeafRelative();
			relativeEle.setPath(contextCategary, refPath);
			if(parentNode.getDefinition().isProcessed()) {
				relativeEle.setDefinition(parentNode.getDefinition().getSolidContextDefinitionElement());
				relativeEle.processed();
			}
			out.setDefinition(relativeEle);
		}
		return out;
	}


	//build interited node from parent
	public static HAPContextDefinitionRoot createRelativeContextDefinitionRoot(HAPContextGroup parentContextGroup, String contextCategary, String refPath, Set<String> excludedInfo) {
		return createRelativeContextDefinitionRoot(parentContextGroup.getElement(contextCategary, refPath), contextCategary, refPath, excludedInfo);
	}

	//go through different context group categaryes to find referenced node in parent. 
	public static HAPInfoRelativeContextResolve resolveReferencedParentContextNode(HAPContextPath contextPath, HAPContextGroup parentContext, String[] categaryes, String mode){
		if(parentContext==null)   return null;
		
		HAPContextDefinitionRootId refNodeId = contextPath.getRootElementId(); 
		String refPath = contextPath.getSubPath();
		
		//candidate categary
		List<String> categaryCandidates = new ArrayList<String>();
		if(HAPBasicUtility.isStringNotEmpty(refNodeId.getCategary()))  categaryCandidates.add(refNodeId.getCategary());  //check path first
		else if(categaryes!=null && categaryes.length>0)    categaryCandidates.addAll(Arrays.asList(categaryes));     //input
		else categaryCandidates.addAll(Arrays.asList(HAPContextGroup.getVisibleContextTypes()));               //otherwise, use visible context
		
		//find candidates, path similar
		List<HAPInfoRelativeContextResolve> candidates = new ArrayList<HAPInfoRelativeContextResolve>();
		for(String contextType : categaryCandidates){
			HAPInfoRelativeContextResolve resolved = new HAPInfoRelativeContextResolve();
			parentContext.getContext(contextType).discoverChild(refNodeId.getName(), refPath, resolved);
			if(resolved.rootNode!=null) {
				resolved.path = new HAPContextPath(contextType, refNodeId.getName(), refPath);
				candidates.add(resolved);
				if(HAPConfigureContextProcessor.VALUE_RESOLVEPARENTMODE_FIRST.equals(mode))   break;
			}
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
		
		if(out!=null && !out.rootNode.isConstant()) {
			if(HAPBasicUtility.isStringEmpty(out.remainPath)) {
				//exactly match with path
				out.resolvedNode = out.referedNode;
			}
			else {
				//nof exactly match with path
				HAPContextDefinitionElement candidateNode = out.referedNode;
				if(HAPConstant.CONTEXT_ELEMENTTYPE_DATA.equals(candidateNode.getType())) {
					//data type node
					HAPContextDefinitionLeafData dataLeafEle = (HAPContextDefinitionLeafData)candidateNode;
					HAPDataTypeCriteria parentCriteria = HAPCriteriaUtility.getChildCriteriaByPath(dataLeafEle.getCriteria().getCriteria(), out.remainPath);
					if(parentCriteria!=null) {
						out.resolvedNode = new HAPContextDefinitionLeafData(HAPVariableInfo.buildVariableInfo(parentCriteria)); 
					}
				}
			}
		}
		return out;
	}
	
	public static HAPContextDefinitionElement mergeDataElement(HAPContextDefinitionElement original, HAPContextDefinitionElement after) {
		String eleName = "abc";
		HAPContext orgContext = new HAPContext();
		orgContext.addElement(eleName, new HAPContextDefinitionRoot(original));
		HAPContext aftContext = new HAPContext();
		aftContext.addElement(eleName, new HAPContextDefinitionRoot(after));
		
		if(after.getType().equals(HAPConstant.CONTEXT_ELEMENTTYPE_DATA)) {
			return after;
		}
		else if(after.getType().equals(HAPConstant.CONTEXT_ELEMENTTYPE_NODE)) {
			processContextDefElementWithPathInfo(after, new HAPContextDefEleProcessor() {
				@Override
				public boolean process(HAPContextDefinitionElement ele, Object value) {
					if(ele.getType().equals(HAPConstant.CONTEXT_ELEMENTTYPE_DATA)) {
						HAPUtilityContext.updateDataDescendant(orgContext, (String)value, (HAPContextDefinitionLeafData)ele);
					}
					return true;
				}

				@Override
				public boolean postProcess(HAPContextDefinitionElement ele, Object value) {
					return true;
				}
			}, "");
			return original;
		}
		else  return original;
	}
}

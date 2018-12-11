package com.nosliw.data.core.script.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.path.HAPComplexName;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPVariableInfo;

public class HAPUtilityContext {

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
	
	public static HAPContextDefinitionElement getDescendants(HAPContextDefinitionElement contextDefEle, String path) {
		HAPContextDefinitionElement out = contextDefEle;
		HAPPath pathObj = new HAPPath(path);
		for(String pathSeg : pathObj.getPathSegs()) {
			if(out!=null)			out = out.getChild(pathSeg);
		}
		return out;
	}

	public static HAPContextDefinitionElement getDescendants(HAPContext context, String path) {
		HAPContextDefinitionElement out = null;
		HAPComplexName complexName = new HAPComplexName(path);
		HAPContextDefinitionRoot root = context.getElement(complexName.getSimpleName());
		if(root!=null) {
			out = getDescendants(root.getDefinition(), complexName.getPath());
		}
		return out;
	}
	
	public static HAPContextDefinitionElement getDescendants(HAPContextGroup contextGroup, String categary, String path) {
		HAPContextDefinitionElement out = null;
		HAPContext context = contextGroup.getContext(categary);
		if(context!=null)   out = getDescendants(context, path);
		return out;
	}
	
	public static boolean isContextDefinitionElementConstant(HAPContextDefinitionElement ele) {   return HAPConstant.CONTEXT_ELEMENTTYPE_CONSTANT.equals(ele.getType());   }
	
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

	
	public static HAPEntityInfoImp toSolidEntityInfo(HAPEntityInfoImp input, Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv) {
		HAPEntityInfoImp out = new HAPEntityInfoImp();
		out.setName(input.getName());
		out.setDescription(input.getDescription());
		out.setInfo(input.getInfo().cloneInfo());
		return out;
	}

	public static void processEscalate(HAPContextGroup contextGroup, Set<String> categarys, Map<String, String> cm) {
		for(String categary : categarys) {
			HAPContext context = contextGroup.getContext(categary);

			Map<String, String> contextMapping = new LinkedHashMap<String, String>();
			contextMapping.putAll(cm);
			for(String key : context.getElementNames()) {
				if(HAPBasicUtility.isStringEmpty(contextMapping.get(key)))			contextMapping.put(key, key);
			}
			
			for(String key : context.getElementNames()) {
				HAPUtilityContext.escalate(contextGroup, key, contextMapping.get(key), categary);
			}
		}
	}
	
	//escalte context node to parent, only absolute variable
	public static void escalate(HAPContextGroup contextGroup, String nodeName, String mappedName, String categaryType) {
		HAPContextDefinitionRoot rootNode = contextGroup.getElement(categaryType, nodeName);
		if(rootNode.isAbsolute()) {
			Object[] a = escalate(mappedName, categaryType, contextGroup.getParent(), rootNode);
			contextGroup.addElement(nodeName, (HAPContextDefinitionRoot)a[0], categaryType);
		}
	}
	
	private static Object[] escalate(String name, String categaryType, HAPContextGroup contextGroup, HAPContextDefinitionRoot original) {
		
		Object[] out = new Object[2];
		HAPInfoRelativeContextResolve resolveInfo = HAPUtilityContext.resolveReferencedParentContextNode(new HAPContextPath(name), contextGroup, null, HAPConfigureContextProcessor.VALUE_RESOLVEPARENTMODE_FIRST);
		if(resolveInfo!=null) {
			//find matched one
			out[0] = HAPUtilityContext.createInheritedElement(resolveInfo.rootNode, resolveInfo.path.getRootElementId().getCategary(), resolveInfo.path.getRootElementId().getName());
			out[1] = resolveInfo.path.getRootElementId().getCategary();
			return out;
		}
		else {
			HAPContextGroup parent = contextGroup.getParent();
			boolean isEnd = false;
			if(parent==null)   isEnd = true;
			else  isEnd = !HAPUtilityContext.getContextGroupPopupMode(parent);

			//not find
			if(isEnd){
				HAPContextDefinitionRoot newRootNode = original.cloneContextDefinitionRoot();
				contextGroup.addElement(name, newRootNode, categaryType);
				return out;
			}
			else {
				Object[] a = escalate(name, categaryType, parent, original);
				
				String categary;
				HAPContextGroup group;
				if(a[0]!=null) {
					contextGroup.addElement(name, (HAPContextDefinitionRoot)a[0], (String)a[1]);
					group = contextGroup;
					categary = (String)a[1];
				}
				else {
					contextGroup.addElement(name, HAPUtilityContext.createInheritedElement(parent, categaryType, name), categaryType);
					group = contextGroup;
					categary = categaryType;
				}
				
				out[0] = HAPUtilityContext.createInheritedElement(group, categary, name);
				out[1] = categary;
				return out;
			}
		}
	}
	
	public static String getContextGroupInheritMode(HAPContextGroup contextGroup) {  
		String out = HAPConfigureContextProcessor.VALUE_INHERITMODE_CHILD;
		if("false".equals(contextGroup.getInfo().getValue(HAPContextGroup.INFO_INHERIT)))  out = HAPConfigureContextProcessor.VALUE_INHERITMODE_NONE;
		return out;				
	}
 
	
	public static boolean getContextGroupPopupMode(HAPContextGroup contextGroup) {  
		boolean out = true;
		if("false".equals(contextGroup.getInfo().getValue(HAPContextGroup.INFO_POPUP)))  out = false;
		return out;				
	} 

	public static boolean getContextGroupEscalateMode(HAPContextGroup contextGroup) {  
		boolean out = false;
		if("true".equals(contextGroup.getInfo().getValue(HAPContextGroup.INFO_ESCALATE)))  out = true;
		return out;				
	} 
	
	public static HAPContextFlat buildFlatContext(HAPContextGroup context) {
		HAPContextFlat out = new HAPContextFlat();
		for(String categary : HAPContextGroup.getContextTypesWithPriority()) {
			Map<String, HAPContextDefinitionRoot> eles = context.getElements(categary);
			for(String name : eles.keySet()) {
				String updatedName = new HAPContextDefinitionRootId(categary, name).getFullName();
				out.addElement(updatedName, eles.get(name));
				
				//
				out.addNameMapping(name, updatedName);
				
				//
				HAPContextDefinitionRoot newEle = HAPUtilityContext.createInheritedElement(eles.get(name), null, updatedName);
				out.addElement(name, newEle);
			}
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
			if(relativeEle.getDefinition()!=null)		discoverCriteriaInContextNode(path, relativeEle.getDefinition(), criterias);
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
	public static HAPContextDefinitionRoot createInheritedElement(HAPContextDefinitionRoot parentNode, String contextCategary, String eleName) {
		HAPContextDefinitionRoot out = null;
		
		if(parentNode.isConstant()) {
			out = parentNode.cloneContextDefinitionRoot();
		}
		else {
			out = new HAPContextDefinitionRoot();
			HAPContextDefinitionLeafRelative relativeEle = new HAPContextDefinitionLeafRelative();
			relativeEle.setPath(contextCategary, eleName);
			relativeEle.setDefinition(parentNode.getDefinition().cloneContextDefinitionElement());
			out.setDefinition(relativeEle);
/*			
			HAPContextNodeRootVariable parentVarNode = (HAPContextNodeRootVariable)parentNode;
			HAPContextNodeRootRelative relativeEle = new HAPContextNodeRootRelative();
			relativeEle.setPath(contextCategary, eleName);
			parentVarNode.toContextNode(relativeEle);
//			relativeEle.setInfo(parentVarNode.getInfo().clone());
			if(parentVarNode.getType().equals(HAPConstant.UIRESOURCE_ROOTTYPE_RELATIVE) && ((HAPContextNodeRootRelative)parentVarNode).isProcessed())	relativeEle.processed();
			out = relativeEle;
			*/
		}
		return out;
	}


	//build interited node from parent
	public static HAPContextDefinitionRoot createInheritedElement(HAPContextGroup parentContextGroup, String contextCategary, String eleName) {
		return createInheritedElement(parentContextGroup.getElement(contextCategary, eleName), contextCategary, eleName);
	}

	//go through different context group categaryes to find referenced node in parent. 
	public static HAPInfoRelativeContextResolve resolveReferencedParentContextNode(HAPContextPath contextPath, HAPContextGroup parentContext, String[] categaryes, String mode){
		if(parentContext==null)   return null;
		
		HAPContextDefinitionRootId refNodeId = contextPath.getRootElementId(); 
		String refPath = contextPath.getPath();
		
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
						out.resolvedNode = new HAPContextDefinitionLeafData(); 
						((HAPContextDefinitionLeafData)out.resolvedNode).setCriteria(new HAPVariableInfo(parentCriteria));
					}
				}
			}
		}
		return out;
	}
}

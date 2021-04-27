package com.nosliw.data.core.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.data.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteriaId;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.story.HAPParentContext;
import com.nosliw.data.core.structure.value.HAPContainerVariableCriteriaInfo;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinition;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinitionFlat;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinitionGroup;
import com.nosliw.data.core.structure.value.HAPContextStructureValueExecutable;
import com.nosliw.data.core.structure.value.HAPElementContextStructureValueExecutable;

public class HAPUtilityContext {

	public static Map<String, Object> discoverContantsValueFromContextStructure(HAPContextStructureValueDefinition contextStructure) {
		HAPContextStructureValueExecutable flatContext = buildFlatContextFromContextStructure(contextStructure);
		return flatContext.getConstantValue();
	}
	
	public static HAPContextStructureValueExecutable buildFlatContextFromContextStructure(HAPContextStructureValueDefinition contextStructure) {
		HAPContextStructureValueExecutable out = null;
		String type = contextStructure.getType();
		if(type.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
			out = buildFlatContextFromContextGroup((HAPContextStructureValueDefinitionGroup)contextStructure);
		}
		else if(type.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT)) {
			out = buildFlatContextFromContext((HAPContextStructureValueDefinitionFlat)contextStructure);
		}
		return out;
	}

	public static HAPContextStructureValueExecutable buildFlatContextFromContext(HAPContextStructureValueDefinitionFlat context) {
		HAPContextStructureValueExecutable out = new HAPContextStructureValueExecutable();
		for(String name : context.getElementNames()) {
			out.addElement(context.getElement(name), name);
		}
		return out;
	}
	
	public static HAPContextStructureValueExecutable buildFlatContextFromContextGroup(HAPContextStructureValueDefinitionGroup context) {
		HAPContextStructureValueExecutable out = new HAPContextStructureValueExecutable();
		
		List<String> categarys = Arrays.asList(HAPContextStructureValueDefinitionGroup.getContextTypesWithPriority());
		Collections.reverse(categarys);
		for(String categary : categarys) {
			Map<String, HAPRoot> eles = context.getElements(categary);
			for(String localName : eles.keySet()) {
				String globalName = new HAPIdContextDefinitionRoot(categary, localName).getFullName();
				Set<String> aliases = new HashSet<String>();
				aliases.add(localName);
				aliases.add(globalName);
				out.addElement(new HAPElementContextStructureValueExecutable(eles.get(localName), categary), globalName, aliases);
			}
		}
		return out;
	}
	
	//traverse through all the context definition element, and process it
	public static void processContextRootElement(HAPRoot contextRoot, String rootName, HAPProcessorContextDefinitionElement processor, Object value) {
		HAPElement processedEle = processContextDefElement(new HAPInfoElement(contextRoot.getDefinition(), new HAPReferenceElement(rootName)), processor, value);
		if(processedEle!=null)  contextRoot.setDefinition(processedEle);
	}
	
	//traverse through all the context definition element, and process it
	//if return not null, then means new context element
	private static HAPElement processContextDefElement(HAPInfoElement contextEleInfo, HAPProcessorContextDefinitionElement processor, Object value) {
		HAPElement out = null;
		HAPElement contextDefEle = contextEleInfo.getContextElement(); 
		HAPReferenceElement path = contextEleInfo.getContextPath();
		Pair<Boolean, HAPElement> processOut = processor.process(contextEleInfo, value);
		boolean going = true;
		if(processOut!=null) {
			if(processOut.getLeft()!=null)    going = processOut.getLeft();
			if(processOut.getRight()!=null) {
				contextDefEle = processOut.getRight();
				contextEleInfo.setContextElement(contextDefEle);
				out = contextDefEle;
			}
		}
		if(going) {
			if(HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE.equals(contextDefEle.getType())) {
				HAPElementNode nodeEle = (HAPElementNode)contextDefEle;
				for(String childNodeName : nodeEle.getChildren().keySet()) {
					HAPElement childProcessed = processContextDefElement(new HAPInfoElement(nodeEle.getChild(childNodeName), path.appendSegment(childNodeName)), processor, value);
					if(childProcessed!=null) {
						//replace with new element
						nodeEle.addChild(childNodeName, childProcessed);
					}
				}
			}
		}
		processor.postProcess(contextEleInfo, value);
		return out;
	}

	public static HAPContextStructureValueDefinition getReferedContext(String name, HAPParentContext parentContext, HAPContextStructureValueDefinition self) {
		if(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_SELF.equals(name))  return self;
		else return parentContext.getContext(name);
	}
	
	public static HAPElement getDescendant(HAPElement contextDefEle, String path) {
		HAPElement out = contextDefEle;
		HAPPath pathObj = new HAPPath(path);
		for(String pathSeg : pathObj.getPathSegs()) {
			if(out!=null)			out = out.getChild(pathSeg);
		}
		return out;
	}

	public static HAPElement getDescendant(HAPContextStructureValueDefinition context, HAPReferenceElement path) {
		if(context.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
			return getDescendant((HAPContextStructureValueDefinitionGroup)context, path.getFullPath());
		}
		else {
			return getDescendant((HAPContextStructureValueDefinitionFlat)context, path.getPath());
		}
	}
	
	public static HAPElement getDescendant(HAPContextStructureValueDefinitionFlat context, String path) {
		HAPElement out = null;
		HAPComplexPath complexPath = new HAPComplexPath(path);
		HAPRoot root = context.getElement(complexPath.getRootName());
		if(root!=null) {
			out = getDescendant(root.getDefinition(), complexPath.getPath());
		}
		return out;
	}
	
	public static HAPElement getDescendant(HAPContextStructureValueDefinitionGroup contextGroup, String categary, String path) {
		HAPElement out = null;
		HAPContextStructureValueDefinitionFlat context = contextGroup.getContext(categary);
		if(context!=null)   out = getDescendant(context, path);
		return out;
	}

	public static HAPElement getDescendant(HAPContextStructureValueDefinitionGroup contextGroup, String path) {
		HAPReferenceElement contextPath = new HAPReferenceElement(path);
		return getDescendant(contextGroup, contextPath.getRootStructureId().getCategary(), contextPath.getPath());
	}
	
	public static void updateDataDescendant(HAPContextStructureValueDefinitionGroup contextGroup, String categary, String path, HAPElementLeafData dataEle) {
		updateDataDescendant(contextGroup.getContext(categary), path, dataEle);
	}

	public static void updateDataDescendant(HAPContextStructureValueDefinitionFlat context, String path, HAPElementLeafData dataEle) {
		setDescendant(context, path, dataEle);
//		HAPComplexPath cpath = new HAPComplexPath(path);
//		HAPContextDefinitionRoot root = context.getElement(cpath.getRootName());
//		if(cpath.getPathSegs().length==0 && root!=null && root.getDefinition().getType().equals(HAPConstant.CONTEXT_ELEMENTTYPE_DATA)) {
//			//for data root replacement, not replace whole, just replace criteria
//			((HAPContextDefinitionLeafData)root.getDefinition()).setCriteria(dataEle.getCriteria());
//		}
//		else {
//			setDescendant(context, path, dataEle);
//		}
	}

	public static void setDescendant(HAPContextStructureValueDefinition contextStructure, HAPReferenceElement contextPath, HAPElement ele) {
		HAPRoot targetRoot = contextStructure.getElement(contextPath.getRootStructureId().getFullName(), true);

		String[] pathSegs = contextPath.getPathSegments();
		if(pathSegs.length==0) {
			if(targetRoot.getDefinition()!=null && !targetRoot.getDefinition().getType().equals(ele.getType()))  HAPErrorUtility.invalid("");  //should be same type
			targetRoot.setDefinition(ele);
		}
		else {
			String seg = pathSegs[0];
			HAPElement parentEle = targetRoot.getDefinition();
			if(parentEle==null && pathSegs.length>0) {
				parentEle = new HAPElementNode();
				targetRoot.setDefinition(parentEle);
			}
			for(int i=0; i<pathSegs.length-1; i++) {
				String pathSeg = pathSegs[i]; 
				HAPElement child = parentEle.getChild(pathSeg);
				if(child==null) {
					child = new HAPElementNode();
					((HAPElementNode)parentEle).addChild(pathSeg, child);
				}
				parentEle = child;
				seg = pathSegs[i+1];
			}
			if(((HAPElementNode)parentEle).getChild(seg)!=null && !((HAPElementNode)parentEle).getChild(seg).getType().equals(ele.getType())) 
				HAPErrorUtility.invalid("");  //should be same type
			((HAPElementNode)parentEle).addChild(seg, ele);
		}
	}
	
	public static void setDescendant(HAPContextStructureValueDefinitionGroup contextGroup, String categary, String path, HAPElement ele) {
//		setDescendant(contextGroup.getContext(categary), path, ele);
		setDescendant(contextGroup, new HAPReferenceElement(categary, path), ele);
	}

	public static void setDescendant(HAPContextStructureValueDefinitionFlat context, String path, HAPElement ele) {
		setDescendant(context, new HAPReferenceElement((String)null, path), ele);
//		HAPComplexPath cpath = new HAPComplexPath(path);
//		HAPContextDefinitionRoot targetRoot = context.getElement(cpath.getRootName());
//		if(targetRoot==null) {
//			targetRoot = new HAPContextDefinitionRoot();
//			context.addElement(cpath.getRootName(), targetRoot);
//		}
//		
//		if(cpath.getPathSegs().length==0) {
//			if(targetRoot.getDefinition()!=null && !targetRoot.getDefinition().getType().equals(ele.getType()))  HAPErrorUtility.invalid("");  //should be same type
//			targetRoot.setDefinition(ele);
//		}
//		else {
//			String seg = cpath.getPathSegs()[0];
//			HAPContextDefinitionElement parentEle = targetRoot.getDefinition();
//			for(int i=0; i<cpath.getPathSegs().length-1; i++) {
//				if(i==0) {
//					if(parentEle==null) {
//						parentEle = new HAPContextDefinitionNode();
//						targetRoot.setDefinition(parentEle);
//					}
//				}
//				String pathSeg = cpath.getPathSegs()[i]; 
//				HAPContextDefinitionElement child = parentEle.getChild(pathSeg);
//				if(child==null) {
//					child = new HAPContextDefinitionNode();
//					((HAPContextDefinitionNode)parentEle).addChild(pathSeg, child);
//				}
//				parentEle = child;
//				seg = cpath.getPathSegs()[i+1];
//			}
//			if(((HAPContextDefinitionNode)parentEle).getChild(seg)!=null && !((HAPContextDefinitionNode)parentEle).getChild(seg).getType().equals(ele.getType())) 
//				HAPErrorUtility.invalid("");  //should be same type
//			((HAPContextDefinitionNode)parentEle).addChild(seg, ele);
//		}
	}
	
	public static boolean isContextDefinitionElementConstant(HAPElement ele) {   return HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT.equals(ele.getType());   }
	
	//discover all the relative elements in context def element
	public static Map<String, HAPElementLeafRelative> isContextDefinitionElementRelative(HAPElement ele) {
		Map<String, HAPElementLeafRelative> out = new LinkedHashMap<String, HAPElementLeafRelative>();
		discoverRelative(ele, out, null);
		return out;
	}
	
	private static void discoverRelative(HAPElement ele, Map<String, HAPElementLeafRelative> out, String path) {
		switch(ele.getType()) {
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE:
			out.put(path+"", (HAPElementLeafRelative)ele);
			break;
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
			HAPElementNode nodeEle = (HAPElementNode)ele;
			for(String subPath : nodeEle.getChildren().keySet()) {
				discoverRelative(nodeEle.getChildren().get(subPath), out, HAPNamingConversionUtility.buildPath(path, subPath));
			}
			break;
		}
	}
	
	//context root name can be like a.b.c and a.b.d
	//these two root name can be consolidated to one root name with a and child of b.c and b.d
	public static HAPContextStructureValueDefinitionFlat consolidateContextRoot(HAPContextStructureValueDefinitionFlat context) {
		HAPContextStructureValueDefinitionFlat out = new HAPContextStructureValueDefinitionFlat();
		
		for(String rootName : context.getElementNames()) {
			HAPElement def = context.getElement(rootName).getDefinition();
			HAPUtilityContext.setDescendant(out, rootName, def);
		}
		return out;
	}
	
	public static String getContextGroupInheritMode(HAPInfo info) {  
		String out = HAPConstant.INHERITMODE_CHILD;
		if("false".equals(info.getValue(HAPContextStructureValueDefinitionGroup.INFO_INHERIT)))  out = HAPConstant.INHERITMODE_NONE;
		return out;				
	}
 
	public static void setContextGroupInheritModeNone(HAPInfo info) {		info.setValue(HAPContextStructureValueDefinitionGroup.INFO_INHERIT, "false");	}
	public static void setContextGroupInheritModeChild(HAPInfo info) {		info.setValue(HAPContextStructureValueDefinitionGroup.INFO_INHERIT, "true");	}
	
	public static boolean getContextGroupPopupMode(HAPInfo info) {  
		boolean out = true;
		if("false".equals(info.getValue(HAPContextStructureValueDefinitionGroup.INFO_POPUP)))  out = false;
		return out;				
	} 
 
	public static boolean getContextGroupEscalateMode(HAPInfo info) {  
		boolean out = false;
		if("true".equals(info.getValue(HAPContextStructureValueDefinitionGroup.INFO_ESCALATE)))  out = true;
		return out;				
	} 
	
	public static HAPContextStructureValueDefinitionGroup buildContextGroupFromContext(HAPContextStructureValueDefinitionFlat context) {
		HAPContextStructureValueDefinitionGroup out = new HAPContextStructureValueDefinitionGroup();
		for(String rootName : context.getElementNames()) {
			HAPRoot root = context.getElement(rootName);
			HAPIdContextDefinitionRoot rootId = new HAPIdContextDefinitionRoot(rootName);
			out.addElement(rootId.getName(), root, rootId.getCategary());
		}
		return out;
	}

	public static HAPContainerVariableCriteriaInfo discoverDataVariablesInContext(HAPContextStructureValueExecutable context) {
		HAPContainerVariableCriteriaInfo out = new HAPContainerVariableCriteriaInfo();
		Map<String, HAPInfoCriteria> dataVarsInfoByIdPath = discoverDataVariablesInContext(context.getContext());
		for(String idPath : dataVarsInfoByIdPath.keySet()) {
			HAPComplexPath path = new HAPComplexPath(idPath);
			String id = path.getRootName();
			Set<String> aliases = context.getAliasById(id);
			Set<String> aliasesPath = new HashSet<String>();
			for(String alias : aliases) {
				HAPComplexPath aliasPath = new HAPComplexPath(alias, path.getPath());
				aliasesPath.add(aliasPath.getFullName());
			}
			out.addVariableCriteriaInfo(dataVarsInfoByIdPath.get(idPath), aliasesPath);
		}
		return out;
	}
	
	//find all data variables in context 
	public static Map<String, HAPInfoCriteria> discoverDataVariablesInContext(HAPContextStructureValueDefinitionFlat context){
		Map<String, HAPInfoCriteria> out = new LinkedHashMap<String, HAPInfoCriteria>();
		for(String rootName : context.getElements().keySet()){
			HAPRoot node = context.getElement(rootName);
			if(!node.isConstant()){
				discoverDataVariableInContextNode(rootName, node.getDefinition(), out);
			}
		}
		return out;
	}

	
	//find all constants in context, including constants defined in leaf
	public static Map<String, Object> discoverConstantValue(HAPContextStructureValueDefinitionFlat context){
		Map<String, Object> out = new LinkedHashMap<String, Object>();

		for(String name : context.getElementNames()) {
			HAPRoot contextRoot = context.getElement(name);
			HAPUtilityContext.processContextRootElement(contextRoot, name, new HAPProcessorContextDefinitionElement() {
				@Override
				public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object obj) {
					if(eleInfo.getContextElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
						HAPElementLeafConstant constantEle = (HAPElementLeafConstant)eleInfo.getContextElement();
						Object value = constantEle.getDataValue();
						if(value==null)   value = constantEle.getValue();
						out.put(eleInfo.getContextPath().getContextFullPath(), value);
					}
					return null;
				}

				@Override
				public void postProcess(HAPInfoElement eleInfo, Object value) {	}
			}, null);
		}

		for(String name : context.getElementNames()) {
			HAPRoot contextRoot = context.getElement(name);
			if(contextRoot.isConstant()) {
				HAPElementLeafConstant constantRootNode = (HAPElementLeafConstant)contextRoot.getDefinition();
				Object value = constantRootNode.getDataValue();
				if(value==null)   value = constantRootNode.getValue();
				out.put(name, value);
			}
		}
		return out;
	}
	

	//discover data type criteria defined in context node
	//the purpose is to find variables related with data type criteria
	//the data type criteria name is full name in path, for instance, a.b.c.d
	private static void discoverDataVariableInContextNode(String path, HAPElement contextDefEle, Map<String, HAPInfoCriteria> criterias){
		switch(contextDefEle.getType()) {
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE:
			HAPElementLeafRelative relativeEle = (HAPElementLeafRelative)contextDefEle;
			if(relativeEle.getDefinition()!=null)		discoverDataVariableInContextNode(path, relativeEle.getSolidContextDefinitionElement(), criterias);
			break;
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
			HAPElementLeafData dataEle = (HAPElementLeafData)contextDefEle;
			HAPInfoCriteria varInfo = HAPInfoCriteria.buildCriteriaInfo(dataEle.getCriteria());
//			varInfo.setId(path);
			criterias.put(path, varInfo);
			break;
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
			HAPElementNode nodeEle = (HAPElementNode)contextDefEle;
			for(String childName : nodeEle.getChildren().keySet()) {
				String childPath = HAPNamingConversionUtility.cascadeComponentPath(path, childName);
				discoverDataVariableInContextNode(childPath, nodeEle.getChildren().get(childName), criterias);
			}
			break;
		}
	}

	public static HAPRoot createRelativeContextDefinitionRoot(HAPRoot parentNode, String parent, String contextCategary, String refPath, Set<String> excludedInfo) {
		HAPRoot out = null;
		
		if(parentNode.isConstant()) {
			out = parentNode.cloneContextDefinitionRoot();
		}
		else {
			out = new HAPRoot();
			out.setInfo(parentNode.getInfo().cloneInfo(excludedInfo));
			HAPElementLeafRelative relativeEle = new HAPElementLeafRelative();
			relativeEle.setParent(parent);
			relativeEle.setPath(contextCategary, refPath);
			if(parentNode.getDefinition().isProcessed()) {
//				relativeEle.setDefinition(parentNode.getDefinition().getSolidContextDefinitionElement());
				relativeEle.setDefinition(HAPUtilityContext.getDescendant(parentNode.getDefinition(), new HAPComplexPath(refPath).getPath()).getSolidContextDefinitionElement());
				relativeEle.processed();
			}
			out.setDefinition(relativeEle);
		}
		return out;
	}
	
	//build interited node from parent
	public static HAPRoot createRelativeContextDefinitionRoot(HAPRoot parentNode, String contextCategary, String refPath, Set<String> excludedInfo) {
		return createRelativeContextDefinitionRoot(parentNode, null, contextCategary, refPath, excludedInfo);
	}


	//build interited node from parent
	public static HAPRoot createRelativeContextDefinitionRoot(HAPContextStructureValueDefinitionGroup parentContextGroup, String contextCategary, String refPath, Set<String> excludedInfo) {
		return createRelativeContextDefinitionRoot(parentContextGroup.getElement(contextCategary, refPath), contextCategary, refPath, excludedInfo);
	}

	public static HAPInfoReferenceResolve resolveReferencedContextElement(HAPReferenceElement contextPath, HAPContextStructureValueDefinition parentContext){
		if(parentContext==null)   return null;
		HAPInfoReferenceResolve out = null;
		String contextType = parentContext.getType();
		if(contextType.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
			out = resolveReferencedContextElement(contextPath, (HAPContextStructureValueDefinitionGroup)parentContext, null, null);
		}
		else {
			out = ((HAPContextStructureValueDefinitionFlat)parentContext).discoverChild(contextPath.getRootStructureId().getName(), contextPath.getSubPath());
			//process remaining path
			out = processContextElementRefResolve(out);
		}
		
		return out;
	}
	
	//go through different context group categaryes to find referenced node in context. 
	public static HAPInfoReferenceResolve resolveReferencedContextElement(HAPReferenceElement contextPath, HAPContextStructureValueDefinitionGroup parentContext, String[] categaryes, String mode){
		if(parentContext==null)   return null;
		
		HAPIdContextDefinitionRoot refNodeId = contextPath.getRootStructureId(); 
		String refPath = contextPath.getSubPath();
		
		//candidate categary
		List<String> categaryCandidates = new ArrayList<String>();
		if(HAPBasicUtility.isStringNotEmpty(refNodeId.getCategary()))  categaryCandidates.add(refNodeId.getCategary());  //check path first
		else if(categaryes!=null && categaryes.length>0)    categaryCandidates.addAll(Arrays.asList(categaryes));     //input
		else categaryCandidates.addAll(Arrays.asList(HAPContextStructureValueDefinitionGroup.getVisibleContextTypes()));               //otherwise, use visible context
		
		//find candidates, path similar
		List<HAPInfoReferenceResolve> candidates = new ArrayList<HAPInfoReferenceResolve>();
		for(String contextType : categaryCandidates){
			HAPInfoReferenceResolve resolved = parentContext.getContext(contextType).discoverChild(refNodeId.getName(), refPath);
			if(resolved!=null&&resolved.rootNode!=null) {
				resolved.path = new HAPReferenceElement(contextType, refNodeId.getName(), refPath);
				candidates.add(resolved);
				if(HAPConstant.RESOLVEPARENTMODE_FIRST.equals(mode))   break;
			}
		}

		//find best node from candidate
		//remaining path is shortest
		HAPInfoReferenceResolve out = null;
		int length = 99999;
		for(HAPInfoReferenceResolve candidate : candidates) {
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
		
		out = processContextElementRefResolve(out);
		return out;
	}
	
	//process remain path into internal node
	private static HAPInfoReferenceResolve processContextElementRefResolve(HAPInfoReferenceResolve out) {
		if(out!=null && !out.rootNode.isConstant()) {
			if(HAPBasicUtility.isStringEmpty(out.remainPath)) {
				//exactly match with path
				out.resolvedNode = out.referedSolidNode;
			}
			else {
				//nof exactly match with path
				HAPElement candidateNode = out.referedSolidNode.getSolidContextDefinitionElement();
				if(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA.equals(candidateNode.getType())) {
					//data type node
					HAPElementLeafData dataLeafEle = (HAPElementLeafData)candidateNode;
					HAPDataTypeCriteria childCriteria = HAPCriteriaUtility.getChildCriteriaByPath(dataLeafEle.getCriteria(), out.remainPath);
					if(childCriteria!=null) {
						out.resolvedNode = new HAPElementLeafData(new HAPVariableDataInfo(childCriteria)); 
					}
					else {
//						out.resolvedNode = new HAPContextDefinitionLeafValue();
					}
				}
				else if(HAPConstantShared.CONTEXT_ELEMENTTYPE_VALUE.equals(candidateNode.getType())){
					out.resolvedNode = candidateNode;
				}
			}
		}
		return out;
	}
	
	//find exact physical node
	public static boolean isPhysicallySolved(HAPInfoReferenceResolve solve) {
		return solve!=null && (solve.resolvedNode!=null && HAPBasicUtility.isStringEmpty(solve.remainPath));
	}

	//find node
	public static boolean isLogicallySolved(HAPInfoReferenceResolve solve) {
		return solve!=null && solve.resolvedNode!=null;
	}

	public static Map<String, HAPMatchers> mergeContextRoot(HAPRoot origin, HAPRoot expect, boolean modifyStructure, HAPRuntimeEnvironment runtimeEnv) {
		Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
		
		HAPUtilityContext.processContextRootElement(expect, "", new HAPProcessorContextDefinitionElement() {
			@Override
			public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object value) {
				String path = eleInfo.getContextPath().getSubPath();
				mergeContextDefitionElement(getDescendant(origin.getDefinition(), path), eleInfo.getContextElement(), modifyStructure, matchers, path, runtimeEnv);
				return null;
			}

			@Override
			public void postProcess(HAPInfoElement eleInfo, Object value) {	}
		}, null);
		
		
//		mergeContextDefitionElement(origin.getDefinition(), expect.getDefinition(), modifyStructure, matchers, null, runtimeEnv);
		return matchers;
	}

	public static Map<String, HAPMatchers> mergeContextDefitionElement(HAPElement originDef, HAPElement expectDef, boolean modifyStructure, String path, HAPRuntimeEnvironment runtimeEnv){
		Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
		mergeContextDefitionElement(originDef, expectDef, modifyStructure, matchers, null, runtimeEnv);
		return matchers;
	}
	
	//merge origin context def with child context def to expect context out
	//also generate matchers from origin to expect
	public static void mergeContextDefitionElement(HAPElement originDef1, HAPElement expectDef1, boolean modifyStructure, Map<String, HAPMatchers> matchers, String path, HAPRuntimeEnvironment runtimeEnv){
		if(path==null)  path = "";
		//merge is about solid
		HAPElement originDef = originDef1.getSolidContextDefinitionElement();
		HAPElement expectDef = expectDef1.getSolidContextDefinitionElement();
		String type = expectDef.getType();
		
		if(originDef.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
			switch(type) {
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
			{
				HAPElementLeafConstant dataOrigin = (HAPElementLeafConstant)originDef.getSolidContextDefinitionElement();
				HAPElementLeafData dataExpect = (HAPElementLeafData)expectDef;
				//cal matchers
				HAPMatchers matcher = HAPCriteriaUtility.mergeVariableInfo(HAPInfoCriteria.buildCriteriaInfo(new HAPDataTypeCriteriaId(dataOrigin.getDataValue().getDataTypeId(), null)), dataExpect.getCriteria(), runtimeEnv.getDataTypeHelper()); 
				if(!matcher.isVoid())  matchers.put(path, matcher);
				break;
			}
			}
		}
		else if(expectDef.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {  //kkkkk
			switch(originDef.getType()) {
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
			{
				HAPElementLeafData dataOrigin = (HAPElementLeafData)originDef;
				 HAPElementLeafConstant dataExpect = (HAPElementLeafConstant)expectDef.getSolidContextDefinitionElement();
				//cal matchers
				HAPMatchers matcher = HAPCriteriaUtility.mergeVariableInfo(HAPInfoCriteria.buildCriteriaInfo(dataOrigin.getCriteria()), new HAPDataTypeCriteriaId(dataExpect.getDataValue().getDataTypeId(), null), runtimeEnv.getDataTypeHelper()); 
				if(!matcher.isVoid())  matchers.put(path, matcher);
				break;
			}
			}
		}
		else {
			if(!originDef.getType().equals(type))   HAPErrorUtility.invalid("");   //not same type, error
			switch(type) {
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
			{
				HAPElementLeafData dataOrigin = (HAPElementLeafData)originDef.getSolidContextDefinitionElement();
				HAPElementLeafData dataExpect = (HAPElementLeafData)expectDef;
				//cal matchers
				HAPMatchers matcher = HAPCriteriaUtility.mergeVariableInfo(HAPInfoCriteria.buildCriteriaInfo(dataOrigin.getCriteria()), dataExpect.getCriteria(), runtimeEnv.getDataTypeHelper()); 
				if(!matcher.isVoid())  matchers.put(path, matcher);
				break;
			}
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
			{
				HAPElementNode nodeOrigin = (HAPElementNode)originDef;
				HAPElementNode nodeExpect = (HAPElementNode)expectDef;
				for(String nodeName : nodeExpect.getChildren().keySet()) {
					HAPElement childNodeExpect = nodeExpect.getChildren().get(nodeName);
					HAPElement childNodeOrigin = nodeOrigin.getChildren().get(nodeName);
					if(childNodeOrigin!=null || modifyStructure) {
						switch(childNodeExpect.getType()) {
						case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
						{
							if(childNodeOrigin==null) {
								childNodeOrigin = new HAPElementLeafData();
								nodeOrigin.addChild(nodeName, childNodeOrigin);
							}
							mergeContextDefitionElement(childNodeOrigin, childNodeExpect, modifyStructure, matchers, HAPNamingConversionUtility.cascadePath(path, nodeName), runtimeEnv);
							break;
						}
						case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
						{
							if(childNodeOrigin==null) {
								childNodeOrigin = new HAPElementNode();
								nodeOrigin.addChild(nodeName, childNodeOrigin);
							}
							mergeContextDefitionElement(childNodeOrigin, childNodeExpect, modifyStructure, matchers, HAPNamingConversionUtility.cascadePath(path, nodeName), runtimeEnv);
							break;
						}
						default :
						{
							if(childNodeOrigin==null) {
								childNodeOrigin = childNodeExpect.cloneContextDefinitionElement();
								nodeOrigin.addChild(nodeName, childNodeOrigin);
							}
							break;
						}
					}
				}
				break;
				}
			}
			}
		}
	}

	public static HAPContextStructureValueDefinition hardMerge(HAPContextStructureValueDefinition child, HAPContextStructureValueDefinition parent) {
		if(child==null) return parent.cloneContextStructure();
		if(parent==null)  return child.cloneContextStructure();
		
		String type1 = child.getType();
		String type2 = parent.getType();
		if(!type1.equals(type2))  throw new RuntimeException();
		
		HAPContextStructureValueDefinition out = null;
		out = child.cloneContextStructure();
		out.hardMergeWith(parent);
		return out;
	}

	
	public static HAPContextStructureValueDefinitionGroup hardMerge(HAPContextStructureValueDefinitionGroup child, HAPContextStructureValueDefinitionGroup parent) {
		HAPContextStructureValueDefinitionGroup out = null;
		if(child==null) out = parent.cloneContextGroup();
		else {
			out = child.cloneContextGroup();
			out.hardMergeWith(parent);;
		}
		return out;
	}
	
}

package com.nosliw.data.core.structure;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPErrorUtility;
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
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionFlat;

public class HAPUtilityStructure {

	public static HAPElement getDescendant(HAPElement element, String path) {
		HAPElement out = element;
		HAPPath pathObj = new HAPPath(path);
		for(String pathSeg : pathObj.getPathSegments()) {
			if(out!=null)			out = out.getChild(pathSeg);
			else throw new RuntimeException();
		}
		return out;
	}

	public static HAPInfoDesendantResolve resolveDescendant(HAPElement element, String path) {
		HAPElement solvedElment = element;
		HAPPath solvedPath = new HAPPath();
		HAPPath remainingPath = new HAPPath();
		if(HAPBasicUtility.isStringNotEmpty(path)) {
			String[] pathSegs = new HAPPath(path).getPathSegments();
			for(String pathSeg : pathSegs){
				if(remainingPath.isEmpty()) {
					//solid node
					HAPElement solidEle = null;
					if(HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE.equals(solvedElment.getType())) {
						solidEle = ((HAPElementNode)solvedElment).getChildren().get(pathSeg);
					}
					if(solidEle==null) 		remainingPath = remainingPath.appendSegment(pathSeg);
					else{
						solvedElment = solidEle;
						solvedPath = solvedPath.appendSegment(pathSeg);
					}
				}
				else {
					remainingPath = remainingPath.appendSegment(pathSeg);
				}
			}
		}
		HAPInfoDesendantResolve out = new HAPInfoDesendantResolve();
		out.resolvedElement = solvedElment;
		if(remainingPath!=null)  out.remainPath = remainingPath;
		out.solvedPath = solvedPath;
		return out;
	}
	
	public static void setDescendant(HAPRoot targetRoot, HAPPath path, HAPElement ele) {
		String[] pathSegs = path.getPathSegments();
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

	//traverse through all the structure element under root, and process it
	public static void traverseElement(HAPRoot root, HAPProcessorContextDefinitionElement processor, Object value) {
		HAPElement processedEle = traverseElement(new HAPInfoElement(root.getDefinition(), new HAPComplexPath(root.getLocalId())), processor, value);
		if(processedEle!=null)  root.setDefinition(processedEle);
	}
	
	//traverse through all the context definition element, and process it
	//if return not null, then means new context element
	private static HAPElement traverseElement(HAPInfoElement elementInfo, HAPProcessorContextDefinitionElement processor, Object value) {
		HAPElement out = null;
		HAPElement element = elementInfo.getElement(); 
		HAPComplexPath path = elementInfo.getElementPath();
		Pair<Boolean, HAPElement> processOut = processor.process(elementInfo, value);
		boolean going = true;
		if(processOut!=null) {
			if(processOut.getLeft()!=null)    going = processOut.getLeft();
			if(processOut.getRight()!=null) {
				element = processOut.getRight();
				elementInfo.setElement(element);
				out = element;
			}
		}
		if(going) {
			if(HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE.equals(element.getType())) {
				HAPElementNode nodeEle = (HAPElementNode)element;
				for(String childNodeName : nodeEle.getChildren().keySet()) {
					HAPElement childProcessed = traverseElement(new HAPInfoElement(nodeEle.getChild(childNodeName), path.appendSegment(childNodeName)), processor, value);
					if(childProcessed!=null) {
						//replace with new element
						nodeEle.addChild(childNodeName, childProcessed);
					}
				}
			}
		}
		processor.postProcess(elementInfo, value);
		return out;
	}
	
	public static Map<String, HAPMatchers> mergeRoot(HAPRoot origin, HAPRoot expect, boolean modifyStructure, HAPRuntimeEnvironment runtimeEnv) {
		Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
		
		HAPUtilityStructure.traverseElement(expect, new HAPProcessorContextDefinitionElement() {
			@Override
			public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object value) {
				String path = eleInfo.getElementPath().getPathStr();
				mergeElement(getDescendant(origin.getDefinition(), path), eleInfo.getElement(), modifyStructure, matchers, path, runtimeEnv);
				return null;
			}

			@Override
			public void postProcess(HAPInfoElement eleInfo, Object value) {	}
		}, null);
		
		return matchers;
	}

	public static Map<String, HAPMatchers> mergeElement(HAPElement originDef, HAPElement expectDef, boolean modifyStructure, String path, HAPRuntimeEnvironment runtimeEnv){
		Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
		mergeElement(originDef, expectDef, modifyStructure, matchers, null, runtimeEnv);
		return matchers;
	}
	
	//merge origin context def with child context def to expect context out
	//also generate matchers from origin to expect
	public static void mergeElement(HAPElement originDef1, HAPElement expectDef1, boolean modifyStructure, Map<String, HAPMatchers> matchers, String path, HAPRuntimeEnvironment runtimeEnv){
		if(path==null)  path = "";
		//merge is about solid
		HAPElement originDef = originDef1.getSolidStructureElement();
		HAPElement expectDef = expectDef1.getSolidStructureElement();
		String type = expectDef.getType();
		
		if(originDef.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
			switch(type) {
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
			{
				HAPElementLeafConstant dataOrigin = (HAPElementLeafConstant)originDef.getSolidStructureElement();
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
				 HAPElementLeafConstant dataExpect = (HAPElementLeafConstant)expectDef.getSolidStructureElement();
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
				HAPElementLeafData dataOrigin = (HAPElementLeafData)originDef.getSolidStructureElement();
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
							mergeElement(childNodeOrigin, childNodeExpect, modifyStructure, matchers, HAPNamingConversionUtility.cascadePath(path, nodeName), runtimeEnv);
							break;
						}
						case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
						{
							if(childNodeOrigin==null) {
								childNodeOrigin = new HAPElementNode();
								nodeOrigin.addChild(nodeName, childNodeOrigin);
							}
							mergeElement(childNodeOrigin, childNodeExpect, modifyStructure, matchers, HAPNamingConversionUtility.cascadePath(path, nodeName), runtimeEnv);
							break;
						}
						default :
						{
							if(childNodeOrigin==null) {
								childNodeOrigin = childNodeExpect.cloneStructureElement();
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

	public static HAPRoot createRootWithRelativeElement(HAPRoot parentNode, String parentStructure, String elePath, Set<String> excludedInfo) {
		HAPRoot out = null;
		
		if(parentNode.isConstant()) {
			out = parentNode.cloneRoot();
		}
		else {
			out = new HAPRoot();
			out.setInfo(parentNode.getInfo().cloneInfo(excludedInfo));
			HAPElementLeafRelative relativeEle = new HAPElementLeafRelative();
			relativeEle.setParent(parentStructure);
			relativeEle.setResolvedPath(new HAPComplexPath(parentNode.getLocalId(), elePath));
			relativeEle.setReferencePath(elePath);
			if(parentNode.getDefinition().isProcessed()) {
//				relativeEle.setDefinition(parentNode.getDefinition().getSolidContextDefinitionElement());
				relativeEle.setDefinition(HAPUtilityStructure.getDescendant(parentNode.getDefinition(), elePath).getSolidStructureElement());
				relativeEle.processed();
			}
			out.setDefinition(relativeEle);
		}
		return out;
	}
	
	//build interited node from parent
	public static HAPRoot createRootWithRelativeElement(HAPRoot parentNode, String elePath, Set<String> excludedInfo) {
		return createRootWithRelativeElement(parentNode, null, elePath, excludedInfo);
	}

	//find all data variables in context 
	public static Map<String, HAPInfoCriteria> discoverDataVariablesInStructure(HAPStructureValueDefinitionFlat structure){
		Map<String, HAPInfoCriteria> out = new LinkedHashMap<String, HAPInfoCriteria>();
		for(HAPRoot root : structure.getRoots()){
			if(!root.isConstant()){
				discoverDataVariableInElement(root.getLocalId(), root.getDefinition(), out);
			}
		}
		return out;
	}

	//find all constants in structure by name, including constants defined in leaf
	public static Map<String, Object> discoverConstantValue(HAPStructure structure){
		//discover cosntant value by id
		Map<String, Object> constantsById = new LinkedHashMap<String, Object>();
		for(HAPRoot root : structure.getAllRoots()) {
			HAPUtilityStructure.traverseElement(root, new HAPProcessorContextDefinitionElement() {
				@Override
				public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object obj) {
					if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
						HAPElementLeafConstant constantEle = (HAPElementLeafConstant)eleInfo.getElement();
						Object value = constantEle.getDataValue();
						if(value==null)   value = constantEle.getValue();
						constantsById.put(eleInfo.getElementPath().getFullName(), value);
					}
					return null;
				}

				@Override
				public void postProcess(HAPInfoElement eleInfo, Object value) {	}
			}, null);
		}

		//update id with name
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		Map<String, Integer> namePriorities = new LinkedHashMap<String, Integer>();
		for(String idPath : constantsById.keySet()) {
			HAPComplexPath complexIdPath = new HAPComplexPath(idPath);
			Set<HAPInfoName> namesInfo = structure.discoverRootNameById(complexIdPath.getRootName());
			for(HAPInfoName nameInfo : namesInfo) {
				String name = nameInfo.getName();
				HAPComplexPath complexNamePath = complexIdPath.updateRootName(name);
				Integer currentPriroty = namePriorities.get(complexNamePath.getRootName());
				if(currentPriroty==null || nameInfo.getPriority()<currentPriroty) {
					namePriorities.put(name, nameInfo.getPriority());
					out.put(complexNamePath.getFullName(), constantsById.get(idPath));
				}
			}
		}
		return out;
	}	
	
	//discover data type criteria defined in context node
	//the purpose is to find variables related with data type criteria
	//the data type criteria name is full name in path, for instance, a.b.c.d
	private static void discoverDataVariableInElement(String path, HAPElement contextDefEle, Map<String, HAPInfoCriteria> criterias){
		switch(contextDefEle.getType()) {
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE:
			HAPElementLeafRelative relativeEle = (HAPElementLeafRelative)contextDefEle;
			if(relativeEle.getDefinition()!=null)		discoverDataVariableInElement(path, relativeEle.getSolidStructureElement(), criterias);
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
				discoverDataVariableInElement(childPath, nodeEle.getChildren().get(childName), criterias);
			}
			break;
		}
	}

	//discover all the relative elements in context def element
	public static Map<String, HAPElementLeafRelative> discoverRelativeElement(HAPRoot root) {
		Map<String, HAPElementLeafRelative> out = new LinkedHashMap<String, HAPElementLeafRelative>();
		
		HAPUtilityStructure.traverseElement(root, new HAPProcessorContextDefinitionElement() {
			@Override
			public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object obj) {
				if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
					out.put(eleInfo.getElementPath().getFullName(), (HAPElementLeafRelative)eleInfo.getElement());
				}
				return null;
			}

			@Override
			public void postProcess(HAPInfoElement eleInfo, Object value) {	}
		}, null);

		return out;
	}

	public static HAPInfoReferenceResolve resolveElementReference(String elementReferenceLiterate, HAPStructure parentStructure, String mode, Set<String> elementTypes){
		HAPInfoReferenceResolve resolveInfo = analyzeElementReference(elementReferenceLiterate, parentStructure, mode, elementTypes);
		resolveInfo.resolvedElement = HAPUtilityStructure.resolveElement(resolveInfo.realSolidSolved);
		return resolveInfo;
	}
	
	public static HAPInfoReferenceResolve analyzeElementReference(String elementReferenceLiterate, HAPStructure parentStructure, String mode, Set<String> elementTypes){
		HAPReferenceElement elementReference = HAPUtilityStructurePath.parseLiterateStructurePath(elementReferenceLiterate, parentStructure.getStructureType());
		return analyzeElementReference(elementReference, parentStructure, mode, elementTypes);
	}
	
	//find best resolved element from structure 
	public static HAPInfoReferenceResolve analyzeElementReference(HAPReferenceElement elementReference, HAPStructure parentStructure, String mode, Set<String> elementTypes){
		if(parentStructure==null)   return null;
		
		//find candidate from structure by root reference
		List<HAPRoot> candidatesRoot = parentStructure.resolveRoot(elementReference.getRootReference(), false);
		
		//resolve path, 
		//find all candidate
		List<HAPInfoReferenceResolve> resolveCandidates = new ArrayList<HAPInfoReferenceResolve>();
		for(HAPRoot root : candidatesRoot) {
			HAPInfoReferenceResolve resolved = new HAPInfoReferenceResolve(); 
			String path = elementReference.getPath();
			resolved.referredRoot = root;
			resolved.path = new HAPComplexPath(root.getLocalId(), path);

			resolved.realSolidSolved = HAPUtilityStructure.resolveDescendant(root.getDefinition().getSolidStructureElement(), path);
			resolved.realSolved = HAPUtilityStructure.resolveDescendant(root.getDefinition(), path);
			
			if(resolved!=null) {
				if(elementTypes==null || elementTypes.contains(resolved.realSolidSolved.resolvedElement.getType())) {
					resolveCandidates.add(resolved);
					if(HAPConstant.RESOLVEPARENTMODE_FIRST.equals(mode))   break;
				}
			}
		}
		
		//find best resolve from candidate
		//remaining path is shortest
		HAPInfoReferenceResolve out = null;
		int length = 99999;
		for(HAPInfoReferenceResolve candidate : resolveCandidates) {
			HAPPath remainingPath = candidate.realSolidSolved.remainPath;
			if(remainingPath.isEmpty()) {
				out = candidate;
				break;
			}
			else {
				if(remainingPath.getLength()<length) {
					length = remainingPath.getLength();
					out = candidate;
				}
			}
		}
		return out;
	}
	
	//resolve the remain path part
	public static HAPElement resolveElement(HAPInfoDesendantResolve resolveInfo) {
		HAPElement out = null;
		if(resolveInfo.remainPath.isEmpty()) {
			//exactly match with path
			out = resolveInfo.resolvedElement;
		}
		else {
			//nof exactly match with path
			HAPElement candidateNode = resolveInfo.resolvedElement.getSolidStructureElement();
			if(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA.equals(candidateNode.getType())) {
				//data type node
				HAPElementLeafData dataLeafEle = (HAPElementLeafData)candidateNode;
				HAPDataTypeCriteria childCriteria = HAPCriteriaUtility.getChildCriteriaByPath(dataLeafEle.getCriteria(), resolveInfo.remainPath.getPath());
				if(childCriteria!=null) {
					out = new HAPElementLeafData(new HAPVariableDataInfo(childCriteria)); 
				}
				else {
//					out.resolvedNode = new HAPContextDefinitionLeafValue();
				}
			}
			else if(HAPConstantShared.CONTEXT_ELEMENTTYPE_VALUE.equals(candidateNode.getType())){
				out = candidateNode;
			}
			else if(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT.equals(candidateNode.getType())){
				//kkkkkk
				out = candidateNode;
			}
		}
		return out;
	}
	
	//find exact physical node
	public static boolean isPhysicallySolved(HAPInfoReferenceResolve solve) {
		return solve!=null && (solve.resolvedElement!=null && solve.realSolidSolved.remainPath.isEmpty());
	}

	//find node
	public static boolean isLogicallySolved(HAPInfoReferenceResolve solve) {
		return solve!=null && solve.resolvedElement!=null;
	}


	
	public static HAPStructure getReferedStructure(String name, HAPContainerStructure parents, HAPStructure self) {
		if(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_SELF.equals(name))  return self;
		else return parents.getStructure(name);
	}

}

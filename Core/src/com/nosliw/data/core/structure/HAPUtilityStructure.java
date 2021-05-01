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
import com.nosliw.data.core.structure.value.HAPStructureValue;
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
	public static void processRootElement(HAPRoot root, HAPProcessorContextDefinitionElement processor, Object value) {
		HAPElement processedEle = processElement(new HAPInfoElement(root.getDefinition(), new HAPComplexPath(root.getLocalId())), processor, value);
		if(processedEle!=null)  root.setDefinition(processedEle);
	}
	
	//traverse through all the context definition element, and process it
	//if return not null, then means new context element
	private static HAPElement processElement(HAPInfoElement elementInfo, HAPProcessorContextDefinitionElement processor, Object value) {
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
					HAPElement childProcessed = processElement(new HAPInfoElement(nodeEle.getChild(childNodeName), path.appendSegment(childNodeName)), processor, value);
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
		
		HAPUtilityStructure.processRootElement(expect, new HAPProcessorContextDefinitionElement() {
			@Override
			public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object value) {
				String path = eleInfo.getElementPath().getPath();
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
	public static Map<String, HAPInfoCriteria> discoverDataVariablesInContext(HAPStructureValueDefinitionFlat context){
		Map<String, HAPInfoCriteria> out = new LinkedHashMap<String, HAPInfoCriteria>();
		for(String rootName : context.getRoots().keySet()){
			HAPRoot node = context.getRoot(rootName);
			if(!node.isConstant()){
				discoverDataVariableInElement(rootName, node.getDefinition(), out);
			}
		}
		return out;
	}

	//find all constants in root, including constants defined in leaf
	public static Map<String, Object> discoverConstantValue(HAPRoot root){
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		HAPUtilityStructure.processRootElement(root, new HAPProcessorContextDefinitionElement() {
			@Override
			public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object obj) {
				if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
					HAPElementLeafConstant constantEle = (HAPElementLeafConstant)eleInfo.getElement();
					Object value = constantEle.getDataValue();
					if(value==null)   value = constantEle.getValue();
					out.put(eleInfo.getElementPath().getFullName(), value);
				}
				return null;
			}

			@Override
			public void postProcess(HAPInfoElement eleInfo, Object value) {	}
		}, null);
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
		
		HAPUtilityStructure.processRootElement(root, new HAPProcessorContextDefinitionElement() {
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
	
	//find best resolved element from structure 
	public static HAPInfoReferenceResolve resolveElementReference(HAPReferenceElement elementReference, HAPStructureValue parentStructure, HAPConfigureReferenceResolve configure){
		if(parentStructure==null)   return null;
		
		//find root candidate from structure
		List<HAPRoot> candidatesRoot = parentStructure.resolveRoot(elementReference.getRootReference(), configure);
		
		//find all resolved
		List<HAPInfoReferenceResolve> resolveCandidates = new ArrayList<HAPInfoReferenceResolve>();
		for(HAPRoot root : candidatesRoot) {
			HAPInfoReferenceResolve resolved = discoverEllement(root, elementReference.getPath());
			if(resolved!=null&&resolved.referredRoot!=null) {
				resolveCandidates.add(resolved);
				if(HAPConstant.RESOLVEPARENTMODE_FIRST.equals(configure.mode))   break;
			}
		}
		
		//find best resolve from candidate
		//remaining path is shortest
		HAPInfoReferenceResolve out = null;
		int length = 99999;
		for(HAPInfoReferenceResolve candidate : resolveCandidates) {
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
		
		//process remain path into internal node
		if(out!=null && !out.referredRoot.isConstant()) {
			if(HAPBasicUtility.isStringEmpty(out.remainPath)) {
				//exactly match with path
				out.resolvedNode = out.referedRealSolidElement;
			}
			else {
				//nof exactly match with path
				HAPElement candidateNode = out.referedRealSolidElement.getSolidStructureElement();
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
	
	//resolve element from root by path
	public static HAPInfoReferenceResolve discoverEllement(HAPRoot root, String path){
		HAPInfoReferenceResolve out = new HAPInfoReferenceResolve(); 
		out.referredRoot = root;
		if(root.isConstant()) {
			out.referedRealSolidElement = null;
			out.remainPath = path;
		}
		else {
			HAPElement outSolidNodeEle = root.getDefinition().getSolidStructureElement();
			HAPElement outRefNodeEle = root.getDefinition();
			String remainingPath = null;
			if(HAPBasicUtility.isStringNotEmpty(path)) {
				String[] pathSegs = HAPNamingConversionUtility.parseComponentPaths(path);
				for(String pathSeg : pathSegs){
					if(remainingPath==null) {
						//solid node
						HAPElement solidEle = null;
						if(HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE.equals(outSolidNodeEle.getType())) {
							solidEle = ((HAPElementNode)outSolidNodeEle).getChildren().get(pathSeg);
						}
						if(solidEle==null) 		remainingPath = pathSeg;
						else{
							outSolidNodeEle = solidEle;
						}

						//real node
						HAPElement refEle = null;
						if(HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE.equals(outRefNodeEle.getType())) {
							refEle = ((HAPElementNode)outRefNodeEle).getChildren().get(pathSeg);
						}
						if(refEle!=null)  outRefNodeEle = refEle;
					}
					else {
						remainingPath = HAPNamingConversionUtility.cascadePath(remainingPath, pathSeg);
					}
				}
			}
			out.referedRealElement = outRefNodeEle;
			out.referedRealSolidElement = outSolidNodeEle;
			out.remainPath = remainingPath;
		}
		out.path = new HAPComplexPath(out.referredRoot.getLocalId(), path);
		return out;
	}

	
	
}
